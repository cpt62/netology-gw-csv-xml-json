import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        // task 1
        CsvToBean<Employee> toBean;
        EmployeeJsonXMLBuilder employeeJsonBuilder = new EmployeeJsonXMLBuilder();

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

            employeeJsonBuilder.createJsonFromCSV("data.json", toBean);

        } catch (IOException ioException) {
            System.out.println(ioException.getMessage());
        }


        //task 2
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File("data.xml"));
            employeeJsonBuilder.createJsonFromXML("data2.json", document);

        } catch (ParserConfigurationException e) {
            System.out.println(e.getMessage());
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }

        //task3
        employeeJsonBuilder.jsonToList("data.json").forEach(System.out::println);

    }
}
