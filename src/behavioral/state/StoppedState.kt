package behavioral.state

class StoppedState: State {
    override fun clickPlay(player: MusicPlayer) {
        println("👉 Bấm Play khi đang STOP:")
        player.startAudio()
        player.changeState(PlayingState())
    }

    override fun clickStop(player: MusicPlayer) {
        println("👉 Bấm Stop khi đang STOP: Nhạc đã dừng rồi, không làm gì thêm.")
    }
}