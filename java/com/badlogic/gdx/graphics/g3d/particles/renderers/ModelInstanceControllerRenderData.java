package com.badlogic.gdx.graphics.g3d.particles.renderers;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.particles.ParallelArray;

public class ModelInstanceControllerRenderData extends ParticleControllerRenderData {
  public ParallelArray.ObjectChannel<ModelInstance> modelInstanceChannel;
  
  public ParallelArray.FloatChannel colorChannel;
  
  public ParallelArray.FloatChannel scaleChannel;
  
  public ParallelArray.FloatChannel rotationChannel;
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\renderers\ModelInstanceControllerRenderData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */