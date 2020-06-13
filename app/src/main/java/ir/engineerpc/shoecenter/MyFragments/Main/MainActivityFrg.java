package ir.engineerpc.shoecenter.MyFragments.Main;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.github.florent37.viewanimator.ViewAnimator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ir.engineerpc.shoecenter.BuildConfig;
import ir.engineerpc.shoecenter.MyClass.CustomDialogs.NoInternetDialog;
import ir.engineerpc.shoecenter.MyClass.Font;
import ir.engineerpc.shoecenter.MyClass.FrgMain.MainFrgRecyclerAdapter;
import ir.engineerpc.shoecenter.MyClass.FrgMain.MainFrgRecyclerRowItems;
import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.OnlineClass.ConnectToNet;
import ir.engineerpc.shoecenter.MyClass.OnlineClass.ResponseFromNet;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.R;
import ir.engineerpc.shoecenter.ShowPDetailsActivity;

public class MainActivityFrg extends Fragment implements ResponseFromNet {

    private View view;
    private SliderLayout sliderShow;
    private TextSliderView textSliderView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NestedScrollView scrollView;
    private LinearLayout sliderLinear;
    private RequestQueue queue;

    private View viewLineCategory;

    public static String response;

    private NoInternetDialog noInternetDialog;

    private RecyclerView myRecycler;
    private LinearLayout linearForAllRecycler;
    private TextView tvCategory,tvVersion;
    private LinearLayout.LayoutParams layoutParams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main_activity_frg, container, false);
        try {
            init();
            reload();
            getOffProducts();

            swipeRefreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.colorPrimary));
            swipeRefreshLayout.setProgressViewEndTarget(false, 180);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getOffProducts();
                    reload();
                    swipeRefreshLayout.setRefreshing(false);

                }
            });

            scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                    if (scrollY < 10 && scrollY < oldScrollY) {
                        ViewAnimator animator = new ViewAnimator();
                        ViewAnimator.animate(sliderLinear)
                                .pulse()
                                .bounce()
                                .duration(400)
                                .start();
                        animator.cancel();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(G.context, "Error", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void init() {
        linearForAllRecycler = view.findViewById(R.id.linear_frg_main_recycler);

        sliderLinear = view.findViewById(R.id.mainfrgsliderlinear);
        scrollView = view.findViewById(R.id.mainfrgnestedsv);
        tvVersion = view.findViewById(R.id.main_frg_tv_version);
        String versionName ="نسخه ی "+ BuildConfig.VERSION_NAME;
        tvVersion.setText(versionName);
        sliderShow = view.findViewById(R.id.slidermain);
        swipeRefreshLayout = getActivity().findViewById(R.id.mainswipe);
        queue = Volley.newRequestQueue(G.context);

        noInternetDialog = new NoInternetDialog(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reload();
                noInternetDialog.dismiss();
            }
        });

        SharedPref.saveInt(G.context, "postpayment", 10000);
    }

    private void setRowOfMainRecycler(final String cat) {

        try {
            final List<MainFrgRecyclerRowItems> list = new ArrayList<>();

            StringRequest request = new StringRequest(Request.Method.POST, G.productsUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        int id;
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            id = object.getInt("id");
                            String title = object.getString("title");
                            String detail = object.getString("description");
                            String img = object.getString("img");
                            int pdid = object.getInt("pdid");
                            String catName = object.getString("cat");

                            MainFrgRecyclerRowItems rowItems = new MainFrgRecyclerRowItems(id, img, title, detail, id, catName);
                            list.add(rowItems);
                        }

                        MainFrgRecyclerAdapter adapter = new MainFrgRecyclerAdapter(getActivity(), list);

                        tvCategory = new TextView(getActivity());
                        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(20, 0, 20, 0);
                        tvCategory.setText(cat);
                        tvCategory.setTextSize(20);
                        tvCategory.setLayoutParams(layoutParams);
                        new Font(G.context, tvCategory, "Ebhaar.otf");
                        linearForAllRecycler.addView(tvCategory);

                        myRecycler = new RecyclerView(G.context);
                        myRecycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        myRecycler.setLayoutManager(new GridLayoutManager(G.context, 1, GridLayoutManager.HORIZONTAL, true));
                        myRecycler.setAdapter(adapter);
                        linearForAllRecycler.addView(myRecycler);

                        viewLineCategory = new View(G.context);
                        layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5);
                        layoutParams.setMargins(0, 20, 0, 18);
                        viewLineCategory.setLayoutParams(layoutParams);
                        viewLineCategory.setBackgroundColor(getResources().getColor(R.color.gray));

                        linearForAllRecycler.addView(viewLineCategory);

                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Error in response" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                pb.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "خطا در برقراری ارتباط با اینترنت", Toast.LENGTH_SHORT).show();
                }
            }) {
                Map<String, String> map = new HashMap<>();

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    map.put("op", "getmainproducts");
                    map.put("cat_name", cat);
                    return map;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(request);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "error in catch", Toast.LENGTH_SHORT).show();
        }
    }

    private void reload() {
        ConnectToNet connect = new ConnectToNet(getActivity(), new ResponseFromNet() {
            @Override
            public void onRequestSuccess(String response) {
                try {
                    linearForAllRecycler.removeAllViews();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String catName = object.getString("cat_name");
                        setRowOfMainRecycler(catName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestError(String errorMessage) {

            }
        }, Request.Method.POST, G.productsUrl, "get_cat");
        connect.getFromNet();

    }

    private void getOffProducts() {
        sliderShow.removeAllSliders();
        ConnectToNet connectToNet = new ConnectToNet(getActivity(), this, Request.Method.POST, G.productsUrl, "getoffpd", "pdid", "1");

        connectToNet.getFromNet();

    }

    @Override
    public void onRequestSuccess(String response) {
//        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                final int off = object.getInt("off");

                final int id = object.getInt("id");
                final String title = object.getString("title");
                final String madein = object.getString("madein");
                final String price = object.getString("price");
                final int amount = object.getInt("amount");
                final int rate = object.getInt("rate");
                final String img1 = object.getString("img1");
                final String img2 = object.getString("img2");
                final String img3 = object.getString("img3");
                final String img4 = object.getString("img4");
                final String img5 = object.getString("img5");
                final String descr = object.getString("description");

                final String size = object.getString("size");
                final String colors = object.getString("colors");
                final int productid = object.getInt("productid");

                NumberFormat formatter = new DecimalFormat("#,###");
                int a = Integer.valueOf(price.replaceAll("[^0-9]", ""));
                final int afterOff = a - ((Integer.valueOf(price) * off) / 100);

                String text = "  " + title + " " + formatter.format(afterOff) + " تومان" + "\n" + "  " + off + "% OFF";
                textSliderView = new TextSliderView(getActivity());
                textSliderView
                        .description(text)
                        .image(img1);
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        Intent inten = new Intent(getActivity(), ShowPDetailsActivity.class);
                        inten.putExtra("id", id);
                        inten.putExtra("title", title);
                        inten.putExtra("madein", madein);
                        inten.putExtra("price", price);
                        inten.putExtra("amount", amount);
                        inten.putExtra("rate", rate);
                        inten.putExtra("img1", img1);
                        inten.putExtra("img2", img2);
                        inten.putExtra("img3", img3);
                        inten.putExtra("img4", img4);
                        inten.putExtra("img5", img5);
                        inten.putExtra("descr", descr);
                        inten.putExtra("off", off);
                        inten.putExtra("afteroff", afterOff);
                        inten.putExtra("size", size);
                        inten.putExtra("colors", colors);
                        inten.putExtra("pdid", productid);
                        startActivity(inten);
                    }
                });

                sliderShow.addSlider(textSliderView);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestError(String errorMessage) {
        final NoInternetDialog dialog = new NoInternetDialog(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reload();
            }
        });
        dialog.show();
        Toast.makeText(getActivity(), "error in interface" + errorMessage, Toast.LENGTH_SHORT).show();
    }
}
