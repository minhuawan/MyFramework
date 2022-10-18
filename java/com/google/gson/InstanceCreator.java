package com.google.gson;

import java.lang.reflect.Type;

public interface InstanceCreator<T> {
  T createInstance(Type paramType);
}


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\InstanceCreator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */