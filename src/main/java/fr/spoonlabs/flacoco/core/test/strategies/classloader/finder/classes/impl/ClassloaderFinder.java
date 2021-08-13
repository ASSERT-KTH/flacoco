package fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.classes.impl;

import fr.spoonlabs.flacoco.core.test.strategies.classloader.finder.classes.ClassFinder;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ClassloaderFinder implements ClassFinder {

    public URLClassLoader urlClassloader;

    public ClassloaderFinder(URLClassLoader urlClassloader) {
        this.urlClassloader = urlClassloader;
    }

    @Override
    public String[] getClasses() {
        List<String> classes = new ArrayList<>();
        for (URL url : urlClassloader.getURLs()) {
            classes.addAll(SourceFolderFinder.getClassesLoc(new File(url.getPath())));
        }
        return classes.toArray(new String[0]);
    }

}
