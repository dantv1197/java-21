package creational.singleton;

import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonBillPugh {
    private SingletonBillPugh() {
        // Preventing Singleton breakage using Java Reflection API
        if (Holder.INSTANCE != null) {
            throw new RuntimeException("Instance is created, cannot create more instance");
        } else {
            try {
                String url = "temple";
                String user = "root";
                String password = "secretpassword";

                DriverManager.getConnection(url, user, password);
                System.out.println(">>> Successfull create Database Connection!");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error connection", e);
            }
        }
    }

    private static class Holder {
        private static final SingletonBillPugh INSTANCE = new SingletonBillPugh();
    }

    public static SingletonBillPugh getInstance() {
        return Holder.INSTANCE;
    }
    /*
     * more function of class
     */
    public static void main(String[] args) {
        SingletonBillPugh billPugh = SingletonBillPugh.getInstance();
        System.out.println("Tempalte" + billPugh);
    }

}
