/*     */ package com.badlogic.gdx.tools.particleeditor;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*     */ import java.awt.FileDialog;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JLabel;
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
/*     */ class ImagePanel
/*     */   extends EditorPanel
/*     */ {
/*     */   JLabel imageLabel;
/*     */   JLabel widthLabel;
/*     */   JLabel heightLabel;
/*     */   String lastDir;
/*     */   
/*     */   public ImagePanel(final ParticleEditor editor, String name, String description) {
/*  40 */     super((ParticleEmitter.ParticleValue)null, name, description);
/*  41 */     JPanel contentPanel = getContentPanel();
/*     */     
/*  43 */     JButton openButton = new JButton("Open");
/*  44 */     contentPanel.add(openButton, new GridBagConstraints(2, 1, 1, 1, 0.0D, 0.0D, 18, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */     
/*  46 */     openButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/*  48 */             FileDialog dialog = new FileDialog(editor, "Open Image", 0);
/*  49 */             if (ImagePanel.this.lastDir != null) dialog.setDirectory(ImagePanel.this.lastDir); 
/*  50 */             dialog.setVisible(true);
/*  51 */             String file = dialog.getFile();
/*  52 */             String dir = dialog.getDirectory();
/*  53 */             if (dir == null || file == null || file.trim().length() == 0)
/*  54 */               return;  ImagePanel.this.lastDir = dir;
/*     */             try {
/*  56 */               ImageIcon icon = new ImageIcon((new File(dir, file)).toURI().toURL());
/*  57 */               ParticleEmitter emitter = editor.getEmitter();
/*  58 */               editor.setIcon(emitter, icon);
/*  59 */               ImagePanel.this.updateIconInfo(icon);
/*  60 */               emitter.setImagePath((new File(dir, file)).getAbsolutePath());
/*  61 */               emitter.setSprite(null);
/*  62 */             } catch (Exception ex) {
/*  63 */               ex.printStackTrace();
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  69 */     JButton defaultButton = new JButton("Default");
/*  70 */     contentPanel.add(defaultButton, new GridBagConstraints(2, 2, 1, 1, 0.0D, 0.0D, 18, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */     
/*  72 */     defaultButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  75 */             ParticleEmitter emitter = editor.getEmitter();
/*  76 */             emitter.setImagePath("particle.png");
/*  77 */             emitter.setSprite(null);
/*     */             
/*  79 */             editor.setIcon(emitter, null);
/*  80 */             ImagePanel.this.updateIconInfo((ImageIcon)null);
/*     */           }
/*     */         });
/*  83 */     JButton defaultPremultButton = new JButton("Default (Premultiplied Alpha)");
/*  84 */     contentPanel.add(defaultPremultButton, new GridBagConstraints(3, 2, 1, 1, 0.0D, 0.0D, 18, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */     
/*  86 */     defaultPremultButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  89 */             ParticleEmitter emitter = editor.getEmitter();
/*  90 */             emitter.setImagePath("pre_particle.png");
/*  91 */             emitter.setSprite(null);
/*  92 */             editor.setIcon(emitter, null);
/*  93 */             ImagePanel.this.updateIconInfo((ImageIcon)null);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/*  98 */     this.widthLabel = new JLabel();
/*  99 */     contentPanel.add(this.widthLabel, new GridBagConstraints(2, 3, 1, 1, 0.0D, 0.0D, 18, 0, new Insets(0, 0, 6, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 103 */     this.heightLabel = new JLabel();
/* 104 */     contentPanel.add(this.heightLabel, new GridBagConstraints(2, 4, 1, 1, 0.0D, 1.0D, 18, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 108 */     this.imageLabel = new JLabel();
/* 109 */     contentPanel.add(this.imageLabel, new GridBagConstraints(3, 1, 1, 3, 1.0D, 0.0D, 18, 0, new Insets(0, 6, 0, 0), 0, 0));
/*     */ 
/*     */     
/* 112 */     updateIconInfo(editor.getIcon(editor.getEmitter()));
/*     */   }
/*     */   
/*     */   public void updateIconInfo(ImageIcon icon) {
/* 116 */     if (icon != null) {
/* 117 */       this.imageLabel.setIcon(icon);
/* 118 */       this.widthLabel.setText("Width: " + icon.getIconWidth());
/* 119 */       this.heightLabel.setText("Height: " + icon.getIconHeight());
/*     */     } else {
/* 121 */       this.imageLabel.setIcon(null);
/* 122 */       this.widthLabel.setText("");
/* 123 */       this.heightLabel.setText("");
/*     */     } 
/* 125 */     revalidate();
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\ImagePanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */