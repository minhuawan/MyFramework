package com.sun.jna;

public interface FromNativeConverter {
  Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext);
  
  Class<?> nativeType();
}


/* Location:              D:\files\slayTheSpire-export\!\com\sun\jna\FromNativeConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */