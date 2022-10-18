package com.google.gson;

import java.lang.reflect.Type;

public interface JsonDeserializationContext {
  <T> T deserialize(JsonElement paramJsonElement, Type paramType) throws JsonParseException;
}


/* Location:              D:\files\slayTheSpire-export\!\com\google\gson\JsonDeserializationContext.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */