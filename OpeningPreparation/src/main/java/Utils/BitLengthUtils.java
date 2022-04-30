package Utils;

import java.util.List;

public class BitLengthUtils {
    /**
     * 计算出需要的最小 bitlength
     * @param bitlength 矩阵中最值集合
     * @return
     */
    public static Integer bitlengthCal(List<Integer> bitlength) {
        int fin = 0;
        for (Integer cur : bitlength) {
            int v = (int) (Math.pow(2.0, cur) - 1);
            fin += v;
        }
        return Integer.toBinaryString(fin).length();
    }
}
