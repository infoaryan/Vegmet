package infoaryan.in.vegmet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;


public class viewpagerc extends AppCompatActivity {
    ViewPager viewpager;
    String title[];
    int res[];
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpagerc);

        skip = findViewById(R.id.skip);
        title = new String[4];
        res = new int[4];
        title[0]="Get Only Fresh Direct from farm!!";
                title[1]="Get to know Vendors near you";
                        title[2]="Don't Mess up the work for vegetables";
        title[3]="Get the value for the money!";
        res[0]=R.drawable.veggie;
        res[1]=R.drawable.kk;
        res[2]=R.drawable.pngtree;
        res[3]=R.drawable.casino;

        //setting viewpager and the Dots indicator
        viewpager= findViewById(R.id.viewpagerc);
        Adapterc adapter = new Adapterc(this,title,res);
        viewpager.setAdapter(adapter);
        DotsIndicator dotsIndicator = findViewById(R.id.dots_indicator);
        dotsIndicator.setViewPager(viewpager);
        dotsIndicator.setVisibility(View.VISIBLE);




        //code for viewpager page change and visibility of the button
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0){
                    skip.setVisibility(View.INVISIBLE);


                }
                else{
                    skip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //code for the next page

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getApplicationContext(),Registration.class);
                intent.putExtra("state","consumer");
                startActivity(intent);
            }
        });


    }
}
