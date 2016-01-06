package ssbit.glwzz.aimgproc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;


public class Algorithm {
    private static int[] px;
    public static boolean getQuality(OnePic pic){
        /*true表示高质量，false表示低质量*/
        Bitmap bitmap=BitmapFactory.decodeFile(pic.getPath());
        int width=bitmap.getWidth();
        int height=bitmap.getHeight();
        /*复用int数组减少内存占用*/
        if(px==null){
            px=new int[width*height];
        }else if(px.length!=width*height){
            Log.i("pxLength",px.length+"");
            px=new int[width*height];
        }
        /*测试这一段需要在identifier中注释掉haveRecord*/
        bitmap.getPixels(px,0,width,0,0,width,height);
        Log.i("bitmapPx","px.Length:"+px.length+",width:"+width+",height:"+height);

        return synthesize(
                alg_1(px,width,height),
                alg_2(px, width, height)
        );
    }
    public  static  boolean synthesize(double a,double b){
        /*综合各种因素来判断照片质量是否属于高质量*/
        if(a>0.3||b>0.3){
            return false;
        }else{
            return true;
        }

    }
    public static double alg_1(int[] px,int width,int height){

        return Color.red(px[width/2+height])/255.0;
    }
    public static double alg_2(int[] px,int width,int height){
        return Color.green(px[height/2+width])/255.0;
    }
}
