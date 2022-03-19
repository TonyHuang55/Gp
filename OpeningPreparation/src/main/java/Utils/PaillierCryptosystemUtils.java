package Utils;

import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;

import java.math.BigInteger;

public class PaillierCryptosystemUtils {
    /**
     * 加密 (自动生成随机数)
     *
     * @param m 明文
     * @return 密文
     */
    public static BigInteger Encryption(BigInteger m, PublicParameters pp) {
        BigInteger N = pp.getN();
        BigInteger g = pp.getG();
        BigInteger Nsquare = N.multiply(N);
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
    public static BigInteger Decryption(BigInteger c, PublicParameters pp, SK_CSP sk) {
        BigInteger N = pp.getN();
        BigInteger Nsquare = N.multiply(N);
        BigInteger lamda = sk.getLamda();
        BigInteger miu = sk.getMiu();
        // L(c^λ mod N^2) * μ mod N
        return BigIntegerUtils.functionL(c.modPow(lamda, Nsquare), N).multiply(miu).mod(N);
    }
}
