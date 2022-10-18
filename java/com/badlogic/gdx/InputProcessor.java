package com.badlogic.gdx;

public interface InputProcessor {
  boolean keyDown(int paramInt);
  
  boolean keyUp(int paramInt);
  
  boolean keyTyped(char paramChar);
  
  boolean touchDown(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  boolean touchUp(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
  
  boolean touchDragged(int paramInt1, int paramInt2, int paramInt3);
  
  boolean mouseMoved(int paramInt1, int paramInt2);
  
  boolean scrolled(int paramInt);
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\InputProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */