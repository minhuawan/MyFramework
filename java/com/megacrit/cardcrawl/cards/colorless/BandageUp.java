/*    */ package com.megacrit.cardcrawl.cards.colorless;
/*    */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*    */ import com.megacrit.cardcrawl.actions.common.HealAction;
/*    */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*    */ import com.megacrit.cardcrawl.characters.AbstractPlayer;
/*    */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*    */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*    */ import com.megacrit.cardcrawl.localization.CardStrings;
/*    */ import com.megacrit.cardcrawl.monsters.AbstractMonster;
/*    */ 
/*    */ public class BandageUp extends AbstractCard {
/* 12 */   private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("Bandage Up"); public static final String ID = "Bandage Up";
/*    */   
/*    */   public BandageUp() {
/* 15 */     super("Bandage Up", cardStrings.NAME, "colorless/skill/bandage_up", 0, cardStrings.DESCRIPTION, AbstractCard.CardType.SKILL, AbstractCard.CardColor.COLORLESS, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);
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
/* 26 */     this.baseMagicNumber = 4;
/* 27 */     this.magicNumber = this.baseMagicNumber;
/* 28 */     this.exhaust = true;
/* 29 */     this.tags.add(AbstractCard.CardTags.HEALING);
/*    */   }
/*    */ 
/*    */   
/*    */   public void use(AbstractPlayer p, AbstractMonster m) {
/* 34 */     addToBot((AbstractGameAction)new HealAction((AbstractCreature)p, (AbstractCreature)p, this.magicNumber));
/*    */   }
/*    */ 
/*    */   
/*    */   public void upgrade() {
/* 39 */     if (!this.upgraded) {
/* 40 */       upgradeName();
/* 41 */       upgradeMagicNumber(2);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public AbstractCard makeCopy() {
/* 47 */     return new BandageUp();
/*    */   }
/*    */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\cards\colorless\BandageUp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */