package behavioral.iterator

class Playlist: PlaylistCollection {
    private val songs = mutableListOf<String>()

    fun addSong(song: String) {
        songs.add(song)
    }
    override fun createIterator(mode: PlayMode): BidirectionalIterator<String> {
        return when (mode) {
            PlayMode.PURE_SHUFFLE -> PureRandomShuffleIterator(songs)
            PlayMode.SEQUENTIAL -> SequentialIterator(songs) //
        }
    }
}