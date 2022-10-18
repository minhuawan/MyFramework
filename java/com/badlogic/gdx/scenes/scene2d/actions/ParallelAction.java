/*     */ package com.badlogic.gdx.scenes.scene2d.actions;
/*     */ 
/*     */ import com.badlogic.gdx.scenes.scene2d.Action;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Pool;
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
/*     */ public class ParallelAction
/*     */   extends Action
/*     */ {
/*  27 */   Array<Action> actions = new Array(4);
/*     */   
/*     */   private boolean complete;
/*     */   
/*     */   public ParallelAction() {}
/*     */   
/*     */   public ParallelAction(Action action1) {
/*  34 */     addAction(action1);
/*     */   }
/*     */   
/*     */   public ParallelAction(Action action1, Action action2) {
/*  38 */     addAction(action1);
/*  39 */     addAction(action2);
/*     */   }
/*     */   
/*     */   public ParallelAction(Action action1, Action action2, Action action3) {
/*  43 */     addAction(action1);
/*  44 */     addAction(action2);
/*  45 */     addAction(action3);
/*     */   }
/*     */   
/*     */   public ParallelAction(Action action1, Action action2, Action action3, Action action4) {
/*  49 */     addAction(action1);
/*  50 */     addAction(action2);
/*  51 */     addAction(action3);
/*  52 */     addAction(action4);
/*     */   }
/*     */   
/*     */   public ParallelAction(Action action1, Action action2, Action action3, Action action4, Action action5) {
/*  56 */     addAction(action1);
/*  57 */     addAction(action2);
/*  58 */     addAction(action3);
/*  59 */     addAction(action4);
/*  60 */     addAction(action5);
/*     */   }
/*     */   
/*     */   public boolean act(float delta) {
/*  64 */     if (this.complete) return true; 
/*  65 */     this.complete = true;
/*  66 */     Pool pool = getPool();
/*  67 */     setPool(null);
/*     */     try {
/*  69 */       Array<Action> actions = this.actions;
/*  70 */       for (int i = 0, n = actions.size; i < n && this.actor != null; i++) {
/*  71 */         Action currentAction = (Action)actions.get(i);
/*  72 */         if (currentAction.getActor() != null && !currentAction.act(delta)) this.complete = false; 
/*  73 */         if (this.actor == null) return true; 
/*     */       } 
/*  75 */       return this.complete;
/*     */     } finally {
/*  77 */       setPool(pool);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void restart() {
/*  82 */     this.complete = false;
/*  83 */     Array<Action> actions = this.actions;
/*  84 */     for (int i = 0, n = actions.size; i < n; i++)
/*  85 */       ((Action)actions.get(i)).restart(); 
/*     */   }
/*     */   
/*     */   public void reset() {
/*  89 */     super.reset();
/*  90 */     this.actions.clear();
/*     */   }
/*     */   
/*     */   public void addAction(Action action) {
/*  94 */     this.actions.add(action);
/*  95 */     if (this.actor != null) action.setActor(this.actor); 
/*     */   }
/*     */   
/*     */   public void setActor(Actor actor) {
/*  99 */     Array<Action> actions = this.actions;
/* 100 */     for (int i = 0, n = actions.size; i < n; i++)
/* 101 */       ((Action)actions.get(i)).setActor(actor); 
/* 102 */     super.setActor(actor);
/*     */   }
/*     */   
/*     */   public Array<Action> getActions() {
/* 106 */     return this.actions;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 110 */     StringBuilder buffer = new StringBuilder(64);
/* 111 */     buffer.append(super.toString());
/* 112 */     buffer.append('(');
/* 113 */     Array<Action> actions = this.actions;
/* 114 */     for (int i = 0, n = actions.size; i < n; i++) {
/* 115 */       if (i > 0) buffer.append(", "); 
/* 116 */       buffer.append(actions.get(i));
/*     */     } 
/* 118 */     buffer.append(')');
/* 119 */     return buffer.toString();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2d\actions\ParallelAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */