/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ColorInfluencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsInfluencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ModelInfluencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ParticleControllerFinalizerInfluencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ParticleControllerInfluencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.RegionInfluencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ScaleInfluencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.SpawnInfluencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.BillboardRenderer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ModelInstanceRenderer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerControllerRenderer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.PointSpriteRenderer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.EllipseSpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.PointSpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.PrimitiveSpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.SpawnShapeValue;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.event.TableModelEvent;
/*     */ import javax.swing.event.TableModelListener;
/*     */ import javax.swing.table.DefaultTableModel;
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
/*     */ class EffectPanel
/*     */   extends JPanel
/*     */ {
/*     */   FlameMain editor;
/*     */   JTable emitterTable;
/*     */   DefaultTableModel emitterTableModel;
/*  68 */   int editIndex = -1;
/*     */   
/*     */   String lastDir;
/*     */   JComboBox controllerTypeCombo;
/*     */   
/*     */   public EffectPanel(FlameMain editor) {
/*  74 */     this.editor = editor;
/*  75 */     initializeComponents();
/*     */   }
/*     */   
/*     */   public <T extends ParticleController> T createDefaultEmitter(FlameMain.ControllerType type, boolean select, boolean add) {
/*     */     ParticleController particleController;
/*  80 */     T controller = null;
/*  81 */     if (type == FlameMain.ControllerType.Billboard) {
/*  82 */       particleController = createDefaultBillboardController();
/*     */     }
/*  84 */     else if (type == FlameMain.ControllerType.ModelInstance) {
/*  85 */       particleController = createDefaultModelInstanceController();
/*     */     }
/*  87 */     else if (type == FlameMain.ControllerType.PointSprite) {
/*  88 */       particleController = createDefaultPointController();
/*     */     }
/*  90 */     else if (type == FlameMain.ControllerType.ParticleController) {
/*  91 */       particleController = createDefaultParticleController();
/*     */     } 
/*     */     
/*  94 */     if (add) {
/*  95 */       particleController.init();
/*  96 */       addEmitter(particleController, select);
/*     */     } 
/*  98 */     return (T)particleController;
/*     */   }
/*     */ 
/*     */   
/*     */   private ParticleController createDefaultModelInstanceController() {
/* 103 */     RegularEmitter emitter = new RegularEmitter();
/* 104 */     emitter.getDuration().setLow(3000.0F);
/* 105 */     emitter.getEmission().setHigh(80.0F);
/* 106 */     emitter.getLife().setHigh(500.0F, 1000.0F);
/* 107 */     emitter.getLife().setTimeline(new float[] { 0.0F, 0.66F, 1.0F });
/* 108 */     emitter.getLife().setScaling(new float[] { 1.0F, 1.0F, 0.3F });
/* 109 */     emitter.setMaxParticleCount(100);
/*     */ 
/*     */     
/* 112 */     ColorInfluencer.Random colorInfluencer = new ColorInfluencer.Random();
/*     */ 
/*     */     
/* 115 */     EllipseSpawnShapeValue spawnShapeValue = new EllipseSpawnShapeValue();
/* 116 */     spawnShapeValue.setDimensions(1.0F, 1.0F, 1.0F);
/* 117 */     SpawnInfluencer spawnSource = new SpawnInfluencer((SpawnShapeValue)spawnShapeValue);
/*     */ 
/*     */     
/* 120 */     DynamicsInfluencer velocityInfluencer = new DynamicsInfluencer();
/*     */ 
/*     */     
/* 123 */     DynamicsModifier.CentripetalAcceleration velocityValue = new DynamicsModifier.CentripetalAcceleration();
/* 124 */     velocityValue.strengthValue.setHigh(5.0F, 11.0F);
/* 125 */     velocityValue.strengthValue.setActive(true);
/*     */     
/* 127 */     velocityInfluencer.velocities.add(velocityValue);
/*     */ 
/*     */ 
/*     */     
/* 131 */     return new ParticleController("ModelInstance Controller", (Emitter)emitter, (ParticleControllerRenderer)new ModelInstanceRenderer(this.editor.getModelInstanceParticleBatch()), new Influencer[] { (Influencer)new ModelInfluencer.Single(new Model[] { (Model)this.editor.assetManager
/* 132 */               .get("monkey.g3db") }), (Influencer)spawnSource, (Influencer)colorInfluencer, (Influencer)velocityInfluencer });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParticleController createDefaultBillboardController() {
/* 141 */     RegularEmitter emitter = new RegularEmitter();
/* 142 */     emitter.getDuration().setLow(3000.0F);
/* 143 */     emitter.getEmission().setHigh(250.0F);
/* 144 */     emitter.getLife().setHigh(500.0F, 1000.0F);
/* 145 */     emitter.getLife().setTimeline(new float[] { 0.0F, 0.66F, 1.0F });
/* 146 */     emitter.getLife().setScaling(new float[] { 1.0F, 1.0F, 0.3F });
/* 147 */     emitter.setMaxParticleCount(200);
/*     */ 
/*     */     
/* 150 */     PointSpawnShapeValue pointSpawnShapeValue = new PointSpawnShapeValue();
/* 151 */     SpawnInfluencer spawnSource = new SpawnInfluencer((SpawnShapeValue)pointSpawnShapeValue);
/*     */ 
/*     */     
/* 154 */     ColorInfluencer.Single colorInfluencer = new ColorInfluencer.Single();
/* 155 */     colorInfluencer.colorValue.setColors(new float[] { 1.0F, 0.12156863F, 0.047058824F, 0.0F, 0.0F, 0.0F });
/* 156 */     colorInfluencer.colorValue.setTimeline(new float[] { 0.0F, 1.0F });
/* 157 */     colorInfluencer.alphaValue.setHigh(1.0F);
/* 158 */     colorInfluencer.alphaValue.setTimeline(new float[] { 0.0F, 0.5F, 0.8F, 1.0F });
/* 159 */     colorInfluencer.alphaValue.setScaling(new float[] { 0.0F, 0.15F, 0.5F, 0.0F });
/*     */ 
/*     */     
/* 162 */     DynamicsInfluencer velocityInfluencer = new DynamicsInfluencer();
/*     */ 
/*     */     
/* 165 */     DynamicsModifier.PolarAcceleration velocityValue = new DynamicsModifier.PolarAcceleration();
/* 166 */     velocityValue.phiValue.setHigh(-35.0F, 35.0F);
/* 167 */     velocityValue.phiValue.setActive(true);
/* 168 */     velocityValue.phiValue.setTimeline(new float[] { 0.0F, 0.5F, 1.0F });
/* 169 */     velocityValue.phiValue.setScaling(new float[] { 1.0F, 0.0F, 0.0F });
/* 170 */     velocityValue.thetaValue.setHigh(0.0F, 360.0F);
/* 171 */     velocityValue.strengthValue.setHigh(5.0F, 10.0F);
/* 172 */     velocityInfluencer.velocities.add(velocityValue);
/*     */     
/* 174 */     return new ParticleController("Billboard Controller", (Emitter)emitter, (ParticleControllerRenderer)new BillboardRenderer(this.editor.getBillboardBatch()), new Influencer[] { (Influencer)new RegionInfluencer.Single(this.editor
/* 175 */             .getTexture()), (Influencer)spawnSource, (Influencer)colorInfluencer, (Influencer)velocityInfluencer });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParticleController createDefaultPointController() {
/* 184 */     RegularEmitter emitter = new RegularEmitter();
/* 185 */     emitter.getDuration().setLow(3000.0F);
/* 186 */     emitter.getEmission().setHigh(250.0F);
/* 187 */     emitter.getLife().setHigh(500.0F, 1000.0F);
/* 188 */     emitter.getLife().setTimeline(new float[] { 0.0F, 0.66F, 1.0F });
/* 189 */     emitter.getLife().setScaling(new float[] { 1.0F, 1.0F, 0.3F });
/* 190 */     emitter.setMaxParticleCount(200);
/*     */ 
/*     */     
/* 193 */     ScaleInfluencer scaleInfluencer = new ScaleInfluencer();
/* 194 */     scaleInfluencer.value.setHigh(1.0F);
/*     */ 
/*     */     
/* 197 */     ColorInfluencer.Single colorInfluencer = new ColorInfluencer.Single();
/* 198 */     colorInfluencer.colorValue.setColors(new float[] { 0.12156863F, 0.047058824F, 1.0F, 0.0F, 0.0F, 0.0F });
/* 199 */     colorInfluencer.colorValue.setTimeline(new float[] { 0.0F, 1.0F });
/* 200 */     colorInfluencer.alphaValue.setHigh(1.0F);
/* 201 */     colorInfluencer.alphaValue.setTimeline(new float[] { 0.0F, 0.5F, 0.8F, 1.0F });
/* 202 */     colorInfluencer.alphaValue.setScaling(new float[] { 0.0F, 0.15F, 0.5F, 0.0F });
/*     */ 
/*     */     
/* 205 */     PointSpawnShapeValue pointSpawnShapeValue = new PointSpawnShapeValue();
/* 206 */     SpawnInfluencer spawnSource = new SpawnInfluencer((SpawnShapeValue)pointSpawnShapeValue);
/*     */ 
/*     */     
/* 209 */     DynamicsInfluencer velocityInfluencer = new DynamicsInfluencer();
/*     */ 
/*     */     
/* 212 */     DynamicsModifier.PolarAcceleration velocityValue = new DynamicsModifier.PolarAcceleration();
/* 213 */     velocityValue.phiValue.setHigh(-35.0F, 35.0F);
/* 214 */     velocityValue.phiValue.setActive(true);
/* 215 */     velocityValue.phiValue.setTimeline(new float[] { 0.0F, 0.5F, 1.0F });
/* 216 */     velocityValue.phiValue.setScaling(new float[] { 1.0F, 0.0F, 0.0F });
/* 217 */     velocityValue.thetaValue.setHigh(0.0F, 360.0F);
/* 218 */     velocityValue.strengthValue.setHigh(5.0F, 10.0F);
/*     */     
/* 220 */     return new ParticleController("PointSprite Controller", (Emitter)emitter, (ParticleControllerRenderer)new PointSpriteRenderer(this.editor.getPointSpriteBatch()), new Influencer[] { (Influencer)new RegionInfluencer.Single((Texture)this.editor.assetManager
/* 221 */             .get("pre_particle.png")), (Influencer)spawnSource, (Influencer)scaleInfluencer, (Influencer)colorInfluencer, (Influencer)velocityInfluencer });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private ParticleController createDefaultParticleController() {
/* 231 */     RegularEmitter emitter = new RegularEmitter();
/* 232 */     emitter.getDuration().setLow(3000.0F);
/* 233 */     emitter.getEmission().setHigh(90.0F);
/* 234 */     emitter.getLife().setHigh(3000.0F);
/* 235 */     emitter.setMaxParticleCount(100);
/*     */ 
/*     */     
/* 238 */     EllipseSpawnShapeValue pointSpawnShapeValue = new EllipseSpawnShapeValue();
/* 239 */     pointSpawnShapeValue.setDimensions(1.0F, 1.0F, 1.0F);
/* 240 */     pointSpawnShapeValue.setSide(PrimitiveSpawnShapeValue.SpawnSide.top);
/* 241 */     SpawnInfluencer spawnSource = new SpawnInfluencer((SpawnShapeValue)pointSpawnShapeValue);
/*     */ 
/*     */     
/* 244 */     ScaleInfluencer scaleInfluencer = new ScaleInfluencer();
/* 245 */     scaleInfluencer.value.setHigh(1.0F);
/* 246 */     scaleInfluencer.value.setLow(0.0F);
/* 247 */     scaleInfluencer.value.setTimeline(new float[] { 0.0F, 1.0F });
/* 248 */     scaleInfluencer.value.setScaling(new float[] { 1.0F, 0.0F });
/*     */ 
/*     */     
/* 251 */     DynamicsInfluencer velocityInfluencer = new DynamicsInfluencer();
/*     */ 
/*     */     
/* 254 */     DynamicsModifier.CentripetalAcceleration velocityValue = new DynamicsModifier.CentripetalAcceleration();
/* 255 */     velocityValue.strengthValue.setHigh(5.0F, 10.0F);
/* 256 */     velocityValue.strengthValue.setActive(true);
/* 257 */     velocityInfluencer.velocities.add(velocityValue);
/*     */     
/* 259 */     return new ParticleController("ParticleController Controller", (Emitter)emitter, (ParticleControllerRenderer)new ParticleControllerControllerRenderer(), new Influencer[] { (Influencer)new ParticleControllerInfluencer.Single(new ParticleController[] { (ParticleController)((ParticleEffect)this.editor.assetManager
/* 260 */               .get("defaultTemplate.pfx", ParticleEffect.class)).getControllers().get(0) }), (Influencer)spawnSource, (Influencer)scaleInfluencer, (Influencer)velocityInfluencer, (Influencer)new ParticleControllerFinalizerInfluencer() });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParticleController createDefaultTemplateController() {
/* 270 */     RegularEmitter emitter = new RegularEmitter();
/* 271 */     emitter.getDuration().setLow(3000.0F);
/* 272 */     emitter.getEmission().setHigh(90.0F);
/* 273 */     emitter.getLife().setHigh(1000.0F);
/* 274 */     emitter.getLife().setTimeline(new float[] { 0.0F, 0.66F, 1.0F });
/* 275 */     emitter.getLife().setScaling(new float[] { 1.0F, 1.0F, 0.3F });
/* 276 */     emitter.setMaxParticleCount(100);
/*     */ 
/*     */     
/* 279 */     PointSpawnShapeValue pointSpawnShapeValue = new PointSpawnShapeValue();
/* 280 */     pointSpawnShapeValue.xOffsetValue.setLow(0.0F, 1.0F);
/* 281 */     pointSpawnShapeValue.xOffsetValue.setActive(true);
/* 282 */     pointSpawnShapeValue.yOffsetValue.setLow(0.0F, 1.0F);
/* 283 */     pointSpawnShapeValue.yOffsetValue.setActive(true);
/* 284 */     pointSpawnShapeValue.zOffsetValue.setLow(0.0F, 1.0F);
/* 285 */     pointSpawnShapeValue.zOffsetValue.setActive(true);
/* 286 */     SpawnInfluencer spawnSource = new SpawnInfluencer((SpawnShapeValue)pointSpawnShapeValue);
/*     */     
/* 288 */     ScaleInfluencer scaleInfluencer = new ScaleInfluencer();
/* 289 */     scaleInfluencer.value.setHigh(1.0F);
/*     */ 
/*     */     
/* 292 */     ColorInfluencer.Single colorInfluencer = new ColorInfluencer.Single();
/* 293 */     colorInfluencer.colorValue.setColors(new float[] { 1.0F, 0.12156863F, 0.047058824F, 0.0F, 0.0F, 0.0F });
/* 294 */     colorInfluencer.colorValue.setTimeline(new float[] { 0.0F, 1.0F });
/* 295 */     colorInfluencer.alphaValue.setHigh(1.0F);
/* 296 */     colorInfluencer.alphaValue.setTimeline(new float[] { 0.0F, 0.5F, 0.8F, 1.0F });
/* 297 */     colorInfluencer.alphaValue.setScaling(new float[] { 0.0F, 0.15F, 0.5F, 0.0F });
/*     */     
/* 299 */     return new ParticleController("Billboard Controller", (Emitter)emitter, (ParticleControllerRenderer)new BillboardRenderer(this.editor.getBillboardBatch()), new Influencer[] { (Influencer)new RegionInfluencer.Single(this.editor
/* 300 */             .getTexture()), (Influencer)spawnSource, (Influencer)scaleInfluencer, (Influencer)colorInfluencer });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void addEmitter(ParticleController emitter, boolean select) {
/* 308 */     this.editor.addEmitter(emitter);
/* 309 */     this.emitterTableModel.addRow(new Object[] { emitter.name, Boolean.valueOf(true) });
/*     */     
/* 311 */     int row = this.emitterTableModel.getRowCount() - 1;
/* 312 */     emitterChecked(row, true);
/*     */     
/* 314 */     if (select) {
/* 315 */       this.emitterTable.getSelectionModel().setSelectionInterval(row, row);
/*     */     }
/*     */   }
/*     */   
/*     */   void emitterSelected() {
/* 320 */     int row = this.emitterTable.getSelectedRow();
/* 321 */     if (row == this.editIndex) {
/*     */       return;
/*     */     }
/* 324 */     this.editIndex = row;
/* 325 */     this.editor.reloadRows();
/*     */   }
/*     */   
/*     */   void emitterChecked(int index, boolean checked) {
/* 329 */     this.editor.setEnabled(index, checked);
/*     */   }
/*     */   
/*     */   void openEffect() {
/* 333 */     File file = this.editor.showFileLoadDialog();
/* 334 */     if (file != null && 
/* 335 */       this.editor.openEffect(file, true) != null) {
/* 336 */       this.emitterTableModel.getDataVector().removeAllElements();
/* 337 */       for (FlameMain.ControllerData data : this.editor.controllersData) {
/* 338 */         this.emitterTableModel.addRow(new Object[] { data.controller.name, Boolean.valueOf(true) });
/*     */       } 
/* 340 */       this.editIndex = 0;
/* 341 */       this.emitterTable.getSelectionModel().setSelectionInterval(this.editIndex, this.editIndex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void importEffect() {
/* 347 */     File file = this.editor.showFileLoadDialog();
/* 348 */     if (file != null) {
/*     */       ParticleEffect effect;
/* 350 */       if ((effect = this.editor.openEffect(file, false)) != null) {
/* 351 */         for (ParticleController controller : effect.getControllers())
/* 352 */           addEmitter(controller, false); 
/* 353 */         this.editIndex = 0;
/* 354 */         this.emitterTable.getSelectionModel().setSelectionInterval(this.editIndex, this.editIndex);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   void saveEffect() {
/* 360 */     File file = this.editor.showFileSaveDialog();
/* 361 */     if (file != null) {
/* 362 */       int index = 0;
/* 363 */       for (FlameMain.ControllerData data : this.editor.controllersData)
/* 364 */         data.controller.name = (String)this.emitterTableModel.getValueAt(index++, 0); 
/* 365 */       this.editor.saveEffect(file);
/*     */     } 
/*     */   }
/*     */   
/*     */   void deleteEmitter() {
/* 370 */     int row = this.emitterTable.getSelectedRow();
/* 371 */     if (row == -1)
/*     */       return; 
/* 373 */     int newIndex = Math.min(this.editIndex, this.emitterTableModel.getRowCount() - 2);
/*     */     
/* 375 */     this.editor.removeEmitter(row);
/* 376 */     this.emitterTableModel.removeRow(row);
/*     */ 
/*     */     
/* 379 */     this.emitterTable.getSelectionModel().setSelectionInterval(newIndex, newIndex);
/*     */   }
/*     */   
/*     */   protected void cloneEmitter() {
/* 383 */     int row = this.emitterTable.getSelectedRow();
/* 384 */     if (row == -1)
/* 385 */       return;  ParticleController controller = ((FlameMain.ControllerData)this.editor.controllersData.get(row)).controller.copy();
/* 386 */     controller.init();
/* 387 */     controller.name += " Clone";
/* 388 */     addEmitter(controller, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   void move(int direction) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void initializeComponents() {
/* 408 */     setLayout(new GridBagLayout());
/*     */     
/* 410 */     JScrollPane scroll = new JScrollPane();
/* 411 */     add(scroll, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */     
/* 414 */     this.emitterTable = new JTable() {
/*     */         public Class getColumnClass(int column) {
/* 416 */           return (column == 1) ? Boolean.class : super.getColumnClass(column);
/*     */         }
/*     */         
/*     */         public Dimension getPreferredScrollableViewportSize() {
/* 420 */           Dimension dim = super.getPreferredScrollableViewportSize();
/* 421 */           dim.height = (getPreferredSize()).height;
/* 422 */           return dim;
/*     */         }
/*     */       };
/* 425 */     this.emitterTable.getTableHeader().setReorderingAllowed(false);
/* 426 */     this.emitterTable.setSelectionMode(0);
/* 427 */     scroll.setViewportView(this.emitterTable);
/* 428 */     this.emitterTableModel = new DefaultTableModel((Object[][])new String[0][0], (Object[])new String[] { "Emitter", "" });
/* 429 */     this.emitterTable.setModel(this.emitterTableModel);
/* 430 */     this.emitterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
/*     */           public void valueChanged(ListSelectionEvent event) {
/* 432 */             if (event.getValueIsAdjusting())
/* 433 */               return;  EffectPanel.this.emitterSelected();
/*     */           }
/*     */         });
/* 436 */     this.emitterTableModel.addTableModelListener(new TableModelListener() {
/*     */           public void tableChanged(TableModelEvent event) {
/* 438 */             if (event.getColumn() != 1)
/* 439 */               return;  EffectPanel.this.emitterChecked(event.getFirstRow(), ((Boolean)EffectPanel.this.emitterTable.getValueAt(event.getFirstRow(), 1)).booleanValue());
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 445 */     JPanel sideButtons = new JPanel(new GridBagLayout());
/* 446 */     add(sideButtons, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 18, 1, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */     
/* 449 */     this.controllerTypeCombo = new JComboBox();
/* 450 */     this.controllerTypeCombo.setModel(new DefaultComboBoxModel<FlameMain.ControllerType>(FlameMain.ControllerType.values()));
/* 451 */     sideButtons.add(this.controllerTypeCombo, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 455 */     JButton newButton = new JButton("New");
/* 456 */     sideButtons.add(newButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 458 */     newButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 460 */             FlameMain.ControllerType item = (FlameMain.ControllerType)EffectPanel.this.controllerTypeCombo.getSelectedItem();
/* 461 */             EffectPanel.this.createDefaultEmitter(item, true, true);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 466 */     JButton deleteButton = new JButton("Delete");
/* 467 */     sideButtons.add(deleteButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 469 */     deleteButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 471 */             EffectPanel.this.deleteEmitter();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 476 */     JButton cloneButton = new JButton("Clone");
/* 477 */     sideButtons.add(cloneButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 479 */     cloneButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 481 */             EffectPanel.this.cloneEmitter();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 486 */     sideButtons.add(new JSeparator(0), new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 490 */     JButton saveButton = new JButton("Save");
/* 491 */     sideButtons.add(saveButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 493 */     saveButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 495 */             EffectPanel.this.saveEffect();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 500 */     JButton openButton = new JButton("Open");
/* 501 */     sideButtons.add(openButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 503 */     openButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 505 */             EffectPanel.this.openEffect();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 510 */     JButton importButton = new JButton("Import");
/* 511 */     sideButtons.add(importButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 513 */     importButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 515 */             EffectPanel.this.importEffect();
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\EffectPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */