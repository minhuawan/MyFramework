/*    */ package com.megacrit.cardcrawl.cards.green;
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ import com.megacrit.cardcrawl.powers.AbstractPower;
/*    */ import com.megacrit.cardcrawl.powers.AfterImagePower;
/*    */ 
/*    */ public class AfterImage extends AbstractCard {
/* 13 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("After Image"); public static final String ID = "After Image";
/*    */   
/*    */   public AfterImage() {
/* 16 */     super("After Image", cardStrings.NAME, "green/power/after_image", 1, cardStrings.DESCRIPTION, AbstractCard.CardType.POWER, AbstractCard.CardColor.GREEN, AbstractCard.CardRarity.RARE, AbstractCard.CardTarget.SELF);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 30 */     addToBot((AbstractGameAction)new ApplyPowerAction((AbstractCreature)p, (AbstractCreature)p, (AbstractPower)new AfterImagePower((AbstractCreature)p, 1), 1));
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 35 */     if (!this.upgraded) {
/* 36 */       upgradeName();
/* 37 */       this.isInnate = true;
/* 38 */       this.rawDescription = cardStrings.UPGRADE_DESCRIPTION;
/* 39 */       initializeDescription();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 45 */     return new AfterImage();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\green\AfterImage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */