package creational.abstractfactory.factoryinterface;

import creational.abstractfactory.interfacefactory.Checkbox;
import creational.abstractfactory.interfacefactory.Button;

public interface  GUIFactory {

    Button createButton();

    Checkbox createCheckbox();
}