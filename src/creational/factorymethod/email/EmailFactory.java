package creational.factorymethod.email;

import creational.factorymethod.Notification;
import creational.factorymethod.NotificationFactory;

public class EmailFactory extends NotificationFactory {
    @Override
    public Notification createNotification() {
        return new EmailNotìication();
    }
    
}
