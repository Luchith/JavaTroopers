package ServiceLayer;

import DBLayer.DataBaseConnection;

import java.sql.*;

public class SaleReportService {
    Connection con;

    private DataBaseConnection singleCon;

    public SaleReportService(){
        singleCon = DataBaseConnection.getSingleInstance();
        con = singleCon.getConnection();
    }

    public int CalTotOrders(){
        try {

            Statement st = con.createStatement();
            String query = "SELECT count(*) from `customer_order`";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {

                int count = rs.getInt(1);
                return count;
            } else {

                return 0;
            }

        }catch (Exception ex) {
            System.out.println("Cannot get order count");
            return 0;
        }
    }

    public int CalTotSuppliers(){
        try {

            Statement st = con.createStatement();

            String query = "SELECT count(*) from `supplier`";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                // Retrieve the count from the first column (which is the only column in this case)
                int count = rs.getInt(1);
                return count;
            } else {

                return 0;
            }

        }catch (Exception ex) {
            System.out.println("Cannot get supplier count");
            return 0;
        }
    }

    public int CalTotEmployees(){
        try {

            Statement st = con.createStatement();

            String query = "SELECT count(*) from `employee`";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {
                // Retrieve the count from the first column (which is the only column in this case)
                int count = rs.getInt(1);
                return count;
            } else {

                return 0;
            }

        }catch (Exception ex) {
            System.out.println("Cannot get employee count");
            return 0;
        }
    }

    public int CalTotItems(){
        try {

            Statement st = con.createStatement();

            String query = "SELECT count(*) from `Products`";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {

                int count = rs.getInt(1);
                return count;
            } else {

                return 0;
            }

        }catch (Exception ex) {
            System.out.println("Cannot get item count");
            return 0;
        }
    }

    public int CalTotPaintOrders(){
        try {

            Statement st = con.createStatement();

            String query = "SELECT count(*) from `customer_order` where OrderCategory = 'Paint'";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {

                int count = rs.getInt(1);
                return count;
            } else {

                return 0;
            }

        }catch (Exception ex) {
            System.out.println("Cannot get count of Paint orders");
            return 0;
        }
    }

    public int CalTotSPOrders(){
        try {

            Statement st = con.createStatement();

            String query = "SELECT count(*) from `customer_order` where OrderCategory = 'Spair Part' ";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {

                int count = rs.getInt(1);
                return count;
            } else {

                return 0;
            }

        }catch (Exception ex) {
            System.out.println("Cannot get count of Spair Parts orders");
            return 0;
        }
    }

    public int CalTotRapirOrders(){
        try {

            Statement st = con.createStatement();

            String query = "SELECT count(*) from `customer_order` where OrderCategory = 'Rapair'";
            ResultSet rs = st.executeQuery(query);
            if (rs.next()) {

                int count = rs.getInt(1);
                return count;
            } else {

                return 0;
            }

        }catch (Exception ex) {
            System.out.println("Cannot get count of Rapair orders");
            return 0;
        }
    }

}
