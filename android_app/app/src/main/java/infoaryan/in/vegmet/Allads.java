package infoaryan.in.vegmet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Allads extends AppCompatActivity {
    Button back;
    RecyclerView recycle;
    productadapter pp;
    List<Product> productList;
    String title,desc;
    int price;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allads);

        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait....");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //checking the connection tot the server
        try {
            if(!isconnected()){
                Toast.makeText(this,"Check the network!!",Toast.LENGTH_LONG).show();
                finish();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        progress.show();
        progress.setCancelable(false);
        //the block for the functionality of the back button
        { back =findViewById(R.id.back);
        recycle = findViewById(R.id.recycle);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Allads.this,Home_Consumer.class);
                startActivity(intent);
                finish();
            }
        });}

       productList = new ArrayList<>();
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        loadproducts();

    }

    void loadproducts(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://smellier-men.000webhostapp.com/v1/getads.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for(int i=0;i<array.length();++i){
                                JSONObject object = array.getJSONObject(i);

                                title = object.getString("title");
                                desc = object.getString("desc");
                                price = object.getInt("price");
                                Product product = new Product(title,desc,price);
                                productList.add(product);
                            }
                            pp = new productadapter(Allads.this,productList);
                            recycle.setAdapter(pp);
                            progress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Allads.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

       RequestHandler.getInstance(Allads.this).addToRequestQueue(stringRequest);
    }
    public Boolean isconnected() throws InterruptedException, IOException {
        // final String command ="ping -c 1 google.com";
        // return Runtime.getRuntime().exec(command).waitFor()==0;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info =cm.getActiveNetworkInfo();
        return (info!=null && info.isConnectedOrConnecting());
    }
}
