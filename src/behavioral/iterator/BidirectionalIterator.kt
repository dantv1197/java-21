package behavioral.iterator

interface BidirectionalIterator<T> {
    fun hasNext(): Boolean
    fun next(): T
    fun hasPrevious(): Boolean
    fun previous(): T
}