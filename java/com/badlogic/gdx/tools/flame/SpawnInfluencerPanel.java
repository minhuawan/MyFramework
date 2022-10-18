/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Mesh;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.SpawnInfluencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.CylinderSpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.EllipseSpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.LineSpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.ParticleValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.PointSpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.PrimitiveSpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.RectangleSpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.SpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.UnweightedMeshSpawnShapeValue;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.WeightMeshSpawnShapeValue;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SpawnInfluencerPanel
/*     */   extends InfluencerPanel<SpawnInfluencer>
/*     */   implements TemplatePickerPanel.Listener<Model>
/*     */ {
/*     */   private static final String SPAWN_SHAPE_POINT = "Point";
/*     */   private static final String SPAWN_SHAPE_LINE = "Line";
/*     */   private static final String SPAWN_SHAPE_RECTANGLE = "Rectangle";
/*     */   private static final String SPAWN_SHAPE_CYLINDER = "Cylinder";
/*     */   private static final String SPAWN_SHAPE_ELLIPSE = "Ellipse";
/*     */   private static final String SPAWN_SHAPE_MESH = "Unweighted Mesh";
/*     */   private static final String SPAWN_SHAPE_WEIGHT_MESH = "Weighted Mesh";
/*     */   JComboBox shapeCombo;
/*     */   JCheckBox edgesCheckbox;
/*     */   JLabel edgesLabel;
/*     */   JComboBox sideCombo;
/*     */   JLabel sideLabel;
/*     */   TemplatePickerPanel<Model> meshPanel;
/*     */   ScaledNumericPanel widthPanel;
/*     */   ScaledNumericPanel heightPanel;
/*     */   ScaledNumericPanel depthPanel;
/*     */   RangedNumericPanel xPanel;
/*  54 */   private static String[] spawnShapes = new String[] { "Point", "Line", "Rectangle", "Ellipse", "Cylinder", "Unweighted Mesh", "Weighted Mesh" };
/*     */   
/*     */   RangedNumericPanel yPanel;
/*     */   
/*     */   RangedNumericPanel zPanel;
/*     */   
/*     */   PointSpawnShapeValue pointSpawnShapeValue;
/*     */   
/*     */   LineSpawnShapeValue lineSpawnShapeValue;
/*     */   
/*     */   RectangleSpawnShapeValue rectangleSpawnShapeValue;
/*     */   
/*     */   EllipseSpawnShapeValue ellipseSpawnShapeValue;
/*     */   
/*     */   CylinderSpawnShapeValue cylinderSpawnShapeValue;
/*     */   
/*     */   UnweightedMeshSpawnShapeValue meshSpawnShapeValue;
/*     */   WeightMeshSpawnShapeValue weightMeshSpawnShapeValue;
/*     */   
/*     */   public SpawnInfluencerPanel(FlameMain editor, SpawnInfluencer influencer) {
/*  74 */     super(editor, influencer, "Spawn Influencer", "Define where the particles are spawned.", true, false);
/*  75 */     setValue(influencer);
/*  76 */     setCurrentSpawnData(influencer.spawnShapeValue);
/*  77 */     this.shapeCombo.setSelectedItem(spawnShapeToString(influencer.spawnShapeValue));
/*     */   }
/*     */   private void setCurrentSpawnData(SpawnShapeValue spawnShapeValue) {
/*     */     WeightMeshSpawnShapeValue weightMeshSpawnShapeValue;
/*  81 */     SpawnShapeValue local = null;
/*  82 */     if (spawnShapeValue instanceof PointSpawnShapeValue) {
/*  83 */       PointSpawnShapeValue pointSpawnShapeValue = this.pointSpawnShapeValue;
/*  84 */     } else if (spawnShapeValue instanceof LineSpawnShapeValue) {
/*  85 */       LineSpawnShapeValue lineSpawnShapeValue = this.lineSpawnShapeValue;
/*  86 */     } else if (spawnShapeValue instanceof RectangleSpawnShapeValue) {
/*  87 */       RectangleSpawnShapeValue rectangleSpawnShapeValue = this.rectangleSpawnShapeValue;
/*  88 */     } else if (spawnShapeValue instanceof EllipseSpawnShapeValue) {
/*  89 */       EllipseSpawnShapeValue ellipseSpawnShapeValue = this.ellipseSpawnShapeValue;
/*  90 */     } else if (spawnShapeValue instanceof CylinderSpawnShapeValue) {
/*  91 */       CylinderSpawnShapeValue cylinderSpawnShapeValue = this.cylinderSpawnShapeValue;
/*  92 */     }  if (spawnShapeValue instanceof UnweightedMeshSpawnShapeValue) {
/*  93 */       UnweightedMeshSpawnShapeValue unweightedMeshSpawnShapeValue = this.meshSpawnShapeValue;
/*  94 */     } else if (spawnShapeValue instanceof WeightMeshSpawnShapeValue) {
/*  95 */       weightMeshSpawnShapeValue = this.weightMeshSpawnShapeValue;
/*  96 */     }  weightMeshSpawnShapeValue.load((ParticleValue)spawnShapeValue);
/*     */   }
/*     */   
/*     */   protected void setSpawnShapeValue(SpawnShapeValue spawnShapeValue) {
/* 100 */     this.xPanel.setValue(spawnShapeValue.xOffsetValue);
/* 101 */     this.yPanel.setValue(spawnShapeValue.yOffsetValue);
/* 102 */     this.zPanel.setValue(spawnShapeValue.zOffsetValue);
/*     */   }
/*     */   
/*     */   protected void setPrimitiveSpawnShape(PrimitiveSpawnShapeValue shape, boolean showEdges, PrimitiveSpawnShapeValue.SpawnSide side) {
/* 106 */     setSpawnShapeValue((SpawnShapeValue)shape);
/* 107 */     SpawnInfluencer influencer = (SpawnInfluencer)this.editor.getEmitter().findInfluencer(SpawnInfluencer.class);
/* 108 */     influencer.spawnShapeValue = (SpawnShapeValue)shape;
/* 109 */     this.widthPanel.setValue(shape.getSpawnWidth());
/* 110 */     this.heightPanel.setValue(shape.getSpawnHeight());
/* 111 */     this.depthPanel.setValue(shape.getSpawnDepth());
/* 112 */     setEdgesVisible(showEdges);
/* 113 */     if (showEdges)
/* 114 */       this.edgesCheckbox.setSelected(shape.isEdges()); 
/* 115 */     if (side != null) {
/* 116 */       setSidesVisible(true);
/* 117 */       this.sideCombo.setSelectedItem(side);
/*     */     } else {
/*     */       
/* 120 */       setSidesVisible(false);
/*     */     } 
/*     */     
/* 123 */     this.widthPanel.setVisible(true);
/* 124 */     this.heightPanel.setVisible(true);
/* 125 */     this.depthPanel.setVisible(true);
/* 126 */     this.meshPanel.setVisible(false);
/*     */   }
/*     */   
/*     */   protected void setMeshSpawnShape(SpawnShapeValue shape) {
/* 130 */     setSpawnShapeValue(shape);
/* 131 */     this.value.spawnShapeValue = shape;
/* 132 */     setEdgesVisible(false);
/* 133 */     setSidesVisible(false);
/* 134 */     this.widthPanel.setVisible(false);
/* 135 */     this.heightPanel.setVisible(false);
/* 136 */     this.depthPanel.setVisible(false);
/* 137 */     this.meshPanel.setVisible(true);
/*     */   }
/*     */   
/*     */   private Object spawnShapeToString(SpawnShapeValue spawnShapeValue) {
/* 141 */     if (spawnShapeValue instanceof PrimitiveSpawnShapeValue) {
/* 142 */       if (spawnShapeValue instanceof PointSpawnShapeValue) return "Point"; 
/* 143 */       if (spawnShapeValue instanceof LineSpawnShapeValue) return "Line"; 
/* 144 */       if (spawnShapeValue instanceof RectangleSpawnShapeValue) return "Rectangle"; 
/* 145 */       if (spawnShapeValue instanceof EllipseSpawnShapeValue) return "Ellipse"; 
/* 146 */       if (spawnShapeValue instanceof CylinderSpawnShapeValue) return "Cylinder"; 
/*     */     } 
/* 148 */     if (spawnShapeValue instanceof WeightMeshSpawnShapeValue) {
/* 149 */       return "Weighted Mesh";
/*     */     }
/* 151 */     if (spawnShapeValue instanceof UnweightedMeshSpawnShapeValue) {
/* 152 */       return "Unweighted Mesh";
/*     */     }
/*     */     
/* 155 */     return null;
/*     */   }
/*     */   
/*     */   public void update(FlameMain editor) {
/* 159 */     SpawnInfluencer influencer = (SpawnInfluencer)editor.getEmitter().findInfluencer(SpawnInfluencer.class);
/* 160 */     this.shapeCombo.setSelectedItem(spawnShapeToString(influencer.spawnShapeValue));
/*     */   }
/*     */ 
/*     */   
/*     */   void setEdgesVisible(boolean visible) {
/* 165 */     this.edgesCheckbox.setVisible(visible);
/* 166 */     this.edgesLabel.setVisible(visible);
/*     */   }
/*     */ 
/*     */   
/*     */   void setSidesVisible(boolean visible) {
/* 171 */     this.sideCombo.setVisible(visible);
/* 172 */     this.sideLabel.setVisible(visible);
/*     */   }
/*     */   
/*     */   protected void initializeComponents() {
/* 176 */     super.initializeComponents();
/*     */     
/* 178 */     this.pointSpawnShapeValue = new PointSpawnShapeValue();
/* 179 */     this.lineSpawnShapeValue = new LineSpawnShapeValue();
/* 180 */     this.rectangleSpawnShapeValue = new RectangleSpawnShapeValue();
/* 181 */     this.ellipseSpawnShapeValue = new EllipseSpawnShapeValue();
/* 182 */     this.cylinderSpawnShapeValue = new CylinderSpawnShapeValue();
/* 183 */     this.meshSpawnShapeValue = new UnweightedMeshSpawnShapeValue();
/* 184 */     this.weightMeshSpawnShapeValue = new WeightMeshSpawnShapeValue();
/*     */     
/* 186 */     this.lineSpawnShapeValue.setDimensions(6.0F, 6.0F, 6.0F);
/* 187 */     this.rectangleSpawnShapeValue.setDimensions(6.0F, 6.0F, 6.0F);
/* 188 */     this.ellipseSpawnShapeValue.setDimensions(6.0F, 6.0F, 6.0F);
/* 189 */     this.cylinderSpawnShapeValue.setDimensions(6.0F, 6.0F, 6.0F);
/*     */     
/* 191 */     this.pointSpawnShapeValue.setActive(true);
/* 192 */     this.lineSpawnShapeValue.setActive(true);
/* 193 */     this.rectangleSpawnShapeValue.setActive(true);
/* 194 */     this.ellipseSpawnShapeValue.setActive(true);
/* 195 */     this.cylinderSpawnShapeValue.setActive(true);
/* 196 */     this.meshSpawnShapeValue.setActive(true);
/* 197 */     this.weightMeshSpawnShapeValue.setActive(true);
/*     */     
/* 199 */     Model defaultModel = (Model)this.editor.assetManager.get("monkey.g3db");
/* 200 */     Array<Model> models = new Array();
/* 201 */     models.add(defaultModel);
/*     */     
/* 203 */     int i = 0;
/* 204 */     JPanel panel = new JPanel(new GridBagLayout());
/* 205 */     EditorPanel.addContent(panel, i, 0, new JLabel("Shape"), false, 17, 0, 0.0F, 0.0F);
/* 206 */     EditorPanel.addContent(panel, i++, 1, this.shapeCombo = new JComboBox(new DefaultComboBoxModel((Object[])spawnShapes)), false, 17, 0, 1.0F, 0.0F);
/* 207 */     EditorPanel.addContent(panel, i, 0, this.edgesLabel = new JLabel("Edges"), false, 17, 0, 0.0F, 0.0F);
/* 208 */     EditorPanel.addContent(panel, i++, 1, this.edgesCheckbox = new JCheckBox(), false, 17, 0, 0.0F, 0.0F);
/* 209 */     EditorPanel.addContent(panel, i, 0, this.sideLabel = new JLabel("Side"), false, 17, 0, 0.0F, 0.0F);
/* 210 */     EditorPanel.addContent(panel, i++, 1, this.sideCombo = new JComboBox(new DefaultComboBoxModel((Object[])PrimitiveSpawnShapeValue.SpawnSide.values())), false, 17, 0, 1.0F, 0.0F);
/* 211 */     this.edgesCheckbox.setHorizontalTextPosition(2);
/*     */     
/* 213 */     i = 0;
/* 214 */     addContent(i++, 0, panel, 17, 2);
/* 215 */     addContent(i++, 0, this.meshPanel = new TemplatePickerPanel<Model>(this.editor, models, this, Model.class, new LoaderButton.ModelLoaderButton(this.editor), true, false), false, 17, 0);
/*     */     
/* 217 */     addContent(i++, 0, this.xPanel = new RangedNumericPanel(this.editor, this.pointSpawnShapeValue.xOffsetValue, "X Offset", "Amount to offset a particle's starting X location, in world units.", false));
/* 218 */     addContent(i++, 0, this.yPanel = new RangedNumericPanel(this.editor, this.pointSpawnShapeValue.yOffsetValue, "Y Offset", "Amount to offset a particle's starting Y location, in world units.", false));
/* 219 */     addContent(i++, 0, this.zPanel = new RangedNumericPanel(this.editor, this.pointSpawnShapeValue.zOffsetValue, "Z Offset", "Amount to offset a particle's starting Z location, in world units.", false));
/* 220 */     addContent(i++, 0, this.widthPanel = new ScaledNumericPanel(this.editor, this.pointSpawnShapeValue.getSpawnWidth(), "Duration", "Spawn Width", "Width of the spawn shape, in world units.", true));
/* 221 */     addContent(i++, 0, this.heightPanel = new ScaledNumericPanel(this.editor, this.pointSpawnShapeValue.getSpawnWidth(), "Duration", "Spawn Height", "Height of the spawn shape, in world units.", true));
/* 222 */     addContent(i++, 0, this.depthPanel = new ScaledNumericPanel(this.editor, this.pointSpawnShapeValue.getSpawnWidth(), "Duration", "Spawn Depth", "Depth of the spawn shape, in world units.", true), false);
/*     */     
/* 224 */     this.meshPanel.setIsAlwayShown(true);
/* 225 */     onTemplateChecked(defaultModel, true);
/*     */     
/* 227 */     this.shapeCombo.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 229 */             String shape = (String)SpawnInfluencerPanel.this.shapeCombo.getSelectedItem();
/* 230 */             if (shape == "Point") {
/* 231 */               SpawnInfluencerPanel.this.setPrimitiveSpawnShape((PrimitiveSpawnShapeValue)SpawnInfluencerPanel.this.pointSpawnShapeValue, false, (PrimitiveSpawnShapeValue.SpawnSide)null);
/*     */             }
/* 233 */             else if (shape == "Line") {
/* 234 */               SpawnInfluencerPanel.this.setPrimitiveSpawnShape((PrimitiveSpawnShapeValue)SpawnInfluencerPanel.this.lineSpawnShapeValue, false, (PrimitiveSpawnShapeValue.SpawnSide)null);
/*     */             }
/* 236 */             else if (shape == "Rectangle") {
/* 237 */               SpawnInfluencerPanel.this.setPrimitiveSpawnShape((PrimitiveSpawnShapeValue)SpawnInfluencerPanel.this.rectangleSpawnShapeValue, true, (PrimitiveSpawnShapeValue.SpawnSide)null);
/*     */             }
/* 239 */             else if (shape == "Ellipse") {
/* 240 */               SpawnInfluencerPanel.this.setPrimitiveSpawnShape((PrimitiveSpawnShapeValue)SpawnInfluencerPanel.this.ellipseSpawnShapeValue, true, SpawnInfluencerPanel.this.ellipseSpawnShapeValue.getSide());
/*     */             }
/* 242 */             else if (shape == "Cylinder") {
/* 243 */               SpawnInfluencerPanel.this.setPrimitiveSpawnShape((PrimitiveSpawnShapeValue)SpawnInfluencerPanel.this.cylinderSpawnShapeValue, true, (PrimitiveSpawnShapeValue.SpawnSide)null);
/*     */             }
/* 245 */             else if (shape == "Unweighted Mesh") {
/* 246 */               SpawnInfluencerPanel.this.setMeshSpawnShape((SpawnShapeValue)SpawnInfluencerPanel.this.meshSpawnShapeValue);
/*     */             }
/* 248 */             else if (shape == "Weighted Mesh") {
/* 249 */               SpawnInfluencerPanel.this.setMeshSpawnShape((SpawnShapeValue)SpawnInfluencerPanel.this.weightMeshSpawnShapeValue);
/*     */             } 
/* 251 */             SpawnInfluencerPanel.this.editor.restart();
/*     */           }
/*     */         });
/*     */     
/* 255 */     this.edgesCheckbox.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 257 */             SpawnInfluencer influencer = (SpawnInfluencer)SpawnInfluencerPanel.this.editor.getEmitter().findInfluencer(SpawnInfluencer.class);
/* 258 */             PrimitiveSpawnShapeValue shapeValue = (PrimitiveSpawnShapeValue)influencer.spawnShapeValue;
/* 259 */             shapeValue.setEdges(SpawnInfluencerPanel.this.edgesCheckbox.isSelected());
/* 260 */             SpawnInfluencerPanel.this.setEdgesVisible(true);
/*     */           }
/*     */         });
/*     */     
/* 264 */     this.sideCombo.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 266 */             PrimitiveSpawnShapeValue.SpawnSide side = (PrimitiveSpawnShapeValue.SpawnSide)SpawnInfluencerPanel.this.sideCombo.getSelectedItem();
/* 267 */             SpawnInfluencer influencer = (SpawnInfluencer)SpawnInfluencerPanel.this.editor.getEmitter().findInfluencer(SpawnInfluencer.class);
/* 268 */             EllipseSpawnShapeValue shapeValue = (EllipseSpawnShapeValue)influencer.spawnShapeValue;
/* 269 */             shapeValue.setSide(side);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onTemplateChecked(Model model, boolean isChecked) {
/* 278 */     SpawnShapeValue shapeValue = null;
/* 279 */     Mesh mesh = (Mesh)model.meshes.get(0);
/* 280 */     this.weightMeshSpawnShapeValue.setMesh(mesh, model);
/* 281 */     this.meshSpawnShapeValue.setMesh(mesh, model);
/* 282 */     if (this.shapeCombo.getSelectedItem() == "Weighted Mesh") {
/* 283 */       SpawnInfluencer influencer = (SpawnInfluencer)this.editor.getEmitter().findInfluencer(SpawnInfluencer.class);
/* 284 */       influencer.spawnShapeValue = (SpawnShapeValue)this.weightMeshSpawnShapeValue;
/*     */     }
/* 286 */     else if (this.shapeCombo.getSelectedItem() == "Unweighted Mesh") {
/* 287 */       SpawnInfluencer influencer = (SpawnInfluencer)this.editor.getEmitter().findInfluencer(SpawnInfluencer.class);
/* 288 */       influencer.spawnShapeValue = (SpawnShapeValue)this.meshSpawnShapeValue;
/*     */     } 
/* 290 */     this.editor.restart();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\SpawnInfluencerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */