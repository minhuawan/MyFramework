/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import java.awt.EventQueue;
/*     */ import java.awt.FileDialog;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.WindowAdapter;
/*     */ import java.awt.event.WindowEvent;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.WritableRaster;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import javax.imageio.ImageIO;
/*     */ import javax.swing.JFrame;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuBar;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.UIManager;
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
/*     */ public class PreAlpha
/*     */   extends JFrame
/*     */ {
/*     */   BufferedImage image;
/*     */   ImagePanel imagePanel;
/*     */   String lastDir;
/*     */   
/*     */   public PreAlpha() {
/*  45 */     super("Premultiply alpha converter");
/*  46 */     addWindowListener(new WindowAdapter() {
/*     */           public void windowClosed(WindowEvent event) {
/*  48 */             System.exit(0);
/*     */           }
/*     */         });
/*     */     
/*  52 */     initializeComponents();
/*  53 */     pack();
/*  54 */     setLocationRelativeTo(null);
/*  55 */     setDefaultCloseOperation(2);
/*  56 */     setVisible(true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void initializeComponents() {
/*  61 */     JMenuBar menuBar = new JMenuBar();
/*     */ 
/*     */     
/*  64 */     JMenu menu = new JMenu("File");
/*  65 */     menuBar.add(menu);
/*     */ 
/*     */     
/*  68 */     JMenuItem menuItem = new JMenuItem("Open");
/*  69 */     menuItem.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent arg0)
/*     */           {
/*  73 */             PreAlpha.this.open();
/*     */           }
/*     */         });
/*  76 */     menu.add(menuItem);
/*     */     
/*  78 */     menuItem = new JMenuItem("Save");
/*  79 */     menuItem.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent arg0)
/*     */           {
/*  83 */             PreAlpha.this.save();
/*     */           }
/*     */         });
/*  86 */     menu.add(menuItem);
/*  87 */     setJMenuBar(menuBar);
/*     */     
/*  89 */     this.imagePanel = new ImagePanel();
/*  90 */     getContentPane().add(this.imagePanel);
/*     */   }
/*     */   
/*     */   protected void save() {
/*  94 */     FileDialog dialog = new FileDialog(this, "Save Image", 1);
/*  95 */     if (this.lastDir != null) dialog.setDirectory(this.lastDir); 
/*  96 */     dialog.setVisible(true);
/*  97 */     String file = dialog.getFile();
/*  98 */     String dir = dialog.getDirectory();
/*  99 */     if (dir == null || file == null || file.trim().length() == 0)
/* 100 */       return;  this.lastDir = dir;
/*     */     try {
/* 102 */       generatePremultiplyAlpha(new File(dir, file));
/* 103 */       JOptionPane.showMessageDialog(this, "Conversion complete!");
/* 104 */     } catch (Exception ex) {
/* 105 */       JOptionPane.showMessageDialog(this, "Error saving image.");
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void open() {
/* 111 */     FileDialog dialog = new FileDialog(this, "Open Image", 0);
/* 112 */     if (this.lastDir != null) dialog.setDirectory(this.lastDir); 
/* 113 */     dialog.setVisible(true);
/* 114 */     String file = dialog.getFile();
/* 115 */     String dir = dialog.getDirectory();
/* 116 */     if (dir == null || file == null || file.trim().length() == 0)
/* 117 */       return;  this.lastDir = dir;
/*     */     try {
/* 119 */       this.image = ImageIO.read(new File(dir, file));
/* 120 */       this.imagePanel.setImage(this.image);
/* 121 */       this.imagePanel.revalidate();
/* 122 */       this.imagePanel.repaint();
/* 123 */       pack();
/* 124 */     } catch (Exception ex) {
/* 125 */       JOptionPane.showMessageDialog(this, "Error opening image.");
/*     */       return;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void generatePremultiplyAlpha(File out) {
/*     */     try {
/* 132 */       BufferedImage outImage = new BufferedImage(this.image.getWidth(), this.image.getHeight(), 2);
/* 133 */       float[] color = new float[4];
/* 134 */       WritableRaster raster = this.image.getRaster();
/* 135 */       WritableRaster outRaster = outImage.getRaster();
/* 136 */       for (int x = 0, w = this.image.getWidth(); x < w; x++) {
/* 137 */         for (int y = 0, h = this.image.getHeight(); y < h; y++) {
/* 138 */           raster.getPixel(x, y, color);
/* 139 */           float alpha = color[3] / 255.0F;
/* 140 */           for (int i = 0; i < 3; i++)
/* 141 */             color[i] = color[i] * alpha; 
/* 142 */           outRaster.setPixel(x, y, color);
/*     */         } 
/* 144 */       }  ImageIO.write(outImage, "png", out);
/* 145 */     } catch (IOException e) {
/* 146 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 152 */     for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
/* 153 */       if ("Nimbus".equals(info.getName())) {
/*     */         try {
/* 155 */           UIManager.setLookAndFeel(info.getClassName());
/* 156 */         } catch (Throwable throwable) {}
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 161 */     EventQueue.invokeLater(new Runnable() {
/*     */           public void run() {
/* 163 */             new PreAlpha();
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\PreAlpha.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */