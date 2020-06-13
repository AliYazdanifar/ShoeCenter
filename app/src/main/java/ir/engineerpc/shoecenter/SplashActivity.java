package ir.engineerpc.shoecenter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.florent37.viewanimator.ViewAnimator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ir.engineerpc.shoecenter.MyClass.CustomDialogs.NoInternetDialog;
import ir.engineerpc.shoecenter.MyClass.Font;
import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {

    RequestQueue queue;
    ProgressBar progressBar;
    int i = 0;
    NoInternetDialog noInternetDialog;

    TextView tvTitle, tvDetails;

    CardView cv;
    ImageView img;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        progressBar = findViewById(R.id.splashprogressBar);
        cv = findViewById(R.id.splashcardview);
        img = findViewById(R.id.splashimg);

        tvTitle = findViewById(R.id.splshtvtitle);
        new Font(SplashActivity.this, tvTitle, getResources().getString(R.string.dimabarf_font_name));

        noInternetDialog = new NoInternetDialog(SplashActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noInternetDialog.dismiss();
                getFromServer(G.userCommunicationUrl, "getadmindata");
            }
        });


        getFromServer(G.userCommunicationUrl, "getadmindata");

    }

    public void getFromServer(String url, final String op) {

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    img.setVisibility(View.VISIBLE);
                    ViewAnimator
                            .animate(cv)
                            .swing()
                            .duration(2000)
                            .start();
                    ViewAnimator
                            .animate(img)
                            .slideBottomIn()
                            .repeatMode(ViewAnimator.RESTART)
                            .duration(2000)
                            .start();
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {


                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String updates = object.getString("updates");
                        final String link = object.getString("link");
                        G.appDownloadLink = link;
                        SharedPref.saveString(G.context, "postcost", object.getString("postcost"));
                        G.postCost = Integer.valueOf(object.getString("postcost"));
//                        G.ip = object.getString("ip");
                        String versionName = BuildConfig.VERSION_NAME;
                        SharedPref.saveString(G.context, "app_version_name", versionName);

                        if (updates.equals(versionName)) {

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        for (int j = 0; j <= 100; j += 2) {
                                            progressBar.setProgress(j);
                                            Thread.sleep(50);
                                        }
                                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();


                        } else {
                            ProgressDialog.Builder pd = new AlertDialog.Builder(SplashActivity.this);
                            pd.setTitle("خبرای خوب");
                            pd.setCancelable(false);
                            pd.setMessage("نسخه ی جدیدی از برنامه منتشر شده لطفا آن را دانلود و نصب کنید");
                            pd.setPositiveButton("دانلود", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(link));
                                    startActivity(intent);
                                }
                            }).show();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(G.context, "Error : " + e.getMessage(), Toast.LENGTH_LONG).show();
                    noInternetDialog.show();
//                    showDialogNoInternet(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            noInternetDialog.dismiss();
//                            getFromServer(G.userCommunicationUrl, "getadmindata");
//                        }
//                    });
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Toast.makeText(G.context,"No Response = "+error.getMessage(),Toast.LENGTH_LONG).show();
//                showDialogNoInternet(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        noInternetDialog.dismiss();
//                        getFromServer(G.userCommunicationUrl, "getadmindata");
//                    }
//                });
                noInternetDialog.show();
            }
        }) {
            Map<String, String> map = new HashMap<>();

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                map.put("op", op);
                return map;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(7000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue = Volley.newRequestQueue(G.context);
        queue.add(request);

    }

}
