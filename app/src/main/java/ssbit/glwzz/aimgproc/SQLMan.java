package ssbit.glwzz.aimgproc;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


class SQLMan {
    private Context context;
    private SQLiteDatabase myDb;

    public SQLMan(Context context) {
        this.context = context;
    }

    private void getMyDb() {
        myDb = new MySQLiteHelper(context, AboutDb.dbName,
                null, AboutDb.dbVersion).getReadableDatabase();
    }

    public int createRecord(Record record) {
        getMyDb();
        long tmp_id = myDb.insert(AboutDb.tableName_quality, null, record.toContentValues(false));
        myDb.close();
        //要不要考虑插入失败的情况以及id到底要不要设置成为long
        return (int) tmp_id;
        //TODO 数据库到底要不要随时关闭？
    }

    //目前只考虑整表读取，单条修改以及单条的插入，不考虑删除
    private final static String Qs_readAllEach = "select * from " + AboutDb.tableName_quality;

    public Record[] readAllRecord() {
        getMyDb();
        Cursor c = myDb.rawQuery(Qs_readAllEach, null);
        Record[] allRecord = new Record[c.getCount()];
        int index__id = c.getColumnIndex(TableFiled.ID);
        int index_absolutePath = c.getColumnIndex(TableFiled.ABSOLUTE_PATH);
        int index_lastModified = c.getColumnIndex(TableFiled.LAST_MODIFIED);
        int index_quality = c.getColumnIndex(TableFiled.QUALITY);
        int i = 0;
        while (c.moveToNext()) {
            allRecord[i++] = new Record(
                    c.getInt(index__id),
                    c.getString(index_absolutePath),
                    c.getLong(index_lastModified),
                    (c.getInt(index_quality) != 0)
            );
        }
        c.close();
        myDb.close();
        return allRecord;
    }

    private final static String Qs_updateOneRecord = TableFiled.ID + "=?";

    public void updateOneRecord(Record record) {
        getMyDb();
        myDb.update(AboutDb.tableName_quality,
                record.toContentValues(true),
                Qs_updateOneRecord,
                new String[]{Integer.toString(record.getId())});
        myDb.close();
    }


}