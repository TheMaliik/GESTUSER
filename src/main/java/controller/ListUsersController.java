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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
    private ServiceUser userS = new ServiceUser();
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
        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(20));
        grid.setAlignment(Pos.TOP_CENTER);

        int columnCount = 3; // Number of cards per row
        int row = 0;
        int col = 0;

        for (User user : users) {
            // Create square card
            VBox card = new VBox();
            card.setPrefSize(250, 250);
            card.setSpacing(10);
            card.setAlignment(Pos.CENTER);
            card.setStyle("-fx-padding: 15; -fx-border-color: grey; -fx-border-width: 1; -fx-background-color: white; -fx-border-radius: 10; -fx-background-radius: 10;");

            // User details with black text
            Label nomLabel = new Label("Name: " + user.getNom());
            nomLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: black;");
            Label prenomLabel = new Label("Surname: " + user.getPrenom());
            prenomLabel.setStyle("-fx-text-fill: black;");
            Label emailLabel = new Label("Email: " + user.getEmail());
            emailLabel.setStyle("-fx-text-fill: black;");
            emailLabel.setWrapText(true);
            Label ageLabel = new Label("Age: " + user.getAge());
            ageLabel.setStyle("-fx-text-fill: black;");
            Label roleLabel = new Label("Role: " + user.getRole());
            roleLabel.setStyle("-fx-text-fill: black;");

            // Buttons with clear text
            Button deleteButton = new Button("Delete");
            deleteButton.setStyle("-fx-background-color: #ff4d4d; -fx-text-fill: white; -fx-font-weight: bold;");
            deleteButton.setOnAction(event -> {
                obslistus.remove(user);
                try {
                    userS.supprimer(user);
                    listUsers();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });

            Button getByIdButton = new Button("View Details");
            getByIdButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
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

            HBox buttonBox = new HBox(10, deleteButton, getByIdButton);
            buttonBox.setAlignment(Pos.CENTER);

            card.getChildren().addAll(nomLabel, prenomLabel, emailLabel, ageLabel, roleLabel, buttonBox);
            grid.add(card, col, row);

            col++;
            if (col >= columnCount) {
                col = 0;
                row++;
            }
        }

        usersVBox.getChildren().add(grid);
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