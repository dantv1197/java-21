package behavioral.state

interface State {
    fun clickPlay(player: MusicPlayer)
    fun clickStop(player: MusicPlayer)
}