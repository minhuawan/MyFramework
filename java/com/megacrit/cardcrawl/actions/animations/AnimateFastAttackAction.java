/*    */ package com.megacrit.cardcrawl.actions.animations;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.Settings;
/*    */ 
/*    */ public class AnimateFastAttackAction extends AbstractGameAction {
/*    */   private boolean called = false;
/*    */   
/*    */   public AnimateFastAttackAction(AbstractCreature owner) {
/* 11 */     setValues(null, owner, 0);
/* 12 */     this.duration = Settings.ACTION_DUR_FAST;
/* 13 */     this.actionType = AbstractGameAction.ActionType.WAIT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 18 */     if (!this.called) {
/* 19 */       this.source.useFastAttackAnimation();
/* 20 */       this.called = true;
/*    */     } 
/* 22 */     tickDuration();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\actions\animations\AnimateFastAttackAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */