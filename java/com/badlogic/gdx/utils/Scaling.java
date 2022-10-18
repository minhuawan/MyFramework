/*    */ package com.badlogic.gdx.utils;
/*    */ 
/*    */ import com.badlogic.gdx.math.Vector2;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum Scaling
/*    */ {
/* 26 */   fit,
/*    */ 
/*    */   
/* 29 */   fill,
/*    */ 
/*    */   
/* 32 */   fillX,
/*    */ 
/*    */   
/* 35 */   fillY,
/*    */   
/* 37 */   stretch,
/*    */ 
/*    */   
/* 40 */   stretchX,
/*    */ 
/*    */   
/* 43 */   stretchY,
/*    */   
/* 45 */   none;
/*    */   private static final Vector2 temp;
/* 47 */   static { temp = new Vector2(); } public Vector2 apply(float sourceWidth, float sourceHeight, float targetWidth, float targetHeight) {
/*    */     float targetRatio;
/*    */     float scale;
/*    */     float sourceRatio;
/*    */     float f1;
/* 52 */     switch (this) {
/*    */       case fit:
/* 54 */         targetRatio = targetHeight / targetWidth;
/* 55 */         sourceRatio = sourceHeight / sourceWidth;
/* 56 */         f1 = (targetRatio > sourceRatio) ? (targetWidth / sourceWidth) : (targetHeight / sourceHeight);
/* 57 */         temp.x = sourceWidth * f1;
/* 58 */         temp.y = sourceHeight * f1;
/*    */         break;
/*    */       
/*    */       case fill:
/* 62 */         targetRatio = targetHeight / targetWidth;
/* 63 */         sourceRatio = sourceHeight / sourceWidth;
/* 64 */         f1 = (targetRatio < sourceRatio) ? (targetWidth / sourceWidth) : (targetHeight / sourceHeight);
/* 65 */         temp.x = sourceWidth * f1;
/* 66 */         temp.y = sourceHeight * f1;
/*    */         break;
/*    */       
/*    */       case fillX:
/* 70 */         scale = targetWidth / sourceWidth;
/* 71 */         temp.x = sourceWidth * scale;
/* 72 */         temp.y = sourceHeight * scale;
/*    */         break;
/*    */       
/*    */       case fillY:
/* 76 */         scale = targetHeight / sourceHeight;
/* 77 */         temp.x = sourceWidth * scale;
/* 78 */         temp.y = sourceHeight * scale;
/*    */         break;
/*    */       
/*    */       case stretch:
/* 82 */         temp.x = targetWidth;
/* 83 */         temp.y = targetHeight;
/*    */         break;
/*    */       case stretchX:
/* 86 */         temp.x = targetWidth;
/* 87 */         temp.y = sourceHeight;
/*    */         break;
/*    */       case stretchY:
/* 90 */         temp.x = sourceWidth;
/* 91 */         temp.y = targetHeight;
/*    */         break;
/*    */       case none:
/* 94 */         temp.x = sourceWidth;
/* 95 */         temp.y = sourceHeight;
/*    */         break;
/*    */     } 
/* 98 */     return temp;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\Scaling.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */