# cs test exam by pavel kruchinin (pavel@kruchinin.info).

#dependencies:
Internet, Java >=1.8

#build & run
Build & run from console in project root directory(change slash to backslash for Windows):
```
./gradlew clean test distZip
unzip  ./build/distributions/* -d ./build
java -jar build/cs-1.0-SNAPSHOT/cs-1.0-SNAPSHOT.jar
```