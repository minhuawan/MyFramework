/*    */ package com.megacrit.cardcrawl.actions.unique;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CannotLoseAction
/*    */   extends AbstractGameAction
/*    */ {
/*    */   public void update() {
/* 13 */     (AbstractDungeon.getCurrRoom()).cannotLose = true;
/* 14 */     this.isDone = true;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\action\\unique\CannotLoseAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */