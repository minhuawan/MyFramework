/*    */ package com.megacrit.cardcrawl.helpers;
/*    */ 
/*    */ import com.badlogic.gdx.Gdx;
/*    */ import com.badlogic.gdx.math.Interpolation;
/*    */ import com.badlogic.gdx.math.MathUtils;
/*    */ import com.badlogic.gdx.utils.viewport.FitViewport;
/*    */ import com.megacrit.cardcrawl.core.Settings;
/*    */ 
/*    */ public class ScreenShake {
/* 10 */   private float x = 0.0F;
/* 11 */   private float duration = 0.0F; private float startDuration = 0.0F;
/*    */   private float intensityValue;
/*    */   private float intervalSpeed;
/*    */   private boolean vertical;
/*    */   
/* 16 */   public enum ShakeIntensity { LOW, MED, HIGH; }
/*    */ 
/*    */   
/*    */   public enum ShakeDur {
/* 20 */     SHORT, MED, LONG, XLONG;
/*    */   }
/*    */ 
/*    */   
/*    */   public void shake(ShakeIntensity intensity, ShakeDur dur, boolean isVertical) {
/* 25 */     this.duration = getDuration(dur);
/* 26 */     this.startDuration = this.duration;
/* 27 */     this.intensityValue = getIntensity(intensity);
/* 28 */     this.vertical = isVertical;
/* 29 */     this.intervalSpeed = 0.3F;
/*    */   }
/*    */   
/*    */   public void rumble(float dur) {
/* 33 */     this.duration = dur;
/* 34 */     this.startDuration = dur;
/* 35 */     this.intensityValue = 10.0F;
/* 36 */     this.vertical = false;
/* 37 */     this.intervalSpeed = 0.7F;
/*    */   }
/*    */   
/*    */   public void mildRumble(float dur) {
/* 41 */     this.duration = dur;
/* 42 */     this.startDuration = dur;
/* 43 */     this.intensityValue = 3.0F;
/* 44 */     this.vertical = false;
/* 45 */     this.intervalSpeed = 0.7F;
/*    */   }
/*    */   
/*    */   public void update(FitViewport viewport) {
/* 49 */     if (Settings.HORIZ_LETTERBOX_AMT != 0 || Settings.VERT_LETTERBOX_AMT != 0) {
/*    */       return;
/*    */     }
/*    */     
/* 53 */     if (this.duration != 0.0F) {
/* 54 */       this.duration -= Gdx.graphics.getDeltaTime();
/*    */       
/* 56 */       if (this.duration < 0.0F) {
/* 57 */         this.duration = 0.0F;
/* 58 */         viewport.update(Settings.M_W, Settings.M_H);
/*    */         return;
/*    */       } 
/* 61 */       float tmp = Interpolation.fade.apply(0.1F, this.intensityValue, this.duration / this.startDuration);
/* 62 */       this.x = MathUtils.cosDeg((float)(System.currentTimeMillis() % 360L) / this.intervalSpeed) * tmp;
/*    */ 
/*    */       
/* 65 */       if (Settings.SCREEN_SHAKE) {
/* 66 */         if (this.vertical) {
/* 67 */           viewport.update(Settings.M_W, (int)(Settings.M_H + Math.abs(this.x)));
/*    */         } else {
/* 69 */           viewport.update((int)(Settings.M_W + this.x), Settings.M_H);
/*    */         } 
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private float getIntensity(ShakeIntensity intensity) {
/* 76 */     switch (intensity) {
/*    */       case SHORT:
/* 78 */         return 20.0F * Settings.scale;
/*    */       case MED:
/* 80 */         return 50.0F * Settings.scale;
/*    */     } 
/* 82 */     return 100.0F * Settings.scale;
/*    */   }
/*    */ 
/*    */   
/*    */   private float getDuration(ShakeDur dur) {
/* 87 */     switch (dur) {
/*    */       case SHORT:
/* 89 */         return 0.3F;
/*    */       case MED:
/* 91 */         return 0.5F;
/*    */       case LONG:
/* 93 */         return 1.0F;
/*    */       case XLONG:
/* 95 */         return 3.0F;
/*    */     } 
/* 97 */     return 1.0F;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\helpers\ScreenShake.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */