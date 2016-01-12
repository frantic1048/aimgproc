package ssbit.glwzz.aimgproc;

import android.graphics.Color;


public class ALG_Exposure {

    static double determine(int[] rgb, int width, int height) {

        double _ImageInfo = 0;
        int hist[] = new int[256];
        int amount = 0;
        for (int i = 0; i < 256; i++) {
            hist[i] = 0;
        }
        int maxHist = 0;
        for (int i = 0; i < width * height; i++) //各个灰度值的统计
        {
            int gray, r, g, b;
            r = Color.red(rgb[i]);
            g = Color.green(rgb[i]);
            b = Color.blue(rgb[i]);
            gray = (int) (0.114 * b + 0.587 * g + 0.299 * r);
            hist[gray]++;
            if (hist[gray] > maxHist) {
                maxHist = hist[gray];
            }
        }
        amount = width * height; //计算像素总数

        double lowHistProportion = 0;
        for (int i = 0; i < 10; i++) {
            lowHistProportion += hist[i];
        }
        lowHistProportion /= amount;
        double highHistProportion = 0;
        for (int i = 246; i < 256; i++) {
            highHistProportion += hist[i];
        }
        highHistProportion /= amount;
        if (lowHistProportion > 0.07 && highHistProportion < 0.07) {
            _ImageInfo = -lowHistProportion;
        } else if (lowHistProportion < 0.07 && highHistProportion > 0.07) {
            _ImageInfo = +highHistProportion;
        } else {
            _ImageInfo = 0;
        }
        return Math.abs(_ImageInfo);
    }


}

