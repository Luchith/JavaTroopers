package View.ManageOrder;


import DBLayer.DataBaseConnection;
import ServiceLayer.OrderService;
import View.HomeView;
import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ManageOrder extends JFrame{
    private JTextField txtSrch;
    private JButton btnSrch;
    private JButton btnShowAll;
    private JTextField txtPID;
    private JTextField txtPName;
    private JTextField txtPPrice;
    private JComboBox PcatBox;
    private JButton ADDButton;
    private JButton btnHome;
    private JButton DELETEButton;
    private JButton UPDATEButton;
    public JPanel contentPane;
    private JLabel lblError;
    private JTable tblP;
    private JComboBox stsBox;
    DataBaseConnection dataBaseConnection;
    PreparedStatement pst;
    Connection con;


    public ManageOrder() {
        dataBaseConnection = DataBaseConnection.getSingleInstance();
        con = dataBaseConnection.getConnection();
        OrderService mng=new OrderService();
        tableLoad();
        ganrateID();
        btnHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                HomeView v1 = new HomeView();
                setVisible(false);
                v1.setContentPane(v1.contentPane);
                v1.setTitle("Home");
                v1.setSize(600, 600);
                v1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                v1.setVisible(true);
            }
        });
        ADDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String OrderID = txtPID.getText();
                String OrderName = txtPName.getText();
                String OrderPrice=txtPPrice.getText();
                String OrderCategory = (String) PcatBox.getSelectedItem();
                String OrderStatus = (String) stsBox.getSelectedItem();



                if (OrderName.isEmpty() || OrderPrice.isEmpty() || OrderCategory.equals("")) {
                    if(OrderCategory.equals("")){
                        lblError.setText("Please Select Catagory");
                    }
                    else if(OrderStatus.equals("")){
                        lblError.setText("Please Select Status");
                    }
                    else{
                        lblError.setText("Fill The Blanks");}

                }

                else {

                    if(!OrderID.isEmpty())
                    {
                        JOptionPane.showMessageDialog(contentPane,"The ID You Entered Is Replaced With An Auto Generated ID", "Alert", 1);
                    }
                    lblError.setText("");

                    if(mng.addOrder(ganrateID(),OrderName,OrderPrice,OrderCategory,OrderStatus)){
                        JOptionPane.showMessageDialog(contentPane,"Order Successfully Added to the DataBase", "Success", 1);
                        tableLoad();
                        clear();

                    }else {
                        JOptionPane.showMessageDialog(contentPane,"Cannot insert to the DB", "Error", 1);
                    }
                }
            }
        });
        //table eke row select krnn
        tblP.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectedRow = tblP.getSelectedRow();


                if (selectedRow >= 0) {
                    String OrderID = tblP.getValueAt(selectedRow, 0).toString();
                    String OrderName = tblP.getValueAt(selectedRow, 1).toString();
                    String OrderPrice = tblP.getValueAt(selectedRow, 2).toString();
                    String OrderCategory = tblP.getValueAt(selectedRow, 3).toString();
                    String OrderStatus = tblP.getValueAt(selectedRow, 4).toString();


                    txtPID.setText(OrderID);
                    txtPName.setText(OrderName);
                    txtPPrice.setText(OrderPrice);
                    PcatBox.setSelectedItem(OrderCategory);
                    stsBox.setSelectedItem(OrderStatus);
                }
            }
        });



        btnShowAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableLoad();
                clear();
            }
        });


        btnSrch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword=txtSrch.getText();

                if (!keyword.equals("")) {
                    try {
                        pst = con.prepareStatement("select CustomerName,CustomerEmail,OrderCategory,Order_ID,Status from customer_order where Order_ID = '"+keyword+"'  OR CustomerName = '"+keyword+"'");
                        ResultSet rs = pst.executeQuery();

                        if (rs.next()) {

                            String Name = rs.getString(1);
                            String Price = rs.getString(2);
                            String Cat = rs.getString(3);
                            String id = rs.getString(4);
                            String stts=rs.getString(5);
                            tableserch(keyword);
                            txtPID.setText(id);
                            lblError.setText("");
                            txtPName.setText(Name);
                            txtPPrice.setText(Price);
                            PcatBox.setSelectedItem(Cat);
                            stsBox.setSelectedItem(stts);
                        } else {

                            tableLoad();
                            clear();
                            lblError.setText(keyword + " is Invalid Keyword");

                        }
                    } catch (SQLException e1) {
                        System.out.println("SQL ISSE");
                    }
                } else {
                    tableLoad();
                    clear();
                    lblError.setText("Please Enter ID Or Order Name");
                }
            }
        });


        UPDATEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name, price, cat, id,stts;

                name = txtPName.getText();
                price = txtPPrice.getText();
                cat = (String)PcatBox.getSelectedItem();
                stts = (String)stsBox.getSelectedItem();
                id = txtPID.getText();

                if(mng.UpdateOrder(id,name,price,cat,stts)){
                    JOptionPane.showMessageDialog(contentPane, "Update succesful","Success",JOptionPane.INFORMATION_MESSAGE);
                    tableLoad();
                    clear();
                }
                else{
                    JOptionPane.showMessageDialog(contentPane,"Cannot UPDATE DB", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });


        DELETEButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id=txtPID.getText();
                int confirmation = JOptionPane.showConfirmDialog(
                        null,
                        "Are you sure you want to delete this Order?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );if (confirmation == JOptionPane.YES_OPTION) {
                    if(mng.DeleteOrder(id)) {
                        JOptionPane.showMessageDialog(null, "Delete Succsessfull","Success",JOptionPane.INFORMATION_MESSAGE);
                        tableLoad();
                        clear();
                    }
                    else {
                        JOptionPane.showMessageDialog(contentPane,"Cannot DELETE ITEM", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });


    }




    void tableLoad() {
        try {
            pst = con.prepareStatement("select * from customer_order");
            ResultSet rs = pst.executeQuery();
            tblP.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e1) {
            System.out.println("SQL ISSUE");
        }
    }
    void tableserch(String keyword) {
        try {
            pst = con.prepareStatement("select * from customer_order where Order_ID = ?  OR CustomerName = ?");
            pst.setString(1, keyword);
            pst.setString(2, keyword);
            ResultSet rs = pst.executeQuery();
            tblP.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException e1) {
            System.out.println("SQL ISSUE");
        }
    }
    void clear(){
        txtPName.setText("");
        txtPPrice.setText("");
        PcatBox.setSelectedItem("");
        stsBox.setSelectedItem("");
        txtPID.setText("");
        txtSrch.setText("");
        lblError.setText("");
    }
    String ganrateID()
    {
        String id="",genID="";
        int intid=0;
        try{
        pst = con.prepareStatement("select * from customer_order ORDER BY Order_ID DESC LIMIT 1");
        ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                 id = rs.getString(1);
                 intid=Integer.parseInt(id.substring(3));
                 intid++;

            } else {
                intid=1000;

            }
            genID="OID"+intid;
            System.out.println("Last record - ID: " + genID);

    } catch (SQLException e1) {
        System.out.println("SQL ISSUE");
    }
        return genID;
    }




    public static void main(String[] args){

        ManageOrder ui=new ManageOrder();

        ui.setContentPane(ui.contentPane);
        ui.setTitle("Manage Order");
        ui.pack();
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setVisible(true);

    }

}
