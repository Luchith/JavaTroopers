package View;


import View.ManageInventory.ManageInventory;
import View.ManageOrder.ManageOrder;

import View.SaleReport.SaleReportView;
import View.CustomerNotify.cusNotify;
import View.EMPemail.empEmail;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeView extends JFrame {
    public JPanel contentPane;
    private JButton btnManageOrder;
    private JButton btnManageSupplier;
    private JButton btnManagenventory;
    private JButton btnManageEmployee;
    private JButton btnAllocation;
    private JButton btnReport;
    private JButton btnCustomerNotification;
    private JButton btnEmployeeNotification;

    public HomeView() {

        btnManageOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                    ManageOrder v1= new ManageOrder();

                    setVisible(false);
                    v1.setContentPane(v1.contentPane);
                    v1.setTitle("Manage Order");
                    v1.pack();
                    v1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    v1.setVisible(true);

            }
        });
//
//        btnManageSupplier.addActionListener(new ActionListener() {
//            @Override
//
//        });
//
        btnManagenventory.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManageInventory v1= new ManageInventory();

                setVisible(false);
                v1.setContentPane(v1.contentPane);
                v1.setTitle("Manage Inventory");
                v1.pack();
                v1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                v1.setVisible(true);
            }
        });
//
//        btnManageEmployee.addActionListener(new ActionListener() {
//
//        });
//
//        btnAllocation.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//
//            }
//        });
//
        btnReport.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SaleReportView v1= new SaleReportView();
                setVisible(false);
                v1.setContentPane(v1.contentPane);
                v1.setTitle("Monthly Report");
                v1.setSize(600,600);
                v1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                v1.setVisible(true);
            }
        });
//
        btnCustomerNotification.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cusNotify v1=new cusNotify();
                setVisible(false);
                v1.setContentPane(v1.contentPane);
                v1.setTitle("Email to Customer");
                v1.pack();
                v1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                v1.setVisible(true);
            }
        });
//
        btnEmployeeNotification.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                empEmail v1=new empEmail();
                setVisible(false);
                v1.setContentPane(v1.contentPane);
                v1.setTitle("Email to Employee");
                v1.pack();
                v1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                v1.setVisible(true);
            }
        });
//
  }


        public static void main (String[]args){
            HomeView ui = new HomeView();
            ui.setContentPane(ui.contentPane);
            ui.setTitle("Home");
            ui.setSize(600, 600);
            ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            ui.setVisible(true);
        }
    }

