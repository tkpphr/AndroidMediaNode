# Android MediaNode
<a target="_blank" href="https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#ICE_CREAM_SANDWICH"><img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat" alt="API" /></a>

## Overview
The tree node that has image and sound.

## Installation

### Gradle
1. Add repository url to build.gradle(Project)
```groovy
allprojects {
    repositories {
        ...
        maven {
            ...
            url 'https://www.myget.org/F/tkpphr-android-feed/maven/'
        }
    }
}
```

2. Add dependency to build.gradle(Module)
```groovy
dependencies {
    ...
    implementation 'com.tkpphr.android:media-node:1.0.0'
    implementation 'com.android.support:appcompat-v7:27.1.1'
    implementation 'com.android.support:design:27.1.1'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
}
```

or

### Maven
```xml
...
<repositories>
  ...
  <repository>
    <id>tkpphr-android-feed</id>
    <url>https://www.myget.org/F/tkpphr-android-feed/maven/</url>
  </repository>
</repositories>
...
<dependencies>
  ...
  <dependency>
    <groupId>com.tkpphr.android</groupId>
    <artifactId>media-node</artifactId>
    <version>1.0.0</version>
    <type>aar</type>
  </dependency>
  <dependency>
    <groupId>com.android.support</groupId>
    <artifactId>appcompat-v7</artifactId>
    <version>27.1.1</version>
    <type>aar</type>
  </dependency>
  <dependency>
    <groupId>com.android.support</groupId>
    <artifactId>design</artifactId>
    <version>27.1.1</version>
    <type>aar</type>
  </dependency>
  <dependency>
    <groupId>com.android.support.constraint</groupId>
    <artifactId>constraint</artifactId>
    <version>1.1.3</version>
    <type>aar</type>
  </dependency>
</dependencies>
...
```

## Usage
1. Create the class that implemented "com.tkpphr.android.medianode.core.MediaNodeSound" interface.
2. Create the class that inherited "com.tkpphr.android.medianode.core.BaseMediaNode" class.
3. Use it.

See details at a demo project.

## License
Released under the Apache 2.0 License.
See LICENSE File.