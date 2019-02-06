# Puzzle


### How to use

```sh
$ git clone ...
$ cd project
$ ./gralew build
```

target - project/build/libs/org.naumen.jar

```java
import org.naumen.Puzzle;

...

Puzzle puz = new Puzzle();
int[] moves = puz.resolve(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 });
```