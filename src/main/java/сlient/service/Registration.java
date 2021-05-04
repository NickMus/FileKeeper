package сlient.service;

import dao.DatabaseHandler;
import javafx.scene.control.Alert;
import сlient.User;
import сonst.Configs;
import сonst.Const;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Registration {
    public static void registrationNewUser(String login, String password) throws SQLException, ClassNotFoundException {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User(login, password);
        createTableUser();
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

    public static void createTableUser() throws SQLException, ClassNotFoundException {
        String create = "CREATE TABLE IF NOT EXISTS " + Configs.dbName + "." + Const.USER_TABLE + " (" +
                "iduser INT NOT NULL AUTO_INCREMENT, " +
                "user_login VARCHAR(45) NOT NULL, " +
                "user_pass VARCHAR(45) NOT NULL, " +
                "PRIMARY KEY (iduser))";
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(create);
        preparedStatement.executeUpdate();
    }
}
