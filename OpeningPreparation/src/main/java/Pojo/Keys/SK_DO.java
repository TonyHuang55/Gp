package Pojo.Keys;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigInteger;

@Data
@AllArgsConstructor
public class SK_DO extends Keys {
    /**
     * R_t —— a random number as the task ID for every data aggregation task
     * n_i —— splits N into m random numbers{n_1,n_2...n_m}
     * SK_DOi = R_t^n_i mod N^2
     */
    public BigInteger SK_DOi;
}
