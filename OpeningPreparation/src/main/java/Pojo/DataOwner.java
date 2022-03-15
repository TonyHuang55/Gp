package Pojo;

import Utils.BigIntegerUtils;
import Utils.DataNormalizationUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class DataOwner {
    /**
     * 准确度
     */
    private final static int certainty = 64;

    protected static AdvancedPaillier advancedPaillier;

    public DataOwner(AdvancedPaillier advancedPaillier) {
        DataOwner.advancedPaillier = advancedPaillier;
    }

    public BigInteger DataEncryption(BigInteger SK_DO) {
        BigInteger N = advancedPaillier.getN(), g = advancedPaillier.getG(), h = advancedPaillier.getH();
        BigInteger x = getRandomX(N);
        BigInteger r = getRandomR(advancedPaillier.getKapa());
        return g.modPow(x, N.multiply(N)).multiply(h.modPow(r, N.multiply(N))).multiply(SK_DO).mod(N.multiply(N));
    }

    private static BigInteger getRandomX(BigInteger N) {
        // a private message of DO_i
        return BigIntegerUtils.validRandomInResidueSystem(N);
    }

    private static BigInteger getRandomR(int kapa) {
        // a random number which satisfies |r_i| < κ/2
        return new BigInteger(kapa / 2, certainty, new Random());
    }

    public List[] dataNormalization(String localURL) throws IOException {
        // 1.配置数据路径，读取数据文件
        FileReader fileReader = new FileReader(localURL);
        // 2.读取 csv 文件第一行标题
        String combineHeaders = new BufferedReader(new FileReader(localURL)).readLine();
        String[] headers = combineHeaders.split(";");
        for (int i = 0; i < headers.length; i++) {
            String tmp = headers[i];
            headers[i] = tmp.substring(1, tmp.length() - 1);
        }
        // 3.CSVFormat 解析，采用 EXCEL 枚举，标题为读取到的第一行分割标题，分割符为 `;`
        CSVFormat format = CSVFormat.EXCEL.withHeader(headers).withDelimiter(';');
        // 4.读取数据
        CSVParser parser = new CSVParser(fileReader, format);
        List<CSVRecord> records = parser.getRecords();

        // 5.分离特征向量和目标变量

        HashMap<List<Double>, Double> D = new LinkedHashMap<>();
        for (int count = 1; count < records.size(); count++) {
            CSVRecord record = records.get(count);
            List<Double> featureVector = new ArrayList<>();
            for (int d = 0; d < record.size()-1; d++) {
                featureVector.add(Double.valueOf(record.get(d)));
            }
            D.put(featureVector, Double.valueOf(record.get(record.size() - 1)));
        }

        List<List<Double>> lists = new ArrayList<>(D.keySet());
        List<Double> maxFeature = DataNormalizationUtils.maxCalculate(lists);
        List<Double> minFeature = DataNormalizationUtils.minCalculate(lists);

        return new List[]{maxFeature, minFeature, Collections.singletonList((records.size() - 1))};
    }
}