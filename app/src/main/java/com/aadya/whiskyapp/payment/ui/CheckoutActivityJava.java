package com.aadya.whiskyapp.payment.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.aadya.whiskyapp.R;
import com.aadya.whiskyapp.payment.model.PaymentResponse;
import com.aadya.whiskyapp.payment.model.PaymentUpdate;
import com.aadya.whiskyapp.payment.viewmodel.PaymentFactory;
import com.aadya.whiskyapp.payment.viewmodel.PaymentUpdateViewModel;
import com.aadya.whiskyapp.utils.AlertModel;
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
import java.util.ArrayList;
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


public class CheckoutActivityJava extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String BACKEND_URL = "http://92.204.128.4:5002/api/Payment/CreatePaymentIntent";

    private OkHttpClient httpClient = new OkHttpClient();
    private String paymentIntentClientSecret;
    private Stripe stripe;
    String amount,authorization,itemType;
    int memberId,itemId;
    private TextView amountTextView;
    Button payButton;
    EditText addressET;
    Spinner stateSp;
    EditText cityEditText,postEditText,cardHolderNameEditText,nameEditText;
    private PaymentUpdateViewModel paymentUpdateViewModel;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date,state;
    private ArrayAdapter<String> stateAdapter;
    private ArrayList<String> stateList;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_java);
        nameEditText=findViewById(R.id.name_edit_text);
        addressET=findViewById(R.id.tv_address_line);
        stateSp=findViewById(R.id.state_spinner);
        cityEditText=findViewById(R.id.city_spinner);
        postEditText=findViewById(R.id.postcode_spinner);
        cardHolderNameEditText=findViewById(R.id.tv_cardholdername);
        paymentUpdateViewModel = new ViewModelProvider(this, new PaymentFactory(getApplication())).get(
                PaymentUpdateViewModel.class
        );

        stateList = new ArrayList<String>();
        stateList.add("Select State");
        stateList.add("London");
        stateList.add("Leeds");
        stateList.add("Scotland");
        stateList.add("Andover");
        stateList.add("Manchester");

        stateSp.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.row_spinner,stateList);
        aa.setDropDownViewResource(R.layout.row_spinner_dialog);
        //Setting the ArrayAdapter data on the Spinner
        stateSp.setAdapter(aa);
        stateSp.setSelection(1);
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
        amount = intent.getStringExtra("amount");
        authorization = intent.getStringExtra("authorization");
        itemType = intent.getStringExtra("itemType");
        itemId = intent.getIntExtra("itemId",0);
        memberId = intent.getIntExtra("memberId",0);
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
        nextButton.setVisibility(View.VISIBLE);
        preButton.setVisibility(View.GONE);

       handleObserver();

        preButton.setOnClickListener((View view) -> {
            nextButton.setVisibility(View.VISIBLE);
            preButton.setVisibility(View.GONE);
            addressLayout.setVisibility(View.VISIBLE);
            paymentLayout.setVisibility(View.GONE);
        });
        nextButton.setOnClickListener((View view) -> {
            if((nameEditText.getText().toString().trim()==null)||nameEditText.getText().toString().trim().isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Name", Toast.LENGTH_LONG).show();
            }
            else if ((addressET.getText().toString()==null)||addressET.getText().toString().trim().isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter AddressLine", Toast.LENGTH_LONG).show();
            }

           else if((cityEditText.getText().toString()==null)||cityEditText.getText().toString().trim().isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter City", Toast.LENGTH_LONG).show();
            }else if((postEditText.getText().toString()==null)||postEditText.getText().toString().trim().isEmpty()){
                Toast.makeText(getApplicationContext(), "Please Enter Post code", Toast.LENGTH_LONG).show();
            }else if(state.equalsIgnoreCase("Select State")){
                Toast.makeText(getApplicationContext(), "Please select State", Toast.LENGTH_LONG).show();
            }else {
                startCheckout();
                nextButton.setVisibility(View.GONE);
                preButton.setVisibility(View.VISIBLE);
                addressLayout.setVisibility(View.GONE);
                paymentLayout.setVisibility(View.VISIBLE);
            }

        });
        Button cancelButton=findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener((View view) -> {
            finish();
        });
    }

    private void handleObserver() {

        paymentUpdateViewModel.getPaymentUpdateObserver().observe(CheckoutActivityJava.this, new Observer<PaymentResponse>() {
            @Override
            public void onChanged(PaymentResponse paymentResponse) {
                startActivity(new Intent(CheckoutActivityJava.this,PaymentSuccessActivity.class));
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
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        Map<String,Object> payMap=new HashMap<>();
        payMap.put("CardHolderName",nameEditText.getText().toString());
        payMap.put("Currency","USD");
        payMap.put("Amount",amount.replace("$",""));
        payMap.put("PaymentMethodType","card");
        payMap.put("Description","Test Payment Mobile");
        payMap.put("Line",addressET.getText().toString());
        payMap.put("City",cityEditText.getText().toString());
        payMap.put("Country","US");
        payMap.put("PostalCode",postEditText.getText().toString());
        payMap.put("State",state);


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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       // Toast.makeText(getApplicationContext(), stateList.get(position), Toast.LENGTH_LONG).show();
        state=stateList.get(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                    authorization,new PaymentUpdate(0,paymentIntentClientSecret,itemType,itemId,memberId,date,paymentStatus,amount.replace("$","")));



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