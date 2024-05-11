package java.beans;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;

public final class GenericBeanInfo implements BeanInfo {

    protected final java.beans.BeanDescriptor _bean;
    protected final PropertyDescriptor[] _properties;

    GenericBeanInfo(java.beans.BeanDescriptor bean, PropertyDescriptor[] properties) {
        _bean = bean;
        _properties = properties;
    }

    public BeanDescriptor getBeanDescriptor() {
        return _bean;
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        return _properties;
    }
}
