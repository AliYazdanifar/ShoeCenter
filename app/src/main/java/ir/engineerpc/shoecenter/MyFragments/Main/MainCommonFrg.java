package ir.engineerpc.shoecenter.MyFragments.Main;


import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import ir.engineerpc.shoecenter.BuildConfig;
import ir.engineerpc.shoecenter.MyClass.G;
import ir.engineerpc.shoecenter.MyClass.SharedPref;
import ir.engineerpc.shoecenter.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainCommonFrg extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.custom_contactus_layout, container, false);

        view = inflater.inflate(R.layout.fragment_main_common_frg, container, false);
        TextView tvVersion = view.findViewById(R.id.textView6);
        String versionName ="نسخه ی "+ BuildConfig.VERSION_NAME;
        tvVersion.setText(versionName);

        return view;
    }

}
