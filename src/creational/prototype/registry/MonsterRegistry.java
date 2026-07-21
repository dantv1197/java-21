package creational.prototype.registry;

import java.util.HashMap;
import java.util.Map;

import creational.prototype.object.Monster;

public class MonsterRegistry {
    private Map<String,Monster> prototypes = new HashMap<>();
    public MonsterRegistry(){
       loadPrototypes();
    }
    private void loadPrototypes() {
        System.out.println("=== Create template ===");
        
        // create 2 template (took total 4 seconds)
        Monster goblin = new Monster("Goblin", 100, 15);
        Monster boss = new Monster("Fire Dragon", 5000, 300);

        prototypes.put("GOBLIN_BASE", goblin);
        prototypes.put("BOSS_FIRE_DRAGON", boss);
        
        System.out.println("=== created template, loading application");
    }
    public Monster getMonster(String type) {
        Monster prototype = prototypes.get(type);
        if (prototype != null) {
            return prototype.clone(); // return clone object
        }
        return null;
    }
}
