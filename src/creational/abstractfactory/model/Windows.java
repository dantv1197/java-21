package creational.abstractfactory.model;

import creational.abstractfactory.interfacefactory.*;

public class Windows {
    public static class WindowsButton implements Button {
        @Override
        public void render() {
            System.out.println("[Windows] Vẽ nút bấm phong cách vuông vức Windows.");
        }
    }
    public static class WindowsCheckbox implements Checkbox {
        @Override
        public void render() {
            System.out.println("[Windows] Vẽ ô tích chọn Checkbox phong cách Windows.");
        }
    }

}
