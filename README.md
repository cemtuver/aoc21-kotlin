# [Advent of Code 2021](https://adventofcode.com/2021) in Kotlin

## Prerequisites
[Kotlin command-line compiler](https://kotlinlang.org/docs/command-line.html) is required to build the solutions. It can be installed from [here](https://github.com/JetBrains/kotlin/releases/tag/v1.6.0) manually or using Homebrew.

```
$ brew update
$ brew install kotlin
```

## Build & Run
Compile the source code using Kotlin command-line compiler and then run the output jar using Java.

```
$ cd day-XX
$ kotlinc part-X.kt -include-runtime -d part-X.jar
$ java -jar part-X.jar
```
