package behavioral.state

class PlayingState: State {
    override fun clickPlay(player: MusicPlayer) {
        println("👉 Bấm Play khi đang PLAYING:")
        player.pauseAudio()
        player.changeState(PauseState())
    }

    override fun clickStop(player: MusicPlayer) {
        println("👉 Bấm Stop khi đang PLAYING:")
        player.stopAudio()
        player.changeState(StoppedState())
    }
}