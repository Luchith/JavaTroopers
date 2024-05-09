package View.Supplier;

import Model.Supplier;

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

public class SupplierUI extends JFrame {
    private JPanel suppliersPanel;
    private JButton addSupplierBtn, updateSupplierBtn, removeSupplierBtn, searchBtn;
    private JTextField searchNameField, searchItemField;
    private JTable suppliersTable;
    private DefaultTableModel suppliersTableModel;

    private ArrayList<Supplier> suppliers = new ArrayList<>();

    private Connection connection;
    private String jdbcUrl = "jdbc:mysql://localhost:3306/shipshape";
    private String username = "root";
    private String password = "";

    public SupplierUI() {
        setTitle("ShipShape Management System");
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
        suppliersPanel = new JPanel(new BorderLayout());

        // Initialize suppliers panel
        initializeSuppliersPanel();

        add(suppliersPanel);
        setVisible(true);
    }

    private void initializeSuppliersPanel() {
        // Heading label
        JLabel headingLabel = new JLabel("Manage Suppliers", SwingConstants.CENTER);
        headingLabel.setFont(new Font("Arial", Font.BOLD, 20));

        // Table model for suppliers
        suppliersTableModel = new DefaultTableModel(new String[]{"Supplier Name", "Contact Information", "Supply Items"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Disable editing
            }
        };
        suppliersTable = new JTable(suppliersTableModel);
        // Center align table content
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        suppliersTable.setDefaultRenderer(Object.class, centerRenderer);
        // Set bold font for table header
        JTableHeader header = suppliersTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        // Set row height
        suppliersTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(suppliersTable);

        // Buttons for adding, updating, and removing suppliers
        addSupplierBtn = new JButton("Add Supplier");
        addSupplierBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSupplier();
            }
        });

        updateSupplierBtn = new JButton("Update Supplier");
        updateSupplierBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSupplier();
            }
        });

        removeSupplierBtn = new JButton("Remove Supplier");
        removeSupplierBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSupplier();
            }
        });

        searchNameField = new JTextField(15);
        searchItemField = new JTextField(15);
        searchBtn = new JButton("Search");
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchSupplier();
            }
        });

        // Add components to the panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addSupplierBtn);
        buttonPanel.add(updateSupplierBtn);
        buttonPanel.add(removeSupplierBtn);

        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search by Name:"));
        searchPanel.add(searchNameField);
        searchPanel.add(new JLabel("Search by Supply Item:"));
        searchPanel.add(searchItemField);
        searchPanel.add(searchBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(buttonPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.SOUTH);

        suppliersPanel.add(headingLabel, BorderLayout.NORTH);
        suppliersPanel.add(topPanel, BorderLayout.CENTER);
        suppliersPanel.add(scrollPane, BorderLayout.SOUTH);

        // Load suppliers from the database
        loadSuppliers();
    }

    private void loadSuppliers() {
        try {
            String sql = "SELECT * FROM suppliers";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String contactInfo = resultSet.getString("contact_info");
                String supplyItems = resultSet.getString("supply_items");
                // Split supply items by comma
                String[] itemsArray = supplyItems.split(",");
                ArrayList<String> supplyItemsList = new ArrayList<>();
                for (String item : itemsArray) {
                    supplyItemsList.add(item.trim());
                }
                suppliers.add(new Supplier(name, contactInfo, supplyItemsList));
            }
            updateSuppliersTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addSupplier() {
        // Dialog to input supplier details
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField supplyItemsField = new JTextField();
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Supplier Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Contact Information:"));
        inputPanel.add(contactField);
        inputPanel.add(new JLabel("Supply Items (comma-separated):"));
        inputPanel.add(supplyItemsField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel,
                "Add New Supplier", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText();
            String contactInfo = contactField.getText();
            String supplyItemsInput = supplyItemsField.getText();
            // Split supply items by comma
            String[] itemsArray = supplyItemsInput.split(",");
            ArrayList<String> supplyItemsList = new ArrayList<>();
            for (String item : itemsArray) {
                supplyItemsList.add(item.trim());
            }
            suppliers.add(new Supplier(name, contactInfo, supplyItemsList));
            insertSupplierIntoDatabase(name, contactInfo, supplyItemsList);
            updateSuppliersTable();
        }
    }

    private void insertSupplierIntoDatabase(String name, String contactInfo, ArrayList<String> supplyItems) {
        try {
            String sql = "INSERT INTO suppliers (name, contact_info, supply_items) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, contactInfo);
            String supplyItemsString = String.join(",", supplyItems);
            preparedStatement.setString(3, supplyItemsString);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSupplier() {
        int selectedRow = suppliersTable.getSelectedRow();
        if (selectedRow != -1) {
            Supplier selectedSupplier = suppliers.get(selectedRow);
            JTextField nameField = new JTextField(selectedSupplier.getName());
            JTextField contactField = new JTextField(selectedSupplier.getContactInfo());
            JTextField supplyItemsField = new JTextField(String.join(",", selectedSupplier.getSupplyItems()));
            JPanel inputPanel = new JPanel(new GridLayout(3, 2));
            inputPanel.add(new JLabel("Supplier Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Contact Information:"));
            inputPanel.add(contactField);
            inputPanel.add(new JLabel("Supply Items (comma-separated):"));
            inputPanel.add(supplyItemsField);

            int result = JOptionPane.showConfirmDialog(null, inputPanel,
                    "Update Supplier", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText();
                String contactInfo = contactField.getText();
                String supplyItemsInput = supplyItemsField.getText();
                String[] itemsArray = supplyItemsInput.split(",");
                ArrayList<String> supplyItemsList = new ArrayList<>();
                for (String item : itemsArray) {
                    supplyItemsList.add(item.trim());
                }
                selectedSupplier.setName(name);
                selectedSupplier.setContactInfo(contactInfo);
                selectedSupplier.setSupplyItems(supplyItemsList);
                updateSupplierInDatabase(selectedSupplier, name, contactInfo, supplyItemsList);
                updateSuppliersTable();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a supplier to update.");
        }
    }

    private void updateSupplierInDatabase(Supplier supplier, String newName, String newContactInfo, ArrayList<String> newSupplyItems) {
        try {
            String sql = "UPDATE suppliers SET name=?, contact_info=?, supply_items=? WHERE name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newContactInfo);
            String supplyItemsString = String.join(",", newSupplyItems);
            preparedStatement.setString(3, supplyItemsString);
            preparedStatement.setString(4, supplier.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeSupplier() {
        int selectedRow = suppliersTable.getSelectedRow();
        if (selectedRow != -1) {
            Supplier selectedSupplier = suppliers.get(selectedRow);
            suppliers.remove(selectedRow);
            deleteSupplierFromDatabase(selectedSupplier);
            updateSuppliersTable();
        } else {
            JOptionPane.showMessageDialog(null, "Please select a supplier to remove.");
        }
    }

    private void deleteSupplierFromDatabase(Supplier supplier) {
        try {
            String sql = "DELETE FROM suppliers WHERE name=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, supplier.getName());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateSuppliersTable() {
        suppliersTableModel.setRowCount(0);
        for (Supplier supplier : suppliers) {
            suppliersTableModel.addRow(new Object[]{supplier.getName(), supplier.getContactInfo(), String.join(", ", supplier.getSupplyItems())});
        }
    }

    private void searchSupplier() {
        String searchName = searchNameField.getText().trim().toLowerCase();
        String searchItem = searchItemField.getText().trim().toLowerCase();
        List<Supplier> filteredSuppliers = suppliers.stream()
                .filter(supplier -> supplier.getName().toLowerCase().contains(searchName) &&
                        supplier.getSupplyItems().stream().anyMatch(item -> item.toLowerCase().contains(searchItem)))
                .collect(Collectors.toList());
        suppliersTableModel.setRowCount(0);
        for (Supplier supplier : filteredSuppliers) {
            suppliersTableModel.addRow(new Object[]{supplier.getName(), supplier.getContactInfo(), String.join(", ", supplier.getSupplyItems())});
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SupplierUI();
            }
        });
    }
}
