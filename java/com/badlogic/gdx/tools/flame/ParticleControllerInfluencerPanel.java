/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleController;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
/*    */ import com.badlogic.gdx.graphics.g3d.particles.influencers.ParticleControllerInfluencer;
/*    */ import com.badlogic.gdx.utils.Array;
/*    */ import java.awt.GridBagConstraints;
/*    */ import java.awt.Insets;
/*    */ 
/*    */ 
/*    */ public class ParticleControllerInfluencerPanel
/*    */   extends InfluencerPanel<ParticleControllerInfluencer>
/*    */   implements TemplatePickerPanel.Listener<ParticleController>, LoaderButton.Listener<ParticleEffect>, EventManager.Listener
/*    */ {
/*    */   TemplatePickerPanel<ParticleController> controllerPicker;
/*    */   
/*    */   public ParticleControllerInfluencerPanel(FlameMain editor, ParticleControllerInfluencer influencer, boolean single, String name, String desc) {
/* 18 */     super(editor, influencer, name, desc, true, false);
/* 19 */     this.controllerPicker.setMultipleSelectionAllowed(!single);
/* 20 */     EventManager.get().attach(0, this);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(ParticleControllerInfluencer value) {
/* 25 */     super.setValue(value);
/* 26 */     if (value == null)
/* 27 */       return;  this.controllerPicker.setValue(value.templates);
/*    */   }
/*    */   
/*    */   protected void initializeComponents() {
/* 31 */     super.initializeComponents();
/* 32 */     this.controllerPicker = new TemplatePickerPanel<ParticleController>(this.editor, null, this, ParticleController.class)
/*    */       {
/*    */         protected String getTemplateName(ParticleController template, int index) {
/* 35 */           return template.name;
/*    */         }
/*    */       };
/* 38 */     reloadControllers();
/* 39 */     this.controllerPicker.setIsAlwayShown(true);
/*    */ 
/*    */     
/* 42 */     this.contentPanel.add(new LoaderButton.ParticleEffectLoaderButton(this.editor, this), new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 6), 0, 0));
/*    */     
/* 44 */     this.contentPanel.add(this.controllerPicker, new GridBagConstraints(0, 1, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 6), 0, 0));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void onTemplateChecked(ParticleController model, boolean isChecked) {
/* 50 */     this.editor.restart();
/*    */   }
/*    */ 
/*    */   
/*    */   public void onResourceLoaded(ParticleEffect resource) {
/* 55 */     reloadControllers();
/*    */   }
/*    */   
/*    */   private void reloadControllers() {
/* 59 */     Array<ParticleEffect> effects = new Array();
/* 60 */     Array<ParticleController> controllers = new Array();
/* 61 */     this.editor.assetManager.getAll(ParticleEffect.class, effects);
/* 62 */     for (ParticleEffect effect : effects) {
/* 63 */       controllers.addAll(effect.getControllers());
/*    */     }
/* 65 */     this.controllerPicker.setLoadedTemplates(controllers);
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(int aEventType, Object aEventData) {
/* 70 */     if (aEventType == 0) {
/* 71 */       Object[] data = (Object[])aEventData;
/* 72 */       if (data[0] instanceof ParticleEffect) {
/* 73 */         ParticleEffect oldEffect = (ParticleEffect)data[0];
/* 74 */         int currentCount = this.value.templates.size;
/* 75 */         this.value.templates.removeAll(oldEffect.getControllers(), true);
/* 76 */         if (this.value.templates.size != currentCount) {
/* 77 */           int diff = currentCount - this.value.templates.size;
/* 78 */           if (diff > 0) {
/* 79 */             ParticleEffect newEffect = (ParticleEffect)data[1];
/* 80 */             Array<ParticleController> newControllers = newEffect.getControllers();
/* 81 */             if (newControllers.size > 0) {
/* 82 */               for (int i = 0, c = Math.min(diff, newControllers.size); i < c; i++) {
/* 83 */                 this.value.templates.add(newControllers.get(i));
/*    */               }
/*    */             }
/*    */           } else {
/* 87 */             this.value.templates.addAll(((ParticleEffect)this.editor.assetManager.get("pre_particle.png")).getControllers());
/*    */           } 
/*    */           
/* 90 */           this.controllerPicker.reloadTemplates();
/* 91 */           this.controllerPicker.setValue(this.value.templates);
/* 92 */           this.editor.restart();
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\ParticleControllerInfluencerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */