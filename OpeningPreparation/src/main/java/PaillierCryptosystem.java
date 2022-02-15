import Utils.BigIntegerUtils;

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
     * 准确度
     */
    private int certainty = 64;

    /**
     * 公钥：pk
     * 私钥：sk
     */
    private BigInteger[] pk;
    private BigInteger[] sk;

    public static void main(String[] args) {
        PaillierCryptosystem paillier = new PaillierCryptosystem(512);
        BigInteger msg = new BigInteger("12345");
        BigInteger c = paillier.Encryption(msg);
        BigInteger d = paillier.Decryption(c);
//        System.out.println(c);
//        System.out.println(d);
        System.out.println(d.equals(msg));

    }

    public PaillierCryptosystem(int λ) {
        keyGeneration(λ, certainty);
    }

    /**
     * 秘钥生成和校验
     *
     * @param bitLengthVal
     * @param certainty
     */
    private void keyGeneration(int bitLengthVal, int certainty) {
        // BigInteger(int bitLength ,int certainty ,Random rnd)
        // 生成 BigInteger 伪随机数，它可能是（概率不小于 1 - 1/2^certainty）一个具有指定 bitLength 的素数
        p = new BigInteger(bitLengthVal, certainty, new Random());
        q = new BigInteger(bitLengthVal, certainty, new Random());

        // 计算 p 和 q 的乘积 N 以及 N^2
        N = p.multiply(q);
        Nsquare = N.multiply(N);

        // 计算 λ = lcm(p-1,q-1) = (p-1) * (q-1) / gcd(p-1, q-1)
        λ = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE)).divide(p.subtract(BigInteger.ONE).gcd(q.subtract(BigInteger.ONE)));

        // 这里 g 不取随机数，直接使用 N + 1 。此时对于这个群，g^kN=(N + 1)^kN = 1，k = 1
        g = N.add(BigInteger.ONE);
        μ = BigIntegerUtils.functionL(g.modPow(λ, Nsquare), N).modInverse(N);

        pk = new BigInteger[]{N, g};
        sk = new BigInteger[]{μ, λ};
//        System.out.println("pk:" + Arrays.toString(pk));
//        System.out.println("sk:" + Arrays.toString(sk));
    }

    /**
     * 加密 (自动生成随机数)
     *
     * @param m 明文
     * @return 密文
     */
    public BigInteger Encryption(BigInteger m) {
        BigInteger r = BigIntegerUtils.validRandomInResidueSystem(N);
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
        return BigIntegerUtils.functionL(c.modPow(λ, Nsquare), N).multiply(μ).mod(N);
    }

    public BigInteger[] getPK() {
        return pk;
    }

    public BigInteger[] getSK() {
        return sk;
    }
}
