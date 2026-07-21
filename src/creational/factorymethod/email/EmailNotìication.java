package creational.factorymethod.email;

import creational.factorymethod.Notification;

public class EmailNotìication implements Notification  {
    @Override
    public void send(String message) {
        System.out.println("Send email to user: " + message);
        
    }
    
}
