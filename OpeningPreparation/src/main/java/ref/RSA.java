package ref;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.sound.sampled.SourceDataLine;

public class RSA {
    // 输入样例--------------------------------预期输出
    // 638 5 851-------------------------------7
    // 95 13 2537------------------------------1520
    // 1648 13 2537----------------------------111
    // 1410 13 2537----------------------------802
    // 1299 13 2537----------------------------1004
    // 1365 13 2537----------------------------2404
    // 24 3 55---------------------------------29
    // 19 3 55---------------------------------24
    // 99999989 100000007 100012280022971------97009676283750
    // 99991 10009 1899827651------------------576844820

    private final static BigInteger ZERO = new BigInteger("0");
    private final static BigInteger ONE = new BigInteger("1");
    private final static BigInteger TWO = new BigInteger("2");

    public static void main(final String[] args) {

        System.out.println("输入密文(c)，公钥(e)，大整数(n)：(用空格分隔)");
        final Scanner cin = new Scanner(System.in);

        BigInteger c_in, e_in, n_in;
        c_in = cin.nextBigInteger();// 密文
        e_in = cin.nextBigInteger();// 公钥
        n_in = cin.nextBigInteger();// 大数
        cin.close();

        // 记录程序开始时间并输出
        Date date1 = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1Str = simpleDateFormat.format(date1);
        System.out.println("开始时间 = " + date1Str);

        // 计算私钥d
        // d * e ≡ 1 ( mod ϕ(n) )
        BigInteger d = Private_key(c_in, e_in, n_in);

        // 解秘操作
        // m = D(c) =c ^ d (mod n)
        BigInteger m = decrypt(c_in, d, n_in);

        // 输出明文
        System.out.println("对应的明文为:" + m);

        // 记录程序结束时间并输出
        Date date2 = new Date();
        String date2Str = simpleDateFormat.format(date2);
        System.out.println("结束时间 = " + date2Str);

    }

    private static BigInteger fn(final BigInteger p, final BigInteger q) {// 欧拉函数
        final BigInteger p1 = p.subtract(ONE);
        final BigInteger q1 = q.subtract(ONE);
        // ϕ(n) = (p - 1)(q - 1)
        return p1.multiply(q1);
    }

    private static BigInteger Private_key(final BigInteger c, final BigInteger e, final BigInteger n) {// 秘钥
        final BigInteger[] pq = Pollards_Rho.decompose(n);// 获取大数N分解后的因数p和q
        final BigInteger p = pq[0];
        final BigInteger q = pq[1];

        final BigInteger fn = fn(p, q);// 计算欧拉函数:ϕ(n) = (p - 1)(q - 1)

        final BigInteger[] d = ex_gcd(e, fn);// 调用扩展欧几里得求逆元
        final BigInteger d_fin = d[1].compareTo(ZERO) == 1 ? d[1] : d[1].add(fn);// 调整结果，处理得到结果可能是负数的情况，返回一个正数
        return d_fin;
    }

    private static BigInteger decrypt(final BigInteger c, final BigInteger d, final BigInteger n) {// 解秘操作
        // m = D(c) =c ^ d (mod n)
        return qpow(c, d, n);// 快速幂
    }

    private static BigInteger qpow(BigInteger a, BigInteger b, final BigInteger p) {// 大数快速幂 求a ^ b % p
        BigInteger s = ONE;
        while (!b.equals(ZERO)) {
            if ((b.mod(TWO)).equals(ONE))// 如果幂是奇数，多一个数出来，要保存
                s = s.multiply(a).mod(p);
            a = a.multiply(a).mod(p);// 底数平方
            b = b.divide(TWO);// 指数减半
        }
        return s;
    }

    private static BigInteger[] ex_gcd(final BigInteger a, final BigInteger b) {// 扩展欧几里得求逆元
        BigInteger ans;
        final BigInteger[] res = new BigInteger[3];// 存储结果
        // b=0时，gcd（a，b）=a，x=1，y=0
        if (b.compareTo(ZERO) == 0) {
            res[0] = a;// 最大公约数
            res[1] = ONE;// x
            res[2] = ZERO;// y
            return res;
        }
        // ax1 + by1 = gcd(a,b);
        // bx2 + (a mod b)y2 = gcd(b,a mod b);
        // 欧几里德原理：gcd(a,b) = gcd(b,a mod b);
        // 则:ax1+ by1 = bx2+ (a mod b)y2;
        // 即:ax1+ by1 = bx2+ (a - [a / b] * b)y2=ay2+ bx2- [a / b] * by2;
        // 即:ax1+ by1 = ay2+ b(x2- [a / b] *y2);
        // 根据恒等定理：x1=y2; y1=x2- [a / b] *y2;
        final BigInteger[] temp = ex_gcd(b, a.mod(b));
        ans = temp[0];// 最大公约数
        res[0] = ans;
        res[1] = temp[2];// 逆元
        res[2] = temp[1].subtract((a.divide(b)).multiply(temp[2]));
        return res;
    }
}

class Pollards_Rho {// Pollard's Rho 算法
    private final static BigInteger ZERO = new BigInteger("0");
    private final static BigInteger ONE = new BigInteger("1");
    private final static BigInteger TWO = new BigInteger("2");
    private final static SecureRandom random = new SecureRandom();

    private static BigInteger gcd(BigInteger a, BigInteger b) {// 最大公约数，辗转相除
        BigInteger temp;// 定义中间变量
        while (b.compareTo(ZERO) != 0) {
            // 欧几里得原理:gcd(a,b) = gcd(b,a mod b);
            temp = a;// 保存a的值
            a = b;// a -> b
            b = temp.mod(b);// b -> a mod b
        }
        return a;
    }

    private static BigInteger pollard_rho(final BigInteger N) {
        BigInteger divisor;// 用于保存因子结果
        final BigInteger a = new BigInteger(N.bitLength(), random);// 生成随机数a
        BigInteger x1 = new BigInteger(N.bitLength(), random);// 随机生成一个开始数:x1
        BigInteger x2 = x1;// x2 = x1，但是x2的速度是x1的两倍，用于检测算法中可能出现的环

        if (N.mod(TWO).compareTo(ZERO) == 0)
            return TWO;// 当N是偶数时候，易得因数就是2

        if (N.isProbablePrime(20))
            return N;// 当N本身是素数时，返回自身

        while (true) {// Floyd判环算法
            // 使用一个函数生产伪随机数
            // f(x) = (x^2 + a) mod N
            x1 = ((x1.multiply(x1)).add(a)).mod(N);// 前进一步
            x2 = ((x2.multiply(x2)).add(a)).mod(N);
            x2 = ((x2.multiply(x2)).add(a)).mod(N);// 前进两步
            while (x1.compareTo(x2) != 0) {
                divisor = gcd(x1.subtract(x2), N);// 计算gcd(x1 - x2 ,N)
                if (divisor.compareTo(ONE) > 0) {// 判断是否存在 gcd(x1 - x2 ,N) > 1，若存在则他就是N的一个因子
                    return divisor;
                }
                x1 = ((x1.multiply(x1)).add(a)).mod(N);// 前进一步
                x2 = ((x2.multiply(x2)).add(a)).mod(N);
                x2 = ((x2.multiply(x2)).add(a)).mod(N);// 前进两步
            }
        }
    }

    public static BigInteger[] decompose(final BigInteger N) {// 大数分解
        final BigInteger[] ans = new BigInteger[2];// 定义存储结果数组
        final BigInteger divisor = pollard_rho(N);// 调用刚刚得到的一个因子
        ans[0] = divisor;
        ans[1] = N.divide(ans[0]);// 通过简单除法得到另一个
        return ans;
    }
}