package ir.engineerpc.shoecenter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.CustomDialogs.NoInternetDialog;
import ir.engineerpc.shoecenter.MyClass.ViewPd.ViewPdRecyclerAdapter;
import ir.engineerpc.shoecenter.MyClass.ViewPd.ViewPdRecyclerRowItems;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductTypesActivity extends AppCompatActivity {

    GridLayoutManager mGridLayoutManager;
    List<ViewPdRecyclerRowItems> mProductList;
    ViewPdRecyclerAdapter viewPdRecyclerAdapter;
    RecyclerView mRecyclerView;

    int pdId;
    int oldid = -2, id = -1;
    RecyclerView rv;
    boolean loading = true;
    String catName;

    NoInternetDialog noInternetDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_types);
        try {
            init();

            Bundle bundle = getIntent().getExtras();
            pdId = bundle.getInt("pdid");
            catName = bundle.getString("cat_name");
            Log.i("ALI", String.valueOf(pdId));
            toolBarCodes();

            getProducts(id);

            rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    final int totalItemCount = mGridLayoutManager.getItemCount();
                    final int visibleItemCount = mGridLayoutManager.getChildCount();
                    final int firstVisibleItem = mGridLayoutManager.findFirstVisibleItemPosition();

//                    Log.i("ALI","ttic = "+totalItemCount+"  vic = "+visibleItemCount+"  fvi="+firstVisibleItem);

//                    if (loading) {
//                        if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
////                            if (id < 10)
//                            Log.i("ALI", "Last Item Wow !" + " id=" + id);
//                            getProducts(id);
//                        }
//                    }
//                    if (!mLoadingStarted && (totalItemCount - visibleItemCount <= firstVisibleItem)) {
//                        mLoadingStarted = true;
//
//                        // fetch more data
//                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(G.context, "Error", Toast.LENGTH_SHORT).show();
        }
    }

    private void init() {
        mRecyclerView = findViewById(R.id.precyclerView);

        mProductList = new ArrayList<>();

        viewPdRecyclerAdapter = new ViewPdRecyclerAdapter(ProductTypesActivity.this, mProductList);
        mGridLayoutManager = new GridLayoutManager(ProductTypesActivity.this, 2);
        mRecyclerView.setAdapter(viewPdRecyclerAdapter);

        noInternetDialog = new NoInternetDialog(ProductTypesActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noInternetDialog.dismiss();
                getProducts(id);
            }
        });

        rv = findViewById(R.id.precyclerView);
    }

    public void toolBarCodes() {
        ImageView imgBack = findViewById(R.id.arrowbackmain);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getProducts(final int endid) {

        final ProgressDialog pd = new ProgressDialog(ProductTypesActivity.this);
        pd.setMessage("کمی صبر کنید");
        pd.show();

        mRecyclerView.setLayoutManager(mGridLayoutManager);


        StringRequest request = new StringRequest(Request.Method.POST, G.productsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        id = object.getInt("id");


                        String title = object.getString("title");
                        String madein = object.getString("madein");
                        String price = object.getString("price");
                        int amount = object.getInt("amount");
                        int rate = object.getInt("rate");
                        String img1 = object.getString("img1");
                        String img2 = object.getString("img2");
                        String img3 = object.getString("img3");
                        String img4 = object.getString("img4");
                        String img5 = object.getString("img5");
                        String descr = object.getString("description");
                        int off = object.getInt("off");
                        String size = object.getString("size");
                        String colors = object.getString("colors");
                        int productid = object.getInt("productid");
                        ViewPdRecyclerRowItems mProductData = new ViewPdRecyclerRowItems(id, title, madein, price, amount, rate, img1, img2, img3, img4, img5, descr, off, size, colors, productid);
                        mProductList.add(mProductData);
                    }

                    viewPdRecyclerAdapter.notifyItemInserted(mProductList.size() - 1);


                } catch (Exception e) {
                    pd.dismiss();
                    noInternetDialog.show();
                    Log.i("ALI", "go" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                noInternetDialog.show();
            }
        }) {
            Map<String, String> map = new HashMap<>();

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.put("op", "getpdtypes");
                map.put("pdid", String.valueOf(pdId));
                map.put("cat", catName);
                map.put("endid", String.valueOf(endid));
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(G.context);
        queue.add(request);

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
}
