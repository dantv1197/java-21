package behavioral.observer.concreteobserver;

import behavioral.observer.definantion.Observer;

class WindowDisplay : Observer {
    override fun update(temperature: Float) {
        println("[PC Display] Màn hình treo tường hiển thị: $temperature°C")
    }
}