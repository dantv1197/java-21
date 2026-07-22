package behavioral.observer

import behavioral.observer.concreteobserver.PhoneDisplay
import behavioral.observer.concreteobserver.WindowDisplay
import behavioral.observer.concretesubject.WeatherStation

fun main() {
    val station = WeatherStation()

    val phone = PhoneDisplay()
    val window = WindowDisplay()

    // Đăng ký nhận tin
    station.registerObserver(phone)
    station.registerObserver(window)

    // Thay đổi nhiệt độ lần 1 -> Cả 2 màn hình đều nhận thông báo
    station.setTemperature(28.5f)

    // Hủy đăng ký điện thoại
    station.removeObserver(phone)

    // Thay đổi nhiệt độ lần 2 -> Chỉ còn WindowDisplay nhận
    station.setTemperature(30.0f)
}