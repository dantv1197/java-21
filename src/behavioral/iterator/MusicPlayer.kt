package behavioral.iterator

class MusicPlayer(private val playlist: PlaylistCollection) {
    private var iterator: BidirectionalIterator<String>? = null

    fun setPlayMode(mode: PlayMode) {
        println("\n>>> ĐỔI CHẾ ĐỘ PHÁT: $mode <<<")
        iterator = playlist.createIterator(mode)
    }

    fun onNextButtonClicked() {
        iterator?.let {
            if (it.hasNext()) {
                val song = it.next()
                println("▶️ Next -> Đang phát: $song")
            } else {
                println("⚠️ Không thể Next (Đã hết danh sách!)")
            }
        }
    }

    fun onPreviousButtonClicked() {
        iterator?.let {
            if (it.hasPrevious()) {
                val song = it.previous()
                println("◀️ Previous -> Quay lại: $song")
            } else {
                println("⚠️ Không thể Previous (Đang ở bài đầu tiên!)")
            }
        }
    }
}