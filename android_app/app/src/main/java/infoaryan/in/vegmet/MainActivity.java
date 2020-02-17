package infoaryan.in.vegmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences("main",MODE_PRIVATE);



        LinearLayout linearLayout = findViewById(R.id.linearlayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1200);
        animationDrawable.setExitFadeDuration(900);
        animationDrawable.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getother();
            }
        },2400);

    }
    void getother(){
       if(!(sp.getBoolean("signed",false))){
           Intent intent = new Intent(MainActivity.this,Seller_Or_Buyer.class);
           startActivity(intent);
           finish();
       }
       else{
           if(sp.getString("state","dd").equalsIgnoreCase("consumer")){
               Intent intent = new Intent(MainActivity.this,Home_Consumer.class);
               startActivity(intent);
               finish();
           }
           else{
               Intent intent = new Intent(MainActivity.this,Home_Seller.class);
               startActivity(intent);
               finish();
           }
       }
    }
}
