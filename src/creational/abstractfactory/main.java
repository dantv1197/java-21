package creational.abstractfactory;

import creational.abstractfactory.concretefactory.UIFactory.LinuxFactory;
import creational.abstractfactory.concretefactory.UIFactory.WindowsFactory;
import creational.abstractfactory.factoryinterface.GUIFactory;

public class main {
    public static void main(String[] args) {
        GUIFactory factory;

        // Lấy thông tin OS từ hệ thống
        String osName = System.getProperty("os.name").toLowerCase();

        // Tự động chọn Factory tương ứng
        if (osName.contains("win")) {
            factory = new WindowsFactory();
        } else {
            factory = new LinuxFactory();
        }

        // Khởi tạo ứng dụng với Factory đã chọn
        Application app = new Application(factory);

        System.out.println("=== HIỂN THỊ GIAO DIỆN ỨNG DỤNG ===");
        app.paint();
    }
}
