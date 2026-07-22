package behavioral.observer.concretesubject

import behavioral.observer.definantion.Subject
import behavioral.observer.definantion.Observer

class WeatherStation : Subject {
    private val observers = mutableListOf<Observer>()
    private var temperature: Float = 0f

    override fun registerObserver(o: Observer) {
        observers.add(o)
    }

    override fun removeObserver(o: Observer) {
        observers.remove(o)
    }

    override fun notifyObservers() {
        for (observer in observers) {
            observer.update(temperature)
        }
    }

    fun setTemperature(newTemp: Float) {
        println("\n[WeatherStation] Nhiệt độ mới: $newTemp°C")
        this.temperature = newTemp
        notifyObservers()
    }
}
