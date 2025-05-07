package controller;

import Utils.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * FXML Controller class
 *
 * @author TheMaliik
 */

public class ChartController {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private Connection connection; // Declare the Connection variable

    public void initialize() {
        // Initialize the bar chart
        xAxis.setLabel("Status");
        yAxis.setLabel("Number of Clients");

        try {
            // Initialize the database connection
            connection = DBConnection.getInstance().getConnection();

            // Query database to get counts
            int approvedCount = getCountFromDatabase(true);
            int blockedCount = getCountFromDatabase(false);
            int totalCount = getTotalUserCount();

            // Create series for approved, blocked, and total clients
            XYChart.Series<String, Number> approvedSeries = new XYChart.Series<>();
            approvedSeries.setName("Approved");
            approvedSeries.getData().add(new XYChart.Data<>("Approved", approvedCount));

            XYChart.Series<String, Number> blockedSeries = new XYChart.Series<>();
            blockedSeries.setName("Blocked");
            blockedSeries.getData().add(new XYChart.Data<>("Blocked", blockedCount));

            XYChart.Series<String, Number> totalSeries = new XYChart.Series<>();
            totalSeries.setName("Total");
            totalSeries.getData().add(new XYChart.Data<>("Total", totalCount));

            // Add series to the bar chart
            barChart.getData().addAll(approvedSeries, blockedSeries, totalSeries);
        } catch (SQLException e) {
            System.out.println("An error occurred while initializing the bar chart: " + e.getMessage());
        }
    }

    private int getCountFromDatabase(boolean isApproved) throws SQLException {
        // Perform database query to get count of clients based on approval status
        String query = "SELECT COUNT(*) FROM user WHERE is_approved = ?";
        int count = 0;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, isApproved);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }
        }

        return count;
    }

    private int getTotalUserCount() throws SQLException {
        // Perform database query to get total count of users
        String query = "SELECT COUNT(*) FROM user";
        int totalCount = 0;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalCount = resultSet.getInt(1);
            }
        }

        return totalCount;
    }

    @FXML
    private void goBackHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListUsers.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
