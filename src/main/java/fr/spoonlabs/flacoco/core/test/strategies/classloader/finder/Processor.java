package fr.spoonlabs.flacoco.core.test.strategies.classloader.finder;

import fr.spoonlabs.flacoco.core.test.method.TestMethod;
import fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.classes.ClassFinder;
import fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.classes.impl.ClassloaderFinder;
import fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.filters.TestMethodFilter;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Processor {

    private static final Logger logger = Logger.getLogger(Processor.class);

    private static final int CLASS_SUFFIX_LENGTH = ".class".length();
    private static final int JAVA_SUFFIX_LENGTH = ".java".length();

    private final TestMethodFilter tester;
    private final ClassFinder finder;

    public Processor(ClassFinder finder, TestMethodFilter tester) {
        this.tester = tester;
        this.finder = finder;
    }

    public List<TestMethod> process() {
        List<TestMethod> testMethods = new ArrayList<>();

        for (String className : finder.getClasses()) {
            if (!className.contains("$"))
                try {
                    Class<?> clazz = Class.forName(className);
                    if (clazz.isLocalClass() || clazz.isAnonymousClass()) {
                        continue;
                    }
                    testMethods.addAll(tester.acceptClass(clazz));
                } catch (ClassNotFoundException cnfe) {
                    try {
                        ClassLoader tmp = Thread.currentThread().getContextClassLoader();
                        Class<?> clazz = Class.forName(className, false, tmp);
                        if (clazz.isLocalClass() || clazz.isAnonymousClass()) {
                            continue;
                        }
                        testMethods.addAll(tester.acceptClass(clazz));
                    } catch (ClassNotFoundException cnfe2) {
                        Class<?> clazz = null;
                        try {
                            clazz = Class.forName(className, false, ((ClassloaderFinder) finder).urlClassloader);
                            if (clazz.isLocalClass() || clazz.isAnonymousClass()) {
                                continue;
                            }
                            testMethods.addAll(tester.acceptClass(clazz));
                        } catch (ClassNotFoundException e) {
                            logger.warn("ClassNotFoundException: " + className);
                            logger.warn(Arrays.toString(((ClassloaderFinder) finder).urlClassloader.getURLs()));
                        }
                    } catch (NoClassDefFoundError ncdfe) {
                        // ignore not instantiable classes
                    }
                } catch (NoClassDefFoundError ncdfe) {
                    // ignore not instantiable classes
                }
        }

        return testMethods;
    }

    private String classNameFromJava(String fileName) {
        String s = replaceFileSeparators(cutOffExtension(fileName, JAVA_SUFFIX_LENGTH));
        while (s.startsWith("."))
            s = s.substring(1);
        return s;
    }

    private boolean isJavaFile(String fileName) {
        return fileName.endsWith(".java");
    }

    private boolean isInnerClass(String className) {
        return className.contains("$");
    }

    private boolean isClassFile(String classFileName) {
        return classFileName.endsWith(".class");
    }

    private String classNameFromFile(String classFileName) {
        String s = replaceFileSeparators(cutOffExtension(classFileName, CLASS_SUFFIX_LENGTH));
        while (s.startsWith("."))
            s = s.substring(1);
        return s;
    }

    private String replaceFileSeparators(String s) {
        String result = s.replace(File.separatorChar, '.');
        if (File.separatorChar != '/') {
            result = result.replace('/', '.');
        }
        return result;
    }

    private String cutOffExtension(String classFileName, int length) {
        return classFileName.substring(0, classFileName.length()
                - length);
    }

}
