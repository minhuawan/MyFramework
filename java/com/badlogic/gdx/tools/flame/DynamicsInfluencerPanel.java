/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsInfluencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.ParticleValue;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.event.TableModelEvent;
/*     */ import javax.swing.event.TableModelListener;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ 
/*     */ public class DynamicsInfluencerPanel
/*     */   extends InfluencerPanel<DynamicsInfluencer>
/*     */ {
/*     */   private static final String VEL_TYPE_ROTATIONAL_2D = "Angular Velocity 2D";
/*     */   private static final String VEL_TYPE_ROTATIONAL_3D = "Angular Velocity 3D";
/*     */   private static final String VEL_TYPE_CENTRIPETAL = "Centripetal";
/*     */   private static final String VEL_TYPE_TANGENTIAL = "Tangential";
/*     */   private static final String VEL_TYPE_POLAR = "Polar";
/*     */   private static final String VEL_TYPE_BROWNIAN = "Brownian";
/*     */   private static final String VEL_TYPE_FACE = "Face";
/*     */   JComboBox velocityBox;
/*     */   JTable velocityTable;
/*     */   DefaultTableModel velocityTableModel;
/*     */   JPanel selectedVelocityPanel;
/*     */   AngularVelocityPanel angularVelocityPanel;
/*     */   StrengthVelocityPanel strengthVelocityPanel;
/*     */   ParticleValuePanel emptyPanel;
/*     */   Array<VelocityWrapper> velocities;
/*     */   
/*     */   protected class VelocityWrapper
/*     */   {
/*     */     public DynamicsModifier velocityValue;
/*     */     public boolean isActive;
/*     */     
/*     */     public VelocityWrapper(DynamicsModifier value, boolean isActive) {
/*  51 */       this.velocityValue = value;
/*  52 */       this.isActive = isActive;
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
/*     */   
/*     */   public DynamicsInfluencerPanel(FlameMain editor, DynamicsInfluencer influencer) {
/*  66 */     super(editor, influencer, "Dynamics Influencer", "Defines how the particles dynamics (acceleration, angular velocity).");
/*     */     
/*  68 */     this.velocities = new Array();
/*  69 */     setValue(this.value);
/*  70 */     set(influencer);
/*     */   }
/*     */   
/*     */   private void set(DynamicsInfluencer influencer) {
/*     */     int i;
/*  75 */     for (i = this.velocityTableModel.getRowCount() - 1; i >= 0; i--) {
/*  76 */       this.velocityTableModel.removeRow(i);
/*     */     }
/*  78 */     this.velocities.clear();
/*     */     
/*     */     int c;
/*  81 */     for (i = 0, c = influencer.velocities.size; i < c; i++) {
/*  82 */       this.velocities.add(new VelocityWrapper(((DynamicsModifier[])influencer.velocities.items)[i], true));
/*  83 */       this.velocityTableModel.addRow(new Object[] { "Velocity " + i, Boolean.valueOf(true) });
/*     */     } 
/*     */     
/*  86 */     DefaultComboBoxModel<Object> model = (DefaultComboBoxModel)this.velocityBox.getModel();
/*  87 */     model.removeAllElements();
/*  88 */     for (Object velocityObject : getAvailableVelocities(this.editor.getControllerType())) {
/*  89 */       model.addElement(velocityObject);
/*     */     }
/*     */   }
/*     */   
/*     */   private Object[] getAvailableVelocities(FlameMain.ControllerType type) {
/*  94 */     if (type == FlameMain.ControllerType.Billboard || type == FlameMain.ControllerType.PointSprite) {
/*  95 */       return (Object[])new String[] { "Angular Velocity 2D", "Centripetal", "Tangential", "Polar", "Brownian" };
/*     */     }
/*     */     
/*  98 */     if (type == FlameMain.ControllerType.ModelInstance || type == FlameMain.ControllerType.ParticleController) {
/*  99 */       return (Object[])new String[] { "Angular Velocity 3D", "Centripetal", "Tangential", "Polar", "Brownian", "Face" };
/*     */     }
/*     */     
/* 102 */     return null;
/*     */   }
/*     */   
/*     */   protected void initializeComponents() {
/* 106 */     super.initializeComponents();
/* 107 */     JPanel velocitiesPanel = new JPanel();
/* 108 */     velocitiesPanel.setLayout(new GridBagLayout());
/*     */     
/* 110 */     JPanel sideButtons = new JPanel(new GridBagLayout());
/* 111 */     velocitiesPanel.add(sideButtons, new GridBagConstraints(1, 0, 1, 1, 1.0D, 1.0D, 18, 0, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */     
/* 114 */     sideButtons.add(this.velocityBox = new JComboBox(new DefaultComboBoxModel()), new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 119 */     JButton newButton = new JButton("New");
/* 120 */     sideButtons.add(newButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 122 */     newButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 124 */             DynamicsInfluencerPanel.this.createVelocity(DynamicsInfluencerPanel.this.velocityBox.getSelectedItem());
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 129 */     JButton deleteButton = new JButton("Delete");
/* 130 */     sideButtons.add(deleteButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 132 */     deleteButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 134 */             DynamicsInfluencerPanel.this.deleteVelocity();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 139 */     JScrollPane scroll = new JScrollPane();
/* 140 */     velocitiesPanel.add(scroll, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */     
/* 142 */     this.velocityTable = new JTable() {
/*     */         public Class getColumnClass(int column) {
/* 144 */           return (column == 1) ? Boolean.class : super.getColumnClass(column);
/*     */         }
/*     */ 
/*     */         
/*     */         public Dimension getPreferredScrollableViewportSize() {
/* 149 */           Dimension dim = super.getPreferredScrollableViewportSize();
/* 150 */           dim.height = (getPreferredSize()).height;
/* 151 */           return dim;
/*     */         }
/*     */       };
/* 154 */     this.velocityTable.getTableHeader().setReorderingAllowed(false);
/* 155 */     this.velocityTable.setSelectionMode(0);
/* 156 */     scroll.setViewportView(this.velocityTable);
/* 157 */     this.velocityTableModel = new DefaultTableModel((Object[][])new String[0][0], (Object[])new String[] { "Velocity", "Active" });
/* 158 */     this.velocityTable.setModel(this.velocityTableModel);
/* 159 */     this.velocityTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
/*     */           public void valueChanged(ListSelectionEvent event) {
/* 161 */             if (event.getValueIsAdjusting())
/* 162 */               return;  DynamicsInfluencerPanel.this.velocitySelected();
/*     */           }
/*     */         });
/* 165 */     this.velocityTableModel.addTableModelListener(new TableModelListener() {
/*     */           public void tableChanged(TableModelEvent event) {
/* 167 */             if (event.getColumn() != 1)
/* 168 */               return;  DynamicsInfluencerPanel.this.velocityChecked(event.getFirstRow(), ((Boolean)DynamicsInfluencerPanel.this.velocityTable.getValueAt(event.getFirstRow(), 1)).booleanValue());
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 173 */     this.emptyPanel = new ParticleValuePanel<ParticleValue>(this.editor, "", "", true, false);
/* 174 */     this.strengthVelocityPanel = new StrengthVelocityPanel(this.editor, null, "Life", "", "");
/* 175 */     this.angularVelocityPanel = new AngularVelocityPanel(this.editor, null, "Life", "", "");
/* 176 */     this.strengthVelocityPanel.setVisible(false);
/* 177 */     this.angularVelocityPanel.setVisible(false);
/* 178 */     this.emptyPanel.setVisible(false);
/* 179 */     this.strengthVelocityPanel.setIsAlwayShown(true);
/* 180 */     this.angularVelocityPanel.setIsAlwayShown(true);
/* 181 */     this.emptyPanel.setIsAlwayShown(true);
/* 182 */     this.emptyPanel.setValue((Object)null);
/*     */ 
/*     */     
/* 185 */     int i = 0;
/* 186 */     addContent(i++, 0, velocitiesPanel);
/* 187 */     addContent(i++, 0, this.strengthVelocityPanel);
/* 188 */     addContent(i++, 0, this.angularVelocityPanel);
/* 189 */     addContent(i++, 0, this.emptyPanel);
/*     */   }
/*     */   
/*     */   protected void velocityChecked(int index, boolean isChecked) {
/* 193 */     ParticleController controller = this.editor.getEmitter();
/* 194 */     DynamicsInfluencer influencer = (DynamicsInfluencer)controller.findInfluencer(DynamicsInfluencer.class);
/* 195 */     influencer.velocities.clear();
/* 196 */     ((VelocityWrapper)this.velocities.get(index)).isActive = isChecked;
/* 197 */     for (VelocityWrapper wrapper : this.velocities) {
/* 198 */       if (wrapper.isActive) {
/* 199 */         influencer.velocities.add(wrapper.velocityValue);
/*     */       }
/*     */     } 
/* 202 */     this.editor.restart();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void velocitySelected() {
/* 207 */     int index = this.velocityTable.getSelectedRow();
/* 208 */     if (index == -1)
/*     */       return; 
/* 210 */     DynamicsModifier velocityValue = ((VelocityWrapper)this.velocities.get(index)).velocityValue;
/* 211 */     EditorPanel velocityPanel = getVelocityPanel(velocityValue);
/*     */ 
/*     */     
/* 214 */     if (this.selectedVelocityPanel != null && this.selectedVelocityPanel != velocityPanel)
/* 215 */       this.selectedVelocityPanel.setVisible(false); 
/* 216 */     velocityPanel.setVisible(true);
/* 217 */     velocityPanel.showContent(true);
/* 218 */     this.selectedVelocityPanel = velocityPanel;
/*     */   }
/*     */   
/*     */   private EditorPanel getVelocityPanel(DynamicsModifier velocityValue) {
/* 222 */     EditorPanel panel = null;
/*     */     
/* 224 */     if (velocityValue instanceof DynamicsModifier.Rotational2D) {
/* 225 */       this.strengthVelocityPanel.setValue((DynamicsModifier.Strength)velocityValue);
/* 226 */       this.strengthVelocityPanel.setName("Angular Velocity");
/* 227 */       this.strengthVelocityPanel.setDescription("The angular speed around the billboard facing direction, in degrees/sec .");
/* 228 */       panel = this.strengthVelocityPanel;
/*     */     }
/* 230 */     else if (velocityValue instanceof DynamicsModifier.CentripetalAcceleration) {
/* 231 */       this.strengthVelocityPanel.setValue((DynamicsModifier.Strength)velocityValue);
/* 232 */       this.strengthVelocityPanel.setName("Centripetal Acceleration");
/* 233 */       this.strengthVelocityPanel.setDescription("A directional acceleration, the direction is towards the origin (global), or towards the emitter position (local), in world units/sec2 .");
/* 234 */       panel = this.strengthVelocityPanel;
/*     */     }
/* 236 */     else if (velocityValue instanceof DynamicsModifier.TangentialAcceleration) {
/* 237 */       this.angularVelocityPanel.setValue((DynamicsModifier.Angular)velocityValue);
/* 238 */       this.angularVelocityPanel.setName("Tangetial Velocity");
/* 239 */       this.angularVelocityPanel.setDescription("A directional acceleration (axis and magnitude), the final direction is the cross product between particle position and the axis, in world units/sec2 .");
/* 240 */       panel = this.angularVelocityPanel;
/*     */     }
/* 242 */     else if (velocityValue instanceof DynamicsModifier.PolarAcceleration) {
/* 243 */       this.angularVelocityPanel.setValue((DynamicsModifier.Angular)velocityValue);
/* 244 */       this.angularVelocityPanel.setName("Polar Velocity");
/* 245 */       this.angularVelocityPanel.setDescription("A directional acceleration (axis and magnitude), in world units/sec2 .");
/* 246 */       panel = this.angularVelocityPanel;
/*     */     }
/* 248 */     else if (velocityValue instanceof DynamicsModifier.BrownianAcceleration) {
/* 249 */       this.strengthVelocityPanel.setValue((DynamicsModifier.Strength)velocityValue);
/* 250 */       this.strengthVelocityPanel.setName("Brownian Velocity");
/* 251 */       this.strengthVelocityPanel.setDescription("A directional acceleration which has random direction at each update, in world units/sec2.");
/* 252 */       panel = this.strengthVelocityPanel;
/*     */     }
/* 254 */     else if (velocityValue instanceof DynamicsModifier.Rotational3D) {
/* 255 */       this.angularVelocityPanel.setValue((DynamicsModifier.Angular)velocityValue);
/* 256 */       this.angularVelocityPanel.setName("Angular Velocity");
/* 257 */       this.angularVelocityPanel.setDescription("An angular velocity (axis and magnitude), in degree/sec2.");
/* 258 */       panel = this.angularVelocityPanel;
/*     */     }
/* 260 */     else if (velocityValue instanceof DynamicsModifier.FaceDirection) {
/* 261 */       this.emptyPanel.setName("Face");
/* 262 */       this.emptyPanel.setDescription("Rotates the model to face its current velocity (Do not add any other angular velocity when using this).");
/* 263 */       panel = this.emptyPanel;
/*     */     } 
/*     */     
/* 266 */     return panel;
/*     */   }
/*     */   private DynamicsModifier createVelocityValue(Object selectedItem) {
/*     */     DynamicsModifier.FaceDirection faceDirection;
/* 270 */     DynamicsModifier velocityValue = null;
/* 271 */     if (selectedItem == "Angular Velocity 2D") { DynamicsModifier.Rotational2D rotational2D = new DynamicsModifier.Rotational2D(); }
/* 272 */     else if (selectedItem == "Angular Velocity 3D") { DynamicsModifier.Rotational3D rotational3D = new DynamicsModifier.Rotational3D(); }
/* 273 */     else if (selectedItem == "Centripetal") { DynamicsModifier.CentripetalAcceleration centripetalAcceleration = new DynamicsModifier.CentripetalAcceleration(); }
/* 274 */     else if (selectedItem == "Tangential") { DynamicsModifier.TangentialAcceleration tangentialAcceleration = new DynamicsModifier.TangentialAcceleration(); }
/* 275 */     else if (selectedItem == "Polar") { DynamicsModifier.PolarAcceleration polarAcceleration = new DynamicsModifier.PolarAcceleration(); }
/* 276 */     else if (selectedItem == "Brownian") { DynamicsModifier.BrownianAcceleration brownianAcceleration = new DynamicsModifier.BrownianAcceleration(); }
/* 277 */     else if (selectedItem == "Face") { faceDirection = new DynamicsModifier.FaceDirection(); }
/* 278 */      return (DynamicsModifier)faceDirection;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void deleteVelocity() {
/* 283 */     int row = this.velocityTable.getSelectedRow();
/* 284 */     if (row == -1) {
/*     */       return;
/*     */     }
/* 287 */     ParticleController controller = this.editor.getEmitter();
/* 288 */     DynamicsInfluencer influencer = (DynamicsInfluencer)controller.findInfluencer(DynamicsInfluencer.class);
/* 289 */     influencer.velocities.removeValue(((VelocityWrapper)this.velocities.removeIndex(row)).velocityValue, true);
/* 290 */     this.velocityTableModel.removeRow(row);
/*     */ 
/*     */     
/* 293 */     this.editor.restart();
/*     */     
/* 295 */     this.selectedVelocityPanel.setVisible(false);
/* 296 */     this.selectedVelocityPanel = null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createVelocity(Object selectedItem) {
/* 301 */     ParticleController controller = this.editor.getEmitter();
/* 302 */     DynamicsInfluencer influencer = (DynamicsInfluencer)controller.findInfluencer(DynamicsInfluencer.class);
/* 303 */     VelocityWrapper wrapper = new VelocityWrapper(createVelocityValue(selectedItem), true);
/* 304 */     this.velocities.add(wrapper);
/* 305 */     influencer.velocities.add(wrapper.velocityValue);
/* 306 */     int index = this.velocities.size - 1;
/* 307 */     this.velocityTableModel.addRow(new Object[] { "Velocity " + index, Boolean.valueOf(true) });
/*     */ 
/*     */     
/* 310 */     this.editor.restart();
/*     */ 
/*     */     
/* 313 */     this.velocityTable.getSelectionModel().setSelectionInterval(index, index);
/* 314 */     revalidate();
/* 315 */     repaint();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\DynamicsInfluencerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */