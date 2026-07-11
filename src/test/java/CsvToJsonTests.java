import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class CsvToJsonTests {
    private static final EmployeeJsonXMLBuilder builder;
    private static final ColumnPositionMappingStrategy<Employee> strategy;
    private static CsvToBean<Employee> toBean;

    static {
        builder = new EmployeeJsonXMLBuilder();

        strategy = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");


    }

    private void getCsvToJson(String csvFileName, String jsonFileName) {

        try (FileReader fr = new FileReader(csvFileName)) {
            toBean =
                    new CsvToBeanBuilder<Employee>(fr)
                            .withSeparator(',')
                            .withMappingStrategy(strategy)
                            .build();

            builder.createJsonFromCSV(jsonFileName, toBean);

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }


    }

    @ParameterizedTest
    @CsvSource({
            "TestFolder/01_task/inputCSV/data.csv",
            "TestFolder/01_task/outputJson/data.json",
            "TestFolder/01_task/act/data.json",

            "TestFolder/01_task/inputCSV/data_big.csv",
            "null",
            ""
    })
    public void equalsResultConvertCsvToJson(String inputCsvFile, String expectedJsonFile, String actualJsonFile) {

        List<Employee> expected, actual;

        getCsvToJson(inputCsvFile, expectedJsonFile);
        expected = builder.jsonToList(expectedJsonFile);
        actual = builder.jsonToList(actualJsonFile);

        //then
        Assertions.assertEquals(expected, actual);
    }


}
