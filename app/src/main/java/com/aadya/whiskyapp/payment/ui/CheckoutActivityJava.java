package com.aadya.whiskyapp.payment.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.aadya.whiskyapp.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethod;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;

import java.lang.ref.WeakReference;
import java.util.Objects;


public class CheckoutActivityJava extends AppCompatActivity {

    private static final String BACKEND_URL ="http://92.204.128.4:5002/api/Payment/UpdatePayment";

    private String paymentIntentClientSecret="pk_test_51JHQWWSBuizC5qVCGh0YxWZuGPmCHFmjj4HpX6GZ9DRBF2TnNId3e2p5ZoKD0PXYlXJWbqx2mnKP1xPpUoHslrss007BoexTxO";
    private Stripe stripe;
    private TextView amountTextView;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_java);
        amountTextView = findViewById(R.id.amountTextView);
        // Configure the SDK with your Stripe publishable key so it can make requests to Stripe
        stripe = new Stripe(
                getApplicationContext(),
                Objects.requireNonNull("pk_test_51JHQWWSBuizC5qVCGh0YxWZuGPmCHFmjj4HpX6GZ9DRBF2TnNId3e2p5ZoKD0PXYlXJWbqx2mnKP1xPpUoHslrss007BoexTxO")
        );
        //startCheckout();
        Button payButton = findViewById(R.id.payButton);
        payButton.setOnClickListener((View view) -> {
            CardMultilineWidget cardInputWidget = findViewById(R.id.cardInputWidget);

            PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
            if (params != null) {
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntentClientSecret);
                stripe.confirmPayment(this, confirmParams);
            }

            if (params == null) {
                return;
            }
            stripe.createPaymentMethod(params, new ApiResultCallback<PaymentMethod>() {
                @Override
                public void onSuccess(@NonNull PaymentMethod result) {
                    // Create and confirm the PaymentIntent by calling the sample server's /pay endpoint.
                    pay(result.id, null);
                }

                @Override
                public void onError(@NonNull Exception e) {

                }
            });
        });
    }

    private void pay(String id, Object o) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Handle the result of stripe.confirmPayment
        //stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(this));
    }

//    private static final class PaymentResultCallback
//            implements ApiResultCallback<PaymentIntentResult> {
//        private final WeakReference<CheckoutActivityJava> activityRef;
//
//        PaymentResultCallback(@NonNull CheckoutActivityJava activity) {
//            activityRef = new WeakReference<>(activity);
//        }
//
//        @Override
//        public void onSuccess(@NonNull PaymentIntentResult result) {
//            final CheckoutActivityJava activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//
//            PaymentIntent paymentIntent = result.getIntent();
//            PaymentIntent.Status status = paymentIntent.getStatus();
//            if (status == PaymentIntent.Status.Succeeded) {
//                // Payment completed successfully
//                activity.runOnUiThread(() -> {
//                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                    activity.displayAlert("Payment completed",
//                            gson.toJson(paymentIntent), true);
//                });
//            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
//                // Payment failed – allow retrying using a different payment method
//                activity.runOnUiThread(() -> {
//                    final PaymentIntent.Error error = paymentIntent.getLastPaymentError();
//                    final String errorMessage;
//                    if (error != null && error.getMessage() != null) {
//                        errorMessage = error.getMessage();
//                    } else {
//                        errorMessage = "Unknown error";
//                    }
//                    activity.displayAlert("Payment failed", errorMessage, false);
//                });
//            } else if (status == PaymentIntent.Status.RequiresConfirmation) {
//                // After handling a required action on the client, the status of the PaymentIntent is
//                // requires_confirmation. You must send the PaymentIntent ID to your backend
//                // and confirm it to finalize the payment. This step enables your integration to
//                // synchronously fulfill the order on your backend and return the fulfillment result
//                // to your client.
//                activity.pay(null, paymentIntent.getId());
//            }
//        }

//        @Override
//        public void onError(@NonNull Exception e) {
//            final CheckoutActivityJava activity = activityRef.get();
//            if (activity == null) {
//                return;
//            }
//
//            // Payment request failed – allow retrying using the same payment method
//            activity.runOnUiThread(() ->
//                    activity.displayAlert("Error", e.toString(), false)
//            );
//        }
  //  }
//    private void displayAlert(@NonNull String title, @NonNull String message, boolean restartDemo) {
//        runOnUiThread(() -> {
//            final AlertDialog.Builder builder =
//                    new AlertDialog.Builder(this)
//                            .setTitle(title)
//                            .setMessage(message);
//            new GsonBuilder()
//                    .setPrettyPrinting()
//                    .create();
//            if (restartDemo) {
//                builder.setPositiveButton("Restart demo",
//                        (DialogInterface dialog, int index) -> loadPage());
//            } else {
//                builder.setPositiveButton("Ok", null);
//            }
//            builder
//                    .create()
//                    .show();
//        });
//    }

}