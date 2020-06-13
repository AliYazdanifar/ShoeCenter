package ir.engineerpc.shoecenter.MyClass.FrgMain;

import android.content.Context;
import android.content.Intent;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import ir.engineerpc.shoecenter.R;
import ir.engineerpc.shoecenter.ProductTypesActivity;

public class MainFrgRecyclerAdapter extends RecyclerView.Adapter<MainFrgRecyclerAdapter.MainFrgViewHolders> {

    private Context context;
    private List<MainFrgRecyclerRowItems> itemList;

    public MainFrgRecyclerAdapter( Context context, List<MainFrgRecyclerRowItems> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public MainFrgViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyceler_mainfrg_row_layout, parent, false);

        return new MainFrgViewHolders(view);
    }

    @Override
    public void onBindViewHolder(final MainFrgViewHolders holder, final int position) {

//        try {

            holder.tvTitle.setText(itemList.get(position).getTvTitle());

        Glide.with(context)
                .load(itemList.get(position).getImgUrl())
                .error(R.drawable.camera_off)
                .placeholder(R.drawable.progress_clock)
                .into(holder.bgImg);
//
//            Picasso.with(context)
//                    .load(itemList.get(position).getImgUrl())
//                    .error(R.drawable.bbbbbb)
//                    .placeholder(R.drawable.progress_clock)
//                    .into(holder.bgImg);
//        new Font(context,holder.tvTitle,"n.ttf");
            holder.tvDetail.setText(itemList.get(position).getTvDetails());

            holder.cvContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProductTypesActivity.class);
                    intent.putExtra("pdid", itemList.get(position).getPdid());
                    intent.putExtra("cat_name", itemList.get(position).getCatName());
                    context.startActivity(intent);
                }
            });


//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(context, "adapter Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class MainFrgViewHolders extends RecyclerView.ViewHolder {

        ImageView bgImg;
        TextView tvTitle, tvDetail;
        CardView cvContainer;

        public MainFrgViewHolders(View itemView) {

            super(itemView);
            bgImg = (ImageView) itemView.findViewById(R.id.mainfrgimgview);
            tvTitle = (TextView) itemView.findViewById(R.id.mainfrgtxttitle);
            tvDetail = (TextView) itemView.findViewById(R.id.mainfrgtxtdetails);
            cvContainer = (CardView) itemView.findViewById(R.id.mainfrgcardview);
        }
    }

}
