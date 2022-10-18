package com.badlogic.gdx;

public interface ApplicationLogger {
  void log(String paramString1, String paramString2);
  
  void log(String paramString1, String paramString2, Throwable paramThrowable);
  
  void error(String paramString1, String paramString2);
  
  void error(String paramString1, String paramString2, Throwable paramThrowable);
  
  void debug(String paramString1, String paramString2);
  
  void debug(String paramString1, String paramString2, Throwable paramThrowable);
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\ApplicationLogger.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */