# 2 - Observables and Subscribers

## The Observable

**An Observable is push-based composable iterator**

An Observable<T> pushes items of type T through a series of operators until it arrives at a final Observer which consumes the item.

## How Observables work

An Observable _passes_ three types of events:
* onNext: pass items one at a time from Observable down to Observer
* onComplete: signals completion all the way down to the Observer
* onError: communicates an Error all they way down to the Observer
	* _retry_ operators can intercept error events

## Using Observable.create()

* the _Source Observable_:
	* is the Observable where emissons originate from
	* is the start of an Observable chain

* Observable.create() factory method
	* lambda with an Observable _emitter_ argument on which you call onNext(), onError() and onComplete()
	* not used that much, mostly to hook into non-reactive sources	
	
* Adding operations: onNext **pushes** result to the next step, eg. _map_ or _filter_

## Using Observable.just()

* Pass up to 10 items, it will call onNext() on each of them and then onComplete()
* Observable.fromIterable(): onNext() for each item and then OnComplete()

## The Observer Interface:

```
Observer<T> {
	void onSubscribe(Disposable d)
	void onNext(T value)
	void onError(Throwable e)
	void onComplete()
}
```

**Each Observable returned by an Operator is internally also an Observer that receives, transforms and relays emissions to the next Observer downstream.**
```
----------+    +----------------------+    +----------
Observable| -> |Observer |> Observable| -> |Observer
----------+    +----------------------+    +----------
                      Operator
```

## Implementing and subscribing to an Observer

When subscribing to an Observable, you have to pass in an Observer that will consume the emitted events.
Calling subscribe on the Observable starts an onNext() call in the source Observable, which is then passed through the chain.

## Shorthand observers with lambdas

* subscribe() can also receive 3 lambdas as parameter
	* Consumer<T> onNext
	* Action onComplete
	* Consumer<Throwable> onError

* You don't always have to pass onComplete.
* You don't always have to pass OnError, but it is highly recommended because otherwise errors will not be handled.

* onSubscribe passes in a _Disposable_
	* used to decouple an Observable from the Observer
	* eg. with infinite or long-running Observables
	* eg. Android Lifecycle
=
