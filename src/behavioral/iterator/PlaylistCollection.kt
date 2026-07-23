package behavioral.iterator

interface PlaylistCollection {
    fun createIterator(mode: PlayMode): BidirectionalIterator<String>
}