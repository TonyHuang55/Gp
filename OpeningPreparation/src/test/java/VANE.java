import Pojo.CloudServiceProvider;
import Pojo.DataOwner;
import Pojo.Keys.Keys;
import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;
import Pojo.Keys.SK_DO;
import Pojo.TrustAuthority;
import Utils.LinearRegressionUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

public class VANE {
    @Test
    public void wine() throws Exception {
        List<String> urls = Arrays.asList("src/main/resources/database/winequality-red.csv",
                "src/main/resources/database/winequality-red.csv");
        BigDecimal[] a = VANETest(urls);
        List<String> newUrls = new ArrayList<>(urls);
        newUrls.add("src/main/resources/database/winequality-red.csv");
        updateByAddDOs(a, newUrls);
    }

    @Test
    public void house() throws Exception {
        List<String> urls = Arrays.asList("src/main/resources/database/house_data.csv",
                "src/main/resources/database/house_data.csv");
        BigDecimal[] a = VANETest(urls);
        List<String> newUrls = new ArrayList<>(urls);
        newUrls.add("src/main/resources/database/house_data.csv");
        updateByAddDOs(a, newUrls);
    }

    private static BigDecimal[] VANETest(List<String> urls) throws Exception {
        System.out.println("本次共有" + urls.size() + "个 DataOwner");
        System.out.println("系统初始化:");
        TrustAuthority ta = new TrustAuthority();
        // keyGenerate 后得到 PP、SK_DO 和 SK_CSP
        HashMap<String, Keys[]> keyGenerate = ta.keyGenerate(urls.size());

        System.out.println("public parameters and secret keys for DOs and CSP");
        for (String s : keyGenerate.keySet()) {
            System.out.println(s + ":" + Arrays.toString(keyGenerate.get(s)));
        }
        Keys[] pp = keyGenerate.get("PP");
        Keys[] sk_dos = keyGenerate.get("SK_DO");
        Keys[] sk_csp = keyGenerate.get("SK_CSP");
        System.out.println("=============================================");

        DataOwner[] dataOwners = new DataOwner[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            DataOwner doi = new DataOwner();
            dataOwners[i] = doi;
        }

        List<List[]> DOs = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            List[] list = dataOwners[i].dataNormalization(urls.get(i));
            DOs.add(list);
        }

        List[] globalMaxMin = ta.globalDataNormalization(DOs);
        System.out.println("各个维度最大值（扰乱后）：" + globalMaxMin[0]);
        System.out.println("各个维度最小值（扰乱后）：" + globalMaxMin[1]);

        for (int i = 0; i < urls.size(); i++) {
            dataOwners[i].localFeatureVectorNormalize(globalMaxMin[0], globalMaxMin[1]);
            dataOwners[i].localTargetNormalize(globalMaxMin[3]);
            dataOwners[i].dataPreprocessing();
        }

        System.out.println("=============================================");
        System.out.println("聚合明文结果：");

        List<Double[][]> mSum = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            Double[][] mi = dataOwners[i].getM();
            mSum.add(mi);
        }

        int[][] m = new int[mSum.get(0).length][mSum.get(0).length];
        for (int k = 0; k < urls.size(); k++) {
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[0].length; j++) {
                    m[i][j] += (int) Math.floor(mSum.get(k)[i][j] * 1000);
                }
            }
        }

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("=============================================");
        long eStart = System.currentTimeMillis();
        List<List<BigInteger[][]>> resSum = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            List<BigInteger[][]> res = dataOwners[i].localTrainingDataEncryption((PublicParameters) pp[0], (SK_DO) sk_dos[i]);
            resSum.add(res);
        }
        long eEnd = System.currentTimeMillis();
        System.out.println("DOs 加密用时：" + (eEnd - eStart) + "ms");
        List M = new ArrayList<BigInteger[][]>() {
            {
                for (int i = 0; i < urls.size(); i++) {
                    add(resSum.get(i).get(0));
                }
            }
        };

        List R = new ArrayList<BigInteger[][]>() {
            {
                for (int i = 0; i < urls.size(); i++) {
                    add(resSum.get(i).get(1));
                }
            }
        };

        CloudServiceProvider csp = new CloudServiceProvider();
        long dStart = System.currentTimeMillis();
        BigInteger[][] res = csp.localTrainingDataAggregation(M, (PublicParameters) pp[0], (SK_CSP) sk_csp[0], R);
        long dEnd = System.currentTimeMillis();
        System.out.println("CSP 聚合 + 解密用时：" + (dEnd - dStart) + "ms");
        System.out.println("聚合解密结果矩阵为：");
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                System.out.print(res[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("=============================================");
        int n = (int) globalMaxMin[2].get(0);

        BigDecimal[] org = new BigDecimal[res.length];
        Arrays.fill(org, BigDecimal.ONE);
        return LinearRegressionUtils.LR(res, n, org);
    }

    public static void updateByAddDOs(BigDecimal[] a, List<String> urls) throws Exception {
        BigDecimal[] copyA = Arrays.copyOf(a, a.length);
        System.out.println("添加至" + urls.size() + "个 DataOwner");
        System.out.println("更新秘钥:");
        TrustAuthority ta = new TrustAuthority();
        // keyGenerate 后得到 PP、SK_DO 和 SK_CSP
        HashMap<String, Keys[]> keyGenerate = ta.keyGenerate(urls.size());

        System.out.println("public parameters and secret keys for DOs and CSP");
        for (String s : keyGenerate.keySet()) {
            System.out.println(s + ":" + Arrays.toString(keyGenerate.get(s)));
        }
        Keys[] pp = keyGenerate.get("PP");
        Keys[] sk_dos = keyGenerate.get("SK_DO");
        Keys[] sk_csp = keyGenerate.get("SK_CSP");
        System.out.println("=============================================");

        DataOwner[] dataOwners = new DataOwner[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            DataOwner doi = new DataOwner();
            dataOwners[i] = doi;
        }

        List<List[]> DOs = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            List[] list = dataOwners[i].dataNormalization(urls.get(i));
            DOs.add(list);
        }

        List[] globalMaxMin = ta.globalDataNormalization(DOs);
        System.out.println("更新后各个维度最大值（扰乱后）：" + globalMaxMin[0]);
        System.out.println("更新后各个维度最小值（扰乱后）：" + globalMaxMin[1]);

        for (int i = 0; i < urls.size(); i++) {
            dataOwners[i].localFeatureVectorNormalize(globalMaxMin[0], globalMaxMin[1]);
            dataOwners[i].localTargetNormalize(globalMaxMin[3]);
            dataOwners[i].dataPreprocessing();
        }

        System.out.println("=============================================");
        System.out.println("聚合明文结果：");

        List<Double[][]> mSum = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            Double[][] mi = dataOwners[i].getM();
            mSum.add(mi);
        }

        int[][] m = new int[mSum.get(0).length][mSum.get(0).length];
        for (int k = 0; k < urls.size(); k++) {
            for (int i = 0; i < m.length; i++) {
                for (int j = 0; j < m[0].length; j++) {
                    m[i][j] += (int) Math.floor(mSum.get(k)[i][j] * 1000);
                }
            }
        }

        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("=============================================");
        long eStart = System.currentTimeMillis();
        List<List<BigInteger[][]>> resSum = new ArrayList<>();
        for (int i = 0; i < urls.size(); i++) {
            List<BigInteger[][]> res = dataOwners[i].localTrainingDataEncryption((PublicParameters) pp[0], (SK_DO) sk_dos[i]);
            resSum.add(res);
        }
        long eEnd = System.currentTimeMillis();
        System.out.println("DOs 加密用时：" + (eEnd - eStart) + "ms");
        List M = new ArrayList<BigInteger[][]>() {
            {
                for (int i = 0; i < urls.size(); i++) {
                    add(resSum.get(i).get(0));
                }
            }
        };

        List R = new ArrayList<BigInteger[][]>() {
            {
                for (int i = 0; i < urls.size(); i++) {
                    add(resSum.get(i).get(1));
                }
            }
        };

        CloudServiceProvider csp = new CloudServiceProvider();
        long dStart = System.currentTimeMillis();
        BigInteger[][] res = csp.localTrainingDataAggregation(M, (PublicParameters) pp[0], (SK_CSP) sk_csp[0], R);
        long dEnd = System.currentTimeMillis();
        System.out.println("CSP 聚合 + 解密用时：" + (dEnd - dStart) + "ms");
        System.out.println("聚合解密结果矩阵为：");
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                System.out.print(res[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("=============================================");
        int n = (int) globalMaxMin[2].get(0);

        BigDecimal[] updateA = LinearRegressionUtils.LR(res, n, a);

        BigDecimal RSS = CloudServiceProvider.ModelEstimation(urls.get(0), updateA);
        BigDecimal OLDRSS = CloudServiceProvider.ModelEstimation(urls.get(0), copyA);
        BigDecimal c = RSS.divide(OLDRSS, 4);
        System.out.println("更新模型后 C = " + c + "\n" + (c.compareTo(BigDecimal.ONE) >= 0 ? " C >= 1，不采纳更新" : " C < 1，采纳更新"));
    }
}
