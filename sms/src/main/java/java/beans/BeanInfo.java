package java.beans;

public interface BeanInfo {

    BeanDescriptor getBeanDescriptor();

    PropertyDescriptor[] getPropertyDescriptors();
}
