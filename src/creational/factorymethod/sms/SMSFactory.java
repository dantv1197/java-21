package creational.factorymethod.sms;

import creational.factorymethod.Notification;
import creational.factorymethod.NotificationFactory;

public class SMSFactory extends NotificationFactory {

    @Override
    public Notification createNotification() {

        return new SMSNotification();
    }

}
