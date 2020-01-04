package observable

import observer.Observer

/**
 * Represents a source of data
 */
interface Observable<T> {
    fun subscribe(observer: Observer<T>)

    fun repeat(numberOfTimes: Int): Observable<T> {
        val inputObservable = this
        val outputObservable = object : Observable<T> {
            override fun subscribe(observer: Observer<T>) {
                val receivedEmissions = mutableListOf<T>()
                // Call the inputObservable and ask it to data and deliver it to our Observer<T>
                inputObservable.subscribe(object : Observer<T> {
                    override fun next(data: T) {
                        receivedEmissions.add(data)
                    }

                    override fun error(e: Throwable) = observer.error(e)

                    override fun complete() {
                        repeat(numberOfTimes) {
                            receivedEmissions.forEach { observer.next(it) }
                        }
                        observer.complete()
                    }
                })
            }

        }
        return outputObservable
    }

    fun filter(condition: (input: T) -> Boolean): Observable<T> {
        val inputObservable = this
        val outputObservable = object : Observable<T> {
            override fun subscribe(observer: Observer<T>) {
                // Call the inputObservable and ask it to data and deliver it to our Observer<T>
                inputObservable.subscribe(object : Observer<T> {
                    override fun next(data: T) {
                        if (condition(data)) {
                            observer.next(data)
                        }
                    }

                    override fun error(e: Throwable) = observer.error(e)

                    override fun complete() = observer.complete()
                })
            }

        }
        return outputObservable
    }

    fun <R> map(transformer: (input: T) -> R): Observable<R> {
        val inputObservable = this
        val outputObservable = object : Observable<R> {
            override fun subscribe(observer: Observer<R>) {
                // Call the inputObservable and ask it to data and deliver it to our Observer<T>
                inputObservable.subscribe(object : Observer<T> {
                    override fun next(data: T) {
                        val y = transformer(data)
                        // Pass the transformed piece of data to whoever observers the result of this map
                        observer.next(y)
                    }

                    // Pass on the error to whoever observes us
                    override fun error(e: Throwable) = observer.error(e)

                    // Pass on the completion to whoever observes us
                    override fun complete() = observer.complete()
                })
            }

        }
        return outputObservable
    }
}

