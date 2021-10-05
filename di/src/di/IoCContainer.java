package di;

import di.configuration.JavaBeanConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.module.ModuleFinder;
import java.net.URL;
import java.util.*;
import java.lang.reflect.*;
import java.util.stream.Collectors;

public class IoCContainer {

    private static   Map<Class,Class> instancesMap;
    private Object scanObject;
    private JavaBeanConfiguration javaBeanConfiguration;
    public IoCContainer(JavaBeanConfiguration javaBeanConfiguration){
        this.javaBeanConfiguration=javaBeanConfiguration;
        if(instancesMap==null){
            instancesMap=new HashMap<>();
        }
        scan();
    }

    private void scan(){
        List<File> urls = new ArrayList<File>();
        String javaClassPath = System.getProperty("java.class.path");
        if (javaClassPath != null) {
            for (String path : javaClassPath.split(File.pathSeparator)) {
                urls.add(new File(path));
            }
        }
        ClassLoader classLoader=ClassLoader.getPlatformClassLoader();
        Package[] packages=classLoader.getDefinedPackages();
        try {
            for (Iterator<URL> it = classLoader.getResources("di").asIterator(); it.hasNext(); ) {
                URL url = it.next();


            }
        }
        catch (Exception ex){

        }
        Arrays.stream(Package.getPackages()).forEach(e-> scanPackage(e.getName()));

    }

    private void scanPackage(String packageName){

        packageName=packageName.replace(".","/");
        try {
           List<Class> classes= Arrays.stream(getClasses(packageName)).filter(e->e.getName().contains("ChatServiceSelector")).collect(Collectors.toList());
           if(classes.size()>0)
                System.out.println(classes.size());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private  Class[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }


    private  List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}
