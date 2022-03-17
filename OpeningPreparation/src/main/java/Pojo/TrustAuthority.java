package Pojo;

import Pojo.Keys.Keys;
import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;
import Pojo.Keys.SK_DO;
import Utils.BigIntegerUtils;
import Utils.DataNormalizationUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class TrustAuthority {
    /**
     * 准确度
     */
    private final static int certainty = 64;
    private final static int kapa = 512;

    /**
     * keyGenerate
     *
     * @param m DOs 的数量
     * @return
     */
    public HashMap<String, Keys[]> keyGenerate(int m) {
        // paillier 加密生成公私钥 (N,g) 和 (μ,λ)
        HashMap<String, BigInteger[]> kg = paillierKeyGeneration();
        BigInteger[] pk_p = kg.get("PK_p");
        BigInteger N = pk_p[0];
        BigInteger g = pk_p[1];
        BigInteger[] sk_p = kg.get("SK_p");
        BigInteger miu = sk_p[0];
        BigInteger lamda = sk_p[1];

        /*
         * TA selects a large random integer γ
         * satisfying |γ| < κ/2 and gcd(k,γ) = 1
         *
         * 这里由于先前 g = N + 1，k = 1
         */
        BigInteger gama = new BigInteger(kapa / 2, certainty, new Random());
        // h = g^γ mod N^2
        BigInteger Nsquare = N.multiply(N);
        BigInteger h = g.modPow(gama, Nsquare);
        PublicParameters PP = new PublicParameters(kapa, N, g, h);

        // splits N into m random numbers{n_1,n_2...n_m}
        BigInteger[] splits = splits(N, m);

        /*
         * a random number as the task ID for every data aggregation task
         */
        BigInteger R_t = BigIntegerUtils.validRandomInResidueSystem(N);

        /*
         * the secret key for each DO_i
         * 数据所有者密钥
         */
        SK_DO[] SK_DOi = new SK_DO[m];
        for (int i = 0; i < m; i++) {
            // SK_DOi = R_t^n_i mod N^2
            SK_DOi[i] = new SK_DO(R_t.modPow(splits[i], Nsquare));
        }

        /*
         * the secret key for each CSP
         * 云服务提供商密钥
         */
        SK_CSP SK_CSP = new SK_CSP(lamda, miu, gama);


        return new HashMap<String, Keys[]>() {{
            put("PP", new Keys[]{PP});
            put("SK_DO", SK_DOi);
            put("SK_CSP", new Keys[]{SK_CSP});
        }};
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
        for (int i = 0; i < maxFeature.size(); i++) {
            // 防止精度丢失
            maxFeature.set(i, BigDecimal.valueOf(maxFeature.get(i)).add(BigDecimal.valueOf(new Random().nextDouble()).setScale(2, BigDecimal.ROUND_HALF_UP)).doubleValue());
            minFeature.set(i, BigDecimal.valueOf(minFeature.get(i)).subtract(BigDecimal.valueOf(new Random().nextDouble()).setScale(2, BigDecimal.ROUND_HALF_UP)).doubleValue());
        }

        return new List[]{maxFeature, minFeature, Collections.singletonList(n)};
    }

    /**
     * paillier 加密得到公钥和私钥
     */
    private static HashMap<String, BigInteger[]> paillierKeyGeneration() {
        // BigInteger(int bitLength ,int certainty ,Random rnd)
        // 生成 BigInteger 伪随机数，它可能是（概率不小于 1 - 1/2^certainty）一个具有指定 bitLength 的素数
        BigInteger p = new BigInteger(kapa, certainty, new Random());
        BigInteger q = new BigInteger(kapa, certainty, new Random());

        // 计算 p 和 q 的乘积 N 以及 N^2
        BigInteger N = p.multiply(q);
        BigInteger Nsquare = N.multiply(N);

        // 计算 λ = lcm(p-1,q-1) = (p-1) * (q-1) / gcd(p-1, q-1)
        BigInteger lamda = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));

        // 这里 g 不取随机数，直接使用 N + 1 。此时对于这个群，g^kN=(N + 1)^kN = 1，k = 1
        BigInteger g = N.add(BigInteger.ONE);
        BigInteger miu = BigIntegerUtils.functionL(g.modPow(lamda, Nsquare), N).modInverse(N);

        return new HashMap<String, BigInteger[]>() {{
            put("PK_p", new BigInteger[]{N, g});
            put("SK_p", new BigInteger[]{miu, lamda});
        }};
    }

    /**
     * https://www.icode9.com/content-1-362158.html
     * https://stackoverflow.com/questions/2640053/getting-n-random-numbers-whose-sum-is-m
     * 生成 0 到 1 之间的 m - 1 个随机，将数字 0 和 1 本身添加到列表中，排序，取相邻数字的差值 ---> 均匀分布。
     */
    private static BigInteger[] splits(BigInteger N, int m) {
        BigInteger[] result = new BigInteger[m];
        BigInteger[] v = new BigInteger[m];

        // 生成 m - 1 随机数
        for (int i = 0; i < m - 1; i++) {
            v[i] = new BigInteger(N.bitLength(), new Random()).mod(N);
        }
        v[m - 1] = N;

        Arrays.sort(v);
        result[0] = v[0];
        for (int i = 1; i < m; i++) {
            // 取相邻数字的差值
            result[i] = v[i].subtract(v[i - 1]);
        }
        return result;
    }
}
