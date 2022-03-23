package Pojo;

import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;
import Utils.SecureDataAggregationAlgorithmUtils;

import java.math.BigInteger;
import java.util.List;

public class CloudServiceProvider {
    /**
     * 聚合并解密
     * @param M
     * @param pp
     * @param sk_csp
     * @param R
     * @return
     */
    public BigInteger[][] localTrainingDataAggregation(List<BigInteger[][]> M, PublicParameters pp, SK_CSP sk_csp, List<BigInteger[][]> R) {
        int d = M.get(0).length;
        BigInteger[][] fin = new BigInteger[d][d];
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                fin[i][j] = new BigInteger("1");
            }
        }
        // 聚合
        for (int n = 0; n < M.size(); n++) {
            BigInteger[][] multiply = M.get(n);
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    BigInteger cur = fin[i][j];
                    fin[i][j] = cur.multiply(multiply[i][j]);
                }
            }
        }

        // 解密
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                BigInteger cur = fin[i][j];
                cur = SecureDataAggregationAlgorithmUtils.DataAggregation(cur, pp);
                BigInteger ri = new BigInteger("0");
                for (int k = 0; k < R.size(); k++) {
                    ri=ri.add(R.get(k)[i][j]);
                }
                fin[i][j] = SecureDataAggregationAlgorithmUtils.AggregatedResultDecryption(cur, pp, sk_csp, ri);
            }
        }
        return fin;
    }
}
