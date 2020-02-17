package infoaryan.in.vegmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Seller_Or_Buyer extends AppCompatActivity {
    Button seller,consumer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller__or__buyer);




        findviews();

        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Seller_Or_Buyer.this,viewpagers.class);
                startActivity(intent);
            }
        });

        consumer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Seller_Or_Buyer.this,viewpagerc.class);
                startActivity(intent);
            }
        });
    }

    void findviews(){
        seller = findViewById(R.id.sellerbutton);
        consumer = findViewById(R.id.consumerbutton);
    }
}
