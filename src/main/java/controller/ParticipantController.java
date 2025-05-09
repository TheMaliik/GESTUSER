package controller;

import Entity.User;
import Services.ServiceUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;

public class ParticipantController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField roleField;
    @FXML
    private Button updateButton;
    @FXML
    private Button logoutButton;

    private User loggedInUser;
    private ServiceUser serviceUser; // Service for database operations

    // Constructor to initialize ServiceUser
    public ParticipantController() {
        this.serviceUser = new ServiceUser();
    }

    // Setter to receive the logged-in user from LoginController
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        initializeDashboard();
    }

    @FXML
    private void initialize() {
        // Try to use UserConnected as a fallback if setLoggedInUser hasn't been called
        if (loggedInUser == null) {
            loggedInUser = LoginController.UserConnected;
        }
        initializeDashboard();
    }

    private void initializeDashboard() {
        if (loggedInUser != null) {
            // Populate user information in text fields
            welcomeLabel.setText("Bienvenue, " + loggedInUser.getPrenom() + " " + loggedInUser.getNom());
            nomField.setText(loggedInUser.getNom());
            prenomField.setText(loggedInUser.getPrenom());
            emailField.setText(loggedInUser.getEmail());
            ageField.setText(String.valueOf(loggedInUser.getAge()));
            roleField.setText(loggedInUser.getRole());
        } else {
            // Handle case where no user is logged in
            welcomeLabel.setText("Erreur: Aucun utilisateur connecté.");
            nomField.setText("N/A");
            prenomField.setText("N/A");
            emailField.setText("N/A");
            ageField.setText("N/A");
            roleField.setText("N/A");
        }
    }

    @FXML
    private void handleUpdate() {
        if (loggedInUser != null) {
            try {
                // Update user information in memory
                String newNom = nomField.getText();
                String newPrenom = prenomField.getText();
                String newEmail = emailField.getText();
                int newAge = Integer.parseInt(ageField.getText());
                String newRole = roleField.getText();

                // Update the User object
                loggedInUser.setNom(newNom);
                loggedInUser.setPrenom(newPrenom);
                loggedInUser.setEmail(newEmail);
                loggedInUser.setAge(newAge);
                loggedInUser.setRole(newRole);
                // Note: motDePasse remains unchanged since no field is provided in FXML

                // Persist changes to the database using ServiceUser
                serviceUser.modifier(loggedInUser);

                // Update welcome label with success message
                welcomeLabel.setText("Mise à jour réussie, " + newPrenom);

            } catch (NumberFormatException e) {
                welcomeLabel.setText("Erreur: Âge doit être un nombre.");
            } catch (SQLException e) {
                welcomeLabel.setText("Erreur: Problème de connexion à la base de données.");
                e.printStackTrace();
            } catch (Exception e) {
                welcomeLabel.setText("Erreur lors de la mise à jour.");
                e.printStackTrace();
            }
        } else {
            welcomeLabel.setText("Erreur: Aucun utilisateur connecté.");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            // Clear the logged-in user
            loggedInUser = null;
            LoginController.UserConnected = null;

            // Load the login FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Parent root = loader.load();

            // Get the current stage
            Stage stage = (Stage) logoutButton.getScene().getWindow();

            // Set the new scene
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            welcomeLabel.setText("Erreur lors de la déconnexion.");
        }
    }
}