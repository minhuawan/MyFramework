/*    */ package com.badlogic.gdx.graphics.g2d;
/*    */ 
/*    */ import com.badlogic.gdx.utils.Pool;
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
/*    */ public class ParticleEffectPool
/*    */   extends Pool<ParticleEffectPool.PooledEffect>
/*    */ {
/*    */   private final ParticleEffect effect;
/*    */   
/*    */   public ParticleEffectPool(ParticleEffect effect, int initialCapacity, int max) {
/* 26 */     super(initialCapacity, max);
/* 27 */     this.effect = effect;
/*    */   }
/*    */   
/*    */   protected PooledEffect newObject() {
/* 31 */     return new PooledEffect(this.effect);
/*    */   }
/*    */   
/*    */   public PooledEffect obtain() {
/* 35 */     PooledEffect effect = (PooledEffect)super.obtain();
/* 36 */     effect.reset();
/* 37 */     return effect;
/*    */   }
/*    */   
/*    */   public class PooledEffect extends ParticleEffect {
/*    */     PooledEffect(ParticleEffect effect) {
/* 42 */       super(effect);
/*    */     }
/*    */ 
/*    */     
/*    */     public void reset() {
/* 47 */       super.reset();
/*    */     }
/*    */     
/*    */     public void free() {
/* 51 */       ParticleEffectPool.this.free(this);
/*    */     }
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\ParticleEffectPool.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */