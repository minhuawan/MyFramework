/*     */ package com.badlogic.gdx.controllers.desktop.ois;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class OisJoystick
/*     */ {
/*     */   private static final int MIN_AXIS = -32768;
/*     */   private static final int MAX_AXIS = 32767;
/*     */   private final String name;
/*     */   private final long joystickPtr;
/*     */   private final boolean[] buttons;
/*     */   private final float[] axes;
/*     */   private final int[] povs;
/*     */   private final boolean[] slidersX;
/*     */   private final boolean[] slidersY;
/*     */   private OisListener listener;
/*     */   
/*     */   public enum OisPov
/*     */   {
/*  25 */     Centered, North, South, East, West, NorthEast, SouthEast, NorthWest, SouthWest;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OisJoystick(long joystickPtr, String name) {
/*  37 */     this.joystickPtr = joystickPtr;
/*  38 */     this.name = name;
/*  39 */     initialize(this);
/*  40 */     this.buttons = new boolean[getButtonCount()];
/*  41 */     this.axes = new float[getAxisCount()];
/*  42 */     this.povs = new int[getPovCount()];
/*  43 */     this.slidersX = new boolean[getSliderCount()];
/*  44 */     this.slidersY = new boolean[getSliderCount()];
/*     */   }
/*     */   
/*     */   public void setListener(OisListener listener) {
/*  48 */     this.listener = listener;
/*     */   }
/*     */   
/*     */   private void buttonPressed(int buttonIndex) {
/*  52 */     this.buttons[buttonIndex] = true;
/*  53 */     if (this.listener != null) this.listener.buttonPressed(this, buttonIndex); 
/*     */   }
/*     */   
/*     */   private void buttonReleased(int buttonIndex) {
/*  57 */     this.buttons[buttonIndex] = false;
/*  58 */     if (this.listener != null) this.listener.buttonReleased(this, buttonIndex); 
/*     */   }
/*     */   
/*     */   private void axisMoved(int axisIndex, int value) {
/*  62 */     this.axes[axisIndex] = (value - -32768 << 1) / 65535.0F - 1.0F;
/*  63 */     if (this.listener != null) this.listener.axisMoved(this, axisIndex, this.axes[axisIndex]); 
/*     */   }
/*     */   
/*     */   private void povMoved(int povIndex, int value) {
/*  67 */     this.povs[povIndex] = value;
/*  68 */     if (this.listener != null) this.listener.povMoved(this, povIndex, getPov(povIndex)); 
/*     */   }
/*     */   
/*     */   private void sliderMoved(int sliderIndex, int x, int y) {
/*  72 */     boolean xChanged = (this.slidersX[sliderIndex] != ((x == 1)));
/*  73 */     boolean yChanged = (this.slidersY[sliderIndex] != ((y == 1)));
/*  74 */     this.slidersX[sliderIndex] = (x == 1);
/*  75 */     this.slidersY[sliderIndex] = (y == 1);
/*  76 */     if (this.listener != null) {
/*  77 */       if (xChanged) this.listener.xSliderMoved(this, sliderIndex, (x == 1)); 
/*  78 */       if (yChanged) this.listener.ySliderMoved(this, sliderIndex, (y == 1)); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void update() {
/*  83 */     update(this.joystickPtr, this);
/*     */   }
/*     */   
/*     */   public int getAxisCount() {
/*  87 */     return getAxesCount(this.joystickPtr);
/*     */   }
/*     */   
/*     */   public int getButtonCount() {
/*  91 */     return getButtonCount(this.joystickPtr);
/*     */   }
/*     */   
/*     */   public int getPovCount() {
/*  95 */     return getPovCount(this.joystickPtr);
/*     */   }
/*     */   
/*     */   public int getSliderCount() {
/*  99 */     return getSliderCount(this.joystickPtr);
/*     */   }
/*     */   
/*     */   public float getAxis(int axisIndex) {
/* 103 */     if (axisIndex < 0 || axisIndex >= this.axes.length) return 0.0F; 
/* 104 */     return this.axes[axisIndex];
/*     */   }
/*     */   
/*     */   public OisPov getPov(int povIndex) {
/* 108 */     if (povIndex < 0 || povIndex >= this.povs.length) return OisPov.Centered; 
/* 109 */     switch (this.povs[povIndex]) {
/*     */       case 0:
/* 111 */         return OisPov.Centered;
/*     */       case 1:
/* 113 */         return OisPov.North;
/*     */       case 16:
/* 115 */         return OisPov.South;
/*     */       case 256:
/* 117 */         return OisPov.East;
/*     */       case 4096:
/* 119 */         return OisPov.West;
/*     */       case 257:
/* 121 */         return OisPov.NorthEast;
/*     */       case 272:
/* 123 */         return OisPov.SouthEast;
/*     */       case 4097:
/* 125 */         return OisPov.NorthWest;
/*     */       case 4112:
/* 127 */         return OisPov.SouthWest;
/*     */     } 
/* 129 */     throw new RuntimeException("Unexpected POV value reported by OIS: " + this.povs[povIndex]);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isButtonPressed(int buttonIndex) {
/* 134 */     if (buttonIndex < 0 || buttonIndex >= this.buttons.length) return false; 
/* 135 */     return this.buttons[buttonIndex];
/*     */   }
/*     */   
/*     */   public boolean getSliderX(int sliderIndex) {
/* 139 */     if (sliderIndex < 0 || sliderIndex >= this.slidersX.length) return false;
/*     */     
/* 141 */     return this.slidersX[sliderIndex];
/*     */   }
/*     */   
/*     */   public boolean getSliderY(int sliderIndex) {
/* 145 */     if (sliderIndex < 0 || sliderIndex >= this.slidersY.length) return false; 
/* 146 */     return this.slidersY[sliderIndex];
/*     */   }
/*     */   
/*     */   public String getName() {
/* 150 */     return this.name;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 154 */     return this.name;
/*     */   }
/*     */   
/*     */   private native void initialize(OisJoystick paramOisJoystick);
/*     */   
/*     */   private native void update(long paramLong, OisJoystick paramOisJoystick);
/*     */   
/*     */   private native int getAxesCount(long paramLong);
/*     */   
/*     */   private native int getButtonCount(long paramLong);
/*     */   
/*     */   private native int getPovCount(long paramLong);
/*     */   
/*     */   private native int getSliderCount(long paramLong);
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\controllers\desktop\ois\OisJoystick.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */