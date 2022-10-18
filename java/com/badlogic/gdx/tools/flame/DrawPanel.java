/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.Insets;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JCheckBox;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ public class DrawPanel
/*    */   extends EditorPanel
/*    */ {
/*    */   JCheckBox drawXYZCheckBox;
/*    */   JCheckBox drawXZPlaneBox;
/*    */   JCheckBox drawXYPlaneBox;
/*    */   
/*    */   public DrawPanel(FlameMain editor, String name, String description) {
/* 19 */     super(editor, name, description);
/* 20 */     setValue(null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void initializeComponents() {
/* 25 */     super.initializeComponents();
/* 26 */     JPanel contentPanel = getContentPanel();
/*    */ 
/*    */     
/* 29 */     contentPanel.add(new JLabel("XYZ:"), new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(6, 0, 0, 0), 0, 0));
/*    */     
/* 31 */     this.drawXYZCheckBox = new JCheckBox();
/* 32 */     contentPanel.add(this.drawXYZCheckBox, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 6, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 36 */     contentPanel.add(new JLabel("XZ Plane:"), new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(6, 0, 0, 0), 0, 0));
/*    */     
/* 38 */     this.drawXZPlaneBox = new JCheckBox();
/* 39 */     contentPanel.add(this.drawXZPlaneBox, new GridBagConstraints(1, 2, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 6, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 43 */     contentPanel.add(new JLabel("XY Plane:"), new GridBagConstraints(0, 3, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(6, 0, 0, 0), 0, 0));
/*    */     
/* 45 */     this.drawXYPlaneBox = new JCheckBox();
/* 46 */     contentPanel.add(this.drawXYPlaneBox, new GridBagConstraints(1, 3, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 6, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 50 */     this.drawXYZCheckBox.addActionListener(new ActionListener() {
/*    */           public void actionPerformed(ActionEvent event) {
/* 52 */             DrawPanel.this.editor.getRenderer().setDrawXYZ(DrawPanel.this.drawXYZCheckBox.isSelected());
/*    */           }
/*    */         });
/* 55 */     this.drawXYZCheckBox.setSelected(this.editor.getRenderer().IsDrawXYZ());
/*    */     
/* 57 */     this.drawXZPlaneBox.addActionListener(new ActionListener() {
/*    */           public void actionPerformed(ActionEvent event) {
/* 59 */             DrawPanel.this.editor.getRenderer().setDrawXZPlane(DrawPanel.this.drawXZPlaneBox.isSelected());
/*    */           }
/*    */         });
/* 62 */     this.drawXZPlaneBox.setSelected(this.editor.getRenderer().IsDrawXZPlane());
/*    */     
/* 64 */     this.drawXYPlaneBox.addActionListener(new ActionListener() {
/*    */           public void actionPerformed(ActionEvent event) {
/* 66 */             DrawPanel.this.editor.getRenderer().setDrawXYPlane(DrawPanel.this.drawXYPlaneBox.isSelected());
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\DrawPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */