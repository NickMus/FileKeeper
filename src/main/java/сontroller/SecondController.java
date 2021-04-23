package сontroller;

import сlient.Client;
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SecondController {

    static Path path1;
    static String files_name = "files_name";
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
    }

    public void getPath() {
        String path = cmdLine.getText();
        path1 = Paths.get(path);
        boolean pathExist = Files.exists(path1);
        System.out.println(pathExist);
        System.out.println(path1);
    }

    public void sendToServer() throws IOException, SQLException, ClassNotFoundException {

        System.out.println("я тут");
        String extention = "";
        int i = String.valueOf(path1).lastIndexOf('.');
        if (i > 0) {
            extention = String.valueOf(path1).substring(i + 1);
        }
        System.out.println(extention);
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(String.valueOf(path1)));
        BufferedOutputStream bos = new BufferedOutputStream(Client.socket.getOutputStream()); //при повторной отправке сокет закрыт
        byte[] byteArray = new byte[8192]; //долго передает большие файлы. увеличить?
        int in;
        while ((in = bis.read(byteArray)) != -1) {
            bos.write(byteArray, 0, in);
        }

        String insert = "INSERT INTO " + Configs.dbName + "." + Const.USER_TABLE_FILES +
                "(" + files_name + "," + files_data + ")" +
                "VALUES(?,?)";
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(insert);
        preparedStatement.setString(1, String.valueOf(path1.getFileName())); //file name check!
        preparedStatement.setBytes(2, byteArray);
        preparedStatement.executeUpdate();
        //bis.close(); проблема тут
        //bos.close();
        System.out.println("вроде получается");

    }

    public void getUploadedList() throws SQLException, ClassNotFoundException {
        ResultSet resultSet;
        String select = "SELECT " + files_name + " FROM " + Configs.dbName + ". " + Const.USER_TABLE_FILES;
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(select);
        resultSet = preparedStatement.executeQuery();
        dbField.clear();
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            System.out.println(name);
            dbField.appendText(name + '\n');
        }
        //System.out.println(resultSet);

    }


    public void downloadFromDatabase() throws SQLException, ClassNotFoundException, IOException {
        System.out.println("start download");
        InputStream is = null;
        ResultSet resultSet;
        String fileName = cmdLine.getText();
        String select = "SELECT " + files_data + " FROM " + Configs.dbName + "." + Const.USER_TABLE_FILES + " WHERE " + files_name + "=?";
        System.out.println("select from db");
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(select);
        preparedStatement.setString(1, fileName);
        resultSet = preparedStatement.executeQuery();
        System.out.println("executed");
        File targetFile = new File(fileName);
        System.out.println(fileName);
        targetFile.createNewFile();

        System.out.println(targetFile.exists());
        System.out.println(targetFile.getTotalSpace());
        while (resultSet.next()) {
            is = resultSet.getBinaryStream(files_data);
        }

        OutputStream bos = new FileOutputStream(targetFile);
        byte[] byteArray = new byte[8192];
        int in;
        while ((in = is.read(byteArray)) != -1) {
            bos.write(byteArray, 0, in);
        }
        System.out.println(targetFile.getTotalSpace());


        System.out.println("ку");

        System.out.println(targetFile.getAbsolutePath());
    }

    public void deleteFromDatabase() {
        ResultSet resultSet = null;
        String fileName = cmdLine.getText();

        // String drop = "DROP FROM " + dbName + "." + Const.Const.USER_TABLE_FILES

    }
}
