import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        CsvToBean<Employee> toBean;

        ColumnPositionMappingStrategy<Employee> strategy
                = new ColumnPositionMappingStrategy<>();
        strategy.setType(Employee.class);
        strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");


        try (FileReader fr = new FileReader("data.csv")) {
            toBean =
                    new CsvToBeanBuilder<Employee>(fr)
                            .withSeparator(',')
                            .withMappingStrategy(strategy)
                            .build();

            EmployeeJsonBuilder employeeJsonBuilder = new EmployeeJsonBuilder();
            employeeJsonBuilder.create("data.json", toBean);

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }

    }
}
