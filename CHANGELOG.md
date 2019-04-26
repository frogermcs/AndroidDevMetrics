Change Log
==========

Version 0.7 *(2019-04-26)*
--------------------------

* Maintenance: fixed Gradle warnings (Kudos AndrewHeyO, erykrutkowski!)
* Maintenance: Groovy plugin rewritten to Kotlin (Kudos skrzyneckik!)

Version 0.6 *(2018-02-12)*
----------------------------

* Maintenance: Updated dependencies
* Fix: Support for notifications on Android 26

Version 0.5 *(2017-04-05)*
----------------------------

* New: Allow custom extension to the library using Interceptor like pattern. (Thanks Amulya Khare)!

Version 0.4 *(2015-03-21)*
----------------------------

* New: Activities lifecycle methods tracing

Version 0.3.1 *(2015-03-06)*
----------------------------

* Update: Use Class.getName() instead of Class.getSimpleName() because of performance issues in anonymous classes
* Update: Don't measure Dagger 2 Producers monitor injections
* Update: Gradle config cleanup, added no-op lib version for release build variant

Version 0.3 *(2015-03-01)*
----------------------------

* **Project renamed to AndroidDevMetrics**

* New: Metrics for Activity lifecycle (measuring execution time of onCreate, onStart, onResume and layout)
* New: Metrics for frame drops
* Fix: NullPointerException for @Nullable 

Version 0.2.1 *(2015-02-02)* 
----------------------------

 * Fix: Overriding stats for non-singleton dependencies
 * Fix: ListView group indicator style


Version 0.2.0 *(2016-01-30)*
----------------------------

Initial release.
