package ir.engineerpc.shoecenter.MyFragments.Main;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import ir.engineerpc.shoecenter.MyClass.FrgHistory.HistoryRecyclerAdapter;
import ir.engineerpc.shoecenter.MyClass.FrgHistory.HistoryRecyclerRowItem;
import ir.engineerpc.shoecenter.MyClass.CustomDialogs.NoInternetDialog;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainHistoryFrg extends Fragment {

    View view;
    RecyclerView recyclerView;
    GridLayoutManager manager;
    List<HistoryRecyclerRowItem> list;

    NoInternetDialog noInternetDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_history_frg, container, false);
        init();

        getHistory();


        return view;
    }

    private void init() {

        recyclerView = view.findViewById(R.id.recycler_for_all);
        manager = new GridLayoutManager(G.context, 1);
        recyclerView.setLayoutManager(manager);

        noInternetDialog = new NoInternetDialog(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noInternetDialog.dismiss();
                getHistory();
            }
        });


    }


    private void getHistory() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("کمی صبر کنید");
        pd.show();
        list = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.POST, G.userCommunicationUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.i("ALI", response);
                    pd.dismiss();
                    JSONArray jsonArray = new JSONArray(response);
                    View v = view.findViewById(R.id.hstrnothinglayout);
                    if (jsonArray.length() == 0) {
                        v.setVisibility(View.VISIBLE);
                    } else {
                        v.setVisibility(View.GONE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt("id");
                            String order = object.getString("order");
                            String reciver = object.getString("reciver");
                            String address = object.getString("address");
                            String tell = object.getString("tell");
                            String cDescr = object.getString("customerdescription");
                            String sDescr = object.getString("sellerdescription");
                            String payment = object.getString("payment");
                            String refid = object.getString("refid");
                            String date = object.getString("date");
                            int userId = object.getInt("userid");
                            HistoryRecyclerRowItem item = new HistoryRecyclerRowItem(id, order, "details", reciver, address, tell, payment, refid, date, cDescr, sDescr, userId);
                            list.add(item);
                        }

                        HistoryRecyclerAdapter adapter = new HistoryRecyclerAdapter(list);
                        recyclerView.setAdapter(adapter);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    pd.dismiss();
//                    ((MainActivity) getActivity()).showDialogNoInternet();

                    Toast.makeText(G.context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
//                ((MainActivity) getActivity()).showDialogNoInternet();
                noInternetDialog.show();
            }
        }) {
            Map<String, String> map = new HashMap();

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.put("op", "gethistory");
                map.put("userid", SharedPref.loadString(G.context, "userid"));
                return map;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(G.context);
        queue.add(request);
    }


}
