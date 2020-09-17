# flacoco

Fault localization for Java based on Jacoco and STAMP-project's [test-runner](https://github.com/STAMP-project/test-runner)

The package `fr.spoonlabs.test_framework` was taken from DSpoon. The main two reasons are: 1) to avoid importing the complete DSpot project and all its dependencies. 2) To change it according to our needs: e.g., classify each test according to the testing framework to automatically choose the test runner. 



## TODOS:

- [ ] Cover tests that throw Exceptions.
- [ ] Automated selection of test runner.
