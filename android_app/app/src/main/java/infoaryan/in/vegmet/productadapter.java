package infoaryan.in.vegmet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class productadapter extends RecyclerView.Adapter<productadapter.Viewholder> {
     private Context ctx;
     private List<Product> productList;

    public productadapter(Context ctx, List<Product> productList) {
        this.ctx = ctx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.additem,null);
        return(new Viewholder(view));

    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        Product product = productList.get(position);
        holder.desc.setText(product.getDesc());
        holder.price.setText("Rs. "+product.getPrice());
        holder.title.setText(product.getTitle());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title,desc,price;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            price = itemView.findViewById(R.id.price);
        }
    }
}
