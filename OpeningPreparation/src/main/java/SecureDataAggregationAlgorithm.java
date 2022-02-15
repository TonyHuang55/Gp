import Utils.BigIntegerUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class SecureDataAggregationAlgorithm {
    /**
     * 准确度
     */
    private final static int certainty = 64;

    /**
     * the number of DOs
     */
    private static int m = 3;

    private static int κ;

    public SecureDataAggregationAlgorithm(int securityParameter) {
        κ = securityParameter;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            // keyGeneration
            HashMap<String, BigInteger[]> keyGeneration = keyGeneration(512);
            BigInteger[] SK_DO = keyGeneration.get("SK_DO");
            BigInteger[] PP = keyGeneration.get("PP");
            BigInteger[] SK_CSP = keyGeneration.get("SK_CSP");
            BigInteger N = PP[1];
            BigInteger[] x = getRandomX(N);
            BigInteger[] r = getRandomR(512);
            // DataEncryption
            BigInteger[] X = DataEncryption(x, SK_DO, PP, r);
            // DataAggregation
            BigInteger XSum = DataAggregation(X, PP);
            // AggregatedResultDecryption
            BigInteger res = AggregatedResultDecryption(XSum, SK_CSP, PP);

            for (String s : keyGeneration.keySet()) {
                System.out.println(s + ":" + Arrays.toString(keyGeneration.get(s)));
            }
            System.out.println("[[x^(i)]]:" + Arrays.toString(X));
            System.out.println("[[x]]:" + XSum);
            System.out.println("res:" + res);
        }

    }

    public static HashMap<String, BigInteger[]> keyGeneration(int securityParameter) {
        /**
         * security parameter
         */
        int κ = securityParameter;
        PaillierCryptosystem paillierCryptosystem = new PaillierCryptosystem(κ);
        BigInteger[] PK_p = paillierCryptosystem.getPK();
        BigInteger[] SK_p = paillierCryptosystem.getSK();

        BigInteger N = PK_p[0];
        BigInteger Nsquare = N.multiply(N);
        BigInteger g = PK_p[1];
        BigInteger μ = SK_p[0];
        BigInteger λ = SK_p[1];

        /*
         * TA selects a large random integer γ
         * satisfying |γ| < κ/2 and gcd(k,γ) = 1
         *
         * 这里由于先前 g = N + 1，k = 1
         */
        BigInteger γ = new BigInteger(κ / 2, certainty, new Random());

        // h = g^γ mod N^2
        BigInteger h = g.modPow(γ, Nsquare);

        /*
         * public parameters
         */
        BigInteger[] PP = new BigInteger[]{new BigInteger(κ + ""), N, g, h};

        BigInteger[] splits = splits(N, m);

        /*
         * a random number as the task ID for every data aggregation task
         */
        BigInteger R_t = BigIntegerUtils.validRandomInResidueSystem(N);

        /*
         * the secret key for each DO_i
         * 数据所有者密钥
         */
        BigInteger[] SK_DO = new BigInteger[m];
        for (int i = 0; i < m; i++) {
            // SK_DOi = R_t^n_i mod N^2
            SK_DO[i] = R_t.modPow(splits[i], Nsquare);
        }

        /*
         * the secret key for each CSP
         * 云服务提供商密钥
         */
        BigInteger[] SK_CSP = new BigInteger[]{λ, μ, γ};

        return new HashMap<String, BigInteger[]>() {{
            put("PP", PP);
            put("SK_DO", SK_DO);
            put("SK_CSP", SK_CSP);
        }};
    }


    public static BigInteger[] DataEncryption(BigInteger[] x, BigInteger[] SK_DO, BigInteger[] PP, BigInteger[] r) {
        BigInteger N = PP[1], g = PP[2], h = PP[3];
        BigInteger[] X = new BigInteger[m];
        for (int i = 0; i < m; i++) {
            X[i] = g.modPow(x[i], N.multiply(N)).multiply(h.modPow(r[i], N.multiply(N))).multiply(SK_DO[i]).mod(N.multiply(N));
        }
        return X;
    }

    public static BigInteger DataAggregation(BigInteger[] X, BigInteger[] PP) {
        BigInteger N = PP[1];
        BigInteger XSum = BigInteger.ZERO;
        for (BigInteger xi : X) {
            XSum = XSum.add(xi.mod(N.multiply(N)));
        }
        return XSum.mod(N.multiply(N));
    }

    public static BigInteger AggregatedResultDecryption(BigInteger XSum, BigInteger[] SK_CSP, BigInteger[] PP) {
        BigInteger N = PP[1];
        BigInteger λ = SK_CSP[0], μ = SK_CSP[1], γ = SK_CSP[2];
        return BigIntegerUtils.functionL(XSum.modPow(λ, N.multiply(N)).multiply(μ), N).mod(N).mod(γ);
    }

    private static BigInteger[] getRandomX(BigInteger N) {
        // a private message of DO_i
        BigInteger[] x = new BigInteger[m];
        for (int i = 0; i < m; i++) {
            x[i] = BigIntegerUtils.validRandomInResidueSystem(N);
        }
        return x;
    }

    private static BigInteger[] getRandomR(int κ) {
        // a random number which satisfies |r_i| < κ/2
        BigInteger[] r = new BigInteger[m];
        for (int i = 0; i < m; i++) {
            r[i] = new BigInteger(κ / 2, certainty, new Random());
        }
        return r;
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
