package com.badlogic.gdx.controllers;

import com.badlogic.gdx.math.Vector3;

public interface Controller {
  boolean getButton(int paramInt);
  
  float getAxis(int paramInt);
  
  PovDirection getPov(int paramInt);
  
  boolean getSliderX(int paramInt);
  
  boolean getSliderY(int paramInt);
  
  Vector3 getAccelerometer(int paramInt);
  
  void setAccelerometerSensitivity(float paramFloat);
  
  String getName();
  
  void addListener(ControllerListener paramControllerListener);
  
  void removeListener(ControllerListener paramControllerListener);
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\Controller.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */