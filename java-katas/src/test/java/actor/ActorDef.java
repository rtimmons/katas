package actor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

class ActorDef {

    private final String clazz;
    private final Map<String,Object> attributes;

    ActorDef(String clazz, Map<String, Object> attributes) {
        this.clazz = clazz;
        this.attributes = attributes;
    }

//    ActorDef wrap(String clazz, Map<String,Object> attributes, String child) {
//        Map<String,Object> cloned = new LinkedHashMap<>(attributes);
//        cloned.put(child, this);
//        return new ActorDef(clazz, cloned);
//    }

    private IActor instance = null;
    private boolean loaded = false;

    public IActor resolve() {
        if (!loaded) {
            this.instance = this.resolveInstance();
            this.loaded = true;
        }
        return this.instance;
    }

    private IActor resolveInstance() {
        String fqcn = "actor." + this.clazz;
        try {
            Class<?> c = Class.forName(fqcn);
            Object instance = c.newInstance();
            BeanInfo info = Introspector.getBeanInfo(c);
            for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
                String name = descriptor.getName();
                if (attributes.containsKey(name)) {
                    Method m = descriptor.getWriteMethod();
                    Object value = attributes.get(name);
                    try {
                        m.invoke(instance, value);
                    }
                    catch(IllegalArgumentException ex) {
                        String message = String.format(
                                "Wanted type %s but got %s for value=[%s]",
                                Arrays.toString(m.getParameterTypes()),
                                value.getClass(),
                                value
                        );
                        throw new RuntimeException(message);
                    }
                }
            }
            return (IActor)instance;
        } catch (ClassNotFoundException|IntrospectionException|IllegalAccessException|InstantiationException|InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
