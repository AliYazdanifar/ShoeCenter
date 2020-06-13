package ir.engineerpc.shoecenter.MyClass;


import android.content.Context;
import android.database.Cursor;

import p32929.androideasysql_library.Column;
import p32929.androideasysql_library.EasyDB;

//before use you must implementation 'com.github.p32929:AndroidEasySQL-Library:1.4.0'
public class MyDatabaseManager {

    public static final int CLMN_ID = 0, CLMN_TITLE = 1, CLMN_PRICE = 2, CLMN_AMOUNT = 3, CLMN_IMG = 4, CLMN_SIZE = 5, CLMN_COLOR = 6, CLMN_PDID = 7;
    public int cartPdCount;
    public EasyDB easyDB;
    private Context context;

    public MyDatabaseManager(Context context) {
        this.context = context;
        String dbName = "shoecenter";
        String tblCart = "carttbl";

//        initDatabae(dbName, tblCart);

        easyDB = EasyDB.init(context, dbName)// name of the DATABASE
                .setTableName(tblCart)  // You can ignore this line if you want
                //You don't have to add any primary key. The library does it by default (as ID column)
                .addColumn(new Column("title", "text"))// Contrains like "text", "unique", "not null" are not case sensitive
                .addColumn(new Column("price", "text"))
                .addColumn(new Column("amount", "text"))
                .addColumn(new Column("img1", "text"))
                .addColumn(new Column("size", "text"))
                .addColumn(new Column("color", "text"))
                .addColumn(new Column("pdid", "int"))
                .doneTableColumn();


    }


    public boolean addToTblCart(String title, String price, String amount, String img, String size, String color, int pdId) {
        return easyDB
                .addData("title", title)
                .addData("price", price)
                .addData("amount", amount)
                .addData("img1", img)
                .addData("size", size)
                .addData("color", color)
                .addData("pdid", pdId)
                .doneDataAdding();
    }

    public void getCartCount() {
        cartPdCount = 0;
        Cursor res = easyDB.getAllData();
        while (res.moveToNext()) {
            cartPdCount++;
        }

    }

}
