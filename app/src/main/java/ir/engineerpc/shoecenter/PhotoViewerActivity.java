package ir.engineerpc.shoecenter;


import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;


public class PhotoViewerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_photo_viewer);

        TextView tvText=findViewById(R.id.photoviewertextview);
        Button btn=findViewById(R.id.photoviewerbtn);


        Bundle bundle = getIntent().getExtras();

        String imageUrl = bundle.getString("img");

        String op = bundle.getString("op");

        if (op.equals("showmsg")) {
            String text=bundle.getString("txt");
            final String link=bundle.getString("link");
            tvText.setVisibility(View.VISIBLE);
            btn.setVisibility(View.VISIBLE);
            tvText.setText(text);

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(link));
                    startActivity(intent);
                }
            });


        }
        WebView wv = findViewById(R.id.yourwebview);
        wv.loadUrl(imageUrl);
        wv.getSettings().setSupportZoom(true);
        wv.getSettings().setBuiltInZoomControls(true);
        wv.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        wv.setInitialScale(1);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setUseWideViewPort(true);
    }

}