import com.google.gson.*;
import com.opencsv.bean.CsvToBean;
import org.w3c.dom.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeeJsonXMLBuilder {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<Employee> parseCSV(CsvToBean<Employee> toBean) {
        List<Employee> list = toBean.parse();
        if (list.isEmpty()) throw new IllegalArgumentException("CSV пуст");
        return list;
    }

    private String objToJson(List<Employee> employee) {
        return gson.toJson(employee);
    }

    private String readString(String jsonFileName) {
        StringBuilder result = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return result.toString();
        }

        return result.toString();
    }

    public List<Employee> jsonToList(String json) {
        return Arrays.asList(gson.fromJson(readString(json), Employee[].class));

    }


    public void createJsonFromCSV(String fileName, CsvToBean<Employee> toBean) throws IOException {
        File file = new File(fileName);
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(objToJson(parseCSV(toBean)));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private List<Employee> parseXML(Document document) {
        if (document == null) throw new NullPointerException("Файл пуст или указана неверная ссылка");
        List<Employee> employeeList = new ArrayList<>();
        NodeList nodeList = document.getElementsByTagName("employee");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Employee emp = new Employee();
                Element element = (Element) node;
                emp.setId(Long.parseLong(element.getElementsByTagName("id").item(0).getTextContent()));
                emp.setFirstName(element.getElementsByTagName("firstName").item(0).getTextContent());
                emp.setLastName(element.getElementsByTagName("lastName").item(0).getTextContent());
                emp.setCountry(element.getElementsByTagName("country").item(0).getTextContent());
                emp.setAge(Integer.parseInt(element.getElementsByTagName("age").item(0).getTextContent()));
                employeeList.add(emp);
            }
        }
        return employeeList;
    }

    public void createJsonFromXML(String fileName, Document document) {
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write(gson.toJson(parseXML(document)));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

}

