package dao;

import javafx.scene.control.TextArea;
import org.apache.commons.io.FilenameUtils;
import сlient.User;
import сonst.Configs;
import сonst.Const;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;


public class DatabaseHandler {
    static Connection dbConnection;

    public static Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + Configs.dbHost + ":" + Configs.dbPort + "/" + Configs.dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, Configs.dbUser, Configs.dbPass);
        return dbConnection;
    }

    public void singUpUser(User user) throws SQLException, ClassNotFoundException {
        String create = "CREATE TABLE IF NOT EXISTS " + Configs.dbName + "." + Const.USER_TABLE + " (" +
                "iduser INT NOT NULL AUTO_INCREMENT, " +
                "user_login VARCHAR(45) NOT NULL, " +
                "user_pass VARCHAR(45) NOT NULL, " +
                "PRIMARY KEY (iduser))";
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(create);
        preparedStatement.executeUpdate();
        String insert = "INSERT INTO " + Configs.dbName + "." + Const.USER_TABLE + "(" + Const.USER_LOGIN + "," + Const.USER_PASSWORD + ")" +
                "VALUES(?,?)";
        try {
            preparedStatement = getDbConnection().prepareStatement(insert);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }


    public ResultSet getUser(User user) {
        ResultSet resultSet = null;
        String select = "SELECT * FROM " + Configs.dbName + "." + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + "=? AND " +
                Const.USER_PASSWORD + "=?";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

    public static void sendToServer(Path path) throws IOException, SQLException, ClassNotFoundException {
        String file_name = "file_name";
        String file_data = "file_data";
        String fileName = String.valueOf(path.getFileName());
        System.out.println(fileName);
        String create = "CREATE TABLE IF NOT EXISTS " + Configs.dbName + "." + FilenameUtils.removeExtension(fileName) +
                " (" +
                "id INT NOT NULL AUTO_INCREMENT, " +
                file_name + " VARCHAR(45) NOT NULL, " +
                file_data + " BLOB NOT NULL, " +
                "PRIMARY KEY(id))";
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(create);
        preparedStatement.executeUpdate();
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(String.valueOf(path)));
        System.out.println("размер файла " + bis.available());
        byte[] byteArray = new byte[8192];
        String insert = "INSERT INTO " + Configs.dbName + "." + FilenameUtils.removeExtension(fileName) +
                "(" + file_name + "," + file_data + ")" +
                "VALUES(?,?)";
        preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(insert);
        while ((bis.read(byteArray)) != -1) {
            preparedStatement.setString(1, String.valueOf(path.getFileName()));
            preparedStatement.setBytes(2, byteArray);
            preparedStatement.executeUpdate();
            System.out.println(bis.available());
            preparedStatement.clearParameters();
        }
        bis.close();
    }

    public static void getUploadedList(TextArea dbField) throws SQLException, ClassNotFoundException {
        ResultSet resultSet;
        String select = "SHOW TABLES ";
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(select);
        resultSet = preparedStatement.executeQuery();
        dbField.clear();
        while (resultSet.next()) {
            String name = resultSet.getString(1);
            System.out.println(name);
            dbField.appendText(name + '\n');
        }

    }


    public static void downloadFromDatabase(String fileName) throws SQLException, ClassNotFoundException, IOException {
        String file_data = "file_data";
        String file_name = "file_name";
        String targetFileName = null;
        InputStream is;
        BufferedInputStream bf;
        ResultSet resultSet;
        PreparedStatement preparedStatement;
        String selectName = "SELECT " + file_name + " FROM " + fileName + " WHERE " + "id = 1";
        preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(selectName);
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            targetFileName = resultSet.getString(file_name);
        }
        Path targetPath = Paths.get("C:\\Users\\Public\\Pictures");
        File targetFile = new File(String.valueOf(targetPath), targetFileName);
        targetFile.createNewFile();

        System.out.println(targetFile);
        System.out.println(targetFile.exists());
        System.out.println(targetFile.getUsableSpace());
        DatabaseHandler.getDbConnection().close();
        String selectData = "SELECT " + file_data + " FROM " + fileName;
        preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(selectData);
        resultSet = preparedStatement.executeQuery();
        OutputStream os = new FileOutputStream(targetFile);
        while (resultSet.next()) {
            is = resultSet.getBinaryStream(file_data);
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

    public static void deleteFromDatabase(String fileName) throws SQLException, ClassNotFoundException {
        String drop = "DROP TABLE " + fileName;
        PreparedStatement preparedStatement = DatabaseHandler.getDbConnection().prepareStatement(drop);
        preparedStatement.executeUpdate();
        System.out.println("File deleted " + fileName);
    }
}


