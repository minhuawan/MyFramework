package com.google.gson;

import java.lang.reflect.Type;

public interface JsonSerializationContext {
  JsonElement serialize(Object paramObject);
  
  JsonElement serialize(Object paramObject, Type paramType);
}


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\JsonSerializationContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */