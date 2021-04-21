import java.sql.*;

public class DatabaseHandler extends Server {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName;
        Class.forName("com.mysql.cj.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, dbUser, dbPass);
        return dbConnection;
    }
    public void singUpUser(User user) {
        String insert = "INSERT INTO " + dbName + "." + Const.USER_TABLE + "(" + Const.USER_LOGIN + "," + Const.USER_PASSWORD + ")" +
                    "VALUES(?,?)";
            try {
                PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);
                preparedStatement.setString(1, user.getLogin());
                preparedStatement.setString(2, user.getPassword());


                preparedStatement.executeUpdate();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        }


    public ResultSet getUser(User user) {
        ResultSet resultSet = null;


        String select = "SELECT * FROM " + dbName + "." + Const.USER_TABLE + " WHERE " + Const.USER_LOGIN + "=? AND " +
                Const.USER_PASSWORD + "=?";
        try {
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(select);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2,user.getPassword());

            resultSet = preparedStatement.executeQuery();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return resultSet;
    }

}
