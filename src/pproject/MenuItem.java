package pproject;

import java.lang.reflect.Method;


//Helper class for designing the menus
//The title is the info that is printed to the console
//The other data are needed in order to invoke the method provided
public class MenuItem {
    String title;
    String targetMethod;
    Object obj;
    Object arg;
    Object arg2;

    public MenuItem(String title) {
        this.title = title;
    }

    public MenuItem(String title, String targetMethod, Object obj) {
        this.title = title;
        this.targetMethod = targetMethod;
        this.obj = obj;
    }

    public MenuItem(String title, String targetMethod, Object obj, Object arg, Object arg2) {
        this.title = title;
        this.targetMethod = targetMethod;
        this.obj = obj;
        this.arg = arg;
        this.arg2 = arg2;
    }

    public MenuItem(String title, String targetMethod, Object obj, Object arg) {
        this.title = title;
        this.targetMethod = targetMethod;
        this.obj = obj;
        this.arg = arg;
    }

    public void setArg(Object arg) {
        this.arg = arg;
    }

    //Executed the target method of obj object with any arguments provided
    public void execute() {
        if (targetMethod == null) return;
        try {
            if (arg == null) {
                Method method = this.obj.getClass().getMethod(targetMethod);
                method.invoke(this.obj);
            } else if (arg2 == null) {
                Method method = this.obj.getClass().getMethod(targetMethod, arg.getClass());
                method.invoke(this.obj, arg);
            } else {
                Method method = this.obj.getClass().getMethod(targetMethod, arg.getClass(), arg2.getClass());
                method.invoke(this.obj, arg, arg2);
            }
        } catch (Exception e) {
            System.out.println("Couldn't call " + targetMethod + ": " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return this.title;
    }
}
