package ir.engineerpc.shoecenter.MyFragments.Login;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.Map;

import ir.engineerpc.shoecenter.LoginActivity;
import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFrstFrg extends Fragment {

    EditText edtPhone;
    FragmentTransaction ft;
    TextView tvDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_frst_frg, container, false);

        tvDetail = getActivity().findViewById(R.id.lgtvdetail);
        tvDetail.setText("برای ورود لطفا شماره ی همراه خود را وارد کنید");
        edtPhone = (EditText) view.findViewById(R.id.edtphonetwo);
        edtPhone.setText(SharedPref.loadString(G.context, "userphone"));

        View btngetCode = view.findViewById(R.id.login_btn_get_code);
        btngetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String userPhone = edtPhone.getText().toString();
                if (userPhone.length() < 10 || userPhone.startsWith("0") || !userPhone.startsWith("9"))
                    Snackbar.make(view, "لطفا شماره همراه را به درستی وارد کنید", Snackbar.LENGTH_SHORT).show();
                else {
                    new MaterialStyledDialog.Builder(getActivity())
                            .setHeaderColor(R.color.gray)
                            .setStyle(Style.HEADER_WITH_ICON)
                            .setIcon(R.drawable.ic_sms_black_24dp)
                            .setTitle("0"+edtPhone.getText().toString())
                            .setDescription(getResources().getString(R.string.login_dialog))
                            .setPositiveText("بله")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    final ProgressDialog pd = new ProgressDialog(getActivity());
                                    pd.setMessage("لطفا صبر کنید...");
                                    pd.setCancelable(false);
                                    pd.show();

                                    SharedPref.saveString(G.context, "userphone", userPhone);
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, G.loginUrl, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                pd.dismiss();
                                                SharedPref.saveString(G.context, "logincode", response);
                                                ((LoginActivity) getActivity()).getFragment("getCode");

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(G.context, "خطا در برقراری ارتباط مجددا تلاش کنید", Toast.LENGTH_SHORT).show();
                                            pd.dismiss();
                                        }
                                    }) {
                                        Map<String, String> map = new HashMap<>();

                                        @Override
                                        protected Map<String, String> getParams() throws AuthFailureError {
                                            map.put("op", "getlogincode");
                                            map.put("tell", edtPhone.getText().toString());
                                            return map;
                                        }
                                    };
                                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    RequestQueue queue = Volley.newRequestQueue(G.context);
                                    queue.add(stringRequest);
                                }
                            })
                            .setNegativeText("خیر").onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        }
                    }).show();

                }
            }
        });

        return view;
    }
}
