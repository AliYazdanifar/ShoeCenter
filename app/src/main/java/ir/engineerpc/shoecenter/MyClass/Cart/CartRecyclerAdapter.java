package ir.engineerpc.shoecenter.MyClass.Cart;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.MyDatabaseManager;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.R;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CartRecyclerViewHolder> {
    private Context mContext;
    private List<CartRecyclerRowItem> mList;
    private TextView activityTvEndPrice;
    private long endPrice = 0;
    public NumberFormat formatter;
    MyDatabaseManager myDatabaseManager;

    public CartRecyclerAdapter(Context mContext, List<CartRecyclerRowItem> mList, TextView activityTvEndPrice) {
        this.mContext = mContext;
        this.mList = mList;
        this.activityTvEndPrice = activityTvEndPrice;
        myDatabaseManager=new MyDatabaseManager(mContext);
    }

    @Override
    public CartRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cart_row_item_layout, parent, false);
        return new CartRecyclerAdapter.CartRecyclerViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final CartRecyclerViewHolder holder, final int position) {
        try {
            Glide.with(G.context)
                    .load(mList.get(position).getImage())
                    .error(R.drawable.camera_off)
                    .placeholder(R.drawable.progress_clock)
                    .into(holder.cImage);


            int pr = Integer.valueOf(mList.get(position).getPrice().replaceAll("[^0-9]", ""));
            int am = Integer.valueOf(mList.get(position).getAmount().replaceAll("[^0-9]", ""));
            endPrice += pr * am;
            formatter = new DecimalFormat("#,###");

//        activityTvEndPrice.setText(formatter.format(endPrice));

            holder.cTitle.setText(mList.get(position).getTitle());
            holder.cDetails.setText("رنگ " + mList.get(position).getColor() + "\n" + "سایز " + mList.get(position).getSize() + "\n" + "تعداد " + mList.get(position).getAmount());
            holder.cPrice.setText(formatter.format(Integer.valueOf(mList.get(position).getPrice().replaceAll("[^0-9]", ""))));


            holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mList.size());
                    endPrice = Integer.valueOf(SharedPref.loadString(G.context, "endprice")) -
                            Integer.valueOf(mList.get(position).getPrice().replaceAll("[^0-9]", ""));
                    SharedPref.saveString(G.context, "endprice", String.valueOf(endPrice));
                    activityTvEndPrice.setText("قیمت کل " + formatter.format(endPrice) + " تومان");
                    myDatabaseManager.easyDB.deleteRow(mList.get(position).getId());
                    mList.remove(position);


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(G.context, "adapter Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class CartRecyclerViewHolder extends RecyclerView.ViewHolder {

        ImageView cImage;
        TextView cTitle, cDetails, cPrice;
        Button btnDelete;

        CartRecyclerViewHolder(View itemView) {
            super(itemView);

            cImage = itemView.findViewById(R.id.cartimageViewRight);
            cTitle = itemView.findViewById(R.id.carttvtitle);
            cDetails = itemView.findViewById(R.id.carttvdetail);
            cPrice = itemView.findViewById(R.id.carttvprice);
            btnDelete = itemView.findViewById(R.id.cartbtndelete);
        }
    }
}
