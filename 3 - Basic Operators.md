# 3 - Basic Operators
---------------------
Operators are Observers to the Observable they operate on.

## Suppresssing Operators

### filter()

syntax: filter(Predicate<T>)
Only allows emissions that map to _true_ to pass. 

### take()

syntax: take(number: Integer)
Takes the specified number of emissions, then calls onComplete().

syntax: take(period, timeunit)
Takes emissions until the specified time has passed, then calls onComplete().

syntax: takeLast(number:Integer) /  takeLast(period, timeunit)
Takes the last specified number of emissions / the emissions made during the last period.

### skip()

syntax: skip(number: Integer) / skip(period, timeunit)
Opposite of take.

### takeWhile() & skipWhile()

syntax: takeWhile(Predicate<T>)
Takes new emissions as long as the predicate for each emissions maps to _true_. The moment an emissions doesn't, it will call onComplete() and dispose.

syntax: skipWhile(Predicate<T>)
Inverse of takeWhile.

### distinct()

syntax: distinct([mapping lambda])
Will emit each unique emission, but suppresses any duplicates. Equality is based on hashCode()/equals().

### distinctUntilChanged()

syntax: distinctUntilChanged([mapping lambda])
Ignores elements if they are equal to/map to the same value as the previously emitted value.
e.g. "1,1,1,2,3,1,1".distinctUntilChanged() = "1,2,3,1"

### elementAt()

syntax: elementAt(index: Long): Maybe<T>
Emits the emission with the specified index (the "index'th" emission), then calls onComplete().
Returns a Maybe because the number of emissions can be lower than the specified index.

Similar:
* elementAtOrError: returns a Single and calls onError when index is not found
* singleElement: turns Observable into a Maybe, but errors if there is more than 1 element
* firstElement
* lastElement

## Transforming Operators

### map()

syntax: map(Function<T,R>)
Transforms each emissions using the specified function.

### cast()

syntax: cast(Class)
Casts each object to the specified class

### startWith

syntax: startWith(emission: T)
Inserts the specified emission before all the others

similar: startWithArray(emissions: varargs T)
Inserts multiple emissions before all others

### DefaultIfEmpty()

syntax: defaultIfEmpty(emission: T)
Specify a default emission in case no emissions have ocurred before onComplete().

### switchIfEmpty()

syntax: switchIfEmpty(alternativeSource: Observable<T>)
Specify a different source of emissions in case the original source is empty.

### sorted()

syntax: sorted() / sorted(Comparator)
Collects all emissions and re-emits them in sorted order.
Only works with finite Observables of course.

### delay()

syntax: delay(period, timeunit)
Delays all emissions by the specified time period. Operates on a different Scheduler.

syntax: delay(observable: Observable)
Delays all emissions until the specified Observable starts emitting.

### repeat()

syntax: repeat(times: Long)
Repeats the emissions for the specified number of times after the initial onComplete().

syntax: repeat()
Repeats the emissions forever.

### scan()

syntax: scan([start,] (accumulator, next) -> ...)
Rolling aggregator: applies a function to each emission, sequentially, and emits each successive value.
e.g. rolling sum: "5,3,8".scan((acc, next) -> acc+next) = "5,8,16"

## Reducing operators

Consolidate an entire (finite) stream of emissions into a Single/Maybe emission.

### count()

syntax: count(): Single
Counts the numbers of emissions and emit that count

### reduce()

syntax: reduce([start,] (total, next) -> ...)): Single/Maybe
Accumulates all emissions into 1 final value. Returns a Single when given a starting value, and a Maybe when not.

### all()

syntax: all(condition: Predicate<T>): Single<Boolean>
Verifies that **all** emissions qualify the specified condition. Will emidiatly retuns _false_ if an emissions does not meet the condition.

### any()

syntax: all(condition: Predicate<T>): Single<Boolean>
Verifies that **at least one** emission qualify the specified condition. Will emidiatly retuns _true_ if an emissions meets the condition.

Observable.empty().any() = _false_

### contains()

syntax: contains(element: T): Single<Boolean>
Verifies that the specified element is emitted. Returns _true_ as soon as the element is encountered, then completes and disposes of itself.

## Collection operators

These operators accumulate all emissions into a collection.
**Avoid collectiong too early** as that undermines the _one-at-a-time_ nature of reactive programming.

### toList()

syntax: toList([capacityHint: Int]): Single<List<T>>
Collects all emissions into a list and emits that list as a Single.

### toSortedList()

syntax: toSortedList([Comparator])
Returns a sorted list containing all the emissions.

### toMap()

syntax: toMap(keyMapper: Function<T,K>): Single<Map<K,T>>
Collects emissions into a Map<K,T>, with the key derived from the emission using the supplied keyMapper function.
e.g. "Alpha", "Beta", "Gamma".tomap(s->s.charAt(0)) = {A=Alpha, B=Beta, G=Gamma}

syntax: toMap(keyMapper: Function<T,K>,valueMapper:Function<T,V>): Single<Map<K,V>>
Maps emissions to keys using the keyMapper and maps emissions to values using the valueMapper.

**Emissions that map to the same key will overwrite eachother!**
Use toMultiMap to map keys to lists of values.

### collect()

syntax: collect(constructor method reference, "add" method reference)
Collects results in a custom collection

## Error Recovery Operators

After onError the stream always terminates. Use recovery operators to attempt recovery.

### onErrorReturn() and onErrorReturnItem()

Specify the item to return when an error occurs
**Placement matters**: should be placed downstream of where the error occurs
**The stream will still end**. If you want to keep the stream going, use try/catch where the error occurs.

### onErrorResumeNext()

Give an Observable instead of an item to subscribe to after an error.

### retry()

retry(): retry every time
retry(numberOfTimes: Int): retry the specified number of times

## Action Operators

Helpful for debugging & getting visibility into the Observable chain

### doOnNext(), doOnComplete(), and doOnError()

Provide side-effects for each event, allow you to peek at each emission.
Do not affect the emissions.

doOnComplete to see when a point in the chain is done submitting.

### doOnSubscribe() and doOnDispose()

Does exactly what it says on the tin

### doOnSucces()

Same as above, but for Single and Maybe
