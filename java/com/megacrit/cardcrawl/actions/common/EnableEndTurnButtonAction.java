/*    */ package com.megacrit.cardcrawl.actions.common;
/*    */ 
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*    */ 
/*    */ public class EnableEndTurnButtonAction
/*    */   extends AbstractGameAction
/*    */ {
/*    */   public void update() {
/* 10 */     AbstractDungeon.overlayMenu.endTurnButton.enable();
/* 11 */     this.isDone = true;
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\actions\common\EnableEndTurnButtonAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */