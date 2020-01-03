# 2 - Observables and Subscribers

## The Observable

**An Observable is push-based composable iterator**

An Observable<T> pushes items of type T through a series of operators until it arrives at a final Observer which consumes the item.

### How Observables work

An Observable _passes_ three types of events:
* onNext: pass items one at a time from Observable down to Observer
* onComplete: signals completion all the way down to the Observer
* onError: communicates an Error all they way down to the Observer
	* _retry_ operators can intercept error events

### Using Observable.create()

* the _Source Observable_:
	* is the Observable where emissons originate from
	* is the start of an Observable chain

* Observable.create() factory method
	* lambda with an Observable _emitter_ argument on which you call onNext(), onError() and onComplete()
	* not used that much, mostly to hook into non-reactive sources	
	
* Adding operations: onNext **pushes** result to the next step, eg. _map_ or _filter_

### Using Observable.just()

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

### Implementing and subscribing to an Observer

When subscribing to an Observable, you have to pass in an Observer that will consume the emitted events.
Calling subscribe on the Observable starts an onNext() call in the source Observable, which is then passed through the chain.

### Shorthand observers with lambdas

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

## Cold versus Hot Observables

Difference is in how Observables behave when there are multiple Observers

### Cold Observables
**Will repeat emissions for each new Observer, one Observer at a time**

Example: emittiong "1, 2, 3" to two Observers:

```
Observer 1: 1
Observer 1: 2
Observer 1: 3
Observer 2: 1
Observer 2: 2
Observer 2: 3
Observer 1: 1
```
Used mostly for data, e.g. reading from a file or database

### Hot Observables

Broadcast the same emissions to all Observers at the same time. 
Past items are not emitted again.

Used to represent events (that can carry data with them), e.g. UI events, live data streams.

### ConnectableObservable

Turn any Observable (hot or cold) into a hot Observable.
How? 
1. call _publish()_ on any Observable
2. _subscribe()_ to the resulting ConnectableObservable (with multiple Observers)
3. call _connect()_ on the ConnectableObservable to start it

Result:
```
Observer 1: 1
Observer 2: 1
Observer 1: 2
Observer 2: 2
Observer 1: 3
Observer 2: 3
```
Used to:
* prevent replaying of past data to each Observer
* force upstream operators to use a single stream instance even if there are multiple Observers downstream (also called multicasting)

## Other Observable sources

### Observable.range()

syntax: Observable.range(start, count)
Emits a range of _[count]_ integers starting at _[start]_

### Observable.interval()

syntax: Observable.interval(interval, timeunit)
Emits integers starting from 0 every _[interval]_

* Runs on a ttimer, so **not** on the main thread
* **Cold** Observable: new Observers start receiving from the beginning

### Observable.future()

syntax: Observable.fromFuture(future)
Turns a Future into an Observable

### Observable.empty()

syntax: Observable.empty()
Immediatly calls onComplete()

* Used to represent empty datasets (Rx equivalent of _null_)

### Observable.never()

syntax: Observable.never()
Never calls anything

* Only used for testing

### Observable.error()

syntax: Observable.error()
Immediatly calls onError()

### Observable.defer()

syntax: Observable.defer(()->[normal Observable])
Creates seperate state for each Observer

* Used when your source is stateful, when the source Observable can change in between different Observers. e.g. when you use a variable to initialise Observable.range() and then change the variable.

### Observable.fromCallable()

syntax: Observable.fromCallable(...)
Perform a calculation and emit the result **lazily**

* Non-lazy equivalent is Observable.just(...)
* Errors will result in onError instead of an Exception (as is the case with Observable.just())

## Single, Completable and Maybe

### Single

* Observable that emits only **1** item
* _onSuccess_ replaces OnNext and OnComplete

### Maybe

* Observable that emits **0 or 1** items
* onNext is called _onSuccess_, onComplete still exists.

### Completable

* Observable that represents an action having executed.
* only onError and onComplete

## Disposing

When subscribing to an Observable, you create a stream, which uses **resources**.
When done with the Observable, we should of course dispose of these.

* Every Observer has a _dispose()_ and _isDisposed()_ method.
* Observable.subscribe() returns a Disposable.

### Handling a Disposable within an Observer

Instead of using Observable.subscribe, which returns the Disposable, you can use Observable.onSubscribe and pass in your own implementation of the Observer interface and control the disposing yourself.
When you use onSubscribe, RxJava assumes you will do this and as such does not return a Disposable!

* If you want to implementt the Interface but not handle the disposing, implement _ResourceObserver_ and use _subscribeWith_.

### Using CompositeDisposable

A CompositeDisposable holds a list of Disposables and allows you to dispose of them all at the same time.

### Handling disposable with Observable.create()

* Use the _ObservableEmitter.isDisposed()_ call to check if you should continue emitting.
* Use _setCancellable_ and _setDisposable_ on the ObservableEmitter to define callbacks for when these actions happen.
