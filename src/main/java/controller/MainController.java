package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {


    @FXML
    private AnchorPane AnchorBut;

    @FXML
    private AnchorPane Anchorimg;

    @FXML
    private Button AddPost_but;

    @FXML
    private Button Addcom_but;
    @FXML
    private Button ManCom_but;

    @FXML
    private Button ManPost_but;

    @FXML
    private AnchorPane anchorMain;

    @FXML
    private ImageView img1;

    @FXML
    private ImageView img2;

    @FXML
    private SplitPane splitP;

    /*
    @FXML
    void AddPost_But(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/AjouterPost.fxml"));
        primaryStage.setTitle("add post");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @FXML
    void AddCom_But(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/AjoutCom.fxml"));
        primaryStage.setTitle("add Com");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

*/


    @FXML
    void ManCom_But(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/GestionCom.fxml"));
        primaryStage.setTitle("Gest Com");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    @FXML
    void ManPost_But(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/GestionPost.fxml"));
        primaryStage.setTitle("Gest post");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    @FXML
    void initialize() {

    }


@FXML
    public void goBackHandler(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDashboard.fxml"));
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

