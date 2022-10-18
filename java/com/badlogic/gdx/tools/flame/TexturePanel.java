/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.graphics.glutils.FileTextureData;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ 
/*     */ public class TexturePanel
/*     */   extends ImagePanel
/*     */ {
/*  16 */   private Color selectedColor = Color.GREEN; private Color unselectedColor = Color.BLUE; private Color indexBackgroundColor = Color.BLACK; private Color indexColor = Color.WHITE;
/*     */   
/*     */   Array<TextureRegion> selectedRegions;
/*     */   
/*     */   Array<TextureRegion> unselectedRegions;
/*     */   
/*     */   Texture texture;
/*     */   
/*     */   public TexturePanel() {
/*  25 */     this.selectedRegions = new Array();
/*  26 */     this.unselectedRegions = new Array();
/*     */     
/*  28 */     addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent event) {
/*  30 */             float x = event.getX(), y = event.getY();
/*  31 */             for (TextureRegion region : TexturePanel.this.unselectedRegions) {
/*  32 */               if (TexturePanel.this.isInsideRegion(region, x, y)) {
/*  33 */                 TexturePanel.this.select(region);
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/*  38 */             for (TextureRegion region : TexturePanel.this.selectedRegions) {
/*  39 */               if (TexturePanel.this.isInsideRegion(region, x, y)) {
/*  40 */                 TexturePanel.this.unselect(region);
/*     */                 return;
/*     */               } 
/*     */             } 
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected boolean isInsideRegion(TextureRegion region, float x, float y) {
/*  49 */     float rx = region.getRegionX(), ry = region.getRegionY();
/*  50 */     return (rx <= x && x <= rx + region.getRegionWidth() && ry <= y && y <= ry + region
/*  51 */       .getRegionHeight());
/*     */   }
/*     */   
/*     */   public TexturePanel(Texture texture, Array<TextureRegion> regions) {
/*  55 */     this();
/*  56 */     setTexture(texture);
/*  57 */     setRegions(regions);
/*     */   }
/*     */   
/*     */   public void setTexture(Texture texture) {
/*  61 */     if (this.texture == texture)
/*  62 */       return;  this.texture = texture;
/*  63 */     FileTextureData data = (FileTextureData)texture.getTextureData();
/*  64 */     setImage(data.getFileHandle().file().getAbsolutePath());
/*     */   }
/*     */   
/*     */   public Texture getTexture() {
/*  68 */     return this.texture;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  72 */     this.selectedRegions.clear();
/*  73 */     this.unselectedRegions.clear();
/*     */   }
/*     */   
/*     */   public void clearSelection() {
/*  77 */     this.unselectedRegions.addAll(this.selectedRegions);
/*  78 */     this.selectedRegions.clear();
/*  79 */     repaint();
/*     */   }
/*     */   
/*     */   public void setRegions(Array<TextureRegion> regions) {
/*  83 */     this.unselectedRegions.clear();
/*  84 */     this.selectedRegions.clear();
/*  85 */     this.unselectedRegions.addAll(regions);
/*     */   }
/*     */ 
/*     */   
/*     */   private void swap(TextureRegion region, Array<TextureRegion> src, Array<TextureRegion> dst) {
/*  90 */     int index = src.indexOf(region, true);
/*  91 */     if (index > -1) {
/*  92 */       src.removeIndex(index);
/*  93 */       dst.add(region);
/*  94 */       repaint();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void select(TextureRegion region) {
/*  99 */     swap(region, this.unselectedRegions, this.selectedRegions);
/*     */   }
/*     */   
/*     */   public void unselect(TextureRegion region) {
/* 103 */     swap(region, this.selectedRegions, this.unselectedRegions);
/*     */   }
/*     */   
/*     */   public void selectAll() {
/* 107 */     this.selectedRegions.addAll(this.unselectedRegions);
/* 108 */     this.unselectedRegions.clear();
/* 109 */     repaint();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void paintComponent(Graphics g) {
/* 115 */     super.paintComponent(g);
/* 116 */     draw(g, this.unselectedRegions, this.unselectedColor, false);
/* 117 */     draw(g, this.selectedRegions, this.selectedColor, true);
/*     */   }
/*     */   
/*     */   private void draw(Graphics g, Array<TextureRegion> regions, Color color, boolean drawIndex) {
/* 121 */     int i = 0;
/* 122 */     for (TextureRegion region : regions) {
/* 123 */       int x = region.getRegionX(), y = region.getRegionY();
/* 124 */       int h = region.getRegionHeight();
/* 125 */       if (drawIndex) {
/* 126 */         String indexString = "" + i;
/* 127 */         Rectangle bounds = g.getFontMetrics().getStringBounds(indexString, g).getBounds();
/* 128 */         g.setColor(this.indexBackgroundColor);
/* 129 */         g.fillRect(x, y + h - bounds.height, bounds.width, bounds.height);
/* 130 */         g.setColor(this.indexColor);
/* 131 */         g.drawString(indexString, x, y + h);
/* 132 */         i++;
/*     */       } 
/* 134 */       g.setColor(color);
/* 135 */       g.drawRect(x, y, region.getRegionWidth(), h);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\TexturePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */