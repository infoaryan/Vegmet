package infoaryan.in.vegmet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Home_Seller extends AppCompatActivity {
TextView namehere,locationhere,titl,postad;
RecyclerView recycle;
productadapter pp;
ProgressDialog progress;
String title,desc,phone;
int price;
Button load;
ImageView account;
List<Product> productList;
SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home__seller);
        findview();

        sp=getSharedPreferences("main",MODE_PRIVATE);
        String tempname,temploc;
        tempname = sp.getString("name","name");
        temploc = sp.getString("location","loc");
        phone = sp.getString("phone","0");
        namehere.setText(tempname);
        locationhere.setText(temploc);

        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        {
        postad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_Seller.this,ad.class);
                startActivity(intent);
            }
        });

            namehere.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openup();
                }
            });
            account.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openup();
                }
            });
        }

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking the connection tot the server
                try {
                    if(!isconnected()){
                        Toast.makeText(Home_Seller.this,"Check the network!!",Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                progress.show();
                progress.setCancelable(false);
                productList = new ArrayList<>();
                recycle.setHasFixedSize(true);
                recycle.setLayoutManager(new LinearLayoutManager(Home_Seller.this));
                loadproducts();

            }
        });


    }

    void loadproducts(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://smellier-men.000webhostapp.com/v1/getadsbyphone.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            if(array.length()==0){
                                titl.setText("Sorry!! No Ads");
                                return;
                            }
                            for(int i=0;i<array.length();++i){
                                JSONObject object = array.getJSONObject(i);

                                title = object.getString("title");
                                desc = object.getString("desc");
                                price = object.getInt("price");
                                Product product = new Product(title,desc,price);
                                productList.add(product);
                            }
                            pp = new productadapter(Home_Seller.this,productList);
                            recycle.setAdapter(pp);
                            progress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){       // here we will pass the values and the further process will carry on
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("phone",phone);


                return params;
            }
        };

        RequestHandler.getInstance(Home_Seller.this).addToRequestQueue(stringRequest);
    }

    private void findview(){
        namehere = findViewById(R.id.namehere);
        locationhere = findViewById(R.id.locationhere);
        titl = findViewById(R.id.title);
        load = findViewById(R.id.load);
        account = findViewById(R.id.account);
        postad = findViewById(R.id.postad);
        recycle = findViewById(R.id.recycle);


    }
    public Boolean isconnected() throws InterruptedException, IOException {
        // final String command ="ping -c 1 google.com";
        // return Runtime.getRuntime().exec(command).waitFor()==0;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =cm.getActiveNetworkInfo();
        return (info!=null && info.isConnectedOrConnecting());
    }
    @Override
    protected void onResume() {
        super.onResume();
        String temp;
        temp = sp.getString("name","ss");
        if(temp.equalsIgnoreCase("name")){
            finish();
        }
    }
    void openup(){
        Intent intent = new Intent(Home_Seller.this,logout.class);
        startActivity(intent);
    }
}
