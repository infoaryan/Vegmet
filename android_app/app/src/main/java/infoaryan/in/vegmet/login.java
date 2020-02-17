package infoaryan.in.vegmet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class login extends AppCompatActivity {
    Button sendotp,login;
    EditText otp,phone;
    String mphone,motp,state;
   String kname,klocation,kphone;
    ProgressDialog progress;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private String verificationcode;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(login.this);
        findViews();
    StartFirebaseLogin();
        state = getIntent().getStringExtra("state");

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mphone=phone.getText().toString();
                if(TextUtils.isEmpty(mphone)){
                    Toast.makeText(login.this,"Enter phone number",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(TextUtils.isDigitsOnly(mphone) && mphone.length()==10)){
                    Toast.makeText(getApplicationContext(),"Enter 10 digit number",Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    progress.show();
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91"+mphone,                    // Phone number to verify
                            60,                           // Timeout duration
                            TimeUnit.SECONDS,                // Unit of timeout
                            login.this,        // Activity (for callback binding)
                            mcallbacks);                      // OnVerificationStateChangedCallbacks
                }

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mphone=phone.getText().toString();
                progress.show();
                if(TextUtils.isEmpty(mphone)){
                    Toast.makeText(getApplicationContext(),"Enter phone number",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(TextUtils.isDigitsOnly(mphone) && mphone.length()==10)){
                    Toast.makeText(getApplicationContext(),"Enter 10 digit number",Toast.LENGTH_SHORT).show();
                    return;
                }

                getsigned();
            }
        });

    }

    void findViews(){
        sendotp = findViewById(R.id.sendotp);
        login = findViewById(R.id.login);
        otp = findViewById(R.id.otprecieved);
        phone = findViewById(R.id.phone);

        otp.setEnabled(false);
        otp.setVisibility(View.INVISIBLE);
        progress = new ProgressDialog(this);
        progress.setMessage("Please wait...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }



    // This method is used for auto detecting the sent otp and performing the action
    void StartFirebaseLogin(){
        auth = FirebaseAuth.getInstance();
        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                otp.setText(phoneAuthCredential.getSmsCode());
                otp.setEnabled(false);
                Toast.makeText(getApplicationContext(),"OTP Verified",Toast.LENGTH_LONG).show();
                sendotp.setVisibility(View.INVISIBLE);
                sendotp.setEnabled(false);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationcode = s;
                progress.dismiss();
                Toast.makeText(getApplicationContext(),"The code is sent!!",Toast.LENGTH_SHORT).show();
                otp.setVisibility(View.VISIBLE);
                otp.setEnabled(true);
                phone.setEnabled(false);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progress.dismiss();
                Toast.makeText(getApplicationContext(),"Check Network",Toast.LENGTH_SHORT).show();
            }
        };
    }

    // This method is used for the manual verification of the otp by entering it in the text field
    void getSignin(PhoneAuthCredential credential){
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            otp.setEnabled(false);
                            progress.dismiss();
                            Toast.makeText(login.this,"OTP verified",Toast.LENGTH_LONG).show();
                            sendotp.setVisibility(View.INVISIBLE);
                            sendotp.setEnabled(false);
                        } else {
                            progress.dismiss();
                            Toast.makeText(login.this,"Verification failure",Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }
    //start signing in
    void getsigned(){
                mphone = phone.getText().toString().trim();
                motp=otp.getText().toString();
                try {
                    if(!isconnected()){
                        Toast.makeText(getApplicationContext(),"Network error",Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //Strat pushing the data into the database bu volley Library methods
                progress.show();
                progress.setCancelable(false);
        final int[] flag = new int[1];

                StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://smellier-men.000webhostapp.com/v1/signinseller.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonObject  = new JSONObject(s);
                            Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                            flag[0] = jsonObject.getInt("id");
                            kname=jsonObject.getString("name");
                            klocation = jsonObject.getString("location");
                            kphone = jsonObject.getString("phone");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(flag[0]==1){
                            int temp ;
                            if(state.equalsIgnoreCase("seller")){
                                temp=1;
                            }
                            else{
                                temp=2;
                            }
                            if(temp==1) {
                                // passing th eintent valuse to the final intene or the home screen
                                Intent intent = new Intent(getApplicationContext(),Home_Seller.class);
                                SharedPreferences sp = getSharedPreferences("main",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("signed",true);
                                editor.putString("name",kname);
                                editor.putString("location",klocation);
                                editor.putString("state","seller");
                                editor.putString("phone",kphone);
                                editor.commit();

                                startActivity(intent);
                                finish();
                            }
                            else{
                                Intent intent = new Intent(getApplicationContext(),Home_Consumer.class);
                                SharedPreferences sp = getSharedPreferences("main",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putBoolean("signed",true);
                                editor.putString("name",kname);
                                editor.putString("location",klocation);
                                editor.putString("phone",kphone);
                                editor.putString("state","consumer");
                                editor.commit();
                                startActivity(intent);
                                finish();
                            }
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                })
                {       // here we will pass the values and the further process will carry on
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<>();
                        params.put("phone",mphone);
                        return params;
                    }
                } ;
                RequestHandler.getInstance(login.this).addToRequestQueue(stringRequest);



    }
    public Boolean isconnected() throws InterruptedException,IOException{
        // final String command ="ping -c 1 google.com";
        // return Runtime.getRuntime().exec(command).waitFor()==0;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =cm.getActiveNetworkInfo();
        return (info!=null && info.isConnectedOrConnecting());
    }
}

