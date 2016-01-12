package ssbit.glwzz.aimgproc;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;

import java.util.Arrays;


public class Algorithm {
    private static int[] px;

    public static boolean getQuality(OnePic pic) {
        /*true表示高质量，false表示低质量*/
        Bitmap bitmap = BitmapFactory.decodeFile(pic.getPath());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        /*复用int数组减少内存占用*/
        if (px == null) {
            px = new int[width * height];
        } else if (px.length != width * height) {
            Log.i("pxLength", px.length + "");
            px = new int[width * height];
        }
        /*测试这一段需要在identifier中注释掉haveRecord*/
        bitmap.getPixels(px, 0, width, 0, 0, width, height);

        return synthesize(
                new double[]{
                        alg_1(px, width, height),
                        alg_2(px, width, height),
                        alg_3(px, width, height),
                        alg_4(px, width, height)
                }

        );
    }

    public static boolean synthesize(double arr[]) {
        /*综合各种因素来判断照片质量是否属于高质量*/
        Log.i("质量", Arrays.toString(arr));
        double[] sill = new double[]{0.5, 0.07, 0.5, 0.58};
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > sill[i]) {
                return false;
            }
        }
        return true;

    }

    /*纯色*/
    public static double alg_1(int[] px, int width, int height) {

        return ALG_ColorPurity.estimate(px, width, height);
    }

    /*过曝*/
    public static double alg_2(int[] px, int width, int height) {
        return ALG_Exposure.determine(px, width, height);
    }

    /*噪声*/
    public static double alg_3(int[] px, int width, int height) {
        return ALG_ImageNoise.estimate(px, width, height);
    }

    /*模糊*/
    public static double alg_4(int[] px, int width, int height) {
        return ALG_Blurness.blurness(px, width, height);
    }
}
