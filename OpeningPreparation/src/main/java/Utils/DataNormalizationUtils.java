package Utils;

import java.util.ArrayList;
import java.util.List;

public class DataNormalizationUtils {
    public static List<Double> maxCalculate(List<List<Double>> lists){
        List<Double> maxFeature = new ArrayList<>();
        List<Double> expRecord = lists.get(0);
        for (int d = 0; d < expRecord.size(); d++) {
            maxFeature.add(expRecord.get(d));
        }

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

    public static List<Double> minCalculate(List<List<Double>> lists){
        List<Double> minFeature = new ArrayList<>();
        List<Double> expRecord = lists.get(0);
        for (int d = 0; d < expRecord.size(); d++) {
            minFeature.add(expRecord.get(d));
        }

        for (int count = 1; count < lists.size(); count++) {
            List<Double> record = lists.get(count);
            for (int d = 0; d < record.size(); d++) {
                Double current = record.get(d);
                // 比较更新 max
                if (minFeature.get(d) >= current) minFeature.set(d, current);
            }
        }
        return minFeature;
    }
}
