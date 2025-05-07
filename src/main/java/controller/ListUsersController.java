package controller;

import Entity.User;
import Services.ServiceUser;
import Utils.DBConnection;
import controller.AdminUserCardController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author TheMaliik
 */
public class ListUsersController implements Initializable {

    @FXML
    private VBox usersVBox;

    @FXML
    private TextField search;

    ObservableList<User> obslistus = FXCollections.observableArrayList();
    private ServiceUser userS = new ServiceUser(); // Moved to class level

    Connection connection = DBConnection.getInstance().getConnection();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            listUsers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void listUsers() throws SQLException {
        obslistus.clear();
        obslistus.addAll(userS.afficher());
        updateUserCards(obslistus);

        search.textProperty().addListener((obs, oldText, newText) -> {
            try {
                List<User> searchResults = userS.Search(newText);
                updateUserCards(searchResults);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void updateUserCards(List<User> users) {
        usersVBox.getChildren().clear();
        for (User user : users) {
            HBox card = new HBox();
            card.setSpacing(10);
            card.setStyle("-fx-padding: 10; -fx-border-color: grey; -fx-border-width: 1; -fx-background-color: white;");

            Label nomLabel = new Label(user.getNom());
            nomLabel.setPrefWidth(150);
            Label prenomLabel = new Label(user.getPrenom());
            prenomLabel.setPrefWidth(150);
            Label emailLabel = new Label(user.getEmail());
            emailLabel.setPrefWidth(200);
            Label ageLabel = new Label(String.valueOf(user.getAge()));
            ageLabel.setPrefWidth(80);
            Label roleLabel = new Label(user.getRole());
            roleLabel.setPrefWidth(100);

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> {
                obslistus.remove(user);
                try {
                    userS.supprimer(user);
                    listUsers(); // Refresh the view
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            Button getByIdButton = new Button("Get by ID");
            getByIdButton.setOnAction(event -> {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminUserCard.fxml"));
                    Parent root = loader.load();
                    AdminUserCardController controller = loader.getController();
                    controller.initData(user.getId());
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(ListUsersController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            HBox buttonBox = new HBox(5, deleteButton, getByIdButton);
            card.getChildren().addAll(nomLabel, prenomLabel, emailLabel, ageLabel, roleLabel, buttonBox);
            usersVBox.getChildren().add(card);
        }
    }

    @FXML
    private void loadStat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Charts.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    private void TrieEmail() throws SQLException {
        List<User> sortedUsers = userS.triEmail();
        updateUserCards(sortedUsers);
    }

    @FXML
    private void triNom() throws SQLException {
        List<User> sortedUsers = userS.triNom();
        updateUserCards(sortedUsers);
    }

    @FXML
    private void goBackHandler(ActionEvent event) {
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