package com.badlogic.gdx.controllers;

import com.badlogic.gdx.utils.Array;

public interface ControllerManager {
  Array<Controller> getControllers();
  
  void addListener(ControllerListener paramControllerListener);
  
  void removeListener(ControllerListener paramControllerListener);
  
  Array<ControllerListener> getListeners();
  
  void clearListeners();
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\ControllerManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */