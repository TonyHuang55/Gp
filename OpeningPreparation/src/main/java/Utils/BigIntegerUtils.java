package Utils;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Huang
 */
public class BigIntegerUtils {

    /**
     * L(x) = (x - 1)/N
     */
    public static BigInteger functionL(BigInteger x, BigInteger N) {
        return x.subtract(BigInteger.ONE).divide(N);
    }

    /**
     * 在剩余系中取随机数
     */
    public static BigInteger validRandomInResidueSystem(BigInteger n){
        BigInteger res;
        do {
            res = new BigInteger(n.bitLength(), new Random());
        } while (res.compareTo(n) >= 0 || res.gcd(n).intValue() != 1);
        return res;
    }
}
