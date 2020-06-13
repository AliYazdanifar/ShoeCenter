package ir.engineerpc.shoecenter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zarinpal.ewallets.purchase.OnCallbackVerificationPaymentListener;
import com.zarinpal.ewallets.purchase.PaymentRequest;
import com.zarinpal.ewallets.purchase.ZarinPal;

import java.util.HashMap;
import java.util.Map;

import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.MyDatabaseManager;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.MyFragments.Cart.CartFirstFrg;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    FragmentTransaction ft;
    Button btnNext, btnPrev;
    ProgressBar progressBar;
    MyDatabaseManager myDatabaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        try {
            init();

            toolBarCodes();

            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

            Uri uri = getIntent().getData();

            ZarinPal.getPurchase(G.context).verificationPayment(uri, new OnCallbackVerificationPaymentListener() {
                @Override
                public void onCallbackResultVerificationPayment(boolean isPaymentSuccess, final String refID, final PaymentRequest paymentRequest) {
                    if (isPaymentSuccess) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(CartActivity.this);
                        alert.setTitle(refID);
                        alert.setMessage("aa = " + paymentRequest.getAmount() + " bb = " + paymentRequest.getAuthority() + " cc = " + paymentRequest.getMerchantID());
                        alert.setPositiveButton("بستن", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                final ProgressDialog pd = new ProgressDialog(CartActivity.this);
                                pd.setMessage("کمی صبر کنید...");
                                pd.show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, G.userCommunicationUrl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        pd.dismiss();
                                        if (response.equals("ok")) {
                                            Intent intent = new Intent(CartActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            myDatabaseManager.easyDB.deleteAllDataFromTable();
                                            finish();
                                        } else {
                                            Toast.makeText(G.context, "خطایی رخ داده لطفا مجددا تلاش کنید", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        showDialogNoInternet();
                                        pd.dismiss();
                                    }
                                }) {
                                    Map<String, String> map = new HashMap<>();

                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {

                                        map.put("op", "order");
                                        map.put("nf", SharedPref.loadString(G.context, "edtnameandfamily"));
                                        map.put("ad", SharedPref.loadString(G.context, "fulladdress"));
                                        map.put("tell", SharedPref.loadString(G.context, "edttell"));
                                        map.put("mail", SharedPref.loadString(G.context, "edtmail"));
                                        map.put("order", SharedPref.loadString(G.context, "tvorder"));
                                        map.put("descr", SharedPref.loadString(G.context, "tvdescr"));
                                        map.put("payment", String.valueOf(paymentRequest.getAmount()));
                                        map.put("merchantid", String.valueOf(paymentRequest.getMerchantID()));
                                        map.put("refid", String.valueOf(refID));
                                        map.put("userid", SharedPref.loadString(G.context, "userid"));
                                        return map;
                                    }
                                };
                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                RequestQueue queue = Volley.newRequestQueue(G.context);
                                queue.add(stringRequest);
                            }
                        });
                        alert.show();

                    } else
                        Toast.makeText(G.context, "خطایی در پرداخت مجددا تلاش کنید.", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(G.context, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        myDatabaseManager = new MyDatabaseManager(getApplicationContext());
        btnNext = findViewById(R.id.cartbtnContinue);
        btnPrev = findViewById(R.id.cartbtnBack);
        progressBar = findViewById(R.id.cartprogressBar);
        getFragment();
    }

    public void toolBarCodes() {
        ImageView imgBack = findViewById(R.id.arrowback);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getFragment() {

        ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,
                R.anim.slide_in_back_from_right, R.anim.slide_out_back_to_left);
        ft.replace(R.id.cartFrmLyt, new CartFirstFrg());
        ft.commit();
        btnPrev.setVisibility(View.INVISIBLE);
        progressBar.setProgress(2);

    }

    public boolean widthGTHeight() {
        boolean a;
        Display display = getWindowManager().getDefaultDisplay();
        float height = display.getHeight();
        float width = display.getWidth();
        if (width > height)
            a = true;

        else
            a = false;


        return a;
    }

    @Override
    public void onBackPressed() {

        FragmentManager manager = CartActivity.this.getSupportFragmentManager();
        if (manager.getBackStackEntryCount() == 1) {
            btnNext.setText("ادامه");
            progressBar.setProgress(2);

            btnPrev.setVisibility(View.INVISIBLE);
        } else if (manager.getBackStackEntryCount() == 2) {
            btnNext.setText("ادامه");
            progressBar.setProgress(50);
        } else {
            super.onBackPressed();
        }
        manager.popBackStack();
    }

    public void showDialogNoInternet() {

        final Dialog dialog = new Dialog(CartActivity.this);

        View view = getLayoutInflater().inflate(R.layout.no_internet_layout, null);

        dialog.setContentView(view);

        Button btnRetry = view.findViewById(R.id.nointernetbtnretry);
        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
