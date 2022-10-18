/*     */ package com.badlogic.gdx.tools.particleeditor;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.awt.event.MouseMotionListener;
/*     */ import java.util.ArrayList;
/*     */ import javax.swing.JPanel;
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
/*     */ 
/*     */ 
/*     */ public class Chart
/*     */   extends JPanel
/*     */ {
/*     */   private static final int POINT_SIZE = 6;
/*     */   private static final int POINT_SIZE_EXPANDED = 10;
/*  37 */   ArrayList<Point> points = new ArrayList<Point>();
/*     */   
/*     */   private int numberHeight;
/*     */   int chartX;
/*     */   int chartY;
/*  42 */   int overIndex = -1; int chartWidth; int chartHeight; int maxX; int maxY;
/*  43 */   int movingIndex = -1;
/*     */   
/*     */   boolean isExpanded;
/*     */   String title;
/*     */   boolean moveAll = false;
/*     */   boolean moveAllProportionally = false;
/*     */   int moveAllPrevY;
/*     */   
/*     */   public Chart(String title) {
/*  52 */     this.title = title;
/*     */     
/*  54 */     setLayout(new GridBagLayout());
/*     */     
/*  56 */     addMouseListener(new MouseAdapter() {
/*     */           public void mousePressed(MouseEvent event) {
/*  58 */             Chart.this.movingIndex = Chart.this.overIndex;
/*  59 */             Chart.this.moveAll = event.isControlDown();
/*  60 */             if (Chart.this.moveAll) {
/*  61 */               Chart.this.moveAllProportionally = event.isShiftDown();
/*  62 */               Chart.this.moveAllPrevY = event.getY();
/*     */             } 
/*     */           }
/*     */           
/*     */           public void mouseReleased(MouseEvent event) {
/*  67 */             Chart.this.movingIndex = -1;
/*  68 */             Chart.this.moveAll = false;
/*     */           }
/*     */           
/*     */           public void mouseClicked(MouseEvent event) {
/*  72 */             if (event.getClickCount() == 2) {
/*  73 */               if (Chart.this.overIndex <= 0 || Chart.this.overIndex >= Chart.this.points.size())
/*  74 */                 return;  Chart.this.points.remove(Chart.this.overIndex);
/*  75 */               Chart.this.pointsChanged();
/*  76 */               Chart.this.repaint();
/*     */               return;
/*     */             } 
/*  79 */             if (Chart.this.movingIndex != -1)
/*  80 */               return;  if (Chart.this.overIndex != -1)
/*  81 */               return;  int mouseX = event.getX();
/*  82 */             int mouseY = event.getY();
/*  83 */             if (mouseX < Chart.this.chartX || mouseX > Chart.this.chartX + Chart.this.chartWidth)
/*  84 */               return;  if (mouseY < Chart.this.chartY || mouseY > Chart.this.chartY + Chart.this.chartHeight)
/*  85 */               return;  Chart.Point newPoint = Chart.this.pixelToPoint(mouseX, mouseY);
/*  86 */             int i = 0;
/*  87 */             Chart.Point lastPoint = null;
/*  88 */             for (Chart.Point point : Chart.this.points) {
/*  89 */               if (point.x > newPoint.x) {
/*  90 */                 if (Math.abs(point.x - newPoint.x) < 0.001F)
/*  91 */                   return;  if (lastPoint != null && Math.abs(lastPoint.x - newPoint.x) < 0.001F)
/*  92 */                   return;  Chart.this.points.add(i, newPoint);
/*  93 */                 Chart.this.overIndex = i;
/*  94 */                 Chart.this.pointsChanged();
/*  95 */                 Chart.this.repaint();
/*     */                 return;
/*     */               } 
/*  98 */               lastPoint = point;
/*  99 */               i++;
/*     */             } 
/* 101 */             Chart.this.overIndex = Chart.this.points.size();
/* 102 */             Chart.this.points.add(newPoint);
/* 103 */             Chart.this.pointsChanged();
/* 104 */             Chart.this.repaint();
/*     */           }
/*     */         });
/* 107 */     addMouseMotionListener(new MouseMotionListener() {
/*     */           public void mouseDragged(MouseEvent event) {
/* 109 */             if (Chart.this.movingIndex == -1 || Chart.this.movingIndex >= Chart.this.points.size())
/* 110 */               return;  if (Chart.this.moveAll) {
/* 111 */               int newY = event.getY();
/* 112 */               float deltaY = (Chart.this.moveAllPrevY - newY) / Chart.this.chartHeight * Chart.this.maxY;
/* 113 */               for (Chart.Point point : Chart.this.points) {
/* 114 */                 point.y = Math.min(Chart.this.maxY, Math.max(0.0F, point.y + (Chart.this.moveAllProportionally ? (deltaY * point.y) : deltaY)));
/*     */               }
/* 116 */               Chart.this.moveAllPrevY = newY;
/*     */             } else {
/* 118 */               float nextX = (Chart.this.movingIndex == Chart.this.points.size() - 1) ? Chart.this.maxX : (((Chart.Point)Chart.this.points.get(Chart.this.movingIndex + 1)).x - 0.001F);
/* 119 */               if (Chart.this.movingIndex == 0) nextX = 0.0F; 
/* 120 */               float prevX = (Chart.this.movingIndex == 0) ? 0.0F : (((Chart.Point)Chart.this.points.get(Chart.this.movingIndex - 1)).x + 0.001F);
/* 121 */               Chart.Point point = Chart.this.points.get(Chart.this.movingIndex);
/* 122 */               point.x = Math.min(nextX, Math.max(prevX, (event.getX() - Chart.this.chartX) / Chart.this.chartWidth * Chart.this.maxX));
/* 123 */               point.y = Math.min(Chart.this.maxY, Math.max(0, Chart.this.chartHeight - event.getY() - Chart.this.chartY) / Chart.this.chartHeight * Chart.this.maxY);
/*     */             } 
/* 125 */             Chart.this.pointsChanged();
/* 126 */             Chart.this.repaint();
/*     */           }
/*     */           
/*     */           public void mouseMoved(MouseEvent event) {
/* 130 */             int mouseX = event.getX();
/* 131 */             int mouseY = event.getY();
/* 132 */             int oldIndex = Chart.this.overIndex;
/* 133 */             Chart.this.overIndex = -1;
/* 134 */             int pointSize = Chart.this.isExpanded ? 10 : 6;
/* 135 */             int i = 0;
/* 136 */             for (Chart.Point point : Chart.this.points) {
/* 137 */               int x = Chart.this.chartX + (int)(Chart.this.chartWidth * point.x / Chart.this.maxX);
/* 138 */               int y = Chart.this.chartY + Chart.this.chartHeight - (int)(Chart.this.chartHeight * point.y / Chart.this.maxY);
/* 139 */               if (Math.abs(x - mouseX) <= pointSize && Math.abs(y - mouseY) <= pointSize) {
/* 140 */                 Chart.this.overIndex = i;
/*     */                 break;
/*     */               } 
/* 143 */               i++;
/*     */             } 
/* 145 */             if (Chart.this.overIndex != oldIndex) Chart.this.repaint(); 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void addPoint(float x, float y) {
/* 151 */     this.points.add(new Point(x, y));
/*     */   }
/*     */ 
/*     */   
/*     */   public void pointsChanged() {}
/*     */   
/*     */   public float[] getValuesX() {
/* 158 */     float[] values = new float[this.points.size()];
/* 159 */     int i = 0;
/* 160 */     for (Point point : this.points)
/* 161 */       values[i++] = point.x; 
/* 162 */     return values;
/*     */   }
/*     */   
/*     */   public float[] getValuesY() {
/* 166 */     float[] values = new float[this.points.size()];
/* 167 */     int i = 0;
/* 168 */     for (Point point : this.points)
/* 169 */       values[i++] = point.y; 
/* 170 */     return values;
/*     */   }
/*     */   
/*     */   public void setValues(float[] x, float[] y) {
/* 174 */     this.points.clear();
/* 175 */     for (int i = 0; i < x.length; i++)
/* 176 */       this.points.add(new Point(x[i], y[i])); 
/*     */   }
/*     */   
/*     */   Point pixelToPoint(float x, float y) {
/* 180 */     Point point = new Point();
/* 181 */     point.x = Math.min(this.maxX, Math.max(0.0F, x - this.chartX) / this.chartWidth * this.maxX);
/* 182 */     point.y = Math.min(this.maxY, Math.max(0.0F, this.chartHeight - y - this.chartY) / this.chartHeight * this.maxY);
/* 183 */     return point;
/*     */   }
/*     */   
/*     */   Point pointToPixel(Point point) {
/* 187 */     Point pixel = new Point();
/* 188 */     pixel.x = (this.chartX + (int)(this.chartWidth * point.x / this.maxX));
/* 189 */     pixel.y = (this.chartY + this.chartHeight - (int)(this.chartHeight * point.y / this.maxY));
/* 190 */     return pixel;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics graphics) {
/*     */     int maxAxisLabelWidth, yAxisWidth;
/* 196 */     super.paintComponent(graphics);
/*     */     
/* 198 */     Graphics2D g = (Graphics2D)graphics;
/* 199 */     FontMetrics metrics = g.getFontMetrics();
/* 200 */     if (this.numberHeight == 0) {
/* 201 */       this
/* 202 */         .numberHeight = (getFont().layoutGlyphVector(g.getFontRenderContext(), new char[] { '0' }, 0, 1, 0).getGlyphPixelBounds(0, g.getFontRenderContext(), 0.0F, 0.0F)).height;
/*     */     }
/*     */     
/* 205 */     int width = getWidth();
/* 206 */     if (!this.isExpanded) width = Math.min(150, width); 
/* 207 */     width = Math.max(100, width);
/* 208 */     int height = getHeight();
/*     */ 
/*     */     
/* 211 */     if (this.isExpanded) {
/* 212 */       maxAxisLabelWidth = metrics.stringWidth("100%");
/* 213 */       yAxisWidth = maxAxisLabelWidth + 8;
/* 214 */       this.chartX = yAxisWidth;
/* 215 */       this.chartY = this.numberHeight / 2 + 1;
/* 216 */       this.chartWidth = width - yAxisWidth - 2;
/* 217 */       this.chartHeight = height - this.chartY - this.numberHeight - 8;
/*     */     } else {
/* 219 */       maxAxisLabelWidth = 0;
/* 220 */       yAxisWidth = 2;
/* 221 */       this.chartX = yAxisWidth;
/* 222 */       this.chartY = 2;
/* 223 */       this.chartWidth = width - yAxisWidth - 2;
/* 224 */       this.chartHeight = height - this.chartY - 3;
/*     */     } 
/*     */     
/* 227 */     g.setColor(Color.white);
/* 228 */     g.fillRect(this.chartX, this.chartY, this.chartWidth, this.chartHeight);
/* 229 */     g.setColor(Color.black);
/* 230 */     g.drawRect(this.chartX, this.chartY, this.chartWidth, this.chartHeight);
/*     */     
/* 232 */     this.maxX = 1;
/*     */     
/* 234 */     int y = height;
/* 235 */     if (this.isExpanded) {
/* 236 */       y -= this.numberHeight;
/*     */     } else {
/* 238 */       y += 5;
/* 239 */     }  int xSplit = (int)Math.min(10.0F, this.chartWidth / maxAxisLabelWidth * 1.5F);
/* 240 */     for (int m = 0; m <= xSplit; m++) {
/* 241 */       float percent = m / xSplit;
/* 242 */       String label = axisLabel(this.maxX * percent);
/* 243 */       int labelWidth = metrics.stringWidth(label);
/* 244 */       int i1 = (int)(yAxisWidth + this.chartWidth * percent);
/* 245 */       if (m != 0 && m != xSplit) {
/* 246 */         g.setColor(Color.lightGray);
/* 247 */         g.drawLine(i1, this.chartY + 1, i1, this.chartY + this.chartHeight);
/* 248 */         g.setColor(Color.black);
/*     */       } 
/* 250 */       g.drawLine(i1, y - 4, i1, y - 8);
/* 251 */       if (this.isExpanded) {
/* 252 */         i1 -= labelWidth / 2;
/* 253 */         if (m == xSplit) i1 = Math.min(i1, width - labelWidth); 
/* 254 */         g.drawString(label, i1, y + this.numberHeight);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 259 */     this.maxY = 1;
/*     */     
/* 261 */     int ySplit = this.isExpanded ? Math.min(10, this.chartHeight / this.numberHeight * 3) : 4;
/* 262 */     for (int i = 0; i <= ySplit; i++) {
/* 263 */       float percent = i / ySplit;
/* 264 */       String label = axisLabel(this.maxY * percent);
/* 265 */       int labelWidth = metrics.stringWidth(label);
/* 266 */       int i1 = (int)((this.chartY + this.chartHeight) - this.chartHeight * percent);
/* 267 */       if (this.isExpanded) g.drawString(label, yAxisWidth - 6 - labelWidth, i1 + this.numberHeight / 2); 
/* 268 */       if (i != 0 && i != ySplit) {
/* 269 */         g.setColor(Color.lightGray);
/* 270 */         g.drawLine(this.chartX, i1, this.chartX + this.chartWidth - 1, i1);
/* 271 */         g.setColor(Color.black);
/*     */       } 
/* 273 */       g.drawLine(yAxisWidth - 4, i1, yAxisWidth, i1);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 278 */     int titleWidth = metrics.stringWidth(this.title);
/* 279 */     int x = yAxisWidth + this.chartWidth / 2 - titleWidth / 2;
/* 280 */     int k = this.chartY + this.chartHeight / 2 - this.numberHeight / 2;
/* 281 */     g.setColor(Color.white);
/* 282 */     g.fillRect(x - 2, k - 2, titleWidth + 4, this.numberHeight + 4);
/* 283 */     g.setColor(Color.lightGray);
/* 284 */     g.drawString(this.title, x, k + this.numberHeight);
/*     */ 
/*     */     
/* 287 */     g.setColor(Color.blue);
/* 288 */     g.setStroke(new BasicStroke(this.isExpanded ? 3.0F : 2.0F));
/* 289 */     int lastX = -1, lastY = -1;
/* 290 */     for (Point point : this.points) {
/* 291 */       Point pixel = pointToPixel(point);
/* 292 */       if (lastX != -1) g.drawLine(lastX, lastY, (int)pixel.x, (int)pixel.y); 
/* 293 */       lastX = (int)pixel.x;
/* 294 */       lastY = (int)pixel.y;
/*     */     } 
/* 296 */     g.drawLine(lastX, lastY, this.chartX + this.chartWidth - 1, lastY);
/* 297 */     for (int j = 0, n = this.points.size(); j < n; j++) {
/* 298 */       Point point = this.points.get(j);
/* 299 */       Point pixel = pointToPixel(point);
/* 300 */       if (this.overIndex == j) {
/* 301 */         g.setColor(Color.red);
/*     */       } else {
/* 303 */         g.setColor(Color.black);
/* 304 */       }  String label = valueLabel(point.y);
/* 305 */       int labelWidth = metrics.stringWidth(label);
/* 306 */       int pointSize = this.isExpanded ? 10 : 6;
/* 307 */       int i1 = (int)pixel.x - pointSize / 2;
/* 308 */       int i2 = (int)pixel.y - pointSize / 2;
/* 309 */       g.fillOval(i1, i2, pointSize, pointSize);
/* 310 */       if (this.isExpanded) {
/* 311 */         g.setColor(Color.black);
/* 312 */         i1 = Math.max(this.chartX + 2, Math.min(this.chartX + this.chartWidth - labelWidth, i1));
/* 313 */         i2 -= 3;
/* 314 */         if (i2 < this.chartY + this.numberHeight + 3) {
/* 315 */           i2 += 27;
/* 316 */         } else if (n > 1) {
/* 317 */           Point comparePoint = (j == n - 1) ? this.points.get(j - 1) : this.points.get(j + 1);
/* 318 */           if (i2 < this.chartY + this.chartHeight - 27 && comparePoint.y > point.y) i2 += 27; 
/*     */         } 
/* 320 */         g.drawString(label, i1, i2);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private String valueLabel(float value) {
/* 326 */     value = (int)(value * 1000.0F) / 10.0F;
/* 327 */     if (value % 1.0F == 0.0F) {
/* 328 */       return String.valueOf((int)value) + '%';
/*     */     }
/* 330 */     return String.valueOf(value) + '%';
/*     */   }
/*     */   
/*     */   private String axisLabel(float value) {
/* 334 */     value = (int)(value * 100.0F);
/* 335 */     if (value % 1.0F == 0.0F) {
/* 336 */       return String.valueOf((int)value) + '%';
/*     */     }
/* 338 */     return String.valueOf(value) + '%';
/*     */   }
/*     */   
/*     */   public static class Point
/*     */   {
/*     */     public float x;
/*     */     public float y;
/*     */     
/*     */     public Point() {}
/*     */     
/*     */     public Point(float x, float y) {
/* 349 */       this.x = x;
/* 350 */       this.y = y;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isExpanded() {
/* 355 */     return this.isExpanded;
/*     */   }
/*     */   
/*     */   public void setExpanded(boolean isExpanded) {
/* 359 */     this.isExpanded = isExpanded;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/* 363 */     this.title = title;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\Chart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */