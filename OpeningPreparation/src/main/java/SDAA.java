import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Random;

public class SDAA {

    /**
     * security parameter
     */
    private BigInteger κ;

    private BigInteger k;

    private PaillierCryptosystem paillierCryptosystem;

    private BigInteger γ;

    private BigInteger Rt;

    private BigInteger[] PP;

    private BigInteger[] SK;

    private int bitLength = 512;

    /**
     * the number of DOs in our system
     */
    private int m = 10;

    public void keyGeneration() {
        paillierCryptosystem = new PaillierCryptosystem();
        BigInteger N = paillierCryptosystem.getPK()[0];
        BigInteger g = paillierCryptosystem.getPK()[1];
        BigInteger μ = paillierCryptosystem.getSK()[0];
        BigInteger λ = paillierCryptosystem.getSK()[1];

        while (true) {
            γ = new BigInteger(bitLength, new Random());
            if (γ.compareTo(κ.divide(new BigInteger("2"))) < 0 && k.gcd(γ).intValue() == 1) {
                break;
            }
        }

        BigInteger h = g.modPow(γ, N.multiply(N));

        PP = new BigInteger[]{κ, N, g, h};

        BigInteger[] splitN = split(N, m);

        while (true) {
            Rt = new BigInteger(bitLength, new Random());
            if (Rt.compareTo(N.multiply(N)) < 0 && Rt.gcd(N.multiply(N)).intValue() == 1) {
                break;
            }
        }

        SK = new BigInteger[]{λ, μ, γ};
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

    }

}
