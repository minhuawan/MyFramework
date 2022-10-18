package com.badlogic.gdx;

import java.util.Map;

public interface Preferences {
  Preferences putBoolean(String paramString, boolean paramBoolean);
  
  Preferences putInteger(String paramString, int paramInt);
  
  Preferences putLong(String paramString, long paramLong);
  
  Preferences putFloat(String paramString, float paramFloat);
  
  Preferences putString(String paramString1, String paramString2);
  
  Preferences put(Map<String, ?> paramMap);
  
  boolean getBoolean(String paramString);
  
  int getInteger(String paramString);
  
  long getLong(String paramString);
  
  float getFloat(String paramString);
  
  String getString(String paramString);
  
  boolean getBoolean(String paramString, boolean paramBoolean);
  
  int getInteger(String paramString, int paramInt);
  
  long getLong(String paramString, long paramLong);
  
  float getFloat(String paramString, float paramFloat);
  
  String getString(String paramString1, String paramString2);
  
  Map<String, ?> get();
  
  boolean contains(String paramString);
  
  void clear();
  
  void remove(String paramString);
  
  void flush();
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\Preferences.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */