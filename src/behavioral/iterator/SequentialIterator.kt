package behavioral.iterator

import kotlin.random.Random

class SequentialIterator (private val songs: List<String>) : BidirectionalIterator<String> {
    private var currentIndex = -1
    override fun hasNext(): Boolean {
        return currentIndex + 1 < songs.size
    }

    override fun next(): String {
        if (!hasNext())
            throw NoSuchElementException("list empty!!!!")
        currentIndex++
        return songs[currentIndex]
    }

    override fun hasPrevious(): Boolean {
        return currentIndex > 0
    }

    override fun previous(): String {
        if (!hasPrevious()) {
            throw NoSuchElementException("Đã ở bài hát đầu tiên, không thể quay lại!")
        }
        currentIndex--
        return songs[currentIndex]
    }
}