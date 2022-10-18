/*     */ package com.badlogic.gdx;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
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
/*     */ public class InputMultiplexer
/*     */   implements InputProcessor
/*     */ {
/*  25 */   private Array<InputProcessor> processors = new Array(4);
/*     */ 
/*     */   
/*     */   public InputMultiplexer() {}
/*     */   
/*     */   public InputMultiplexer(InputProcessor... processors) {
/*  31 */     for (int i = 0; i < processors.length; i++)
/*  32 */       this.processors.add(processors[i]); 
/*     */   }
/*     */   
/*     */   public void addProcessor(int index, InputProcessor processor) {
/*  36 */     if (processor == null) throw new NullPointerException("processor cannot be null"); 
/*  37 */     this.processors.insert(index, processor);
/*     */   }
/*     */   
/*     */   public void removeProcessor(int index) {
/*  41 */     this.processors.removeIndex(index);
/*     */   }
/*     */   
/*     */   public void addProcessor(InputProcessor processor) {
/*  45 */     if (processor == null) throw new NullPointerException("processor cannot be null"); 
/*  46 */     this.processors.add(processor);
/*     */   }
/*     */   
/*     */   public void removeProcessor(InputProcessor processor) {
/*  50 */     this.processors.removeValue(processor, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  55 */     return this.processors.size;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  59 */     this.processors.clear();
/*     */   }
/*     */   
/*     */   public void setProcessors(Array<InputProcessor> processors) {
/*  63 */     this.processors = processors;
/*     */   }
/*     */   
/*     */   public Array<InputProcessor> getProcessors() {
/*  67 */     return this.processors;
/*     */   }
/*     */   
/*     */   public boolean keyDown(int keycode) {
/*  71 */     for (int i = 0, n = this.processors.size; i < n; i++) {
/*  72 */       if (((InputProcessor)this.processors.get(i)).keyDown(keycode)) return true; 
/*  73 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean keyUp(int keycode) {
/*  77 */     for (int i = 0, n = this.processors.size; i < n; i++) {
/*  78 */       if (((InputProcessor)this.processors.get(i)).keyUp(keycode)) return true; 
/*  79 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean keyTyped(char character) {
/*  83 */     for (int i = 0, n = this.processors.size; i < n; i++) {
/*  84 */       if (((InputProcessor)this.processors.get(i)).keyTyped(character)) return true; 
/*  85 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean touchDown(int screenX, int screenY, int pointer, int button) {
/*  89 */     for (int i = 0, n = this.processors.size; i < n; i++) {
/*  90 */       if (((InputProcessor)this.processors.get(i)).touchDown(screenX, screenY, pointer, button)) return true; 
/*  91 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean touchUp(int screenX, int screenY, int pointer, int button) {
/*  95 */     for (int i = 0, n = this.processors.size; i < n; i++) {
/*  96 */       if (((InputProcessor)this.processors.get(i)).touchUp(screenX, screenY, pointer, button)) return true; 
/*  97 */     }  return false;
/*     */   }
/*     */   
/*     */   public boolean touchDragged(int screenX, int screenY, int pointer) {
/* 101 */     for (int i = 0, n = this.processors.size; i < n; i++) {
/* 102 */       if (((InputProcessor)this.processors.get(i)).touchDragged(screenX, screenY, pointer)) return true; 
/* 103 */     }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseMoved(int screenX, int screenY) {
/* 108 */     for (int i = 0, n = this.processors.size; i < n; i++) {
/* 109 */       if (((InputProcessor)this.processors.get(i)).mouseMoved(screenX, screenY)) return true; 
/* 110 */     }  return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean scrolled(int amount) {
/* 115 */     for (int i = 0, n = this.processors.size; i < n; i++) {
/* 116 */       if (((InputProcessor)this.processors.get(i)).scrolled(amount)) return true; 
/* 117 */     }  return false;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\InputMultiplexer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */