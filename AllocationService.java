package ServiceLayer;

import DBLayer.DataBaseConnection;

import java.sql.ResultSet;

public class AllocationService {
    private DataBaseConnection singleCon;

    public AllocationService() {
        singleCon = DataBaseConnection.getSingleInstance();
    }

    public boolean assignOrder(String orderID, String employeeName) {
        try {

            String fetchEmpIdQuery = "SELECT EmpID FROM employee WHERE EmpName = '" + employeeName + "'";
            ResultSet rs = singleCon.executeQuery(fetchEmpIdQuery);
            String employeeId = null;
            if (rs.next()) {
                employeeId = rs.getString("EmpID");
            }
            rs.close();

            if (employeeId == null) {
                System.out.println("No employee found with the name: " + employeeName);
                return false;
            }

            String query = "insert into allocation (EID, OID) values ('" + employeeId + "', '" + orderID + "')";
            boolean result = singleCon.ExecuteQuery(query);

            if (result) {
                String updateQuery = "update customer_order set Status = 'Allocate' where Order_ID = '" + orderID + "'";
                return singleCon.ExecuteQuery(updateQuery);
            }
            return false;
        } catch (Exception ex) {
            System.out.println("Cannot assign order");
            ex.printStackTrace();
            return false;
        }
    }


}
