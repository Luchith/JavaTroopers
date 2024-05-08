package View.ManageEmp;

import Model.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageEmp extends JFrame{
    private JPanel employeesPanel;
    private JButton addEmployeeBtn, updateEmployeeBtn, removeEmployeeBtn, searchBtn;
    private JTextField searchNameField, searchRoleField;
    private JTable employeesTable;
    private DefaultTableModel employeesTableModel;

    private ArrayList<Employee> employees = new ArrayList<>();

    private Connection connection;
    private String jdbcUrl = "jdbc:mysql://localhost:3306/shipshape";
    private String username = "root";
    private String password = "";

    private JSpinner startTimeSpinner;
    private JSpinner endTimeSpinner;

    public ManageEmp() {
        setTitle("Manage Employees");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Initialize database connection
        try {
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected to the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Initialize components
        employeesPanel = new JPanel(new BorderLayout());

        // Initialize employees panel
        initializeEmployeesPanel();

        add(employeesPanel);
        setVisible(true);
    }

    private void initializeEmployeesPanel() {
        // Heading label
        JLabel headingLabel = new JLabel("Manage Employees", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Table model for employees
        employeesTableModel = new DefaultTableModel(new String[]{"Name", "Role", "Start Time", "End Time"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing
            }
        };
        employeesTable = new JTable(employeesTableModel);
        // Center align table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        employeesTable.setDefaultRenderer(Object.class, centerRenderer);
        // Set bold font for table header
        JTableHeader header = employeesTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        // Set row height
        employeesTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(employeesTable);

        // Buttons for adding, updating, and removing employees
        addEmployeeBtn = new JButton("Add Employee");
        addEmployeeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addEmployee();
            }
        });
        updateEmployeeBtn = new JButton("Update Employee");
        updateEmployeeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateEmployee();
            }
        });

        removeEmployeeBtn = new JButton("Remove Employee");
        removeEmployeeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeEmployee();
            }
        });

        searchNameField = new JTextField(15);
        searchRoleField = new JTextField(15);
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchEmployee();
            }
        });

        // Initialize time pickers
        initializeTimePickers();

        // Add components to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addEmployeeBtn);
        buttonPanel.add(updateEmployeeBtn);
        buttonPanel.add(removeEmployeeBtn);

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(searchNameField);
        searchPanel.add(new JLabel("Search by Role:"));
        searchPanel.add(searchRoleField);
        searchPanel.add(searchBtn);

        JPanel timePanel = new JPanel(new GridLayout(2, 2));


        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(timePanel, BorderLayout.SOUTH);

        employeesPanel.add(headingLabel, BorderLayout.NORTH);
        employeesPanel.add(topPanel, BorderLayout.CENTER);
        employeesPanel.add(scrollPane, BorderLayout.SOUTH);

        // Load employees from the database
        loadEmployees();
    }

    private void initializeTimePickers() {
        // Create time spinners
        SpinnerDateModel startModel = new SpinnerDateModel();
        startTimeSpinner = new JSpinner(startModel);
        JSpinner.DateEditor startEditor = new JSpinner.DateEditor(startTimeSpinner, "HH:mm:ss");
        startTimeSpinner.setEditor(startEditor);
        startTimeSpinner.setValue(new java.util.Date()); // Set default value to current time

        SpinnerDateModel endModel = new SpinnerDateModel();
        endTimeSpinner = new JSpinner(endModel);
        JSpinner.DateEditor endEditor = new JSpinner.DateEditor(endTimeSpinner, "HH:mm:ss");
        endTimeSpinner.setEditor(endEditor);
        endTimeSpinner.setValue(new java.util.Date()); // Set default value to current time
    }

    private void loadEmployees() {
        try {
            String sql = "SELECT * FROM employees";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String role = resultSet.getString("role");
                Time startTime = resultSet.getTime("start_time");
                Time endTime = resultSet.getTime("end_time");
                employees.add(new Employee(id, name, role, startTime, endTime));
            }
            updateEmployeesTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addEmployee() {
        // Dialog to input employee details
        JTextField nameField = new JTextField();
        JTextField roleField = new JTextField();

        // Enable time pickers
        startTimeSpinner.setEnabled(true);
        endTimeSpinner.setEnabled(true);

        int result = JOptionPane.showConfirmDialog(null, new Object[]{"Name:", nameField, "Role:", roleField, "Start Time:", startTimeSpinner, "End Time:", endTimeSpinner},
                "Add New Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String role = roleField.getText();
            Time startTime = new Time(((java.util.Date) startTimeSpinner.getValue()).getTime());
            Time endTime = new Time(((java.util.Date) endTimeSpinner.getValue()).getTime());
            employees.add(new Employee(name, role, startTime, endTime));
            insertEmployeeIntoDatabase(name, role, startTime, endTime);
            updateEmployeesTable();
        }

        // Disable time pickers after adding employee
        startTimeSpinner.setEnabled(false);
        endTimeSpinner.setEnabled(false);
    }

    private void insertEmployeeIntoDatabase(String name, String role, Time startTime, Time endTime) {
        try {
            String sql = "INSERT INTO employees (name, role, start_time, end_time) VALUES (?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, role);
            preparedStatement.setTime(3, startTime);
            preparedStatement.setTime(4, endTime);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateEmployee() {
        int selectedRow = employeesTable.getSelectedRow();
        if (selectedRow != -1) {
            Employee selectedEmployee = employees.get(selectedRow);
            JTextField nameField = new JTextField(selectedEmployee.getName());
            JTextField roleField = new JTextField(selectedEmployee.getRole());

            // Set time spinner values
            startTimeSpinner.setValue(selectedEmployee.getStartTime());
            endTimeSpinner.setValue(selectedEmployee.getEndTime());

            int result = JOptionPane.showConfirmDialog(null, new Object[]{"Name:", nameField, "Role:", roleField, "Start Time:", startTimeSpinner, "End Time:", endTimeSpinner},
                    "Update Employee", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String role = roleField.getText();
                Time startTime = new Time(((java.util.Date) startTimeSpinner.getValue()).getTime());
                Time endTime = new Time(((java.util.Date) endTimeSpinner.getValue()).getTime());
                selectedEmployee.setName(name);
                selectedEmployee.setRole(role);
                selectedEmployee.setStartTime(startTime);
                selectedEmployee.setEndTime(endTime);
                updateEmployeeInDatabase(selectedEmployee, name, role, startTime, endTime);
                updateEmployeesTable();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select an employee to update.");
        }
    }

    private void updateEmployeeInDatabase(Employee employee, String newName, String newRole, Time newStartTime, Time newEndTime) {
        try {
            String sql = "UPDATE employees SET name=?, role=?, start_time=?, end_time=? WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newRole);
            preparedStatement.setTime(3, newStartTime);
            preparedStatement.setTime(4, newEndTime);
            preparedStatement.setInt(5, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeEmployee() {
        int selectedRow = employeesTable.getSelectedRow();
        if (selectedRow != -1) {
            Employee selectedEmployee = employees.get(selectedRow);
            employees.remove(selectedRow);
            deleteEmployeeFromDatabase(selectedEmployee);
            updateEmployeesTable();
        } else {
            JOptionPane.showMessageDialog(null, "Please select an employee to remove.");
        }
    }

    private void deleteEmployeeFromDatabase(Employee employee) {
        try {
            String sql = "DELETE FROM employees WHERE id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, employee.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateEmployeesTable() {
        employeesTableModel.setRowCount(0);
        for (Employee employee : employees) {
            employeesTableModel.addRow(new Object[]{employee.getName(), employee.getRole(), employee.getStartTime(), employee.getEndTime()});
        }
    }

    private void searchEmployee() {
        String searchName = searchNameField.getText().trim().toLowerCase();
        String searchRole = searchRoleField.getText().trim().toLowerCase();
        List<Employee> filteredEmployees = employees.stream()
                .filter(emp -> emp.getName().toLowerCase().contains(searchName) &&
                        emp.getRole().toLowerCase().contains(searchRole))
                .collect(Collectors.toList());
        employeesTableModel.setRowCount(0);
        for (Employee employee : filteredEmployees) {
            employeesTableModel.addRow(new Object[]{employee.getName(), employee.getRole(), employee.getStartTime(), employee.getEndTime()});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ManageEmp();
            }
        });
    }
}
