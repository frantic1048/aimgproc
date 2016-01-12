package ssbit.glwzz.aimgproc;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Calculate color purity of pictures
 * <p/>
 * Use as: ColorPurity.determine(int[] rgb, int width, int height)
 *
 * @author MYLS
 * @version 0.99
 */


public class ALG_ColorPurity {

	/* 预定义常量 */

    final static int COLOR_VALUE_MAX = 256;			/* 每个颜色值的上限 */
    final static int INTERRUPTED_DISTANCE_MAX = 3;	/* 统计时，频率的误差上限 */
    final static double THRESHOLD_FREQUENCY = 0.06;	/* 算作统计的最小值 */
    final static double PEAK_FREQUENCY = 0.16;		/* 判定为峰的最小值 */
    final static double SCALE_FREQUENCY = 0.618;		/* 结果频率的缩放比例 */

    static double estimate(int[] pix, int wid, int hgt) {

		/* 初始化 */

        double[] frequency = new double[COLOR_VALUE_MAX];
        double sum = 0;

		/* 统计灰度图的颜色频率 */
        for (int color : pix) {
            int r = Color.red(color);
            int g = Color.green(color);
            int b = Color.blue(color);
            int gray = (int) (0.114 * (double) b + 0.587 * (double) g + 0.299 * (double) r);
            frequency[gray] += 1.0;
        }

		/* 统计颜色的总数 */
        for (int i = 0; i < COLOR_VALUE_MAX; i++)
            sum += frequency[i];

		/* 统计颜色的频率 */
        for (int i = 0; i < COLOR_VALUE_MAX; i++)
            frequency[i] /= sum;

		/* 开始检查每个较大频率部分 */
        ArrayList<Double> peaks = new ArrayList<Double>();
        int dis = 0;
        double accumulation = 0;
        for (double f : frequency) {
            if (f > THRESHOLD_FREQUENCY) {
                dis = INTERRUPTED_DISTANCE_MAX;				/* 遇到高峰，则重置离散计数器 */
                accumulation += f;							/* 累计这次高峰 */
            } else {
                if (dis > 0) {
                    dis--;									/* 在离散值内，遇到缺失，忽略这个中断 */
                } else {
                    if (accumulation > PEAK_FREQUENCY)
                        peaks.add(accumulation);			/* 如果超过一个阈值，则记录这次连续累计的结果 */
                    accumulation = 0;
                }
            }
        }

		/* 更新比例 */
        double res = 0;
        if (peaks.size() > 0) {
            for (double v : peaks)
                res += v;
            res /= peaks.size();
        }
        res = Math.min(res / SCALE_FREQUENCY, 1);

		/* 测试
        System.out.println("frequency(Top 10)");
		Arrays.sort(frequency);
		for(int i = 0; i < 10; i++)
			System.out.println(frequency[i]);
		System.out.println("Peak");
		for (double d : peaks)
			System.out.println(d);
		 */

        return res;
    }

}
