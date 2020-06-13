package ir.engineerpc.shoecenter.MyFragments.Cart;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartGetInfoFrg extends Fragment {

    EditText edtNameFamily, edtAddress, edtPostCode, edtTell, edtEmail, edtdescr;
    Button btnProvince, btnCity;
    ArrayList<String> myArray;
    int cityId;
    ProgressDialog pd;

    View view;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cart_get_info_frg, container, false);


        edtNameFamily = view.findViewById(R.id.cartgetinfoedtnamefamily);
        edtAddress = view.findViewById(R.id.cartgetinfoedtaddress);
        edtPostCode = view.findViewById(R.id.cartgetinfoedtpostcode);
        edtTell = view.findViewById(R.id.cartgetinfoedttell);
        edtEmail = view.findViewById(R.id.cartgetinfoedtemail);
        edtdescr = view.findViewById(R.id.cartgetinfoedtdescr);

        edtNameFamily.setText(SharedPref.loadString(G.context, "edtnameandfamily"));
        edtAddress.setText(SharedPref.loadString(G.context, "edtaddress"));
        edtPostCode.setText(SharedPref.loadString(G.context, "edtpostcode"));
        edtTell.setText(SharedPref.loadString(G.context, "edttell"));
        edtEmail.setText(SharedPref.loadString(G.context, "edtmail"));
        edtdescr.setText(SharedPref.loadString(G.context, "edtdescr"));

        btnProvince = view.findViewById(R.id.cartgetinfobtnstate);
        btnCity = view.findViewById(R.id.cartgetinfobtncity);


        btnProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFromNet("state", "");

            }
        });
        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFromNet("city", cityId + "");
            }
        });


        final Button btnNext = getActivity().findViewById(R.id.cartbtnContinue);
        btnNext.setVisibility(View.VISIBLE);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edtNameFamily.getText().toString().isEmpty()) {
                    edtNameFamily.requestFocus();
                    edtNameFamily.setError("لطفا نام و نام خانوادگی دریافت کننده را وارد کنید");
                } else if (btnProvince.getText().toString().equals("استان")) {
                    btnProvince.requestFocus();
                    Snackbar.make(view, "لطفا استان خود را وارد کنید", Snackbar.LENGTH_SHORT).show();

                } else if (btnCity.getText().toString().equals("شهر")) {
                    btnCity.requestFocus();
                    Snackbar.make(view, "لطفا شهر خود را وارد کنید", Snackbar.LENGTH_SHORT).show();
                } else if (edtAddress.getText().toString().length() < 8) {
                    edtAddress.requestFocus();
                    edtAddress.setError("آدرس باید حداقل 5 کاراکتر داشته باشد");

                } else if (edtPostCode.getText().toString().isEmpty() || edtPostCode.getText().toString().length() < 10) {
                    edtPostCode.requestFocus();
                    edtPostCode.setError("لطفا کد پستی را درست وارد کنید");
                } else if (edtTell.getText().toString().length() < 11) {
                    edtTell.requestFocus();
                    edtTell.setError("لطفا شماره ی همراه را به درستی وارد کنید");
                } else {

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    final ProgressBar progressBar = getActivity().findViewById(R.id.cartprogressBar);
                    SharedPref.saveString(G.context, "edtnameandfamily", edtNameFamily.getText().toString());
                    SharedPref.saveString(G.context, "btnstate", btnProvince.getText().toString());
                    SharedPref.saveString(G.context, "btncity", btnCity.getText().toString());
                    SharedPref.saveString(G.context, "edtaddress", edtAddress.getText().toString());
                    SharedPref.saveString(G.context, "edtpostcode", edtPostCode.getText().toString());
                    SharedPref.saveString(G.context, "edttell", edtTell.getText().toString());
                    SharedPref.saveString(G.context, "edtemail", edtEmail.getText().toString());
                    SharedPref.saveString(G.context, "edtdescr", edtdescr.getText().toString());

                    SharedPref.saveString(G.context, "fulladdress", btnProvince.getText().toString() +
                            " - " + btnCity.getText().toString() + " - " + edtAddress.getText().toString() +
                            "\n" + "کد پستی : " + edtPostCode.getText().toString());

                    ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,
                            R.anim.slide_in_back_from_right, R.anim.slide_out_back_to_left);
                    ft.replace(R.id.cartFrmLyt, new CartShowDetailsFrg()).addToBackStack(null);
                    ft.commit();

                    progressBar.setProgress(97);
                    btnNext.setText("پرداخت");
                }
            }
        });


        return view;
    }


    public void getFromNet(final String value, final String id) {
        pd = new ProgressDialog(getActivity());
        pd.setMessage("کمی صبر کنید...");
        pd.setCancelable(false);
        pd.show();
        myArray = new ArrayList<>();
        final Dialog dialogState = new Dialog(getActivity());
        dialogState.setContentView(R.layout.custom_state_and_citylist);
        final ListView lstState = (ListView) dialogState.findViewById(R.id.lstState);
        StringRequest request = new StringRequest(Request.Method.POST, G.stateAndCityUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int id = jsonObject.getInt("id");
                        String name = jsonObject.getString("name");

                        myArray.add(name);

                    }
                    ArrayAdapter adapterState = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, myArray);
                    lstState.setAdapter(adapterState);
                    dialogState.show();
                    if (value.equals("state")) {
                        lstState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                btnProvince.setText(myArray.get(i));
                                cityId = i + 1;
                                getFromNet("city", String.valueOf(i + 1));
                                dialogState.dismiss();
                            }
                        });
                    } else {
                        lstState.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                btnCity.setText(myArray.get(i));
                                dialogState.dismiss();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(G.context, "Bug......", Toast.LENGTH_SHORT).show();
                    Log.i("ALI", "bug in try in Spinnerstat Method = " + e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
            }
        }) {
            Map<String, String> map = new HashMap<>();

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.put("value", value);
                map.put("id", id);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(8000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

}
