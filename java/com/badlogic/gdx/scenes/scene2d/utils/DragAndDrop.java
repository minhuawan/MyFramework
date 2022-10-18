/*     */ package com.badlogic.gdx.scenes.scene2d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.Touchable;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
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
/*     */ 
/*     */ public class DragAndDrop
/*     */ {
/*  33 */   static final Vector2 tmpVector = new Vector2();
/*     */   
/*     */   Payload payload;
/*     */   Actor dragActor;
/*     */   Target target;
/*     */   boolean isValidTarget;
/*  39 */   Array<Target> targets = new Array();
/*  40 */   ObjectMap<Source, DragListener> sourceListeners = new ObjectMap();
/*  41 */   private float tapSquareSize = 8.0F;
/*     */   private int button;
/*  43 */   float dragActorX = 0.0F; float dragActorY = 0.0F; float touchOffsetX;
/*     */   float touchOffsetY;
/*     */   long dragStartTime;
/*  46 */   int dragTime = 250;
/*  47 */   int activePointer = -1;
/*     */   boolean cancelTouchFocus = true;
/*     */   boolean keepWithinStage = true;
/*     */   
/*     */   public void addSource(final Source source) {
/*  52 */     DragListener listener = new DragListener() {
/*     */         public void dragStart(InputEvent event, float x, float y, int pointer) {
/*  54 */           if (DragAndDrop.this.activePointer != -1) {
/*  55 */             event.stop();
/*     */             
/*     */             return;
/*     */           } 
/*  59 */           DragAndDrop.this.activePointer = pointer;
/*     */           
/*  61 */           DragAndDrop.this.dragStartTime = System.currentTimeMillis();
/*  62 */           DragAndDrop.this.payload = source.dragStart(event, getTouchDownX(), getTouchDownY(), pointer);
/*  63 */           event.stop();
/*     */           
/*  65 */           if (DragAndDrop.this.cancelTouchFocus && DragAndDrop.this.payload != null) source.getActor().getStage().cancelTouchFocusExcept((EventListener)this, source.getActor()); 
/*     */         }
/*     */         
/*     */         public void drag(InputEvent event, float x, float y, int pointer) {
/*  69 */           if (DragAndDrop.this.payload == null)
/*  70 */             return;  if (pointer != DragAndDrop.this.activePointer)
/*     */             return; 
/*  72 */           Stage stage = event.getStage();
/*     */           
/*  74 */           Touchable dragActorTouchable = null;
/*  75 */           if (DragAndDrop.this.dragActor != null) {
/*  76 */             dragActorTouchable = DragAndDrop.this.dragActor.getTouchable();
/*  77 */             DragAndDrop.this.dragActor.setTouchable(Touchable.disabled);
/*     */           } 
/*     */ 
/*     */           
/*  81 */           DragAndDrop.Target newTarget = null;
/*  82 */           DragAndDrop.this.isValidTarget = false;
/*  83 */           float stageX = event.getStageX() + DragAndDrop.this.touchOffsetX, stageY = event.getStageY() + DragAndDrop.this.touchOffsetY;
/*  84 */           Actor hit = event.getStage().hit(stageX, stageY, true);
/*  85 */           if (hit == null) hit = event.getStage().hit(stageX, stageY, false); 
/*  86 */           if (hit != null) {
/*  87 */             for (int i = 0, n = DragAndDrop.this.targets.size; i < n; ) {
/*  88 */               DragAndDrop.Target target = (DragAndDrop.Target)DragAndDrop.this.targets.get(i);
/*  89 */               if (!target.actor.isAscendantOf(hit)) { i++; continue; }
/*  90 */                newTarget = target;
/*  91 */               target.actor.stageToLocalCoordinates(DragAndDrop.tmpVector.set(stageX, stageY));
/*     */             } 
/*     */           }
/*     */ 
/*     */           
/*  96 */           if (newTarget != DragAndDrop.this.target) {
/*  97 */             if (DragAndDrop.this.target != null) DragAndDrop.this.target.reset(source, DragAndDrop.this.payload); 
/*  98 */             DragAndDrop.this.target = newTarget;
/*     */           } 
/*     */           
/* 101 */           if (newTarget != null) DragAndDrop.this.isValidTarget = newTarget.drag(source, DragAndDrop.this.payload, DragAndDrop.tmpVector.x, DragAndDrop.tmpVector.y, pointer);
/*     */           
/* 103 */           if (DragAndDrop.this.dragActor != null) DragAndDrop.this.dragActor.setTouchable(dragActorTouchable);
/*     */ 
/*     */           
/* 106 */           Actor actor = null;
/* 107 */           if (DragAndDrop.this.target != null) actor = DragAndDrop.this.isValidTarget ? DragAndDrop.this.payload.validDragActor : DragAndDrop.this.payload.invalidDragActor; 
/* 108 */           if (actor == null) actor = DragAndDrop.this.payload.dragActor; 
/* 109 */           if (actor == null)
/* 110 */             return;  if (DragAndDrop.this.dragActor != actor) {
/* 111 */             if (DragAndDrop.this.dragActor != null) DragAndDrop.this.dragActor.remove(); 
/* 112 */             DragAndDrop.this.dragActor = actor;
/* 113 */             stage.addActor(actor);
/*     */           } 
/* 115 */           float actorX = event.getStageX() - actor.getWidth() + DragAndDrop.this.dragActorX;
/* 116 */           float actorY = event.getStageY() + DragAndDrop.this.dragActorY;
/* 117 */           if (DragAndDrop.this.keepWithinStage) {
/* 118 */             if (actorX < 0.0F) actorX = 0.0F; 
/* 119 */             if (actorY < 0.0F) actorY = 0.0F; 
/* 120 */             if (actorX + actor.getWidth() > stage.getWidth()) actorX = stage.getWidth() - actor.getWidth(); 
/* 121 */             if (actorY + actor.getHeight() > stage.getHeight()) actorY = stage.getHeight() - actor.getHeight(); 
/*     */           } 
/* 123 */           actor.setPosition(actorX, actorY);
/*     */         }
/*     */         
/*     */         public void dragStop(InputEvent event, float x, float y, int pointer) {
/* 127 */           if (pointer != DragAndDrop.this.activePointer)
/* 128 */             return;  DragAndDrop.this.activePointer = -1;
/* 129 */           if (DragAndDrop.this.payload == null)
/*     */             return; 
/* 131 */           if (System.currentTimeMillis() - DragAndDrop.this.dragStartTime < DragAndDrop.this.dragTime) DragAndDrop.this.isValidTarget = false; 
/* 132 */           if (DragAndDrop.this.dragActor != null) DragAndDrop.this.dragActor.remove(); 
/* 133 */           if (DragAndDrop.this.isValidTarget) {
/* 134 */             float stageX = event.getStageX() + DragAndDrop.this.touchOffsetX, stageY = event.getStageY() + DragAndDrop.this.touchOffsetY;
/* 135 */             DragAndDrop.this.target.actor.stageToLocalCoordinates(DragAndDrop.tmpVector.set(stageX, stageY));
/* 136 */             DragAndDrop.this.target.drop(source, DragAndDrop.this.payload, DragAndDrop.tmpVector.x, DragAndDrop.tmpVector.y, pointer);
/*     */           } 
/* 138 */           source.dragStop(event, x, y, pointer, DragAndDrop.this.payload, DragAndDrop.this.isValidTarget ? DragAndDrop.this.target : null);
/* 139 */           if (DragAndDrop.this.target != null) DragAndDrop.this.target.reset(source, DragAndDrop.this.payload); 
/* 140 */           DragAndDrop.this.payload = null;
/* 141 */           DragAndDrop.this.target = null;
/* 142 */           DragAndDrop.this.isValidTarget = false;
/* 143 */           DragAndDrop.this.dragActor = null;
/*     */         }
/*     */       };
/* 146 */     listener.setTapSquareSize(this.tapSquareSize);
/* 147 */     listener.setButton(this.button);
/* 148 */     source.actor.addCaptureListener((EventListener)listener);
/* 149 */     this.sourceListeners.put(source, listener);
/*     */   }
/*     */   
/*     */   public void removeSource(Source source) {
/* 153 */     DragListener dragListener = (DragListener)this.sourceListeners.remove(source);
/* 154 */     source.actor.removeCaptureListener((EventListener)dragListener);
/*     */   }
/*     */   
/*     */   public void addTarget(Target target) {
/* 158 */     this.targets.add(target);
/*     */   }
/*     */   
/*     */   public void removeTarget(Target target) {
/* 162 */     this.targets.removeValue(target, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 167 */     this.targets.clear();
/* 168 */     for (ObjectMap.Entries<ObjectMap.Entry<Source, DragListener>> entries = this.sourceListeners.entries().iterator(); entries.hasNext(); ) { ObjectMap.Entry<Source, DragListener> entry = entries.next();
/* 169 */       ((Source)entry.key).actor.removeCaptureListener((EventListener)entry.value); }
/* 170 */      this.sourceListeners.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTapSquareSize(float halfTapSquareSize) {
/* 175 */     this.tapSquareSize = halfTapSquareSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setButton(int button) {
/* 180 */     this.button = button;
/*     */   }
/*     */   
/*     */   public void setDragActorPosition(float dragActorX, float dragActorY) {
/* 184 */     this.dragActorX = dragActorX;
/* 185 */     this.dragActorY = dragActorY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTouchOffset(float touchOffsetX, float touchOffsetY) {
/* 191 */     this.touchOffsetX = touchOffsetX;
/* 192 */     this.touchOffsetY = touchOffsetY;
/*     */   }
/*     */   
/*     */   public boolean isDragging() {
/* 196 */     return (this.payload != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public Actor getDragActor() {
/* 201 */     return this.dragActor;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDragTime(int dragMillis) {
/* 207 */     this.dragTime = dragMillis;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCancelTouchFocus(boolean cancelTouchFocus) {
/* 214 */     this.cancelTouchFocus = cancelTouchFocus;
/*     */   }
/*     */   
/*     */   public void setKeepWithinStage(boolean keepWithinStage) {
/* 218 */     this.keepWithinStage = keepWithinStage;
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class Source
/*     */   {
/*     */     final Actor actor;
/*     */     
/*     */     public Source(Actor actor) {
/* 227 */       if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 228 */       this.actor = actor;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public abstract DragAndDrop.Payload dragStart(InputEvent param1InputEvent, float param1Float1, float param1Float2, int param1Int);
/*     */ 
/*     */ 
/*     */     
/*     */     public void dragStop(InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Actor getActor() {
/* 242 */       return this.actor;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static abstract class Target
/*     */   {
/*     */     final Actor actor;
/*     */     
/*     */     public Target(Actor actor) {
/* 252 */       if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 253 */       this.actor = actor;
/* 254 */       Stage stage = actor.getStage();
/* 255 */       if (stage != null && actor == stage.getRoot()) {
/* 256 */         throw new IllegalArgumentException("The stage root cannot be a drag and drop target.");
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public abstract boolean drag(DragAndDrop.Source param1Source, DragAndDrop.Payload param1Payload, float param1Float1, float param1Float2, int param1Int);
/*     */ 
/*     */     
/*     */     public void reset(DragAndDrop.Source source, DragAndDrop.Payload payload) {}
/*     */ 
/*     */     
/*     */     public abstract void drop(DragAndDrop.Source param1Source, DragAndDrop.Payload param1Payload, float param1Float1, float param1Float2, int param1Int);
/*     */ 
/*     */     
/*     */     public Actor getActor() {
/* 271 */       return this.actor;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Payload
/*     */   {
/*     */     Actor dragActor;
/*     */     Actor validDragActor;
/*     */     Actor invalidDragActor;
/*     */     Object object;
/*     */     
/*     */     public void setDragActor(Actor dragActor) {
/* 283 */       this.dragActor = dragActor;
/*     */     }
/*     */     
/*     */     public Actor getDragActor() {
/* 287 */       return this.dragActor;
/*     */     }
/*     */     
/*     */     public void setValidDragActor(Actor validDragActor) {
/* 291 */       this.validDragActor = validDragActor;
/*     */     }
/*     */     
/*     */     public Actor getValidDragActor() {
/* 295 */       return this.validDragActor;
/*     */     }
/*     */     
/*     */     public void setInvalidDragActor(Actor invalidDragActor) {
/* 299 */       this.invalidDragActor = invalidDragActor;
/*     */     }
/*     */     
/*     */     public Actor getInvalidDragActor() {
/* 303 */       return this.invalidDragActor;
/*     */     }
/*     */     
/*     */     public Object getObject() {
/* 307 */       return this.object;
/*     */     }
/*     */     
/*     */     public void setObject(Object object) {
/* 311 */       this.object = object;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\DragAndDrop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */