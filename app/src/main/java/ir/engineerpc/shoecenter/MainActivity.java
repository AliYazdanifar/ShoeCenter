package ir.engineerpc.shoecenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Style;
import com.google.android.material.navigation.NavigationView;

import ir.engineerpc.shoecenter.MyClass.Font;
import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.MyDatabaseManager;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.MyFragments.Main.MainActivityFrg;
import ir.engineerpc.shoecenter.MyFragments.Main.MainCommonFrg;
import ir.engineerpc.shoecenter.MyFragments.Main.MainHistoryFrg;
import ir.engineerpc.shoecenter.MyFragments.Main.MainMessagesFrg;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private static long BACK_PRESSED;
    DrawerLayout drawer;
    NavigationView navigationView;
    FragmentTransaction ft;
    TextView tvBadge, tvDrawerUserNf, tvDrawerUserTell;
    LinearLayout linearSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        try {
            init();
            toolbarMethod();
            tvBadge = findViewById(R.id.tvbadge);

//        G.easyDB.deleteAllDataFromTable();
            ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,
                    R.anim.slide_in_back_from_right, R.anim.slide_out_back_to_left);
            ft.replace(R.id.mainfrmlyt, new MainActivityFrg(), "MAIN");
            ft.commit();

//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(G.context, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    protected void onResume() {
        MyDatabaseManager myDatabaseManager = new MyDatabaseManager(getApplicationContext());
        myDatabaseManager.getCartCount();
        if (myDatabaseManager.cartPdCount == 0)
            tvBadge.setVisibility(View.GONE);
        else {
            tvBadge.setVisibility(View.VISIBLE);
            tvBadge.setText(String.valueOf(myDatabaseManager.cartPdCount));
        }
        super.onResume();
    }

    private void toolbarMethod() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView imgdrawer = findViewById(R.id.imgdrawermenu);
        ImageView imgCart = findViewById(R.id.imgMaincart);
        TextView tvTitle = findViewById(R.id.maintvtitle);
        new Font(MainActivity.this, tvTitle, getResources().getString(R.string.dimabarf_font_name));
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        imgdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.RIGHT);
            }
        });
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void init() {

        linearSlider = findViewById(R.id.mainfrgsliderlinear);
        if (widthGTHeight())
            linearSlider.setMinimumHeight(260);
        navigationView = findViewById(R.id.nav_view);
        View view = navigationView.getHeaderView(0);
        tvDrawerUserNf = view.findViewById(R.id.drawerTvNf);
        tvDrawerUserTell = view.findViewById(R.id.drawerTvTell);

        if (SharedPref.loadString(G.context, "login").equals("logedin")) {
            String text = SharedPref.loadString(G.context, "usernf");
            tvDrawerUserNf.setText(text);
            text = SharedPref.loadString(G.context, "userphone") + "کد شناسایی شما :" + SharedPref.loadString(G.context, "userid");
            tvDrawerUserTell.setText(text);
        } else {
            tvDrawerUserNf.setText("کاربر مهمان");
            tvDrawerUserTell.setText("www.adinetak.ir");
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.RIGHT)) {
            drawer.closeDrawer(Gravity.RIGHT);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else {
            if (BACK_PRESSED + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(G.context, "برای خروج دوباره دکمه ی برگشت را لمس کنید", Toast.LENGTH_SHORT).show();
                BACK_PRESSED = System.currentTimeMillis();
            }
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_main) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,
                    R.anim.slide_in_back_from_right, R.anim.slide_out_back_to_left);
            ft.replace(R.id.mainfrmlyt, new MainActivityFrg());
            ft.commit();
        } else if (id == R.id.nav_history) {

            if (!SharedPref.loadString(G.context, "login").equals("logedin")) {
                new MaterialStyledDialog.Builder(MainActivity.this)
                        .setHeaderColor(R.color.my)
                        .setStyle(Style.HEADER_WITH_TITLE)
                        .setTitle("ورود به برنامه")
                        .setDescription(getResources().getString(R.string.msg_login))
                        .setPositiveText("ورود")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Intent myintent = new Intent(MainActivity.this, LoginActivity.class);
                                startActivity(myintent);
                            }
                        })

                        .show();
            } else {
                ft = getSupportFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,
                        R.anim.slide_in_back_from_right, R.anim.slide_out_back_to_left);
                ft.replace(R.id.mainfrmlyt, new MainHistoryFrg());
                ft.addToBackStack(null);
                ft.commit();
            }

        } else if (id == R.id.nav_share) {
            new MaterialStyledDialog.Builder(MainActivity.this)
                    .setHeaderColor(R.color.my)
                    .setIcon(R.drawable.ic_menu_share)
                    .setStyle(Style.HEADER_WITH_ICON)
                    .setTitle("از انتخاب شما متشکریم")
                    .setDescription("لطفا جهت حمایت از ما برنامه را به دوستان خود معرفی کنید")
                    .setPositiveText("اشتراک")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Intent myintent = new Intent();
                            myintent.setAction(Intent.ACTION_SEND);
                            myintent.putExtra(Intent.EXTRA_TEXT, "سلام من برای خرید کیف و کفش از برنامه ی تولیدی آدینه استفاده میکنم .شما هم میتوانید توسط لینک زیر برنامه را دانلود کنید : " + G.appDownloadLink);
                            myintent.setType("text/plain");
                            startActivity(myintent);
                        }
                    })

                    .show();

        } else if (id == R.id.nav_messages) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,
                    R.anim.slide_in_back_from_right, R.anim.slide_out_back_to_left);
            ft.replace(R.id.mainfrmlyt, new MainMessagesFrg());
            ft.addToBackStack(null);

            ft.commit();
        } else if (id == R.id.nav_communication) {
            ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right,
                    R.anim.slide_in_back_from_right, R.anim.slide_out_back_to_left);
            SharedPref.saveString(G.context, "drawerop", "contact_us");
            ft.replace(R.id.mainfrmlyt, new MainCommonFrg());
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_logout) {

            new MaterialStyledDialog.Builder(MainActivity.this)
                    .setHeaderColor(R.color.my)
                    .setStyle(Style.HEADER_WITH_TITLE)
                    .setTitle("خروج !")
                    .setDescription("از حساب کاربری خارج می شوید؟")
                    .setPositiveText("بله")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            SharedPref.saveString(G.context, "login", "logedout");
                            finish();
                        }
                    })
                    .setNegativeText("خیر").onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                }
            }).show();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(Gravity.RIGHT);
        return true;
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
