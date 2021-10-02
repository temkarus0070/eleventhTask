package di.configuration;

public class JavaBeanConfigurationImpl implements  JavaBeanConfiguration{
    private String packageName;
    public JavaBeanConfigurationImpl(String packageName) {
        this.packageName = packageName;
        this.packageName=this.packageName.replace(".","/");
    }

    @Override
    public String getPackageName() {
        return packageName;
    }
}
