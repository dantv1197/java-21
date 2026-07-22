package behavioral.observer.concreteobserver

import behavioral.observer.definantion.Observer

class PhoneDisplay : Observer {
    override fun update(temperature: Float) {
        println("[Phone Display] Cập nhật nhiệt độ trên điện thoại: $temperature°C")
    }
}