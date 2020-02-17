package infoaryan.in.vegmet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class Adapterc extends PagerAdapter {
    Context ctx;
    String Title[];
    int resources[];
    Adapterc(Context mcontext,String tite[],int res[]){
        ctx=mcontext;
        Title=new String[4];
        Title=tite;
        resources = new int[4];
        resources=res;

    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view= LayoutInflater.from(ctx).inflate(R.layout.sample_viewpager_card,container,false);
        TextView tf= view.findViewById(R.id.sample_text);
        ImageView in=view.findViewById(R.id.sample_image);
        tf.setText(Title[position]);
        in.setImageResource(resources[position]);


        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
       container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return resources.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==object);
    }
}
