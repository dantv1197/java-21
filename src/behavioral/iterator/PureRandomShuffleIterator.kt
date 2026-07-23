package behavioral.iterator

import kotlin.random.Random

class PureRandomShuffleIterator(private val songs: List<String>) : BidirectionalIterator<String> {
    private val history = mutableListOf<String>()
    private var historyIndex = -1
    override fun hasNext(): Boolean {
        return songs.isNotEmpty()
    }

    override fun next(): String {
        if (!hasNext())
            throw NoSuchElementException("list empty!!!!")
        if (historyIndex < history.size - 1) {
            historyIndex++
            return history[historyIndex]
        }
        val randomIndex = Random.nextInt(songs.size)
        val nextSong = songs[randomIndex]

        history.add(nextSong)
        historyIndex++

        return nextSong
    }

    override fun hasPrevious(): Boolean {
        return historyIndex > 0
    }

    override fun previous(): String {
        if (!hasPrevious()) {
            historyIndex == history.size - 1
        }

        historyIndex--
        return history[historyIndex]
    }
}