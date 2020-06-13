package ir.engineerpc.shoecenter.MyClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Android on 06/02/2018.
 */


public class SharedPref {

    String key;
    int value;

    public SharedPref(){

        this.key="A";
        this.value=1;
    }



    //Shared preferences **************  * * * * * * * *  ** * *****************************************************************
    public static void saveInt(Context context, String key, int value){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();

    }
    public static void saveLong(Context context, String key, long value){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putLong(key,value);
        editor.commit();

    }
    public static void saveString(Context context, String key, String value){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();

    }


//    public static void saveBoolean(Context context,String key,boolean value){
//        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
//        SharedPreferences.Editor editor=sharedPreferences.edit();
//        editor.putBoolean(key,value);
//        editor.commit();
//
//    }
//
//    public static boolean loadBoolean(Context context,String key){
//        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
//        boolean i=sharedPreferences.getBoolean(key,false);
//        return i;
//    }

    public static long loadLong(Context context, String key){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        long i=sharedPreferences.getLong(key,-1);
        return i;
    }
    public static int loadInt(Context context, String key){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        int i=sharedPreferences.getInt(key,-1);
        return i;
    }

    public static String loadString(Context context, String key){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        String i=sharedPreferences.getString(key,"");
        return i;
    }
    //****************************************************************************************           *******************************

    public static void clearAll(Context context){
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();

    }







}
