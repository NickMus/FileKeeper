package сontroller;

import dao.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.*;
import java.nio.file.Paths;
import java.sql.*;

public class SecondController {
    @FXML
    private Button downloadBtn;

    @FXML
    private ImageView download_btn;

    @FXML
    private Button uploadBtn;

    @FXML
    private ImageView download_btn1;

    @FXML
    private Button trashBtn;

    @FXML
    private ImageView download_btn11;

    @FXML
    private TextArea clientField;

    @FXML
    private TextArea dbField;

    @FXML
    private Button refreshBtn;

    @FXML
    private TextField cmdLine;

    @FXML
    private ImageView download_btn111;

    @FXML
    void initialize() {
        uploadBtn.setOnAction(event -> {
            String path = cmdLine.getText();
            try {
                DatabaseHandler.sendToServer(Paths.get(path));
            } catch (IOException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        refreshBtn.setOnAction(event -> {
            try {
                DatabaseHandler.getUploadedList(dbField);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        downloadBtn.setOnAction(event -> {
            String fileName = cmdLine.getText();
            try {
                DatabaseHandler.downloadFromDatabase(fileName);
            } catch (SQLException | IOException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        trashBtn.setOnAction(event -> {
            String fileName = cmdLine.getText();
            try {
                DatabaseHandler.deleteFromDatabase(fileName);
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
    }
}

