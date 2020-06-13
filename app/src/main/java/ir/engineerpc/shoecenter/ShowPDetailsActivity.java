package ir.engineerpc.shoecenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.MyDatabaseManager;
import ir.engineerpc.shoecenter.MyClass.CustomDialogs.NoInternetDialog;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowPDetailsActivity extends AppCompatActivity {

    String title, madein, price, img1Url, img2Url, img3Url, img4Url, img5Url, descr, size, colors, endPrice, imgurl;

    SliderLayout sliderShow;
    TextSliderView textSliderView;
    List<String> pdImages;
    int x = 0;
    int thisPdId, amount, rate, off, pdId, userAmountForOrder = 1, qKol = 0;

    MyDatabaseManager myDatabaseManager;


    ImageView img1, img2, img3, img4, img5;
    TextView tvTitle, tvMadein, tvPrice, tvAmount, tvDescr, tvUserAmountChose, tvEndPrice;
    RatingBar ratingBar;
    Button btnColor, btnSize, btnPlus, btnMines, btnAddToCart, btnAddToFav;

    NoInternetDialog noInternetDialog;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pdetails);
        toolBarCodes();
        try {


            init();
            setValues();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(G.context, "Error", Toast.LENGTH_SHORT).show();
        }
    }


    private void init() {
        tvTitle = findViewById(R.id.showpdtxttitle);
        tvMadein = findViewById(R.id.showpdtxtmadein);
        tvPrice = findViewById(R.id.showpdtxtprice);
        tvAmount = findViewById(R.id.showpdtxtamount);
        tvDescr = findViewById(R.id.showpdtxtdescr);
        tvUserAmountChose = findViewById(R.id.showpdtxtwhatamount);
        tvEndPrice = findViewById(R.id.showpdtxtendprice);
        sliderShow = findViewById(R.id.slidershowpdactivity);

        ratingBar = findViewById(R.id.showpdratingbar);

//        imgHeader = findViewById(R.id.showpdimgheader);
        img1 = findViewById(R.id.showpdimg1);
        img2 = findViewById(R.id.showpdimg2);
        img3 = findViewById(R.id.showpdimg3);
        img4 = findViewById(R.id.showpdimg4);
        img5 = findViewById(R.id.showpdimg5);

        btnColor = findViewById(R.id.showpdbtncolor);
        btnSize = findViewById(R.id.showpdbtnsize);

        myDatabaseManager=new MyDatabaseManager(getApplicationContext());

        btnPlus = findViewById(R.id.showpdbtnplus);
        btnMines = findViewById(R.id.showpdbtnmines);

        btnAddToCart = findViewById(R.id.showpdbtnaddtocart);
        btnAddToFav = findViewById(R.id.showpdbtnaddtofavorites);
//        btnPrev = findViewById(R.id.showpdbtnprev);
//        btnNext = findViewById(R.id.showpdbtnnext);
        pdImages = new ArrayList<>();

        noInternetDialog = new NoInternetDialog(ShowPDetailsActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noInternetDialog.dismiss();
            }
        });
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

    private void setValues() {
        Bundle bundle = getIntent().getExtras();
        thisPdId = bundle.getInt("id");
        title = bundle.getString("title");
        madein = bundle.getString("madein");
        price = bundle.getString("price");
        qKol = Integer.valueOf(bundle.getString("price"));

        amount = bundle.getInt("amount");
        rate = bundle.getInt("rate");
        img1Url = bundle.getString("img1");
        img2Url = bundle.getString("img2");
        img3Url = bundle.getString("img3");
        img4Url = bundle.getString("img4");
        img5Url = bundle.getString("img5");
        pdImages.add(img1Url);
        pdImages.add(img2Url);
        pdImages.add(img3Url);
        pdImages.add(img4Url);
        pdImages.add(img5Url);
        descr = bundle.getString("descr");
        off = bundle.getInt("off");
        size = bundle.getString("size");
        colors = bundle.getString("colors");
        pdId = bundle.getInt("pdid");


        String text;


        tvTitle.setText(title);
        tvMadein.setText(madein);
        NumberFormat formatter = new DecimalFormat("#,###");

        if (off > 0) {
            int a = Integer.valueOf(price.replaceAll("[^0-9]", ""));
            final int afterOff = a - ((Integer.valueOf(price) * off) / 100);

            text = "قیمت اصلی = " + price + " تومان\n" + "با " + off + "% تخفیف = " + formatter.format(Integer.valueOf(afterOff)) + " تومان";
            price = String.valueOf(afterOff);
        } else
            text = formatter.format(Integer.valueOf(price)) + " تومان";
        tvPrice.setText(text);


        text = "قیمت کل " + userAmountForOrder * Integer.valueOf(price) + " تومان";
        tvEndPrice.setText(text);

        tvAmount.setText("موجود در انبار : " + amount + " جفت");
        tvDescr.setText(descr);

        ratingBar.setRating(rate);

        imgurl = img1Url;
        glideForImages(img1Url, img1);
        glideForImages(img2Url, img2);
        glideForImages(img3Url, img3);
        glideForImages(img4Url, img4);
        glideForImages(img5Url, img5);


        btnSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (size != null) {
                    spinners(size, btnSize);
                }
            }
        });
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (colors != null) {
                    spinners(colors, btnColor);
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userAmountForOrder < amount - 1) {
                    userAmountForOrder += amount - (amount - 1);
                    tvUserAmountChose.setText(userAmountForOrder + " جفت");
                    NumberFormat formatter = new DecimalFormat("#,###");
                    endPrice = formatter.format(userAmountForOrder * Integer.valueOf(price));
                    qKol = userAmountForOrder * Integer.valueOf(price);
                    tvEndPrice.setText("قیمت کل " + endPrice + " تومان");

                } else {
                    Snackbar.make(view, "اتمام مجودی", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        btnPlus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (userAmountForOrder < amount - 1) {

                    userAmountForOrder += amount - (amount - 1);
                    tvUserAmountChose.setText(userAmountForOrder + " جفت");
                    NumberFormat formatter = new DecimalFormat("#,###");
                    endPrice = formatter.format(userAmountForOrder * Integer.valueOf(price));
                    qKol = userAmountForOrder * Integer.valueOf(price);
                    tvEndPrice.setText("قیمت کل " + endPrice + " تومان");


                } else {
                    Snackbar.make(v, "اتمام مجودی", Snackbar.LENGTH_LONG).show();
                }
                return true;
            }
        });
        btnMines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userAmountForOrder > 1) {
                    userAmountForOrder -= 1;
                    tvUserAmountChose.setText(userAmountForOrder + " جفت");
                    NumberFormat formatter = new DecimalFormat("#,###");
                    endPrice = formatter.format(userAmountForOrder * Integer.valueOf(price));
                    qKol = userAmountForOrder * Integer.valueOf(price);
                    tvEndPrice.setText("جمع کل " + endPrice + " تومان");

                }
            }
        });

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (btnSize.getText().toString().equals("انتخاب کنید"))
                    Snackbar.make(view, "لطفا سایز کفش را انتخاب کنید", Snackbar.LENGTH_LONG).show();
                else if (btnColor.getText().toString().equals("انتخاب کنید"))
                    Snackbar.make(view, "لطفا رنگ کفش را انتخاب کنید", Snackbar.LENGTH_LONG).show();
                else {
                    SharedPref.saveString(G.context, "endprice", String.valueOf(qKol));

                    if (myDatabaseManager.addToTblCart
                            (title, String.valueOf(qKol),
                                    tvUserAmountChose.getText().toString(),
                                    img1Url,
                                    btnSize.getText().toString(),
                                    btnColor.getText().toString(),
                                    pdId
                            )
                    ) {

                        btnSize.setText("انتخاب کنید");
                        btnColor.setText("انتخاب کنید");
                        Toast.makeText(ShowPDetailsActivity.this, "به سبد خرید اضافه شد", Toast.LENGTH_SHORT).show();

                    } else
                        Toast.makeText(ShowPDetailsActivity.this, "Error in Db", Toast.LENGTH_SHORT).show();
                }
            }

        });
        btnAddToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowPDetailsActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

    }

    private void spinners(String data, final Button btn) {
        final PopupMenu popupMenu = new PopupMenu(ShowPDetailsActivity.this, btn);
        int j = 0;
        if (data.contains("-"))
            for (int i = 0; i < data.length(); i++) {

                if (data.charAt(i) == '-') {
                    String a = data.substring(j, i);
                    popupMenu.getMenu().add(a);
                    j = i + 1;
                }
            }
        else
            popupMenu.getMenu().add(data);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                btn.setText(menuItem.getTitle());
                return true;
            }
        });
        popupMenu.show();
    }

    public void glideForImages(final String url, ImageView img) {
        try {

            if (!url.equals("no img")) {
                textSliderView = new TextSliderView(ShowPDetailsActivity.this);
                textSliderView
                        .description("")
                        .image(url);


                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        imgurl = url;
                        Intent intent = new Intent(ShowPDetailsActivity.this, PhotoViewerActivity.class);
                        intent.putExtra("op", "showimg");
                        intent.putExtra("img", imgurl);
                        startActivity(intent);
                    }
                });

                sliderShow.addSlider(textSliderView);

                img.setVisibility(View.VISIBLE);
                Picasso.with(this)
                        .load(url)
                        .error(R.drawable.undrawnodata)
                        .placeholder(R.drawable.progress_clock)
                        .into(img);
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgurl = url;
                        Intent intent = new Intent(ShowPDetailsActivity.this, PhotoViewerActivity.class);
                        intent.putExtra("op", "showimg");
                        intent.putExtra("img", imgurl);
                        startActivity(intent);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
