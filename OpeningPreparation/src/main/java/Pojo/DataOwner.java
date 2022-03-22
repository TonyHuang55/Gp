package Pojo;

import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_DO;
import Utils.DataNormalizationUtils;
import Utils.SecureDataAggregationAlgorithmUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

public class DataOwner {
    private List<CSVRecord> totalData;
    private List<List<Double>> featureVector;
    private List<Double> targetVariable;
    private Double[][] M;

    public List[] dataNormalization(String localURL) throws IOException {
        read(localURL);

        featureVector = new ArrayList<>();
        targetVariable = new ArrayList<>();
        // 排除 head，从实际数据开始记录
        for (int count = 1; count < totalData.size(); count++) {
            CSVRecord record = totalData.get(count);
            List<Double> fv = new ArrayList<>();
            // 排除目标向量 y
            for (int d = 0; d < record.size() - 1; d++) {
                fv.add(Double.valueOf(record.get(d)));
            }
            featureVector.add(fv);
            targetVariable.add(Double.valueOf(record.get(record.size() - 1)));
        }

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

    public void localFeatureVectorNormalize(List<Double> max, List<Double> min) {
        for (int i = 0; i < featureVector.size(); i++) {
            List<Double> list = featureVector.get(i);
            for (int j = 0; j < list.size(); j++) {
                list.set(j, (list.get(j) - min.get(j)) / (max.get(j) - min.get(j)));
            }
        }
    }

    public void dataPreprocessing() {
        int d = featureVector.get(0).size();
        // 初始化
        M = new Double[d + 1][d + 1];
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
    }

    public Double[][] getM(){
        return M;
    }

    public BigInteger[][] localTrainingDataEncryption(PublicParameters pp, SK_DO sk_do) {
        int d = M.length - 1;
        BigInteger[][] Mi = new BigInteger[M.length][M.length];
        for (int i = 0; i <= d; i++) {
            for (int j = 0; j <= d; j++) {
                String integer = String.valueOf(Math.floor(M[i][j] * 1000));
                BigInteger m = new BigInteger(integer.substring(0, integer.length() - 2));
                Mi[i][j] = SecureDataAggregationAlgorithmUtils.DataEncryption(m, pp, sk_do);
            }
        }
        return Mi;
    }
}