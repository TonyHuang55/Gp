package Pojo;

import Utils.DataNormalizationUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataOwner {
    /**
     * 准确度
     */
    private final static int certainty = 64;
    private final static int kapa = 512;

    private List<CSVRecord> totalData;
    private List<List<Double>> featureVector;
    private List<Double> targetVariable;

    public List[] dataNormalization(String localURL) throws IOException {
        read(localURL);

        HashMap<List<Double>, Double> D = new LinkedHashMap<>();
        // 排除 head，从实际数据开始记录
        for (int count = 1; count < totalData.size(); count++) {
            CSVRecord record = totalData.get(count);
            List<Double> featureVector = new ArrayList<>();
            // 排除目标向量 y
            for (int d = 0; d < record.size() - 1; d++) {
                featureVector.add(Double.valueOf(record.get(d)));
            }
            // 存入键值对为：(特征向量,目标向量)
            D.put(featureVector, Double.valueOf(record.get(record.size() - 1)));
        }

        featureVector = new ArrayList<>(D.keySet());
        targetVariable = new ArrayList<>(D.values());
        List<Double> maxFeature = DataNormalizationUtils.maxCalculate(featureVector);
        List<Double> minFeature = DataNormalizationUtils.minCalculate(featureVector);

        return new List[]{maxFeature, minFeature, Collections.singletonList((totalData.size() - 1))};
    }

    private void read(String localURL) throws IOException {
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
        totalData = parser.getRecords();
    }

    public void localDatasetNormalize(List<Double> max, List<Double> min) {
        for (int i = 0; i < featureVector.size(); i++) {
            List<Double> list = featureVector.get(i);
            for (int j = 0; j < list.size(); j++) {
                list.set(j, (list.get(j) - min.get(j)) / (max.get(j) - min.get(j)));
            }
        }
//        for (List<Double> localDatum : localData) {
//            System.out.println(localDatum);
//        }
    }
}