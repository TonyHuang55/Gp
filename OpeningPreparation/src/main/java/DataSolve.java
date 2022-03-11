import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Huang
 */
public class DataSolve {
    public static void dataRead(String sourcePath) throws IOException {
        // 1.配置数据路径，读取数据文件
        FileReader fileReader = new FileReader(sourcePath);
        // 2.读取 csv 文件第一行标题
        String combineHeaders = new BufferedReader(new FileReader(sourcePath)).readLine();
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
        // 6.同时对特征向量进行记录，获取到 max 和 min
        List<Double> maxFeature = new ArrayList<>(), minFeature = new ArrayList<>();
        // 用第一组数据对 max 和 min 进行初始化
        for (int d = 0; d < records.get(1).size() - 1; d++) {
            maxFeature.add(Double.valueOf(records.get(1).get(d)));
            minFeature.add(Double.valueOf(records.get(1).get(d)));
        }

        HashMap<List<Double>, Double> D = new LinkedHashMap<List<Double>, Double>();
        for (int count = 1; count < records.size(); count++) {
            CSVRecord record = records.get(count);
            List<Double> featureVector = new ArrayList<>();
            for (int d = 0; d < record.size() - 1; d++) {
                Double current = Double.valueOf(record.get(d));
                featureVector.add(current);
                // 比较更新 max 和 min
                if (maxFeature.get(d) <= current) maxFeature.set(d, current);
                if (minFeature.get(d) >= current) minFeature.set(d, current);
            }
            D.put(featureVector, Double.valueOf(record.get(record.size() - 1)));
        }
    }
}
