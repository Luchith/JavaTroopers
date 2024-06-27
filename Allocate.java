package View.Allocate;

import ServiceLayer.EmployeeService;
import ServiceLayer.OrderService;
import ServiceLayer.AllocationService;
import View.HomeView;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;
import java.awt.*;

public class Allocate extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel lblEmployee;
    private JComboBox<String> comboEmployee;
    private JLabel lblOrder;
    private JComboBox<String> comboOrder;
    private JButton homeButton;

    public Allocate() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        homeButton.addActionListener(new ActionListener() {
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
        setPreferredSize(new Dimension(600, 400));

        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });


        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        initEmployeeComboBox();
        initOrderComboBox();
        setTitle("Allocate Order");
        pack();
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initEmployeeComboBox() {
        EmployeeService employeeService = new EmployeeService();
        List<String> employees = employeeService.getAllEmployees();
        for (String employee : employees) {
            comboEmployee.addItem(employee);
        }
    }

    private void initOrderComboBox(){
        OrderService orderService = new OrderService();
        List<String> orders = orderService.getAllOrders();
        for (String order : orders) {
            comboOrder.addItem(order);
        }
    }

    public JPanel getContentPane() {
        return contentPane;
    }

    private AllocationService allocationService = new AllocationService();
    private void onOK() {
        String selectedEmployee = (String) comboEmployee.getSelectedItem();
        String selectedOrder = (String) comboOrder.getSelectedItem();

        if (selectedEmployee != null && selectedOrder != null) {
            boolean allocationResult = allocationService.assignOrder(selectedOrder, selectedEmployee);
            if (allocationResult) {
                JOptionPane.showMessageDialog(this, "Job order allocated successfully to " + selectedEmployee);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to allocate Job order", "Allocation Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "You must select both an job order and an employee.", "Selection Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void onCancel() {

        dispose();
    }

    public static void main(String[] args) {
        Allocate dialog = new Allocate();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }

    private void createUIComponents() {
    }
}
