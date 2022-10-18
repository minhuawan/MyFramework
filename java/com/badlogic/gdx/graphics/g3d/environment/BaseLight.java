/*    */ package com.badlogic.gdx.graphics.g3d.environment;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Color;
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
/*    */ public abstract class BaseLight<T extends BaseLight<T>>
/*    */ {
/* 22 */   public final Color color = new Color(0.0F, 0.0F, 0.0F, 1.0F);
/*    */   
/*    */   public T setColor(float r, float g, float b, float a) {
/* 25 */     this.color.set(r, g, b, a);
/* 26 */     return (T)this;
/*    */   }
/*    */   
/*    */   public T setColor(Color color) {
/* 30 */     this.color.set(color);
/* 31 */     return (T)this;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\environment\BaseLight.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */