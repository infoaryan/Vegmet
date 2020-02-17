package infoaryan.in.vegmet;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Home_Consumer extends AppCompatActivity  {
    TextView namehere,locationhere,fastlane;
    ImageView veg,fruit,fav,seasonal,fresh,near,account;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_consumer);

        sp = getSharedPreferences("main",MODE_PRIVATE);
        findviews();

        //
        Toast.makeText(Home_Consumer.this,"click on fastlane to browse...",Toast.LENGTH_LONG).show();
        //on click listeners for the account retrieval
        {
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
        fastlane.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startActivity(new Intent(Home_Consumer.this,Allads.class)); }
        });
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


    void findviews(){
        namehere = findViewById(R.id.namehere);
        locationhere = findViewById(R.id.locationhere);
        fastlane = findViewById(R.id.fastlane);
        account = findViewById(R.id.account);
        veg = findViewById(R.id.cat_veg);
        fruit = findViewById(R.id.cat_fruit);
        fav = findViewById(R.id.cat_favorites);
        seasonal = findViewById(R.id.cat_seasonal);
        near = findViewById(R.id.cat_nearest);
        fresh = findViewById(R.id.cat_fresh);

        namehere.setText(sp.getString("name","hh "));
        locationhere.setText(sp.getString("location"," hh"));
    }

    void openup(){
        Intent intent = new Intent(Home_Consumer.this,logout.class);
        startActivity(intent);
    }
}
