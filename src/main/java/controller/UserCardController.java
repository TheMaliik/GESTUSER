package controller;

import Entity.User;
import Services.ServiceUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author TheMaliik
 */
public class UserCardController implements Initializable {

    private int idCu;

    private User user;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nomTextField;

    @FXML
    private TextField prenomTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField roleTextField;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.user = new User();
        LoadData();
    }

    public void LoadData() {
        ServiceUser us = new ServiceUser();
        try {
            User u = us.findById(this.idCu);
            if (u != null) {
                if (u.getNom() != null) {
                    nomTextField.setText(u.getNom());
                }
                if (u.getPrenom() != null) {
                    prenomTextField.setText(u.getPrenom());
                }
                if (u.getEmail() != null) {
                    emailTextField.setText(u.getEmail());
                }
                if (u.getAge() != 0) {
                    ageTextField.setText(String.valueOf(u.getAge()));
                }
                if (u.getRole() != null) {
                    roleTextField.setText(u.getRole());
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public int initData(int id) {
        this.idCu = id;
        this.LoadData();
        System.err.println("ena aaaaaa" + this.idCu);
        return this.idCu;
    }

    @FXML
    private void updateData(ActionEvent event) {
        if (nomTextField.getText().isEmpty() ||
                prenomTextField.getText().isEmpty() ||
                emailTextField.getText().isEmpty() ||
                ageTextField.getText().isEmpty() ||
                roleTextField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all fields!");
            alert.show();
            return;
        }

        try {
            ServiceUser us = new ServiceUser();
            User userToUpdate = us.findById(idCu);

            userToUpdate.setNom(nomTextField.getText());
            userToUpdate.setPrenom(prenomTextField.getText());
            userToUpdate.setEmail(emailTextField.getText());
            userToUpdate.setAge(Integer.parseInt(ageTextField.getText()));
            userToUpdate.setRole(roleTextField.getText());

            us.modifier(userToUpdate);

            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setHeaderText(null);
            successAlert.setContentText("Modification réussie !!");
            successAlert.show();
        } catch (SQLException ex) {
            ex.printStackTrace();
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("An error occurred while updating user data!");
            errorAlert.show();
        } catch (NumberFormatException ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Invalid age format!");
            errorAlert.show();
        } catch (IllegalArgumentException ex) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setHeaderText(null);
            errorAlert.setContentText(ex.getMessage());
            errorAlert.show();
        }
    }

    public void goBackHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientDashboard.fxml"));
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