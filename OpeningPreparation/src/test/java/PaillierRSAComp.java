import Utils.BigIntegerUtils;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Random;

import static java.math.BigInteger.ONE;

public class PaillierRSAComp {
    public static void main(String[] args) {
        for (int j = 1; j <= 20; j++) {
            System.out.println("第"+j+"轮：");
            long rStart = System.currentTimeMillis();
            HashMap<String, BigInteger[]> RSAKey = RSA.KeyGeneration();
            BigInteger[] rsapk = RSAKey.get("PK");
            BigInteger[] rsask = RSAKey.get("SK");
            long rEnd = System.currentTimeMillis();
            System.out.println("RSA 生成密钥用时：" + (rEnd - rStart) + "ms");

            long rStart1 = System.currentTimeMillis();
            for (int i = 1; i <= 100; i++) {
                String m = i+"";
                BigInteger rc = RSA.Encryption(new BigInteger(m), rsapk);
                BigInteger rm = RSA.Decryption(rc, rsapk, rsask);
            }
            long rEnd1 = System.currentTimeMillis();
            System.out.println("RSA 加解密100条信息用时：" + (rEnd1 - rStart1) + "ms");

            long pStart = System.currentTimeMillis();
            HashMap<String, BigInteger[]> PaillierKey = Paillier.KeyGeneration();
            BigInteger[] ppk = PaillierKey.get("PK");
            BigInteger[] psk = PaillierKey.get("SK");
            long pEnd = System.currentTimeMillis();
            System.out.println("Paillier 生成密钥用时：" + (pEnd - pStart) + "ms");

            long pStart1 = System.currentTimeMillis();
            for (int i = 1; i <= 100; i++) {
                String m = i + "";
                BigInteger pc = Paillier.Encryption(new BigInteger(m), ppk);
                BigInteger pm = Paillier.Decryption(pc, ppk, psk);
            }
            long pEnd1 = System.currentTimeMillis();
            System.out.println("Paillier 加解密100条信息用时：" + (pEnd1 - pStart1) + "ms");
        }
    }
}

class Paillier {
    private static int kapa = 1024;
    private static int certainty = 64;

    public static HashMap<String, BigInteger[]> KeyGeneration() {
        // BigInteger(int bitLength ,int certainty ,Random rnd)
        // 生成 BigInteger 伪随机数，它可能是（概率不小于 1 - 1/2^certainty）一个具有指定 bitLength 的素数
        BigInteger p = new BigInteger(kapa / 2, certainty, new Random());
        BigInteger q = new BigInteger(kapa / 2, certainty, new Random());

        // 计算 p 和 q 的乘积 N 以及 N^2
        BigInteger N = p.multiply(q);
        BigInteger Nsquare = N.multiply(N);

        // 计算 λ = lcm(p-1,q-1) = (p-1) * (q-1) / gcd(p-1, q-1)
        BigInteger lamda = p.subtract(ONE).multiply(q.subtract(ONE)).divide(p.subtract(ONE).gcd(q.subtract(ONE)));

        // 这里 g 不取随机数，直接使用 N + 1 。此时对于这个群，g^kN=(N + 1)^kN = 1，k = 1
        BigInteger g = N.add(ONE);
        BigInteger miu = BigIntegerUtils.functionL(g.modPow(lamda, Nsquare), N).modInverse(N);

        return new HashMap<String, BigInteger[]>() {{
            put("PK", new BigInteger[]{N, g});
            put("SK", new BigInteger[]{miu, lamda});
        }};
    }

    public static BigInteger Encryption(BigInteger m, BigInteger[] pk) {
        BigInteger N = pk[0], g = pk[1];
        BigInteger Nsquare = N.multiply(N);
        BigInteger r = new BigInteger(kapa, new Random());
        // [[x(i)]] = g^x(i) · h^ri · SK_DOi mod N^2
        return g.modPow(m, Nsquare).multiply(r.modPow(N, Nsquare)).mod(Nsquare);
    }

    public static BigInteger Decryption(BigInteger c, BigInteger[] pk, BigInteger[] sk) {
        BigInteger N = pk[0];
        BigInteger Nsquare = N.multiply(N);
        BigInteger miu = sk[0], lamda = sk[1];
        return (BigIntegerUtils.functionL(c.modPow(lamda, Nsquare), N).multiply(miu)).mod(N);
    }
}

class RSA {
    private static int kapa = 1024;
    private static int certainty = 64;

    public static HashMap<String, BigInteger[]> KeyGeneration() {
        // BigInteger(int bitLength ,int certainty ,Random rnd)
        // 生成 BigInteger 伪随机数，它可能是（概率不小于 1 - 1/2^certainty）一个具有指定 bitLength 的素数
        BigInteger p = new BigInteger(kapa / 2, certainty, new Random());
        BigInteger q = new BigInteger(kapa / 2, certainty, new Random());

        BigInteger N = p.multiply(q);
        // ϕ(n) = (p - 1)(q - 1)
        BigInteger fn = (p.subtract(ONE)).multiply(q.subtract(ONE));

        BigInteger e;
        do {
            int bl= new Random().nextInt(kapa - 2) + 1;
            e = new BigInteger(bl, certainty,new Random());
        } while (e.gcd(fn) == ONE && e.compareTo(ONE) > 0 && e.compareTo(fn) < 0);

        BigInteger d = e.modInverse(fn);

        BigInteger finalE = e;
        return new HashMap<String, BigInteger[]>() {{
            put("PK", new BigInteger[]{N, finalE});
            put("SK", new BigInteger[]{d});
        }};
    }

    public static BigInteger Encryption(BigInteger m, BigInteger[] pk) {
        BigInteger N = pk[0], c = pk[1];
        return m.modPow(c, N);
    }

    public static BigInteger Decryption(BigInteger c, BigInteger[] pk, BigInteger[] sk) {
        BigInteger N = pk[0], d = sk[0];
        return c.modPow(d, N);
    }
}
