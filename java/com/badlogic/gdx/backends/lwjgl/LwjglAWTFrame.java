/*     */ package com.badlogic.gdx.backends.lwjgl;
/*     */ 
/*     */ import com.badlogic.gdx.ApplicationListener;
/*     */ import java.awt.Dimension;
/*     */ import javax.swing.JFrame;
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
/*     */ public class LwjglAWTFrame
/*     */   extends JFrame
/*     */ {
/*     */   final LwjglAWTCanvas lwjglAWTCanvas;
/*     */   private Thread shutdownHook;
/*     */   
/*     */   public LwjglAWTFrame(ApplicationListener listener, String title, int width, int height) {
/*  31 */     super(title);
/*     */     
/*  33 */     this.lwjglAWTCanvas = new LwjglAWTCanvas(listener) {
/*     */         protected void stopped() {
/*  35 */           LwjglAWTFrame.this.dispose();
/*     */         }
/*     */         
/*     */         protected void setTitle(String title) {
/*  39 */           LwjglAWTFrame.this.setTitle(title);
/*     */         }
/*     */         
/*     */         protected void setDisplayMode(int width, int height) {
/*  43 */           LwjglAWTFrame.this.getContentPane().setPreferredSize(new Dimension(width, height));
/*  44 */           LwjglAWTFrame.this.getContentPane().invalidate();
/*  45 */           LwjglAWTFrame.this.pack();
/*  46 */           LwjglAWTFrame.this.setLocationRelativeTo(null);
/*  47 */           LwjglAWTFrame.this.updateSize(width, height);
/*     */         }
/*     */         
/*     */         protected void resize(int width, int height) {
/*  51 */           LwjglAWTFrame.this.updateSize(width, height);
/*     */         }
/*     */         
/*     */         protected void start() {
/*  55 */           LwjglAWTFrame.this.start();
/*     */         }
/*     */       };
/*  58 */     getContentPane().add(this.lwjglAWTCanvas.getCanvas());
/*     */     
/*  60 */     setHaltOnShutdown(true);
/*     */     
/*  62 */     setDefaultCloseOperation(3);
/*  63 */     getContentPane().setPreferredSize(new Dimension(width, height));
/*  64 */     initialize();
/*  65 */     pack();
/*  66 */     setLocationRelativeTo(null);
/*  67 */     setVisible(true);
/*  68 */     this.lwjglAWTCanvas.getCanvas().requestFocus();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHaltOnShutdown(boolean halt) {
/*  74 */     if (halt) {
/*  75 */       if (this.shutdownHook != null)
/*  76 */         return;  this.shutdownHook = new Thread() {
/*     */           public void run() {
/*  78 */             Runtime.getRuntime().halt(0);
/*     */           }
/*     */         };
/*  81 */       Runtime.getRuntime().addShutdownHook(this.shutdownHook);
/*  82 */     } else if (this.shutdownHook != null) {
/*  83 */       Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
/*  84 */       this.shutdownHook = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initialize() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start() {}
/*     */ 
/*     */   
/*     */   public void updateSize(int width, int height) {}
/*     */ 
/*     */   
/*     */   public LwjglAWTCanvas getLwjglAWTCanvas() {
/* 101 */     return this.lwjglAWTCanvas;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglAWTFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */