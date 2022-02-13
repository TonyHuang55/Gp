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
    private int bitLength = 512;

    /**
     * 公钥：pk
     * 私钥：sk
     */
    public BigInteger[] pk;
    public BigInteger[] sk;

    public static void main(String[] args) {
        PaillierCryptosystem paillier = new PaillierCryptosystem();
        BigInteger msg = new BigInteger("12345");
        BigInteger c = paillier.Encryption(msg);
        //System.out.println(c);
        BigInteger d = paillier.Decryption(c);
        //System.out.println(d);
        System.out.println(d.equals(msg));

    }

    /**
     * 有参构造
     *
     * @param bitLengthVal 位数
     * @param certainty    确定性
     */
    @Deprecated
    public PaillierCryptosystem(int bitLengthVal, int certainty) {
        keyGeneration(bitLengthVal, certainty);
    }

    /**
     * 无参构造，默认位数为：512，确定性为：64
     */
    public PaillierCryptosystem() {
        keyGeneration(bitLength, 64);
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
        p = new BigInteger(bitLength, certainty, new Random());
        q = new BigInteger(bitLength, certainty, new Random());

        // 计算 p 和 q 的乘积 N 以及 N^2
        N = p.multiply(q);
        Nsquare = N.multiply(N);

        // 计算 λ = lcm(p-1,q-1) = (p-1) * (q-1) / gcd(p-1, q-1)
        λ = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))
                .divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));

        // 取满足条件的随机数 g
        while (true) {
            // BigInteger(int numBits, Random rnd)
            // 此构造函数用于构造一个随机生成的 BigInteger，范围在 0 到 (2^numBits - 1), 包括边界值
            g = new BigInteger(bitLength, new Random());
            if (g.compareTo(Nsquare) < 0 && g.gcd(Nsquare).intValue() == 1) {
                μ = (g.modPow(λ, Nsquare).subtract(BigInteger.ONE).divide(N)).modInverse(N);
                if (g.modPow(λ, Nsquare).subtract(BigInteger.ONE).divide(N).gcd(N).intValue() == 1) {
                    break;
                }
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
    @Deprecated
    public BigInteger Encryption(BigInteger m, BigInteger r) {
        // c = g^m * r^N mod N^2
        return g.modPow(m, Nsquare).multiply(r.modPow(N, Nsquare)).mod(Nsquare);
    }

    /**
     * 加密 (自动生成随机数)
     *
     * @param m 明文
     * @return 密文
     */
    public BigInteger Encryption(BigInteger m) {
        BigInteger r;
        while (true) {
            r = new BigInteger(bitLength, new Random());
            if (r.compareTo(Nsquare) < 0 && r.gcd(Nsquare).intValue() == 1) {
                break;
            }
        }
        // c = g^m * r^N mod N^2
        return g.modPow(m, Nsquare).multiply(r.modPow(N, Nsquare)).mod(Nsquare);
    }

    /**
     * 解密
     *
     * @param c 密文
     * @return 明文
     */
    public BigInteger Decryption(BigInteger c) {
        // L(c^λ mod N^2) * μ mod N
        return c.modPow(λ, Nsquare).subtract(BigInteger.ONE).divide(N).multiply(μ).mod(N);
    }

    public BigInteger[] getPK() {
        return pk;
    }

    public BigInteger[] getSK() {
        return sk;
    }
}
