package behavioral.iterator

fun main() {
    val playlist = Playlist().apply {
        addSong("Bài A")
        addSong("Bài B")
        addSong("Bài C")
    }

    val player = MusicPlayer(playlist)

    // ==========================================
    // KHỊ CHẠY CHẾ ĐỘ PHÁT TUẦN TỰ (SEQUENTIAL)
    // ==========================================
    player.setPlayMode(PlayMode.SEQUENTIAL)

    player.onNextButtonClicked() // Phát Bài A
    player.onNextButtonClicked() // Phát Bài B
    player.onNextButtonClicked() // Phát Bài C
    player.onNextButtonClicked() // Báo hết danh sách

    player.onPreviousButtonClicked() // Quay lại Bài B
    player.onPreviousButtonClicked() // Quay lại Bài A
    player.onPreviousButtonClicked() // Báo không thể lùi thêm nữa

    // ==========================================
    // KHI CHẠY CHẾ ĐỘ PHÁT SHUFFLE (PURE SHUFFLE)
    // ==========================================
    player.setPlayMode(PlayMode.PURE_SHUFFLE)

    player.onNextButtonClicked() // Bốc ngẫu nhiên bài 1
    player.onNextButtonClicked() // Bốc ngẫu nhiên bài 2
    player.onNextButtonClicked() // Bốc ngẫu nhiên bài 3

    player.onPreviousButtonClicked() // Quay lại bài 2 trong Lịch sử
    player.onNextButtonClicked()     // Tiến tới bài 3 trong Lịch sử
    player.onNextButtonClicked()     // Đã hết lịch sử cũ -> Bốc ngẫu nhiên bài 4 mới
}