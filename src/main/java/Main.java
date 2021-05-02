import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.flywaydb.core.Flyway;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Server.startServer();


        Parent root = FXMLLoader.load(getClass().getResource("windows/main.fxml"));
        primaryStage.setTitle("FileKeeper");
        primaryStage.setScene(new Scene(root, 650, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        migrateDatabase();

    }

    public void migrateDatabase() {
        Flyway flyway = new Flyway();
        flyway.setDataSource("jdbc:mysql://localhost:3306/test", "root", "password");
        flyway.migrate();
    }
}
