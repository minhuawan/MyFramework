/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.influencers.DynamicsModifier;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.Insets;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JCheckBox;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ 
/*    */ public class AngularVelocityPanel
/*    */   extends EditorPanel<DynamicsModifier.Angular>
/*    */ {
/*    */   JCheckBox isGlobalCheckBox;
/*    */   ScaledNumericPanel thetaPanel;
/*    */   ScaledNumericPanel phiPanel;
/*    */   ScaledNumericPanel magnitudePanel;
/*    */   
/*    */   public AngularVelocityPanel(FlameMain editor, DynamicsModifier.Angular aValue, String charTitle, String name, String description) {
/* 23 */     super(editor, name, description);
/* 24 */     initializeComponents(aValue, charTitle);
/* 25 */     setValue(this.value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(DynamicsModifier.Angular value) {
/* 30 */     super.setValue(value);
/* 31 */     if (value == null)
/* 32 */       return;  setValue(this.isGlobalCheckBox, this.value.isGlobal);
/* 33 */     this.magnitudePanel.setValue(this.value.strengthValue);
/* 34 */     this.thetaPanel.setValue(this.value.thetaValue);
/* 35 */     this.phiPanel.setValue(this.value.phiValue);
/*    */   }
/*    */   
/*    */   private void initializeComponents(DynamicsModifier.Angular aValue, String charTitle) {
/* 39 */     JPanel contentPanel = getContentPanel();
/*    */     
/* 41 */     JPanel panel = new JPanel();
/* 42 */     panel.add(new JLabel("Global"), new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */     
/* 44 */     panel.add(this.isGlobalCheckBox = new JCheckBox(), new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */     
/* 47 */     contentPanel.add(panel, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 52 */     contentPanel.add(this.magnitudePanel = new ScaledNumericPanel(this.editor, (aValue == null) ? null : aValue.strengthValue, charTitle, "Strength", "In world units per second.", true), new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 6), 0, 0));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 57 */     contentPanel.add(this.phiPanel = new ScaledNumericPanel(this.editor, (aValue == null) ? null : aValue.phiValue, charTitle, "Azimuth", "Rotation starting on Y", true), new GridBagConstraints(0, 4, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 6), 0, 0));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 62 */     contentPanel.add(this.thetaPanel = new ScaledNumericPanel(this.editor, (aValue == null) ? null : aValue.thetaValue, charTitle, "Polar angle", "around Y axis on XZ plane", true), new GridBagConstraints(0, 3, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 6), 0, 0));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 67 */     JPanel spacer = new JPanel();
/* 68 */     spacer.setPreferredSize(new Dimension());
/* 69 */     contentPanel.add(spacer, new GridBagConstraints(6, 0, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 73 */     this.magnitudePanel.setIsAlwayShown(true);
/* 74 */     this.phiPanel.setIsAlwayShown(true);
/* 75 */     this.thetaPanel.setIsAlwayShown(true);
/*    */     
/* 77 */     this.isGlobalCheckBox.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 80 */             AngularVelocityPanel.this.value.isGlobal = AngularVelocityPanel.this.isGlobalCheckBox.isSelected();
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public ScaledNumericPanel getThetaPanel() {
/* 86 */     return this.thetaPanel;
/*    */   }
/*    */   
/*    */   public ScaledNumericPanel getPhiPanel() {
/* 90 */     return this.phiPanel;
/*    */   }
/*    */   
/*    */   public ScaledNumericPanel getMagnitudePanel() {
/* 94 */     return this.magnitudePanel;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\AngularVelocityPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */