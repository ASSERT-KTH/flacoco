# flacoco · [![Build Status](https://travis-ci.com/SpoonLabs/flacoco.svg?branch=master)](https://travis-ci.com/SpoonLabs/flacoco) [![codecov](https://codecov.io/gh/SpoonLabs/flacoco/branch/master/graph/badge.svg?token=7WWFGI1KWW)](https://codecov.io/gh/SpoonLabs/flacoco)

**flacoco** is a fault localization tool for Java based on [Jacoco](https://github.com/jacoco/jacoco) and [test-runner](https://github.com/STAMP-project/test-runner) that aims at emcompassing several fault localization techniques.

* **Ease of use**: With an intuitive, reliable and stable API, flacoco can be easily used in other projects such as automated program repair tools.
* **Compatibility**: Supports JUnit3, Junit4 and JUni5. Supports Java 8 to 16. Supports Linux, MacOS and Windows.
* **Stability**: Tests are executed in an isolated JVM.

## Installation

flacoco is currently only available as a SNAPSHOT. 

You can use it by installing locally yourself:
```bash
mvn install
```

Or by adding flacoco as a maven dependency:

```xml
<dependency>
  <groupId>com.github.spoonlabs</groupId>
  <artifactId>flacoco</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```


## Documentation

flacoco is currently available through it's [API](src/main/java/fr/spoonlabs/flacoco/api/Flacoco.java) and configurable through [FlacocoConfig](src/main/java/fr/spoonlabs/flacoco/core/config/FlacocoConfig.java).

Example usage:
```java
Flacoco flacoco = new Flacoco();
FlacocoConfig config = FlacocoConfig.getInstance();

config.setProjectPath("./examples/exampleFL1/FLtest1");
config.setClasspath(classpath);
config.setFamily(FlacocoConfig.FaultLocalizationFamily.SPECTRUM_BASED);
config.setSpectrumFormula(SpectrumFormula.OCHIAI);

Map<String, Double> susp = flacoco.runDefault();
```

A more extensive listing of configurable options, as well as detailed explanation of flacoco use-cases, features and limitation will be soon available in a dedicated wiki.

## Contributing

### License

[MIT License](LICENSE)

### Pull requests

External contributions are welcome.

## Related tools

* https://github.com/SpoonLabs/CoCoSpoon/
* https://github.com/GZoltar/gzoltar/
* https://github.com/saeg/jaguar
