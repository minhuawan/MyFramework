/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*    */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.influencers.RegionInfluencer;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JDialog;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JScrollPane;
/*    */ 
/*    */ 
/*    */ public class RegionInfluencerPanel
/*    */   extends InfluencerPanel<RegionInfluencer>
/*    */   implements RegionPickerPanel.Listener
/*    */ {
/*    */   JDialog regionSelectDialog;
/*    */   RegionPickerPanel regionPickerPanel;
/*    */   
/*    */   public RegionInfluencerPanel(FlameMain editor, String name, String desc, RegionInfluencer influencer) {
/* 23 */     super(editor, influencer, name, desc);
/* 24 */     setValue(influencer);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void initializeComponents() {
/* 29 */     super.initializeComponents();
/*    */ 
/*    */     
/* 32 */     this.regionSelectDialog = new JDialog(this.editor, "Pick regions", true);
/* 33 */     this.regionPickerPanel = new RegionPickerPanel(this);
/* 34 */     JScrollPane scrollPane = new JScrollPane();
/* 35 */     scrollPane.setViewportView(this.regionPickerPanel);
/* 36 */     this.regionSelectDialog.setContentPane(scrollPane);
/* 37 */     this.regionSelectDialog.setDefaultCloseOperation(1);
/*    */     JButton pickButton;
/* 39 */     addContent(0, 0, pickButton = new JButton("Pick Regions"), false, 17, 0);
/*    */     
/* 41 */     pickButton.addActionListener(new ActionListener()
/*    */         {
/*    */           public void actionPerformed(ActionEvent arg0) {
/* 44 */             if (RegionInfluencerPanel.this.editor.isUsingDefaultTexture()) {
/* 45 */               JOptionPane.showMessageDialog(RegionInfluencerPanel.this.editor, "Load a Texture or an Atlas first.");
/*    */               
/*    */               return;
/*    */             } 
/* 49 */             TextureAtlas atlas = RegionInfluencerPanel.this.editor.getAtlas();
/* 50 */             if (atlas != null) {
/* 51 */               RegionInfluencerPanel.this.regionPickerPanel.setAtlas(atlas);
/*    */             } else {
/* 53 */               RegionInfluencerPanel.this.regionPickerPanel.setTexture(RegionInfluencerPanel.this.editor.getTexture());
/*    */             } 
/* 55 */             RegionInfluencerPanel.this.regionPickerPanel.revalidate();
/* 56 */             RegionInfluencerPanel.this.regionPickerPanel.repaint();
/* 57 */             RegionInfluencerPanel.this.regionSelectDialog.validate();
/* 58 */             RegionInfluencerPanel.this.regionSelectDialog.repaint();
/* 59 */             RegionInfluencerPanel.this.regionSelectDialog.pack();
/* 60 */             RegionInfluencerPanel.this.regionSelectDialog.setVisible(true);
/*    */           }
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRegionsSelected(Array<TextureRegion> regions) {
/* 67 */     this.regionSelectDialog.setVisible(false);
/* 68 */     if (regions.size == 0)
/* 69 */       return;  this.value.clear();
/* 70 */     this.value.add((TextureRegion[])regions.toArray(TextureRegion.class));
/* 71 */     this.editor.setTexture(((TextureRegion)regions.get(0)).getTexture());
/* 72 */     this.editor.restart();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\RegionInfluencerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */