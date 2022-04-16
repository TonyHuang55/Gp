import Pojo.CloudServiceProvider;
import Pojo.DataOwner;
import Pojo.Keys.Keys;
import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;
import Pojo.Keys.SK_DO;
import Pojo.TrustAuthority;
import Utils.LinearRegressionUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class VANE {
    @Test
    public void wine() throws IOException {
        System.out.println("系统初始化:");
        TrustAuthority ta = new TrustAuthority();
        // keyGenerate 后得到 PP、SK_DO 和 SK_CSP
        HashMap<String, Keys[]> keyGenerate = ta.keyGenerate(2);

        System.out.println("public parameters and secret keys for DOs and CSP");
        for (String s : keyGenerate.keySet()) {
            System.out.println(s + ":" + Arrays.toString(keyGenerate.get(s)));
        }
        Keys[] pp = keyGenerate.get("PP");
        Keys[] sk_dos = keyGenerate.get("SK_DO");
        Keys[] sk_csp = keyGenerate.get("SK_CSP");
        System.out.println("=============================================");

        DataOwner do1 = new DataOwner();
        DataOwner do2 = new DataOwner();
        DataOwner[] dataOwners = {do1, do2};

        List<List[]> DOs = new ArrayList<>();
        List[] list1 = dataOwners[0].dataNormalization("src/main/resources/database/winequality-red1.csv");
        List[] list2 = dataOwners[1].dataNormalization("src/main/resources/database/winequality-red2.csv");
        DOs.add(list1);
        DOs.add(list2);

        List[] globalMaxMin = ta.globalDataNormalization(DOs);
        System.out.println("各个维度最大值（扰乱后）：" + globalMaxMin[0]);
        System.out.println("各个维度最小值（扰乱后）：" + globalMaxMin[1]);

        dataOwners[0].localFeatureVectorNormalize(globalMaxMin[0], globalMaxMin[1]);
        dataOwners[1].localFeatureVectorNormalize(globalMaxMin[0], globalMaxMin[1]);
        dataOwners[0].dataPreprocessing();
        dataOwners[1].dataPreprocessing();

        System.out.println("=============================================");
        System.out.println("聚合明文结果：");
        Double[][] m1 = dataOwners[0].getM();
        Double[][] m2 = dataOwners[1].getM();
        int[][] m = new int[m1.length][m1.length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                m[i][j] = (int) Math.floor(m1[i][j] * 1000) + (int) Math.floor(m2[i][j] * 1000);
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
        List<BigInteger[][]> res1 = dataOwners[0].localTrainingDataEncryption((PublicParameters) pp[0], (SK_DO) sk_dos[0]);
        List<BigInteger[][]> res2 = dataOwners[1].localTrainingDataEncryption((PublicParameters) pp[0], (SK_DO) sk_dos[1]);
        long eEnd = System.currentTimeMillis();
        System.out.println("DOs 加密用时：" + (eEnd - eStart) + "ms");
        List M = new ArrayList<BigInteger[][]>() {
            {
                add(res1.get(0));
                add(res2.get(0));
            }
        };

        List R = new ArrayList<BigInteger[][]>() {
            {
                add(res1.get(1));
                add(res2.get(1));
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
        LinearRegressionUtils.LR(res, n);
    }

    @Test
    public void wine1() throws IOException {
        String[] urls = {
                "src/main/resources/database/winequality-red.csv",
                "src/main/resources/database/winequality-red.csv",
                "src/main/resources/database/winequality-red.csv"
        };
        VANETest(urls);
    }

    @Test
    public void house() throws IOException {
        System.out.println("系统初始化:");
        TrustAuthority ta = new TrustAuthority();
        // keyGenerate 后得到 PP、SK_DO 和 SK_CSP
        HashMap<String, Keys[]> keyGenerate = ta.keyGenerate(2);

        System.out.println("public parameters and secret keys for DOs and CSP");
        for (String s : keyGenerate.keySet()) {
            System.out.println(s + ":" + Arrays.toString(keyGenerate.get(s)));
        }
        Keys[] pp = keyGenerate.get("PP");
        Keys[] sk_dos = keyGenerate.get("SK_DO");
        Keys[] sk_csp = keyGenerate.get("SK_CSP");
        System.out.println("=============================================");

        DataOwner do1 = new DataOwner();
        DataOwner do2 = new DataOwner();
        DataOwner[] dataOwners = {do1, do2};

        List<List[]> DOs = new ArrayList<>();
        List[] list1 = dataOwners[0].dataNormalization("src/main/resources/database/house_data1.csv");
        List[] list2 = dataOwners[1].dataNormalization("src/main/resources/database/house_data2.csv");
        DOs.add(list1);
        DOs.add(list2);

        List[] globalMaxMin = ta.globalDataNormalization(DOs);
        System.out.println("各个维度最大值（扰乱后）：" + globalMaxMin[0]);
        System.out.println("各个维度最小值（扰乱后）：" + globalMaxMin[1]);

        dataOwners[0].localFeatureVectorNormalize(globalMaxMin[0], globalMaxMin[1]);
        dataOwners[1].localFeatureVectorNormalize(globalMaxMin[0], globalMaxMin[1]);
        dataOwners[0].dataPreprocessing();
        dataOwners[1].dataPreprocessing();

        System.out.println("=============================================");
        System.out.println("聚合明文结果：");
        Double[][] m1 = dataOwners[0].getM();
        Double[][] m2 = dataOwners[1].getM();
        int[][] m = new int[m1.length][m1.length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                m[i][j] = (int) Math.floor(m1[i][j] * 1000) + (int) Math.floor(m2[i][j] * 1000);
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
        List<BigInteger[][]> res1 = dataOwners[0].localTrainingDataEncryption((PublicParameters) pp[0], (SK_DO) sk_dos[0]);
        List<BigInteger[][]> res2 = dataOwners[1].localTrainingDataEncryption((PublicParameters) pp[0], (SK_DO) sk_dos[1]);
        long eEnd = System.currentTimeMillis();
        System.out.println("DOs 加密用时：" + (eEnd - eStart) + "ms");
        List M = new ArrayList<BigInteger[][]>() {
            {
                add(res1.get(0));
                add(res2.get(0));
            }
        };

        List R = new ArrayList<BigInteger[][]>() {
            {
                add(res1.get(1));
                add(res2.get(1));
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
        LinearRegressionUtils.LR(res, n);
    }

    @Test
    public void house1() throws IOException {
        String[] urls = {
                "src/main/resources/database/house_data.csv",
                "src/main/resources/database/house_data.csv",
                "src/main/resources/database/house_data.csv"
        };
        VANETest(urls);
    }

    private static void VANETest(String[] urls) throws IOException {
        System.out.println("系统初始化:");
        TrustAuthority ta = new TrustAuthority();
        // keyGenerate 后得到 PP、SK_DO 和 SK_CSP
        HashMap<String, Keys[]> keyGenerate = ta.keyGenerate(urls.length);

        System.out.println("public parameters and secret keys for DOs and CSP");
        for (String s : keyGenerate.keySet()) {
            System.out.println(s + ":" + Arrays.toString(keyGenerate.get(s)));
        }
        Keys[] pp = keyGenerate.get("PP");
        Keys[] sk_dos = keyGenerate.get("SK_DO");
        Keys[] sk_csp = keyGenerate.get("SK_CSP");
        System.out.println("=============================================");

        DataOwner[] dataOwners = new DataOwner[urls.length];
        for (int i = 0; i < urls.length; i++) {
            DataOwner doi = new DataOwner();
            dataOwners[i] = doi;
        }

        List<List[]> DOs = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            List[] list = dataOwners[i].dataNormalization(urls[i]);
            DOs.add(list);
        }

        List[] globalMaxMin = ta.globalDataNormalization(DOs);
        System.out.println("各个维度最大值（扰乱后）：" + globalMaxMin[0]);
        System.out.println("各个维度最小值（扰乱后）：" + globalMaxMin[1]);

        for (int i = 0; i < urls.length; i++) {
            dataOwners[i].localFeatureVectorNormalize(globalMaxMin[0], globalMaxMin[1]);
            dataOwners[i].localTargetNormalize(globalMaxMin[3]);
            dataOwners[i].dataPreprocessing();
        }

        System.out.println("=============================================");
        System.out.println("聚合明文结果：");

        List<Double[][]> mSum = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            Double[][] mi = dataOwners[i].getM();
            mSum.add(mi);
        }

        int[][] m = new int[mSum.get(0).length][mSum.get(0).length];
        for (int k = 0; k < urls.length; k++) {
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
        for (int i = 0; i < urls.length; i++) {
            List<BigInteger[][]> res = dataOwners[i].localTrainingDataEncryption((PublicParameters) pp[0], (SK_DO) sk_dos[i]);
            resSum.add(res);
        }
        long eEnd = System.currentTimeMillis();
        System.out.println("DOs 加密用时：" + (eEnd - eStart) + "ms");
        List M = new ArrayList<BigInteger[][]>() {
            {
                for (int i = 0; i < urls.length; i++) {
                    add(resSum.get(i).get(0));
                }
            }
        };

        List R = new ArrayList<BigInteger[][]>() {
            {
                for (int i = 0; i < urls.length; i++) {
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
        LinearRegressionUtils.LR(res, n);
    }

}
