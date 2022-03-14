package Pojo;

import Utils.BigIntegerUtils;

import java.math.BigInteger;

public class CloudServiceProvider {
    protected static AdvancedPaillier advancedPaillier;

    public CloudServiceProvider(AdvancedPaillier advancedPaillier) {
        this.advancedPaillier = advancedPaillier;
    }

    public BigInteger DataAggregation(BigInteger[] X) {
        BigInteger N = advancedPaillier.getN();
        BigInteger XSum = BigInteger.ZERO;
        for (BigInteger xi : X) {
            XSum = XSum.add(xi.mod(N.multiply(N)));
        }
        advancedPaillier.setXSum(XSum);
        return XSum.mod(N.multiply(N));
    }

    public BigInteger AggregatedResultDecryption(BigInteger XSum) {
        BigInteger N = advancedPaillier.getN();
        BigInteger[] sk_csp = advancedPaillier.getSK_CSP();
        BigInteger lamda = sk_csp[0], miu = sk_csp[1], gama = sk_csp[2];
        BigInteger res = BigIntegerUtils.functionL(XSum.modPow(lamda, N.multiply(N)).multiply(miu), N).mod(N).mod(gama);
        advancedPaillier.setChi(res);
        return res;
    }
}
