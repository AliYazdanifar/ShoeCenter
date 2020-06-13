package ir.engineerpc.shoecenter.MyFragments.Main;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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

import ir.engineerpc.shoecenter.MyClass.CustomDialogs.NoInternetDialog;
import ir.engineerpc.shoecenter.MyClass.FrgMessage.MessageRecyclerAdapter;
import ir.engineerpc.shoecenter.MyClass.FrgMessage.MessageRecyclerRowItem;
import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainMessagesFrg extends Fragment {

    View view;
    RecyclerView recyclerView;
    GridLayoutManager manager;
    List<MessageRecyclerRowItem> list;
    NoInternetDialog noInternetDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_messages_frg, container, false);

        init();


        getNewMessages();

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
                getNewMessages();
            }
        });
    }

    private void getNewMessages() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("کمی صبر کنید");
        pd.show();
        list = new ArrayList<>();

        StringRequest request = new StringRequest(Request.Method.POST, G.userCommunicationUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    pd.dismiss();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String title = object.getString("title");
                        String text = object.getString("text");
                        String img = object.getString("img");
                        String link = object.getString("link");
                        MessageRecyclerRowItem item = new MessageRecyclerRowItem(id, title, text, img, link);
                        list.add(item);
                    }

                    MessageRecyclerAdapter adapter = new MessageRecyclerAdapter(getActivity(), list);
                    recyclerView.setAdapter(adapter);


                } catch (Exception e) {
                    pd.dismiss();
//                    ((MainActivity)getActivity()).showDialogNoInternet();

                    noInternetDialog.show();
                    e.printStackTrace();
                    Toast.makeText(G.context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pd.dismiss();
//                ((MainActivity)getActivity()).showDialogNoInternet();
                noInternetDialog.show();
            }
        }) {
            Map<String, String> map = new HashMap();

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.put("op", "fabmessages");
                return map;

            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(G.context);
        queue.add(request);
    }

}
