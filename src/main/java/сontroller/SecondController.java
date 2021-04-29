package сontroller;

import сonst.Const;
import dao.DatabaseHandler;
import сonst.Configs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class SecondController {

    static Path path1;
    static String file_name = "files_name";
    static String files_data = "files_data";


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
            getPath();
            try {
                sendToServer();
            } catch (IOException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        refreshBtn.setOnAction(event -> {
            try {
                getUploadedList();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        downloadBtn.setOnAction(event -> {
            try {
                downloadFromDatabase();
            } catch (SQLException | IOException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });

        trashBtn.setOnAction(event -> {
            try {
                deleteFromDatabase();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    public void getPath() {
        String path = cmdLine.getText();
        System.out.println(path);
        path1 = Paths.get(path);
        boolean pathExist = Files.exists(path1);
        System.out.println(pathExist);
        System.out.println(path1);
    }

    public void sendToServer() throws IOException, SQLException, ClassNotFoundException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(String.valueOf(path1)));
        System.out.println("размер файла " + bis.available());

        byte[] byteArray = new byte[8192];
        String insert = "INSERT INTO " + Configs.dbName + "." + Const.USER_TABLE_FILE +
                "(" + file_name + "," + files_data + ")" +
                "VALUES(?,?)";
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(insert);
        while ((bis.read(byteArray)) != -1) {
            preparedStatement.setString(1, String.valueOf(path1.getFileName()));
            preparedStatement.setBytes(2, byteArray);
            preparedStatement.executeUpdate();
            System.out.println(bis.available());
            preparedStatement.clearParameters();
        }
        bis.close();
    }

    public void getUploadedList() throws SQLException, ClassNotFoundException {
        ResultSet resultSet;
        String select = "SELECT " + file_name + " FROM " + Configs.dbName + ". " + Const.USER_TABLE_FILE;
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(select);
        resultSet = preparedStatement.executeQuery();
        dbField.clear();
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            System.out.println(name);
            dbField.appendText(name + '\n');
        }
    }


    public void downloadFromDatabase() throws SQLException, ClassNotFoundException, IOException {
        System.out.println("start download");
        InputStream is = null;
        BufferedInputStream bf = null;
        ResultSet resultSet;
        String fileName = cmdLine.getText();
        String select = "SELECT " + files_data + " FROM " + Configs.dbName + "." + Const.USER_TABLE_FILE +
                " WHERE " + file_name + "=?";
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(select);
        preparedStatement.setString(1, fileName);
        resultSet = preparedStatement.executeQuery();
        File targetFile = new File(fileName);
        System.out.println(targetFile.exists());
        System.out.println(targetFile.getUsableSpace());
        OutputStream os = new FileOutputStream(targetFile);
        while (resultSet.next()) {
            is = resultSet.getBinaryStream(files_data);
            bf = new BufferedInputStream(is);
            byte[] byteArray = new byte[8192 * 4];
            int in;
            while ((in = bf.read(byteArray)) != -1) {
                os.write(byteArray, 0, in);
            }
            os.flush();
            is.close();
            bf.close();
        }
    }

    public void deleteFromDatabase() throws SQLException, ClassNotFoundException {
        String fileName = cmdLine.getText();
        String delete = "DELETE FROM " + Configs.dbName + "." + Const.USER_TABLE_FILE + " WHERE " +
                file_name + "=?";
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(delete);
        preparedStatement.setString(1, fileName);
        preparedStatement.executeUpdate();
        System.out.println("File deleted " + fileName);
    }
}

