package controller;

import Entity.User;
import Services.ServiceUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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
    @FXML
    private Button updateButton;
    @FXML
    private VBox userInfoVBox;
    @FXML
    private VBox updateFormVBox;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField ageField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private User loggedInUser;
    private ServiceUser userService = new ServiceUser();

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
            ageLabel.setText("Âge: " + String.valueOf(loggedInUser.getAge()));
            roleLabel.setText("Rôle: " + loggedInUser.getRole());
            // Ensure user info is visible and update form is hidden
            userInfoVBox.setVisible(true);
            userInfoVBox.setManaged(true);
            updateFormVBox.setVisible(false);
            updateFormVBox.setManaged(false);
        } else {
            // Handle case where no user is logged in
            welcomeLabel.setText("Erreur: Aucun utilisateur connecté.");
            nomLabel.setText("Nom: N/A");
            prenomLabel.setText("Prénom: N/A");
            emailLabel.setText("Email: N/A");
            ageLabel.setText("Âge: N/A");
            roleLabel.setText("Rôle: N/A");
            // Ensure user info is visible and update form is hidden
            userInfoVBox.setVisible(true);
            userInfoVBox.setManaged(true);
            updateFormVBox.setVisible(false);
            updateFormVBox.setManaged(false);
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

    @FXML
    private void handleUpdate() {
        if (loggedInUser != null) {
            // Populate update form with current user data
            nomField.setText(loggedInUser.getNom());
            prenomField.setText(loggedInUser.getPrenom());
            emailField.setText(loggedInUser.getEmail());
            ageField.setText(String.valueOf(loggedInUser.getAge()));
            // Toggle visibility
            userInfoVBox.setVisible(false);
            userInfoVBox.setManaged(false);
            updateFormVBox.setVisible(true);
            updateFormVBox.setManaged(true);
        } else {
            welcomeLabel.setText("Erreur: Aucun utilisateur à modifier.");
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Validate inputs
            String nom = nomField.getText().trim();
            String prenom = prenomField.getText().trim();
            String email = emailField.getText().trim();
            String ageText = ageField.getText().trim();

            if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || ageText.isEmpty()) {
                welcomeLabel.setText("Erreur: Tous les champs sont requis.");
                return;
            }

            int age = Integer.parseInt(ageText);

            // Update user object
            loggedInUser.setNom(nom);
            loggedInUser.setPrenom(prenom);
            loggedInUser.setEmail(email);
            loggedInUser.setAge(age);

            // Persist changes to database
            userService.modifier(loggedInUser);

            // Toggle visibility back to user info and refresh dashboard
            userInfoVBox.setVisible(true);
            userInfoVBox.setManaged(true);
            updateFormVBox.setVisible(false);
            updateFormVBox.setManaged(false);
            initializeDashboard();
        } catch (NumberFormatException e) {
            // Handle invalid age input
            ageField.setText("Âge invalide");
        } catch (Exception e) {
            e.printStackTrace();
            welcomeLabel.setText("Erreur lors de la mise à jour.");
        }
    }

    @FXML
    private void handleCancel() {
        // Toggle visibility back to user info
        userInfoVBox.setVisible(true);
        userInfoVBox.setManaged(true);
        updateFormVBox.setVisible(false);
        updateFormVBox.setManaged(false);
    }
}