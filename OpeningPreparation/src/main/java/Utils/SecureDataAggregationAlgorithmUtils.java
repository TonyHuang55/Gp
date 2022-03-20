package Utils;

import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;
import Pojo.Keys.SK_DO;

import java.math.BigInteger;
import java.util.Random;

public class SecureDataAggregationAlgorithmUtils {
    /**
     * 准确度
     */
    private final static int certainty = 64;

    public static BigInteger DataEncryption(BigInteger x,PublicParameters pp, SK_DO sk_do) {
        BigInteger N = pp.getN(), g = pp.getG(), h = pp.getH();
        BigInteger r = getRandomR(pp.getKapa());
        return g.modPow(x, N.multiply(N)).multiply(h.modPow(r, N.multiply(N))).multiply(sk_do.getSK_DOi()).mod(N.multiply(N));
    }

    public static BigInteger DataAggregation(BigInteger x,PublicParameters pp) {
        BigInteger N = pp.getN();
        return x.mod(N.multiply(N));
    }

    public static BigInteger AggregatedResultDecryption(BigInteger x,PublicParameters pp, SK_CSP sk_csp) {
        BigInteger N = pp.getN();
        BigInteger lamda = sk_csp.getLamda(), miu = sk_csp.getMiu(), gama = sk_csp.getLamda();
        return BigIntegerUtils.functionL(x.modPow(lamda, N.multiply(N)).multiply(miu), N).mod(N).mod(gama);
    }

    private static BigInteger getRandomR(int kapa) {
        // a random number which satisfies |r_i| < κ/2
        return new BigInteger(kapa / 2, certainty, new Random());
    }

}
