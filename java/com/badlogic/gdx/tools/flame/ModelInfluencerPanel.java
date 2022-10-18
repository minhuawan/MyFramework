/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.Model;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ModelInfluencer;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.Insets;
/*    */ 
/*    */ public class ModelInfluencerPanel
/*    */   extends InfluencerPanel<ModelInfluencer>
/*    */   implements TemplatePickerPanel.Listener<Model>, EventManager.Listener
/*    */ {
/*    */   TemplatePickerPanel<Model> pickerPanel;
/*    */   
/*    */   public ModelInfluencerPanel(FlameMain editor, ModelInfluencer influencer, boolean single, String name, String desc) {
/* 15 */     super(editor, influencer, name, desc, true, false);
/* 16 */     this.pickerPanel.setMultipleSelectionAllowed(!single);
/* 17 */     EventManager.get().attach(0, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(ModelInfluencer value) {
/* 22 */     super.setValue(value);
/* 23 */     if (value == null)
/* 24 */       return;  this.pickerPanel.setValue(value.models);
/*    */   }
/*    */   
/*    */   protected void initializeComponents() {
/* 28 */     super.initializeComponents();
/* 29 */     this.pickerPanel = new TemplatePickerPanel<Model>(this.editor, null, this, Model.class, new LoaderButton.ModelLoaderButton(this.editor));
/* 30 */     this.pickerPanel.setIsAlwayShown(true);
/* 31 */     this.contentPanel.add(this.pickerPanel, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 6), 0, 0));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTemplateChecked(Model model, boolean isChecked) {
/* 37 */     this.editor.restart();
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(int aEventType, Object aEventData) {
/* 42 */     if (aEventType == 0) {
/* 43 */       Object[] data = (Object[])aEventData;
/* 44 */       if (data[0] instanceof Model && 
/* 45 */         this.value.models.removeValue(data[0], true)) {
/* 46 */         this.value.models.add(data[1]);
/* 47 */         this.pickerPanel.reloadTemplates();
/* 48 */         this.pickerPanel.setValue(this.value.models);
/* 49 */         this.editor.restart();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\ModelInfluencerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */