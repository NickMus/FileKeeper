package сlient.service;

import dao.DatabaseHandler;
import javafx.scene.control.Alert;
import сlient.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Registration {
    public static void registrationNewUser(String login, String password) {
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
    }
}
