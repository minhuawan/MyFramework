package com.badlogic.gdx.controllers;

import com.badlogic.gdx.math.Vector3;

public interface ControllerListener {
  void connected(Controller paramController);
  
  void disconnected(Controller paramController);
  
  boolean buttonDown(Controller paramController, int paramInt);
  
  boolean buttonUp(Controller paramController, int paramInt);
  
  boolean axisMoved(Controller paramController, int paramInt, float paramFloat);
  
  boolean povMoved(Controller paramController, int paramInt, PovDirection paramPovDirection);
  
  boolean xSliderMoved(Controller paramController, int paramInt, boolean paramBoolean);
  
  boolean ySliderMoved(Controller paramController, int paramInt, boolean paramBoolean);
  
  boolean accelerometerMoved(Controller paramController, int paramInt, Vector3 paramVector3);
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\ControllerListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */