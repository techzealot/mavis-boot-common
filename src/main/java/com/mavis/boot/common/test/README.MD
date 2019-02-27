###1. jvm对注解的处理：
若直接注解的class不在类路径，则可以创建该注解修饰的类(此时并不加载注解的class)反射不到该注解;    
若直接注解的类在类路径，且引用了不在类路径的间接类，则会报运行时异常：java.lang.ArrayStoreException: sun.reflect.annotation.TypeNotPresentExceptionProxy