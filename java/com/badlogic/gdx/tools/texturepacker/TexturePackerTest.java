/*     */ package com.badlogic.gdx.tools.texturepacker;
/*     */ 
/*     */ import com.badlogic.gdx.ApplicationAdapter;
/*     */ import com.badlogic.gdx.ApplicationListener;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.util.Random;
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
/*     */ public class TexturePackerTest
/*     */   extends ApplicationAdapter
/*     */ {
/*     */   ShapeRenderer renderer;
/*     */   Array<TexturePacker.Page> pages;
/*     */   
/*     */   public void create() {
/*  40 */     this.renderer = new ShapeRenderer();
/*     */   }
/*     */   
/*     */   public void render() {
/*  44 */     Gdx.gl.glClear(16384);
/*     */     
/*  46 */     TexturePacker.Settings settings = new TexturePacker.Settings();
/*  47 */     settings.fast = false;
/*  48 */     settings.pot = false;
/*  49 */     settings.maxWidth = 1024;
/*  50 */     settings.maxHeight = 1024;
/*  51 */     settings.rotation = false;
/*  52 */     settings.paddingX = 0;
/*     */     
/*  54 */     if (this.pages == null) {
/*  55 */       Random random = new Random(1243L);
/*  56 */       Array<TexturePacker.Rect> inputRects = new Array(); int i;
/*  57 */       for (i = 0; i < 240; i++) {
/*  58 */         TexturePacker.Rect rect = new TexturePacker.Rect();
/*  59 */         rect.name = "rect" + i;
/*  60 */         rect.height = 16 + random.nextInt(120);
/*  61 */         rect.width = 16 + random.nextInt(240);
/*  62 */         inputRects.add(rect);
/*     */       } 
/*  64 */       for (i = 0; i < 10; i++) {
/*  65 */         TexturePacker.Rect rect = new TexturePacker.Rect();
/*  66 */         rect.name = "rect" + (40 + i);
/*  67 */         rect.height = 400 + random.nextInt(340);
/*  68 */         rect.width = 1 + random.nextInt(10);
/*  69 */         inputRects.add(rect);
/*     */       } 
/*     */       
/*  72 */       long s = System.nanoTime();
/*     */       
/*  74 */       this.pages = (new MaxRectsPacker(settings)).pack(inputRects);
/*     */       
/*  76 */       long e = System.nanoTime();
/*  77 */       System.out.println("fast: " + settings.fast);
/*  78 */       System.out.println(((float)(e - s) / 1000000.0F) + " ms");
/*  79 */       System.out.println();
/*     */     } 
/*     */     
/*  82 */     int x = 20, y = 20;
/*  83 */     for (TexturePacker.Page page : this.pages) {
/*  84 */       this.renderer.setColor(Color.GRAY);
/*  85 */       this.renderer.begin(ShapeRenderer.ShapeType.Filled); int i;
/*  86 */       for (i = 0; i < page.outputRects.size; i++) {
/*  87 */         TexturePacker.Rect rect = (TexturePacker.Rect)page.outputRects.get(i);
/*  88 */         this.renderer.rect((x + rect.x + settings.paddingX), (y + rect.y + settings.paddingY), (rect.width - settings.paddingX), (rect.height - settings.paddingY));
/*     */       } 
/*     */       
/*  91 */       this.renderer.end();
/*  92 */       this.renderer.setColor(Color.RED);
/*  93 */       this.renderer.begin(ShapeRenderer.ShapeType.Line);
/*  94 */       for (i = 0; i < page.outputRects.size; i++) {
/*  95 */         TexturePacker.Rect rect = (TexturePacker.Rect)page.outputRects.get(i);
/*  96 */         this.renderer.rect((x + rect.x + settings.paddingX), (y + rect.y + settings.paddingY), (rect.width - settings.paddingX), (rect.height - settings.paddingY));
/*     */       } 
/*     */       
/*  99 */       this.renderer.setColor(Color.GREEN);
/* 100 */       this.renderer.rect(x, y, (page.width + settings.paddingX * 2), (page.height + settings.paddingY * 2));
/* 101 */       this.renderer.end();
/* 102 */       x += page.width + 20;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resize(int width, int height) {
/* 107 */     this.renderer.setProjectionMatrix((new Matrix4()).setToOrtho2D(0.0F, 0.0F, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 111 */     new LwjglApplication((ApplicationListener)new TexturePackerTest(), "", 640, 480);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\texturepacker\TexturePackerTest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */