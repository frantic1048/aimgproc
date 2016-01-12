package ssbit.glwzz.aimgproc;

import android.graphics.Color;

/**
 * Calculate noise of pictures
 * <p/>
 * Use as: ImageNoise.determine(int[] rgb, int width, int height)
 *
 * @author MYLS
 * @version 0.99
 */


public class ALG_ImageNoise {

    static double estimate(int[] pix, int wid, int hgt) {

		/*
         * 参考资料：
		 * 
		 * [How can I measure image noise]
		 * (http://stackoverflow.com/questions/8960462/how-can-i-measure-image-noise)
		 * 
		 * [Noise Estimation / Noise Measurement in Image]
		 * (http://stackoverflow.com/questions/2440504/noise-estimation-noise-measurement-in-image)
		 */
		
		/* 转化 pix 为灰度图 */

        int[] gray = new int[pix.length];
        for (int i = 0; i < pix.length; i++)
            gray[i] = (Color.red(pix[i]) + Color.green(pix[i]) + Color.blue(pix[i])) / 3;
		
		/* 对灰度图执行卷积：
		 * 其中 M = 
		 *    [[ 1, -2,  1],
		 *     [-2,  4, -2],
		 *     [ 1, -2,  1]]
		 */

        int[] convolution_result = new int[pix.length];
        for (int y = 1, y_max = hgt - 1; y < y_max; y++) {
            int delta_x = y * wid;				/* 这些辅助的一堆变量都是为了优化：减少乘法 */
            int last_delta_x = delta_x - wid;
            int next_delta_x = delta_x + wid;
            for (int x = 1, x_max = wid - 1; x < x_max; x++) {
                int index = x + delta_x;
                int last_x = x - 1;
                int next_x = x + 1;
                int up_down_left_right
                        = gray[x + last_delta_x]
                        + gray[x + next_delta_x]
                        + gray[next_x + delta_x]
                        + gray[last_x + delta_x];
                int corners
                        = gray[last_x + last_delta_x]
                        + gray[next_x + last_delta_x]
                        + gray[last_x + next_delta_x]
                        + gray[next_x + next_delta_x];
                convolution_result[index] = (gray[index] << 2) - (up_down_left_right << 1) + corners;
            }
        }
		
		/* 求矩阵绝对值的和 */
        int sum = 0;
        for (int v : convolution_result)
            sum += (v < 0) ? -v : v;

		/* 按照公式求取结果 */
        double sigma = sum * Math.sqrt(0.5 * Math.PI) / (6 * (wid - 2) * (hgt - 2));
		
		/* 将结果映射到 [0, 1] 区间并输出 */
        sigma = Math.min(sigma * sigma / 10.0, 1);

        return sigma;
    }
}
