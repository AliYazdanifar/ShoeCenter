package ir.engineerpc.shoecenter;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ir.engineerpc.shoecenter.MyClass.FrgMain.MainFrgRecyclerAdapter;
import ir.engineerpc.shoecenter.MyClass.FrgMain.MainFrgRecyclerRowItems;
import ir.engineerpc.shoecenter.MyClass.G;

public class TestActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    TextView tv1, tv2;
    Button btnRed;

    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        linearLayout = findViewById(R.id.linear_test);

        tv1 = new TextView(TestActivity.this);
        tv2 = new TextView(TestActivity.this);
        btnRed = new Button(TestActivity.this);
        btnRed.setText("ok");
        btnRed.setBackgroundColor(Color.RED);
        btnRed.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        ArrayList<MainFrgRecyclerRowItems> list = new ArrayList<>();


        MainFrgRecyclerRowItems rowItems = new MainFrgRecyclerRowItems(1, "http://google.com", "title", "detail", 2, "a");
        list.add(rowItems);

        MainFrgRecyclerAdapter adapter = new MainFrgRecyclerAdapter(G.context, list);


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i <= 8; i++) {
            recyclerView = new RecyclerView(TestActivity.this);
            recyclerView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            recyclerView.setLayoutManager(new GridLayoutManager(TestActivity.this, 1));
            recyclerView.setAdapter(adapter);
            linearLayout.addView(recyclerView);
        }


//        tv1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//        linearLayout.addView(tv1);
//        linearLayout.addView(recyclerView);
//        linearLayout.addView(btnRed);
//
//        btnRed=new Button(TestActivity.this);
//        btnRed.setText("two");
//        btnRed.setBackgroundColor(Color.GREEN);
//
//        linearLayout.addView(btnRed);


    }
}
