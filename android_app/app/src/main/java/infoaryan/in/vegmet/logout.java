package infoaryan.in.vegmet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class logout extends AppCompatActivity {
TextView logout;
SharedPreferences sp;
SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);


        logout = findViewById(R.id.logout);

        sp = getSharedPreferences("main",MODE_PRIVATE);
        editor = sp.edit();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putBoolean("signed",false);
                editor.putString("name","name");
                editor.putString("phone","phone");
                editor.putString("location","location here");
                editor.commit();
                Intent intent = new Intent(logout.this,Seller_Or_Buyer.class);
                startActivity(intent);
             finish();
            }
        });
    }
}
