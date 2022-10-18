/*    */ package com.badlogic.gdx.tools.particleeditor;
/*    */ 
/*    */ import java.awt.BorderLayout;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.EventQueue;
/*    */ import javax.swing.JFrame;
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
/*    */ class Slider
/*    */   extends JPanel
/*    */ {
/*    */   private JSpinner spinner;
/*    */   
/*    */   public Slider(float initialValue, float min, float max, float stepSize, float sliderMin, float sliderMax) {
/* 44 */     this.spinner = new JSpinner(new SpinnerNumberModel(initialValue, min, max, stepSize));
/* 45 */     setLayout(new BorderLayout());
/* 46 */     add(this.spinner);
/*    */   }
/*    */   
/*    */   public void setValue(float value) {
/* 50 */     this.spinner.setValue(Double.valueOf(value));
/*    */   }
/*    */   
/*    */   public float getValue() {
/* 54 */     return ((Double)this.spinner.getValue()).floatValue();
/*    */   }
/*    */   
/*    */   public void addChangeListener(ChangeListener listener) {
/* 58 */     this.spinner.addChangeListener(listener);
/*    */   }
/*    */   
/*    */   public Dimension getPreferredSize() {
/* 62 */     Dimension size = super.getPreferredSize();
/* 63 */     size.width = 75;
/* 64 */     size.height = 26;
/* 65 */     return size;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 69 */     EventQueue.invokeLater(new Runnable() {
/*    */           public void run() {
/* 71 */             JFrame frame = new JFrame();
/* 72 */             frame.setDefaultCloseOperation(2);
/* 73 */             frame.setSize(480, 320);
/* 74 */             frame.setLocationRelativeTo(null);
/* 75 */             JPanel panel = new JPanel();
/* 76 */             frame.getContentPane().add(panel);
/* 77 */             panel.add(new Slider(200.0F, 100.0F, 500.0F, 0.1F, 150.0F, 300.0F));
/* 78 */             frame.setVisible(true);
/*    */           }
/*    */         });
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\Slider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */