package com.aadya.whiskyapp.profile.ui

import android.content.Context
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.transition.TransitionInflater
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.*
import com.aadya.whiskyapp.events.ui.EventAttendingFragment
import com.aadya.whiskyapp.profile.model.ProfileResponseModel
import com.aadya.whiskyapp.purchasehistory.ui.PurchaseHistoryFragment
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager
import com.bumptech.glide.Glide

class ProfileDetailFragment : Fragment() {


    private lateinit var mBinding: FragmentProfileDetailNewBinding
    private lateinit var mCommonUtils: CommonUtils
    private lateinit var mSessionManager: SessionManager
    private var mProfileModel : ProfileResponseModel? = null
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private lateinit var mDOBLayoutBinding: DoblayoutBinding
    private lateinit var mPhoneLayoutBinding: PhonelayoutBinding
    private lateinit var mEmailLayoutBinding: EmaillayoutBinding
    private lateinit var mAddressLayoutBinding: AddresslayoutBinding
    private var mDrawerInterface: DrawerInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(R.transition.fade)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        setUIValues()


        return mBinding.root
    }

    private fun setUIValues() {
        mDOBLayoutBinding = mBinding.doblayout
        if(!mProfileModel?.photograph.isNullOrEmpty()){
            context?.let {
                Glide.with(it)
                    .load(CommonUtils.APIURL.Profile_IMAGE_URL+mProfileModel?.photograph)
                    .into(mBinding.profileImage)
            }

        }
        val textPaint = mDOBLayoutBinding.tvUserDob.paint
        val width = textPaint.measureText(mBinding.tvAgentid.text.toString())
        val textShader: Shader = LinearGradient(0f, 0f, width, mBinding.tvAgentid.textSize, intArrayOf(
            Color.parseColor("#F77F00"),
            Color.parseColor("#9D0208")
        ), null, Shader.TileMode.REPEAT)

        if(mProfileModel?.dateOfBirth != null)

        mDOBLayoutBinding.tvUserDob.text = mCommonUtils.date_dd_MMM_yyyy(mProfileModel?.dateOfBirth)
        mPhoneLayoutBinding = mBinding.phonelayout
        mPhoneLayoutBinding.tvUserPhone.text = mProfileModel?.phoneNumber
        mEmailLayoutBinding = mBinding.emaillayout
        mEmailLayoutBinding.tvUserEmail.text = mProfileModel?.email
        mAddressLayoutBinding = mBinding.addresslayout
        mAddressLayoutBinding.tvUserAdress.text = mProfileModel?.address
        val agentPaint = mBinding.tvAgentid.paint
        val agentWidth = agentPaint.measureText(mBinding.tvAgentid.text.toString())
        val agentTextShader: Shader = LinearGradient(0f, 0f, agentWidth, mBinding.tvAgentid.textSize, intArrayOf(
            Color.parseColor("#F97C3C"),
            Color.parseColor("#0A8967"),
            Color.parseColor("#FDB54E")

        ), null, Shader.TileMode.REPEAT)

        mBinding.tvAgentid.paint.shader = agentTextShader

        mBinding.tvAgentid.text="Special Agent "+mProfileModel?.agentID
        mDOBLayoutBinding.tvUserDob.paint.shader = textShader
        mPhoneLayoutBinding.tvUserPhone.paint.shader=textShader
        mEmailLayoutBinding.tvUserEmail.paint.shader=textShader
        mAddressLayoutBinding.tvUserAdress.paint.shader=textShader
        mDOBLayoutBinding.imgProfileedit.setOnClickListener {
            launchFragment(ProfileEditFragment.newInstance(), "ProfileEditFragment")
        }
        mAddressLayoutBinding.imgEventattendingIcon.setOnClickListener {
            launchFragment(EventAttendingFragment.newInstance(), "EventAttendingFragment")
        }

        mPhoneLayoutBinding.imgPurchasehistoryIcon.setOnClickListener {
            launchFragment(PurchaseHistoryFragment.newInstance(), "PurchaseHistoryFragment")
        }
    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {


        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile_detail_new,
            container,
            false
        )

        mCommonUtils = CommonUtils
        setIncludedLayout()
        mSessionManager = SessionManager.getInstance(requireContext())!!
        mProfileModel = mSessionManager.getProfileModel()


    }



    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainHeader
        mIncludedLayoutBinding.imgDrawer.setOnClickListener {
            mDrawerInterface?.setOnDrwawerClickResult()
        }
        mIncludedLayoutBinding.imgLogo.setOnClickListener {

            launchFragment(ProfileFragment.newInstance(), "ProfileFragment")
        }

    }

    private fun launchFragment(fragment: Fragment, tag: String) {
        val ft = activity?.supportFragmentManager?.beginTransaction()
        ft?.replace(R.id.app_container, fragment, tag)
        ft?.addToBackStack(null)
        ft?.commit()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileDetailFragment().apply {

            }
    }
}