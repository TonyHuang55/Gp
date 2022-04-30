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
    /**
     * 数据集原始数据
     */
    private List<CSVRecord> totalData;
    /**
     * 特征向量集合
     */
    private List<List<Double>> featureVector;
    /**
     * 目标变量集合
     */
    private List<Double> targetVariable;
    /**
     * 归一化矩阵
     */
    private Double[][] M;

    /**
     * 统计最值向量
     *
     * @param localURL 本地数据集路径
     * @return 传输给 CSP 的最值向量
     * @throws IOException 文件读取异常
     */
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
        Double maxTarget = DataNormalizationUtils.maxTarget(targetVariable);
        Double minTarget = DataNormalizationUtils.minTarget(targetVariable);

        return new List[]{maxFeature, minFeature, Collections.singletonList((totalData.size() - 1)), Arrays.asList(maxTarget, minTarget)};
    }

    /**
     * 读取 csv 文件
     *
     * @param localURL 本地数据集路径
     * @throws IOException 文件读取异常
     */
    private void read(String localURL) throws IOException {
        // 1.配置数据路径，读取数据文件
        FileReader fileReader = new FileReader(localURL);
        // 2.读取 csv 文件第一行标题
        String combineHeaders = new BufferedReader(new FileReader(localURL)).readLine();
        String[] headers = combineHeaders.split(";");
        // 3.CSVFormat 解析，采用 EXCEL 枚举，标题为读取到的第一行分割标题，分割符为 `,`
        CSVFormat format = CSVFormat.EXCEL.withHeader(headers).withDelimiter(';');
        // 4.读取数据
        CSVParser parser = new CSVParser(fileReader, format);
        totalData = parser.getRecords();
    }

    /**
     * 特征向量归一化
     *
     * @param max 全局最大值向量
     * @param min 全局最小值向量
     */
    public void localFeatureVectorNormalize(List<Double> max, List<Double> min) {
        for (int i = 0; i < featureVector.size(); i++) {
            List<Double> list = featureVector.get(i);
            for (int j = 0; j < list.size(); j++) {
                list.set(j, (list.get(j) - min.get(j)) / (max.get(j) - min.get(j)));
            }
        }
    }

    /**
     * 目标变量归一化
     *
     * @param M 全局最值变量
     */
    public void localTargetNormalize(List<Double> M) {
        for (int i = 0; i < targetVariable.size(); i++) {
            Double target = targetVariable.get(i);
            targetVariable.set(i, (target - M.get(1)) / (M.get(0) - M.get(1)));
        }
    }

    /**
     * 生成聚合矩阵
     */
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

    /**
     * 获取矩阵
     *
     * @return
     */
    public Double[][] getM() {
        return M;
    }

    /**
     * 加密
     *
     * @param pp    公共参数
     * @param sk_do DOs密钥
     * @return 加密后的聚合矩阵
     */
    public BigInteger[][] localTrainingDataEncryption(PublicParameters pp, SK_DO sk_do) {
        int d = M.length - 1;
        BigInteger[][] Mi = new BigInteger[M.length][M.length];
        for (int i = 0; i <= d; i++) {
            for (int j = 0; j <= d; j++) {
                String integer = String.valueOf(Math.floor(M[i][j] * 1000));
                BigInteger m = new BigInteger(integer.substring(0, integer.indexOf('.')));
                Mi[i][j] = SecureDataAggregationAlgorithmUtils.DataEncryption(m, pp, sk_do);
            }
        }
        return Mi;
    }
}