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

public class ChartController {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    private Connection connection;

    public void initialize() {
        // Initialize the bar chart
        xAxis.setLabel("Category");
        yAxis.setLabel("Count");

        try {
            // Initialize the database connection
            connection = DBConnection.getInstance().getConnection();

            // Query database to get counts
            int participantCount = getRoleCountFromDatabase("Participant");
            int organisateurCount = getRoleCountFromDatabase("Organisateur");
            int totalCount = getTotalUserCount();

            // Create series for Participant, Organisateur, and Total
            XYChart.Series<String, Number> participantSeries = new XYChart.Series<>();
            participantSeries.setName("Participants");
            participantSeries.getData().add(new XYChart.Data<>("Participants", participantCount));

            XYChart.Series<String, Number> organisateurSeries = new XYChart.Series<>();
            organisateurSeries.setName("Organisateurs");
            organisateurSeries.getData().add(new XYChart.Data<>("Organisateurs", organisateurCount));

            XYChart.Series<String, Number> totalSeries = new XYChart.Series<>();
            totalSeries.setName("Total Users");
            totalSeries.getData().add(new XYChart.Data<>("Total", totalCount));

            // Add series to the bar chart
            barChart.getData().addAll(participantSeries, organisateurSeries, totalSeries);
        } catch (SQLException e) {
            System.out.println("An error occurred while initializing the bar chart: " + e.getMessage());
        }
    }

    private int getRoleCountFromDatabase(String role) throws SQLException {
        // Perform database query to get count of users based on role
        String query = "SELECT COUNT(*) FROM user WHERE role = ?";
        int count = 0;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, role);
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