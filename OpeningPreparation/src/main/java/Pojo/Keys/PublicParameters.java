package Pojo.Keys;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class PublicParameters extends Keys{
    /**
     * security parameter
     * 本例中为 2048
     */
    public int kapa;

    /**
     * Paillier 公钥
     */
    public BigInteger N;

    public BigInteger g;

    /**
     * TA selects a large random integer γ
     * satisfying |γ| < κ/2 and gcd(k,γ) = 1
     * h = g^γ mod N^2
     */
    public BigInteger h;
}
