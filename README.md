# cs test exam by pavel kruchinin (pavel@kruchinin.info).

# Dependencies:

* Internet,
* Java Developer Kit (OpenJDK) >=1.8

# Build

Build in console from  project root directory (change slash to backslash for Windows):
```
./gradlew clean test distZip
unzip  ./build/distributions/* -d ./build
```

# Run

Run in console from  project root directory (change slash to backslash for Windows):
```
java -jar build/cs-1.0-SNAPSHOT/cs-1.0-SNAPSHOT.jar
```