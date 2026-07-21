package creational.prototype;

import creational.prototype.object.Monster;
import creational.prototype.registry.MonsterRegistry;

public class TemplatePrototype {
    public static void main(String[] args) {
        // 1. Khởi tạo kho mẫu (Chỉ tốn thời gian load 1 lần duy nhất)
        MonsterRegistry registry = new MonsterRegistry();

        System.out.println("--- SPAWN A SERIES OF MONSTERS ON THE MAP ---");

        long startTime = System.currentTimeMillis();

        // Spawn con Goblin số 1 từ mẫu
        Monster goblin1 = registry.getMonster("GOBLIN_BASE");
        
        // Spawn con Goblin số 2 từ mẫu và biến đổi nhẹ (như Goblin đột biến có HP cao hơn)
        Monster goblin2 = registry.getMonster("GOBLIN_BASE");
        goblin2.setHp(150); 

        // Spawn Boss Rồng Lửa
        Monster dragon = registry.getMonster("BOSS_FIRE_DRAGON");

        long endTime = System.currentTimeMillis();

        // 2. Hiển thị thông tin các quái vật vừa spawn
        goblin1.showInfo();
        goblin2.showInfo();
        dragon.showInfo();

        System.out.println("\n-> Time to clone 3 monsters: " + (endTime - startTime) + " ms");
    }
}
