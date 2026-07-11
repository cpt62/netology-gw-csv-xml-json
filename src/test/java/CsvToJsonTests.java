import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
                            .withThrowExceptions(true)
                            .build();

            builder.createJsonFromCSV(jsonFileName, toBean);

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());

        }


    }

    @ParameterizedTest
    @CsvSource({
            "TestFolder/01_task/inputCSV/data.csv, TestFolder/01_task/outputJson/data.json, TestFolder/01_task/act/data.json",
            "TestFolder/01_task/inputCSV/data_big.csv, TestFolder/01_task/outputJson/data_big.json, TestFolder/01_task/act/data_big.json"
    })
    public void equalsResultConvertCsvToJson(String inputCsvFile, String expectedJsonFile, String actualJsonFile) {

        List<Employee> expected, actual;

        getCsvToJson(inputCsvFile, expectedJsonFile);
        expected = builder.jsonToList(expectedJsonFile);
        actual = builder.jsonToList(actualJsonFile);

        //then
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void emptyCsvFileTest() throws IOException {
        String inputEmptyCsv = "TestFolder/01_task/inputCSV/empty.csv";
        // Данная переменная выступает в роли заглушки
        String actEmptyJson = "TestFolder/01_task/act/data_empty.json";
        Assertions.assertThrows(IllegalArgumentException.class, () -> getCsvToJson(inputEmptyCsv, actEmptyJson));

    }


    @AfterAll
    public static void clear() {
        if (Files.exists(Path.of("TestFolder/01_task/outputJson"))) {
            try {
                Files.walk(Path.of("TestFolder/01_task/outputJson"))
                        .skip(1)
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}




