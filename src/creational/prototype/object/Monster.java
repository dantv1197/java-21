package creational.prototype.object;

import creational.prototype.Prototype;

public class Monster implements Prototype<Monster> {
    private String name;
    private int hp;
    private int damage;
    private String modelData; // data heavy simulation( load db, draw graphic,....)

    public Monster(String name, int hp, int damage) {
        this.name = name;
        this.hp = hp;
        this.damage = damage;

        // sample load data from DB or disk
        this.modelData = loadHeavyGraphicModel(name);
    }

    private Monster(Monster target) {
        if (target != null) {
            this.name = target.name;
            this.hp = target.hp;
            this.damage = target.damage;
            // share or coppy data has already been loaded into RAM
            this.modelData = target.modelData;
        }
    }

    private String loadHeavyGraphicModel(String name) {
        System.out.println("-> load data from DB or disk " + name + "... (took 2 seconds)");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }
        return "Model_3D_Data_Of_" + name;
    }

    @Override
    public Monster clone() {
        return new Monster(this);
    }

    /*
     * get, set or other function
     */
    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public String getModelData() {
        return modelData;
    }

    public void setModelData(String modelData) {
        this.modelData = modelData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void showInfo() {
        System.out.println(
                "Monster: " + name + " | HP: " + hp + " | DMG: " + damage + " | Memory Hash: " + this.hashCode());
    }

}
