package Utils;

import java.util.ArrayList;
import java.util.List;

public class DataNormalizationUtils {
    /**
     * 求最大值
     * @param lists
     * @return
     */
    public static List<Double> maxCalculate(List<List<Double>> lists){
        List<Double> maxFeature = new ArrayList<>(lists.get(0));
        for (int count = 1; count < lists.size(); count++) {
            List<Double> record = lists.get(count);
            for (int d = 0; d < record.size(); d++) {
                Double current = record.get(d);
                // 比较更新 max
                if (maxFeature.get(d) <= current) maxFeature.set(d, current);
            }
        }
        return maxFeature;
    }

    /**
     * 求最小值
     * @param lists
     * @return
     */
    public static List<Double> minCalculate(List<List<Double>> lists){
        List<Double> minFeature = new ArrayList<>(lists.get(0));
        for (int count = 1; count < lists.size(); count++) {
            List<Double> record = lists.get(count);
            for (int d = 0; d < record.size(); d++) {
                Double current = record.get(d);
                // 比较更新 min
                if (minFeature.get(d) >= current) minFeature.set(d, current);
            }
        }
        return minFeature;
    }
}
