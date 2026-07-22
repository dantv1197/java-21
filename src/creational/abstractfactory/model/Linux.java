package creational.abstractfactory.model;

import creational.abstractfactory.interfacefactory.*;

public class Linux {
    public static class LinuxButton implements Button {
        @Override
        public void render() {
            System.out.println("[Linux] Vẽ nút bấm phong cách vuông vức Windows.");
        }
    }

    public static class LinuxCheckbox implements Checkbox {
        @Override
        public void render() {
            System.out.println("[Linux] Vẽ ô tích chọn Checkbox phong cách Windows.");
        }
    }
}
