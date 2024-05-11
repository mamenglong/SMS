package java.beans;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;

public interface BeanInfo {

    BeanDescriptor getBeanDescriptor();

    PropertyDescriptor[] getPropertyDescriptors();
}
