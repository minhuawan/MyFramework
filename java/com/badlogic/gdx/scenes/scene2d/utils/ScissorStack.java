/*     */ package com.badlogic.gdx.scenes.scene2d.utils;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Camera;
/*     */ import com.badlogic.gdx.graphics.glutils.HdpiUtils;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Rectangle;
/*     */ import com.badlogic.gdx.math.Vector3;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScissorStack
/*     */ {
/*  33 */   private static Array<Rectangle> scissors = new Array();
/*  34 */   static Vector3 tmp = new Vector3();
/*  35 */   static final Rectangle viewport = new Rectangle();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean pushScissors(Rectangle scissor) {
/*  46 */     fix(scissor);
/*     */     
/*  48 */     if (scissors.size == 0) {
/*  49 */       if (scissor.width < 1.0F || scissor.height < 1.0F) return false; 
/*  50 */       Gdx.gl.glEnable(3089);
/*     */     } else {
/*     */       
/*  53 */       Rectangle parent = (Rectangle)scissors.get(scissors.size - 1);
/*  54 */       float minX = Math.max(parent.x, scissor.x);
/*  55 */       float maxX = Math.min(parent.x + parent.width, scissor.x + scissor.width);
/*  56 */       if (maxX - minX < 1.0F) return false;
/*     */       
/*  58 */       float minY = Math.max(parent.y, scissor.y);
/*  59 */       float maxY = Math.min(parent.y + parent.height, scissor.y + scissor.height);
/*  60 */       if (maxY - minY < 1.0F) return false;
/*     */       
/*  62 */       scissor.x = minX;
/*  63 */       scissor.y = minY;
/*  64 */       scissor.width = maxX - minX;
/*  65 */       scissor.height = Math.max(1.0F, maxY - minY);
/*     */     } 
/*  67 */     scissors.add(scissor);
/*  68 */     HdpiUtils.glScissor((int)scissor.x, (int)scissor.y, (int)scissor.width, (int)scissor.height);
/*  69 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Rectangle popScissors() {
/*  77 */     Rectangle old = (Rectangle)scissors.pop();
/*  78 */     if (scissors.size == 0) {
/*  79 */       Gdx.gl.glDisable(3089);
/*     */     } else {
/*  81 */       Rectangle scissor = (Rectangle)scissors.peek();
/*  82 */       HdpiUtils.glScissor((int)scissor.x, (int)scissor.y, (int)scissor.width, (int)scissor.height);
/*     */     } 
/*  84 */     return old;
/*     */   }
/*     */   
/*     */   public static Rectangle peekScissors() {
/*  88 */     return (Rectangle)scissors.peek();
/*     */   }
/*     */   
/*     */   private static void fix(Rectangle rect) {
/*  92 */     rect.x = Math.round(rect.x);
/*  93 */     rect.y = Math.round(rect.y);
/*  94 */     rect.width = Math.round(rect.width);
/*  95 */     rect.height = Math.round(rect.height);
/*  96 */     if (rect.width < 0.0F) {
/*  97 */       rect.width = -rect.width;
/*  98 */       rect.x -= rect.width;
/*     */     } 
/* 100 */     if (rect.height < 0.0F) {
/* 101 */       rect.height = -rect.height;
/* 102 */       rect.y -= rect.height;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void calculateScissors(Camera camera, Matrix4 batchTransform, Rectangle area, Rectangle scissor) {
/* 109 */     calculateScissors(camera, 0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), batchTransform, area, scissor);
/*     */   }
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
/*     */   public static void calculateScissors(Camera camera, float viewportX, float viewportY, float viewportWidth, float viewportHeight, Matrix4 batchTransform, Rectangle area, Rectangle scissor) {
/* 123 */     tmp.set(area.x, area.y, 0.0F);
/* 124 */     tmp.mul(batchTransform);
/* 125 */     camera.project(tmp, viewportX, viewportY, viewportWidth, viewportHeight);
/* 126 */     scissor.x = tmp.x;
/* 127 */     scissor.y = tmp.y;
/*     */     
/* 129 */     tmp.set(area.x + area.width, area.y + area.height, 0.0F);
/* 130 */     tmp.mul(batchTransform);
/* 131 */     camera.project(tmp, viewportX, viewportY, viewportWidth, viewportHeight);
/* 132 */     scissor.width = tmp.x - scissor.x;
/* 133 */     scissor.height = tmp.y - scissor.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public static Rectangle getViewport() {
/* 138 */     if (scissors.size == 0) {
/* 139 */       viewport.set(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
/* 140 */       return viewport;
/*     */     } 
/* 142 */     Rectangle scissor = (Rectangle)scissors.peek();
/* 143 */     viewport.set(scissor);
/* 144 */     return viewport;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\utils\ScissorStack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */