package com.badlogic.gdx.graphics.g2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Disposable;

public interface Batch extends Disposable {
  public static final int X1 = 0;
  
  public static final int Y1 = 1;
  
  public static final int C1 = 2;
  
  public static final int U1 = 3;
  
  public static final int V1 = 4;
  
  public static final int X2 = 5;
  
  public static final int Y2 = 6;
  
  public static final int C2 = 7;
  
  public static final int U2 = 8;
  
  public static final int V2 = 9;
  
  public static final int X3 = 10;
  
  public static final int Y3 = 11;
  
  public static final int C3 = 12;
  
  public static final int U3 = 13;
  
  public static final int V3 = 14;
  
  public static final int X4 = 15;
  
  public static final int Y4 = 16;
  
  public static final int C4 = 17;
  
  public static final int U4 = 18;
  
  public static final int V4 = 19;
  
  void begin();
  
  void end();
  
  void setColor(Color paramColor);
  
  void setColor(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  void setColor(float paramFloat);
  
  Color getColor();
  
  float getPackedColor();
  
  void draw(Texture paramTexture, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2);
  
  void draw(Texture paramTexture, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean1, boolean paramBoolean2);
  
  void draw(Texture paramTexture, float paramFloat1, float paramFloat2, int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  void draw(Texture paramTexture, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8);
  
  void draw(Texture paramTexture, float paramFloat1, float paramFloat2);
  
  void draw(Texture paramTexture, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  void draw(Texture paramTexture, float[] paramArrayOffloat, int paramInt1, int paramInt2);
  
  void draw(TextureRegion paramTextureRegion, float paramFloat1, float paramFloat2);
  
  void draw(TextureRegion paramTextureRegion, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
  
  void draw(TextureRegion paramTextureRegion, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9);
  
  void draw(TextureRegion paramTextureRegion, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, float paramFloat7, float paramFloat8, float paramFloat9, boolean paramBoolean);
  
  void draw(TextureRegion paramTextureRegion, float paramFloat1, float paramFloat2, Affine2 paramAffine2);
  
  void flush();
  
  void disableBlending();
  
  void enableBlending();
  
  void setBlendFunction(int paramInt1, int paramInt2);
  
  int getBlendSrcFunc();
  
  int getBlendDstFunc();
  
  Matrix4 getProjectionMatrix();
  
  Matrix4 getTransformMatrix();
  
  void setProjectionMatrix(Matrix4 paramMatrix4);
  
  void setTransformMatrix(Matrix4 paramMatrix4);
  
  void setShader(ShaderProgram paramShaderProgram);
  
  ShaderProgram getShader();
  
  boolean isBlendingEnabled();
  
  boolean isDrawing();
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\Batch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */