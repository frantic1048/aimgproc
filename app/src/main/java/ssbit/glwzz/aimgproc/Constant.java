package ssbit.glwzz.aimgproc;


public interface Constant {
}


interface TableFiled {
    String ID = "_id";
    String ABSOLUTE_PATH = "absolutePath";
    String LAST_MODIFIED = "lastModified";
    String QUALITY = "quality";
}

interface AboutDb {
    int dbVersion = 1;
    String dbName = "almgproc";
    String tableName_quality = "quality";
}


interface Msg {
    int ScanDone = 1;
    int IdentifyDone = 2;
    int detailRequestCode = 3;
    int detailResultCode = 4;
}
interface MyExtraName{
    String pic="pic";
}