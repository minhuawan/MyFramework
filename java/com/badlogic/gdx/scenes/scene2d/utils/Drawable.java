package com.badlogic.gdx.scenes.scene2d.utils;

import com.badlogic.gdx.graphics.g2d.Batch;

public interface Drawable {
  void draw(Batch paramBatch, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  float getLeftWidth();
  
  void setLeftWidth(float paramFloat);
  
  float getRightWidth();
  
  void setRightWidth(float paramFloat);
  
  float getTopHeight();
  
  void setTopHeight(float paramFloat);
  
  float getBottomHeight();
  
  void setBottomHeight(float paramFloat);
  
  float getMinWidth();
  
  void setMinWidth(float paramFloat);
  
  float getMinHeight();
  
  void setMinHeight(float paramFloat);
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\Drawable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */