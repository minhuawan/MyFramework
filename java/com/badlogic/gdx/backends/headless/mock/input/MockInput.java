/*     */ package com.badlogic.gdx.backends.headless.mock.input;
/*     */ 
/*     */ import com.badlogic.gdx.Input;
/*     */ import com.badlogic.gdx.InputAdapter;
/*     */ import com.badlogic.gdx.InputProcessor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MockInput
/*     */   implements Input
/*     */ {
/*     */   private InputProcessor mockInputProcessor;
/*     */   
/*     */   public float getAccelerometerX() {
/*  30 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAccelerometerY() {
/*  35 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAccelerometerZ() {
/*  40 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getGyroscopeX() {
/*  45 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getGyroscopeY() {
/*  50 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getGyroscopeZ() {
/*  55 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX() {
/*  60 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getX(int pointer) {
/*  65 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaX() {
/*  70 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaX(int pointer) {
/*  75 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY() {
/*  80 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getY(int pointer) {
/*  85 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaY() {
/*  90 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDeltaY(int pointer) {
/*  95 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTouched() {
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean justTouched() {
/* 105 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTouched(int pointer) {
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isButtonPressed(int button) {
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKeyPressed(int key) {
/* 120 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isKeyJustPressed(int key) {
/* 125 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void getTextInput(Input.TextInputListener listener, String title, String text, String hint) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOnscreenKeyboardVisible(boolean visible) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void vibrate(int milliseconds) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void vibrate(long[] pattern, int repeat) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void cancelVibrate() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public float getAzimuth() {
/* 155 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPitch() {
/* 160 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getRoll() {
/* 165 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void getRotationMatrix(float[] matrix) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public long getCurrentEventTime() {
/* 175 */     return 0L;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatchBackKey(boolean catchBack) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCatchBackKey() {
/* 185 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCatchMenuKey(boolean catchMenu) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCatchMenuKey() {
/* 195 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInputProcessor(InputProcessor processor) {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InputProcessor getInputProcessor() {
/* 207 */     if (this.mockInputProcessor == null) {
/* 208 */       this.mockInputProcessor = (InputProcessor)new InputAdapter();
/*     */     }
/* 210 */     return this.mockInputProcessor;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPeripheralAvailable(Input.Peripheral peripheral) {
/* 215 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRotation() {
/* 220 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public Input.Orientation getNativeOrientation() {
/* 225 */     return Input.Orientation.Landscape;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCursorCatched(boolean catched) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCursorCatched() {
/* 235 */     return false;
/*     */   }
/*     */   
/*     */   public void setCursorPosition(int x, int y) {}
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\headless\mock\input\MockInput.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */