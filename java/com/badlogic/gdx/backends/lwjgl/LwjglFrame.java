/*     */ package com.badlogic.gdx.backends.lwjgl;
/*     */ 
/*     */ import com.badlogic.gdx.ApplicationListener;
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Point;
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
/*     */ public class LwjglFrame
/*     */   extends JFrame
/*     */ {
/*     */   LwjglCanvas lwjglCanvas;
/*     */   private Thread shutdownHook;
/*     */   
/*     */   public LwjglFrame(ApplicationListener listener, String title, int width, int height) {
/*  33 */     super(title);
/*  34 */     LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
/*  35 */     config.title = title;
/*  36 */     config.width = width;
/*  37 */     config.height = height;
/*  38 */     construct(listener, config);
/*     */   }
/*     */   
/*     */   public LwjglFrame(ApplicationListener listener, LwjglApplicationConfiguration config) {
/*  42 */     super(config.title);
/*  43 */     construct(listener, config);
/*     */   }
/*     */   
/*     */   private void construct(ApplicationListener listener, LwjglApplicationConfiguration config) {
/*  47 */     this.lwjglCanvas = new LwjglCanvas(listener, config) {
/*     */         protected void stopped() {
/*  49 */           LwjglFrame.this.dispose();
/*     */         }
/*     */         
/*     */         protected void setTitle(String title) {
/*  53 */           LwjglFrame.this.setTitle(title);
/*     */         }
/*     */         
/*     */         protected void setDisplayMode(int width, int height) {
/*  57 */           LwjglFrame.this.getContentPane().setPreferredSize(new Dimension(width, height));
/*  58 */           LwjglFrame.this.getContentPane().invalidate();
/*  59 */           LwjglFrame.this.pack();
/*  60 */           LwjglFrame.this.setLocationRelativeTo((Component)null);
/*  61 */           LwjglFrame.this.updateSize(width, height);
/*     */         }
/*     */         
/*     */         protected void resize(int width, int height) {
/*  65 */           LwjglFrame.this.updateSize(width, height);
/*     */         }
/*     */         
/*     */         protected void start() {
/*  69 */           LwjglFrame.this.start();
/*     */         }
/*     */         
/*     */         protected void exception(Throwable t) {
/*  73 */           LwjglFrame.this.exception(t);
/*     */         }
/*     */         
/*     */         protected int getFrameRate() {
/*  77 */           int frameRate = LwjglFrame.this.getFrameRate();
/*  78 */           return (frameRate == 0) ? super.getFrameRate() : frameRate;
/*     */         }
/*     */       };
/*     */     
/*  82 */     setHaltOnShutdown(true);
/*     */     
/*  84 */     setDefaultCloseOperation(3);
/*  85 */     getContentPane().setPreferredSize(new Dimension(config.width, config.height));
/*     */     
/*  87 */     initialize();
/*  88 */     pack();
/*  89 */     Point location = getLocation();
/*  90 */     if (location.x == 0 && location.y == 0) setLocationRelativeTo((Component)null); 
/*  91 */     this.lwjglCanvas.getCanvas().setSize(getSize());
/*     */ 
/*     */     
/*  94 */     EventQueue.invokeLater(new Runnable() {
/*     */           public void run() {
/*  96 */             LwjglFrame.this.addCanvas();
/*  97 */             LwjglFrame.this.setVisible(true);
/*  98 */             LwjglFrame.this.lwjglCanvas.getCanvas().requestFocus();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHaltOnShutdown(boolean halt) {
/* 106 */     if (halt) {
/* 107 */       if (this.shutdownHook != null)
/* 108 */         return;  this.shutdownHook = new Thread() {
/*     */           public void run() {
/* 110 */             Runtime.getRuntime().halt(0);
/*     */           }
/*     */         };
/* 113 */       Runtime.getRuntime().addShutdownHook(this.shutdownHook);
/* 114 */     } else if (this.shutdownHook != null) {
/* 115 */       Runtime.getRuntime().removeShutdownHook(this.shutdownHook);
/* 116 */       this.shutdownHook = null;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected int getFrameRate() {
/* 121 */     return 0;
/*     */   }
/*     */   
/*     */   protected void exception(Throwable ex) {
/* 125 */     ex.printStackTrace();
/* 126 */     this.lwjglCanvas.stop();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void initialize() {}
/*     */ 
/*     */   
/*     */   protected void addCanvas() {
/* 135 */     getContentPane().add(this.lwjglCanvas.getCanvas());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void start() {}
/*     */ 
/*     */   
/*     */   public void updateSize(int width, int height) {}
/*     */ 
/*     */   
/*     */   public LwjglCanvas getLwjglCanvas() {
/* 147 */     return this.lwjglCanvas;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */