package controller;


import Entity.mail;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Services.ServiceUser;


/**
 * FXML Controller class
 *
 * @author TheMaliik
 */


public class emailChecker implements Initializable {

    @FXML
    private TextField email_check;
    @FXML
    private TextField code;
    @FXML
    private Button valbtn;
    @FXML
    private Button valcodebtn;

    int code2;

    ServiceUser us = new ServiceUser();


    String email_set;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void passwrd_reset(ActionEvent event) throws SQLException {

        if (us.existemail(email_check.getText())==true){
            email_set=email_check.getText();
            Random rand = new Random();
            code2 = rand.nextInt(9000) + 1000;
            mail m = new mail();

            mail.send(email_check.getText(), "Récupération de mot de passe", "Bonjour,\n Votre code de vérification pour la récupération de mot de passe est : "+String.valueOf(code2)+"\n Cordialement,\n" +
                    "[L'équipe de support Culturium]", "guesmimelek928@gmail.com", "mxoq naou nzou fokg");

            email_check.setVisible(false);
            valbtn.setVisible(false);
            code.setVisible(true);
            valcodebtn.setVisible(true);


        }






    }

    @FXML
    private void retour_login(ActionEvent event) throws IOException {



        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();


        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();

    }



    @FXML
    private void verif_code(ActionEvent event) throws IOException {

        if(code2==Integer.parseInt(code.getText())){


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ChangementMdp.fxml"));
            Parent root = loader.load();
            ChangementMDPController controller=loader.getController();
            controller.updateMdp(email_set);

            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("changement de mot de passe");
            stage.setScene(scene);
            stage.show();

        }

        else {


            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("code non correcte");
            alert.setHeaderText(null);
            alert.setContentText("code");
            alert.show();

        }

    }


}
