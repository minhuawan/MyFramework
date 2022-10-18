/*    */ package com.badlogic.gdx.tools.flame;
/*    */ 
/*    */ import com.badlogic.gdx.graphics.g3d.particles.emitters.RegularEmitter;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JCheckBox;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegularEmitterPanel
/*    */   extends EditorPanel<RegularEmitter>
/*    */ {
/*    */   CountPanel countPanel;
/*    */   RangedNumericPanel delayPanel;
/*    */   RangedNumericPanel durationPanel;
/*    */   ScaledNumericPanel emissionPanel;
/*    */   ScaledNumericPanel lifePanel;
/*    */   ScaledNumericPanel lifeOffsetPanel;
/*    */   JCheckBox continuousCheckbox;
/*    */   
/*    */   public RegularEmitterPanel(FlameMain particleEditor3D, RegularEmitter emitter) {
/* 24 */     super(particleEditor3D, "Regular Emitter", "This is a generic emitter used to generate particles regularly.");
/* 25 */     initializeComponents(emitter);
/* 26 */     setValue(null);
/*    */   }
/*    */   
/*    */   private void initializeComponents(RegularEmitter emitter) {
/* 30 */     this.continuousCheckbox = new JCheckBox("Continuous");
/* 31 */     this.continuousCheckbox.setSelected(emitter.isContinuous());
/* 32 */     this.continuousCheckbox.addActionListener(new ActionListener() {
/*    */           public void actionPerformed(ActionEvent event) {
/* 34 */             RegularEmitter emitter = (RegularEmitter)(RegularEmitterPanel.this.editor.getEmitter()).emitter;
/* 35 */             emitter.setContinuous(RegularEmitterPanel.this.continuousCheckbox.isSelected());
/*    */           }
/*    */         });
/* 38 */     this.continuousCheckbox.setHorizontalTextPosition(2);
/*    */     
/* 40 */     int i = 0;
/* 41 */     addContent(i++, 0, this.continuousCheckbox, 17, 0);
/* 42 */     addContent(i++, 0, this.countPanel = new CountPanel(this.editor, "Count", "Min number of particles at all times, max number of particles allowed.", emitter.minParticleCount, emitter.maxParticleCount));
/* 43 */     addContent(i++, 0, this.delayPanel = new RangedNumericPanel(this.editor, emitter.getDelay(), "Delay", "Time from beginning of effect to emission start, in milliseconds.", false));
/* 44 */     addContent(i++, 0, this.durationPanel = new RangedNumericPanel(this.editor, emitter.getDuration(), "Duration", "Time particles will be emitted, in milliseconds."));
/* 45 */     addContent(i++, 0, this.emissionPanel = new ScaledNumericPanel(this.editor, emitter.getEmission(), "Duration", "Emission", "Number of particles emitted per second."));
/* 46 */     addContent(i++, 0, this.lifePanel = new ScaledNumericPanel(this.editor, emitter.getLife(), "Duration", "Life", "Time particles will live, in milliseconds."));
/* 47 */     addContent(i++, 0, this.lifeOffsetPanel = new ScaledNumericPanel(this.editor, emitter.getLifeOffset(), "Duration", "Life Offset", "Particle starting life consumed, in milliseconds.", false));
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\RegularEmitterPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */