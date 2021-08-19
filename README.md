# flacoco · [![tests](https://github.com/SpoonLabs/flacoco/actions/workflows/tests.yml/badge.svg)](https://github.com/SpoonLabs/flacoco/actions/workflows/tests.yml) [![codecov](https://codecov.io/gh/SpoonLabs/flacoco/branch/master/graph/badge.svg?token=7WWFGI1KWW)](https://codecov.io/gh/SpoonLabs/flacoco) ![Supported Platforms](https://img.shields.io/badge/platforms-Linux%2C%20macOS%2C%20Windows-blue.svg)

**flacoco** is a fault localization tool for Java based on [Jacoco](https://github.com/jacoco/jacoco) and [test-runner](https://github.com/STAMP-project/test-runner) that aims at encompassing several fault localization techniques.

* **Ease of use**: With an intuitive, reliable and stable API, flacoco can be easily used in other projects such as automated program repair tools.
* **Compatibility**: 
    * Supports JUnit3, Junit4 and JUni5.
    * Supports Java 5 to Java 16 bytecode.
    * Runs on Java 8 to Java 16.
    * Runs on Linux, MacOS and Windows.
* **Stability**: Tests are executed in an isolated JVM.

## Installation

flacoco is currently only available as a SNAPSHOT.

You can use it by installing locally yourself, or by adding flacoco as a maven dependency:
```bash
$ mvn install
```
```xml
<dependency>
    <groupId>com.github.spoonlabs</groupId>
    <artifactId>flacoco</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```


## Documentation

The documentation lives in the Github wiki at <https://github.com/SpoonLabs/flacoco/wiki>

## Contributing

### License

[MIT License](LICENSE)

### Pull requests

External contributions are welcome.

## Related tools

* https://github.com/SpoonLabs/CoCoSpoon/
* https://github.com/GZoltar/gzoltar/
* https://github.com/saeg/jaguar
