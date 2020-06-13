package ir.engineerpc.shoecenter.MyClass.ViewPd;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.florent37.viewanimator.ViewAnimator;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.R;
import ir.engineerpc.shoecenter.ShowPDetailsActivity;

public class ViewPdRecyclerAdapter extends RecyclerView.Adapter<ViewPdRecyclerAdapter.ProductViewHolders> {

    private Context mContext;
    private List<ViewPdRecyclerRowItems> mProductList;

    public ViewPdRecyclerAdapter(Context mContext, List<ViewPdRecyclerRowItems> mProductList) {
        this.mContext = mContext;
        this.mProductList = mProductList;
    }

    @Override
    public ProductViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_viewpd_row_item_layout, parent, false);
        return new ProductViewHolders(mView);
    }

    @Override
    public void onBindViewHolder(ProductViewHolders holder, final int position) {

        try {
            setAnimation(holder.mCardView, position);

            if (mProductList.get(position).getOff()>0){
                holder.tvBadgeOff.setVisibility(View.VISIBLE);
                String text=mProductList.get(position).getOff()+"% off";
                holder.tvBadgeOff.setText(text);

            }

            Glide.with(mContext)
                    .load(mProductList.get(position).getImg1())
                    .error(R.drawable.camera_off)
                    .placeholder(R.drawable.progress_clock)
                    .into(holder.mImage);
            holder.mTitle.setText(mProductList.get(position).getProductTitle());
            holder.ratingBar.setRating(mProductList.get(position).getRate());

            NumberFormat formatter = new DecimalFormat("#,###");
            int a = Integer.valueOf(mProductList.get(position).getProductPrice().replaceAll("[^0-9]", ""));

            final int afterOff = a - ((Integer.valueOf(a) * mProductList.get(position).getOff()) / 100);

            holder.mPrice.setText(formatter.format(afterOff) + " ت");


            holder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent inten = new Intent(mContext, ShowPDetailsActivity.class);
                    inten.putExtra("id", mProductList.get(position).getId());
                    inten.putExtra("title", mProductList.get(position).getProductTitle());
                    inten.putExtra("madein", mProductList.get(position).getMadeIn());
                    inten.putExtra("price", mProductList.get(position).getPrice());
                    inten.putExtra("amount", mProductList.get(position).getAmount());
                    inten.putExtra("rate", mProductList.get(position).getRate());
                    inten.putExtra("img1", mProductList.get(position).getImg1());
                    inten.putExtra("img2", mProductList.get(position).getImg2());
                    inten.putExtra("img3", mProductList.get(position).getImg3());
                    inten.putExtra("img4", mProductList.get(position).getImg4());
                    inten.putExtra("img5", mProductList.get(position).getImg5());
                    inten.putExtra("descr", mProductList.get(position).getDescription());
                    inten.putExtra("off", mProductList.get(position).getOff());
                    inten.putExtra("size", mProductList.get(position).getSize());
                    inten.putExtra("colors", mProductList.get(position).getColors());
                    inten.putExtra("pdid", mProductList.get(position).getPdId());
                    mContext.startActivity(inten);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(G.context, "adapter Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    class ProductViewHolders extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mTitle, mPrice, tvBadgeOff;
        CardView mCardView;
        RatingBar ratingBar;

        ProductViewHolders(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.ivImage);
            mTitle = itemView.findViewById(R.id.tvTitle);
            tvBadgeOff = itemView.findViewById(R.id.tvbadgeoff);
            mPrice = itemView.findViewById(R.id.tvPrice);
            ratingBar = itemView.findViewById(R.id.viewpdrating);
            mCardView = itemView.findViewById(R.id.cardviewproduct);

        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        int lastPosition = -1;
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ViewAnimator
                    .animate(viewToAnimate)
                    .bounceIn()
                    .duration(1000)
                    .start();
//            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.md_styled_zoom_in);
//            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
