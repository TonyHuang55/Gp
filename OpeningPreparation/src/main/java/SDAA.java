import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class SDAA {

    /**
     * security parameter
     */
    private BigInteger κ=new BigInteger(1024, new Random());

    private BigInteger k=new BigInteger("2");

    private BigInteger N, g, h, λ, μ;

    private PaillierCryptosystem paillierCryptosystem;

    private BigInteger γ;

    private BigInteger Rt;

    private BigInteger[] PP;

    private BigInteger[] SK;

    private BigInteger[] SKDO;

    private BigInteger[] x, r;

    private BigInteger[] X;

    private BigInteger χ = BigInteger.ZERO;

    private BigInteger XSum = BigInteger.ZERO;

    private int bitLength = 512;

    /**
     * the number of DOs in our system
     */
    private int m = 10;

    public void keyGeneration() {
        paillierCryptosystem = new PaillierCryptosystem();
        N = paillierCryptosystem.getPK()[0];
        g = paillierCryptosystem.getPK()[1];
        μ = paillierCryptosystem.getSK()[0];
        λ = paillierCryptosystem.getSK()[1];

        while (true) {
            γ = new BigInteger(bitLength, new Random());
            if (γ.compareTo(κ.divide(new BigInteger("2"))) < 0 && k.gcd(γ).intValue() == 1) {
                break;
            }
        }

        h = g.modPow(γ, N.multiply(N));

        PP = new BigInteger[]{κ, N, g, h};

        System.out.println("PP:"+Arrays.toString(PP));

        BigInteger[] splitN = split(N, m);

        while (true) {
            Rt = new BigInteger(bitLength, new Random());
            if (Rt.compareTo(N.multiply(N)) < 0 && Rt.gcd(N.multiply(N)).intValue() == 1) {
                break;
            }
        }

        SKDO = new BigInteger[m];
        for (int i = 0; i < m; i++) {
            SKDO[i] = Rt.modPow(splitN[i], N.multiply(N));
        }

        SK = new BigInteger[]{λ, μ, γ};

        System.out.println("SK:"+Arrays.toString(SK));
    }

    public void getx() {
        x = new BigInteger[m];
        for (int i = 0; i < m; i++) {
            while (true) {
                x[i] = new BigInteger(bitLength, new Random());
                if (Rt.compareTo(N) < 0 && Rt.gcd(N).intValue() == 1) {
                    break;
                }
            }
        }
    }

    public void getr() {
        r = new BigInteger[m];
        BigInteger halfκ = κ.divide(new BigInteger("2"));
        for (int i = 0; i < m; i++) {
            while (true) {
                r[i] = new BigInteger(bitLength, new Random());
                if (Rt.compareTo(halfκ) < 0 && Rt.gcd(halfκ).intValue() == 1) {
                    break;
                }
            }
        }
    }

    public void DataEncryption() {
        getx();
        getr();
        X = new BigInteger[m];
        for (int i = 0; i < m; i++) {
            X[i] = g.modPow(x[i], N.multiply(N)).multiply(h.modPow(r[i], N.multiply(N))).multiply(SKDO[i]).mod(N.multiply(N));
        }
        System.out.println("[[X^(i)]]:"+Arrays.toString(X));
    }

    public void DataAggregation() {
        for (BigInteger i : X) {
            XSum = XSum.add(i.mod(N.multiply(N)));
        }
        XSum = XSum.mod(N.multiply(N));
        System.out.println("[[X]]:"+XSum);
    }

    public void AggregatedResultDecryption() {
        χ = XSum.modPow(λ, N.multiply(N)).multiply(μ).subtract(BigInteger.ONE).divide(N).mod(N).mod(γ);
        System.out.println("χ:"+χ);
    }

    /**
     * https://www.icode9.com/content-1-362158.html
     * https://stackoverflow.com/questions/2640053/getting-n-random-numbers-whose-sum-is-m
     * 生成 0 到 1 之间的 m - 1 个随机数，将数字 0 和 1 本身添加到列表中，排序，取相邻数字的差值 ---> 均匀分布。
     */
    public static BigInteger[] split(BigInteger N, int m) {
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


    public static void main(String[] args) {
        SDAA sdaa = new SDAA();
        sdaa.keyGeneration();
        sdaa.DataEncryption();
        sdaa.DataAggregation();
        sdaa.AggregatedResultDecryption();
    }

}
