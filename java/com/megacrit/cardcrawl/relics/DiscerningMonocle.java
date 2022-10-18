/*    */ package com.megacrit.cardcrawl.relics;
/*    */ 
/*    */ import com.megacrit.cardcrawl.rooms.AbstractRoom;
/*    */ 
/*    */ public class DiscerningMonocle
/*    */   extends AbstractRelic
/*    */ {
/*    */   public static final String ID = "Discerning Monocle";
/*    */   public static final float MULTIPLIER = 0.8F;
/*    */   
/*    */   public DiscerningMonocle() {
/* 12 */     super("Discerning Monocle", "monocle.png", AbstractRelic.RelicTier.UNCOMMON, AbstractRelic.LandingSound.CLINK);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getUpdatedDescription() {
/* 17 */     return this.DESCRIPTIONS[0];
/*    */   }
/*    */ 
/*    */   
/*    */   public void onEnterRoom(AbstractRoom room) {
/* 22 */     if (room instanceof com.megacrit.cardcrawl.rooms.ShopRoom) {
/* 23 */       flash();
/* 24 */       this.pulse = true;
/*    */     } else {
/* 26 */       this.pulse = false;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractRelic makeCopy() {
/* 32 */     return new DiscerningMonocle();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\relics\DiscerningMonocle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */