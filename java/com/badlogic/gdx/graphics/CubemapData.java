package com.badlogic.gdx.graphics;

public interface CubemapData {
  boolean isPrepared();
  
  void prepare();
  
  void consumeCubemapData();
  
  int getWidth();
  
  int getHeight();
  
  boolean isManaged();
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\CubemapData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */