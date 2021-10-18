package com.aadya.whiskyapp.payment.ui;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.aadya.whiskyapp.R;
import com.aadya.whiskyapp.dashboard.ui.DashBoardActivity;
import com.aadya.whiskyapp.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardInputWidget;
import com.stripe.android.view.CardMultilineWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
public class Payment_Activity extends AppCompatActivity {

    // 10.0.2.2 is the Android emulator's alias to localhost
    //private static final String BACKEND_URL = "https://guarded-harbor-90989.herokuapp.com/";
    private static final String BACKEND_URL ="http://92.204.128.4:5002/api/Payment/UpdatePayment";
    private static final String KEY ="pk_test_51JHQWWSBuizC5qVCGh0YxWZuGPmCHFmjj4HpX6GZ9DRBF2TnNId3e2p5ZoKD0PXYlXJWbqx2mnKP1xPpUoHslrss007BoexTxO";
    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    private View mView;
    private KProgressHUD kProgressHUD;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51JHQWWSBuizC5qVCGh0YxWZuGPmCHFmjj4HpX6GZ9DRBF2TnNId3e2p5ZoKD0PXYlXJWbqx2mnKP1xPpUoHslrss007BoexTxO")
        );
        setHeader();
        startCheckout();
        setButtonFont();
    }

    private void setButtonFont() {
        Button continueButton = findViewById(R.id.continueButton);
        continueButton.setTypeface(ResourcesCompat.getFont(this, R.font.archivonarrow_regular));
    }

    private void setHeader() {
        mView = findViewById(R.id.signupheader_layout);
       /* TextView tvheader = mView.findViewById(R.id.tv_header);
        tvheader.setText(getResources().getString(R.string.payment));*/
        /*ImageView imgback = mView.findViewById(R.id.img_back);
        imgback.setOnClickListener(v -> {
            Log.d("Tag","clicked");
            this.finish();
        });*/
    }

    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
      /*  String json = "{"
                + ""currency":"usd","
                + ""items":["
                + "{"id":"photo_subscription"}"
                + "]"
                + "}";*/
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        double amount=Double.valueOf("500")*100;
        Map<String,Object> payMap=new HashMap<>();
        Map<String,Object> itemMap=new HashMap<>();
        List<Map<String,Object>> itemList =new ArrayList<>();
//        payMap.put("currency","USD");
//        itemMap.put("id","photo_subscription");
//        itemMap.put("amount",amount);





            itemMap.put( "PaymentID",0);
            itemMap.put("TicketID",KEY);
            itemMap.put("ItemType","S");
            itemMap.put("ItemID",23);// event id /special offer //
            itemMap.put("MemberID",1);
            itemMap.put("PaymentDate",currentDate.toString());
            itemMap.put("PaymentStatus","Pending");
            itemMap.put("Amount",amount);



        itemList.add(itemMap);
        payMap.put("items",itemList);
        String json = new Gson().toJson(payMap);

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL + "create-payment-intent")
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new Payment_Activity.PayCallback(this));
        // Hook up the pay button to the card widget and stripe instance
        Button payButton = findViewById(R.id.continueButton);
        payButton.setOnClickListener((View view) -> {


           /* CardMultilineWidget cardInputWidget = findViewById(R.id.cardMultiLineInputWidget);
            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                hideKeyboard();
                showProgress(
                        getResources().getString(R.string.pleasewait));

                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);
            }*/
        });
    }
    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        dismissProgress();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                /*Intent mIntent = new Intent(Payment_Activity.this, CreateQRCode_IDActivity.class);
                startActivity(mIntent);
                finish();*/
            }
        });
        builder.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }
    private void onPaymentSuccess(@NonNull final Response response) throws IOException {

       // mCommonUtils.dismissProgress();
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret = responseMap.get("clientSecret");
    }
    private  final class PayCallback implements Callback {
        @NonNull private final WeakReference<Payment_Activity> activityRef;
        PayCallback(@NonNull Payment_Activity activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final Payment_Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }
          //  mCommonUtils.dismissProgress();
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final Payment_Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }

           // mCommonUtils.dismissProgress();
            if (!response.isSuccessful()) {
                activity.runOnUiThread(() ->
                        Toast.makeText(
                                activity, "Error: " + response.toString(), Toast.LENGTH_LONG
                        ).show()
                );
            } else {
                activity.onPaymentSuccess(response);
            }
        }
    }
    private  final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<Payment_Activity> activityRef;
        PaymentResultCallback(@NonNull Payment_Activity activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final Payment_Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();

           // mCommonUtils.dismissProgress();

            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                //gson.toJson(paymentIntent)
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                activity.displayAlert(
                        "Payment completed",
                        "Payment Successfully Done."
                );
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
            }
        }
        @Override
        public void onError(@NonNull Exception e) {
            final Payment_Activity activity = activityRef.get();
            if (activity == null) {
                return;
            }

           // mCommonUtils.dismissProgress();
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }


    public void showProgress(String message) {


        kProgressHUD = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .setBackgroundColor(ContextCompat.getColor(this, R.color.themecolor))
                .show();

    }

    public void  dismissProgress() {
        if (kProgressHUD.isShowing()) kProgressHUD.dismiss();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}