package com.badlogic.gdx.tools.hiero.unicodefont.effects;

import java.util.List;

public interface ConfigurableEffect extends Effect {
  List getValues();
  
  void setValues(List paramList);
  
  public static interface Value {
    String getName();
    
    void setString(String param1String);
    
    String getString();
    
    Object getObject();
    
    void showDialog();
  }
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\hier\\unicodefont\effects\ConfigurableEffect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */