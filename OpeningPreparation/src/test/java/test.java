import Pojo.CloudServiceProvider;
import Pojo.DataOwner;
import Pojo.Keys.Keys;
import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;
import Pojo.Keys.SK_DO;
import Pojo.TrustAuthority;
import Utils.LinearRegressionUtils;
import Utils.PaillierCryptosystemUtils;
import Utils.SecureDataAggregationAlgorithmUtils;
import org.apache.commons.csv.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class test {
    @Test
    public void test1() throws IOException {
        System.out.println("4.3.1 System Initialization:");
        System.out.println("Step1:System Parameters and Keys Distribution:");
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

        System.out.println("Step2:Data Normalization:");
        DataOwner do1 = new DataOwner();
        DataOwner do2 = new DataOwner();
        DataOwner[] dataOwners = {do1, do2};

        List<List[]> DOs = new ArrayList<>();
//        for (int i = 0; i < dataOwners.length; i++) {
//            List[] lists = dataOwners[i].dataNormalization("src/main/resources/database/winequality-red.csv");
//            DOs.add(lists);
//            System.out.println("DO" + i + ":" + Arrays.toString(lists));
//        }
        List[] list1 = dataOwners[0].dataNormalization("src/main/resources/database/winequality-red1.csv");
        List[] list2 = dataOwners[1].dataNormalization("src/main/resources/database/winequality-red2.csv");
        DOs.add(list1);
        DOs.add(list2);

        List[] globalMaxMin = ta.globalDataNormalization(DOs);
        System.out.println(Arrays.toString(globalMaxMin));

        dataOwners[0].localFeatureVectorNormalize(globalMaxMin[0], globalMaxMin[1]);
        dataOwners[1].localFeatureVectorNormalize(globalMaxMin[0], globalMaxMin[1]);

        dataOwners[0].dataPreprocessing();
        dataOwners[1].dataPreprocessing();
        Double[][] m1 = dataOwners[0].getM();
        Double[][] m2 = dataOwners[1].getM();
        Double[][] m = new Double[m1.length][m1.length];
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m1[0].length; j++) {
                m[i][j] = Math.floor((m1[i][j] + m2[i][j]) * 1000);
            }
        }
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m.length; j++) {
                System.out.print(m[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("=============================================");

        BigInteger[][] res1 = dataOwners[0].localTrainingDataEncryption((PublicParameters) pp[0], (SK_DO) sk_dos[0]);
        BigInteger[][] res2 = dataOwners[1].localTrainingDataEncryption((PublicParameters) pp[0], (SK_DO) sk_dos[1]);

        List list = new ArrayList<BigInteger[][]>() {
            {
                add(res1);
                add(res2);
            }
        };

        CloudServiceProvider csp = new CloudServiceProvider();
        BigInteger[][] res = csp.localTrainingDataAggregation(list, (PublicParameters) pp[0], (SK_CSP) sk_csp[0]);
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length; j++) {
                System.out.print(res[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void csvTest() throws Exception {
//        String[] headers = new String[]{
//                "fixed acidity",
//                "volatile acidity",
//                "citric acid",
//                "residual sugar",
//                "chlorides",
//                "free sulfur dioxide",
//                "total sulfur dioxide",
//                "density",
//                "pH",
//                "sulphates",
//                "alcohol",
//                "quality"};
//        CSVFormat format = CSVFormat.EXCEL.withHeader(headers).withDelimiter(';');
//
        // 1.配置数据路径，读取数据文件
        String path = "src/main/resources/database/winequality-red.csv";
        FileReader fileReader = new FileReader(path);
        // 2. 读取 csv 文件第一行标题
        String combineTitle = new BufferedReader(new FileReader(path)).readLine();
        String[] headers = combineTitle.split(";");
        for (int i = 0; i < headers.length; i++) {
            String tmp = headers[i];
            headers[i] = tmp.substring(1, tmp.length() - 1);
        }
        System.out.println(Arrays.toString(headers));

        CSVFormat format = CSVFormat.EXCEL.withHeader(headers).withDelimiter(';');
        // 3. 读取数据
        CSVParser parser = new CSVParser(fileReader, format);
        List<CSVRecord> records = parser.getRecords();

        parser.close();
        fileReader.close();

        int size = records.size();
        for (int i = 1; i < size; i++) {
            System.out.println(records.get(i));
        }
    }

    @Test
    public void dataPreprocessingTest() {
        List<List<Double>> featureVector = new ArrayList<List<Double>>() {{
            add(Arrays.asList(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0}));
            add(Arrays.asList(new Double[]{6.0, 7.0, 8.0, 9.0, 10.0}));
            add(Arrays.asList(new Double[]{11.0, 12.0, 13.0, 14.0, 15.0}));
            add(Arrays.asList(new Double[]{16.0, 17.0, 18.0, 19.0, 20.0}));
            add(Arrays.asList(new Double[]{21.0, 22.0, 23.0, 24.0, 25.0}));
        }};
        List<Double> targetVariable = Arrays.asList(new Double[]{1.0, 2.0, 3.0, 4.0, 5.0});

        int d = featureVector.get(0).size();
        // 初始化
        Double[][] M = new Double[d + 1][d + 1];
        for (int i = 0; i <= d; i++) {
            for (int j = 0; j <= d; j++) {
                M[i][j] = 0.0;
            }
        }
        // 分成四个区块进行赋值计算
        for (int n = 0; n < featureVector.size(); n++) {
            // 第一行
            for (int j = 0; j <= d - 1; j++) {
                M[0][j] += featureVector.get(n).get(j);
            }
            // 第一行的第 d 列
            M[0][d] += targetVariable.get(n);
            // 第二行到第 d 行
            for (int i = 1; i <= d; i++) {
                for (int j = 0; j <= d - 1; j++) {
                    M[i][j] += featureVector.get(n).get(j) * featureVector.get(n).get(i - 1);
                }
            }
            // 第二行到第 d 行的第 d 列
            for (int i = 1; i <= d; i++) {
                M[i][d] += targetVariable.get(n) * featureVector.get(n).get(i - 1);
            }
        }

        for (int i = 0; i <= d; i++) {
            for (int j = 0; j <= d; j++) {
                System.out.print(M[i][j] + " ");
            }
            System.out.println();
        }
    }

    @Test
    public void SecureDataAggregationAlgorithmUtils(){
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

        BigInteger x1 = new BigInteger(64, 64, new Random());
        BigInteger x2 = new BigInteger(64, 64, new Random());
        BigInteger x3 = new BigInteger(64, 64, new Random());
        BigInteger x4 = new BigInteger(64, 64, new Random());
        BigInteger m1 = SecureDataAggregationAlgorithmUtils.DataEncryption(x1, (PublicParameters) pp[0], (SK_DO) sk_dos[0]);
        BigInteger m2 = SecureDataAggregationAlgorithmUtils.DataEncryption(x2, (PublicParameters) pp[0], (SK_DO) sk_dos[0]);
        BigInteger m3 = SecureDataAggregationAlgorithmUtils.DataEncryption(x3, (PublicParameters) pp[0], (SK_DO) sk_dos[1]);
        BigInteger m4 = SecureDataAggregationAlgorithmUtils.DataEncryption(x4, (PublicParameters) pp[0], (SK_DO) sk_dos[1]);

        BigInteger m = SecureDataAggregationAlgorithmUtils.DataAggregation(m1.multiply(m2).multiply(m3).multiply(m4), (PublicParameters) pp[0]);

        BigInteger res = SecureDataAggregationAlgorithmUtils.AggregatedResultDecryption(m, (PublicParameters) pp[0], (SK_CSP) sk_csp[0]);
        System.out.println(res);
        BigInteger add = SecureDataAggregationAlgorithmUtils.DataAggregation(x1.add(x2).add(x3).add(x4), (PublicParameters) pp[0]);
        System.out.println(add);
        System.out.println(Objects.equals(res, add));
    }

    @Test
    public void PaillierCryptosystemUtils(){
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

        BigInteger x1 = new BigInteger(64, 64, new Random());
        BigInteger x2 = new BigInteger(64, 64, new Random());
        BigInteger m1 = PaillierCryptosystemUtils.Encryption(x1, (PublicParameters) pp[0]);
        BigInteger m2 = PaillierCryptosystemUtils.Encryption(x2, (PublicParameters) pp[0]);

        BigInteger m = m1.multiply(m2);
        System.out.println(x1.add(x2));
        System.out.println(PaillierCryptosystemUtils.Decryption(m, (PublicParameters) pp[0], (SK_CSP) sk_csp[0]));
    }
}
