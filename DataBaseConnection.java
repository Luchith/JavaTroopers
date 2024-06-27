package DBLayer;


import java.sql.*;

public class DataBaseConnection {
    private final String URL="jdbc:mysql://localhost:3306/shipshape";
    private final String UName="root";
    private final String Password="";
    private static DataBaseConnection Instance;
    private Connection con;


    public DataBaseConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con= DriverManager.getConnection(URL,UName,Password);
            System.out.println("DataBase Connection Success");
        }catch (ClassNotFoundException ex){
            System.out.println("Driver Class Error"+ex.getMessage());
        }catch (SQLException ex) {
            System.out.println("DataBase Connection Error"+ex.getMessage());
        }

    }
    public static DataBaseConnection getSingleInstance(){
        try {
            if (Instance==null){
                Instance = new DataBaseConnection();
            } else if (Instance.con.isClosed()) {
                Instance = new DataBaseConnection();
            }else {
                return Instance;
            }
            return Instance;
        }catch (SQLException ex){
            System.out.println("DataBase Connection Error"+ex.getMessage());
            return null;
        }
    }
    public Connection getConnection() {
        return con;
    }
    public boolean ExecuteQuery(String sqlQ){
        try {
            Statement st = con.createStatement();
            int result = st.executeUpdate(sqlQ);
            return result > 0;
        }catch (SQLException ex){
            System.out.println("SQL Error"+ex.getMessage());
            return false;
        }
    }
    public ResultSet executeQuery(String query) {
        try {
            Statement st = con.createStatement();
            return st.executeQuery(query);
        } catch (SQLException ex) {
            System.out.println("SQL Error" + ex.getMessage());
            return null;
        }
    }
}

