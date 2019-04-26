# AndroidDevMetrics
(formerly dagger2metrics)

Performance metrics library for Android development. 

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-AndroidDevMetrics-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3120)

The problem with performance is that it often decreases slowly so in day-by-day development it's hard to notice that our app (or Activity or any other view) launches 50ms longer. And another 150ms longer, and another 100ms...

With **AndroidDevMetrics** you will be able to see how performant are the most common operations like object initialization (in Dagger 2 graph), or Activity lifecycle methods (`onCreate()`, `onStart()`, `onResume()`).

It won't show you exact reason of performance issues or bottlenecks (yet!) but it can point out where you should start looking first. 

AndroidDevMetrics currently includes:

* Activity lifecycle metrics - metrics for lifecycle methods execution (`onCreate()`, `onStart()`, `onResume()`)
* Activity lifecycle methods tracing without app recompiling
* Frame rate drops - metrics for fps drops for each of screens (activity)
* Dagger 2 metrics - metrics for objects initialization in Dagger 2 

![screenshot1.png](https://raw.githubusercontent.com/frogermcs/androiddevmetrics/master/art/activities_metrics.png)

![screenshot.png](https://raw.githubusercontent.com/frogermcs/androiddevmetrics/master/art/dagger2_metrics.png)

## Getting started

Script below shows how to enable all available metrics.

In your `build.gradle`:

```gradle
 buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath 'com.frogermcs.androiddevmetrics:androiddevmetrics-plugin:0.7'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'com.frogermcs.androiddevmetrics'
```

In your `Application` class:

```java
public class ExampleApplication extends Application {

 @Override
 public void onCreate() {
     super.onCreate();
     //Use it only in debug builds
     if (BuildConfig.DEBUG) {
         AndroidDevMetrics.initWith(this);
     }
  }
 }
```

## How does it work?

Detailed description how it works under the hood can be found on wiki pages:

* [Activity lifecycle and frame drops metrics](https://github.com/frogermcs/AndroidDevMetrics/wiki/Activity-lifecycle-metrics)
* [Activity lifecycle methods tracing](http://frogermcs.github.io/androiddevmetrics-activity-lifecycle-methods-tracing/)
* [Dagger 2 metrics](https://github.com/frogermcs/AndroidDevMetrics/wiki/Dagger-2-metrics)

## I found performance issue, what should I do next?

There is no silver bullet for performance issues but here are a couple steps which can help you with potential bugs hunting.

If measured time of object initialization or method execution looks suspicious you should definitely give a try to [TraceView](http://developer.android.com/tools/debugging/debugging-tracing.html). This tool logs method execution over time and shows execution data, per-thread timelines, and call stacks. Practical example of TraceView usage can be found in this blog post: [Measuring Dagger 2 graph creation performance](http://frogermcs.github.io/dagger-graph-creation-performance/]).

---

If it seems that layout or view can be a reason of performance issue you should start with those links from official Android documentation:

* http://developer.android.com/training/improving-layouts/index.html
* http://developer.android.com/training/improving-layouts/optimizing-layout.html

--- 

Finally, if you want to understand where most of performance issues come from, here *is a collection of videos focused entirely on helping developers write faster, more performant Android Applications.*

* [Android Performance Patterns](https://www.youtube.com/playlist?list=PLWz5rJ2EKKc9CBxr3BVjPTPoDPLdPIFCE)

## Example app

You can check [GithubClient](https://github.com/frogermcs/githubclient) - example Android app which shows how to use Dagger 2. Most recent version uses **AndroidDevMetrics** for measuring performance.

## Building AndroidDevMetrics

Build AndroidDevMetrics plugin with [`./gradlew clean build`]. The tests can be run with
`./gradlew clean test`. To install the plugin in your local maven repository (usually located at
`~/.m2/repository`) use `./gradlew clean install`. You can change `VERSION_NAME` value in `gradle.properties`
to easily recognise your version.

## License

    Copyright 2016 Miroslaw Stanek

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
