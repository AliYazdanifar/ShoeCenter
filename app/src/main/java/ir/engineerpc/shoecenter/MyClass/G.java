package ir.engineerpc.shoecenter.MyClass;

import android.app.Application;
import android.content.Context;

import ir.engineerpc.shoecenter.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class G extends Application {
    public static Context context;
    public static String ip, a, loginUrl, productsUrl, historyUrl, userCommunicationUrl, stateAndCityUrl;
    public static int postCost = 0;
    public static String appDownloadLink;


    @Override
    public void onCreate() {
//        ip = "192.168.42.97";
//        ip = "192.168.43.182/shoecenter/";
        ip = "http://engineerpc.ir/app/shoecenter/app/";
//        ip = "http://adinetak.ir/";
        context = getApplicationContext();
        loginUrl = ip + "users.php";
        productsUrl = ip + "products.php";
        stateAndCityUrl = ip + "stateandcity.php";
        historyUrl =  ip + "history.php";
        userCommunicationUrl = ip + "users.php";
//        dbName = "shoecenter";
//        tblCart = "carttbl";
//        tblFav = "favtbl";

//        easyDB = EasyDB.init(G.context, G.dbName)
//                .setTableName(G.tblCart)  // You can ignore this line if you want
//                .addColumn(new Column("title", "text"))
//                .addColumn(new Column("price", "text"))
//                .addColumn(new Column("amount", "text"))
//                .addColumn(new Column("img1", "text"))
//                .addColumn(new Column("size", "text"))
//                .addColumn(new Column("color", "text"))
//                .addColumn(new Column("pdid", "int"))
//                .doneTableColumn();


        super.onCreate();


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/"+getResources().getString(R.string.app_font_name))
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

//    public static void getCartCount() {
//        cartPdCount = 0;
//        Cursor res = easyDB.getAllData();
//        while (res.moveToNext()) {
//            cartPdCount++;
//        }
//
//    }
}
