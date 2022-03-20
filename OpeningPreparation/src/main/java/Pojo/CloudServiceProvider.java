package Pojo;

import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;
import Utils.PaillierCryptosystemUtils;
import Utils.SecureDataAggregationAlgorithmUtils;

import java.math.BigInteger;
import java.util.List;

public class CloudServiceProvider {
    public BigInteger[][] localTrainingDataAggregation(List<BigInteger[][]> list, PublicParameters pp, SK_CSP sk) {
        BigInteger[][] org = list.get(0);
        int d = org.length - 1;
        for (int n = 1; n < list.size() - 1; n++) {
            BigInteger[][] multiply = list.get(n);
            for (int i = 0; i <= d; i++) {
                for (int j = 0; j <= d; j++) {
                    BigInteger cur = org[i][j];
                    org[i][j] = cur.multiply(multiply[i][j]);
                }
            }
        }
        for (int i = 0; i <= d; i++) {
            for (int j = 0; j <= d; j++) {
                BigInteger cur = org[i][j];
                cur = SecureDataAggregationAlgorithmUtils.DataAggregation(cur,pp);
                org[i][j]=SecureDataAggregationAlgorithmUtils.AggregatedResultDecryption(cur,pp,sk);
            }
        }
        return org;
    }
}
