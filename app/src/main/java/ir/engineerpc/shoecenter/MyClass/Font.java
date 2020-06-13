package ir.engineerpc.shoecenter.MyClass;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Android on 13/07/2018.
 */

public class Font{
    public Font(Context context,TextView textView, String fontName){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/"+fontName);
        textView.setTypeface(typeface);
    }

    public Font(Context context, EditText editText, String fontName){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/"+fontName);
        editText.setTypeface(typeface);
    }

    public Font(Context context, Button button, String fontName){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/"+fontName);
        button.setTypeface(typeface);
    }
}