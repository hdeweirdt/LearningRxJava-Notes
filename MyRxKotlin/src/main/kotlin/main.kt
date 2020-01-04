import observable.ArrayObservable
import observer.Observer

fun main() {
    ArrayObservable()
        .map { x -> x + 3 }
        .map { x -> "Number $x" }
        .repeat(2)
        .subscribe(object : Observer<String> {
            override fun next(data: String) {
                println(data)
            }

            override fun error(e: Throwable) {
                e.printStackTrace()
            }

            override fun complete() {
                println("Completed!")
            }
        })
}