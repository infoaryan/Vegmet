package infoaryan.in.vegmet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.AvailableNetworkInfo;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Registration extends AppCompatActivity {

    EditText name,phone,otp,location;
    Button otpsend,register;
    TextView verify,logintext;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    CheckBox locatio;
    String mname,mlocation,mphone,motp,state;
    private String verificationcode;
    FirebaseAuth auth;
    FusedLocationProviderClient fusedLocationProviderClient;
    Geocoder geocoder;
    List<Address> list;
    Location[] loc;
    ProgressDialog progress;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

       sp = getSharedPreferences("main",MODE_PRIVATE);
        editor = sp.edit();


        StartFirebaseLogin();
        loc = new Location[1];
        //here the views are being finded with there respective id
        findViews();
        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        otp.setVisibility(View.INVISIBLE);

        Intent in = getIntent();
       state= in.getStringExtra("state");
        //the on click listener for the register button
        {
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //getting the parameters
                    mname = name.getText().toString();
                    mlocation = location.getText().toString();
                    mphone = phone.getText().toString().trim();
                    motp = otp.getText().toString();
                    try {
                        if (!isconnected()) {
                            Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(isempty()){
                        return;

                    }
                    if(!checkforvalidity()){
                        return;
                    }
                    final int[] rr = {0};
                    //Strat pushing the data into the database bu volley Library methods
                    progress.show();
                    progress.setCancelable(false);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://smellier-men.000webhostapp.com/v1/create" + state + ".php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                rr[0] = jsonObject.getInt("id");
                                progress.dismiss();
                                mname = jsonObject.getString("name");
                                mlocation = jsonObject.getString("location");
                                mphone = jsonObject.getString("phone");
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                            }

                            if (rr[0] == 1) {

                                int temp;
                                if (state.equalsIgnoreCase("seller")) {
                                    temp = 1;
                                } else {
                                    temp = 2;
                                }
                                if (temp == 1) {
                                    // passing th eintent valuse to the final intene or the home screen
                                    Intent intent = new Intent(getApplicationContext(), Home_Seller.class);
                                    editor.putBoolean("signed", true);
                                    editor.putString("name", mname);
                                    editor.putString("location", mlocation);
                                    editor.putString("phone", mphone);
                                    editor.putString("state", "seller");
                                    editor.commit();
                                    startActivity(intent);
                                    finish();

                                } else {
                                    Intent intent = new Intent(getApplicationContext(), Home_Consumer.class);
                                    editor.putBoolean("signed", true);
                                    editor.putString("name", mname);
                                    editor.putString("location", mlocation);
                                    editor.putString("phone", mphone);
                                    editor.putString("state", "consumer");
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
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }) {       // here we will pass the values and the further process will carry on
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name", mname);
                            params.put("phone", mphone);
                            params.put("location", mlocation);

                            return params;
                        }
                    };
                    RequestHandler.getInstance(Registration.this).addToRequestQueue(stringRequest);

                }
            });
        }

        //activity for openning the other activity for login user
        logintext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this,login.class);
                intent.putExtra("state",state);
                startActivity(intent);
            }
        });


        //button for sending otp
        otpsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mphone=phone.getText().toString();
               if(TextUtils.isEmpty(mphone)){
                   Toast.makeText(getApplicationContext(),"Enter phone number",Toast.LENGTH_SHORT).show();
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
                           Registration.this,        // Activity (for callback binding)
                           mcallbacks);                      // OnVerificationStateChangedCallbacks
               }

            }
        });

        //onclick of check box for the location
        locatio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                if(isChecked){
                    try {
                        if(isconnected()){
                            setLocation();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Check your connection!",Toast.LENGTH_SHORT).show();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Enter your address",Toast.LENGTH_SHORT).show();
                    location.requestFocus();
                    location.setText("");
                }
            }
        });

    }

    //initialize the views
    void findViews(){
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phonenumber);
        otp = findViewById(R.id.otprecieved);
        location = findViewById(R.id.addres);
        locatio = findViewById(R.id.usecurrentlocation);
        verify = findViewById(R.id.verified);
        otpsend = findViewById(R.id.sendotp);
        register = findViewById(R.id.registerbutton);
        logintext=findViewById(R.id.signintext);


    }

    //check for empty fields
    private Boolean isempty(){
        if(TextUtils.isEmpty(name.getText().toString())){
            Toast.makeText(getApplicationContext(),"Please Enter Name ",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(TextUtils.isEmpty(phone.getText().toString())){
            Toast.makeText(getApplicationContext(),"Please Enter Number ",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(TextUtils.isEmpty(location.getText().toString())){
            Toast.makeText(getApplicationContext(),"Please Enter Address ",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    //proper check for everything
    private Boolean checkforvalidity(){


        for(int i=0;i<mname.length();++i){
            char a=mname.charAt(i);
            if(!((a>='a' && a<='z') || (a>='A' && a<='Z') || a==' ')){
                Toast.makeText(getApplicationContext(),"Enter suitable Name",Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        for(int i=0;i<mlocation.length();++i){
            char a=mlocation.charAt(i);
            if(!((a>='a' && a<='z') || (a>='A' && a<='Z') || a==' ' || (a>='0' && a<='9') || a==',' || a=='.' || a=='-')){
                Toast.makeText(getApplicationContext(),"Only a-z 1-9 , . - are allowed in address",Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        if(motp==null || motp.equalsIgnoreCase("") || motp.equalsIgnoreCase(" ")){
            Toast.makeText(getApplicationContext(),"Number not verified!",Toast.LENGTH_SHORT).show();
            return false;}
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationcode,motp);
        getSignin(credential);
        if(!verify.getText().toString().trim().equalsIgnoreCase("verified")){
            Toast.makeText(getApplicationContext(),"Please verify the number",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // This method is used for auto detecting the sent otp and performing the action
     void StartFirebaseLogin(){
        auth = FirebaseAuth.getInstance();
        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                otp.setText(phoneAuthCredential.getSmsCode());
                otp.setEnabled(false);

                verify.setText("verified");
                verify.setTextColor(Color.GREEN);
                otpsend.setVisibility(View.INVISIBLE);
                otpsend.setEnabled(false);
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationcode = s;
                progress.dismiss();
                Toast.makeText(getApplicationContext(),"The code is sent!!",Toast.LENGTH_SHORT).show();

                otp.setVisibility(View.VISIBLE);
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
                            verify.setText("verified");
                            verify.setTextColor(Color.GREEN);
                            otpsend.setVisibility(View.INVISIBLE);
                            otpsend.setEnabled(false);
                        } else {

                        }
                    }
                });
    }

    //this method gives the location to the user based on current longitude and latitude
    //get location method for getting geocoded location from latitudes and longitudes
    void setLocation(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Registration.this);
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(Registration.this,
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location locati) {
                      geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                        try {
                            list=geocoder.getFromLocation(locati.getLatitude(),locati.getLongitude(),1);
                            location.setText(list.get(0).getAddressLine(0));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });


    }

    //check for the availability of internet
    public Boolean isconnected() throws InterruptedException,IOException{
       // final String command ="ping -c 1 google.com";
       // return Runtime.getRuntime().exec(command).waitFor()==0;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =cm.getActiveNetworkInfo();
        return (info!=null && info.isConnectedOrConnecting());
    }

    //check for duplicate data in the database


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==201 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(getApplicationContext(),"Granted permission",Toast.LENGTH_SHORT).show();
            return;
        }
        else{
            Toast.makeText(getApplicationContext(),"not granted",Toast.LENGTH_SHORT).show();

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
