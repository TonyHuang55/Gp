package Pojo.Keys;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class SK_CSP extends Keys{
    /**
     * Paillier 私钥
     */
    public BigInteger lamda;

    public BigInteger miu;

    /**
     * TA selects a large random integer γ
     * satisfying |γ| < κ/2 and gcd(k,γ) = 1
     */
    public BigInteger gama;
}
