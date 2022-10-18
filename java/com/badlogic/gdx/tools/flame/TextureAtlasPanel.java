/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.Texture;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*    */ import com.badlogic.gdx.graphics.glutils.FileTextureData;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import com.badlogic.gdx.utils.ObjectSet;
/*    */ import java.awt.Component;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.GridBagLayout;
/*    */ import java.awt.Insets;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ 
/*    */ public class TextureAtlasPanel
/*    */   extends JPanel
/*    */ {
/*    */   JPanel regionsPanel;
/*    */   TextureAtlas atlas;
/*    */   
/*    */   public TextureAtlasPanel() {
/* 26 */     initializeComponents();
/*    */   }
/*    */   
/*    */   private void initializeComponents() {
/* 30 */     setLayout(new GridBagLayout());
/*    */     
/*    */     JButton backwardButton;
/* 33 */     add(backwardButton = new JButton("<"), new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
/*    */     
/* 35 */     add(this.regionsPanel = new JPanel(), new GridBagConstraints(1, 0, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
/*    */     JButton forwardButton;
/* 37 */     add(forwardButton = new JButton(">"), new GridBagConstraints(2, 0, 1, 1, 1.0D, 1.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
/*    */ 
/*    */     
/* 40 */     this.regionsPanel.setLayout(new CustomCardLayout());
/*    */     
/* 42 */     backwardButton.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent arg0) {
/* 45 */             if (TextureAtlasPanel.this.atlas == null)
/* 46 */               return;  CustomCardLayout layout = (CustomCardLayout)TextureAtlasPanel.this.regionsPanel.getLayout();
/* 47 */             layout.previous(TextureAtlasPanel.this.regionsPanel);
/*    */           }
/*    */         });
/*    */     
/* 51 */     forwardButton.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent arg0) {
/* 54 */             if (TextureAtlasPanel.this.atlas == null)
/* 55 */               return;  CustomCardLayout layout = (CustomCardLayout)TextureAtlasPanel.this.regionsPanel.getLayout();
/* 56 */             layout.next(TextureAtlasPanel.this.regionsPanel);
/*    */           }
/*    */         });
/*    */   }
/*    */   
/*    */   public void setAtlas(TextureAtlas atlas) {
/* 62 */     if (atlas == this.atlas)
/* 63 */       return;  this.regionsPanel.removeAll();
/* 64 */     Array<TextureAtlas.AtlasRegion> atlasRegions = atlas.getRegions();
/* 65 */     CustomCardLayout layout = (CustomCardLayout)this.regionsPanel.getLayout();
/* 66 */     Array<TextureRegion> regions = new Array();
/* 67 */     for (ObjectSet.ObjectSetIterator<Texture> objectSetIterator = atlas.getTextures().iterator(); objectSetIterator.hasNext(); ) { Texture texture = objectSetIterator.next();
/* 68 */       FileTextureData file = (FileTextureData)texture.getTextureData();
/* 69 */       this.regionsPanel.add(new TexturePanel(texture, getRegions(texture, atlasRegions, regions))); }
/*    */     
/* 71 */     layout.first(this.regionsPanel);
/* 72 */     this.atlas = atlas;
/*    */   }
/*    */   
/*    */   protected Array<TextureRegion> getRegions(Texture texture, Array<TextureAtlas.AtlasRegion> atlasRegions, Array<TextureRegion> out) {
/* 76 */     out.clear();
/* 77 */     for (TextureRegion region : atlasRegions) {
/* 78 */       if (region.getTexture() == texture)
/* 79 */         out.add(region); 
/*    */     } 
/* 81 */     return out;
/*    */   }
/*    */   
/*    */   public Array<TextureRegion> getSelectedRegions() {
/* 85 */     CustomCardLayout layout = (CustomCardLayout)this.regionsPanel.getLayout();
/* 86 */     TexturePanel panel = getCurrentRegionPanel();
/* 87 */     return panel.selectedRegions;
/*    */   }
/*    */   
/*    */   public TexturePanel getCurrentRegionPanel() {
/* 91 */     CustomCardLayout layout = (CustomCardLayout)this.regionsPanel.getLayout();
/* 92 */     return layout.<TexturePanel>getCurrentCard(this.regionsPanel);
/*    */   }
/*    */   
/*    */   public void clearSelection() {
/* 96 */     for (Component regionPanel : this.regionsPanel.getComponents())
/* 97 */       ((TexturePanel)regionPanel).clearSelection(); 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\TextureAtlasPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */