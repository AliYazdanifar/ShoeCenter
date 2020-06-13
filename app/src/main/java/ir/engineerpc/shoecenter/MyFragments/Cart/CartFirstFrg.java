package ir.engineerpc.shoecenter.MyFragments.Cart;

import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ir.engineerpc.shoecenter.CartActivity;
import ir.engineerpc.shoecenter.MyClass.Cart.CartRecyclerAdapter;
import ir.engineerpc.shoecenter.MyClass.Cart.CartRecyclerRowItem;
import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.MyDatabaseManager;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class CartFirstFrg extends Fragment {


    MyDatabaseManager myDatabaseManager;
    int endPrice = 0;
    View view;
    RecyclerView recyclerView;
    TextView tvEndP;
    List<CartRecyclerRowItem> list;
    Button btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_cart_first_frg, container, false);

        init();


        if (((CartActivity) getActivity()).widthGTHeight()) {
            GridLayoutManager manager = new GridLayoutManager(G.context, 2);
            recyclerView.setLayoutManager(manager);

        } else {
            GridLayoutManager manager = new GridLayoutManager(G.context, 1);
            recyclerView.setLayoutManager(manager);
        }


        CartRecyclerAdapter adapter = new CartRecyclerAdapter(getActivity(), list, tvEndP);
        recyclerView.setAdapter(adapter);

//

        NumberFormat formatter = new DecimalFormat("#,###");

        tvEndP.setText("قیمت کل " + formatter.format(endPrice) + " تومان");



        activityBtn();

        endPrice=0;

        return view;
    }

    private void init() {
        myDatabaseManager=new MyDatabaseManager(getContext());
        recyclerView = view.findViewById(R.id.recycler_for_all);
        btnNext = getActivity().findViewById(R.id.cartbtnContinue);
        tvEndP = getActivity().findViewById(R.id.carttvendprice);


        /*
        get and initialize all data from database to Cart recyclerView
        */
        Cursor res = myDatabaseManager.easyDB.getAllData();
        list = new ArrayList<>();
        if (res.getCount()!=0) {


            View nothing = view.findViewById(R.id.cartnothing);
            nothing.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            tvEndP.setVisibility(View.VISIBLE);

            while (res.moveToNext()) {

                int id = res.getInt(MyDatabaseManager.CLMN_ID);
                String title = res.getString(MyDatabaseManager.CLMN_TITLE);
                String img = res.getString(MyDatabaseManager.CLMN_IMG);
                String color = res.getString(MyDatabaseManager.CLMN_COLOR);
                String price = res.getString(MyDatabaseManager.CLMN_PRICE);
                String amount = res.getString(MyDatabaseManager.CLMN_AMOUNT);

                endPrice += Integer.valueOf(price);
                SharedPref.saveString(G.context,"endprice",String.valueOf(endPrice));
                String size = res.getString(MyDatabaseManager.CLMN_SIZE);
                String pdId = res.getString(MyDatabaseManager.CLMN_PDID);
                CartRecyclerRowItem rowItem = new CartRecyclerRowItem(id, img, title, price, size, color, amount);
                list.add(rowItem);

            }
        }else {
            SharedPref.saveString(G.context,"endprice","0");
            recyclerView.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
            tvEndP.setVisibility(View.GONE);
            View nothing = view.findViewById(R.id.cartnothing);
            nothing.setVisibility(View.VISIBLE);
        }


    }

    public void activityBtn() {
        final Button btnPrev = getActivity().findViewById(R.id.cartbtnBack);
        final ProgressBar progressBar = getActivity().findViewById(R.id.cartprogressBar);
        btnPrev.setVisibility(View.INVISIBLE);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPref.saveString(G.context, "payment", String.valueOf(endPrice));
                btnNext.setText("ادامه");
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,
                        R.anim.slide_in_back_from_right, R.anim.slide_out_back_to_left);
                ft.replace(R.id.cartFrmLyt, new CartGetInfoFrg()).addToBackStack(null);
                ft.commit();
                progressBar.setProgress(50);
                btnPrev.setVisibility(View.VISIBLE);
            }
        });


    }


}
