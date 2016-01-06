package ssbit.glwzz.aimgproc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

import java.io.File;
import java.io.Serializable;

public class OnePic implements Serializable {
    private int idInDb;
    private File file;
    private boolean identified;
    private boolean highQuality;
    private int windowWidth;
    //    private int windowHeight;
    private int picWidth;
    private transient Bitmap bitmap;


    public void setIdInDb(int idInDb) {
        this.idInDb = idInDb;
    }

    public int getIdInDb() {
        return this.idInDb;
    }

    public OnePic(Point windowSize, String path) {
        this.windowWidth = windowSize.x;
//        this.windowHeight = windowSize.y;
        BitmapFactory.Options scanOpt = new BitmapFactory.Options();
        scanOpt.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, scanOpt);
        picWidth = scanOpt.outWidth;
        scanOpt.inJustDecodeBounds = false;
        /*横屏一行四张，竖屏一行两张*/
        int row = windowSize.y > windowSize.x ? 2 : 4;
        /*多除一个二是为了减少内存占用*/
        scanOpt.inSampleSize = scanOpt.outWidth / (windowSize.x / row / 2);
        this.bitmap = BitmapFactory.decodeFile(path, scanOpt);

        this.file = new File(path);
    }

    public Bitmap getBitmap() {
        return this.bitmap;
    }

    public Bitmap getBitmapBig() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = picWidth > windowWidth ? picWidth / windowWidth : 1;
        return BitmapFactory.decodeFile(getPath(), options);
    }

    public void setHighQuality(boolean q) {
        this.highQuality = q;
    }

    public boolean getHighQuality() {
        return this.highQuality;
    }

    public String getPath() {
        return this.file.getPath();

    }

    public long getLastModified() {
        return this.file.lastModified();
    }

    public void toggleHighQuality() {
        this.highQuality = !this.highQuality;
    }

    public boolean isIdentified() {
        return this.identified;
    }

    public void makeIdentified() {
        this.identified = true;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof OnePic) {
            OnePic pic2 = ((OnePic) o);
            return (this.file.getAbsolutePath().equals(
                    pic2.getPath()) &&
                    this.file.lastModified() == pic2.getLastModified()
            );
        } else {
            return false;
        }


    }
}


