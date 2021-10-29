package com.aadya.whiskyapp.payment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aadya.whiskyapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CheckoutActivityJava extends AppCompatActivity {

    // 10.0.2.2 is the Android emulator's alias to localhost
    private static final String BACKEND_URL = "http://92.204.128.4:5002/api/Payment/CreatePaymentIntent";

    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    String amount;
    private TextView amountTextView;
    Button payButton;
    private  ArrayAdapter<String> noofpeopleAdapter;
    private ArrayList<String> noOfPeopleList;
    EditText addressET;
    Spinner countrySp,stateSp,citySp,postSp;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_java);
        addressET=findViewById(R.id.tv_address_line);
        countrySp=findViewById(R.id.country_spinner);
        stateSp=findViewById(R.id.state_spinner);
        citySp=findViewById(R.id.city_spinner);
        postSp=findViewById(R.id.postcode_spinner);

        payButton = findViewById(R.id.payButton);
        ImageView imgDrawer= findViewById(R.id.img_drawer);
        ImageView profileIcon= findViewById(R.id.img_logo);
        profileIcon.setVisibility(View.GONE);
        imgDrawer.setVisibility(View.GONE);
        amountTextView = findViewById(R.id.amountTextView);
        Intent intent = getIntent();
        amount = intent.getStringExtra("amount");
        amountTextView = findViewById(R.id.amountTextView);
        amountTextView.setText("Total "+amount);
        amountTextView.setVisibility(View.GONE);
        LinearLayout addressLayout=findViewById(R.id.address_layout);
        LinearLayout paymentLayout=findViewById(R.id.payment_layout);
        payButton.setText("Total "+amount +" CONFIRM ORDER" );
        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51Jlt4ySAToMq1oS5dtA0kVj5d5nmZV7pzAaiucGkO2CFPqHuYz22tmHoNKPQaFtbLNDwYZh7lKc9JQRAAv7gHuJT00NRFz273R")
        );
        Button preButton=findViewById(R.id.prevButton);
        Button nextButton=findViewById(R.id.nexButton);
        nextButton.setVisibility(View.GONE);
        preButton.setVisibility(View.GONE);
        preButton.setOnClickListener((View view) -> {
            nextButton.setVisibility(View.VISIBLE);
            preButton.setVisibility(View.GONE);
            addressLayout.setVisibility(View.VISIBLE);
            paymentLayout.setVisibility(View.GONE);
        });
        nextButton.setOnClickListener((View view) -> {
            nextButton.setVisibility(View.GONE);
            preButton.setVisibility(View.VISIBLE);
            addressLayout.setVisibility(View.GONE);
            paymentLayout.setVisibility(View.VISIBLE);
        });
        Button cancelButton=findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener((View view) -> {
            finish();
        });
        startCheckout();
    }
    private void startCheckout() {
        // Create a PaymentIntent by calling the server's endpoint.
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        Map<String,Object> payMap=new HashMap<>();
        payMap.put("Currency","USD");
        payMap.put("Amount",amount.replace("$",""));
        payMap.put("PaymentMethodType","card");
        payMap.put("Description","Test Payment Mobile");

        String json = new Gson().toJson(payMap);

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(BACKEND_URL)
                .post(body)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));
        // Hook up the pay button to the card widget and stripe instance

        payButton.setOnClickListener((View view) -> {
            payButton.setClickable(false);
            CardMultilineWidget cardInputWidget = findViewById(R.id.cardInputWidget);
            PaymentMethodCreateParams params =
                    cardInputWidget.getPaymentMethodCreateParams();

            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);
            }
        });
    }
    private void displayAlert(@NonNull String title,
                              @Nullable String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message);
        builder.setPositiveButton("Ok", null);
        builder.create().show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }
    private void onPaymentSuccess(@NonNull final Response response) throws IOException {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, String>>(){}.getType();
        Map<String, String> responseMap = gson.fromJson(
                Objects.requireNonNull(response.body()).string(),
                type
        );
        paymentIntentClientSecret=String.valueOf(responseMap.get("clientSecret"));

    }
    private static final class PayCallback implements Callback {
        @NonNull private final WeakReference<CheckoutActivityJava> activityRef;
        PayCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
        }
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            activity.runOnUiThread(() ->
                    Toast.makeText(
                            activity, "Error: " + e.toString(), Toast.LENGTH_LONG
                    ).show()
            );
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull final Response response)
                throws IOException {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
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
    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<CheckoutActivityJava> activityRef;
        PaymentResultCallback(@NonNull CheckoutActivityJava activity) {
            activityRef = new WeakReference<>(activity);
            payButton.setClickable(true);
        }
        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                //gson.toJson(paymentIntent)
                Gson gson = new GsonBuilder().setPrettyPrinting().create();

//                startActivity(new Intent(CheckoutActivityJava.this,PaymentSuccessActivity.class));
//                finish();
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
            final CheckoutActivityJava activity = activityRef.get();
            if (activity == null) {
                return;
            }
            // Payment request failed – allow retrying using the same payment method
            activity.displayAlert("Error", e.toString());
        }
    }
}