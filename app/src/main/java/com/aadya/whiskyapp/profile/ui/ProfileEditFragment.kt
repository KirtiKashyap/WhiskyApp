package com.aadya.whiskyapp.profile.ui

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.*
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.profile.model.ProfileEditRequestModel
import com.aadya.whiskyapp.profile.model.ProfileResponseModel
import com.aadya.whiskyapp.profile.model.UploadRequestBody
import com.aadya.whiskyapp.profile.model.UploadResponse
import com.aadya.whiskyapp.profile.upload.*
import com.aadya.whiskyapp.profile.viewmodel.ProfileEditFactory
import com.aadya.whiskyapp.profile.viewmodel.ProfileEditViewModel
import com.aadya.whiskyapp.profile.viewmodel.ProfileFactory
import com.aadya.whiskyapp.profile.viewmodel.ProfileViewModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager
import kotlinx.android.synthetic.main.fragment_profile_edit.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception


class ProfileEditFragment : Fragment(), UploadRequestBody.UploadCallback {

    private lateinit var mBinding: FragmentProfileEditBinding
    private lateinit var mCommonUtils: CommonUtils
    private lateinit var mSessionManager: SessionManager
    private var mProfileModel : ProfileResponseModel? = null
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private lateinit var mDOBLayoutBinding: DoblayoutProfileeditBinding
    private lateinit var mPhoneLayoutBinding: PhonelayoutProfileeditBinding
    private lateinit var mEmailLayoutBinding: EmaillayoutProfileeditBinding
    private lateinit var mAddressLayoutBinding: AddresslayoutProfileeditBinding
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var mProfileEditViewModel : ProfileEditViewModel
    private lateinit var mProfileViewModel : ProfileViewModel
    private val IMAGE_CAPTURE_CODE = 1001
    private val REQUEST_CODE=1002
    private var imageUri: Uri? = null
    private val PermissionsRequestCode = 123
    private lateinit var managePermissions: ManagePermissions


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)

        return mBinding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }

    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainHeader
        mIncludedLayoutBinding.imgDrawer.setOnClickListener {
            mDrawerInterface?.setOnDrwawerClickResult()
        }
        mIncludedLayoutBinding.imgLogo.setOnClickListener {

            launchFragment(ProfileFragment.newInstance(), "ProfileFragment")
        }

        mDOBLayoutBinding = mBinding.doblayoutProfileedit
        mPhoneLayoutBinding = mBinding.phonelayoutProfileedit
        mEmailLayoutBinding = mBinding.emaillayoutProfileedit
        mAddressLayoutBinding = mBinding.addresslayoutProfileedit
    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.app_container, fragment, tag)
        ft?.addToBackStack(null)
        ft?.commit()
    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {


        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile_edit,
            container,
            false
        )

        mCommonUtils = CommonUtils
        setIncludedLayout()
        mSessionManager = SessionManager.getInstance(requireContext())!!
        mProfileModel = mSessionManager.getProfileModel()
        setUIValues()
        handleClickListner()
        mProfileEditViewModel = ViewModelProvider(this, ProfileEditFactory(activity?.application)).get(
            ProfileEditViewModel::class.java
        )
        mProfileViewModel = ViewModelProvider(this, ProfileFactory(activity?.application)).get(
            ProfileViewModel::class.java
        )
        handleObserver()

        // Initialize a list of required permissions to request runtime
        val list = listOf<String>(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        // Initialize a new instance of ManagePermissions class
        managePermissions = ManagePermissions(requireActivity(), list, PermissionsRequestCode)

        mBinding.imageViewCamera.setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)

            // Request permission
           // val permissionGranted = managePermissions.checkPermissions()
            if (managePermissions.checkPermissions()) {
                // Open the camera interface
                showPictureDialog()
            }
        }

    }
    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(requireActivity())
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select photo from gallery", "Capture photo from camera")
        pictureDialog.setItems(
            pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> openCameraInterface()
            }
        }
        pictureDialog.show()
    }

    private fun choosePhotoFromGallary() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ContextCompat.checkSelfPermission(
                            requireActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ===
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        // Permission was granted
                        openCameraInterface()
                    }
                } else {
                    // Permission was denied
                    showAlert("Camera permission was denied. Unable to take a picture.");
                }
                return
            }
        }
    }

    private fun openCameraInterface() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, R.string.take_picture)
        values.put(MediaStore.Images.Media.DESCRIPTION, R.string.take_picture_description)
        imageUri = activity?.contentResolver?.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            values
        )

        // Create camera intent
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

        // Launch intent
        startActivityForResult(intent, IMAGE_CAPTURE_CODE)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Callback from camera intent
        if (resultCode == Activity.RESULT_OK){

            if(requestCode == REQUEST_CODE){
                imageUri = data?.data

                // if want to bitmap
               // var bitmap = (mBinding.imgTop.drawable as BitmapDrawable).bitmap
                uploadImage(imageUri)
            }else{
                // Set image captured to image view
//                mBinding.imgTop?.setImageURI(imageUri)
                uploadImage(imageUri)

            }

        }
        else {
            // Failed to take picture
            showAlert("Failed to take camera picture")
        }
    }

    private fun uploadImage(imageUri: Uri?) {

        if (imageUri == null) {
            layout_root.snackbar("Select an Image First")
            return
        }
        progress_bar.visibility=View.VISIBLE
        val parcelFileDescriptor =
            requireActivity().contentResolver.openFileDescriptor(imageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(requireActivity().cacheDir, requireActivity().contentResolver.getFileName(imageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        progress_bar.progress = 0
        val body = UploadRequestBody(file, "File", this)

        MyAPI().uploadImage(
            mSessionManager.getAuthorization()!!,
            MultipartBody.Part.createFormData(
                "File",
                file.name,
                body
            ),mSessionManager.getUserDetailLoginModel()?.memberID!!
        ).enqueue(object : Callback<UploadResponse> {
            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                layout_root.snackbar(t.message!!)
                progress_bar.progress = 0
                progress_bar.visibility=View.GONE
            }

            override fun onResponse(
                call: Call<UploadResponse>,
                response: Response<UploadResponse>
            ) {
                response.body()?.let {
                    layout_root.snackbar(it.MemberID)
                    progress_bar.progress = 100
                    progress_bar.visibility=View.GONE
                    mBinding.profileImage?.setImageURI(imageUri)
                }
            }
        })

    }


    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(activity as Context)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.ok_button_title, null)

        val dialog = builder.create()
        dialog.show()
    }
    private fun handleObserver() {
        mProfileEditViewModel.getprofileUnAuthorized().observe(viewLifecycleOwner, Observer {
            val alertModel = AlertModel(
                2000,
                resources.getString(R.string.login_error),
                resources.getString(R.string.please_login),
                R.drawable.wrong_icon,
                R.color.notiFailColor
            )
            mCommonUtils.showAlert(
                alertModel.duration,
                alertModel.title,
                alertModel.message,
                alertModel.drawable,
                alertModel.color,
                requireActivity()

            )

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(requireActivity(), LandingActivity::class.java)
                startActivity(intent)
            }, 2000)

        })


        mProfileEditViewModel.getEditProfileObserver().observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            mSessionManager.setProfileModel(it)
            mProfileModel = mSessionManager.getProfileModel()
            Log.d("TAG", "UserId:" + it)
            val alertModel = AlertModel(
                2000, resources.getString(R.string.profile_update), resources.getString(
                    R.string.profile_update_successfully
                ), R.drawable.correct_icon, R.color.notiSuccessColor
            )
            mCommonUtils.showAlert(
                alertModel.duration,
                alertModel.title,
                alertModel.message,
                alertModel.drawable,
                alertModel.color,
                requireActivity()
            )
            setUIValues()
        })

        mProfileViewModel.getProfileObserver().observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            mSessionManager.setProfileModel(it)
            mProfileModel = mSessionManager.getProfileModel()
            val alertModel = AlertModel(
                2000, resources.getString(R.string.profile_update), resources.getString(
                    R.string.profile_update_successfully
                ), R.drawable.correct_icon, R.color.notiSuccessColor
            )
            mCommonUtils.showAlert(
                alertModel.duration,
                alertModel.title,
                alertModel.message,
                alertModel.drawable,
                alertModel.color,
                requireActivity()
            )

            setUIValues()
        })

        mProfileViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
            object : Observer<AlertModel?> {
                override fun onChanged(alertModel: AlertModel?) {
                    if (alertModel == null) return
                    mCommonUtils.showAlert(
                        alertModel.duration,
                        alertModel.title,
                        alertModel.message,
                        alertModel.drawable,
                        alertModel.color,
                        requireActivity()

                    )
                }
            })


        mProfileViewModel.getProgressState()?.observe(viewLifecycleOwner,
            object : Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    if (progressState === CommonUtils.ProgressDialog.showDialog)
                        mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), requireContext()
                        )
                    else if (progressState === CommonUtils.ProgressDialog.dismissDialog)
                        mCommonUtils.dismissProgress()
                }
            })

        mProfileEditViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
            object : Observer<AlertModel?> {
                override fun onChanged(alertModel: AlertModel?) {
                    if (alertModel == null) return
                    mCommonUtils.showAlert(
                        alertModel.duration,
                        alertModel.title,
                        alertModel.message,
                        alertModel.drawable,
                        alertModel.color,
                        requireActivity()

                    )
                }
            })


        mProfileEditViewModel.getProgressState()?.observe(viewLifecycleOwner,
            object : Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    if (progressState === CommonUtils.ProgressDialog.showDialog)
                        mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), requireContext()
                        )
                    else if (progressState === CommonUtils.ProgressDialog.dismissDialog)
                        mCommonUtils.dismissProgress()
                }
            })
    }


    private fun handleClickListner() {
        mBinding.submitButton.setOnClickListener {
            if ( !CommonUtils.isValidEmail(mEmailLayoutBinding.tvUserEmail.text.toString())) {
                val alertModel = AlertModel(
                    2000, resources.getString(R.string.profile_update), resources.getString(
                        R.string.Please_Enter_Valid_Email_Id
                    ), R.drawable.wrong_icon, R.color.notiFailColor
                )

                mCommonUtils.showAlert(
                    alertModel.duration,
                    alertModel.title,
                    alertModel.message,
                    alertModel.drawable,
                    alertModel.color,
                    requireActivity()

                )
            }
            else {

                var mProfileRequestModel = ProfileEditRequestModel(
                    MemberID = mSessionManager.getUserDetailLoginModel()?.memberID,
                    firstName = "",
                    lastName = "",
                    middleName = "",
                    phoneNo = mPhoneLayoutBinding.tvUserPhone.text.toString(),
                    email = mEmailLayoutBinding.tvUserEmail.text.toString(),
                    dateOfBirth = mCommonUtils.date_dd_MM_yyyy(
                        mDOBLayoutBinding.tvUserDob.text.toString()
                    ),
                    address = mAddressLayoutBinding.tvUserAdress.text.toString(),
                    Occupation = "",
                    SpouseName = "",
                    TypeofMembership = "",
                    AliasID = "",
                    FavoriteCocktail = ""
                )
                mSessionManager.getAuthorization()?.let { it1 ->
                    mProfileEditViewModel.editProfile(
                        it1,
                        mProfileRequestModel
                    )
                }
            }
        }
    }


    private fun setUIValues() {

        if(mProfileModel?.dateOfBirth != null)
        mDOBLayoutBinding.tvUserDob.setText(mCommonUtils.date_dd_MMM_yyyy(mProfileModel?.dateOfBirth))
        mPhoneLayoutBinding.tvUserPhone.setText(mProfileModel?.phoneNumber)
        mEmailLayoutBinding.tvUserEmail.setText(mProfileModel?.email)
        mAddressLayoutBinding.tvUserAdress.setText(mProfileModel?.address)
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileEditFragment().apply {

            }
    }

    override fun onProgressUpdate(percentage: Int) {
        try {
            progress_bar.progress = percentage
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}


class ManagePermissions(val activity: Activity, val list: List<String>, val code: Int) {
    var isPermissionGrant=false
    // Check permissions at runtime
    fun checkPermissions() : Boolean{

        if (isPermissionsGranted() != PackageManager.PERMISSION_GRANTED) {
            showAlert()
        } else {
            //activity.toast("Permissions already granted.")
            isPermissionGrant=true
        }
        return isPermissionGrant
    }


    // Check permissions status
    private fun isPermissionsGranted(): Int {
        // PERMISSION_GRANTED : Constant Value: 0
        // PERMISSION_DENIED : Constant Value: -1
        var counter = 0;
        for (permission in list) {
            counter += ContextCompat.checkSelfPermission(activity, permission)
        }
        return counter
    }


    // Find the first denied permission
    private fun deniedPermission(): String {
        for (permission in list) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_DENIED) return permission
        }
        return ""
    }


    // Show alert dialog to request permissions
    private fun showAlert() {
        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Need permission(s)")
        builder.setMessage("Some permissions are required to do the task.")
        builder.setPositiveButton("OK", { dialog, which -> requestPermissions() })
        builder.setNeutralButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }


    // Request the permissions at run time
    private fun requestPermissions() {
        val permission = deniedPermission()
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
            // Show an explanation asynchronously
           // activity.toast("Should show an explanation.")
        } else {
            ActivityCompat.requestPermissions(activity, list.toTypedArray(), code)
        }
        isPermissionGrant=true

    }


    // Process permissions result
    fun processPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        var result = 0
        if (grantResults.isNotEmpty()) {
            for (item in grantResults) {
                result += item
            }
        }
        if (result == PackageManager.PERMISSION_GRANTED) return true
        return false
    }
}