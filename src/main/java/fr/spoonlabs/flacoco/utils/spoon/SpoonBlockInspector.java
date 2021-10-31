package fr.spoonlabs.flacoco.utils.spoon;

import fr.spoonlabs.flacoco.api.result.Location;
import fr.spoonlabs.flacoco.core.config.FlacocoConfig;
import org.apache.log4j.Logger;
import spoon.Launcher;
import spoon.reflect.code.CtStatement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SpoonBlockInspector {

    private static final Logger logger = Logger.getLogger(SpoonBlockInspector.class);

    private FlacocoConfig config;

    public SpoonBlockInspector(FlacocoConfig config) {
        this.config = config;
    }

    public List<Location> getBlockLocations(StackTraceElement element) {
        // Find the corresponding source code file
        String path = element.getClassName().replace(".", "/") + ".java";
        for (String dir : config.getSrcJavaDir()) {
            if (new File(new File(dir), path).exists()) {
                path = new File(new File(dir), path).getAbsolutePath();
                break;
            }
        }

        // If we cannot find the source code file, we return an empty location list
        if (!new File(path).exists()) {
            logger.debug("No source code file was found for stack-trace element: " + element);
            return new ArrayList<>();
        }


        // Init spoon Launcher
        Launcher launcher = new Launcher();
        launcher.addInputResource(path);
        launcher.buildModel();
        launcher.addProcessor(new SpoonBlockLocationsFinder());

        // Process the source code to find the block of the given stack-trace element
        SpoonBlockLocationsFinder.fullyQualifiedClassName = element.getClassName();
        SpoonBlockLocationsFinder.lineNumber = element.getLineNumber();
        SpoonBlockLocationsFinder.found = null;
        launcher.process();

        if (SpoonBlockLocationsFinder.found == null) {
            logger.debug("No block was found for stack-trace element: " + element);
            return new ArrayList<>();
        }

        // Process the block
        List<Location> locations = new ArrayList<>();
        for (CtStatement ctStatement : SpoonBlockLocationsFinder.found.getStatements()) {
            if (ctStatement.getPosition().getLine() <= element.getLineNumber()) {
                locations.add(new Location(element.getClassName(), ctStatement.getPosition().getLine()));
            }
        }

        // Clean results
        SpoonBlockLocationsFinder.found = null;

        return locations;
    }

}