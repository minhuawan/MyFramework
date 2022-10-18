/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.values.NumericValue;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.values.ParticleValue;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.Insets;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JSpinner;
/*    */ import javax.swing.SpinnerNumberModel;
/*    */ import javax.swing.event.ChangeEvent;
/*    */ import javax.swing.event.ChangeListener;
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
/*    */ 
/*    */ class NumericPanel
/*    */   extends ParticleValuePanel<NumericValue>
/*    */ {
/*    */   JSpinner valueSpinner;
/*    */   
/*    */   public NumericPanel(FlameMain editor, NumericValue value, String name, String description) {
/* 36 */     super(editor, name, description);
/* 37 */     setValue(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(NumericValue value) {
/* 42 */     super.setValue(value);
/* 43 */     if (value == null)
/* 44 */       return;  setValue(this.valueSpinner, Float.valueOf(value.getValue()));
/*    */   }
/*    */   
/*    */   protected void initializeComponents() {
/* 48 */     super.initializeComponents();
/* 49 */     JPanel contentPanel = getContentPanel();
/*    */     
/* 51 */     JLabel label = new JLabel("Value:");
/* 52 */     contentPanel.add(label, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 13, 0, new Insets(0, 0, 0, 6), 0, 0));
/*    */ 
/*    */ 
/*    */     
/* 56 */     this.valueSpinner = new JSpinner(new SpinnerNumberModel(new Float(0.0F), new Float(-99999.0F), new Float(99999.0F), new Float(0.1F)));
/* 57 */     contentPanel.add(this.valueSpinner, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */     
/* 60 */     this.valueSpinner.addChangeListener(new ChangeListener() {
/*    */           public void stateChanged(ChangeEvent event) {
/* 62 */             NumericPanel.this.value.setValue(((Float)NumericPanel.this.valueSpinner.getValue()).floatValue());
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\NumericPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */