/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.scenes.scene2d.Action;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*     */ import com.badlogic.gdx.scenes.scene2d.actions.Actions;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Timer;
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
/*     */ 
/*     */ 
/*     */ public class TooltipManager
/*     */ {
/*     */   private static TooltipManager instance;
/*     */   private static Files files;
/*  38 */   public float initialTime = 2.0F;
/*     */   
/*  40 */   public float subsequentTime = 0.0F;
/*     */   
/*  42 */   public float resetTime = 1.5F;
/*     */   
/*     */   public boolean enabled = true;
/*     */   
/*     */   public boolean animations = true;
/*     */   
/*  48 */   public float maxWidth = 2.14748365E9F;
/*     */   
/*  50 */   public float offsetX = 15.0F, offsetY = 19.0F;
/*     */ 
/*     */   
/*  53 */   public float edgeDistance = 7.0F;
/*     */   
/*  55 */   final Array<Tooltip> shown = new Array();
/*     */   
/*  57 */   float time = this.initialTime;
/*  58 */   final Timer.Task resetTask = new Timer.Task() {
/*     */       public void run() {
/*  60 */         TooltipManager.this.time = TooltipManager.this.initialTime;
/*     */       }
/*     */     };
/*     */   Tooltip showTooltip;
/*     */   
/*  65 */   final Timer.Task showTask = new Timer.Task() {
/*     */       public void run() {
/*  67 */         if (TooltipManager.this.showTooltip == null)
/*     */           return; 
/*  69 */         Stage stage = TooltipManager.this.showTooltip.targetActor.getStage();
/*  70 */         if (stage == null)
/*  71 */           return;  stage.addActor((Actor)TooltipManager.this.showTooltip.container);
/*  72 */         TooltipManager.this.showTooltip.container.toFront();
/*  73 */         TooltipManager.this.shown.add(TooltipManager.this.showTooltip);
/*     */         
/*  75 */         TooltipManager.this.showTooltip.container.clearActions();
/*  76 */         TooltipManager.this.showAction(TooltipManager.this.showTooltip);
/*     */         
/*  78 */         if (!TooltipManager.this.showTooltip.instant) {
/*  79 */           TooltipManager.this.time = TooltipManager.this.subsequentTime;
/*  80 */           TooltipManager.this.resetTask.cancel();
/*     */         } 
/*     */       }
/*     */     };
/*     */   
/*     */   public void touchDown(Tooltip tooltip) {
/*  86 */     this.showTask.cancel();
/*  87 */     if (tooltip.container.remove()) this.resetTask.cancel(); 
/*  88 */     this.resetTask.run();
/*  89 */     if (this.enabled || tooltip.always) {
/*  90 */       this.showTooltip = tooltip;
/*  91 */       Timer.schedule(this.showTask, this.time);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void enter(Tooltip tooltip) {
/*  96 */     this.showTooltip = tooltip;
/*  97 */     this.showTask.cancel();
/*  98 */     if (this.enabled || tooltip.always)
/*  99 */       if (this.time == 0.0F || tooltip.instant) {
/* 100 */         this.showTask.run();
/*     */       } else {
/* 102 */         Timer.schedule(this.showTask, this.time);
/*     */       }  
/*     */   }
/*     */   
/*     */   public void hide(Tooltip tooltip) {
/* 107 */     this.showTooltip = null;
/* 108 */     this.showTask.cancel();
/* 109 */     if (tooltip.container.hasParent()) {
/* 110 */       this.shown.removeValue(tooltip, true);
/* 111 */       hideAction(tooltip);
/* 112 */       this.resetTask.cancel();
/* 113 */       Timer.schedule(this.resetTask, this.resetTime);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void showAction(Tooltip tooltip) {
/* 119 */     float actionTime = this.animations ? ((this.time > 0.0F) ? 0.5F : 0.15F) : 0.1F;
/* 120 */     tooltip.container.setTransform(true);
/* 121 */     (tooltip.container.getColor()).a = 0.2F;
/* 122 */     tooltip.container.setScale(0.05F);
/* 123 */     tooltip.container.addAction((Action)Actions.parallel((Action)Actions.fadeIn(actionTime, Interpolation.fade), (Action)Actions.scaleTo(1.0F, 1.0F, actionTime, Interpolation.fade)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void hideAction(Tooltip tooltip) {
/* 129 */     tooltip.container
/* 130 */       .addAction((Action)Actions.sequence((Action)Actions.parallel((Action)Actions.alpha(0.2F, 0.2F, Interpolation.fade), (Action)Actions.scaleTo(0.05F, 0.05F, 0.2F, Interpolation.fade)), (Action)Actions.removeActor()));
/*     */   }
/*     */   
/*     */   public void hideAll() {
/* 134 */     this.resetTask.cancel();
/* 135 */     this.showTask.cancel();
/* 136 */     this.time = this.initialTime;
/* 137 */     this.showTooltip = null;
/*     */     
/* 139 */     for (Tooltip tooltip : this.shown)
/* 140 */       tooltip.hide(); 
/* 141 */     this.shown.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void instant() {
/* 146 */     this.time = 0.0F;
/* 147 */     this.showTask.run();
/* 148 */     this.showTask.cancel();
/*     */   }
/*     */   
/*     */   public static TooltipManager getInstance() {
/* 152 */     if (files == null || files != Gdx.files) {
/* 153 */       files = Gdx.files;
/* 154 */       instance = new TooltipManager();
/*     */     } 
/* 156 */     return instance;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\TooltipManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */