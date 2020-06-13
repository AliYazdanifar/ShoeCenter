package ir.engineerpc.shoecenter.MyFragments.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.alahammad.otp_view.OTPListener;
import com.alahammad.otp_view.OtpView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.engineerpc.shoecenter.MainActivity;
import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.R;

public class LoginScndFrg extends Fragment {


    private String loginCode, userCode;
    private Button btnCompare;
    private OtpView otpView;
    private TextView tvDetails, tvTimer;
    int i = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_scnd_frg, container, false);

        tvDetails = getActivity().findViewById(R.id.lgtvdetail);
        tvTimer = getActivity().findViewById(R.id.lgtvtimer);

        tvDetails.setText("لطفا تا قبل از دریافت کد فعالسازی خارج نشوید");

        CountDownTimer timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("حداکثر تا " + String.valueOf(millisUntilFinished / 1000) + " ثانیه ی دیگر کد را دریافت می کنید.");
            }

            @Override
            public void onFinish() {

                tvTimer.setText("کد فعالسازی دریافت نکرده اید؟");
                tvTimer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "لطفا چند دقیقه بعد مجددا تلاش کنید", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                });
            }
        }.start();
        otpView = view.findViewById(R.id.otp);


        btnCompare = view.findViewById(R.id.login_btn_compare);


        btnCompare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compare();
            }
        });


        otpView.setOnOtpFinished(new OTPListener() {
            @Override
            public void otpFinished(String s) {
                compare();
            }
        });

        return view;
    }

    private void compare() {
        loginCode = SharedPref.loadString(G.context, "logincode");
        final int pl = Integer.valueOf(loginCode.replaceAll("[^0-9]", ""));
        userCode = otpView.getOTP();

        final int pu = Integer.valueOf(userCode.replaceAll("[^0-9]", ""));

        if (pl == pu) {
            Toast.makeText(G.context, "خوش آمدید", Toast.LENGTH_SHORT).show();

            final ProgressDialog pd = new ProgressDialog(getActivity());
            pd.setMessage("لطفا صبر کنید...");
            pd.setCancelable(false);
            pd.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, G.loginUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    pd.dismiss();

                    try {

                        JSONObject object = new JSONObject(response);

                        SharedPref.saveString(G.context, "userid", object.getString("id"));
                        SharedPref.saveString(G.context, "usernf", object.getString("nf"));
                        SharedPref.saveString(G.context, "usermail", object.getString("mail"));
                        SharedPref.saveString(G.context, "useraddress", object.getString("address"));

                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);


                        SharedPref.saveString(G.context, "login", "logedin");
                        getActivity().finish();

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
                    map.put("op", "adduser");
                    map.put("tell", SharedPref.loadString(G.context, "userphone"));
                    return map;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            RequestQueue queue = Volley.newRequestQueue(G.context);
            queue.add(stringRequest);


        } else {
            Toast.makeText(getActivity(), "کد فعالسازی صحیح نیست.لطفا پیامکی که برایتان ارسال شد را بررسی کنید ", Toast.LENGTH_SHORT).show();
        }
    }
}
