package Pojo;

import Utils.BigIntegerUtils;

import java.math.BigInteger;
import java.util.Random;

public class DataOwner {
    /**
     * 准确度
     */
    private final static int certainty = 64;

    protected static AdvancedPaillier advancedPaillier;

    public DataOwner(AdvancedPaillier advancedPaillier) {
        this.advancedPaillier = advancedPaillier;
    }

    public BigInteger DataEncryption(BigInteger SK_DO) {
        BigInteger N = advancedPaillier.getN(), g = advancedPaillier.getG(), h = advancedPaillier.getH();
        BigInteger x = getRandomX(N);
        BigInteger r = getRandomR(advancedPaillier.getKapa());
        return g.modPow(x, N.multiply(N)).multiply(h.modPow(r, N.multiply(N))).multiply(SK_DO).mod(N.multiply(N));
    }

    private static BigInteger getRandomX(BigInteger N) {
        // a private message of DO_i
        return BigIntegerUtils.validRandomInResidueSystem(N);
    }

    private static BigInteger getRandomR(int kapa) {
        // a random number which satisfies |r_i| < κ/2
        return new BigInteger(kapa / 2, certainty, new Random());
    }
}
