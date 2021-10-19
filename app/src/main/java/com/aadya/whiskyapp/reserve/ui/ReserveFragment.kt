package com.aadya.whiskyapp.reserve.ui

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentReserveBinding
import com.aadya.whiskyapp.databinding.MainHeaderNewBinding
import com.aadya.whiskyapp.events.ui.RSVPAcknowledgeFragment
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.profile.ui.ProfileFragment
import com.aadya.whiskyapp.reserve.viewmodel.ReserveFactory
import com.aadya.whiskyapp.reserve.viewmodel.ReserveViewModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ReserveFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var mBinding: FragmentReserveBinding
    private lateinit var mIncludedLayoutBinding: MainHeaderNewBinding
    private var mDrawerInterface: DrawerInterface? = null
    val calendar = Calendar.getInstance()
    private lateinit var noofpeopleAdapter: ArrayAdapter<String>
    private lateinit var noOfPeopleList: ArrayList<String>
    private var selected_no_of_people : String = ""
    private lateinit var  reserveViewModel: ReserveViewModel
    private lateinit var  mCommonUtils : CommonUtils
    private lateinit var mSessionManager: SessionManager
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mDrawerInterface = context as DrawerInterface
    }

    var date =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

    private fun updateLabel() {
        val myFormat = "MM/dd/yy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        mBinding.edDate.setText(sdf.format(calendar.getTime()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleObserver()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        intializeMembers(inflater, container)
        handleClickListner()

        return mBinding.root
    }

    private fun handleObserver() {

        reserveViewModel = ViewModelProvider(this, ReserveFactory(activity?.application)).get(
            ReserveViewModel::class.java
        )

        reserveViewModel.getReserveViewState()?.observe(
            this,
            androidx.lifecycle.Observer {
                if (it == null)
                    return@Observer
                resetUI()
                var msg =
                    "Your request for reservation has been sent for approval, You would confirmed by the Admin soon."
                launchFragment(
                    RSVPAcknowledgeFragment.newInstance(msg, ""),
                    "RSVPAcknowledgeFragment"
                )
            })

        reserveViewModel.getreserveUnAuthorized()?.observe(
            this,
            androidx.lifecycle.Observer {
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

                Handler().postDelayed({
                    val intent = Intent(requireActivity(), LandingActivity::class.java)
                    startActivity(intent)
                }, 2000)
            })

        reserveViewModel.getAlertViewState()?.observe(this,
            object : androidx.lifecycle.Observer<AlertModel?> {
                override fun onChanged(alertModel: AlertModel?) {

                    Log.d("TAG", "In Observer alert")
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


        reserveViewModel.getProgressState()?.observe(this,
            object : androidx.lifecycle.Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    Log.d("TAG", "In Observer progress")
                    if (progressState === CommonUtils.ProgressDialog.showDialog)
                        mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), requireContext()
                        )
                    else if (progressState === CommonUtils.ProgressDialog.dismissDialog)
                        mCommonUtils.dismissProgress()
                }
            })



    }

    private fun resetUI() {
        mBinding.edWhatUEat.text = null
        mBinding.edDate.text = null
        mBinding.edTime.text = null
        mBinding.spinnerNoOfPeople.setSelection(0)
        selected_no_of_people = "0"

    }

    private fun handleClickListner() {
        mBinding.reserveButton.setOnClickListener{


            reserveViewModel.checkReserveValidation(
                requireContext(),
                mBinding.edWhatUEat.text.toString(),
                mBinding.edDate.text.toString(),
                mBinding.edTime.text.toString(),
                selected_no_of_people, mSessionManager.getUserDetailLoginModel()?.memberID,mSessionManager?.getAuthorization()
            )

        }

        mBinding.edDate.setOnClickListener {
            mCommonUtils.hideKeyboard(requireContext())
            val mDateTimePickerDialogue: DatePickerDialog
            mDateTimePickerDialogue =
            DatePickerDialog(
                requireContext(), R.style.TimePickerTheme, date, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            mDateTimePickerDialogue.show()

            mDateTimePickerDialogue.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                resources.getColor(
                    R.color.notiFailColor
                )
            )
            mDateTimePickerDialogue.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
                resources.getColor(
                    R.color.notiSuccessColor
                )
            )
        }

        mBinding.edTime.setOnClickListener {
            openTimeFragment()

        }


    }

    fun openTimeFragment() {
        val mTimePicker: TimePickerDialog
        val c = Calendar.getInstance()
        val hour = c[Calendar.HOUR_OF_DAY]
        val minute = c[Calendar.MINUTE]
        mTimePicker = TimePickerDialog(
            requireContext(), R.style.TimePickerTheme,
            { timePicker, selectedHour, selectedMinute ->
                val time = "$selectedHour:$selectedMinute"
                val fmt = SimpleDateFormat("HH:mm")
                var date: Date? = null
                try {
                    date = fmt.parse(time)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                val fmtOut = SimpleDateFormat("hh:mm aa")
                val formattedTime = fmtOut.format(date)
                mBinding.edTime.setText(formattedTime)
            }, hour, minute, false
        )
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()

        mTimePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
            resources.getColor(
                R.color.notiFailColor
            )
        )
        mTimePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(
            resources.getColor(
                R.color.notiSuccessColor
            ))
    }


    private fun convert12hrformat_24hrformat(inTime: String): String {
        val displayFormat = SimpleDateFormat("HH:mm")
        val parseFormat = SimpleDateFormat("hh:mm a")
        val date = parseFormat.parse(inTime)
        println(parseFormat.format(date).toString() + " = " + displayFormat.format(date))
        return displayFormat.format(date)
    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {
        mSessionManager = SessionManager.getInstance(context)!!
        mCommonUtils = CommonUtils
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_reserve,
            container,
            false
        )

        noOfPeopleList = ArrayList<String>()
        noOfPeopleList.add(resources.getString(R.string.no_of_persons))
        noOfPeopleList.add("1")
        noOfPeopleList.add("2")
        noOfPeopleList.add("3")
        noOfPeopleList.add("4")

        noofpeopleAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.row_spinner,
            noOfPeopleList
        )

        noofpeopleAdapter.setDropDownViewResource(R.layout.row_spinner_dialog)

        mBinding.spinnerNoOfPeople.adapter = noofpeopleAdapter
        mBinding.spinnerNoOfPeople.onItemSelectedListener = this
        setIncludedLayout()



    }
    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainheader
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
            ReserveFragment().apply {
            }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position != 0)
        selected_no_of_people = noOfPeopleList.get(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        selected_no_of_people = "0"
    }
}