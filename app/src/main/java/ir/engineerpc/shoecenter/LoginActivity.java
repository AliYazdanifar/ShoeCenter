package ir.engineerpc.shoecenter;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import ir.engineerpc.shoecenter.MyClass.Font;
import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyFragments.Login.LoginFrstFrg;
import ir.engineerpc.shoecenter.MyFragments.Login.LoginScndFrg;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    FragmentTransaction ft;
    TextView tvTitle, tvDetail;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        try {

            tvTitle = findViewById(R.id.lgtv);
            tvDetail = findViewById(R.id.lgtvdetail);
            new Font(LoginActivity.this, tvTitle, "dimabarf.ttf");
            getFragment("main");


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(G.context, "Error", Toast.LENGTH_SHORT).show();
        }
    }


    public void getFragment(String frgName) {
        switch (frgName) {
            case "main":
                tvDetail.setText("برای ورود لطفا شماره ی همراه خود را وارد کنید");
//                Begin the transaction
                ft = getSupportFragmentManager().beginTransaction();
//                Replace the contents of the container with the new fragment
                ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,
                        R.anim.slide_in_back_from_right, R.anim.slide_out_back_to_left);
                ft.replace(R.id.lgnfrmlyt, new LoginFrstFrg());
//                or ft.add(R.id.your_placeholder, new FooFragment());
//                Complete the changes added above
                ft.commit();
                break;
            case "getCode":
                //                Begin the transaction
                ft = getSupportFragmentManager().beginTransaction();
//                Replace the contents of the container with the new fragment
                ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,
                        R.anim.slide_in_back_from_right, R.anim.slide_out_back_to_left);

                ft.replace(R.id.lgnfrmlyt, new LoginScndFrg()).addToBackStack(null);
//                or ft.add(R.id.your_placeholder, new FooFragment());
//                Complete the changes added above
                ft.commit();
                break;
        }
    }

}
