package ir.engineerpc.shoecenter.MyClass.CustomDialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ir.engineerpc.shoecenter.R;

public class NoInternetDialog extends AlertDialog implements android.view.View.OnClickListener {
    public Activity activity;
    public Button retry, no;
    public View.OnClickListener listener;

    public NoInternetDialog(Activity activity, View.OnClickListener listener) {
        super(activity);
        // TODO Auto-generated constructor stub
        this.activity = activity;
        this.listener = listener;
        setCancelable(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.no_internet_layout);
        retry = (Button) findViewById(R.id.nointernetbtnretry);
//        no = (Button) findViewById(R.id.btn_no);
        retry.setOnClickListener(listener);
//        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.nointernetbtnretry) {
            dismiss();
        }
        dismiss();
    }


}