package com.aadya.whiskyapp.payment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aadya.whiskyapp.MyApplication;
import com.aadya.whiskyapp.R;
import com.aadya.whiskyapp.payment.model.PaymentResponse;
import com.aadya.whiskyapp.payment.model.PaymentUpdate;
import com.aadya.whiskyapp.payment.viewmodel.PaymentFactory;
import com.aadya.whiskyapp.payment.viewmodel.PaymentUpdateViewModel;
import com.aadya.whiskyapp.utils.AlertModel;
import com.aadya.whiskyapp.utils.CommonUtils;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
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


public class CheckoutActivityJava extends AppCompatActivity{

    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    String amount,authorization,itemType,email,address,description,name,auth;
    int memberId,itemId;
    private TextView amountTextView;
    Button payButton;
    EditText cardHolderNameEditText;
    int mRemainingGuestPasses=0;
    private PaymentUpdateViewModel paymentUpdateViewModel;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_java);
        cardHolderNameEditText=findViewById(R.id.tv_cardholdername);
        paymentUpdateViewModel = new ViewModelProvider(this, new PaymentFactory(getApplication())).get(
                PaymentUpdateViewModel.class
        );


        //Creating the ArrayAdapter instance having the country list

        payButton = findViewById(R.id.payButton);
        ImageView imgDrawer= findViewById(R.id.img_drawer);
        ImageView profileIcon= findViewById(R.id.img_logo);
        ImageView backIcon= findViewById(R.id.img_back);
        backIcon.setVisibility(View.VISIBLE);
        profileIcon.setVisibility(View.GONE);
        imgDrawer.setVisibility(View.GONE);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        amountTextView = findViewById(R.id.amountTextView);
        Intent intent = getIntent();
        auth=intent.getStringExtra("auth");
        amount = intent.getStringExtra("amount");
        authorization = intent.getStringExtra("authorization");
        email = intent.getStringExtra("email");
        itemType = intent.getStringExtra("itemType");
        itemId = intent.getIntExtra("itemId",0);
        memberId = intent.getIntExtra("memberId",0);

        description=intent.getStringExtra("description");
        address= intent.getStringExtra("address");
        name=intent.getStringExtra("name");

        amountTextView = findViewById(R.id.amountTextView);
        amountTextView.setText("Total "+amount);
        amountTextView.setVisibility(View.GONE);
        payButton.setText("Total "+amount +" CONFIRM ORDER" );
        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
       /* stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_live_51JgvGWHg3vu6Xtwj4qO7v4IyjdnNEY7NjwaPlKcZ8hwa438erHUm70f9WPOixvz3noPeMkQ1XUtCcKObUvbKFq8J00EBr9TiJX")
        );*/

        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51JgvGWHg3vu6Xtwj7pamp4M0EWab892xZ5oFuXem3Wz7iMpthh1W22FxdJ2vVuOHkjH4yz0kje34k0yJBQk38aOL00pOB49p3n")
        );
        startCheckout();
        handleObserver();

        Button cancelButton=findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener((View view) -> {
            finish();
        });
    }

    private void handleObserver() {

        paymentUpdateViewModel.getPaymentUpdateObserver().observe(CheckoutActivityJava.this, new Observer<PaymentResponse>() {
            @Override
            public void onChanged(PaymentResponse paymentResponse) {

                Intent intent=new Intent(CheckoutActivityJava.this,PaymentSuccessActivity.class);
                intent.putExtra("activityCheck","CheckOut");
                startActivity(intent);
                finish();
            }
        });



       paymentUpdateViewModel.getAlertViewState().observe(this, new Observer<AlertModel>() {
                    @Override
                    public void onChanged(AlertModel alertModel) {
                       /* if (alertModel == null) return;
                                mCommonUtils.showAlert(
                                        alertModel.duration,
                                        alertModel.title,
                                        alertModel.message,
                                        alertModel.drawable,
                                        alertModel.color,
                                        requireActivity()

                                );*/
                    }
                });

        paymentUpdateViewModel.getProgressState().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer progressState) {
               /* if (progressState == null) return;
                if (progressState == CommonUtils.ProgressDialog.showDialog)
                    mCommonUtils.showProgress(
                            resources.getString(R.string.pleasewait), requireContext()
                    );
                else if (progressState == CommonUtils.ProgressDialog.dismissDialog)
                    mCommonUtils.dismissProgress();*/
            }
        });
        paymentUpdateViewModel.getPaymentUnAuthorized().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });

    }

    private void startCheckout() {





        // Create a PaymentIntent by calling the server's endpoint.
        String amountTemp = amount.replace("$", "");
        String amount = amountTemp.split("\\.")[0];

        String[] addressTemp = address.split(",");

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        Map<String,Object> payMap=new HashMap<>();
//        payMap.put("CardHolderName",name);
//        payMap.put("email",email);
//        payMap.put("Currency","usd");
//        payMap.put("PaymentMethodType","card");
        payMap.put("amount",amount);
//        payMap.put("Description",description);
//        payMap.put("Country","US");
        /*try {
            payMap.put("Line", addressTemp[0]);
            payMap.put("City", addressTemp[1]);
            payMap.put("State", addressTemp[2]);
            payMap.put("PostalCode", addressTemp[3]);
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e("Address","Address is not complete");
            payMap.put("Line", "Address Line");
            payMap.put("City", "City");
            payMap.put("State", "state");
            payMap.put("PostalCode", "123we3");
        }*/


        String json = new Gson().toJson(payMap);

        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(CommonUtils.APIURL.PaymentUrl)
                .post(body)
                .addHeader("Authorization",auth)
                .build();
        httpClient.newCall(request)
                .enqueue(new PayCallback(this));
        // Hook up the pay button to the card widget and stripe instance

        payButton.setOnClickListener((View view) -> {

            if(cardHolderNameEditText.getText().toString().trim()==null || cardHolderNameEditText.getText().toString().trim().isEmpty()){
                Toast.makeText(CheckoutActivityJava.this,"Enter Card Holder Name",Toast.LENGTH_SHORT).show();
            }else {
                CardMultilineWidget cardInputWidget = findViewById(R.id.cardInputWidget);
                PaymentMethodCreateParams params =
                        cardInputWidget.getPaymentMethodCreateParams();

                if (params != null) {
                    payButton.setClickable(false);
                    ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                            .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                    stripe.confirmPayment(this, confirmParams);
                }

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
            String paymentStatus = null;
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                //gson.toJson(paymentIntent)
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                calendar = Calendar.getInstance();
                dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                date = dateFormat.format(calendar.getTime());
                paymentStatus="Success";
               
            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                paymentStatus="Fail";
                // Payment failed – allow retrying using a different payment method
                activity.displayAlert(
                        "Payment failed",
                        Objects.requireNonNull(paymentIntent.getLastPaymentError()).getMessage()
                );
                
            }
            paymentUpdateViewModel.getPaymentUpdate(
                    authorization,new PaymentUpdate(0,paymentIntentClientSecret,itemType,itemId,memberId,date,paymentStatus,amount.replace("$",""), MyApplication.getMSelectedGuestPass(),MyApplication.isPlusOne()));



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