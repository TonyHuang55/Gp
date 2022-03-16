import Pojo.AdvancedPaillier;
import Pojo.CloudServiceProvider;
import Pojo.DataOwner;
import Pojo.TrustAuthority;
import org.apache.commons.csv.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class test {
    @Test
    public void test1() throws IOException {
        TrustAuthority ta = new TrustAuthority(512);
        AdvancedPaillier ap = ta.getAP();
        HashMap<String, BigInteger[]> map = ta.keyGenerate(3);

        for (String s : map.keySet()) {
            System.out.println(s + ":" + Arrays.toString(map.get(s)));
        }

        BigInteger[] sk_dos = map.get("SK_DO");

        DataOwner do1 = new DataOwner(ap);
        DataOwner do2 = new DataOwner(ap);
        DataOwner do3 = new DataOwner(ap);
        DataOwner[] dataOwners = {do1, do2, do3};

        BigInteger[] X = new BigInteger[dataOwners.length];
        for (int i = 0; i < dataOwners.length; i++) {
            X[i] = dataOwners[i].DataEncryption(sk_dos[i]);
        }

        System.out.println(Arrays.toString(X));

        CloudServiceProvider csp = new CloudServiceProvider(ap);
        BigInteger XSum = csp.DataAggregation(X);
        System.out.println(XSum);
        BigInteger res = csp.AggregatedResultDecryption(XSum);
        System.out.println(res);

        System.out.println("加密");

        List<List[]> DOs = new ArrayList<>();
        for (int i = 0; i < dataOwners.length; i++) {
            List[] lists = dataOwners[i].dataNormalization("src/main/resources/database/winequality-red.csv");
            DOs.add(lists);
            System.out.println("DO" + i + ":" + Arrays.toString(lists));
        }

        List[] taList = ta.globalDataNormalization(DOs);
        System.out.println(Arrays.toString(taList));

        dataOwners[0].localDatasetNormalize(taList[0],taList[1]);

        System.out.println("数据归一化");


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
}
