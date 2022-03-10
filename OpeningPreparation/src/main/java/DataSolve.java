import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * @author Huang
 */
public class DataSolve {
    public void dataRead(String sourcePath) throws IOException {
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


    }
}
