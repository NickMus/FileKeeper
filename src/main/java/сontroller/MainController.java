package сontroller;

import сlient.Client;
import сlient.User;
import dao.DatabaseHandler;
import animation.Shake;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginFld;

    @FXML
    private PasswordField passFld;

    @FXML
    private Button enterBtn;

    @FXML
    private Button singUpBtn;

    @FXML
    void initialize() {
        singUpBtn.setOnAction(event -> {

            DatabaseHandler dbHandler = new DatabaseHandler();

            String login = loginFld.getText().trim();
            String password = passFld.getText().trim();

            User user = new User(login, password);
            ResultSet resultSet = dbHandler.getUser(user);
            int count = 0;
            while (true) {
                try {
                    if (!resultSet.next())
                        break;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                count++;
            }
            if (count == 0) {
                dbHandler.singUpUser(user);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Регистриция");
                alert.setHeaderText(null);
                alert.setContentText("Успешно");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Регистриция");
                alert.setHeaderText(null);
                alert.setContentText("Такой пользователь уже зарегистрирован");
                alert.showAndWait();
            }
        });

        enterBtn.setOnAction(event -> {
            String login = loginFld.getText().trim();
            String password = passFld.getText().trim();

            if (!login.equals("") && !password.equals("")) {
                loginUser(login, password);
            }
        });


    }

    private void loginUser(String login, String password) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User(login, password);
        ResultSet resultSet = dbHandler.getUser(user);

        int count = 0;
        while (true) {
            try {
                if (!resultSet.next())
                    break;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            count++;
        }
        if (count >= 1) {
            System.out.println("Client.User is in database");
            openNewScene("/windows/second.fxml");
        } else {
            Shake loginAnim = new Shake(loginFld);
            Shake passAnim = new Shake(passFld);
            loginAnim.playAnim();
            passAnim.playAnim();
            System.out.println("Client.User isn't in database");
        }
    }

    public void openNewScene(String window) {
        enterBtn.getScene().getWindow().hide();

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(window));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("FileKeeper");
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(event -> Client.closeConnection());
    }
}



