package creational.abstractfactory;

import creational.abstractfactory.interfacefactory.Checkbox;
import creational.abstractfactory.factoryinterface.GUIFactory;
import creational.abstractfactory.interfacefactory.Button;

public class Application {
    private Button button;
    private Checkbox checkbox;
    public Application(GUIFactory factory){
        button = factory.createButton();
        checkbox = factory.createCheckbox();

    }
    public void paint(){
        button.render();
        checkbox.render();
    }
    
}
