# 1 - Thinking Reactively 

## A brief history

 * Reactive programming = functional, event-driven programming
 * Started in .NET: Reactive Exensions = Rx
 * **Core idea: events are data and data are events**
 	* Merge events and data to model a world in motion
 
 ## Thinking reactively
 
 * Model code as multiple concurrent streams of events/data happening at the same time
 
 ## A quick exposure to RxJava
 
 * _Observables_ push data to _Observers_
 * Use _Operators_ between Observables and Observers to transform and manipulate emissions
 	* Observable -> _Operator_ -> Observable
	
* Difference with streams/sequences
	* Observables are **push-based**
	* Streams/sequences are **pull-based**

* Working push-based allows us to work with data **and events**
	* Eg. an Observable that emits an _event_ every second	
