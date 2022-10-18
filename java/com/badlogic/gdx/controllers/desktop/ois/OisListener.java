package com.badlogic.gdx.controllers.desktop.ois;

public interface OisListener {
  void buttonPressed(OisJoystick paramOisJoystick, int paramInt);
  
  void buttonReleased(OisJoystick paramOisJoystick, int paramInt);
  
  void axisMoved(OisJoystick paramOisJoystick, int paramInt, float paramFloat);
  
  void povMoved(OisJoystick paramOisJoystick, int paramInt, OisJoystick.OisPov paramOisPov);
  
  void xSliderMoved(OisJoystick paramOisJoystick, int paramInt, boolean paramBoolean);
  
  void ySliderMoved(OisJoystick paramOisJoystick, int paramInt, boolean paramBoolean);
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\desktop\ois\OisListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */