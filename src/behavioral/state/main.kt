package behavioral.state

fun main() {
    val player = MusicPlayer()

    println("=== LẦN 1: Bấm Play từ StoppedState ===")
    player.clickPlay() // Phát nhạc -> Chuyển sang PlayingState

    println("\\n=== LẦN 2: Bấm Play khi đang PlayingState ===")
    player.clickPlay() // Tạm dừng -> Chuyển sang PausedState

    println("\\n=== LẦN 3: Bấm Play khi đang PausedState ===")
    player.clickPlay() // Tiếp tục phát -> Chuyển sang PlayingState

    println("\\n=== LẦN 4: Bấm Stop khi đang PlayingState ===")
    player.clickStop() // Dừng hẳn -> Chuyển sang StoppedState
}