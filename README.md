# flacoco · [![tests](https://github.com/SpoonLabs/flacoco/actions/workflows/tests.yml/badge.svg)](https://github.com/SpoonLabs/flacoco/actions/workflows/tests.yml) [![codecov](https://codecov.io/gh/SpoonLabs/flacoco/branch/master/graph/badge.svg?token=7WWFGI1KWW)](https://codecov.io/gh/SpoonLabs/flacoco) ![Supported Platforms](https://img.shields.io/badge/platforms-Linux%2C%20macOS%2C%20Windows-blue.svg) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.spoonlabs/flacoco/badge.svg)](https://mavenbadges.herokuapp.com/maven-central/com.github.spoonlabs/flacoco)


**flacoco** is a fault localization tool for Java based on [Jacoco](https://github.com/jacoco/jacoco) and [test-runner](https://github.com/STAMP-project/test-runner) that aims at encompassing several fault localization techniques.

* **Ease of use**: With an intuitive, reliable and stable API, flacoco can be easily used in other projects such as automated program repair tools.
* **Compatibility**: 
    * Supports JUnit3, Junit4 and JUnit5.
    * Supports Java 1 to Java 17 bytecode.
    * Runs on Java 8 to Java 17.
    * Runs on Linux, MacOS and Windows.
* **Stability**: Tests are executed in an isolated JVM.

If you use flacoco in academic research, please cite "[FLACOCO: Fault Localization for Java based on Industry-grade Coverage](http://arxiv.org/pdf/2111.12513)", Technical report, arXiv 2111.12513, 2021. 

```bibtex
@techreport{flacoco2021,
 title = {FLACOCO: Fault Localization for Java based on Industry-grade Coverage},
 year = {2021},
 author = {André Silva and Matias Martinez and Benjamin Danglot and Davide Ginelli and Martin Monperrus},
 url = {http://arxiv.org/pdf/2111.12513},
 number = {2111.12513},
 institution = {arXiv},
}
```

## Installation

flacoco is currently available through Maven Central and as a SNAPSHOT

You can use it by installing locally yourself (if you do so, the install version will be `1.0.6-SNAPSHOT`), or by adding flacoco as a maven dependency:
```bash
$ mvn install -DskipTests
```
```xml
<dependency>
    <groupId>com.github.spoonlabs</groupId>
    <artifactId>flacoco</artifactId>
    <version>1.0.5</version>
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
