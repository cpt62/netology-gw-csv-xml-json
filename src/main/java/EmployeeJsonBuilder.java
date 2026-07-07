import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.bean.CsvToBean;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EmployeeJsonBuilder {
    private static final Gson gson = new GsonBuilder().create();

    private List<Employee> csvToObject(CsvToBean<Employee> toBean) {
        return toBean.parse();
    }

    private String objToJson(List<Employee> employee) {
        return gson.toJson(employee);
    }

    public void create(String fileName, CsvToBean<Employee> toBean) {
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(objToJson(csvToObject(toBean)));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
