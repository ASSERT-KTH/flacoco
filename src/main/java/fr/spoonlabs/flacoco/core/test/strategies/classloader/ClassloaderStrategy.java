package fr.spoonlabs.flacoco.core.test.strategies.classloader;

import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import fr.spoonlabs.flacoco.core.test.TestContext;
import fr.spoonlabs.flacoco.core.test.strategies.TestDetectionStrategy;
import fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.CustomClassLoaderThreadFactory;
import fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.TestFinderRunner;
import org.apache.log4j.Logger;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Runs a new thread with the specified test binary directories as the classpath.
 * <p>
 * Scans all available classes and filters them according to the JUnit specification.
 */
public class ClassloaderStrategy implements TestDetectionStrategy {

    private Logger logger = Logger.getLogger(ClassloaderStrategy.class);

    private FlacocoConfig config;

    public ClassloaderStrategy(FlacocoConfig config) {
        this.config = config;
    }

    @Override
    public List<TestContext> findTests() {
        try {
            // Build urlClassLoader for new thread
            URLClassLoader urlClassLoader = new URLClassLoader(getUrls());

            // Run the new thread with the finder
            ThreadFactory threadFactory = new CustomClassLoaderThreadFactory(urlClassLoader);
            ExecutorService executor = Executors.newSingleThreadExecutor(threadFactory);
            Future<List<TestContext>> future = executor.submit(new TestFinderRunner(config));
            executor.shutdown();

            return future.get();
        } catch (InterruptedException | ExecutionException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private URL[] getUrls() throws MalformedURLException {
        List<URL> urls = new ArrayList<>();
        // Build the classloader for the new thread
        for (String dir : config.getBinJavaDir()) {
            urls.add(new File(dir).toURI().toURL());
        }
        for (String dir : config.getBinTestDir()) {
            urls.add(new File(dir).toURI().toURL());
        }
        if (!config.getClasspath().isEmpty()) {
            for (String dir : config.getClasspath().split(File.pathSeparator)) {
                urls.add(new File(dir).toURI().toURL());
            }
        }
        return urls.toArray(new URL[0]);
    }
}
