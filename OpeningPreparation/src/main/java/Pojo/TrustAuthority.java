package Pojo;

import Pojo.Keys.Keys;
import Utils.DataNormalizationUtils;
import Utils.SecureDataAggregationAlgorithmUtils;

import java.math.BigDecimal;
import java.util.*;

public class TrustAuthority {
    /**
     * 密钥生成
     *
     * @param m DOs 的数量
     * @return
     */
    public HashMap<String, Keys[]> keyGenerate(int m) {
        return SecureDataAggregationAlgorithmUtils.keyGenerate(m);
    }

    /**
     * 计算各个维度的最值 & 扰乱最值
     *
     * @param DOs DOs
     * @return 最值向量集合
     */
    public List[] globalDataNormalization(List<List[]> DOs) {
        List<List<Double>> max = new ArrayList<>();
        List<List<Double>> min = new ArrayList<>();
        Double maxTarget = Double.MIN_VALUE;
        Double minTarget = Double.MAX_VALUE;
        int n = 0;
        for (List[] DO : DOs) {
            max.add(DO[0]);
            min.add(DO[1]);
            n += Double.parseDouble(String.valueOf(DO[2].get(0)));
            double curMaxT = Double.parseDouble(String.valueOf(DO[3].get(0)));
            double curMinT = Double.parseDouble(String.valueOf(DO[3].get(1)));
            maxTarget = curMaxT >= maxTarget ? curMaxT : maxTarget;
            minTarget = curMinT <= minTarget ? curMinT : minTarget;
        }
        List<Double> maxFeature = DataNormalizationUtils.maxCalculate(max);
        List<Double> minFeature = DataNormalizationUtils.minCalculate(min);
        // disturbs
        for (int i = 0; i < maxFeature.size(); i++) {
            // 防止精度丢失
            maxFeature.set(i, BigDecimal.valueOf(maxFeature.get(i)).add(BigDecimal.valueOf(new Random().nextDouble()).setScale(2, BigDecimal.ROUND_HALF_UP)).doubleValue());
            minFeature.set(i, BigDecimal.valueOf(minFeature.get(i)).subtract(BigDecimal.valueOf(new Random().nextDouble()).setScale(2, BigDecimal.ROUND_HALF_UP)).doubleValue());
        }

        return new List[]{maxFeature, minFeature, Collections.singletonList(n), Arrays.asList(maxTarget, minTarget)};
    }

}
