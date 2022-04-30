package Pojo;

import Pojo.Keys.PublicParameters;
import Pojo.Keys.SK_CSP;
import Utils.SecureDataAggregationAlgorithmUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ZERO;

public class CloudServiceProvider {
    /**
     * 将 m 个矩阵聚合
     *
     * @param M  矩阵 List
     * @param pp 公共参数
     * @return 聚合后的矩阵
     */
    public BigInteger[][] localTrainingDataAggregation(List<BigInteger[][]> M, PublicParameters pp) {
        int d = M.get(0).length;
        BigInteger[][] fin = new BigInteger[d][d];
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                fin[i][j] = new BigInteger("1");
            }
        }
        // 做哈达玛积
        for (BigInteger[][] multiply : M) {
            for (int i = 0; i < d; i++) {
                for (int j = 0; j < d; j++) {
                    BigInteger cur = fin[i][j];
                    fin[i][j] = cur.multiply(multiply[i][j]);
                }
            }
        }
        // 聚合
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                fin[i][j] = SecureDataAggregationAlgorithmUtils.DataAggregation(fin[i][j], pp);
            }
        }
        return fin;
    }

    /**
     * 对聚合矩阵解密
     *
     * @param fin    聚合矩阵
     * @param pp     公共参数
     * @param sk_csp CSP秘钥
     * @return 聚合明文
     */
    public BigInteger[][] trainAggregatedResultDecryption(BigInteger[][] fin, PublicParameters pp, SK_CSP sk_csp) {
        int d = fin.length;
        // 解密
        for (int i = 0; i < d; i++) {
            for (int j = 0; j < d; j++) {
                fin[i][j] = SecureDataAggregationAlgorithmUtils.AggregatedResultDecryption(fin[i][j], pp, sk_csp);
            }
        }
        return fin;
    }

    /**
     * 模型评估
     *
     * @param url 测试数据集路径
     * @param a   回归模型的回归系数
     * @return 残差平方和
     * @throws Exception 文件读取异常
     */
    public static BigDecimal ModelEstimation(String url, BigDecimal[] a) throws Exception {
        // 1.配置数据路径，读取数据文件
        FileReader fileReader = new FileReader(url);
        // 2.读取 csv 文件第一行标题
        String combineHeaders = new BufferedReader(new FileReader(url)).readLine();
        String[] headers = combineHeaders.split(";");
        // 3.CSVFormat 解析，采用 EXCEL 枚举，标题为读取到的第一行分割标题，分割符为 `,`
        CSVFormat format = CSVFormat.EXCEL.withHeader(headers).withDelimiter(';');
        // 4.读取数据
        CSVParser parser = new CSVParser(fileReader, format);
        List<CSVRecord> totalData = parser.getRecords();
        List<List<Double>> featureVector = new ArrayList<>();
        List<Double> targetVariable = new ArrayList<>();
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

        BigDecimal RSS = ZERO;
        for (int i = 0; i < featureVector.size(); i++) {
            List<Double> x = featureVector.get(i);
            Double y = targetVariable.get(i);
            BigDecimal preY = a[0];
            for (int j = 1; j < a.length; j++) {
                preY = preY.add(a[j].multiply(BigDecimal.valueOf(x.get(j - 1))));
            }
            BigDecimal sub = preY.subtract(new BigDecimal(y));
            RSS = RSS.add(sub.multiply(sub));
        }
        return RSS;
    }
}
