package creational.factorymethod.sms;

import creational.factorymethod.Notification;

public class SMSNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Sent sms to user: "+ message);
    }
    
}
