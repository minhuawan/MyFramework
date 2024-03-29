package com.google.gson.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface JsonAdapter {
  Class<?> value();
}


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\annotations\JsonAdapter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */