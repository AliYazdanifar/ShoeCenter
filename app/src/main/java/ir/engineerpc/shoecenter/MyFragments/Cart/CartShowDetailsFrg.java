package ir.engineerpc.shoecenter.MyFragments.Cart;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.zarinpal.ewallets.purchase.OnCallbackRequestPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import ir.engineerpc.shoecenter.LoginActivity;
import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.MyDatabaseManager;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartShowDetailsFrg extends Fragment {

    MyDatabaseManager myDatabaseManager;
    TextView tvOrder, tvNameandFamily, tvAddress, tvTellAndMail, tvEndPrice, tvDescr;
    String payment;
    long endPayment;
    NumberFormat formatter;
    TextView tvEndP;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart_show_details, container, false);


        init();

        Button btnPayment = getActivity().findViewById(R.id.cartbtnContinue);
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!SharedPref.loadString(G.context, "login").equals("logedin")) {
                    new MaterialStyledDialog.Builder(getActivity())
                            .setHeaderColor(R.color.my)
                            .setStyle(Style.HEADER_WITH_TITLE)
                            .setTitle("ورود به برنامه")
                            .setDescription(getResources().getString(R.string.msg_login))
                            .setPositiveText("ورود")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Intent myintent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(myintent);
                                }
                            })

                            .show();
                } else {

                    SharedPref.saveString(G.context, "tvorder", tvOrder.getText().toString());
                    SharedPref.saveString(G.context, "tvdescr", tvDescr.getText().toString());
                    myPayment();
                }
            }
        });


        return view;
    }


    @Override
    public void onStop() {
        endPayment = Integer.valueOf(payment);
        tvEndP.setText("قیمت کل " + formatter.format(endPayment) + " تومان");
        super.onStop();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void init() {
        tvOrder = view.findViewById(R.id.cartshowdetailtvorders);
        tvNameandFamily = view.findViewById(R.id.cartshowdetailtvnamefamily);
        tvAddress = view.findViewById(R.id.cartshowdetailtvaddress);
        tvTellAndMail = view.findViewById(R.id.cartshowdetailtvtellandemail);
        tvEndPrice = view.findViewById(R.id.txtcshodetails);
        tvDescr = view.findViewById(R.id.cartshowdetailtvdescr);
        myDatabaseManager = new MyDatabaseManager(getContext());
        Cursor res = myDatabaseManager.easyDB.getAllData();

        StringBuilder title = new StringBuilder();
        while (res.moveToNext()) {

            title.append(res.getString(MyDatabaseManager.CLMN_TITLE)
                    + " - رنگ: " + res.getString(MyDatabaseManager.CLMN_COLOR)
                    + " - سایز: " + res.getString(MyDatabaseManager.CLMN_SIZE)
                    + " - تعداد: " + res.getString(MyDatabaseManager.CLMN_AMOUNT) + "\n");

        }
        tvOrder.setText(title + "\n");
        tvNameandFamily.setText(SharedPref.loadString(G.context, "edtnameandfamily"));
        tvAddress.setText(SharedPref.loadString(G.context, "fulladdress"));
        tvTellAndMail.setText(SharedPref.loadString(G.context, "edttell") +
                "\n" + SharedPref.loadString(G.context, "edtemail"));
        tvDescr.setText(SharedPref.loadString(G.context, "edtdescr"));

        tvEndP = getActivity().findViewById(R.id.carttvendprice);
        payment = SharedPref.loadString(G.context, "endprice");

        formatter = new DecimalFormat("#,###");

        //        if (a.isEmpty()) {
        endPayment = Integer.valueOf(payment) + Integer.valueOf(SharedPref.loadString(G.context, "postcost"));
        String text = "قیمت کل = مجموع اجناس خریداری شده + هزینه ی ارسال"
                + "\nقیمت کل : " + formatter.format(Integer.valueOf(payment))
                + " + " + formatter.format(Integer.valueOf(SharedPref.loadString(G.context, "postcost")))
                + " = " + formatter.format(endPayment) + " تومان ";
        tvEndPrice.setText(text);
        tvEndP.setText("قیمت کل " + formatter.format(endPayment) + " تومان");
//        }
    }


    ///////////////////////////////payment zariinpal
    private void myPayment() {
        try {
            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("لطفا صبر کنید...");
            pd.setCancelable(false);
            pd.show();
            ZarinPal purchase = ZarinPal.getPurchase(getActivity());
            PaymentRequest payment = ZarinPal.getPaymentRequest();

            payment.setMerchantID("116011b0-07e5-11e8-aa38-000c295eb8fc");

            payment.setAmount(endPayment);
            payment.setAuthority("آدینه تک");
            payment.setDescription(" خرید از تولیدی آدینه");
            payment.setCallbackURL("return://zarinpalpayment");

//
            payment.setEmail(SharedPref.loadString(G.context, "edtemail"));
            payment.setMobile(SharedPref.loadString(G.context, "edttell"));

            purchase.startPayment(payment, new OnCallbackRequestPaymentListener() {
                @Override
                public void onCallbackResultPaymentRequest(int status, String authority, Uri paymentGatewayUri, Intent intent) {
                    if (status == 100) {
                        pd.dismiss();
                        startActivity(intent);
                    } else {
                        pd.dismiss();
                        Toast.makeText(G.context, "err " + status, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(G.context, "Bug......", Toast.LENGTH_SHORT).show();
            Log.i("ALI", "bug in try in mypayment Method = " + e.getMessage());
        }

    }

}
