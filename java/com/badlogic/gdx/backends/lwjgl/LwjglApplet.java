/*    */ package com.badlogic.gdx.backends.lwjgl;
/*    */ 
/*    */ import com.badlogic.gdx.Application;
/*    */ import com.badlogic.gdx.ApplicationListener;
/*    */ import java.applet.Applet;
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Canvas;
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
/*    */ public class LwjglApplet
/*    */   extends Applet
/*    */ {
/*    */   final Canvas canvas;
/*    */   LwjglApplication app;
/*    */   
/*    */   class LwjglAppletApplication
/*    */     extends LwjglApplication
/*    */   {
/*    */     public LwjglAppletApplication(ApplicationListener listener, Canvas canvas) {
/* 34 */       super(listener, canvas);
/*    */     }
/*    */     
/*    */     public LwjglAppletApplication(ApplicationListener listener, Canvas canvas, LwjglApplicationConfiguration config) {
/* 38 */       super(listener, config, canvas);
/*    */     }
/*    */ 
/*    */     
/*    */     public Application.ApplicationType getType() {
/* 43 */       return Application.ApplicationType.Applet;
/*    */     }
/*    */   }
/*    */   
/*    */   public LwjglApplet(final ApplicationListener listener, final LwjglApplicationConfiguration config) {
/* 48 */     LwjglNativesLoader.load = false;
/* 49 */     this.canvas = new Canvas() {
/*    */         public final void addNotify() {
/* 51 */           super.addNotify();
/* 52 */           LwjglApplet.this.app = new LwjglApplet.LwjglAppletApplication(listener, LwjglApplet.this.canvas, config);
/*    */         }
/*    */         
/*    */         public final void removeNotify() {
/* 56 */           LwjglApplet.this.app.stop();
/* 57 */           super.removeNotify();
/*    */         }
/*    */       };
/* 60 */     setLayout(new BorderLayout());
/* 61 */     this.canvas.setIgnoreRepaint(true);
/* 62 */     add(this.canvas);
/* 63 */     this.canvas.setFocusable(true);
/* 64 */     this.canvas.requestFocus();
/*    */   }
/*    */   
/*    */   public LwjglApplet(final ApplicationListener listener) {
/* 68 */     LwjglNativesLoader.load = false;
/* 69 */     this.canvas = new Canvas() {
/*    */         public final void addNotify() {
/* 71 */           super.addNotify();
/* 72 */           LwjglApplet.this.app = new LwjglApplet.LwjglAppletApplication(listener, LwjglApplet.this.canvas);
/*    */         }
/*    */         
/*    */         public final void removeNotify() {
/* 76 */           LwjglApplet.this.app.stop();
/* 77 */           super.removeNotify();
/*    */         }
/*    */       };
/* 80 */     setLayout(new BorderLayout());
/* 81 */     this.canvas.setIgnoreRepaint(true);
/* 82 */     add(this.canvas);
/* 83 */     this.canvas.setFocusable(true);
/* 84 */     this.canvas.requestFocus();
/*    */   }
/*    */   
/*    */   public void destroy() {
/* 88 */     remove(this.canvas);
/* 89 */     super.destroy();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\backends\lwjgl\LwjglApplet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */