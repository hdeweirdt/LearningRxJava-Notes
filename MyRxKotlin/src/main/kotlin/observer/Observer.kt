package observer

interface Observer<T> {

    fun next(data: T)

    fun error(e: Throwable)

    fun complete()
}