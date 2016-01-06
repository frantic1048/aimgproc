package ssbit.glwzz.aimgproc;

import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.os.Handler;
import android.view.WindowManager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;


public class PicScanner implements Runnable {
//    private Context context;
    private Handler handler;
    private ArrayList<OnePic> pics = new ArrayList<>();
    private Point windowSize = new Point();

    public PicScanner(Context context, Handler handler) {
//        this.context = context;
        this.handler = handler;
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)
        ).getDefaultDisplay().getSize(windowSize);
    }

    @Override
    public void run() {
        File folder_DCIM = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM);
        System.out.println(folder_DCIM.getAbsolutePath());
        File[] folder_Camera_array = folder_DCIM.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.equals("Camera");
            }
        });
        if (folder_Camera_array.length == 1) {
            File[] photos = folder_Camera_array[0].listFiles(
                    new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            return filename.endsWith(".jpg");
                        }
                    }
            );
            /*仅调试使用，为了保证速度最多扫描20张图片*/
            int i=0,maxAmount=20;
            for (File fp : photos) {
                if(i++>maxAmount){
                    break;
                }
                pics.add(new OnePic(windowSize, fp.getAbsolutePath()));
            }
        }
        handler.sendEmptyMessage(Msg.ScanDone);
    }

    public ArrayList<OnePic> getPics() {
        return this.pics;
    }
}
