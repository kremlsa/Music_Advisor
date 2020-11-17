class SuperClassGetter {

    public Class getSuperClassByName(String name) throws ClassNotFoundException {
        // write your code here
        Class myClass = Class.forName(name);   
        return myClass.getSuperclass();
    }

    public Class getSuperClassByInstance(Object object) {
        // write your code here
        return object.getClass().getSuperclass();
    }
}
