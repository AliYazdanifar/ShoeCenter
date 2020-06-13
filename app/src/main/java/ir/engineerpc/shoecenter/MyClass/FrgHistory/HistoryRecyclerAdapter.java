package ir.engineerpc.shoecenter.MyClass.FrgHistory;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.R;

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryRecyclerAdapter.Holder> {

    private List<HistoryRecyclerRowItem> list;


    public HistoryRecyclerAdapter(List<HistoryRecyclerRowItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_history_row_item, parent, false);

        return new HistoryRecyclerAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {
        try {
            if (list.get(position).getSellerDescr().contains("حال"))
                holder.cv.setBackgroundColor(G.context.getResources().getColor(R.color.warning));
//        if (list.get(position).getSellerDescr().contains("موفق"))
//            holder.cv.setBackgroundColor(G.context.getResources().getColor(R.color.success));
//        if (list.get(position).getSellerDescr().contains("لغو"))
//            holder.cv.setBackgroundColor(G.context.getResources().getColor(R.color.danger));
//        holder.tvTitle.setText(list.get(position).getOrder().substring(0,25)+" ...");
            holder.tvTitle.setText(list.get(position).getOrder());
            String msg = "سفارشات : " + list.get(position).getOrder()
                    + "\n" + "وضعیت سفارش : " + list.get(position).getSellerDescr()
                    + "\n" + "تحویل گیرنده : " + list.get(position).getReciver()
                    + "\n" + "در آدرس : " + list.get(position).getAddress()
                    + "\n" + "شماره ی تماس ثبت شده : " + list.get(position).getTell()
                    + "\n" + "توضیحات خریدار : " + list.get(position).getCustomerDescr()
                    + "\n" + "مبلغ پرداختی : " + list.get(position).getPayment()
                    + "\n" + "کد پیگیری سفارش : " + list.get(position).getRefid()
                    + "\n" + "تاریخ ثبت سفارش : " + "\n" + list.get(position).getDate();
            holder.tvDetails.setText(msg);

//        holder.img.setImageResource(R.drawable.ic_luncher);
            holder.cl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.tvDetails.getVisibility() == View.GONE)
                        holder.tvDetails.setVisibility(View.VISIBLE);
                    else
                        holder.tvDetails.setVisibility(View.GONE);


//                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
//                alert.setTitle("وضعیت سفارش : " + list.get(position).getSellerDescr());
//                String msg = "سفارشات : " + list.get(position).getOrder()
//                        + "\n" + "تحویل گیرنده : " + list.get(position).getReciver()
//                        + "\n" + "در آدرس : " + list.get(position).getAddress()
//                        + "\n" + "شماره ی تماس ثبت شده : " + list.get(position).getTell()
//                        + "\n" + "توضیحات خریدار : " + list.get(position).getCustomerDescr()
//                        + "\n" + "مبلغ پرداختی : " + list.get(position).getPayment()
//                        + "\n" + "کد پیگیری سفارش : " + list.get(position).getRefid()
//                        + "\n" + "تاریخ ثبت سفارش : " + list.get(position).getDate();
//                alert.setMessage(msg);
//                alert.setPositiveButton("فهمیدم", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//
//                    }
//                }).show();

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
        ConstraintLayout cl;
        //        ImageView img;
        TextView tvTitle, tvDetails;


        Holder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.historycv);
            cl = itemView.findViewById(R.id.historyconstspinner);
//            img = itemView.findViewById(R.id.historyimg);
            tvTitle = itemView.findViewById(R.id.historytvtitle);
            tvDetails = itemView.findViewById(R.id.historytvdetail);
        }
    }
}
