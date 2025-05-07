package controller;

import Entity.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class OrganisateurController {

    @FXML
    private Label welcomeLabel;
    @FXML
    private Label nomLabel;
    @FXML
    private Label prenomLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Button logoutButton;

    private User loggedInUser;

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
            // Populate user information
            welcomeLabel.setText("Bienvenue, " + loggedInUser.getPrenom() + " " + loggedInUser.getNom());
            nomLabel.setText("Nom: " + loggedInUser.getNom());
            prenomLabel.setText("Prénom: " + loggedInUser.getPrenom());
            emailLabel.setText("Email: " + loggedInUser.getEmail());
            ageLabel.setText("Âge: " + loggedInUser.getAge());
            roleLabel.setText("Rôle: " + loggedInUser.getRole());
        } else {
            // Handle case where no user is logged in
            welcomeLabel.setText("Erreur: Aucun utilisateur connecté.");
            nomLabel.setText("Nom: N/A");
            prenomLabel.setText("Prénom: N/A");
            emailLabel.setText("Email: N/A");
            ageLabel.setText("Âge: N/A");
            roleLabel.setText("Rôle: N/A");
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