package com.badlogic.gdx.graphics.g3d.model.data;

import com.badlogic.gdx.math.Vector2;

public class ModelTexture {
  public static final int USAGE_UNKNOWN = 0;
  
  public static final int USAGE_NONE = 1;
  
  public static final int USAGE_DIFFUSE = 2;
  
  public static final int USAGE_EMISSIVE = 3;
  
  public static final int USAGE_AMBIENT = 4;
  
  public static final int USAGE_SPECULAR = 5;
  
  public static final int USAGE_SHININESS = 6;
  
  public static final int USAGE_NORMAL = 7;
  
  public static final int USAGE_BUMP = 8;
  
  public static final int USAGE_TRANSPARENCY = 9;
  
  public static final int USAGE_REFLECTION = 10;
  
  public String id;
  
  public String fileName;
  
  public Vector2 uvTranslation;
  
  public Vector2 uvScaling;
  
  public int usage;
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\model\data\ModelTexture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */