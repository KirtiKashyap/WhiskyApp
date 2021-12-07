package com.aadya.whiskyapp.utils

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.aadya.whiskyapp.R
import com.kaopiz.kprogresshud.KProgressHUD
import com.tapadoo.alerter.Alerter
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern



object CommonUtils {
    private lateinit var kProgressHUD : KProgressHUD
    private lateinit var mCommonUtils: CommonUtils
    @JvmStatic var specialoffer_adapter_position : Int = 0

    fun setButtonFont(mbutton: Button, context: Context) {
         mbutton.typeface = ResourcesCompat.getFont(
             context,
             R.font.archivonarrow_regular
         );
    }
    fun LogMessage(message: String?){
        Log.d("TAG", "Message :$message")
    }

    fun isValidEmail(email: String?): Boolean {
        val pattern = Pattern.compile(".+@.+\\.[a-z]+")
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun hideKeyboard(activity: Context) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)
    }

    interface ProgressDialog {
        companion object {
            const val showDialog = 10
            const val dismissDialog = 11
        }
    }

    interface APIURL {

        companion object {
            const val Profile_IMAGE_URL="https://ultimiz.com/UploadFiles/MembershipImage/"
            //const val Event_IMAGE_URL="https://ultimiz.com"
            const val Event_IMAGE_URL="https://ultimiz.com/UploadFiles/EventImage/"
            const val QRCode_IMAGE_URL="https://ultimiz.com/UploadFiles/QRCode/"
            const val BASE_URL: String = "https://api.ultimiz.com/api/"
            const val Profile: String = "Mobile/GetAppUserByID"
            const val SpecialOffer: String = "Mobile/GetSpecialofferListForApp"
            const val LOGIN_USER: String  = "Login/AppUserAuthenticate"
            const val FORGOT_PASSWORD: String  = "https://ultimiz.com/Login/ForgotPassword"
            const val Events : String = "Mobile/GetEventListForApp"
            const val ProfileEdit : String = "Mobile/UpdateAppUser"
            const val RSVP :  String = "Mobile/UpdateEventResponseForApp"
            const val EventAttending :  String = "Mobile/GetEventListWithFeedbackForApp"
            const val PurchaseHistory :  String = "Payment/GetPaymentHistory"
            const val Reserve: String  = "Reservation/UpdateReservation"
            const val UploadProfileImage: String="Mobile/UpdateAppMemberPhoto"
            const val PaymentUpdate: String="Payment/UpdatePayment"
            const val EventNotification: String="Mobile/ChangeMemberEventNotification"
            const val OfferNotification: String="Mobile/ChangeMemberSpecialNotification"
            const val Logout: String="Login/LogOut"
        }
    }

    fun date_dd_MMM_yyyy(inputDateString: String?): String {

        val inputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val date: Date = inputFormat.parse(inputDateString)
        val outputDateStr: String = outputFormat.format(date)
        return  outputDateStr
    }

    fun date_dd_MM_yyyy(inputDateString: String?): String {
        val inputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val outputFormat: DateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date: Date = inputFormat.parse(inputDateString)
        val outputDateStr: String = outputFormat.format(date)
        return  outputDateStr
    }
    //@RequiresApi(Build.VERSION_CODES.O)
    fun getWeekDay(lastSeen: String?): String{
        val toTypedArray = lastSeen?.split(" ")!!.toTypedArray()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val date = sdf.parse(toTypedArray[0])
        sdf.applyPattern("EEEE")
        val str = sdf.format(date)
        println(str)
        try {
            val localTime: LocalTime =
                LocalTime.parse(
                    toTypedArray[1],
                    DateTimeFormatter.ofPattern("HH:mm", Locale.ENGLISH)
                )

            // Output 12 hrs
            println(
                localTime.format(
                    DateTimeFormatter.ofPattern(
                        "hh:mm a",
                        Locale.ENGLISH
                    )
                )
            )
        }catch (e: Exception){
            e.printStackTrace()
        }
        return SimpleDateFormat("EEEE", Locale.ENGLISH).format(date) +" at "+ toTypedArray[1]
    }

    fun showAlert(
        setDuration: Long,
        title: String?,
        text: String?,
        drawable: Int,
        color: Int,
        activity: Activity

    ) {
        Alerter.create(activity)
            .setTitle(title)
            .setText(text)
            .setDuration(setDuration)
            .setIcon(drawable)
            .setBackgroundColorRes(color)
            .show()
    }

    fun showProgress(message: String?, context: Context) {
        mCommonUtils = CommonUtils


            kProgressHUD = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(ContextCompat.getColor(context, R.color.progressDialogueColor))
                .show()

    }

    fun dismissProgress() {
        if (kProgressHUD.isShowing) kProgressHUD.dismiss()
    }



    fun getWeek_Day(inputDate: Date?): String {
        val formatter: DateFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
        return formatter.format(inputDate)
    }

    fun convertString_To_Date(inputString: String): Date? {
        val format: DateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val date = format.parse(inputString)
        System.out.println(date)
        return date
    }

    fun getMonth_From_Date(inputDate: Date?): String {
        val c = Calendar.getInstance()
        c.time = inputDate
        val month_date = SimpleDateFormat("MMMM")
        val month_name = month_date.format(c.time)
        return month_name
    }

    fun convertString_To_Date_dd_MMM_yyyy_format(inputString: String?): String {

        val df2 = SimpleDateFormat("dd MMM yyyy")
        val df1 = SimpleDateFormat("yyyy-MM-dd")
        var returnDate: Date? = null
        var returnString : String = ""
        try {
            returnDate = df1.parse(inputString)
            returnString = df2.format(returnDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return  returnString
    }

    fun convertString_To_Date_ddMMMyyyyformat(inputString: String?): String {

        val df2 = SimpleDateFormat("MM/dd/yyyy")
        val df1 = SimpleDateFormat("yyyy-MM-dd")
        var returnDate: Date? = null
        var returnString : String = ""
        try {
            returnDate = df1.parse(inputString)
            returnString = df2.format(returnDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return  returnString
    }

    fun  getDay_From_Date(inputDate: Date?) : String{
        val c = Calendar.getInstance()
        c.time = inputDate
         val day : Int=c.get(Calendar.DATE);

        when(day % 10){
            1 -> return SimpleDateFormat("d'st'").format(inputDate)
            2 ->
                return SimpleDateFormat("d'nd'").format(inputDate)
            3 ->
                return SimpleDateFormat("d'rd'").format(inputDate)
            else ->
                return  SimpleDateFormat("d'th'").format(inputDate)
        }
    }


}