package behavioral.state

class PauseState: State {
    override fun clickPlay(player: MusicPlayer) {
        println("👉 Bấm Play khi đang PAUSED:")
        player.startAudio()
        player.changeState(PlayingState())
    }

    override fun clickStop(player: MusicPlayer) {
        println("👉 Bấm Stop khi đang PAUSED:")
        player.stopAudio()
        player.changeState(StoppedState())
    }
}