# Dagger2Metrics
Performance metrics library for Dagger 2 initialization process.

If you use Dagger 2 for dependency injection in your Android app you probably know that it's super optimized and non-reflection piece of code served by great engineers from Google (and formerly form Square). 

Even with all those optimizations and fully generated non-dynamic code, still there are potential performance issues hidden in our code and all 3rd parties injected via Dagger 2. 

The problem with performance is that it often decreases slowly so in day-by-day development it's hard to notice that our app (or Activity or any other view) launches 50ms longer. And another 150ms longer, and another 100ms...

With **Dagger2Metrics** you will be able to see how much time was needed to initialize all requested dependencies (and dependencies of those dependencies).

![screenshot.png](https://raw.githubusercontent.com/frogermcs/dagger2metrics/master/art/dagger2metrics.png)

## Getting started

In your `build.gradle`:

```gradle
 buildscript {
  repositories {
    jcenter()
  }

  dependencies {
    classpath 'com.frogermcs.dagger2metrics:dagger2metrics-plugin:0.2'
  }
}

apply plugin: 'com.android.application'
apply plugin: 'com.frogermcs.dagger2metrics'
```

In your `Application` class:

```java
public class ExampleApplication extends Application {

 @Override
 public void onCreate() {
     super.onCreate();
     //Use it only in debug builds
     if (BuildConfig.DEBUG) {
         Dagger2Metrics.enableCapturing(this);
     }
  }
 }
```

And that's all. In your app you will see notification which opens simple summary of all finished initializations.

![notification.png](https://raw.githubusercontent.com/frogermcs/dagger2metrics/master/art/dagger2metrics-notification.png)

## How does it work?

Dagger2Metrics captures all initializations from both - `@Module` -> `@Provides` annotated methods and `@Inject` annotated constructors.

In summary you will see the most-top injected dependencies with trees of their dependencies. Each of dependency shows how much time was needed to provide this object to Dagger 2 object graph (construction time itself and overall time with all dependencies).

![screenshot.png](https://raw.githubusercontent.com/frogermcs/dagger2metrics/master/art/dagger2metrics.png)

### Why I don't see all (sub) dependencies?
Metric trees don't show dependencies which are already provided to Dagger's graph, so only those constructed from scratch will be visible. Mainly because of readability and from a simple reason - we don't want to measure Dagger 2 performance which in most cases won't be an issue.  
Instead we should be sure that our code provides requested dependencies as fast as it's possible.

## Customizing

Dagger2Metrics has three default levels of warnings:

```java
Dagger2Metrics.WARNING_1_LIMIT_MILLIS // 30ms
Dagger2Metrics.WARNING_2_LIMIT_MILLIS // 50ms
Dagger2Metrics.WARNING_3_LIMIT_MILLIS // 100ms
```

You can adjust them to your needs.

## Example app

You can check [GithubClient](https://github.com/frogermcs/githubclient) project  - example Android app which shows how to use Dagger 2. Most recent version uses *Dagger2Metrics* for measuring construction times.

## More about Dagger 2
If you're just starting with Dagger 2, here is the list of resources which can help you with it:

[GithubClient](https://github.com/frogermcs/githubclient) - example of Github API client implemented on top of Dagger 2 DI framework. 

Blog posts:

- [Dagger 1 to 2 migration process](http://frogermcs.github.io/dagger-1-to-2-migration/)
- [Introdution to Dependency Injection](http://frogermcs.github.io/dependency-injection-with-dagger-2-introdution-to-di/)
- [Dagger 2 API](http://frogermcs.github.io/dependency-injection-with-dagger-2-the-api/)
- [Dagger 2 - custom scopes](http://frogermcs.github.io/dependency-injection-with-dagger-2-custom-scopes/)
- [Dagger 2 - graph creation performance](http://frogermcs.github.io/dagger-graph-creation-performance/)

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
