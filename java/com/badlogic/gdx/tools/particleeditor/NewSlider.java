/*     */ package com.badlogic.gdx.tools.particleeditor;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.event.ChangeEvent;
/*     */ import javax.swing.event.ChangeListener;
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
/*     */ class NewSlider
/*     */   extends JPanel
/*     */ {
/*     */   private static final int KNOB_WIDTH = 10;
/*     */   float value;
/*     */   float min;
/*     */   float max;
/*     */   float stepSize;
/*     */   float sliderMin;
/*     */   float sliderMax;
/*     */   ChangeListener listener;
/*  40 */   int border = 2;
/*  41 */   Color bgColor = new Color(0.6F, 0.6F, 0.6F);
/*  42 */   Color knobColor = Color.lightGray;
/*     */ 
/*     */   
/*     */   public NewSlider(float initialValue, float min, float max, float stepSize, final float sliderMin, final float sliderMax) {
/*  46 */     this.min = min;
/*  47 */     this.max = max;
/*  48 */     this.stepSize = stepSize;
/*  49 */     this.sliderMin = sliderMin;
/*  50 */     this.sliderMax = sliderMax;
/*  51 */     this.value = Math.max(min, Math.min(max, initialValue));
/*     */     
/*  53 */     setLayout(new GridBagLayout());
/*     */     
/*  55 */     addMouseListener(new MouseAdapter() {
/*     */           public void mousePressed(MouseEvent event) {
/*  57 */             float width = (NewSlider.this.getWidth() - 10 - NewSlider.this.border * 2);
/*  58 */             float mouseX = (event.getX() - 5 - NewSlider.this.border);
/*  59 */             NewSlider.this.setValue(sliderMin + (sliderMax - sliderMin) * Math.max(0.0F, Math.min(width, mouseX)) / width);
/*     */           }
/*     */ 
/*     */           
/*     */           public void mouseReleased(MouseEvent event) {}
/*     */ 
/*     */           
/*     */           public void mouseClicked(MouseEvent event) {
/*  67 */             NewSlider.this.repaint();
/*     */           }
/*     */         });
/*  70 */     addMouseMotionListener(new MouseMotionListener() {
/*     */           public void mouseDragged(MouseEvent event) {
/*  72 */             float width = (NewSlider.this.getWidth() - 10 - NewSlider.this.border * 2);
/*  73 */             float mouseX = (event.getX() - 5 - NewSlider.this.border);
/*  74 */             NewSlider.this.setValue(sliderMin + (sliderMax - sliderMin) * Math.max(0.0F, Math.min(width, mouseX)) / width);
/*     */           }
/*     */           
/*     */           public void mouseMoved(MouseEvent event) {
/*  78 */             int mouseX = event.getX();
/*  79 */             int mouseY = event.getY();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics graphics) {
/*  86 */     super.paintComponent(graphics);
/*     */     
/*  88 */     Graphics2D g = (Graphics2D)graphics;
/*  89 */     int width = getWidth();
/*  90 */     int height = getHeight();
/*     */     
/*  92 */     g.setColor(this.bgColor);
/*  93 */     g.fillRect(this.border, this.border, width - this.border * 2, height - this.border * 2);
/*     */     
/*  95 */     int maxKnobX = width - this.border - 10;
/*  96 */     int knobX = (int)((width - this.border * 2 - 10) * (this.value - this.sliderMin) / (this.sliderMax - this.sliderMin)) + this.border;
/*  97 */     g.setColor(this.knobColor);
/*  98 */     g.fillRect(Math.max(this.border, Math.min(maxKnobX, knobX)), 0, 10, height);
/*     */     
/* 100 */     float displayValue = (int)(this.value * 10.0F) / 10.0F;
/* 101 */     String label = (displayValue == (int)displayValue) ? String.valueOf((int)displayValue) : String.valueOf(displayValue);
/* 102 */     FontMetrics metrics = g.getFontMetrics();
/* 103 */     int labelWidth = metrics.stringWidth(label);
/* 104 */     g.setColor(Color.white);
/* 105 */     g.drawString(label, width / 2 - labelWidth / 2, height / 2 + metrics.getAscent() / 2);
/*     */   }
/*     */   
/*     */   public void setValue(float value) {
/* 109 */     this.value = (int)(Math.max(this.min, Math.min(this.max, value)) / this.stepSize) * this.stepSize;
/* 110 */     repaint();
/* 111 */     if (this.listener != null) this.listener.stateChanged(new ChangeEvent(this)); 
/*     */   }
/*     */   
/*     */   public float getValue() {
/* 115 */     return this.value;
/*     */   }
/*     */   
/*     */   public void addChangeListener(ChangeListener listener) {
/* 119 */     this.listener = listener;
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize() {
/* 123 */     Dimension size = super.getPreferredSize();
/* 124 */     size.width = 150;
/* 125 */     size.height = 26;
/* 126 */     return size;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) throws Exception {
/* 130 */     EventQueue.invokeLater(new Runnable() {
/*     */           public void run() {
/* 132 */             JFrame frame = new JFrame();
/* 133 */             frame.setDefaultCloseOperation(2);
/* 134 */             frame.setSize(480, 320);
/* 135 */             frame.setLocationRelativeTo(null);
/* 136 */             JPanel panel = new JPanel();
/* 137 */             frame.getContentPane().add(panel);
/* 138 */             panel.add(new NewSlider(200.0F, 100.0F, 500.0F, 0.1F, 150.0F, 300.0F));
/* 139 */             frame.setVisible(true);
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\NewSlider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */