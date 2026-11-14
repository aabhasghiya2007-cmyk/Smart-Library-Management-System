import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// ============================================================
// DATABASE CONNECTION
// Change USER and PASSWORD to your MySQL login.
// ============================================================

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/library_db"
                    + "?useSSL=false&allowPublicKeyRetrieval=true"
                    + "&serverTimezone=UTC";

    private static final String USER = "root";

    private static final String PASSWORD = "@ADITI140205";


    public static Connection getConnection() throws SQLException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            throw new SQLException(
                    "MySQL JDBC driver not found. "
                            + "Add mysql-connector-j.jar to the classpath.");
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}