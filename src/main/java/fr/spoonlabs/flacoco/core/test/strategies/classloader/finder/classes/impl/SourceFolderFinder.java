package fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.classes.impl;

import fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.classes.ClassFinder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SourceFolderFinder implements ClassFinder {

    private String srcFolder;

    public SourceFolderFinder(String srcFolder) {
        this.srcFolder = srcFolder;
    }

    @Override
    public String[] getClasses() {
        return getClassesLoc(new File(srcFolder), null).toArray(new String[0]);
    }

    // TODO: make this prettier
    static List<String> getClassesLoc(File testSrcFolder, String pack) {
        List<String> classes = new ArrayList<>();
        if (!testSrcFolder.isDirectory()) {
            if (testSrcFolder.getName().endsWith(".java") || testSrcFolder.getName().endsWith(".class")) {
                String className = pack == null ? testSrcFolder.getName() : pack + '.' + testSrcFolder.getName();
                classes.add(className);
            }
        }

        for (File file : testSrcFolder.listFiles()) {
            if (file.isDirectory())
                classes.addAll(getClassesLoc(file, pack == null ? file.getName() : pack + '.' + file.getName()));
            else if (file.getName().endsWith(".java")) {
                String className = pack == null ? file.getName() : pack + '.' + file.getName();
                classes.add(className);
            } else if (file.getName().endsWith(".class")) {
                String className = pack == null ? file.getName() : pack + '.' + file.getName();
                classes.add(className);
            }
        }
        return classes;
    }
}
