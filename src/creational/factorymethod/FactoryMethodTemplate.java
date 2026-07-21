package creational.factorymethod;

import creational.factorymethod.email.EmailFactory;
import creational.factorymethod.sms.SMSFactory;

public class FactoryMethodTemplate {
    public static void main(String[] args) {
        NotificationFactory factory = new SMSFactory();
        factory.sendNotification("hello user");
        System.out.println("-----------------------------------");
        factory = new EmailFactory();
        factory.sendNotification("Email was sent to user");
    }
}
