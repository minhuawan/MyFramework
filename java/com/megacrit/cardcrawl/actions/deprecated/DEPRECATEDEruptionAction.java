/*    */ package com.megacrit.cardcrawl.actions.deprecated;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ 
/*    */ public class DEPRECATEDEruptionAction
/*    */   extends AbstractGameAction {
/*    */   private int baseDamage;
/*    */   
/*    */   public DEPRECATEDEruptionAction(int baseDamage) {
/* 10 */     this.baseDamage = baseDamage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void update() {
/* 20 */     this.isDone = true;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\actions\deprecated\DEPRECATEDEruptionAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */