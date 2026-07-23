package behavioral.state

class MusicPlayer {
    var state: State = StoppedState()

    fun changeState(newState: State) {
        println("🔄 Chuyển trạng thái: ${state::class.simpleName} ──► ${newState::class.simpleName}")
        this.state = newState
    }

    // Các phương thức ủy nhiệm (delegate) cho State hiện tại xử lý
    fun clickPlay() {
        state.clickPlay(this)
    }

    fun clickStop() {
        state.clickStop(this)
    }

    // Các hành động thực tế của Player
    fun startAudio() = println("🎵 Đang phát nhạc...")
    fun pauseAudio() = println("⏸️ Đang tạm dừng...")
    fun stopAudio()  = println("⏹️ Đã dừng hẳn bài hát.")
}