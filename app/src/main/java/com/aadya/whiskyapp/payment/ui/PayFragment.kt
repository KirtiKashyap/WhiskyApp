package com.aadya.whiskyapp.payment.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.databinding.FragmentPayBinding
import com.aadya.whiskyapp.databinding.MainHeaderBinding
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.payment.model.PaymentByEmail
import com.aadya.whiskyapp.payment.model.PaymentUpdate
import com.aadya.whiskyapp.payment.viewmodel.PaymentFactory
import com.aadya.whiskyapp.payment.viewmodel.PaymentUpdateViewModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.DrawerInterface
import com.aadya.whiskyapp.utils.SessionManager
import com.google.gson.GsonBuilder
import java.text.SimpleDateFormat
import java.util.*

const val AMOUNT = "amount"
const val ITEM_TYPE = "itemType"
const val ITEM_ID = "itemId"
const val MEMBER_ID = "memberId"

class PayFragment : Fragment() {
    private var paramAmount: String? = null
    private var paramItemType: String? = null
    private var paramItemId: Int? = null
    private var paramMemberId: Int? = null

    private lateinit var mBinding: FragmentPayBinding
    private lateinit var mIncludedLayoutBinding: MainHeaderBinding
    private var mDrawerInterface: DrawerInterface? = null
    private lateinit var mCommonUtils: CommonUtils
    private lateinit var mSessionManager: SessionManager
    private lateinit var mPaymentUpdateViewModel: PaymentUpdateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramAmount = it.getString(AMOUNT)
            paramItemType = it.getString(ITEM_TYPE)
            paramItemId = it.getInt(ITEM_ID)
            paramMemberId = it.getInt(MEMBER_ID)
        }
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
        return mBinding.root
    }

    private fun intializeMembers(inflater: LayoutInflater, container: ViewGroup?) {
        mBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pay,
            container,
            false
        )

        mCommonUtils = CommonUtils
        setIncludedLayout()
        mPaymentUpdateViewModel = ViewModelProvider(this, PaymentFactory(activity?.application)).get(
            PaymentUpdateViewModel::class.java
        )
        mBinding.amountTextView.text=paramAmount

        // Create a PaymentIntent by calling the server's endpoint.
        val amountTemp: String = paramAmount!!.replace("$", "")
        val amount = amountTemp.split("\\.".toRegex()).toTypedArray()[0]
        mBinding.mainHeader.imgBack.visibility=View.VISIBLE
        mBinding.mainHeader.imgLogo.visibility = View.GONE
        mBinding.mainHeader.imgDrawer.visibility = View.GONE


        mBinding.mainHeader.imgBack.setOnClickListener { activity?.supportFragmentManager?.popBackStack() }
        mBinding.checkOutButton.setOnClickListener {

            val mPaymentByEmail= PaymentByEmail(amount, "usd", "kashyap.kirti11@gmail.com")
            mSessionManager.getAuthorization()?.let { it1 ->
                mPaymentUpdateViewModel.paymentByEmail(
                    it1,
                    mPaymentByEmail
                )
            }

        }
        mBinding.cancelButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        mSessionManager = SessionManager.getInstance(requireContext())!!
        handleObserver()
    }

    private fun handleObserver() {

        mPaymentUpdateViewModel.getPaymentByEmailObserver().observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            val splitStatusAndSecret = it.clientSecret?.split(":")?.toTypedArray()
            val gson = GsonBuilder().setPrettyPrinting().create()
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("MM/dd/yyyy")
            val date = dateFormat.format(calendar.getTime())
           // val paymentStatus = "Success"
            mSessionManager.getAuthorization()?.let { it1 ->
                mPaymentUpdateViewModel.getPaymentUpdate(
                    it1,
                    PaymentUpdate(
                        0,
                        splitStatusAndSecret?.get(1)!!,
                        paramItemType!!,
                        paramItemId!!,
                        paramMemberId!!,
                        date,
                        splitStatusAndSecret[0]!!,
                        paramAmount!!
                        //amount.replace("$", "")
                    )
                )
            }

        })
 mPaymentUpdateViewModel.getPaymentUpdateObserver().observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
     activity?.supportFragmentManager?.popBackStack()
              activity?.let {
              val intent = Intent(it, PaymentSuccessActivity::class.java)
                it.startActivity(intent)

                  /*Handler(Looper.getMainLooper()).postDelayed({
                      val intent = Intent(it, PaymentSuccessActivity::class.java)
                      startActivity(intent)
                  }, 2000)*/
            }

        })

        mPaymentUpdateViewModel.getPaymentUnAuthorized().observe(viewLifecycleOwner, Observer {
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
        mPaymentUpdateViewModel.getProgressState()?.observe(viewLifecycleOwner,
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


        mPaymentUpdateViewModel.getAlertViewState()?.observe(viewLifecycleOwner,
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

    }


    private fun setIncludedLayout() {
        mIncludedLayoutBinding = mBinding.mainHeader
        mIncludedLayoutBinding.imgDrawer.setOnClickListener {
            mDrawerInterface?.setOnDrwawerClickResult()
        }
        mIncludedLayoutBinding.imgLogo.visibility = View.GONE
    }
    companion object {
        @JvmStatic
        fun newInstance(amount: String, itemType: String, itemId: Int, memberID: Int?) =
            PayFragment().apply {
                arguments = Bundle().apply {
                    putString(AMOUNT, amount)
                    putString(ITEM_TYPE, itemType)
                    putInt(ITEM_ID, itemId)
                    putInt(MEMBER_ID, memberID!!)
                }
            }
    }
}