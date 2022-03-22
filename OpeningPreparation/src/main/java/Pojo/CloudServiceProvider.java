package Pojo;

import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;
import Utils.SecureDataAggregationAlgorithmUtils;

import java.math.BigInteger;
import java.util.List;

public class CloudServiceProvider {
    public BigInteger[][] localTrainingDataAggregation(List<BigInteger[][]> list, PublicParameters pp, SK_CSP sk) {
        int d = list.get(0).length;
        BigInteger[][] fin = new BigInteger[d][d];
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                fin[i][j] = new BigInteger("1");
            }
        }
        for (int n = 0; n < list.size() - 1; n++) {
            BigInteger[][] multiply = list.get(n);
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    BigInteger cur = fin[i][j];
                    fin[i][j] = cur.multiply(multiply[i][j]);
                }
            }
        }
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                BigInteger cur = fin[i][j];
                cur = SecureDataAggregationAlgorithmUtils.DataAggregation(cur, pp);
                fin[i][j] = SecureDataAggregationAlgorithmUtils.AggregatedResultDecryption(cur, pp, sk);
            }
        }
        return fin;
    }
}
