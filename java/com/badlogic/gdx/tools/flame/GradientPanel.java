/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g3d.particles.values.GradientColorValue;
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
/*     */ 
/*     */ class GradientPanel
/*     */   extends ParticleValuePanel<GradientColorValue>
/*     */ {
/*     */   private GradientEditor gradientEditor;
/*     */   ColorSlider saturationSlider;
/*     */   ColorSlider lightnessSlider;
/*     */   JPanel colorPanel;
/*     */   private ColorSlider hueSlider;
/*     */   
/*     */   public GradientPanel(FlameMain editor, GradientColorValue value, String name, String description, boolean hideGradientEditor) {
/*  49 */     super(editor, name, description);
/*  50 */     setValue(value);
/*     */     
/*  52 */     if (hideGradientEditor) {
/*  53 */       this.gradientEditor.setVisible(false);
/*     */     }
/*  55 */     this.gradientEditor.percentages.clear();
/*  56 */     for (float percent : value.getTimeline()) {
/*  57 */       this.gradientEditor.percentages.add(Float.valueOf(percent));
/*     */     }
/*  59 */     this.gradientEditor.colors.clear();
/*  60 */     float[] colors = value.getColors();
/*  61 */     for (int i = 0; i < colors.length; ) {
/*  62 */       float r = colors[i++];
/*  63 */       float g = colors[i++];
/*  64 */       float b = colors[i++];
/*  65 */       this.gradientEditor.colors.add(new Color(r, g, b));
/*     */     } 
/*  67 */     if (this.gradientEditor.colors.isEmpty() || this.gradientEditor.percentages.isEmpty()) {
/*  68 */       this.gradientEditor.percentages.clear();
/*  69 */       this.gradientEditor.percentages.add(Float.valueOf(0.0F));
/*  70 */       this.gradientEditor.percentages.add(Float.valueOf(1.0F));
/*  71 */       this.gradientEditor.colors.clear();
/*  72 */       this.gradientEditor.colors.add(Color.white);
/*     */     } 
/*  74 */     setColor(this.gradientEditor.colors.get(0));
/*     */   }
/*     */   
/*     */   public Dimension getPreferredSize() {
/*  78 */     Dimension size = super.getPreferredSize();
/*  79 */     size.width = 10;
/*  80 */     return size;
/*     */   }
/*     */   
/*     */   protected void initializeComponents() {
/*  84 */     super.initializeComponents();
/*  85 */     JPanel contentPanel = getContentPanel();
/*     */     
/*  87 */     this.gradientEditor = new GradientEditor() {
/*     */         public void handleSelected(Color color) {
/*  89 */           GradientPanel.this.setColor(color);
/*     */         }
/*     */       };
/*  92 */     contentPanel.add(this.gradientEditor, new GridBagConstraints(0, 1, 3, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 10));
/*     */ 
/*     */ 
/*     */     
/*  96 */     this.hueSlider = new ColorSlider(new Color[] { Color.red, Color.yellow, Color.green, Color.cyan, Color.blue, Color.magenta, Color.red })
/*     */       {
/*     */         protected void colorPicked() {
/*  99 */           GradientPanel.this.saturationSlider.setColors(new Color[] { new Color(Color.HSBtoRGB(getPercentage(), 1.0F, 1.0F)), Color.white });
/* 100 */           GradientPanel.this.updateColor();
/*     */         }
/*     */       };
/* 103 */     contentPanel.add(this.hueSlider, new GridBagConstraints(1, 2, 2, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 107 */     this.saturationSlider = new ColorSlider(new Color[] { Color.red, Color.white }) {
/*     */         protected void colorPicked() {
/* 109 */           GradientPanel.this.updateColor();
/*     */         }
/*     */       };
/* 112 */     contentPanel.add(this.saturationSlider, new GridBagConstraints(1, 3, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 116 */     this.lightnessSlider = new ColorSlider(new Color[0]) {
/*     */         protected void colorPicked() {
/* 118 */           GradientPanel.this.updateColor();
/*     */         }
/*     */       };
/* 121 */     contentPanel.add(this.lightnessSlider, new GridBagConstraints(2, 3, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 125 */     this.colorPanel = new JPanel() {
/*     */         public Dimension getPreferredSize() {
/* 127 */           Dimension size = super.getPreferredSize();
/* 128 */           size.width = 52;
/* 129 */           return size;
/*     */         }
/*     */       };
/* 132 */     contentPanel.add(this.colorPanel, new GridBagConstraints(0, 2, 1, 2, 0.0D, 0.0D, 10, 1, new Insets(3, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 136 */     this.colorPanel.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent e) {
/* 138 */             Color color = JColorChooser.showDialog(GradientPanel.this.colorPanel, "Set Color", GradientPanel.this.colorPanel.getBackground());
/* 139 */             if (color != null) GradientPanel.this.setColor(color); 
/*     */           }
/*     */         });
/* 142 */     this.colorPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
/*     */   }
/*     */   
/*     */   public void setColor(Color color) {
/* 146 */     float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
/* 147 */     this.hueSlider.setPercentage(hsb[0]);
/* 148 */     this.saturationSlider.setPercentage(1.0F - hsb[1]);
/* 149 */     this.lightnessSlider.setPercentage(1.0F - hsb[2]);
/* 150 */     this.colorPanel.setBackground(color);
/*     */   }
/*     */   
/*     */   void updateColor() {
/* 154 */     Color color = new Color(Color.HSBtoRGB(this.hueSlider.getPercentage(), 1.0F - this.saturationSlider.getPercentage(), 1.0F));
/* 155 */     this.lightnessSlider.setColors(new Color[] { color, Color.black });
/* 156 */     color = new Color(Color.HSBtoRGB(this.hueSlider.getPercentage(), 1.0F - this.saturationSlider.getPercentage(), 1.0F - this.lightnessSlider
/* 157 */           .getPercentage()));
/* 158 */     this.colorPanel.setBackground(color);
/* 159 */     this.gradientEditor.setColor(color);
/*     */     
/* 161 */     float[] colors = new float[this.gradientEditor.colors.size() * 3];
/* 162 */     int i = 0;
/* 163 */     for (Color c : this.gradientEditor.colors) {
/* 164 */       colors[i++] = c.getRed() / 255.0F;
/* 165 */       colors[i++] = c.getGreen() / 255.0F;
/* 166 */       colors[i++] = c.getBlue() / 255.0F;
/*     */     } 
/* 168 */     float[] percentages = new float[this.gradientEditor.percentages.size()];
/* 169 */     i = 0;
/* 170 */     for (Float percent : this.gradientEditor.percentages)
/* 171 */       percentages[i++] = percent.floatValue(); 
/* 172 */     this.value.setColors(colors);
/* 173 */     this.value.setTimeline(percentages);
/*     */   }
/*     */   
/*     */   public class GradientEditor extends JPanel {
/* 177 */     ArrayList<Color> colors = new ArrayList<Color>();
/* 178 */     ArrayList<Float> percentages = new ArrayList<Float>();
/*     */     
/* 180 */     int handleWidth = 12;
/* 181 */     int handleHeight = 12;
/* 182 */     int gradientX = this.handleWidth / 2;
/* 183 */     int gradientY = 0;
/*     */     int gradientWidth;
/*     */     int gradientHeight;
/* 186 */     int dragIndex = -1;
/*     */     int selectedIndex;
/*     */     
/*     */     public GradientEditor() {
/* 190 */       setPreferredSize(new Dimension(100, 30));
/*     */       
/* 192 */       addMouseListener(new MouseAdapter() {
/*     */             public void mousePressed(MouseEvent event) {
/* 194 */               GradientPanel.GradientEditor.this.dragIndex = -1;
/* 195 */               int mouseX = event.getX();
/* 196 */               int mouseY = event.getY();
/* 197 */               int y = GradientPanel.GradientEditor.this.gradientY + GradientPanel.GradientEditor.this.gradientHeight;
/* 198 */               for (int i = 0, n = GradientPanel.GradientEditor.this.colors.size(); i < n; i++) {
/* 199 */                 int x = GradientPanel.GradientEditor.this.gradientX + (int)(((Float)GradientPanel.GradientEditor.this.percentages.get(i)).floatValue() * GradientPanel.GradientEditor.this.gradientWidth) - GradientPanel.GradientEditor.this.handleWidth / 2;
/* 200 */                 if (mouseX >= x && mouseX <= x + GradientPanel.GradientEditor.this.handleWidth && mouseY >= GradientPanel.GradientEditor.this.gradientY && mouseY <= y + GradientPanel.GradientEditor.this.handleHeight) {
/* 201 */                   GradientPanel.GradientEditor.this.dragIndex = GradientPanel.GradientEditor.this.selectedIndex = i;
/* 202 */                   GradientPanel.GradientEditor.this.handleSelected(GradientPanel.GradientEditor.this.colors.get(GradientPanel.GradientEditor.this.selectedIndex));
/* 203 */                   GradientPanel.GradientEditor.this.repaint();
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             }
/*     */             
/*     */             public void mouseReleased(MouseEvent event) {
/* 210 */               if (GradientPanel.GradientEditor.this.dragIndex != -1) {
/* 211 */                 GradientPanel.GradientEditor.this.dragIndex = -1;
/* 212 */                 GradientPanel.GradientEditor.this.repaint();
/*     */               } 
/*     */             }
/*     */             
/*     */             public void mouseClicked(MouseEvent event) {
/* 217 */               int mouseX = event.getX();
/* 218 */               int mouseY = event.getY();
/* 219 */               if (event.getClickCount() == 2) {
/* 220 */                 if (GradientPanel.GradientEditor.this.percentages.size() <= 1)
/* 221 */                   return;  if (GradientPanel.GradientEditor.this.selectedIndex == -1 || GradientPanel.GradientEditor.this.selectedIndex == 0)
/* 222 */                   return;  int y = GradientPanel.GradientEditor.this.gradientY + GradientPanel.GradientEditor.this.gradientHeight;
/* 223 */                 int x = GradientPanel.GradientEditor.this.gradientX + (int)(((Float)GradientPanel.GradientEditor.this.percentages.get(GradientPanel.GradientEditor.this.selectedIndex)).floatValue() * GradientPanel.GradientEditor.this.gradientWidth) - GradientPanel.GradientEditor.this.handleWidth / 2;
/* 224 */                 if (mouseX >= x && mouseX <= x + GradientPanel.GradientEditor.this.handleWidth && mouseY >= GradientPanel.GradientEditor.this.gradientY && mouseY <= y + GradientPanel.GradientEditor.this.handleHeight) {
/* 225 */                   GradientPanel.GradientEditor.this.percentages.remove(GradientPanel.GradientEditor.this.selectedIndex);
/* 226 */                   GradientPanel.GradientEditor.this.colors.remove(GradientPanel.GradientEditor.this.selectedIndex);
/*     */                   
/* 228 */                   GradientPanel.GradientEditor.this.dragIndex = --GradientPanel.GradientEditor.this.selectedIndex;
/* 229 */                   if (GradientPanel.GradientEditor.this.percentages.size() == 2) GradientPanel.GradientEditor.this.percentages.set(1, Float.valueOf(1.0F)); 
/* 230 */                   GradientPanel.GradientEditor.this.handleSelected(GradientPanel.GradientEditor.this.colors.get(GradientPanel.GradientEditor.this.selectedIndex));
/* 231 */                   GradientPanel.GradientEditor.this.repaint();
/*     */                 } 
/*     */                 return;
/*     */               } 
/* 235 */               if (mouseX < GradientPanel.GradientEditor.this.gradientX || mouseX > GradientPanel.GradientEditor.this.gradientX + GradientPanel.GradientEditor.this.gradientWidth)
/* 236 */                 return;  if (mouseY < GradientPanel.GradientEditor.this.gradientY || mouseY > GradientPanel.GradientEditor.this.gradientY + GradientPanel.GradientEditor.this.gradientHeight)
/* 237 */                 return;  float percent = (event.getX() - GradientPanel.GradientEditor.this.gradientX) / GradientPanel.GradientEditor.this.gradientWidth;
/* 238 */               if (GradientPanel.GradientEditor.this.percentages.size() == 1) percent = 1.0F; 
/* 239 */               for (int i = 0, n = GradientPanel.GradientEditor.this.percentages.size(); i <= n; i++) {
/* 240 */                 if (i == n || percent < ((Float)GradientPanel.GradientEditor.this.percentages.get(i)).floatValue()) {
/* 241 */                   GradientPanel.GradientEditor.this.percentages.add(i, Float.valueOf(percent));
/* 242 */                   GradientPanel.GradientEditor.this.colors.add(i, GradientPanel.GradientEditor.this.colors.get(i - 1));
/* 243 */                   GradientPanel.GradientEditor.this.dragIndex = GradientPanel.GradientEditor.this.selectedIndex = i;
/* 244 */                   GradientPanel.GradientEditor.this.handleSelected(GradientPanel.GradientEditor.this.colors.get(GradientPanel.GradientEditor.this.selectedIndex));
/* 245 */                   GradientPanel.GradientEditor.this.repaint();
/*     */                   break;
/*     */                 } 
/*     */               } 
/*     */             }
/*     */           });
/* 251 */       addMouseMotionListener(new MouseMotionAdapter() {
/*     */             public void mouseDragged(MouseEvent event) {
/* 253 */               if (GradientPanel.GradientEditor.this.dragIndex == -1 || GradientPanel.GradientEditor.this.dragIndex == 0 || GradientPanel.GradientEditor.this.dragIndex == GradientPanel.GradientEditor.this.percentages.size() - 1)
/* 254 */                 return;  float percent = (event.getX() - GradientPanel.GradientEditor.this.gradientX) / GradientPanel.GradientEditor.this.gradientWidth;
/* 255 */               percent = Math.max(percent, ((Float)GradientPanel.GradientEditor.this.percentages.get(GradientPanel.GradientEditor.this.dragIndex - 1)).floatValue() + 0.01F);
/* 256 */               percent = Math.min(percent, ((Float)GradientPanel.GradientEditor.this.percentages.get(GradientPanel.GradientEditor.this.dragIndex + 1)).floatValue() - 0.01F);
/* 257 */               GradientPanel.GradientEditor.this.percentages.set(GradientPanel.GradientEditor.this.dragIndex, Float.valueOf(percent));
/* 258 */               GradientPanel.GradientEditor.this.repaint();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public void setColor(Color color) {
/* 264 */       if (this.selectedIndex == -1)
/* 265 */         return;  this.colors.set(this.selectedIndex, color);
/* 266 */       repaint();
/*     */     }
/*     */ 
/*     */     
/*     */     public void handleSelected(Color color) {}
/*     */     
/*     */     protected void paintComponent(Graphics graphics) {
/* 273 */       super.paintComponent(graphics);
/* 274 */       Graphics2D g = (Graphics2D)graphics;
/* 275 */       int width = getWidth() - 1;
/* 276 */       int height = getHeight();
/*     */       
/* 278 */       this.gradientWidth = width - this.handleWidth;
/* 279 */       this.gradientHeight = height - 16;
/*     */       
/* 281 */       g.translate(this.gradientX, this.gradientY);
/* 282 */       for (int i = 0, n = (this.colors.size() == 1) ? 1 : (this.colors.size() - 1); i < n; i++) {
/* 283 */         Color color1 = this.colors.get(i);
/* 284 */         Color color2 = (this.colors.size() == 1) ? color1 : this.colors.get(i + 1);
/* 285 */         float percent1 = ((Float)this.percentages.get(i)).floatValue();
/* 286 */         float percent2 = (this.colors.size() == 1) ? 1.0F : ((Float)this.percentages.get(i + 1)).floatValue();
/* 287 */         int point1 = (int)(percent1 * this.gradientWidth);
/* 288 */         int point2 = (int)Math.ceil((percent2 * this.gradientWidth));
/* 289 */         g.setPaint(new GradientPaint(point1, 0.0F, color1, point2, 0.0F, color2, false));
/* 290 */         g.fillRect(point1, 0, point2 - point1, this.gradientHeight);
/*     */       } 
/* 292 */       g.setPaint((Paint)null);
/* 293 */       g.setColor(Color.black);
/* 294 */       g.drawRect(0, 0, this.gradientWidth, this.gradientHeight);
/*     */       
/* 296 */       int y = this.gradientHeight;
/* 297 */       int[] yPoints = new int[3];
/* 298 */       yPoints[0] = y;
/* 299 */       yPoints[1] = y + this.handleHeight;
/* 300 */       yPoints[2] = y + this.handleHeight;
/* 301 */       int[] xPoints = new int[3];
/* 302 */       for (int j = 0, k = this.colors.size(); j < k; j++) {
/* 303 */         int x = (int)(((Float)this.percentages.get(j)).floatValue() * this.gradientWidth);
/* 304 */         xPoints[0] = x;
/* 305 */         xPoints[1] = x - this.handleWidth / 2;
/* 306 */         xPoints[2] = x + this.handleWidth / 2;
/* 307 */         if (j == this.selectedIndex) {
/* 308 */           g.setColor(this.colors.get(j));
/* 309 */           g.fillPolygon(xPoints, yPoints, 3);
/* 310 */           g.fillRect(xPoints[1], yPoints[1] + 2, this.handleWidth + 1, 2);
/* 311 */           g.setColor(Color.black);
/*     */         } 
/* 313 */         g.drawPolygon(xPoints, yPoints, 3);
/*     */       } 
/* 315 */       g.translate(-this.gradientX, -this.gradientY);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class ColorSlider extends JPanel {
/*     */     Color[] paletteColors;
/*     */     JSlider slider;
/*     */     private ColorPicker colorPicker;
/*     */     
/*     */     public ColorSlider(Color[] paletteColors) {
/* 325 */       this.paletteColors = paletteColors;
/* 326 */       setLayout(new GridBagLayout());
/*     */       
/* 328 */       this.slider = new JSlider(0, 1000, 0);
/* 329 */       this.slider.setPaintTrack(false);
/* 330 */       add(this.slider, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(0, 6, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */       
/* 334 */       this.colorPicker = new ColorPicker();
/* 335 */       add(this.colorPicker, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 6, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */       
/* 339 */       this.slider.addChangeListener(new ChangeListener() {
/*     */             public void stateChanged(ChangeEvent event) {
/* 341 */               GradientPanel.ColorSlider.this.colorPicked();
/*     */             }
/*     */           });
/*     */     }
/*     */     
/*     */     public Dimension getPreferredSize() {
/* 347 */       Dimension size = super.getPreferredSize();
/* 348 */       size.width = 10;
/* 349 */       return size;
/*     */     }
/*     */     
/*     */     public void setPercentage(float percent) {
/* 353 */       this.slider.setValue((int)(1000.0F * percent));
/*     */     }
/*     */     
/*     */     public float getPercentage() {
/* 357 */       return this.slider.getValue() / 1000.0F;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void colorPicked() {}
/*     */     
/*     */     public void setColors(Color[] colors) {
/* 364 */       this.paletteColors = colors;
/* 365 */       repaint();
/*     */     }
/*     */     
/*     */     public class ColorPicker extends JPanel {
/*     */       public ColorPicker() {
/* 370 */         addMouseListener(new MouseAdapter() {
/*     */               public void mouseClicked(MouseEvent event) {
/* 372 */                 GradientPanel.ColorSlider.this.slider.setValue((int)(event.getX() / GradientPanel.ColorSlider.ColorPicker.this.getWidth() * 1000.0F));
/*     */               }
/*     */             });
/*     */       }
/*     */       
/*     */       protected void paintComponent(Graphics graphics) {
/* 378 */         Graphics2D g = (Graphics2D)graphics;
/* 379 */         int width = getWidth() - 1;
/* 380 */         int height = getHeight() - 1;
/* 381 */         for (int i = 0, n = GradientPanel.ColorSlider.this.paletteColors.length - 1; i < n; i++) {
/* 382 */           Color color1 = GradientPanel.ColorSlider.this.paletteColors[i];
/* 383 */           Color color2 = GradientPanel.ColorSlider.this.paletteColors[i + 1];
/* 384 */           float point1 = i / n * width;
/* 385 */           float point2 = (i + 1) / n * width;
/* 386 */           g.setPaint(new GradientPaint(point1, 0.0F, color1, point2, 0.0F, color2, false));
/* 387 */           g.fillRect((int)point1, 0, (int)Math.ceil((point2 - point1)), height);
/*     */         } 
/* 389 */         g.setPaint((Paint)null);
/* 390 */         g.setColor(Color.black);
/* 391 */         g.drawRect(0, 0, width, height);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\GradientPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */