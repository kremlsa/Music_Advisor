import java.lang.reflect.*;

class MethodFinder {

    public static String findMethod(String methodName, String[] classNames) throws ClassNotFoundException {
        for (String c : classNames) {
            Method[] methods = Class.forName(c).getMethods();
            for (Method m : methods) {
                if (m.getName().equals(methodName)) {
                    return Class.forName(c).getName();
                }
            }
        }
        return null;

    }
}
