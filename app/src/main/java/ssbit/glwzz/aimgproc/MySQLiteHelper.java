package ssbit.glwzz.aimgproc;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class MySQLiteHelper extends SQLiteOpenHelper {

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + AboutDb.tableName_quality + "("
                + TableFiled.ID + " integer primary key autoincrement,"
                + TableFiled.ABSOLUTE_PATH + " text,"
                + TableFiled.LAST_MODIFIED + " integer,"
                + TableFiled.QUALITY + " integer"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}



