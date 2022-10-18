package com.badlogic.gdx.graphics.g3d.model.data;

import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class ModelNode {
  public String id;
  
  public Vector3 translation;
  
  public Quaternion rotation;
  
  public Vector3 scale;
  
  public String meshId;
  
  public ModelNodePart[] parts;
  
  public ModelNode[] children;
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\model\data\ModelNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */