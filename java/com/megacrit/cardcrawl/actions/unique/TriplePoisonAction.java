/*    */ package com.megacrit.cardcrawl.actions.unique;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*    */ import com.megacrit.cardcrawl.powers.PoisonPower;
/*    */ 
/*    */ public class TriplePoisonAction extends AbstractGameAction {
/*    */   public TriplePoisonAction(AbstractCreature target, AbstractCreature source) {
/* 11 */     this.target = target;
/* 12 */     this.source = source;
/* 13 */     this.actionType = AbstractGameAction.ActionType.DEBUFF;
/* 14 */     this.attackEffect = AbstractGameAction.AttackEffect.FIRE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void update() {
/* 19 */     if (this.target.hasPower("Poison")) {
/* 20 */       addToTop((AbstractGameAction)new ApplyPowerAction(this.target, this.source, (AbstractPower)new PoisonPower(this.target, this.source, 
/*    */ 
/*    */ 
/*    */               
/* 24 */               (this.target.getPower("Poison")).amount * 2), 
/* 25 */             (this.target.getPower("Poison")).amount * 2));
/*    */     }
/* 27 */     this.isDone = true;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\action\\unique\TriplePoisonAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */