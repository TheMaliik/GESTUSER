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
public class AdminUserCardController implements Initializable {

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
                if (u.getRole() != null) {
                    roleTextField.setText(u.getRole());
                }
                ageTextField.setText(String.valueOf(u.getAge()));
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