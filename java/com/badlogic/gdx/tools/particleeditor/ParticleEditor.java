/*     */ package com.badlogic.gdx.tools.particleeditor;
/*     */ 
/*     */ import com.badlogic.gdx.ApplicationListener;
/*     */ import com.badlogic.gdx.Files;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.InputProcessor;
/*     */ import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.OrthographicCamera;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEffect;
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*     */ import com.badlogic.gdx.graphics.g2d.Sprite;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.io.File;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.CompoundBorder;
/*     */ import javax.swing.plaf.basic.BasicSplitPaneUI;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticleEditor
/*     */   extends JFrame
/*     */ {
/*     */   public static final String DEFAULT_PARTICLE = "particle.png";
/*     */   public static final String DEFAULT_PREMULT_PARTICLE = "pre_particle.png";
/*     */   LwjglCanvas lwjglCanvas;
/*     */   JPanel rowsPanel;
/*     */   JPanel editRowsPanel;
/*     */   EffectPanel effectPanel;
/*     */   private JSplitPane splitPane;
/*     */   OrthographicCamera worldCamera;
/*     */   OrthographicCamera textCamera;
/*     */   ParticleEmitter.NumericValue pixelsPerMeter;
/*     */   ParticleEmitter.NumericValue zoomLevel;
/*     */   ParticleEmitter.NumericValue deltaMultiplier;
/*     */   ParticleEmitter.GradientColorValue backgroundColor;
/*     */   float pixelsPerMeterPrev;
/*     */   float zoomLevelPrev;
/*  72 */   ParticleEffect effect = new ParticleEffect();
/*     */   File effectFile;
/*  74 */   final HashMap<ParticleEmitter, ParticleData> particleData = new HashMap<ParticleEmitter, ParticleData>();
/*     */   
/*     */   public ParticleEditor() {
/*  77 */     super("Particle Editor");
/*     */     
/*  79 */     this.lwjglCanvas = new LwjglCanvas(new Renderer());
/*  80 */     addWindowListener(new WindowAdapter() {
/*     */           public void windowClosed(WindowEvent event) {
/*  82 */             System.exit(0);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  87 */     initializeComponents();
/*     */     
/*  89 */     setSize(1000, 950);
/*  90 */     setLocationRelativeTo((Component)null);
/*  91 */     setDefaultCloseOperation(2);
/*  92 */     setVisible(true);
/*     */   }
/*     */   
/*     */   void reloadRows() {
/*  96 */     EventQueue.invokeLater(new Runnable() {
/*     */           public void run() {
/*  98 */             ParticleEditor.this.editRowsPanel.removeAll();
/*  99 */             ParticleEditor.this.addEditorRow(new NumericPanel(ParticleEditor.this.pixelsPerMeter, "Pixels per meter", ""));
/* 100 */             ParticleEditor.this.addEditorRow(new NumericPanel(ParticleEditor.this.zoomLevel, "Zoom level", ""));
/* 101 */             ParticleEditor.this.addEditorRow(new NumericPanel(ParticleEditor.this.deltaMultiplier, "Delta multiplier", ""));
/* 102 */             ParticleEditor.this.addEditorRow(new GradientPanel(ParticleEditor.this.backgroundColor, "Background color", "", true));
/*     */             
/* 104 */             ParticleEditor.this.rowsPanel.removeAll();
/* 105 */             ParticleEmitter emitter = ParticleEditor.this.getEmitter();
/* 106 */             ParticleEditor.this.addRow(new ImagePanel(ParticleEditor.this, "Image", ""));
/* 107 */             ParticleEditor.this.addRow(new CountPanel(ParticleEditor.this, "Count", "Min number of particles at all times, max number of particles allowed."));
/*     */             
/* 109 */             ParticleEditor.this.addRow(new RangedNumericPanel(emitter.getDelay(), "Delay", "Time from beginning of effect to emission start, in milliseconds."));
/*     */             
/* 111 */             ParticleEditor.this.addRow(new RangedNumericPanel(emitter.getDuration(), "Duration", "Time particles will be emitted, in milliseconds."));
/* 112 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getEmission(), "Duration", "Emission", "Number of particles emitted per second."));
/*     */             
/* 114 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getLife(), "Duration", "Life", "Time particles will live, in milliseconds."));
/* 115 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getLifeOffset(), "Duration", "Life Offset", "Particle starting life consumed, in milliseconds."));
/*     */             
/* 117 */             ParticleEditor.this.addRow(new RangedNumericPanel(emitter.getXOffsetValue(), "X Offset", "Amount to offset a particle's starting X location, in world units."));
/*     */             
/* 119 */             ParticleEditor.this.addRow(new RangedNumericPanel(emitter.getYOffsetValue(), "Y Offset", "Amount to offset a particle's starting Y location, in world units."));
/*     */             
/* 121 */             ParticleEditor.this.addRow(new SpawnPanel(ParticleEditor.this, emitter.getSpawnShape(), "Spawn", "Shape used to spawn particles."));
/* 122 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getSpawnWidth(), "Duration", "Spawn Width", "Width of the spawn shape, in world units."));
/*     */             
/* 124 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getSpawnHeight(), "Duration", "Spawn Height", "Height of the spawn shape, in world units."));
/*     */             
/* 126 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getScale(), "Life", "Size", "Particle size, in world units."));
/* 127 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getVelocity(), "Life", "Velocity", "Particle speed, in world units per second."));
/* 128 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getAngle(), "Life", "Angle", "Particle emission angle, in degrees."));
/* 129 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getRotation(), "Life", "Rotation", "Particle rotation, in degrees."));
/* 130 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getWind(), "Life", "Wind", "Wind strength, in world units per second."));
/* 131 */             ParticleEditor.this.addRow(new ScaledNumericPanel(emitter.getGravity(), "Life", "Gravity", "Gravity strength, in world units per second."));
/* 132 */             ParticleEditor.this.addRow(new GradientPanel(emitter.getTint(), "Tint", "", false));
/* 133 */             ParticleEditor.this.addRow(new PercentagePanel(emitter.getTransparency(), "Life", "Transparency", ""));
/* 134 */             ParticleEditor.this.addRow(new OptionsPanel(ParticleEditor.this, "Options", ""));
/* 135 */             for (Component component : ParticleEditor.this.rowsPanel.getComponents()) {
/* 136 */               if (component instanceof EditorPanel) ((EditorPanel)component).update(ParticleEditor.this); 
/* 137 */             }  ParticleEditor.this.rowsPanel.repaint();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   void addEditorRow(JPanel row) {
/* 143 */     row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
/* 144 */     this.editRowsPanel.add(row, new GridBagConstraints(0, -1, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   void addRow(JPanel row) {
/* 149 */     row.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black));
/* 150 */     this.rowsPanel.add(row, new GridBagConstraints(0, -1, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVisible(String name, boolean visible) {
/* 155 */     for (Component component : this.rowsPanel.getComponents()) {
/* 156 */       if (component instanceof EditorPanel && ((EditorPanel)component).getName().equals(name)) component.setVisible(visible); 
/*     */     } 
/*     */   }
/*     */   public ParticleEmitter getEmitter() {
/* 160 */     return (ParticleEmitter)this.effect.getEmitters().get(this.effectPanel.editIndex);
/*     */   }
/*     */   
/*     */   public ImageIcon getIcon(ParticleEmitter emitter) {
/* 164 */     ParticleData data = this.particleData.get(emitter);
/* 165 */     if (data == null) this.particleData.put(emitter, data = new ParticleData()); 
/* 166 */     String imagePath = emitter.getImagePath();
/* 167 */     if (data.icon == null && imagePath != null) {
/*     */       try {
/*     */         URL url;
/* 170 */         File file = new File(imagePath);
/* 171 */         if (file.exists()) {
/* 172 */           url = file.toURI().toURL();
/*     */         } else {
/* 174 */           url = ParticleEditor.class.getResource(imagePath);
/* 175 */           if (url == null) return null; 
/*     */         } 
/* 177 */         data.icon = new ImageIcon(url);
/* 178 */       } catch (MalformedURLException ex) {
/* 179 */         ex.printStackTrace();
/*     */       } 
/*     */     }
/* 182 */     return data.icon;
/*     */   }
/*     */   
/*     */   public void setIcon(ParticleEmitter emitters, ImageIcon icon) {
/* 186 */     ParticleData data = this.particleData.get(emitters);
/* 187 */     if (data == null) this.particleData.put(emitters, data = new ParticleData()); 
/* 188 */     data.icon = icon;
/*     */   }
/*     */   
/*     */   public void setEnabled(ParticleEmitter emitter, boolean enabled) {
/* 192 */     ParticleData data = this.particleData.get(emitter);
/* 193 */     if (data == null) this.particleData.put(emitter, data = new ParticleData()); 
/* 194 */     data.enabled = enabled;
/* 195 */     emitter.reset();
/*     */   }
/*     */   
/*     */   public boolean isEnabled(ParticleEmitter emitter) {
/* 199 */     ParticleData data = this.particleData.get(emitter);
/* 200 */     if (data == null) return true; 
/* 201 */     return data.enabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeComponents() {
/* 212 */     this.splitPane = new JSplitPane();
/* 213 */     this.splitPane.setUI(new BasicSplitPaneUI()
/*     */         {
/*     */           public void paint(Graphics g, JComponent jc) {}
/*     */         });
/* 217 */     this.splitPane.setDividerSize(4);
/* 218 */     getContentPane().add(this.splitPane, "Center");
/*     */     
/* 220 */     JSplitPane rightSplit = new JSplitPane(0);
/* 221 */     rightSplit.setUI(new BasicSplitPaneUI()
/*     */         {
/*     */           public void paint(Graphics g, JComponent jc) {}
/*     */         });
/* 225 */     rightSplit.setDividerSize(4);
/* 226 */     this.splitPane.add(rightSplit, "right");
/*     */ 
/*     */     
/* 229 */     JPanel propertiesPanel = new JPanel(new GridBagLayout());
/* 230 */     rightSplit.add(propertiesPanel, "top");
/* 231 */     propertiesPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(3, 0, 6, 6), 
/* 232 */           BorderFactory.createTitledBorder("Editor Properties")));
/*     */     
/* 234 */     JScrollPane scroll = new JScrollPane();
/* 235 */     propertiesPanel.add(scroll, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 11, 1, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/* 237 */     scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/*     */     
/* 239 */     this.editRowsPanel = new JPanel(new GridBagLayout());
/* 240 */     scroll.setViewportView(this.editRowsPanel);
/* 241 */     scroll.getVerticalScrollBar().setUnitIncrement(70);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 247 */     propertiesPanel = new JPanel(new GridBagLayout());
/* 248 */     rightSplit.add(propertiesPanel, "bottom");
/* 249 */     propertiesPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(3, 0, 6, 6), 
/* 250 */           BorderFactory.createTitledBorder("Emitter Properties")));
/*     */     
/* 252 */     scroll = new JScrollPane();
/* 253 */     propertiesPanel.add(scroll, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 11, 1, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/* 255 */     scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/*     */     
/* 257 */     this.rowsPanel = new JPanel(new GridBagLayout());
/* 258 */     scroll.setViewportView(this.rowsPanel);
/* 259 */     scroll.getVerticalScrollBar().setUnitIncrement(70);
/*     */ 
/*     */ 
/*     */     
/* 263 */     rightSplit.setDividerLocation(200);
/*     */ 
/*     */ 
/*     */     
/* 267 */     JSplitPane leftSplit = new JSplitPane(0);
/* 268 */     leftSplit.setUI(new BasicSplitPaneUI()
/*     */         {
/*     */           public void paint(Graphics g, JComponent jc) {}
/*     */         });
/* 272 */     leftSplit.setDividerSize(4);
/* 273 */     this.splitPane.add(leftSplit, "left");
/*     */     
/* 275 */     JPanel spacer = new JPanel(new BorderLayout());
/* 276 */     leftSplit.add(spacer, "bottom");
/* 277 */     spacer.add(this.lwjglCanvas.getCanvas());
/* 278 */     spacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
/*     */ 
/*     */     
/* 281 */     JPanel emittersPanel = new JPanel(new BorderLayout());
/* 282 */     leftSplit.add(emittersPanel, "top");
/* 283 */     emittersPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(0, 6, 6, 0), 
/* 284 */           BorderFactory.createTitledBorder("Effect Emitters")));
/*     */     
/* 286 */     this.effectPanel = new EffectPanel(this);
/* 287 */     emittersPanel.add(this.effectPanel);
/*     */ 
/*     */     
/* 290 */     leftSplit.setDividerLocation(575);
/*     */     
/* 292 */     this.splitPane.setDividerLocation(325);
/*     */   }
/*     */ 
/*     */   
/*     */   class Renderer
/*     */     implements ApplicationListener, InputProcessor
/*     */   {
/*     */     private float maxActiveTimer;
/*     */     private int maxActive;
/*     */     private int lastMaxActive;
/*     */     private boolean mouseDown;
/*     */     private int activeCount;
/*     */     
/*     */     public void create() {
/* 306 */       if (this.spriteBatch != null)
/*     */         return; 
/* 308 */       this.spriteBatch = new SpriteBatch();
/*     */       
/* 310 */       ParticleEditor.this.worldCamera = new OrthographicCamera();
/* 311 */       ParticleEditor.this.textCamera = new OrthographicCamera();
/*     */       
/* 313 */       ParticleEditor.this.pixelsPerMeter = new ParticleEmitter.NumericValue();
/* 314 */       ParticleEditor.this.pixelsPerMeter.setValue(1.0F);
/* 315 */       ParticleEditor.this.pixelsPerMeter.setAlwaysActive(true);
/*     */       
/* 317 */       ParticleEditor.this.zoomLevel = new ParticleEmitter.NumericValue();
/* 318 */       ParticleEditor.this.zoomLevel.setValue(1.0F);
/* 319 */       ParticleEditor.this.zoomLevel.setAlwaysActive(true);
/*     */       
/* 321 */       ParticleEditor.this.deltaMultiplier = new ParticleEmitter.NumericValue();
/* 322 */       ParticleEditor.this.deltaMultiplier.setValue(1.0F);
/* 323 */       ParticleEditor.this.deltaMultiplier.setAlwaysActive(true);
/*     */       
/* 325 */       ParticleEditor.this.backgroundColor = new ParticleEmitter.GradientColorValue();
/* 326 */       ParticleEditor.this.backgroundColor.setColors(new float[] { 0.0F, 0.0F, 0.0F });
/*     */       
/* 328 */       this.font = new BitmapFont(Gdx.files.getFileHandle("default.fnt", Files.FileType.Internal), Gdx.files.getFileHandle("default.png", Files.FileType.Internal), true);
/*     */       
/* 330 */       ParticleEditor.this.effectPanel.newExampleEmitter("Untitled", true);
/*     */       
/* 332 */       Gdx.input.setInputProcessor(this);
/*     */     }
/*     */     private int mouseX; private int mouseY; private BitmapFont font; private SpriteBatch spriteBatch; private Sprite bgImage;
/*     */     
/*     */     public void resize(int width, int height) {
/* 337 */       Gdx.gl.glViewport(0, 0, width, height);
/*     */       
/* 339 */       if (ParticleEditor.this.pixelsPerMeter.getValue() <= 0.0F) {
/* 340 */         ParticleEditor.this.pixelsPerMeter.setValue(1.0F);
/*     */       }
/* 342 */       ParticleEditor.this.worldCamera.setToOrtho(false, width / ParticleEditor.this.pixelsPerMeter.getValue(), height / ParticleEditor.this.pixelsPerMeter.getValue());
/* 343 */       ParticleEditor.this.worldCamera.update();
/*     */       
/* 345 */       ParticleEditor.this.textCamera.setToOrtho(true, width, height);
/* 346 */       ParticleEditor.this.textCamera.update();
/*     */       
/* 348 */       ParticleEditor.this.effect.setPosition(ParticleEditor.this.worldCamera.viewportWidth / 2.0F, ParticleEditor.this.worldCamera.viewportHeight / 2.0F);
/*     */     }
/*     */     
/*     */     public void render() {
/* 352 */       int viewWidth = Gdx.graphics.getWidth();
/* 353 */       int viewHeight = Gdx.graphics.getHeight();
/*     */       
/* 355 */       float delta = Math.max(0.0F, Gdx.graphics.getDeltaTime() * ParticleEditor.this.deltaMultiplier.getValue());
/*     */       
/* 357 */       float[] colors = ParticleEditor.this.backgroundColor.getColors();
/* 358 */       Gdx.gl.glClearColor(colors[0], colors[1], colors[2], 1.0F);
/* 359 */       Gdx.gl.glClear(16384);
/*     */       
/* 361 */       if (ParticleEditor.this.pixelsPerMeter.getValue() != ParticleEditor.this.pixelsPerMeterPrev || ParticleEditor.this.zoomLevel.getValue() != ParticleEditor.this.zoomLevelPrev) {
/* 362 */         if (ParticleEditor.this.pixelsPerMeter.getValue() <= 0.0F) {
/* 363 */           ParticleEditor.this.pixelsPerMeter.setValue(1.0F);
/*     */         }
/*     */         
/* 366 */         ParticleEditor.this.worldCamera.setToOrtho(false, viewWidth / ParticleEditor.this.pixelsPerMeter.getValue(), viewHeight / ParticleEditor.this.pixelsPerMeter.getValue());
/* 367 */         ParticleEditor.this.worldCamera.zoom = ParticleEditor.this.zoomLevel.getValue();
/* 368 */         ParticleEditor.this.worldCamera.update();
/* 369 */         ParticleEditor.this.effect.setPosition(ParticleEditor.this.worldCamera.viewportWidth / 2.0F, ParticleEditor.this.worldCamera.viewportHeight / 2.0F);
/* 370 */         ParticleEditor.this.zoomLevelPrev = ParticleEditor.this.zoomLevel.getValue();
/* 371 */         ParticleEditor.this.pixelsPerMeterPrev = ParticleEditor.this.pixelsPerMeter.getValue();
/*     */       } 
/*     */       
/* 374 */       this.spriteBatch.setProjectionMatrix(ParticleEditor.this.worldCamera.combined);
/*     */       
/* 376 */       this.spriteBatch.begin();
/* 377 */       this.spriteBatch.enableBlending();
/* 378 */       this.spriteBatch.setBlendFunction(770, 771);
/*     */       
/* 380 */       if (this.bgImage != null) {
/* 381 */         this.bgImage.setPosition((viewWidth / 2) - this.bgImage.getWidth() / 2.0F, (viewHeight / 2) - this.bgImage.getHeight() / 2.0F);
/* 382 */         this.bgImage.draw((Batch)this.spriteBatch);
/*     */       } 
/*     */       
/* 385 */       this.activeCount = 0;
/* 386 */       boolean complete = true;
/* 387 */       for (ParticleEmitter emitter : ParticleEditor.this.effect.getEmitters()) {
/* 388 */         if (emitter.getSprite() == null && emitter.getImagePath() != null) loadImage(emitter); 
/* 389 */         boolean enabled = ParticleEditor.this.isEnabled(emitter);
/* 390 */         if (enabled) {
/* 391 */           if (emitter.getSprite() != null) emitter.draw((Batch)this.spriteBatch, delta); 
/* 392 */           this.activeCount += emitter.getActiveCount();
/* 393 */           if (!emitter.isComplete()) complete = false; 
/*     */         } 
/*     */       } 
/* 396 */       if (complete) ParticleEditor.this.effect.start();
/*     */       
/* 398 */       this.maxActive = Math.max(this.maxActive, this.activeCount);
/* 399 */       this.maxActiveTimer += delta;
/* 400 */       if (this.maxActiveTimer > 3.0F) {
/* 401 */         this.maxActiveTimer = 0.0F;
/* 402 */         this.lastMaxActive = this.maxActive;
/* 403 */         this.maxActive = 0;
/*     */       } 
/*     */       
/* 406 */       if (this.mouseDown);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 411 */       this.spriteBatch.setProjectionMatrix(ParticleEditor.this.textCamera.combined);
/*     */       
/* 413 */       this.font.draw((Batch)this.spriteBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5.0F, 15.0F);
/* 414 */       this.font.draw((Batch)this.spriteBatch, "Count: " + this.activeCount, 5.0F, 35.0F);
/* 415 */       this.font.draw((Batch)this.spriteBatch, "Max: " + this.lastMaxActive, 5.0F, 55.0F);
/* 416 */       this.font.draw((Batch)this.spriteBatch, (int)(ParticleEditor.this.getEmitter().getPercentComplete() * 100.0F) + "%", 5.0F, 75.0F);
/*     */       
/* 418 */       this.spriteBatch.end();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private void loadImage(ParticleEmitter emitter) {
/* 425 */       final String imagePath = emitter.getImagePath();
/* 426 */       String imageName = (new File(imagePath.replace('\\', '/'))).getName();
/*     */       try {
/*     */         FileHandle file;
/* 429 */         if (imagePath.equals("particle.png") || imagePath.equals("pre_particle.png")) {
/* 430 */           file = Gdx.files.classpath(imagePath);
/*     */         }
/* 432 */         else if ((imagePath.contains("/") || imagePath.contains("\\")) && !imageName.contains("..")) {
/* 433 */           file = Gdx.files.absolute(imagePath);
/* 434 */           if (!file.exists())
/*     */           {
/* 436 */             file = Gdx.files.absolute((new File(ParticleEditor.this.effectFile.getParentFile(), imageName)).getAbsolutePath());
/*     */           }
/*     */         } else {
/* 439 */           file = Gdx.files.absolute((new File(ParticleEditor.this.effectFile.getParentFile(), imagePath)).getAbsolutePath());
/*     */         } 
/*     */         
/* 442 */         emitter.setSprite(new Sprite(new Texture(file)));
/* 443 */         if (ParticleEditor.this.effectFile != null) {
/* 444 */           URI relativeUri = ParticleEditor.this.effectFile.getParentFile().toURI().relativize(file.file().toURI());
/* 445 */           emitter.setImagePath(relativeUri.getPath());
/*     */         } 
/* 447 */       } catch (GdxRuntimeException ex) {
/* 448 */         ex.printStackTrace();
/* 449 */         EventQueue.invokeLater(new Runnable() {
/*     */               public void run() {
/* 451 */                 JOptionPane.showMessageDialog(ParticleEditor.this, "Error loading image:\n" + imagePath);
/*     */               }
/*     */             });
/* 454 */         emitter.setImagePath(null);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean keyDown(int keycode) {
/* 459 */       return false;
/*     */     }
/*     */     
/*     */     public boolean keyUp(int keycode) {
/* 463 */       return false;
/*     */     }
/*     */     
/*     */     public boolean keyTyped(char character) {
/* 467 */       return false;
/*     */     }
/*     */     
/*     */     public boolean touchDown(int x, int y, int pointer, int newParam) {
/* 471 */       Vector3 touchPoint = new Vector3(x, y, 0.0F);
/* 472 */       ParticleEditor.this.worldCamera.unproject(touchPoint);
/* 473 */       ParticleEditor.this.effect.setPosition(touchPoint.x, touchPoint.y);
/* 474 */       return false;
/*     */     }
/*     */     
/*     */     public boolean touchUp(int x, int y, int pointer, int button) {
/* 478 */       ParticleEditor.this.dispatchEvent(new WindowEvent(ParticleEditor.this, 208));
/* 479 */       ParticleEditor.this.dispatchEvent(new WindowEvent(ParticleEditor.this, 207));
/* 480 */       ParticleEditor.this.requestFocusInWindow();
/* 481 */       return false;
/*     */     }
/*     */     
/*     */     public boolean touchDragged(int x, int y, int pointer) {
/* 485 */       Vector3 touchPoint = new Vector3(x, y, 0.0F);
/* 486 */       ParticleEditor.this.worldCamera.unproject(touchPoint);
/* 487 */       ParticleEditor.this.effect.setPosition(touchPoint.x, touchPoint.y);
/* 488 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void dispose() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void pause() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public void resume() {}
/*     */ 
/*     */     
/*     */     public boolean mouseMoved(int x, int y) {
/* 505 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean scrolled(int amount) {
/* 510 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   static class ParticleData {
/*     */     public ImageIcon icon;
/*     */     public String imagePath;
/*     */     public boolean enabled = true;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 521 */     for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
/* 522 */       if ("Nimbus".equals(info.getName())) {
/*     */         try {
/* 524 */           UIManager.setLookAndFeel(info.getClassName());
/* 525 */         } catch (Throwable throwable) {}
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 530 */     EventQueue.invokeLater(new Runnable() {
/*     */           public void run() {
/* 532 */             new ParticleEditor();
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\ParticleEditor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */