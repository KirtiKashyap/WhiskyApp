package com.aadya.whiskyapp.payment.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aadya.whiskyapp.MyApplication
import com.aadya.whiskyapp.R
import com.aadya.whiskyapp.landing.ui.LandingActivity
import com.aadya.whiskyapp.payment.model.PaymentByEmail
import com.aadya.whiskyapp.payment.model.PaymentUpdate
import com.aadya.whiskyapp.payment.viewmodel.PaymentFactory
import com.aadya.whiskyapp.payment.viewmodel.PaymentUpdateViewModel
import com.aadya.whiskyapp.utils.AlertModel
import com.aadya.whiskyapp.utils.CommonUtils
import com.aadya.whiskyapp.utils.SessionManager
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.fragment_pay.*
import java.text.SimpleDateFormat
import java.util.*

class PayActivity : AppCompatActivity() {
    var amount: String? = null
    var authorization: String? = null
    var imageName: String? = null
    var eventTitle: String? = null
    private var itemType:String? = null
    var itemId: Int? = null
    var mRemainingGuestPasses :Int=0
    private var memberId:Int? = null
    private lateinit var mCommonUtils: CommonUtils
    private lateinit var mSessionManager: SessionManager
    private lateinit var mPaymentUpdateViewModel: PaymentUpdateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_pay)
        val intent = intent
        amount = intent.getStringExtra("amount")
        authorization = intent.getStringExtra("authorization")
        imageName = intent.getStringExtra("imageName")
        eventTitle = intent.getStringExtra("eventTitle")
        itemType = intent.getStringExtra("itemType")
        itemId = intent.getIntExtra("itemId", 0)
        memberId = intent.getIntExtra("memberId", 0)
        mSessionManager = SessionManager.getInstance(this)!!
        mCommonUtils = CommonUtils

        val imgDrawer = findViewById<ImageView>(R.id.img_drawer)
        val profileIcon = findViewById<ImageView>(R.id.img_logo)
        val backIcon = findViewById<ImageView>(R.id.img_back)
        backIcon.visibility = View.VISIBLE
        profileIcon.visibility = View.GONE
        imgDrawer.visibility = View.GONE

        backIcon.setOnClickListener { finish() }

        mPaymentUpdateViewModel =
            ViewModelProvider(this, PaymentFactory(this?.application)).get(
                PaymentUpdateViewModel::class.java
            )
        setData()
        handleObserver()
        checkOutButton.setOnClickListener {
            val amountTemp: String = amount!!.replace("$", "")
            val amount1 = amountTemp.split("\\.".toRegex()).toTypedArray()[0]
                val mPaymentByEmail= PaymentByEmail(
                    amount1!!,
                    "usd",
                    mSessionManager.getProfileModel()?.email!!
                )
                mSessionManager.getAuthorization()?.let { it1 ->
                    mPaymentUpdateViewModel.paymentByEmail(
                        it1,
                        mPaymentByEmail
                    )
                }


        }
        cancelButton.setOnClickListener {
            finish()
        }
    }

    private fun handleObserver() {

        mPaymentUpdateViewModel.getPaymentByEmailObserver().observe(this, Observer {
            if (it!!.clientSecret == null) {

                val builder = AlertDialog.Builder(this, R.style.CustomAlertDialog)
                    .create()
                val view = layoutInflater.inflate(R.layout.customview_layout, null)
                val button = view.findViewById<Button>(R.id.dialogDismiss_button)
                val titleTextView = view.findViewById<TextView>(R.id.title)
                val messageTextView = view.findViewById<TextView>(R.id.message)
                titleTextView.text = resources.getString(R.string.registration_error)
                messageTextView.text = resources.getString(R.string.not_register)
                builder.setView(view)
                button.setOnClickListener {
                    builder.dismiss()
                }
                builder.setCanceledOnTouchOutside(false)
                builder.show()

            } else {
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
                            itemType!!,
                            itemId!!,
                            memberId!!,
                            date,
                            splitStatusAndSecret[0]!!,
                            amount!!,
                            MyApplication.mSelectedGuestPass,
                            MyApplication.isPlusOne
                        )
                    )
                }
            }

        })
        mPaymentUpdateViewModel.getPaymentUpdateObserver().observe(this, Observer {
            if (it == null) return@Observer
            this?.let {
                val intent = Intent(this, PaymentSuccessActivity::class.java)
                intent.putExtra("activityCheck", "Pay")
                startActivity(intent)
                finish()
            }

        })

        mPaymentUpdateViewModel.getPaymentUnAuthorized().observe(this, Observer {
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
                this

            )

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LandingActivity::class.java)
                startActivity(intent)
            }, 2000)

        })
        mPaymentUpdateViewModel.getProgressState()?.observe(this,
            object : Observer<Int?> {
                override fun onChanged(progressState: Int?) {
                    if (progressState == null) return
                    if (progressState === CommonUtils.ProgressDialog.showDialog)
                        mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), this@PayActivity
                        )
                    else if (progressState === CommonUtils.ProgressDialog.dismissDialog)
                        mCommonUtils.dismissProgress()
                }
            })


        mPaymentUpdateViewModel.getAlertViewState()?.observe(this,
            object : Observer<AlertModel?> {
                override fun onChanged(alertModel: AlertModel?) {
                    if (alertModel == null) return
                    mCommonUtils.showAlert(
                        alertModel.duration,
                        alertModel.title,
                        alertModel.message,
                        alertModel.drawable,
                        alertModel.color,
                        this@PayActivity

                    )
                }
            })

    }


    private fun setData() {
        val amountTemp: String = amount!!.replace("$", "")
        val amount = amountTemp.split("\\.".toRegex()).toTypedArray()[0]


        idTextView.text = "Exclusive ID: " + itemId + " " + eventTitle
        messageTextView.text =
            "You have agreed to purchase" + eventTitle + "at the special offer of " + amount + " per bottle.\n" +
                    "\n" +
                    "This amount would be deducted from your CC through Stripe Payment gateway and would be delivered to:\n" +
                    "\n" +
                    mSessionManager.getProfileModel()?.address


        if (imageName?.isNullOrEmpty() == false) {
            if (itemType.equals("S")) {
                application?.let {
                    Glide.with(this)
                        .load(CommonUtils.APIURL.Special_Offer_Image_Url + imageName)
                        .into(itemImage)
                }
            }else{
                application?.let {
                    Glide.with(this)
                        .load(CommonUtils.APIURL.Event_IMAGE_URL + imageName)
                        .into(itemImage)
                }
            }

        }
    }
}