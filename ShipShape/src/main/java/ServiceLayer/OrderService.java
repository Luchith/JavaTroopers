package ServiceLayer;

import DBLayer.DataBaseConnection;
public class OrderService {
    private DataBaseConnection singleCon;
    public OrderService(){
        singleCon = DataBaseConnection.getSingleInstance();
    }


    public boolean addOrder(String PID,String OrderName,String Email,String Category,String Stts){
        try {
            String querry = "insert into customer_order (Order_ID,CustomerName, CustomerEmail, OrderCategory,Status) values('"+PID+"','"+OrderName+"','"+Email+"','"+Category+"','"+Stts+"')";

            boolean result = singleCon.ExecuteQuery(querry);
            return result;
        }catch (Exception ex){
            System.out.println("Cannot Add This Order");
            return false;
        }
    }

    public boolean UpdateOrder(String OrderID,String OrderName,String Email,String Category,String Stts){
        try {
            String querry = "update customer_order set CustomerName='"+OrderName+"',CustomerEmail='"+Email+"',OrderCategory='"+Category+"',Status='"+Stts+"' where Order_ID='"+OrderID+"'";

            boolean result = singleCon.ExecuteQuery(querry);
            return result;
        }catch (Exception ex){
            System.out.println("Cannot Update This Order");
            return false;
        }
    }

    public boolean DeleteOrder(String OrderID){
        try {
            String querry = "delete from customer_order where Order_ID='"+OrderID+"'";

            boolean result = singleCon.ExecuteQuery(querry);
            return result;
        }catch (Exception ex){
            System.out.println("Cannot Delete This Order");
            return false;
        }
    }
}
