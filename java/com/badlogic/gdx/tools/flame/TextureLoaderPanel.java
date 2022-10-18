/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.loaders.TextureLoader;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import javax.swing.DefaultComboBoxModel;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JLabel;
/*     */ 
/*     */ 
/*     */ public class TextureLoaderPanel
/*     */   extends EditorPanel
/*     */ {
/*     */   public TextureLoaderPanel(FlameMain editor, String name, String description) {
/*  23 */     super(editor, name, description);
/*  24 */     setValue(null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void initializeComponents() {
/*  29 */     super.initializeComponents();
/*  30 */     JButton atlasButton = new JButton("Open Atlas");
/*  31 */     JButton textureButton = new JButton("Open Texture");
/*  32 */     JButton defaultTextureButton = new JButton("Default Texture");
/*  33 */     final JCheckBox genMipMaps = new JCheckBox("Generate MipMaps");
/*  34 */     final JComboBox minFilterBox = new JComboBox(new DefaultComboBoxModel((Object[])Texture.TextureFilter.values()));
/*  35 */     final JComboBox magFilterBox = new JComboBox(new DefaultComboBoxModel((Object[])Texture.TextureFilter.values()));
/*     */     
/*  37 */     minFilterBox.setSelectedItem(this.editor.getTexture().getMinFilter());
/*  38 */     magFilterBox.setSelectedItem(this.editor.getTexture().getMagFilter());
/*     */     
/*  40 */     ActionListener filterListener = new ActionListener() {
/*     */         public void actionPerformed(ActionEvent event) {
/*  42 */           TextureLoaderPanel.this.editor.getTexture().setFilter((Texture.TextureFilter)minFilterBox.getSelectedItem(), (Texture.TextureFilter)magFilterBox.getSelectedItem());
/*     */         }
/*     */       };
/*     */     
/*  46 */     minFilterBox.addActionListener(filterListener);
/*  47 */     magFilterBox.addActionListener(filterListener);
/*     */     
/*  49 */     atlasButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  52 */             File file = TextureLoaderPanel.this.editor.showFileLoadDialog();
/*  53 */             if (file != null) {
/*  54 */               TextureAtlas atlas = TextureLoaderPanel.this.editor.<TextureAtlas>load(file.getAbsolutePath(), TextureAtlas.class, null, null);
/*  55 */               if (atlas != null) {
/*  56 */                 TextureLoaderPanel.this.editor.setAtlas(atlas);
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/*  62 */     textureButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  65 */             File file = TextureLoaderPanel.this.editor.showFileLoadDialog();
/*  66 */             if (file != null) {
/*  67 */               TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
/*  68 */               params.genMipMaps = genMipMaps.isSelected();
/*  69 */               params.minFilter = (Texture.TextureFilter)minFilterBox.getSelectedItem();
/*  70 */               params.magFilter = (Texture.TextureFilter)magFilterBox.getSelectedItem();
/*  71 */               Texture texture = TextureLoaderPanel.this.editor.<Texture>load(file.getAbsolutePath(), Texture.class, null, (AssetLoaderParameters<Texture>)params);
/*  72 */               if (texture != null) {
/*  73 */                 TextureLoaderPanel.this.editor.setTexture(texture);
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/*  79 */     defaultTextureButton.addActionListener(new ActionListener()
/*     */         {
/*     */           public void actionPerformed(ActionEvent e) {
/*  82 */             TextureLoaderPanel.this.editor.setTexture((Texture)TextureLoaderPanel.this.editor.assetManager.get("pre_particle.png", Texture.class));
/*     */           }
/*     */         });
/*     */     
/*  86 */     this.contentPanel.add(new JLabel("Min. Filter"), new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/*  88 */     this.contentPanel.add(minFilterBox, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/*  90 */     this.contentPanel.add(new JLabel("Mag. Filter"), new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/*  92 */     this.contentPanel.add(magFilterBox, new GridBagConstraints(1, 1, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/*  94 */     this.contentPanel.add(genMipMaps, new GridBagConstraints(0, 2, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/*  96 */     this.contentPanel.add(atlasButton, new GridBagConstraints(0, 3, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/*  98 */     this.contentPanel.add(textureButton, new GridBagConstraints(1, 3, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */     
/* 100 */     this.contentPanel.add(defaultTextureButton, new GridBagConstraints(2, 3, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(6, 0, 0, 0), 0, 0));
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\TextureLoaderPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */