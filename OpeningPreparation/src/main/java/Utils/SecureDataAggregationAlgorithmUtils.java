package Utils;

import Pojo.Keys.Keys;
import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;
import Pojo.Keys.SK_DO;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class SecureDataAggregationAlgorithmUtils {
    /**
     * 准确度
     */
    private final static int certainty = 64;
    private final static int kapa = 1024;

    /**
     * keyGenerate
     *
     * @param m DOs 的数量
     * @return
     */
    public static HashMap<String, Keys[]> keyGenerate(int m) {
        // paillier 加密生成公私钥 (N,g) 和 (μ,λ)
        HashMap<String, BigInteger[]> kg = paillierKeyGeneration();
        BigInteger[] pk_p = kg.get("PK_p");
        BigInteger N = pk_p[0], g = pk_p[1];
        BigInteger[] sk_p = kg.get("SK_p");
        BigInteger miu = sk_p[0], lamda = sk_p[1];

        /*
         * TA selects a large random integer γ
         * satisfying |γ| < κ/2 and gcd(k,γ) = 1
         *
         * 这里由于先前 g = N + 1，k = 1
         */

        int bl;
        do {
            bl = new Random().nextInt(kapa / 2 - 1) + 1;
        } while (bl == 1);
        BigInteger gama = new BigInteger(bl, certainty, new Random());
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
            put("PP", new PublicParameters[]{PP});
            put("SK_DO", SK_DOi);
            put("SK_CSP", new SK_CSP[]{SK_CSP});
        }};
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

    /**
     * DataEncryption
     * @param x     明文
     * @param pp    pp
     * @param sk_do sk_do
     * @return
     */
    public static BigInteger[] DataEncryption(BigInteger x, PublicParameters pp, SK_DO sk_do) {
        BigInteger N = pp.getN(), g = pp.getG(), h = pp.getH();
        BigInteger Nsquare = N.multiply(N);
        // a random number which satisfies |r_i| < κ/2
        int bl;
        do {
            bl = new Random().nextInt(pp.getKapa() / 2 - 1) + 1;
        } while (bl == 1);
        BigInteger r = new BigInteger(bl, certainty, new Random());
        // [[x(i)]] = g^x(i) · h^ri · SK_DOi mod N^2
        BigInteger c = g.modPow(x, Nsquare).multiply(h.modPow(r, Nsquare)).multiply(sk_do.getSK_DOi()).mod(Nsquare);
        /**
         * 注：
         * 解密时 mod N mod γ，因为|γ|＜κ/2 所以最后模γ后 x 是不可能超过它的，而原始明文空间的 x 是在 Z_N 中的。
         * 如果 x 是一个介于γ和N之间的数，恢复出来的就不对
         *
         * 这里把 mod N mod γ 改为 => - γ∑ri mod N
         * 故需要传参 ri
         */
        return new BigInteger[]{c, r};
    }

    /**
     * DataAggregation
     * @param x     密文
     * @param pp    pp
     * @return
     */
    public static BigInteger DataAggregation(BigInteger x, PublicParameters pp) {
        BigInteger N = pp.getN();
        // [[x]] = [[x(i)]] mod N
        return x.mod(N.multiply(N));
    }

    /**
     * AggregatedResultDecryption
     * @param x         密文
     * @param pp        pp
     * @param sk_csp    sk_csp
     * @param ri        随机数 ri
     * @return
     */
    public static BigInteger AggregatedResultDecryption(BigInteger x, PublicParameters pp, SK_CSP sk_csp,BigInteger ri) {
        BigInteger N = pp.getN();
        BigInteger Nsquare = N.multiply(N);
        BigInteger lamda = sk_csp.getLamda(), miu = sk_csp.getMiu(), gama = sk_csp.getGama();
        // x = (L([[x]]^γ mod N^2 ·μ)mod N) mod γ = (L([[x]]^γ mod N^2 ·μ)- γ∑ri)mod N
        return (BigIntegerUtils.functionL(x.modPow(lamda, Nsquare).multiply(miu), N).subtract(ri.multiply(gama))).mod(N);
    }
}
