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
/*    */ 
/*    */ public class StrengthVelocityPanel
/*    */   extends EditorPanel<DynamicsModifier.Strength>
/*    */ {
/*    */   JCheckBox isGlobalCheckBox;
/*    */   ScaledNumericPanel magnitudePanel;
/*    */   
/*    */   public StrengthVelocityPanel(FlameMain editor, DynamicsModifier.Strength value, String charTitle, String name, String description) {
/* 22 */     super(editor, name, description);
/* 23 */     initializeComponents(charTitle);
/* 24 */     setValue(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(DynamicsModifier.Strength value) {
/* 29 */     super.setValue(value);
/* 30 */     if (value == null)
/* 31 */       return;  setValue(this.isGlobalCheckBox, this.value.isGlobal);
/* 32 */     this.magnitudePanel.setValue(value.strengthValue);
/*    */   }
/*    */ 
/*    */   
/*    */   private void initializeComponents(String charTitle) {
/* 37 */     JPanel contentPanel = getContentPanel();
/*    */     
/* 39 */     JPanel panel = new JPanel();
/* 40 */     panel.add(new JLabel("Global"), new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */     
/* 42 */     panel.add(this.isGlobalCheckBox = new JCheckBox(), new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */     
/* 45 */     contentPanel.add(panel, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     contentPanel.add(this.magnitudePanel = new ScaledNumericPanel(this.editor, null, charTitle, "Strength", "In world units per second.", true), new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 6), 0, 0));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 55 */     JPanel spacer = new JPanel();
/* 56 */     spacer.setPreferredSize(new Dimension());
/* 57 */     contentPanel.add(spacer, new GridBagConstraints(6, 0, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 61 */     this.magnitudePanel.setIsAlwayShown(true);
/*    */     
/* 63 */     this.isGlobalCheckBox.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent e) {
/* 66 */             StrengthVelocityPanel.this.value.isGlobal = StrengthVelocityPanel.this.isGlobalCheckBox.isSelected();
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\StrengthVelocityPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */