package controller;

import Entity.SmsSender;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author TheMaliik
 */
public class SignUpController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btn;

    @FXML
    private TextField nomField;

    @FXML
    private TextField prenomField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField ageField;

    @FXML
    private TextField motDePasseField;

    @FXML
    private TextField confMotDePasse;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    void addUser(ActionEvent event) {
    }

    @FXML
    void initialize() {
        roleComboBox.getItems().addAll("admin", "participant", "organisateur");
        roleComboBox.setPromptText("Select Role");
    }

    ServiceUser ServUser = new ServiceUser();

    @FXML
    private void add() throws SQLException, IOException {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String ageText = ageField.getText();
        String motDePasse = motDePasseField.getText();
        String confirmMotDePasse = confMotDePasse.getText();
        String role = roleComboBox.getValue();

        // Check if any field is empty
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || ageText.isEmpty() || motDePasse.isEmpty() || confirmMotDePasse.isEmpty() || role == null) {
            showAlert("Please fill in all the fields.");
            return;
        }

        // Validate age
        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0) {
                showAlert("Age must be a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid age format!");
            return;
        }

        // Check password complexity
        int complexityScore = evaluatePasswordComplexity(motDePasse);
        updateComplexityMessage(complexityScore);

        if (complexityScore <= 1) {
            showAlert("The password is too weak. Please choose a stronger password.");
            return;
        }

        // Check email format
        if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")) {
            showAlert("Invalid email format!");
            return;
        }

        // Check if the email is already registered
        if (ServUser.existemail(email)) {
            showAlert("This email is already registered!");
            return;
        }

        // Check if passwords match
        if (!motDePasse.equals(confirmMotDePasse)) {
            showAlert("Passwords do not match!");
            return;
        }

        // Add the user to the database
        try {
            User client = new User();
            client.setNom(nom);
            client.setPrenom(prenom);
            client.setEmail(email);
            client.setMotDePasse(motDePasse);
            client.setAge(age);
            client.setRole(role);
            ServUser.ajouter(client);

            // Send SMS notification
            SmsSender.sendSMS("+1234567890", "Bienvenue " + nom + " " + prenom + "! Votre inscription est réussie.\nEmail: " + email + "\nPassword: " + motDePasse + "\nRole: " + role);

            showAlert("Welcome! Registration successful.");
        } catch (SQLException ex) {
            System.out.println("Error: " + ex.getMessage());
            showAlert("An error occurred during registration.");
        }
    }

    @FXML
    private void generatePassword() {
        String generatedPassword = RandomStringUtils.randomAlphanumeric(10);
        motDePasseField.setText(generatedPassword);
    }

    private void updateComplexityMessage(int complexityScore) {
        String message;
        if (complexityScore <= 1) {
            message = "Weak";
        } else if (complexityScore <= 3) {
            message = "Moderate";
        } else if (complexityScore <= 5) {
            message = "Strong";
        } else {
            message = "Very Strong";
        }
        // complexityLabel.setText("Password Complexity: " + message);
    }

    private int evaluatePasswordComplexity(String password) {
        int score = 0;

        if (password.length() >= 8) {
            score++;
        }

        if (password.matches(".*[a-z].*") && password.matches(".*[A-Z].*")) {
            score++;
        }

        if (password.matches(".*\\d.*")) {
            score++;
        }

        if (password.matches(".*[^a-zA-Z0-9].*")) {
            score++;
        }

        return score;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void cancel(ActionEvent event) throws IOException {
        nomField.setText("");
        prenomField.setText("");
        emailField.setText("");
        ageField.setText("");
        motDePasseField.setText("");
        confMotDePasse.setText("");
        roleComboBox.setValue(null);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage adminStage = new Stage();
        adminStage.setScene(scene);
        adminStage.show();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setEmailField(TextField emailField) {
        this.emailField = emailField;
    }
}