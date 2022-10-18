/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import javax.imageio.ImageIO;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ImagePanel
/*    */   extends JPanel
/*    */ {
/*    */   private BufferedImage image;
/*    */   
/*    */   public void setImage(BufferedImage image) {
/* 19 */     this.image = image;
/*    */   }
/*    */   
/*    */   public void setImage(String file) {
/*    */     try {
/* 24 */       this.image = ImageIO.read(new File(file));
/* 25 */     } catch (IOException e) {
/* 26 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void paintComponent(Graphics g) {
/* 32 */     super.paintComponent(g);
/* 33 */     g.drawImage(this.image, 0, 0, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public Dimension getPreferredSize() {
/* 38 */     Dimension dimension = super.getPreferredSize();
/* 39 */     if (this.image != null) {
/* 40 */       dimension.width = this.image.getWidth();
/* 41 */       dimension.height = this.image.getHeight();
/*    */     } 
/* 43 */     return dimension;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\ImagePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */