package creational.singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Thread-safe & Anti-Reflection
public class SingletonTemplate {
    public static void main(String[] args) {
        DatabaseConnection databaseConnection = DatabaseConnection.INSTANCE;
        databaseConnection.connect();
        LazyConnection lazyConnection = LazyConnection.INSTANCE;
        lazyConnection.lazyConnection();

    }

    public enum DatabaseConnection {
        INSTANCE;

        private Connection connection;

        public void connect() {
            System.out.println("success connect!");
        }

        private DatabaseConnection() {
            try {
                String url = "temple";
                String user = "root";
                String password = "secretpassword";

                this.connection = DriverManager.getConnection(url, user, password);
                System.out.println(">>> Successfull create Database Connection!");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error connection", e);
            }
        }

        public Connection getConnection() {
            return connection;
        }
    }

    public enum LazyConnection {
        INSTANCE;

        private Connection connection;

        public synchronized Connection lazyConnection() {
            try {
                if (connection == null || connection.isClosed()) {
                    String url = "temple";
                    String user = "root";
                    String password = "secretpassword";

                    this.connection = DriverManager.getConnection(url, user, password);
                    System.out.println(">>> Successfull create Database Connection!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error connection", e);
            }
            return connection;
        }

    }
}
