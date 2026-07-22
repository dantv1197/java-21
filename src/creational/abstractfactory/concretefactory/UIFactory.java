package creational.abstractfactory.concretefactory;

import creational.abstractfactory.factoryinterface.GUIFactory;
import creational.abstractfactory.interfacefactory.Checkbox;
import creational.abstractfactory.interfacefactory.Button;
import creational.abstractfactory.model.Windows.WindowsButton;
import creational.abstractfactory.model.Windows.WindowsCheckbox;
import creational.abstractfactory.model.Linux.LinuxButton;
import creational.abstractfactory.model.Linux.LinuxCheckbox;

public class UIFactory {
    public static class WindowsFactory implements GUIFactory {

        @Override
        public Button createButton() {
            return new WindowsButton();
        }

        @Override
        public Checkbox createCheckbox() {
            return new WindowsCheckbox();
        }

    }

    public static class LinuxFactory implements GUIFactory {

        @Override
        public Button createButton() {
            return new LinuxButton();
        }

        @Override
        public Checkbox createCheckbox() {
            return new LinuxCheckbox();
        }

    }

}