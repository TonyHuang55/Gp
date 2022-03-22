package Pojo;

import Pojo.Keys.Keys;
import Utils.DataNormalizationUtils;
import Utils.SecureDataAggregationAlgorithmUtils;

import java.math.BigDecimal;
import java.util.*;

public class TrustAuthority {
    /**
     * keyGenerate
     *
     * @param m DOs 的数量
     * @return
     */
    public HashMap<String, Keys[]> keyGenerate(int m) {
        return SecureDataAggregationAlgorithmUtils.keyGenerate(m);
    }

    public List[] globalDataNormalization(List<List[]> DOs) {
        List<List<Double>> max = new ArrayList<>();
        List<List<Double>> min = new ArrayList<>();
        int n = 0;
        for (List[] DO : DOs) {
            max.add(DO[0]);
            min.add(DO[1]);
            n += Double.parseDouble(String.valueOf(DO[2].get(0)));
        }
        List<Double> maxFeature = DataNormalizationUtils.maxCalculate(max);
        List<Double> minFeature = DataNormalizationUtils.minCalculate(min);
//        for (int i = 0; i < maxFeature.size(); i++) {
//            // 防止精度丢失
//            maxFeature.set(i, BigDecimal.valueOf(maxFeature.get(i)).add(BigDecimal.valueOf(new Random().nextDouble()).setScale(2, BigDecimal.ROUND_HALF_UP)).doubleValue());
//            minFeature.set(i, BigDecimal.valueOf(minFeature.get(i)).subtract(BigDecimal.valueOf(new Random().nextDouble()).setScale(2, BigDecimal.ROUND_HALF_UP)).doubleValue());
//        }

        return new List[]{maxFeature, minFeature, Collections.singletonList(n)};
    }

}
