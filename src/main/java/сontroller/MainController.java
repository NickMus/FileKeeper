package сontroller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.Sceneable;
import сlient.service.LoginUser;
import сlient.service.Registration;

import java.io.IOException;

public class MainController {

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
            String login = loginFld.getText().trim();
            String password = passFld.getText().trim();
            Registration.registrationNewUser(login, password);
        });

        enterBtn.setOnAction(event -> {
            String login = loginFld.getText().trim();
            String password = passFld.getText().trim();
            if (!login.equals("") && !password.equals("")) {
                LoginUser loginUser = new LoginUser();
                loginUser.loginUser(login, password);
            }
        });
    }

}



