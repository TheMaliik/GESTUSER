package controller;

import Entity.User;
import Services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * FXML Controller class
 *
 * @author TheMaliik
 */
public class LoginController {

    @FXML
    private Button login_btn;

    @FXML
    private TextField emailF;

    @FXML
    private PasswordField mdp;

    public static User UserConnected;

    @FXML
    private TextField visiblePassword;

    @FXML
    private ImageView eyeIcon;

    private boolean passwordVisible = false;

    ServiceUser us = new ServiceUser();

    @FXML
    private void Login(ActionEvent event) {
        String email = emailF.getText();
        String password = mdp.getText();

        // Log input values for debugging
        System.out.println("Login attempt: email='" + email + "', password='" + password + "'");

        // Validate inputs
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Veuillez remplir tous les champs.");
            return;
        }

        try {
            // Create a User object for login
            User user = new User(email.trim(), password.trim());
            String loginMessage = ServiceUser.login(user);

            if (loginMessage.isEmpty()) {
                // Login successful, find user by ID to get role
                User loggedInUser = us.findById(ServiceUser.idUser);
                if (loggedInUser == null || loggedInUser.getRole() == null) {
                    showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Utilisateur ou rôle non trouvé.");
                    return;
                }
                String role = loggedInUser.getRole();
                String fxmlFile;

                // Log successful login
                System.out.println("Login successful: userId=" + ServiceUser.idUser + ", role=" + role);

                // Determine the dashboard based on role
                FXMLLoader loader;
                Parent root;
                Scene scene;
                Stage stage;
                switch (role) {
                    case "admin":
                        fxmlFile = "/AdminDashboard.fxml";
                        loader = new FXMLLoader(getClass().getResource(fxmlFile));
                        root = loader.load();
                        scene = new Scene(root);
                        stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                        break;
                    case "participant":
                        fxmlFile = "/ParticipantDashboard.fxml";
                        loader = new FXMLLoader(getClass().getResource(fxmlFile));
                        root = loader.load();
                        ParticipantController participantController = loader.getController();
                        participantController.setLoggedInUser(loggedInUser);
                        scene = new Scene(root);
                        stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                        break;
                    case "organisateur":
                        fxmlFile = "/OrganisateurDashboard.fxml";
                        loader = new FXMLLoader(getClass().getResource(fxmlFile));
                        root = loader.load();
                        OrganisateurController organisateurController = loader.getController();
                        organisateurController.setLoggedInUser(loggedInUser);
                        scene = new Scene(root);
                        stage = new Stage();
                        stage.setScene(scene);
                        stage.show();
                        break;
                    default:
                        showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Rôle utilisateur invalide: " + role);
                        return;
                }

                // Close the login stage
                Stage loginStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                loginStage.close();

                // Set the current user
                UserConnected = loggedInUser;
                showAlert(Alert.AlertType.INFORMATION, "Connexion réussie", "Bienvenue, " + loggedInUser.getNom() + " " + loggedInUser.getPrenom() + "!");
            } else {
                // Show login error
                showAlert(Alert.AlertType.ERROR, "Erreur de connexion", loginMessage);
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Une erreur s'est produite: " + e.getMessage());
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de connexion", "Erreur lors du chargement de l'interface: " + e.getMessage());
        }
    }





    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void SignUp(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignUpController.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("SignUp");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void ForgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ResetPassword.fxml"));
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