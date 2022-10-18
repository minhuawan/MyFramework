/*     */ package com.badlogic.gdx.tools.particleeditor;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GradientPaint;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Paint;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseMotionAdapter;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JColorChooser;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSlider;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ class GradientPanel
/*     */   extends EditorPanel
/*     */ {
/*     */   private final ParticleEmitter.GradientColorValue value;
/*     */   private GradientEditor gradientEditor;
/*     */   ColorSlider saturationSlider;
/*     */   ColorSlider lightnessSlider;
/*     */   JPanel colorPanel;
/*     */   private ColorSlider hueSlider;
/*     */   
/*     */   public GradientPanel(ParticleEmitter.GradientColorValue value, String name, String description, boolean hideGradientEditor) {
/*  49 */     super((ParticleEmitter.ParticleValue)value, name, description);
/*  50 */     this.value = value;
/*     */     
/*  52 */     initializeComponents();
/*     */     
/*  54 */     if (hideGradientEditor) {
/*  55 */       this.gradientEditor.setVisible(false);
/*     */     }
/*  57 */     this.gradientEditor.percentages.clear();
/*  58 */     for (float percent : value.getTimeline()) {
/*  59 */       this.gradientEditor.percentages.add(Float.valueOf(percent));
/*     */     }
/*  61 */     this.gradientEditor.colors.clear();
/*  62 */     float[] colors = value.getColors();
/*  63 */     for (int i = 0; i < colors.length; ) {
/*  64 */       float r = colors[i++];
/*  65 */       float g = colors[i++];
/*  66 */       float b = colors[i++];
/*  67 */       this.gradientEditor.colors.add(new Color(r, g, b));
/*     */     } 
/*  69 */     if (this.gradientEditor.colors.isEmpty() || this.gradientEditor.percentages.isEmpty()) {
/*  70 */       this.gradientEditor.percentages.clear();
/*  71 */       this.gradientEditor.percentages.add(Float.valueOf(0.0F));
/*  72 */       this.gradientEditor.percentages.add(Float.valueOf(1.0F));
/*  73 */       this.gradientEditor.colors.clear();
/*  74 */       this.gradientEditor.colors.add(Color.white);
/*     */     } 
/*  76 */     setColor(this.gradientEditor.colors.get(0));
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize() {
/*  80 */     Dimension size = super.getPreferredSize();
/*  81 */     size.width = 10;
/*  82 */     return size;
/*     */   }
/*     */   
/*     */   private void initializeComponents() {
/*  86 */     JPanel contentPanel = getContentPanel();
/*     */     
/*  88 */     this.gradientEditor = new GradientEditor() {
/*     */         public void handleSelected(Color color) {
/*  90 */           GradientPanel.this.setColor(color);
/*     */         }
/*     */       };
/*  93 */     contentPanel.add(this.gradientEditor, new GridBagConstraints(0, 1, 3, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 10));
/*     */ 
/*     */ 
/*     */     
/*  97 */     this.hueSlider = new ColorSlider(new Color[] { Color.red, Color.yellow, Color.green, Color.cyan, Color.blue, Color.magenta, Color.red })
/*     */       {
/*     */         protected void colorPicked() {
/* 100 */           GradientPanel.this.saturationSlider.setColors(new Color[] { new Color(Color.HSBtoRGB(getPercentage(), 1.0F, 1.0F)), Color.white });
/* 101 */           GradientPanel.this.updateColor();
/*     */         }
/*     */       };
/* 104 */     contentPanel.add(this.hueSlider, new GridBagConstraints(1, 2, 2, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 108 */     this.saturationSlider = new ColorSlider(new Color[] { Color.red, Color.white }) {
/*     */         protected void colorPicked() {
/* 110 */           GradientPanel.this.updateColor();
/*     */         }
/*     */       };
/* 113 */     contentPanel.add(this.saturationSlider, new GridBagConstraints(1, 3, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 117 */     this.lightnessSlider = new ColorSlider(new Color[0]) {
/*     */         protected void colorPicked() {
/* 119 */           GradientPanel.this.updateColor();
/*     */         }
/*     */       };
/* 122 */     contentPanel.add(this.lightnessSlider, new GridBagConstraints(2, 3, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 126 */     this.colorPanel = new JPanel() {
/*     */         public Dimension getPreferredSize() {
/* 128 */           Dimension size = super.getPreferredSize();
/* 129 */           size.width = 52;
/* 130 */           return size;
/*     */         }
/*     */       };
/* 133 */     contentPanel.add(this.colorPanel, new GridBagConstraints(0, 2, 1, 2, 0.0D, 0.0D, 10, 1, new Insets(3, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 137 */     this.colorPanel.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 139 */             Color color = JColorChooser.showDialog(GradientPanel.this.colorPanel, "Set Color", GradientPanel.this.colorPanel.getBackground());
/* 140 */             if (color != null) GradientPanel.this.setColor(color); 
/*     */           }
/*     */         });
/* 143 */     this.colorPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
/*     */   }
/*     */   
/*     */   public void setColor(Color color) {
/* 147 */     float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
/* 148 */     this.hueSlider.setPercentage(hsb[0]);
/* 149 */     this.saturationSlider.setPercentage(1.0F - hsb[1]);
/* 150 */     this.lightnessSlider.setPercentage(1.0F - hsb[2]);
/* 151 */     this.colorPanel.setBackground(color);
/*     */   }
/*     */   
/*     */   void updateColor() {
/* 155 */     Color color = new Color(Color.HSBtoRGB(this.hueSlider.getPercentage(), 1.0F - this.saturationSlider.getPercentage(), 1.0F));
/* 156 */     this.lightnessSlider.setColors(new Color[] { color, Color.black });
/* 157 */     color = new Color(Color.HSBtoRGB(this.hueSlider.getPercentage(), 1.0F - this.saturationSlider.getPercentage(), 1.0F - this.lightnessSlider
/* 158 */           .getPercentage()));
/* 159 */     this.colorPanel.setBackground(color);
/* 160 */     this.gradientEditor.setColor(color);
/*     */     
/* 162 */     float[] colors = new float[this.gradientEditor.colors.size() * 3];
/* 163 */     int i = 0;
/* 164 */     for (Color c : this.gradientEditor.colors) {
/* 165 */       colors[i++] = c.getRed() / 255.0F;
/* 166 */       colors[i++] = c.getGreen() / 255.0F;
/* 167 */       colors[i++] = c.getBlue() / 255.0F;
/*     */     } 
/* 169 */     float[] percentages = new float[this.gradientEditor.percentages.size()];
/* 170 */     i = 0;
/* 171 */     for (Float percent : this.gradientEditor.percentages)
/* 172 */       percentages[i++] = percent.floatValue(); 
/* 173 */     this.value.setColors(colors);
/* 174 */     this.value.setTimeline(percentages);
/*     */   }
/*     */   
/*     */   public class GradientEditor extends JPanel {
/* 178 */     ArrayList<Color> colors = new ArrayList<Color>();
/* 179 */     ArrayList<Float> percentages = new ArrayList<Float>();
/*     */     
/* 181 */     int handleWidth = 12;
/* 182 */     int handleHeight = 12;
/* 183 */     int gradientX = this.handleWidth / 2;
/* 184 */     int gradientY = 0;
/*     */     int gradientWidth;
/*     */     int gradientHeight;
/* 187 */     int dragIndex = -1;
/*     */     int selectedIndex;
/*     */     
/*     */     public GradientEditor() {
/* 191 */       setPreferredSize(new Dimension(100, 30));
/*     */       
/* 193 */       addMouseListener(new MouseAdapter() {
/*     */             public void mousePressed(MouseEvent event) {
/* 195 */               GradientPanel.GradientEditor.this.dragIndex = -1;
/* 196 */               int mouseX = event.getX();
/* 197 */               int mouseY = event.getY();
/* 198 */               int y = GradientPanel.GradientEditor.this.gradientY + GradientPanel.GradientEditor.this.gradientHeight;
/* 199 */               for (int i = 0, n = GradientPanel.GradientEditor.this.colors.size(); i < n; i++) {
/* 200 */                 int x = GradientPanel.GradientEditor.this.gradientX + (int)(((Float)GradientPanel.GradientEditor.this.percentages.get(i)).floatValue() * GradientPanel.GradientEditor.this.gradientWidth) - GradientPanel.GradientEditor.this.handleWidth / 2;
/* 201 */                 if (mouseX >= x && mouseX <= x + GradientPanel.GradientEditor.this.handleWidth && mouseY >= GradientPanel.GradientEditor.this.gradientY && mouseY <= y + GradientPanel.GradientEditor.this.handleHeight) {
/* 202 */                   GradientPanel.GradientEditor.this.dragIndex = GradientPanel.GradientEditor.this.selectedIndex = i;
/* 203 */                   GradientPanel.GradientEditor.this.handleSelected(GradientPanel.GradientEditor.this.colors.get(GradientPanel.GradientEditor.this.selectedIndex));
/* 204 */                   GradientPanel.GradientEditor.this.repaint();
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             }
/*     */             
/*     */             public void mouseReleased(MouseEvent event) {
/* 211 */               if (GradientPanel.GradientEditor.this.dragIndex != -1) {
/* 212 */                 GradientPanel.GradientEditor.this.dragIndex = -1;
/* 213 */                 GradientPanel.GradientEditor.this.repaint();
/*     */               } 
/*     */             }
/*     */             
/*     */             public void mouseClicked(MouseEvent event) {
/* 218 */               int mouseX = event.getX();
/* 219 */               int mouseY = event.getY();
/* 220 */               if (event.getClickCount() == 2) {
/* 221 */                 if (GradientPanel.GradientEditor.this.percentages.size() <= 1)
/* 222 */                   return;  if (GradientPanel.GradientEditor.this.selectedIndex == -1 || GradientPanel.GradientEditor.this.selectedIndex == 0)
/* 223 */                   return;  int y = GradientPanel.GradientEditor.this.gradientY + GradientPanel.GradientEditor.this.gradientHeight;
/* 224 */                 int x = GradientPanel.GradientEditor.this.gradientX + (int)(((Float)GradientPanel.GradientEditor.this.percentages.get(GradientPanel.GradientEditor.this.selectedIndex)).floatValue() * GradientPanel.GradientEditor.this.gradientWidth) - GradientPanel.GradientEditor.this.handleWidth / 2;
/* 225 */                 if (mouseX >= x && mouseX <= x + GradientPanel.GradientEditor.this.handleWidth && mouseY >= GradientPanel.GradientEditor.this.gradientY && mouseY <= y + GradientPanel.GradientEditor.this.handleHeight) {
/* 226 */                   GradientPanel.GradientEditor.this.percentages.remove(GradientPanel.GradientEditor.this.selectedIndex);
/* 227 */                   GradientPanel.GradientEditor.this.colors.remove(GradientPanel.GradientEditor.this.selectedIndex);
/*     */                   
/* 229 */                   GradientPanel.GradientEditor.this.dragIndex = --GradientPanel.GradientEditor.this.selectedIndex;
/* 230 */                   if (GradientPanel.GradientEditor.this.percentages.size() == 2) GradientPanel.GradientEditor.this.percentages.set(1, Float.valueOf(1.0F)); 
/* 231 */                   GradientPanel.GradientEditor.this.handleSelected(GradientPanel.GradientEditor.this.colors.get(GradientPanel.GradientEditor.this.selectedIndex));
/* 232 */                   GradientPanel.GradientEditor.this.repaint();
/*     */                 } 
/*     */                 return;
/*     */               } 
/* 236 */               if (mouseX < GradientPanel.GradientEditor.this.gradientX || mouseX > GradientPanel.GradientEditor.this.gradientX + GradientPanel.GradientEditor.this.gradientWidth)
/* 237 */                 return;  if (mouseY < GradientPanel.GradientEditor.this.gradientY || mouseY > GradientPanel.GradientEditor.this.gradientY + GradientPanel.GradientEditor.this.gradientHeight)
/* 238 */                 return;  float percent = (event.getX() - GradientPanel.GradientEditor.this.gradientX) / GradientPanel.GradientEditor.this.gradientWidth;
/* 239 */               if (GradientPanel.GradientEditor.this.percentages.size() == 1) percent = 1.0F; 
/* 240 */               for (int i = 0, n = GradientPanel.GradientEditor.this.percentages.size(); i <= n; i++) {
/* 241 */                 if (i == n || percent < ((Float)GradientPanel.GradientEditor.this.percentages.get(i)).floatValue()) {
/* 242 */                   GradientPanel.GradientEditor.this.percentages.add(i, Float.valueOf(percent));
/* 243 */                   GradientPanel.GradientEditor.this.colors.add(i, GradientPanel.GradientEditor.this.colors.get(i - 1));
/* 244 */                   GradientPanel.GradientEditor.this.dragIndex = GradientPanel.GradientEditor.this.selectedIndex = i;
/* 245 */                   GradientPanel.GradientEditor.this.handleSelected(GradientPanel.GradientEditor.this.colors.get(GradientPanel.GradientEditor.this.selectedIndex));
/* 246 */                   GradientPanel.this.updateColor();
/* 247 */                   GradientPanel.GradientEditor.this.repaint();
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           });
/* 253 */       addMouseMotionListener(new MouseMotionAdapter() {
/*     */             public void mouseDragged(MouseEvent event) {
/* 255 */               if (GradientPanel.GradientEditor.this.dragIndex == -1 || GradientPanel.GradientEditor.this.dragIndex == 0 || GradientPanel.GradientEditor.this.dragIndex == GradientPanel.GradientEditor.this.percentages.size() - 1)
/* 256 */                 return;  float percent = (event.getX() - GradientPanel.GradientEditor.this.gradientX) / GradientPanel.GradientEditor.this.gradientWidth;
/* 257 */               percent = Math.max(percent, ((Float)GradientPanel.GradientEditor.this.percentages.get(GradientPanel.GradientEditor.this.dragIndex - 1)).floatValue() + 0.01F);
/* 258 */               percent = Math.min(percent, ((Float)GradientPanel.GradientEditor.this.percentages.get(GradientPanel.GradientEditor.this.dragIndex + 1)).floatValue() - 0.01F);
/* 259 */               GradientPanel.GradientEditor.this.percentages.set(GradientPanel.GradientEditor.this.dragIndex, Float.valueOf(percent));
/* 260 */               GradientPanel.this.updateColor();
/* 261 */               GradientPanel.GradientEditor.this.repaint();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public void setColor(Color color) {
/* 267 */       if (this.selectedIndex == -1)
/* 268 */         return;  this.colors.set(this.selectedIndex, color);
/* 269 */       repaint();
/*     */     }
/*     */ 
/*     */     
/*     */     public void handleSelected(Color color) {}
/*     */     
/*     */     protected void paintComponent(Graphics graphics) {
/* 276 */       super.paintComponent(graphics);
/* 277 */       Graphics2D g = (Graphics2D)graphics;
/* 278 */       int width = getWidth() - 1;
/* 279 */       int height = getHeight();
/*     */       
/* 281 */       this.gradientWidth = width - this.handleWidth;
/* 282 */       this.gradientHeight = height - 16;
/*     */       
/* 284 */       g.translate(this.gradientX, this.gradientY);
/* 285 */       for (int i = 0, n = (this.colors.size() == 1) ? 1 : (this.colors.size() - 1); i < n; i++) {
/* 286 */         Color color1 = this.colors.get(i);
/* 287 */         Color color2 = (this.colors.size() == 1) ? color1 : this.colors.get(i + 1);
/* 288 */         float percent1 = ((Float)this.percentages.get(i)).floatValue();
/* 289 */         float percent2 = (this.colors.size() == 1) ? 1.0F : ((Float)this.percentages.get(i + 1)).floatValue();
/* 290 */         int point1 = (int)(percent1 * this.gradientWidth);
/* 291 */         int point2 = (int)Math.ceil((percent2 * this.gradientWidth));
/* 292 */         g.setPaint(new GradientPaint(point1, 0.0F, color1, point2, 0.0F, color2, false));
/* 293 */         g.fillRect(point1, 0, point2 - point1, this.gradientHeight);
/*     */       } 
/* 295 */       g.setPaint((Paint)null);
/* 296 */       g.setColor(Color.black);
/* 297 */       g.drawRect(0, 0, this.gradientWidth, this.gradientHeight);
/*     */       
/* 299 */       int y = this.gradientHeight;
/* 300 */       int[] yPoints = new int[3];
/* 301 */       yPoints[0] = y;
/* 302 */       yPoints[1] = y + this.handleHeight;
/* 303 */       yPoints[2] = y + this.handleHeight;
/* 304 */       int[] xPoints = new int[3];
/* 305 */       for (int j = 0, k = this.colors.size(); j < k; j++) {
/* 306 */         int x = (int)(((Float)this.percentages.get(j)).floatValue() * this.gradientWidth);
/* 307 */         xPoints[0] = x;
/* 308 */         xPoints[1] = x - this.handleWidth / 2;
/* 309 */         xPoints[2] = x + this.handleWidth / 2;
/* 310 */         if (j == this.selectedIndex) {
/* 311 */           g.setColor(this.colors.get(j));
/* 312 */           g.fillPolygon(xPoints, yPoints, 3);
/* 313 */           g.fillRect(xPoints[1], yPoints[1] + 2, this.handleWidth + 1, 2);
/* 314 */           g.setColor(Color.black);
/*     */         } 
/* 316 */         g.drawPolygon(xPoints, yPoints, 3);
/*     */       } 
/* 318 */       g.translate(-this.gradientX, -this.gradientY);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ColorSlider extends JPanel {
/*     */     Color[] paletteColors;
/*     */     JSlider slider;
/*     */     private ColorPicker colorPicker;
/*     */     
/*     */     public ColorSlider(Color[] paletteColors) {
/* 328 */       this.paletteColors = paletteColors;
/* 329 */       setLayout(new GridBagLayout());
/*     */       
/* 331 */       this.slider = new JSlider(0, 1000, 0);
/* 332 */       this.slider.setPaintTrack(false);
/* 333 */       add(this.slider, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 6, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */       
/* 337 */       this.colorPicker = new ColorPicker();
/* 338 */       add(this.colorPicker, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 6, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */       
/* 342 */       this.slider.addChangeListener(new ChangeListener() {
/*     */             public void stateChanged(ChangeEvent event) {
/* 344 */               GradientPanel.ColorSlider.this.colorPicked();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public Dimension getPreferredSize() {
/* 350 */       Dimension size = super.getPreferredSize();
/* 351 */       size.width = 10;
/* 352 */       return size;
/*     */     }
/*     */     
/*     */     public void setPercentage(float percent) {
/* 356 */       this.slider.setValue((int)(1000.0F * percent));
/*     */     }
/*     */     
/*     */     public float getPercentage() {
/* 360 */       return this.slider.getValue() / 1000.0F;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void colorPicked() {}
/*     */     
/*     */     public void setColors(Color[] colors) {
/* 367 */       this.paletteColors = colors;
/* 368 */       repaint();
/*     */     }
/*     */     
/*     */     public class ColorPicker extends JPanel {
/*     */       public ColorPicker() {
/* 373 */         addMouseListener(new MouseAdapter() {
/*     */               public void mouseClicked(MouseEvent event) {
/* 375 */                 GradientPanel.ColorSlider.this.slider.setValue((int)(event.getX() / GradientPanel.ColorSlider.ColorPicker.this.getWidth() * 1000.0F));
/*     */               }
/*     */             });
/*     */       }
/*     */       
/*     */       protected void paintComponent(Graphics graphics) {
/* 381 */         Graphics2D g = (Graphics2D)graphics;
/* 382 */         int width = getWidth() - 1;
/* 383 */         int height = getHeight() - 1;
/* 384 */         for (int i = 0, n = GradientPanel.ColorSlider.this.paletteColors.length - 1; i < n; i++) {
/* 385 */           Color color1 = GradientPanel.ColorSlider.this.paletteColors[i];
/* 386 */           Color color2 = GradientPanel.ColorSlider.this.paletteColors[i + 1];
/* 387 */           float point1 = i / n * width;
/* 388 */           float point2 = (i + 1) / n * width;
/* 389 */           g.setPaint(new GradientPaint(point1, 0.0F, color1, point2, 0.0F, color2, false));
/* 390 */           g.fillRect((int)point1, 0, (int)Math.ceil((point2 - point1)), height);
/*     */         } 
/* 392 */         g.setPaint((Paint)null);
/* 393 */         g.setColor(Color.black);
/* 394 */         g.drawRect(0, 0, width, height);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\GradientPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */