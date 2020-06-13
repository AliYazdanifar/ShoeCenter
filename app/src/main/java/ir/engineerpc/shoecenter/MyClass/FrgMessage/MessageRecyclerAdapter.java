package ir.engineerpc.shoecenter.MyClass.FrgMessage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.R;

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageRecyclerAdapter.Holder> {

    List<MessageRecyclerRowItem> list;
    Activity activity;

    public MessageRecyclerAdapter(Activity activity, List<MessageRecyclerRowItem> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_message_row_item_layout, parent, false);
        return new MessageRecyclerAdapter.Holder(mView);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        try {
            Picasso.with(G.context)
                    .load(list.get(position).getImg())
                    .error(R.drawable.camera_off)
                    .placeholder(R.drawable.progress_clock)
                    .into(holder.img);

            holder.tvTitle.setText(list.get(position).getTitle());

            if (!SharedPref.loadString(G.context, "readmessage" + list.get(position).getId()).equals(String.valueOf(list.get(position).getId()))) {
                holder.tvBadge.setText("New");
            } else {
                holder.tvBadge.setVisibility(View.INVISIBLE);
            }
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        SharedPref.saveString(G.context, "readmessage" + list.get(position).getId(), String.valueOf(list.get(position).getId()));
                        notifyDataSetChanged();
                        notifyItemChanged(position);

                        final Dialog dialog = new Dialog(activity);
                        final View dialogView = LayoutInflater.from(activity).inflate(R.layout.custom_msg_layout, null);
                        ImageView img = dialogView.findViewById(R.id.custom_msg_img);
                        TextView tv = dialogView.findViewById(R.id.custom_msg_txttitle);
                        Button btnShow = dialogView.findViewById(R.id.custom_msg_btnpositive);
                        Button btnCancel = dialogView.findViewById(R.id.custom_msg_btncancel);
                        Glide.with(activity)
                                .load(list.get(position).getImg())
                                .placeholder(R.drawable.progress_clock)
                                .error(R.drawable.camera_off)
                                .into(img);
                        tv.setText(list.get(position).getText());
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        btnShow.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(list.get(position).getLink()));
                                activity.startActivity(intent);
                            }
                        });
                        dialog.setContentView(dialogView);

                        dialog.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(G.context, "adapter Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView img;
        TextView tvTitle, tvBadge;


        public Holder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.messagesCardViews);
            img = itemView.findViewById(R.id.messageimg);
            tvTitle = itemView.findViewById(R.id.messagetvtitle);
            tvBadge = itemView.findViewById(R.id.messagetvbadge);

        }
    }
}
