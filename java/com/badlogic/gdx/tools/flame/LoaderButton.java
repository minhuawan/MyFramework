/*     */ package com.badlogic.gdx.tools.flame;
/*     */ import com.badlogic.gdx.assets.loaders.AssetLoader;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
/*     */ import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
/*     */ import com.badlogic.gdx.utils.BaseJsonReader;
/*     */ import com.badlogic.gdx.utils.JsonReader;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JOptionPane;
/*     */ 
/*     */ public abstract class LoaderButton<T> extends JButton {
/*     */   private String lastDir;
/*     */   protected Listener<T> listener;
/*     */   FlameMain editor;
/*     */   
/*     */   public static class ParticleEffectLoaderButton extends LoaderButton<ParticleEffect> {
/*     */     public ParticleEffectLoaderButton(FlameMain editor) {
/*  24 */       this(editor, (LoaderButton.Listener<ParticleEffect>)null);
/*     */     }
/*     */     public ParticleEffectLoaderButton(FlameMain editor, LoaderButton.Listener<ParticleEffect> listener) {
/*  27 */       super(editor, "Load Controller", listener);
/*     */     }
/*     */     
/*     */     protected void loadResource() {
/*  31 */       File file = this.editor.showFileLoadDialog();
/*  32 */       if (file != null)
/*     */         try {
/*  34 */           String resource = file.getAbsolutePath();
/*  35 */           this.listener.onResourceLoaded(this.editor.openEffect(file, false));
/*  36 */         } catch (Exception ex) {
/*  37 */           System.out.println("Error loading effect: " + file.getAbsolutePath());
/*  38 */           ex.printStackTrace();
/*  39 */           JOptionPane.showMessageDialog(getParent(), "Error opening effect.");
/*     */           return;
/*     */         }  
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ModelLoaderButton
/*     */     extends LoaderButton<Model> {
/*     */     public ModelLoaderButton(FlameMain editor) {
/*  48 */       this(editor, (LoaderButton.Listener<Model>)null);
/*     */     }
/*     */     public ModelLoaderButton(FlameMain editor, LoaderButton.Listener<Model> listener) {
/*  51 */       super(editor, "Load Model", listener);
/*     */     }
/*     */     
/*     */     protected void loadResource() {
/*  55 */       File file = this.editor.showFileLoadDialog();
/*  56 */       if (file != null) {
/*     */         try {
/*  58 */           G3dModelLoader g3dModelLoader; String resource = file.getAbsolutePath();
/*  59 */           ModelLoader modelLoader = null;
/*  60 */           if (resource.endsWith(".obj")) {
/*  61 */             ObjLoader objLoader = new ObjLoader((FileHandleResolver)new AbsoluteFileHandleResolver());
/*     */           }
/*  63 */           else if (resource.endsWith(".g3dj")) {
/*  64 */             g3dModelLoader = new G3dModelLoader((BaseJsonReader)new JsonReader(), (FileHandleResolver)new AbsoluteFileHandleResolver());
/*     */           }
/*  66 */           else if (resource.endsWith(".g3db")) {
/*  67 */             g3dModelLoader = new G3dModelLoader((BaseJsonReader)new UBJsonReader(), (FileHandleResolver)new AbsoluteFileHandleResolver());
/*     */           } else {
/*  69 */             throw new Exception();
/*  70 */           }  this.listener.onResourceLoaded(this.editor.load(resource, Model.class, (AssetLoader)g3dModelLoader, null));
/*     */         }
/*  72 */         catch (Exception ex) {
/*  73 */           System.out.println("Error loading model: " + file.getAbsolutePath());
/*  74 */           ex.printStackTrace();
/*  75 */           JOptionPane.showMessageDialog(getParent(), "Error opening effect.");
/*     */           return;
/*     */         } 
/*     */       }
/*     */     }
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
/*     */   public LoaderButton(FlameMain editor, String text, Listener<T> listener) {
/*  91 */     super(text);
/*  92 */     this.editor = editor;
/*  93 */     this.listener = listener;
/*  94 */     addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  97 */             LoaderButton.this.loadResource();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public LoaderButton(FlameMain editor, String text) {
/* 103 */     this(editor, text, (Listener<T>)null);
/*     */   }
/*     */   
/*     */   public void setListener(Listener<T> listener) {
/* 107 */     this.listener = listener;
/*     */   }
/*     */   
/*     */   protected abstract void loadResource();
/*     */   
/*     */   public static interface Listener<T> {
/*     */     void onResourceLoaded(T param1T);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\LoaderButton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */