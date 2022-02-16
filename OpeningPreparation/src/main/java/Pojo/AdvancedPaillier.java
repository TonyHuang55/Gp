package Pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class AdvancedPaillier {
    /**
     * security parameter
     * 本案例中默认 512 即可
     */
    public int kapa;

    /**
     * the number of DOs
     */
    public int m;

    /**
     * Paillier 公钥
     */
    public BigInteger N, g;
    public BigInteger[] PK_p;
    /**
     * Paillier 私钥
     */
    public BigInteger miu, lamda;
    public BigInteger[] SK_p;

    /**
     * TA selects a large random integer γ
     * satisfying |γ| < κ/2 and gcd(k,γ) = 1
     */
    public BigInteger gama;

    /**
     * h = g^γ mod N^2
     */
    public BigInteger h;

    /**
     * public parameters
     */
    public BigInteger[] PP;

    /**
     * a random number as the task ID for every data aggregation task
     */
    public BigInteger R_t;

    /**
     * the secret key for each DO_i
     * 数据所有者密钥
     */
    public BigInteger[] SK_DO;

    /**
     * the secret key for each CSP
     * 云服务提供商密钥
     */
    public BigInteger[] SK_CSP = new BigInteger[]{lamda, miu, gama};

    /**
     * [[x^(i)]]
     */
    public BigInteger[] X;

    /**
     * [[x]]
     */
    public BigInteger XSum;

    /**
     * x
     */
    public BigInteger chi;

    public AdvancedPaillier(int kapa){
        this.kapa = kapa;
    }
}
