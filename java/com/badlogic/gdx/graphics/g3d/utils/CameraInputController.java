/*     */ package com.badlogic.gdx.graphics.g3d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.input.GestureDetector;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.math.Vector3;
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
/*     */ 
/*     */ public class CameraInputController
/*     */   extends GestureDetector
/*     */ {
/*  31 */   public int rotateButton = 0;
/*     */   
/*  33 */   public float rotateAngle = 360.0F;
/*     */   
/*  35 */   public int translateButton = 1;
/*     */   
/*  37 */   public float translateUnits = 10.0F;
/*     */   
/*  39 */   public int forwardButton = 2;
/*     */   
/*  41 */   public int activateKey = 0;
/*     */   
/*     */   protected boolean activatePressed;
/*     */   
/*     */   public boolean alwaysScroll = true;
/*     */   
/*  47 */   public float scrollFactor = -0.1F;
/*     */   
/*  49 */   public float pinchZoomFactor = 10.0F;
/*     */   
/*     */   public boolean autoUpdate = true;
/*     */   
/*  53 */   public Vector3 target = new Vector3();
/*     */   
/*     */   public boolean translateTarget = true;
/*     */   
/*     */   public boolean forwardTarget = true;
/*     */   
/*     */   public boolean scrollTarget = false;
/*  60 */   public int forwardKey = 51;
/*     */   protected boolean forwardPressed;
/*  62 */   public int backwardKey = 47;
/*     */   protected boolean backwardPressed;
/*  64 */   public int rotateRightKey = 29;
/*     */   protected boolean rotateRightPressed;
/*  66 */   public int rotateLeftKey = 32;
/*     */   
/*     */   protected boolean rotateLeftPressed;
/*     */   
/*     */   public Camera camera;
/*  71 */   protected int button = -1;
/*     */   private float startX;
/*     */   private float startY;
/*  74 */   private final Vector3 tmpV1 = new Vector3();
/*  75 */   private final Vector3 tmpV2 = new Vector3();
/*     */   protected final CameraGestureListener gestureListener;
/*     */   private int touched;
/*     */   private boolean multiTouch;
/*     */   
/*     */   protected static class CameraGestureListener extends GestureDetector.GestureAdapter { public CameraInputController controller;
/*     */     
/*     */     public boolean touchDown(float x, float y, int pointer, int button) {
/*  83 */       this.previousZoom = 0.0F;
/*  84 */       return false;
/*     */     }
/*     */     private float previousZoom;
/*     */     
/*     */     public boolean tap(float x, float y, int count, int button) {
/*  89 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean longPress(float x, float y) {
/*  94 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean fling(float velocityX, float velocityY, int button) {
/*  99 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean pan(float x, float y, float deltaX, float deltaY) {
/* 104 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean zoom(float initialDistance, float distance) {
/* 109 */       float newZoom = distance - initialDistance;
/* 110 */       float amount = newZoom - this.previousZoom;
/* 111 */       this.previousZoom = newZoom;
/* 112 */       float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
/* 113 */       return this.controller.pinchZoom(amount / ((w > h) ? h : w));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
/* 118 */       return false;
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected CameraInputController(CameraGestureListener gestureListener, Camera camera) {
/* 125 */     super((GestureDetector.GestureListener)gestureListener);
/* 126 */     this.gestureListener = gestureListener;
/* 127 */     this.gestureListener.controller = this;
/* 128 */     this.camera = camera;
/*     */   }
/*     */   
/*     */   public CameraInputController(Camera camera) {
/* 132 */     this(new CameraGestureListener(), camera);
/*     */   }
/*     */   
/*     */   public void update() {
/* 136 */     if (this.rotateRightPressed || this.rotateLeftPressed || this.forwardPressed || this.backwardPressed) {
/* 137 */       float delta = Gdx.graphics.getDeltaTime();
/* 138 */       if (this.rotateRightPressed) this.camera.rotate(this.camera.up, -delta * this.rotateAngle); 
/* 139 */       if (this.rotateLeftPressed) this.camera.rotate(this.camera.up, delta * this.rotateAngle); 
/* 140 */       if (this.forwardPressed) {
/* 141 */         this.camera.translate(this.tmpV1.set(this.camera.direction).scl(delta * this.translateUnits));
/* 142 */         if (this.forwardTarget) this.target.add(this.tmpV1); 
/*     */       } 
/* 144 */       if (this.backwardPressed) {
/* 145 */         this.camera.translate(this.tmpV1.set(this.camera.direction).scl(-delta * this.translateUnits));
/* 146 */         if (this.forwardTarget) this.target.add(this.tmpV1); 
/*     */       } 
/* 148 */       if (this.autoUpdate) this.camera.update();
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean touchDown(int screenX, int screenY, int pointer, int button) {
/* 157 */     this.touched |= 1 << pointer;
/* 158 */     this.multiTouch = !MathUtils.isPowerOfTwo(this.touched);
/* 159 */     if (this.multiTouch) {
/* 160 */       this.button = -1;
/* 161 */     } else if (this.button < 0 && (this.activateKey == 0 || this.activatePressed)) {
/* 162 */       this.startX = screenX;
/* 163 */       this.startY = screenY;
/* 164 */       this.button = button;
/*     */     } 
/* 166 */     return (super.touchDown(screenX, screenY, pointer, button) || this.activateKey == 0 || this.activatePressed);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean touchUp(int screenX, int screenY, int pointer, int button) {
/* 171 */     this.touched &= 0xFFFFFFFF ^ 1 << pointer;
/* 172 */     this.multiTouch = !MathUtils.isPowerOfTwo(this.touched);
/* 173 */     if (button == this.button) this.button = -1; 
/* 174 */     return (super.touchUp(screenX, screenY, pointer, button) || this.activatePressed);
/*     */   }
/*     */   
/*     */   protected boolean process(float deltaX, float deltaY, int button) {
/* 178 */     if (button == this.rotateButton) {
/* 179 */       (this.tmpV1.set(this.camera.direction).crs(this.camera.up)).y = 0.0F;
/* 180 */       this.camera.rotateAround(this.target, this.tmpV1.nor(), deltaY * this.rotateAngle);
/* 181 */       this.camera.rotateAround(this.target, Vector3.Y, deltaX * -this.rotateAngle);
/* 182 */     } else if (button == this.translateButton) {
/* 183 */       this.camera.translate(this.tmpV1.set(this.camera.direction).crs(this.camera.up).nor().scl(-deltaX * this.translateUnits));
/* 184 */       this.camera.translate(this.tmpV2.set(this.camera.up).scl(-deltaY * this.translateUnits));
/* 185 */       if (this.translateTarget) this.target.add(this.tmpV1).add(this.tmpV2); 
/* 186 */     } else if (button == this.forwardButton) {
/* 187 */       this.camera.translate(this.tmpV1.set(this.camera.direction).scl(deltaY * this.translateUnits));
/* 188 */       if (this.forwardTarget) this.target.add(this.tmpV1); 
/*     */     } 
/* 190 */     if (this.autoUpdate) this.camera.update(); 
/* 191 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean touchDragged(int screenX, int screenY, int pointer) {
/* 196 */     boolean result = super.touchDragged(screenX, screenY, pointer);
/* 197 */     if (result || this.button < 0) return result; 
/* 198 */     float deltaX = (screenX - this.startX) / Gdx.graphics.getWidth();
/* 199 */     float deltaY = (this.startY - screenY) / Gdx.graphics.getHeight();
/* 200 */     this.startX = screenX;
/* 201 */     this.startY = screenY;
/* 202 */     return process(deltaX, deltaY, this.button);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean scrolled(int amount) {
/* 207 */     return zoom(amount * this.scrollFactor * this.translateUnits);
/*     */   }
/*     */   
/*     */   public boolean zoom(float amount) {
/* 211 */     if (!this.alwaysScroll && this.activateKey != 0 && !this.activatePressed) return false; 
/* 212 */     this.camera.translate(this.tmpV1.set(this.camera.direction).scl(amount));
/* 213 */     if (this.scrollTarget) this.target.add(this.tmpV1); 
/* 214 */     if (this.autoUpdate) this.camera.update(); 
/* 215 */     return true;
/*     */   }
/*     */   
/*     */   protected boolean pinchZoom(float amount) {
/* 219 */     return zoom(this.pinchZoomFactor * amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyDown(int keycode) {
/* 224 */     if (keycode == this.activateKey) this.activatePressed = true; 
/* 225 */     if (keycode == this.forwardKey)
/* 226 */     { this.forwardPressed = true; }
/* 227 */     else if (keycode == this.backwardKey)
/* 228 */     { this.backwardPressed = true; }
/* 229 */     else if (keycode == this.rotateRightKey)
/* 230 */     { this.rotateRightPressed = true; }
/* 231 */     else if (keycode == this.rotateLeftKey) { this.rotateLeftPressed = true; }
/* 232 */      return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyUp(int keycode) {
/* 237 */     if (keycode == this.activateKey) {
/* 238 */       this.activatePressed = false;
/* 239 */       this.button = -1;
/*     */     } 
/* 241 */     if (keycode == this.forwardKey)
/* 242 */     { this.forwardPressed = false; }
/* 243 */     else if (keycode == this.backwardKey)
/* 244 */     { this.backwardPressed = false; }
/* 245 */     else if (keycode == this.rotateRightKey)
/* 246 */     { this.rotateRightPressed = false; }
/* 247 */     else if (keycode == this.rotateLeftKey) { this.rotateLeftPressed = false; }
/* 248 */      return false;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\CameraInputController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */