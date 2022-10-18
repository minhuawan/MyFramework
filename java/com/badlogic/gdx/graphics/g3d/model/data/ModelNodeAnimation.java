package com.badlogic.gdx.graphics.g3d.model.data;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class ModelNodeAnimation {
  public String nodeId;
  
  public Array<ModelNodeKeyframe<Vector3>> translation;
  
  public Array<ModelNodeKeyframe<Quaternion>> rotation;
  
  public Array<ModelNodeKeyframe<Vector3>> scaling;
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\model\data\ModelNodeAnimation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */