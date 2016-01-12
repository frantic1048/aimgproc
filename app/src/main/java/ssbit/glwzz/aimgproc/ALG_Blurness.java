package ssbit.glwzz.aimgproc;

import android.graphics.Color;

/**
 * 功能：对过度模糊的照片的判断
 * 日期：2016-01-12
 * 作者：郭珈豪
 */
public class ALG_Blurness {
    public static double blurness(int[] rgb, int width, int height) {

        int blurness = 1;
        for (int left = 0; left < width; left += 1) {
            for (int top = 0; top < height; top += 1) {
                /**
                 * 对 (left, top) 位置的像素取亮度值（HSV 模型）用如下核卷积
                 * 0  1  0
                 * 1 -4  1
                 * 0  1  0
                 * 对图像之外的位置全取 0 值计算
                 */
                int result = 0;
                if (top > 0) {
                    /** 不在上边缘，计算上侧像素 */
                    int c = rgb[width * (top - 1) + left];
                    int value = Math.max(Color.red(c), Math.max(Color.green(c), Color.blue(c)));
                    result += value * 1;
                }
                if (left > 0) {
                    /** 不在左边缘，计算左侧像素 */
                    int c = rgb[width * top + (left - 1)];
                    int value = Math.max(Color.red(c), Math.max(Color.green(c), Color.blue(c)));
                    result += value * 1;
                }
                if (left < width - 1) {
                    /** 不在右边缘，计算右侧像素 */
                    int c = rgb[width * top + (left + 1)];
                    int value = Math.max(Color.red(c), Math.max(Color.green(c), Color.blue(c)));
                    result += value * 1;
                }
                if (top < height - 1) {
                    /** 不在下边缘，计算下侧像素 */
                    int c = rgb[width * (top + 1) + left];
                    int value = Math.max(Color.red(c), Math.max(Color.green(c), Color.blue(c)));
                    result += value * 1;
                }
                /** 计算当前位置像素 */
                int c = rgb[width * top + left];
                int value = Math.max(Color.red(c), Math.max(Color.green(c), Color.blue(c)));
                result += value * (-4);

                /** 对 0 值计数 */
                if (result < 1) {
                    blurness += 1;
                }
            }
        }

        /*1表示百分百质量差，0表示百分百质量好*/
        /**
         * 推荐阈值：
         * 好：[0, 0.62]
         * 不好： [0.62, 1]
         */
        return (float) blurness / rgb.length;
    }
}
