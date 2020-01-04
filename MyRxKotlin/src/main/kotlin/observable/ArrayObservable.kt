package observable

import observer.Observer

class ArrayObservable : Observable<Int> {
    override fun subscribe(observer: Observer<Int>) {
        intArrayOf(10, 20, 30).forEach {
            // Provide the next piece of data to our observer
            observer.next(it)
        }
        observer.complete()
    }
}