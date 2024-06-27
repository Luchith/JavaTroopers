package ServiceLayer;

import DBLayer.DataBaseConnection;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private DataBaseConnection singleCon;
    public EmployeeService(){
        singleCon = DataBaseConnection.getSingleInstance();
    }

    public List<String> getAllEmployees() {
        List<String> employees = new ArrayList<>();
        try {
            String query = "SELECT * FROM employee";
            ResultSet rs = singleCon.executeQuery(query);

            while (rs.next()) {
                employees.add(rs.getString("EmpName"));
            }
            rs.close();
        } catch (Exception ex) {
            System.out.println("Cannot fetch employee data");
            ex.printStackTrace();
        }
        return employees;
    }
}
