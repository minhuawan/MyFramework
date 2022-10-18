/*     */ package com.badlogic.gdx.graphics.g3d.decals;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.glutils.ShaderProgram;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Sort;
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
/*     */ public class SimpleOrthoGroupStrategy
/*     */   implements GroupStrategy
/*     */ {
/*  70 */   private Comparator comparator = new Comparator();
/*     */   
/*     */   private static final int GROUP_OPAQUE = 0;
/*     */   private static final int GROUP_BLEND = 1;
/*     */   
/*     */   public int decideGroup(Decal decal) {
/*  76 */     return decal.getMaterial().isOpaque() ? 0 : 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeGroup(int group, Array<Decal> contents) {
/*  81 */     if (group == 1) {
/*  82 */       Sort.instance().sort(contents, this.comparator);
/*  83 */       Gdx.gl.glEnable(3042);
/*     */ 
/*     */       
/*  86 */       Gdx.gl.glDepthMask(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterGroup(int group) {
/*  94 */     if (group == 1) {
/*  95 */       Gdx.gl.glDepthMask(true);
/*  96 */       Gdx.gl.glDisable(3042);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void beforeGroups() {
/* 102 */     Gdx.gl.glEnable(3553);
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterGroups() {
/* 107 */     Gdx.gl.glDisable(3553);
/*     */   }
/*     */   
/*     */   class Comparator
/*     */     implements java.util.Comparator<Decal> {
/*     */     public int compare(Decal a, Decal b) {
/* 113 */       if (a.getZ() == b.getZ()) return 0; 
/* 114 */       return (a.getZ() - b.getZ() < 0.0F) ? -1 : 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ShaderProgram getGroupShader(int group) {
/* 120 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\decals\SimpleOrthoGroupStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */