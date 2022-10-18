/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.JPanel;
/*    */ import javax.swing.JSpinner;
/*    */ import javax.swing.SpinnerNumberModel;
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
/*    */ public class Slider
/*    */   extends JPanel
/*    */ {
/*    */   public JSpinner spinner;
/*    */   
/*    */   public Slider(float initialValue, float min, float max, float stepSize) {
/* 32 */     this.spinner = new JSpinner(new SpinnerNumberModel(initialValue, min, max, stepSize));
/* 33 */     setLayout(new BorderLayout());
/* 34 */     add(this.spinner);
/*    */   }
/*    */   
/*    */   public void setValue(float value) {
/* 38 */     this.spinner.setValue(Double.valueOf(value));
/*    */   }
/*    */   
/*    */   public float getValue() {
/* 42 */     return ((Double)this.spinner.getValue()).floatValue();
/*    */   }
/*    */   
/*    */   public void addChangeListener(ChangeListener listener) {
/* 46 */     this.spinner.addChangeListener(listener);
/*    */   }
/*    */   
/*    */   public Dimension getPreferredSize() {
/* 50 */     Dimension size = super.getPreferredSize();
/* 51 */     size.width = 75;
/* 52 */     size.height = 26;
/* 53 */     return size;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\Slider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */