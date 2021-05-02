package сlient.service;

import dao.DatabaseHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.Sceneable;
import сlient.User;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginUser implements Sceneable {

    public void loginUser(String login, String password) {
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
            System.out.println("Client.User isn't in database");
        }
    }

    @Override
    public void openNewScene(String window) {
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

    }
}


