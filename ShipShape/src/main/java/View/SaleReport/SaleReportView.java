package View.SaleReport;

import ServiceLayer.SaleReportService;
import View.HomeView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SaleReportView extends JFrame{
    public JPanel contentPane;
    private JTextPane txtPaneTotOrders;
    private JTextPane txtPanetotalSuppliers;
    private JTextPane txtPanetotalEmployees;
    private JTextPane txtPanetotalItems;
    private JTextPane totRapairOrder;
    private JTextPane totSPorders;
    private JTextPane totPaintOrder;
    private JButton btnGENERATE;
    private JButton btnHOME;
    private JLabel lbltotOrders;
    private JLabel lblTotSuppliers;
    private JLabel lblTotEmployees;
    private JLabel lblTotItems;
    private JLabel lblTotArtworks;
    private JLabel lblTotCampaigns;
    private JLabel lblTotBanners;
    private JLabel lblTitle;


    SaleReportService reportService = new SaleReportService();

    private void displayTotalOrders() {
        int totalOrders = reportService.CalTotOrders();
        String totalOrdersText = String.valueOf(totalOrders);
        txtPaneTotOrders.setText(totalOrdersText);
    }

    private void displayTotalSuppliers() {
        int totalSuppliers = reportService.CalTotSuppliers();
        String totalSuppliersText = String.valueOf(totalSuppliers);
        txtPanetotalSuppliers.setText(totalSuppliersText);
    }

    private void displayTotalEmployees() {
        int totalEmployees = reportService.CalTotEmployees();
        String totalEmployeesText = String.valueOf(totalEmployees);
        txtPanetotalEmployees.setText(totalEmployeesText);
    }

    private void displayTotalItems() {
        int totalItems = reportService.CalTotItems();
        String totalItemsText = String.valueOf(totalItems);
        txtPanetotalItems.setText(totalItemsText);
    }

    private void displayTotalRapairOrders() {

        int totalRapirOrders = reportService.CalTotRapirOrders();
        String totalDAOrdersText = String.valueOf(totalRapirOrders);
        totRapairOrder.setText(totalDAOrdersText);
    }

    private void displayTotalSpOrders() {

        int totalSPOrders = reportService.CalTotSPOrders();
        String totalCampaignOrdersText = String.valueOf(totalSPOrders);
        totSPorders.setText(totalCampaignOrdersText);
    }

    private void displayTotalPaintOrders() {
        int CalTotPaintOrder = reportService.CalTotPaintOrders();
        String totalBannerOrdersText = String.valueOf(CalTotPaintOrder);
        totPaintOrder.setText(totalBannerOrdersText);
    }

    public SaleReportView() {
        btnGENERATE.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTotalOrders();
                displayTotalSuppliers();
                displayTotalEmployees();
                displayTotalItems();
                displayTotalRapairOrders();
                displayTotalPaintOrders();
                displayTotalSpOrders();

            }
        });
        btnHOME.addActionListener(new ActionListener() {
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
    }


    public static void main(String[] args){

        SaleReportView ui=new SaleReportView();
        ui.setContentPane(ui.contentPane);
        ui.setTitle("Manage Sale Reports");
        ui.setSize(500,550);
        ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ui.setVisible(true);
    }

}

