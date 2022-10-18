/*    */ package com.badlogic.gdx.graphics.g3d.utils;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.Renderable;
/*    */ import com.badlogic.gdx.graphics.g3d.Shader;
/*    */ import com.badlogic.gdx.utils.Array;
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
/*    */ public abstract class BaseShaderProvider
/*    */   implements ShaderProvider
/*    */ {
/* 25 */   protected Array<Shader> shaders = new Array();
/*    */ 
/*    */   
/*    */   public Shader getShader(Renderable renderable) {
/* 29 */     Shader suggestedShader = renderable.shader;
/* 30 */     if (suggestedShader != null && suggestedShader.canRender(renderable)) return suggestedShader; 
/* 31 */     for (Shader shader1 : this.shaders) {
/* 32 */       if (shader1.canRender(renderable)) return shader1; 
/*    */     } 
/* 34 */     Shader shader = createShader(renderable);
/* 35 */     shader.init();
/* 36 */     this.shaders.add(shader);
/* 37 */     return shader;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract Shader createShader(Renderable paramRenderable);
/*    */   
/*    */   public void dispose() {
/* 44 */     for (Shader shader : this.shaders) {
/* 45 */       shader.dispose();
/*    */     }
/* 47 */     this.shaders.clear();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3\\utils\BaseShaderProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */