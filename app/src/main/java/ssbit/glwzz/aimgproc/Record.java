package ssbit.glwzz.aimgproc;


import android.content.ContentValues;

import java.io.File;

public class Record {
    private int id;
    private String absolutePath;
    private long lastModified;
    private boolean quality;


    public Record(OnePic pic) {
        this.absolutePath = pic.getPath();
        this.lastModified = pic.getLastModified();
        this.quality=pic.getHighQuality();
    }

    public Record(int id, String absolutePath, long lastModified, boolean quality) {
        this.id = id;
        this.absolutePath = absolutePath;
        this.lastModified = lastModified;
        this.quality = quality;
    }

    public Record(int id, OnePic pic) {
        this(pic);
        this.id = id;
    }

    public boolean match(OnePic pic) {
        if (pic.getPath().equals(absolutePath)) {
            File file = new File(pic.getPath());
            if (file.lastModified() == this.lastModified) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public boolean getQuality() {
        return quality;
    }

    public ContentValues toContentValues(boolean withId) {
        ContentValues cv = new ContentValues();
        if(withId){

            cv.put(TableFiled.ID, this.getId());
        }
        cv.put(TableFiled.ABSOLUTE_PATH, this.absolutePath);
        cv.put(TableFiled.LAST_MODIFIED, this.lastModified);
        cv.put(TableFiled.QUALITY, this.quality);
        return cv;
    }

    public int getId() {
        return this.id;
    }
}