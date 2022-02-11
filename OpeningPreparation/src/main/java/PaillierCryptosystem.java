import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

/**
 * @author Huang
 */
public class PaillierCryptosystem {
    /**
     * 两个随机大素数 p，q
     * λ = lcm(p-1,q-1) = (p-1) * (q-1) / gcd(p-1, q-1)
     */
    private BigInteger p, q, λ;

    /**
     * N = p * q
     */
    private BigInteger N;

    /**
     * N^2
     */
    private BigInteger Nsquare;

    /**
     * 随机数 g
     * 要满足：gcd(L(g^λ mod N^2),N) = 1
     * 其中：L(x) = (x - 1)/N
     */

    private BigInteger g;

    /**
     * μ = (L(g^λ mod N^2))^-1 mod N
     */
    private BigInteger μ;

    /**
     * 模数位数
     */
    private int bitLength;

    /**
     * 公钥：pk
     * 私钥：sk
     */
    private BigInteger[] pk;
    private BigInteger[] sk;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println("第" + i + "次：");
            PaillierCryptosystem paillier = new PaillierCryptosystem();
            BigInteger msg = new BigInteger("12345");
            BigInteger c = paillier.Encryption(msg);
            System.out.println(c);
            BigInteger d = paillier.Decryption(c);
            System.out.println(d);
            System.out.println(d.equals(msg));
        }

    }

    /**
     * 有参构造
     *
     * @param bitLengthVal 位数
     * @param certainty    确定性
     */
    public PaillierCryptosystem(int bitLengthVal, int certainty) {
        keyGeneration(bitLengthVal, certainty);
    }

    /**
     * 无参构造，默认位数为：512，确定性为：64
     */
    public PaillierCryptosystem() {
        keyGeneration(512, 64);
    }

    /**
     * 秘钥生成和校验
     *
     * @param bitLengthVal
     * @param certainty
     */
    public void keyGeneration(int bitLengthVal, int certainty) {
        bitLength = bitLengthVal;
        // BigInteger(int bitLength ,int certainty ,Random rnd)
        // 生成 BigInteger 伪随机数，它可能是（概率不小于 1 - 1/2^certainty）一个具有指定 bitLength 的素数
        p = new BigInteger(bitLength / 2, certainty, new Random());
        q = new BigInteger(bitLength / 2, certainty, new Random());

        // 计算 p 和 q 的乘积 N 以及 N^2
        N = p.multiply(q);
        Nsquare = N.multiply(N);

        // 计算 λ = lcm(p-1,q-1) = (p-1) * (q-1) / gcd(p-1, q-1)
        λ = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
                .divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));

        // 取满足条件的随机数 g
        g = new BigInteger("2");
        for (; g.compareTo(Nsquare) < 0; g = g.add(BigInteger.ONE)) {
            // 计算 μ (求逆：modInverse)
            μ = (g.modPow(λ, N.multiply(N)).subtract(BigInteger.ONE).divide(N)).modInverse(N);
            if (g.modPow(λ, N.multiply(N)).subtract(BigInteger.ONE).divide(N).gcd(N).intValue() == 1) {
                break;
            }
        }

        pk = new BigInteger[]{N, g};
        sk = new BigInteger[]{μ, λ};
    }

    /**
     * 加密
     *
     * @param m 明文
     * @param r 随机数 r
     * @return 密文
     */
    public BigInteger Encryption(BigInteger m, BigInteger r) {
        return g.modPow(m, Nsquare).multiply(r.modPow(N, Nsquare)).mod(Nsquare);
    }

    /**
     * 加密 (自动生成随机数)
     *
     * @param m 明文
     * @return 密文
     */
    public BigInteger Encryption(BigInteger m) {
        BigInteger r = new BigInteger(bitLength, new Random());
        return g.modPow(m, Nsquare).multiply(r.modPow(N, Nsquare)).mod(Nsquare);
    }

    /**
     * 解密
     *
     * @param c 密文
     * @return 明文
     */
    public BigInteger Decryption(BigInteger c) {
        return c.modPow(λ, Nsquare).subtract(BigInteger.ONE).divide(N).multiply(μ).mod(N);
    }
}
