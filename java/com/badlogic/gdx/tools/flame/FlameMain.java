/*      */ package com.badlogic.gdx.tools.flame;
/*      */ 
/*      */ import com.badlogic.gdx.ApplicationListener;
/*      */ import com.badlogic.gdx.Gdx;
/*      */ import com.badlogic.gdx.InputMultiplexer;
/*      */ import com.badlogic.gdx.InputProcessor;
/*      */ import com.badlogic.gdx.assets.AssetDescriptor;
/*      */ import com.badlogic.gdx.assets.AssetErrorListener;
/*      */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*      */ import com.badlogic.gdx.assets.AssetManager;
/*      */ import com.badlogic.gdx.assets.loaders.AssetLoader;
/*      */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*      */ import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
/*      */ import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
/*      */ import com.badlogic.gdx.backends.lwjgl.LwjglCanvas;
/*      */ import com.badlogic.gdx.files.FileHandle;
/*      */ import com.badlogic.gdx.graphics.Camera;
/*      */ import com.badlogic.gdx.graphics.Color;
/*      */ import com.badlogic.gdx.graphics.PerspectiveCamera;
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*      */ import com.badlogic.gdx.graphics.g3d.Attribute;
/*      */ import com.badlogic.gdx.graphics.g3d.Environment;
/*      */ import com.badlogic.gdx.graphics.g3d.Material;
/*      */ import com.badlogic.gdx.graphics.g3d.Model;
/*      */ import com.badlogic.gdx.graphics.g3d.ModelBatch;
/*      */ import com.badlogic.gdx.graphics.g3d.ModelInstance;
/*      */ import com.badlogic.gdx.graphics.g3d.RenderableProvider;
/*      */ import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
/*      */ import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
/*      */ import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.batches.BillboardParticleBatch;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.batches.ModelInstanceParticleBatch;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsInfluencer;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ModelInfluencer;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ParticleControllerFinalizerInfluencer;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ParticleControllerInfluencer;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.influencers.RegionInfluencer;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ScaleInfluencer;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.influencers.SpawnInfluencer;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.values.GradientColorValue;
/*      */ import com.badlogic.gdx.graphics.g3d.particles.values.NumericValue;
/*      */ import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
/*      */ import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.math.RandomXS128;
/*      */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*      */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*      */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*      */ import com.badlogic.gdx.scenes.scene2d.Stage;
/*      */ import com.badlogic.gdx.scenes.scene2d.ui.Label;
/*      */ import com.badlogic.gdx.scenes.scene2d.ui.Skin;
/*      */ import com.badlogic.gdx.scenes.scene2d.ui.Table;
/*      */ import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
/*      */ import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/*      */ import com.badlogic.gdx.utils.Array;
/*      */ import com.badlogic.gdx.utils.StreamUtils;
/*      */ import com.badlogic.gdx.utils.StringBuilder;
/*      */ import java.awt.BorderLayout;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.EventQueue;
/*      */ import java.awt.FileDialog;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.GridBagConstraints;
/*      */ import java.awt.GridBagLayout;
/*      */ import java.awt.Insets;
/*      */ import java.awt.event.ActionEvent;
/*      */ import java.awt.event.ActionListener;
/*      */ import java.awt.event.WindowAdapter;
/*      */ import java.awt.event.WindowEvent;
/*      */ import java.io.File;
/*      */ import java.io.Writer;
/*      */ import java.util.Random;
/*      */ import javax.swing.BorderFactory;
/*      */ import javax.swing.DefaultComboBoxModel;
/*      */ import javax.swing.JButton;
/*      */ import javax.swing.JComboBox;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JFrame;
/*      */ import javax.swing.JOptionPane;
/*      */ import javax.swing.JPanel;
/*      */ import javax.swing.JScrollPane;
/*      */ import javax.swing.JSplitPane;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.border.CompoundBorder;
/*      */ import javax.swing.plaf.basic.BasicSplitPaneUI;
/*      */ 
/*      */ public class FlameMain
/*      */   extends JFrame
/*      */   implements AssetErrorListener
/*      */ {
/*      */   public static final String DEFAULT_FONT = "default.fnt";
/*      */   public static final String DEFAULT_BILLBOARD_PARTICLE = "pre_particle.png";
/*      */   public static final String DEFAULT_MODEL_PARTICLE = "monkey.g3db";
/*      */   public static final String DEFAULT_TEMPLATE_PFX = "defaultTemplate.pfx";
/*      */   public static final String DEFAULT_SKIN = "uiskin.json";
/*      */   public static final int EVT_ASSET_RELOADED = 0;
/*      */   LwjglCanvas lwjglCanvas;
/*      */   JPanel controllerPropertiesPanel;
/*      */   JPanel editorPropertiesPanel;
/*      */   EffectPanel effectPanel;
/*      */   JSplitPane splitPane;
/*      */   NumericValue fovValue;
/*      */   NumericValue deltaMultiplier;
/*      */   GradientColorValue backgroundColor;
/*      */   AppRenderer renderer;
/*      */   AssetManager assetManager;
/*      */   JComboBox influencerBox;
/*      */   private ParticleEffect effect;
/*      */   public Array<ControllerData> controllersData;
/*      */   ParticleSystem particleSystem;
/*      */   String lastDir;
/*      */   
/*      */   static class ControllerData {
/*      */     public boolean enabled = true;
/*      */     public ParticleController controller;
/*      */     
/*      */     public ControllerData(ParticleController emitter) {
/*  130 */       this.controller = emitter;
/*      */     } }
/*      */   
/*      */   private static class InfluencerWrapper<T> {
/*      */     String string;
/*      */     Class<Influencer> type;
/*      */     
/*      */     public InfluencerWrapper(String string, Class<Influencer> type) {
/*  138 */       this.string = string;
/*  139 */       this.type = type;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  144 */       return this.string;
/*      */     }
/*      */   }
/*      */   
/*      */   public enum ControllerType {
/*  149 */     Billboard("Billboard", new FlameMain.InfluencerWrapper[] { new FlameMain.InfluencerWrapper("Single Color", (Class)ColorInfluencer.Single.class), new FlameMain.InfluencerWrapper("Random Color", (Class)ColorInfluencer.Random.class), new FlameMain.InfluencerWrapper("Single Region", (Class)RegionInfluencer.Single.class), new FlameMain.InfluencerWrapper("Random Region", (Class)RegionInfluencer.Random.class), new FlameMain.InfluencerWrapper("Animated Region", (Class)RegionInfluencer.Animated.class), new FlameMain.InfluencerWrapper("Scale", (Class)ScaleInfluencer.class), new FlameMain.InfluencerWrapper("Spawn", (Class)SpawnInfluencer.class), new FlameMain.InfluencerWrapper("Dynamics", (Class)DynamicsInfluencer.class)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  158 */     PointSprite("Point Sprite", new FlameMain.InfluencerWrapper[] { new FlameMain.InfluencerWrapper("Single Color", (Class)ColorInfluencer.Single.class), new FlameMain.InfluencerWrapper("Random Color", (Class)ColorInfluencer.Random.class), new FlameMain.InfluencerWrapper("Single Region", (Class)RegionInfluencer.Single.class), new FlameMain.InfluencerWrapper("Random Region", (Class)RegionInfluencer.Random.class), new FlameMain.InfluencerWrapper("Animated Region", (Class)RegionInfluencer.Animated.class), new FlameMain.InfluencerWrapper("Scale", (Class)ScaleInfluencer.class), new FlameMain.InfluencerWrapper("Spawn", (Class)SpawnInfluencer.class), new FlameMain.InfluencerWrapper("Dynamics", (Class)DynamicsInfluencer.class)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  167 */     ModelInstance("Model Instance", new FlameMain.InfluencerWrapper[] { new FlameMain.InfluencerWrapper("Single Color", (Class)ColorInfluencer.Single.class), new FlameMain.InfluencerWrapper("Random Color", (Class)ColorInfluencer.Random.class), new FlameMain.InfluencerWrapper("Single Model", (Class)ModelInfluencer.Single.class), new FlameMain.InfluencerWrapper("Random Model", (Class)ModelInfluencer.Random.class), new FlameMain.InfluencerWrapper("Scale", (Class)ScaleInfluencer.class), new FlameMain.InfluencerWrapper("Spawn", (Class)SpawnInfluencer.class), new FlameMain.InfluencerWrapper("Dynamics", (Class)DynamicsInfluencer.class)
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }),
/*  175 */     ParticleController("Particle Controller", new FlameMain.InfluencerWrapper[] { new FlameMain.InfluencerWrapper("Single Particle Controller", (Class)ParticleControllerInfluencer.Single.class), new FlameMain.InfluencerWrapper("Random Particle Controller", (Class)ParticleControllerInfluencer.Random.class), new FlameMain.InfluencerWrapper("Scale", (Class)ScaleInfluencer.class), new FlameMain.InfluencerWrapper("Spawn", (Class)SpawnInfluencer.class), new FlameMain.InfluencerWrapper("Dynamics", (Class)DynamicsInfluencer.class) });
/*      */ 
/*      */     
/*      */     public String desc;
/*      */ 
/*      */     
/*      */     public FlameMain.InfluencerWrapper[] wrappers;
/*      */ 
/*      */     
/*      */     ControllerType(String desc, FlameMain.InfluencerWrapper[] wrappers) {
/*  185 */       this.desc = desc;
/*  186 */       this.wrappers = wrappers;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public FlameMain() {
/*  208 */     super("Flame");
/*  209 */     MathUtils.random = (Random)new RandomXS128();
/*  210 */     this.particleSystem = ParticleSystem.get();
/*  211 */     this.effect = new ParticleEffect();
/*  212 */     this.particleSystem.add(this.effect);
/*  213 */     this.assetManager = new AssetManager();
/*  214 */     this.assetManager.setErrorListener(this);
/*  215 */     this.assetManager.setLoader(ParticleEffect.class, (AssetLoader)new ParticleEffectLoader((FileHandleResolver)new InternalFileHandleResolver()));
/*  216 */     this.controllersData = new Array();
/*      */     
/*  218 */     this.lwjglCanvas = new LwjglCanvas(this.renderer = new AppRenderer());
/*  219 */     addWindowListener(new WindowAdapter()
/*      */         {
/*      */           public void windowClosed(WindowEvent event) {
/*  222 */             Gdx.app.exit();
/*      */           }
/*      */         });
/*      */     
/*  226 */     initializeComponents();
/*      */     
/*  228 */     setSize(1280, 950);
/*  229 */     setLocationRelativeTo(null);
/*  230 */     setDefaultCloseOperation(2);
/*  231 */     setVisible(true);
/*      */   }
/*      */   
/*      */   public ControllerType getControllerType() {
/*  235 */     ParticleController controller = getEmitter();
/*  236 */     ControllerType type = null;
/*  237 */     if (controller.renderer instanceof com.badlogic.gdx.graphics.g3d.particles.renderers.BillboardRenderer) {
/*  238 */       type = ControllerType.Billboard;
/*  239 */     } else if (controller.renderer instanceof com.badlogic.gdx.graphics.g3d.particles.renderers.PointSpriteRenderer) {
/*  240 */       type = ControllerType.PointSprite;
/*  241 */     } else if (controller.renderer instanceof com.badlogic.gdx.graphics.g3d.particles.renderers.ModelInstanceRenderer) {
/*  242 */       type = ControllerType.ModelInstance;
/*  243 */     } else if (controller.renderer instanceof com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerControllerRenderer) {
/*  244 */       type = ControllerType.ParticleController;
/*  245 */     }  return type;
/*      */   }
/*      */   
/*      */   void reloadRows() {
/*  249 */     EventQueue.invokeLater(new Runnable()
/*      */         {
/*      */           public void run()
/*      */           {
/*  253 */             EventManager.get().clear();
/*      */ 
/*      */             
/*  256 */             FlameMain.this.editorPropertiesPanel.removeAll();
/*  257 */             FlameMain.this.influencerBox.removeAllItems();
/*  258 */             FlameMain.this.controllerPropertiesPanel.removeAll();
/*      */ 
/*      */             
/*  261 */             FlameMain.this.addRow(FlameMain.this.editorPropertiesPanel, new NumericPanel(FlameMain.this, FlameMain.this.fovValue, "Field of View", ""));
/*  262 */             FlameMain.this.addRow(FlameMain.this.editorPropertiesPanel, new NumericPanel(FlameMain.this, FlameMain.this.deltaMultiplier, "Delta multiplier", ""));
/*  263 */             FlameMain.this.addRow(FlameMain.this.editorPropertiesPanel, new GradientPanel(FlameMain.this, FlameMain.this.backgroundColor, "Background color", "", true));
/*  264 */             FlameMain.this.addRow(FlameMain.this.editorPropertiesPanel, new DrawPanel(FlameMain.this, "Draw", ""));
/*  265 */             FlameMain.this.addRow(FlameMain.this.editorPropertiesPanel, new TextureLoaderPanel(FlameMain.this, "Texture", ""));
/*  266 */             FlameMain.this.addRow(FlameMain.this.editorPropertiesPanel, new BillboardBatchPanel(FlameMain.this, FlameMain.this.renderer.billboardBatch), 1.0F, 1.0F);
/*  267 */             FlameMain.this.editorPropertiesPanel.repaint();
/*      */ 
/*      */             
/*  270 */             ParticleController controller = FlameMain.this.getEmitter();
/*  271 */             if (controller != null) {
/*      */               
/*  273 */               DefaultComboBoxModel<Object> model = (DefaultComboBoxModel)FlameMain.this.influencerBox.getModel();
/*  274 */               FlameMain.ControllerType type = FlameMain.this.getControllerType();
/*  275 */               if (type != null)
/*  276 */                 for (FlameMain.InfluencerWrapper value : type.wrappers) {
/*  277 */                   model.addElement(value);
/*      */                 } 
/*  279 */               JPanel panel = null;
/*  280 */               FlameMain.this.addRow(FlameMain.this.controllerPropertiesPanel, FlameMain.this.getPanel(controller.emitter));
/*  281 */               for (int i = 0; i < c; i++) {
/*  282 */                 Influencer influencer = (Influencer)controller.influencers.get(i);
/*  283 */                 panel = FlameMain.this.getPanel(influencer);
/*  284 */                 if (panel != null)
/*  285 */                   FlameMain.this.addRow(FlameMain.this.controllerPropertiesPanel, panel, 1.0F, (i == c - 1) ? 1.0F : 0.0F); 
/*      */               } 
/*  287 */               for (Component component : FlameMain.this.controllerPropertiesPanel.getComponents()) {
/*  288 */                 if (component instanceof EditorPanel)
/*  289 */                   ((EditorPanel)component).update(FlameMain.this); 
/*      */               } 
/*  291 */             }  FlameMain.this.controllerPropertiesPanel.repaint();
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   protected JPanel getPanel(Emitter emitter) {
/*  297 */     if (emitter instanceof RegularEmitter) {
/*  298 */       return new RegularEmitterPanel(this, (RegularEmitter)emitter);
/*      */     }
/*  300 */     return null;
/*      */   }
/*      */   
/*      */   protected JPanel getPanel(Influencer influencer) {
/*  304 */     if (influencer instanceof ColorInfluencer.Single) {
/*  305 */       return new ColorInfluencerPanel(this, (ColorInfluencer.Single)influencer);
/*      */     }
/*  307 */     if (influencer instanceof ColorInfluencer.Random)
/*  308 */       return new InfluencerPanel<ColorInfluencer.Random>(this, (ColorInfluencer.Random)influencer, "Random Color Influencer", "Assign a random color to the particles") {
/*      */         
/*      */         }; 
/*  311 */     if (influencer instanceof ScaleInfluencer) {
/*  312 */       return new ScaleInfluencerPanel(this, (ScaleInfluencer)influencer);
/*      */     }
/*  314 */     if (influencer instanceof SpawnInfluencer) {
/*  315 */       return new SpawnInfluencerPanel(this, (SpawnInfluencer)influencer);
/*      */     }
/*  317 */     if (influencer instanceof DynamicsInfluencer) {
/*  318 */       return new DynamicsInfluencerPanel(this, (DynamicsInfluencer)influencer);
/*      */     }
/*  320 */     if (influencer instanceof ModelInfluencer) {
/*  321 */       boolean single = influencer instanceof ModelInfluencer.Single;
/*  322 */       String name = single ? "Model Single Influencer" : "Model Random Influencer";
/*  323 */       return new ModelInfluencerPanel(this, (ModelInfluencer)influencer, single, name, "Defines what model will be used for the particles");
/*      */     } 
/*  325 */     if (influencer instanceof ParticleControllerInfluencer) {
/*  326 */       boolean single = influencer instanceof ParticleControllerInfluencer.Single;
/*  327 */       String name = single ? "Particle Controller Single Influencer" : "Particle Controller Random Influencer";
/*  328 */       return new ParticleControllerInfluencerPanel(this, (ParticleControllerInfluencer)influencer, single, name, "Defines what controller will be used for the particles");
/*      */     } 
/*  330 */     if (influencer instanceof RegionInfluencer.Single) {
/*  331 */       return new RegionInfluencerPanel(this, "Billboard Single Region Influencer", "Assign the chosen region to the particles", (RegionInfluencer)influencer);
/*      */     }
/*      */     
/*  334 */     if (influencer instanceof RegionInfluencer.Animated) {
/*  335 */       return new RegionInfluencerPanel(this, "Billboard Animated Region Influencer", "Animates the region of the particles", (RegionInfluencer)influencer);
/*      */     }
/*      */     
/*  338 */     if (influencer instanceof RegionInfluencer.Random) {
/*  339 */       return new RegionInfluencerPanel(this, "Billboard Random Region Influencer", "Assigns a randomly picked (among those selected) region to the particles", (RegionInfluencer)influencer);
/*      */     }
/*      */     
/*  342 */     if (influencer instanceof ParticleControllerFinalizerInfluencer) {
/*  343 */       return new InfluencerPanel<ParticleControllerFinalizerInfluencer>(this, (ParticleControllerFinalizerInfluencer)influencer, "ParticleControllerFinalizer Influencer", "This is required when dealing with a controller of controllers, it will update the controller assigned to each particle, it MUST be the last influencer always.", true, false)
/*      */         {
/*      */         
/*      */         };
/*      */     }
/*  348 */     return null;
/*      */   }
/*      */   
/*      */   protected JPanel getPanel(ParticleBatch renderer) {
/*  352 */     if (renderer instanceof PointSpriteParticleBatch) {
/*  353 */       return new EmptyPanel(this, "Point Sprite Batch", "It renders particles as point sprites.");
/*      */     }
/*  355 */     if (renderer instanceof BillboardParticleBatch) {
/*  356 */       return new BillboardBatchPanel(this, (BillboardParticleBatch)renderer);
/*      */     }
/*  358 */     if (renderer instanceof ModelInstanceParticleBatch) {
/*  359 */       return new EmptyPanel(this, "Model Instance Batch", "It renders particles as model instances.");
/*      */     }
/*      */     
/*  362 */     return null;
/*      */   }
/*      */   
/*      */   void addRow(JPanel panel, JPanel row) {
/*  366 */     addRow(panel, row, 1.0F, 0.0F);
/*      */   }
/*      */   
/*      */   void addRow(JPanel panel, JPanel row, float wx, float wy) {
/*  370 */     row.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.black));
/*  371 */     panel.add(row, new GridBagConstraints(0, -1, 1, 1, wx, wy, 11, 2, new Insets(0, 0, 0, 0), 0, 0));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setVisible(String name, boolean visible) {
/*  376 */     for (Component component : this.controllerPropertiesPanel.getComponents()) {
/*  377 */       if (component instanceof EditorPanel && ((EditorPanel)component).getName().equals(name)) component.setVisible(visible); 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void rebuildActiveControllers() {
/*  382 */     Array<ParticleController> effectControllers = this.effect.getControllers();
/*  383 */     effectControllers.clear();
/*  384 */     for (ControllerData controllerData : this.controllersData) {
/*  385 */       if (controllerData.enabled) {
/*  386 */         effectControllers.add(controllerData.controller);
/*      */       }
/*      */     } 
/*      */     
/*  390 */     this.effect.init();
/*  391 */     this.effect.start();
/*      */   }
/*      */   
/*      */   public ParticleController getEmitter() {
/*  395 */     return (this.effectPanel.editIndex >= 0) ? ((ControllerData)this.controllersData.get(this.effectPanel.editIndex)).controller : null;
/*      */   }
/*      */   
/*      */   public void addEmitter(ParticleController emitter) {
/*  399 */     this.controllersData.add(new ControllerData(emitter));
/*  400 */     rebuildActiveControllers();
/*      */   }
/*      */   
/*      */   public void removeEmitter(int row) {
/*  404 */     ((ControllerData)this.controllersData.removeIndex(row)).controller.dispose();
/*  405 */     rebuildActiveControllers();
/*      */   }
/*      */   
/*      */   public void setEnabled(int emitterIndex, boolean enabled) {
/*  409 */     ControllerData data = (ControllerData)this.controllersData.get(emitterIndex);
/*  410 */     data.enabled = enabled;
/*  411 */     rebuildActiveControllers();
/*      */   }
/*      */   
/*      */   public boolean isEnabled(int emitterIndex) {
/*  415 */     return ((ControllerData)this.controllersData.get(emitterIndex)).enabled;
/*      */   }
/*      */   
/*      */   private void initializeComponents() {
/*  419 */     this.splitPane = new JSplitPane();
/*  420 */     this.splitPane.setUI(new BasicSplitPaneUI()
/*      */         {
/*      */           public void paint(Graphics g, JComponent jc) {}
/*      */         });
/*  424 */     this.splitPane.setDividerSize(4);
/*  425 */     getContentPane().add(this.splitPane, "Center");
/*      */     
/*  427 */     JSplitPane rightSplit = new JSplitPane(0);
/*  428 */     rightSplit.setUI(new BasicSplitPaneUI()
/*      */         {
/*      */           public void paint(Graphics g, JComponent jc) {}
/*      */         });
/*  432 */     rightSplit.setDividerSize(4);
/*  433 */     this.splitPane.add(rightSplit, "right");
/*      */ 
/*      */     
/*  436 */     JPanel propertiesPanel = new JPanel(new GridBagLayout());
/*  437 */     rightSplit.add(propertiesPanel, "top");
/*  438 */     propertiesPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(3, 0, 6, 6), 
/*  439 */           BorderFactory.createTitledBorder("Editor Properties")));
/*      */     
/*  441 */     JScrollPane scroll = new JScrollPane();
/*  442 */     propertiesPanel.add(scroll, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 11, 1, new Insets(0, 0, 0, 0), 0, 0));
/*      */     
/*  444 */     scroll.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/*      */     
/*  446 */     this.editorPropertiesPanel = new JPanel(new GridBagLayout());
/*  447 */     scroll.setViewportView(this.editorPropertiesPanel);
/*  448 */     scroll.getVerticalScrollBar().setUnitIncrement(70);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  454 */     JSplitPane rightSplitPane = new JSplitPane(0);
/*  455 */     rightSplitPane.setUI(new BasicSplitPaneUI()
/*      */         {
/*      */           public void paint(Graphics g, JComponent jc) {}
/*      */         });
/*  459 */     rightSplitPane.setDividerSize(4);
/*  460 */     rightSplitPane.setDividerLocation(100);
/*  461 */     rightSplit.add(rightSplitPane, "bottom");
/*      */     
/*  463 */     JPanel jPanel1 = new JPanel(new GridBagLayout());
/*  464 */     rightSplitPane.add(jPanel1, "top");
/*  465 */     jPanel1.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(3, 0, 6, 6), 
/*  466 */           BorderFactory.createTitledBorder("Influencers")));
/*      */     
/*  468 */     JScrollPane jScrollPane1 = new JScrollPane();
/*  469 */     jPanel1.add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 11, 1, new Insets(0, 0, 0, 0), 0, 0));
/*      */     
/*  471 */     jScrollPane1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/*      */     
/*  473 */     JPanel influencersPanel = new JPanel(new GridBagLayout());
/*  474 */     this.influencerBox = new JComboBox(new DefaultComboBoxModel());
/*  475 */     JButton addInfluencerButton = new JButton("Add");
/*  476 */     addInfluencerButton.addActionListener(new ActionListener()
/*      */         {
/*      */           public void actionPerformed(ActionEvent e) {
/*  479 */             FlameMain.InfluencerWrapper wrapper = (FlameMain.InfluencerWrapper)FlameMain.this.influencerBox.getSelectedItem();
/*  480 */             ParticleController controller = FlameMain.this.getEmitter();
/*  481 */             if (controller != null) {
/*  482 */               FlameMain.this.addInfluencer(wrapper.type, controller);
/*      */             }
/*      */           }
/*      */         });
/*  486 */     influencersPanel.add(this.influencerBox, new GridBagConstraints(0, 0, 1, 1, 0.0D, 1.0D, 18, 0, new Insets(0, 0, 0, 0), 0, 0));
/*      */     
/*  488 */     influencersPanel.add(addInfluencerButton, new GridBagConstraints(1, 0, 1, 1, 1.0D, 1.0D, 18, 0, new Insets(0, 0, 0, 0), 0, 0));
/*      */     
/*  490 */     jScrollPane1.setViewportView(influencersPanel);
/*  491 */     jScrollPane1.getVerticalScrollBar().setUnitIncrement(70);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  496 */     jPanel1 = new JPanel(new GridBagLayout());
/*  497 */     rightSplitPane.add(jPanel1, "bottom");
/*  498 */     jPanel1.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(3, 0, 6, 6), 
/*  499 */           BorderFactory.createTitledBorder("Particle Controller Components")));
/*      */     
/*  501 */     jScrollPane1 = new JScrollPane();
/*  502 */     jPanel1.add(jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 11, 1, new Insets(0, 0, 0, 0), 0, 0));
/*      */     
/*  504 */     jScrollPane1.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
/*      */     
/*  506 */     this.controllerPropertiesPanel = new JPanel(new GridBagLayout());
/*  507 */     jScrollPane1.setViewportView(this.controllerPropertiesPanel);
/*  508 */     jScrollPane1.getVerticalScrollBar().setUnitIncrement(70);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  513 */     rightSplit.setDividerLocation(250);
/*      */ 
/*      */ 
/*      */     
/*  517 */     JSplitPane leftSplit = new JSplitPane(0);
/*  518 */     leftSplit.setUI(new BasicSplitPaneUI()
/*      */         {
/*      */           public void paint(Graphics g, JComponent jc) {}
/*      */         });
/*  522 */     leftSplit.setDividerSize(4);
/*  523 */     this.splitPane.add(leftSplit, "left");
/*      */     
/*  525 */     JPanel spacer = new JPanel(new BorderLayout());
/*  526 */     leftSplit.add(spacer, "top");
/*  527 */     spacer.add(this.lwjglCanvas.getCanvas());
/*  528 */     spacer.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
/*      */ 
/*      */     
/*  531 */     JPanel emittersPanel = new JPanel(new BorderLayout());
/*  532 */     leftSplit.add(emittersPanel, "bottom");
/*  533 */     emittersPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(0, 6, 6, 0), 
/*  534 */           BorderFactory.createTitledBorder("Particle Controllers")));
/*      */     
/*  536 */     this.effectPanel = new EffectPanel(this);
/*  537 */     emittersPanel.add(this.effectPanel);
/*      */ 
/*      */     
/*  540 */     leftSplit.setDividerLocation(625);
/*      */     
/*  542 */     this.splitPane.setDividerLocation(500);
/*      */   }
/*      */   
/*      */   protected void addInfluencer(Class<Influencer> type, ParticleController controller) {
/*  546 */     if (controller.findInfluencer(type) != null)
/*      */       return; 
/*      */     try {
/*  549 */       controller.end();
/*      */       
/*  551 */       Influencer newInfluencer = type.newInstance();
/*  552 */       boolean replaced = false;
/*  553 */       if (ColorInfluencer.class.isAssignableFrom(type)) {
/*  554 */         replaced = controller.replaceInfluencer(ColorInfluencer.class, newInfluencer);
/*      */       }
/*  556 */       else if (RegionInfluencer.class.isAssignableFrom(type)) {
/*  557 */         replaced = controller.replaceInfluencer(RegionInfluencer.class, newInfluencer);
/*      */       }
/*  559 */       else if (ModelInfluencer.class.isAssignableFrom(type)) {
/*  560 */         ModelInfluencer newModelInfluencer = (ModelInfluencer)newInfluencer;
/*  561 */         ModelInfluencer currentInfluencer = (ModelInfluencer)controller.findInfluencer(ModelInfluencer.class);
/*  562 */         if (currentInfluencer != null) {
/*  563 */           newModelInfluencer.models.add(currentInfluencer.models.first());
/*      */         }
/*  565 */         replaced = controller.replaceInfluencer(ModelInfluencer.class, newInfluencer);
/*      */       }
/*  567 */       else if (ParticleControllerInfluencer.class.isAssignableFrom(type)) {
/*  568 */         ParticleControllerInfluencer newModelInfluencer = (ParticleControllerInfluencer)newInfluencer;
/*  569 */         ParticleControllerInfluencer currentInfluencer = (ParticleControllerInfluencer)controller.findInfluencer(ParticleControllerInfluencer.class);
/*  570 */         if (currentInfluencer != null) {
/*  571 */           newModelInfluencer.templates.add(currentInfluencer.templates.first());
/*      */         }
/*  573 */         replaced = controller.replaceInfluencer(ParticleControllerInfluencer.class, newInfluencer);
/*      */       } 
/*      */       
/*  576 */       if (!replaced) {
/*  577 */         if (getControllerType() != ControllerType.ParticleController) {
/*  578 */           controller.influencers.add(newInfluencer);
/*      */         } else {
/*  580 */           Influencer finalizer = (Influencer)controller.influencers.pop();
/*  581 */           controller.influencers.add(newInfluencer);
/*  582 */           controller.influencers.add(finalizer);
/*      */         } 
/*      */       }
/*      */       
/*  586 */       controller.init();
/*  587 */       this.effect.start();
/*  588 */       reloadRows();
/*  589 */     } catch (Exception e1) {
/*  590 */       e1.printStackTrace();
/*      */     } 
/*      */   }
/*      */   
/*      */   protected boolean canAddInfluencer(Class<?> influencerType, ParticleController controller) {
/*  595 */     boolean hasSameInfluencer = (controller.findInfluencer(influencerType) != null);
/*  596 */     if (!hasSameInfluencer) {
/*  597 */       if ((ColorInfluencer.Single.class.isAssignableFrom(influencerType) && controller.findInfluencer(ColorInfluencer.Random.class) != null) || (ColorInfluencer.Random.class
/*  598 */         .isAssignableFrom(influencerType) && controller.findInfluencer(ColorInfluencer.Single.class) != null)) {
/*  599 */         return false;
/*      */       }
/*      */       
/*  602 */       if (RegionInfluencer.class.isAssignableFrom(influencerType)) {
/*  603 */         return (controller.findInfluencer(RegionInfluencer.class) == null);
/*      */       }
/*  605 */       if (ModelInfluencer.class.isAssignableFrom(influencerType)) {
/*  606 */         return (controller.findInfluencer(ModelInfluencer.class) == null);
/*      */       }
/*  608 */       if (ParticleControllerInfluencer.class.isAssignableFrom(influencerType)) {
/*  609 */         return (controller.findInfluencer(ParticleControllerInfluencer.class) == null);
/*      */       }
/*      */     } 
/*  612 */     return !hasSameInfluencer;
/*      */   }
/*      */ 
/*      */   
/*      */   class AppRenderer
/*      */     implements ApplicationListener
/*      */   {
/*      */     private float maxActiveTimer;
/*      */     
/*      */     private int maxActive;
/*      */     
/*      */     private int lastMaxActive;
/*      */     
/*      */     boolean isUpdate = true;
/*      */     
/*      */     private CameraInputController cameraInputController;
/*      */     
/*      */     private Stage ui;
/*      */     
/*      */     TextButton playPauseButton;
/*      */     
/*      */     private Label fpsLabel;
/*      */     
/*      */     private Label pointCountLabel;
/*      */     private Label billboardCountLabel;
/*      */     private Label modelInstanceCountLabel;
/*      */     private Label maxLabel;
/*      */     StringBuilder stringBuilder;
/*      */     
/*      */     public void create() {
/*  642 */       if (this.ui != null)
/*  643 */         return;  int w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
/*  644 */       this.modelBatch = new ModelBatch();
/*  645 */       this.environment = new Environment();
/*  646 */       this.environment.add((new DirectionalLight()).set(Color.WHITE, 0.0F, 0.0F, -1.0F));
/*      */       
/*  648 */       this.worldCamera = new PerspectiveCamera(67.0F, w, h);
/*  649 */       this.worldCamera.position.set(10.0F, 10.0F, 10.0F);
/*  650 */       this.worldCamera.lookAt(0.0F, 0.0F, 0.0F);
/*  651 */       this.worldCamera.near = 0.1F;
/*  652 */       this.worldCamera.far = 300.0F;
/*  653 */       this.worldCamera.update();
/*      */       
/*  655 */       this.cameraInputController = new CameraInputController((Camera)this.worldCamera);
/*      */ 
/*      */       
/*  658 */       this.pointSpriteBatch = new PointSpriteParticleBatch();
/*  659 */       this.pointSpriteBatch.setCamera((Camera)this.worldCamera);
/*      */       
/*  661 */       this.billboardBatch = new BillboardParticleBatch();
/*  662 */       this.billboardBatch.setCamera((Camera)this.worldCamera);
/*  663 */       this.modelInstanceParticleBatch = new ModelInstanceParticleBatch();
/*      */       
/*  665 */       FlameMain.this.particleSystem.add((ParticleBatch)this.billboardBatch);
/*  666 */       FlameMain.this.particleSystem.add((ParticleBatch)this.pointSpriteBatch);
/*  667 */       FlameMain.this.particleSystem.add((ParticleBatch)this.modelInstanceParticleBatch);
/*      */       
/*  669 */       FlameMain.this.fovValue = new NumericValue();
/*  670 */       FlameMain.this.fovValue.setValue(67.0F);
/*  671 */       FlameMain.this.fovValue.setActive(true);
/*      */       
/*  673 */       FlameMain.this.deltaMultiplier = new NumericValue();
/*  674 */       FlameMain.this.deltaMultiplier.setValue(1.0F);
/*  675 */       FlameMain.this.deltaMultiplier.setActive(true);
/*      */       
/*  677 */       FlameMain.this.backgroundColor = new GradientColorValue();
/*  678 */       Color color = Color.valueOf("878787");
/*  679 */       FlameMain.this.backgroundColor.setColors(new float[] { color.r, color.g, color.b });
/*      */       
/*  681 */       this.models = new Array();
/*  682 */       ModelBuilder builder = new ModelBuilder();
/*  683 */       Model xyzModel = builder.createXYZCoordinates(10.0F, new Material(), 5L);
/*  684 */       Model planeModel = builder.createLineGrid(10, 10, 1.0F, 1.0F, new Material(new Attribute[] { (Attribute)ColorAttribute.createDiffuse(Color.WHITE) }, ), 1L);
/*  685 */       this.models.add(xyzModel);
/*  686 */       this.models.add(planeModel);
/*  687 */       this.xyzInstance = new ModelInstance(xyzModel);
/*  688 */       this.xzPlaneInstance = new ModelInstance(planeModel);
/*  689 */       this.xyPlaneInstance = new ModelInstance(planeModel);
/*  690 */       this.xyPlaneInstance.transform.rotate(1.0F, 0.0F, 0.0F, 90.0F);
/*      */       
/*  692 */       setDrawXYZ(true);
/*  693 */       setDrawXZPlane(true);
/*      */ 
/*      */ 
/*      */       
/*  697 */       ParticleEffectLoader.ParticleEffectLoadParameter params = new ParticleEffectLoader.ParticleEffectLoadParameter(FlameMain.this.particleSystem.getBatches());
/*  698 */       FlameMain.this.assetManager.load("pre_particle.png", Texture.class);
/*  699 */       FlameMain.this.assetManager.load("monkey.g3db", Model.class);
/*  700 */       FlameMain.this.assetManager.load("uiskin.json", Skin.class);
/*  701 */       FlameMain.this.assetManager.load("defaultTemplate.pfx", ParticleEffect.class, (AssetLoaderParameters)params);
/*      */       
/*  703 */       FlameMain.this.assetManager.finishLoading();
/*  704 */       FlameMain.this.assetManager.setLoader(ParticleEffect.class, (AssetLoader)new ParticleEffectLoader((FileHandleResolver)new AbsoluteFileHandleResolver()));
/*  705 */       ((Material)((Model)FlameMain.this.assetManager.get("monkey.g3db", Model.class)).materials.get(0)).set((Attribute)new BlendingAttribute(1, 771, 1.0F));
/*      */ 
/*      */       
/*  708 */       this.stringBuilder = new StringBuilder();
/*  709 */       Skin skin = (Skin)FlameMain.this.assetManager.get("uiskin.json", Skin.class);
/*  710 */       this.ui = new Stage();
/*  711 */       this.fpsLabel = new Label("", skin);
/*  712 */       this.pointCountLabel = new Label("", skin);
/*  713 */       this.billboardCountLabel = new Label("", skin);
/*  714 */       this.modelInstanceCountLabel = new Label("", skin);
/*      */       
/*  716 */       this.maxLabel = new Label("", skin);
/*  717 */       this.playPauseButton = new TextButton("Pause", skin);
/*  718 */       this.playPauseButton.addListener((EventListener)new ClickListener()
/*      */           {
/*      */             public void clicked(InputEvent event, float x, float y) {
/*  721 */               FlameMain.AppRenderer.this.isUpdate = !FlameMain.AppRenderer.this.isUpdate;
/*  722 */               FlameMain.AppRenderer.this.playPauseButton.setText(FlameMain.AppRenderer.this.isUpdate ? "Pause" : "Play");
/*      */             }
/*      */           });
/*  725 */       Table table = new Table(skin);
/*  726 */       table.setFillParent(true);
/*  727 */       table.pad(5.0F);
/*  728 */       table.add((Actor)this.fpsLabel).expandX().left().row();
/*  729 */       table.add((Actor)this.pointCountLabel).expandX().left().row();
/*  730 */       table.add((Actor)this.billboardCountLabel).expandX().left().row();
/*  731 */       table.add((Actor)this.modelInstanceCountLabel).expandX().left().row();
/*  732 */       table.add((Actor)this.maxLabel).expandX().left().row();
/*  733 */       table.add((Actor)this.playPauseButton).expand().bottom().left().row();
/*  734 */       this.ui.addActor((Actor)table);
/*      */       
/*  736 */       FlameMain.this.setTexture((Texture)FlameMain.this.assetManager.get("pre_particle.png"));
/*  737 */       FlameMain.this.effectPanel.createDefaultEmitter(FlameMain.ControllerType.Billboard, true, true);
/*      */     }
/*      */     public PerspectiveCamera worldCamera; private boolean isDrawXYZ; private boolean isDrawXZPlane; private boolean isDrawXYPlane; private Array<Model> models; private ModelInstance xyzInstance; private ModelInstance xzPlaneInstance; private ModelInstance xyPlaneInstance; private Environment environment; private ModelBatch modelBatch; PointSpriteParticleBatch pointSpriteBatch; BillboardParticleBatch billboardBatch; ModelInstanceParticleBatch modelInstanceParticleBatch;
/*      */     
/*      */     public void resize(int width, int height) {
/*  742 */       Gdx.input.setInputProcessor((InputProcessor)new InputMultiplexer(new InputProcessor[] { (InputProcessor)this.ui, (InputProcessor)this.cameraInputController }));
/*  743 */       Gdx.gl.glViewport(0, 0, width, height);
/*      */       
/*  745 */       this.worldCamera.viewportWidth = width;
/*  746 */       this.worldCamera.viewportHeight = height;
/*  747 */       this.worldCamera.update();
/*  748 */       this.ui.getViewport().setWorldSize(width, height);
/*  749 */       this.ui.getViewport().update(width, height, true);
/*      */     }
/*      */ 
/*      */     
/*      */     public void render() {
/*  754 */       update();
/*  755 */       renderWorld();
/*      */     }
/*      */     
/*      */     private void update() {
/*  759 */       this.worldCamera.fieldOfView = FlameMain.this.fovValue.getValue();
/*  760 */       this.worldCamera.update();
/*  761 */       this.cameraInputController.update();
/*  762 */       if (this.isUpdate) {
/*  763 */         FlameMain.this.particleSystem.update();
/*      */         
/*  765 */         this.stringBuilder.delete(0, this.stringBuilder.length);
/*  766 */         this.stringBuilder.append("Point Sprites : ").append(this.pointSpriteBatch.getBufferedCount());
/*  767 */         this.pointCountLabel.setText((CharSequence)this.stringBuilder);
/*  768 */         this.stringBuilder.delete(0, this.stringBuilder.length);
/*  769 */         this.stringBuilder.append("Billboards : ").append(this.billboardBatch.getBufferedCount());
/*  770 */         this.billboardCountLabel.setText((CharSequence)this.stringBuilder);
/*  771 */         this.stringBuilder.delete(0, this.stringBuilder.length);
/*  772 */         this.stringBuilder.append("Model Instances : ").append(this.modelInstanceParticleBatch.getBufferedCount());
/*  773 */         this.modelInstanceCountLabel.setText((CharSequence)this.stringBuilder);
/*      */       } 
/*  775 */       this.stringBuilder.delete(0, this.stringBuilder.length);
/*  776 */       this.stringBuilder.append("FPS : ").append(Gdx.graphics.getFramesPerSecond());
/*  777 */       this.fpsLabel.setText((CharSequence)this.stringBuilder);
/*  778 */       this.ui.act(Gdx.graphics.getDeltaTime());
/*      */     }
/*      */     
/*      */     private void renderWorld() {
/*  782 */       float[] colors = FlameMain.this.backgroundColor.getColors();
/*  783 */       Gdx.gl.glClear(16640);
/*  784 */       Gdx.gl.glClearColor(colors[0], colors[1], colors[2], 0.0F);
/*  785 */       this.modelBatch.begin((Camera)this.worldCamera);
/*  786 */       if (this.isDrawXYZ) this.modelBatch.render((RenderableProvider)this.xyzInstance); 
/*  787 */       if (this.isDrawXZPlane) this.modelBatch.render((RenderableProvider)this.xzPlaneInstance); 
/*  788 */       if (this.isDrawXYPlane) this.modelBatch.render((RenderableProvider)this.xyPlaneInstance); 
/*  789 */       FlameMain.this.particleSystem.begin();
/*  790 */       FlameMain.this.particleSystem.draw();
/*  791 */       FlameMain.this.particleSystem.end();
/*      */ 
/*      */       
/*  794 */       this.modelBatch.render((RenderableProvider)FlameMain.this.particleSystem, this.environment);
/*  795 */       this.modelBatch.end();
/*  796 */       this.ui.draw();
/*      */     }
/*      */ 
/*      */     
/*      */     public void dispose() {}
/*      */ 
/*      */     
/*      */     public void pause() {}
/*      */ 
/*      */     
/*      */     public void resume() {}
/*      */ 
/*      */     
/*      */     public void setDrawXYZ(boolean isDraw) {
/*  810 */       this.isDrawXYZ = isDraw;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean IsDrawXYZ() {
/*  815 */       return this.isDrawXYZ;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setDrawXZPlane(boolean isDraw) {
/*  820 */       this.isDrawXZPlane = isDraw;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean IsDrawXZPlane() {
/*  825 */       return this.isDrawXZPlane;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setDrawXYPlane(boolean isDraw) {
/*  830 */       this.isDrawXYPlane = isDraw;
/*      */     }
/*      */ 
/*      */     
/*      */     public boolean IsDrawXYPlane() {
/*  835 */       return this.isDrawXYPlane;
/*      */     }
/*      */   }
/*      */   
/*      */   public static void main(String[] args) {
/*  840 */     for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
/*  841 */       if ("Nimbus".equals(info.getName())) {
/*      */         try {
/*  843 */           UIManager.setLookAndFeel(info.getClassName());
/*  844 */         } catch (Throwable throwable) {}
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  849 */     EventQueue.invokeLater(new Runnable() {
/*      */           public void run() {
/*  851 */             new FlameMain();
/*      */           }
/*      */         });
/*      */   }
/*      */   
/*      */   public AppRenderer getRenderer() {
/*  857 */     return this.renderer;
/*      */   }
/*      */ 
/*      */   
/*      */   public File showFileLoadDialog() {
/*  862 */     return showFileDialog("Open", 0);
/*      */   }
/*      */   
/*      */   public File showFileSaveDialog() {
/*  866 */     return showFileDialog("Save", 1);
/*      */   }
/*      */   
/*      */   private File showFileDialog(String title, int mode) {
/*  870 */     FileDialog dialog = new FileDialog(this, title, mode);
/*  871 */     if (this.lastDir != null) dialog.setDirectory(this.lastDir); 
/*  872 */     dialog.setVisible(true);
/*  873 */     String file = dialog.getFile();
/*  874 */     String dir = dialog.getDirectory();
/*  875 */     if (dir == null || file == null || file.trim().length() == 0)
/*  876 */       return null; 
/*  877 */     this.lastDir = dir;
/*  878 */     return new File(dir, file);
/*      */   }
/*      */ 
/*      */   
/*      */   public void error(AssetDescriptor asset, Throwable throwable) {
/*  883 */     throwable.printStackTrace();
/*      */   }
/*      */   
/*      */   public PointSpriteParticleBatch getPointSpriteBatch() {
/*  887 */     return this.renderer.pointSpriteBatch;
/*      */   }
/*      */   
/*      */   public BillboardParticleBatch getBillboardBatch() {
/*  891 */     return this.renderer.billboardBatch;
/*      */   }
/*      */   
/*      */   public ModelInstanceParticleBatch getModelInstanceParticleBatch() {
/*  895 */     return this.renderer.modelInstanceParticleBatch;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAtlas(TextureAtlas atlas) {
/*  900 */     setTexture((Texture)atlas.getTextures().first());
/*      */   }
/*      */   
/*      */   public void setTexture(Texture texture) {
/*  904 */     this.renderer.billboardBatch.setTexture(texture);
/*  905 */     this.renderer.pointSpriteBatch.setTexture(texture);
/*      */   }
/*      */   
/*      */   public Texture getTexture() {
/*  909 */     return this.renderer.billboardBatch.getTexture();
/*      */   }
/*      */   
/*      */   public TextureAtlas getAtlas(Texture texture) {
/*  913 */     Array<TextureAtlas> atlases = this.assetManager.getAll(TextureAtlas.class, new Array());
/*  914 */     for (TextureAtlas atlas : atlases) {
/*  915 */       if (atlas.getTextures().contains(texture))
/*  916 */         return atlas; 
/*      */     } 
/*  918 */     return null;
/*      */   }
/*      */   
/*      */   public TextureAtlas getAtlas() {
/*  922 */     return getAtlas(this.renderer.billboardBatch.getTexture());
/*      */   }
/*      */   
/*      */   public boolean isUsingDefaultTexture() {
/*  926 */     return (this.renderer.billboardBatch.getTexture() == this.assetManager.get("pre_particle.png", Texture.class));
/*      */   }
/*      */   
/*      */   public Array<ParticleEffect> getParticleEffects(Array<ParticleController> controllers, Array<ParticleEffect> out) {
/*  930 */     out.clear();
/*  931 */     this.assetManager.getAll(ParticleEffect.class, out);
/*  932 */     for (int i = 0; i < out.size; ) {
/*  933 */       ParticleEffect effect = (ParticleEffect)out.get(i);
/*  934 */       Array<ParticleController> effectControllers = effect.getControllers();
/*  935 */       boolean remove = true;
/*  936 */       for (ParticleController controller : controllers) {
/*  937 */         if (effectControllers.contains(controller, true)) {
/*  938 */           remove = false;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*  943 */       if (remove) {
/*  944 */         out.removeIndex(i);
/*      */         
/*      */         continue;
/*      */       } 
/*  948 */       i++;
/*      */     } 
/*      */     
/*  951 */     return out;
/*      */   }
/*      */   
/*      */   public void saveEffect(File file) {
/*  955 */     Writer fileWriter = null;
/*      */     try {
/*  957 */       ParticleEffectLoader loader = (ParticleEffectLoader)this.assetManager.getLoader(ParticleEffect.class);
/*  958 */       loader.save(this.effect, new ParticleEffectLoader.ParticleEffectSaveParameter(new FileHandle(file.getAbsolutePath()), this.assetManager, this.particleSystem.getBatches()));
/*  959 */     } catch (Exception ex) {
/*  960 */       System.out.println("Error saving effect: " + file.getAbsolutePath());
/*  961 */       ex.printStackTrace();
/*  962 */       JOptionPane.showMessageDialog(this, "Error saving effect.");
/*      */     } finally {
/*  964 */       StreamUtils.closeQuietly(fileWriter);
/*      */     } 
/*      */   }
/*      */   
/*      */   public ParticleEffect openEffect(File file, boolean replaceCurrentWorkspace) {
/*      */     try {
/*  970 */       ParticleEffect loadedEffect = load(file.getAbsolutePath(), ParticleEffect.class, (AssetLoader)null, (AssetLoaderParameters<ParticleEffect>)new ParticleEffectLoader.ParticleEffectLoadParameter(this.particleSystem
/*  971 */             .getBatches()));
/*  972 */       loadedEffect = loadedEffect.copy();
/*  973 */       loadedEffect.init();
/*  974 */       if (replaceCurrentWorkspace) {
/*  975 */         this.effect = loadedEffect;
/*  976 */         this.controllersData.clear();
/*  977 */         this.particleSystem.removeAll();
/*  978 */         this.particleSystem.add(this.effect);
/*  979 */         for (ParticleController controller : this.effect.getControllers())
/*  980 */           this.controllersData.add(new ControllerData(controller)); 
/*  981 */         rebuildActiveControllers();
/*      */       } 
/*  983 */       reloadRows();
/*  984 */       return loadedEffect;
/*  985 */     } catch (Exception ex) {
/*  986 */       System.out.println("Error loading effect: " + file.getAbsolutePath());
/*  987 */       ex.printStackTrace();
/*  988 */       JOptionPane.showMessageDialog(this, "Error opening effect.");
/*      */       
/*  990 */       return null;
/*      */     } 
/*      */   }
/*      */   public <T> T load(String resource, Class<T> type, AssetLoader loader, AssetLoaderParameters<T> params) {
/*  994 */     String resolvedPath = (new String(resource)).replaceAll("\\\\", "/");
/*  995 */     boolean exist = this.assetManager.isLoaded(resolvedPath, type);
/*  996 */     T oldAsset = null;
/*  997 */     if (exist) {
/*  998 */       oldAsset = (T)this.assetManager.get(resolvedPath, type);
/*  999 */       for (int i = this.assetManager.getReferenceCount(resolvedPath); i > 0; i--) {
/* 1000 */         this.assetManager.unload(resolvedPath);
/*      */       }
/*      */     } 
/* 1003 */     AssetLoader<T, AssetLoaderParameters<T>> currentLoader = this.assetManager.getLoader(type);
/* 1004 */     if (loader != null) {
/* 1005 */       this.assetManager.setLoader(type, loader);
/*      */     }
/* 1007 */     this.assetManager.load(resource, type, params);
/* 1008 */     this.assetManager.finishLoading();
/* 1009 */     T res = (T)this.assetManager.get(resolvedPath);
/* 1010 */     if (currentLoader != null) {
/* 1011 */       this.assetManager.setLoader(type, currentLoader);
/*      */     }
/* 1013 */     if (exist) {
/* 1014 */       EventManager.get().fire(0, new Object[] { oldAsset, res });
/*      */     }
/* 1016 */     return res;
/*      */   }
/*      */   
/*      */   public void restart() {
/* 1020 */     this.effect.init();
/* 1021 */     this.effect.start();
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\FlameMain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */