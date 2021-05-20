# FlowChart
[![](https://jitpack.io/v/axel7083/FlowChart.svg)](https://jitpack.io/#axel7083/FlowChart)
![Screenshot](https://github.com/axel7083/FlowChart/blob/main/Screenshot.jpg)

The goal of this library is to provide an easy to use system for visual programming, and easy configuration. The repository contains an simple application example, showing how to use the library with 3 nodes (display, value, and sum).

# Download

### Gradle

Add it in your root `build.gradle` at the end of repositories:
```gradle
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
 ```
Then add the dependency
 ```gradle
dependencies {
	 implementation 'com.github.axel7083:FlowChart:Tag'
}
 ```

### Maeven
Add the JitPack repository to your build file
 ```Maeven
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories> 
 ```
Add the dependency
```
<dependency>
 <groupId>com.github.axel7083</groupId>
 <artifactId>FlowChart</artifactId>
 <version>Tag</version>
</dependency>
```

# Under the Hood

The FlowGraph class is extending the ViewGroup class.

# Documentation

Todo

# Todo

A lot.

