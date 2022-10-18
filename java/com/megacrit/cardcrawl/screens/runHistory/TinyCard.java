/*     */ package com.megacrit.cardcrawl.screens.runHistory;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.GameCursor;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.helpers.FontHelper;
/*     */ import com.megacrit.cardcrawl.helpers.Hitbox;
/*     */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*     */ import com.megacrit.cardcrawl.helpers.input.InputHelper;
/*     */ 
/*     */ 
/*     */ public class TinyCard
/*     */ {
/*     */   private static final int MAX_CARD_TEXT_LENGTH = 18;
/*  19 */   private static final Color RED_BACKGROUND_COLOR = new Color(-719117313);
/*  20 */   private static final Color RED_DESCRIPTION_COLOR = new Color(1613902591);
/*     */   
/*  22 */   private static final Color GREEN_BACKGROUND_COLOR = new Color(1792302079);
/*  23 */   private static final Color GREEN_DESCRIPTION_COLOR = new Color(894908927);
/*     */   
/*  25 */   private static final Color BLUE_BACKGROUND_COLOR = new Color(1774256127);
/*  26 */   private static final Color BLUE_DESCRIPTION_COLOR = new Color(1417522687);
/*     */   
/*  28 */   private static final Color PURPLE_BACKGROUND_COLOR = new Color(-1657150465);
/*  29 */   private static final Color PURPLE_DESCRIPTION_COLOR = new Color(1611837695);
/*     */   
/*  31 */   private static final Color COLORLESS_BACKGROUND_COLOR = new Color(2054847231);
/*  32 */   private static final Color COLORLESS_DESCRIPTION_COLOR = new Color(1077952767);
/*     */   
/*  34 */   private static final Color CURSE_BACKGROUND_COLOR = new Color(993541375);
/*  35 */   private static final Color CURSE_DESCRIPTION_COLOR = new Color(454761471);
/*     */   
/*  37 */   private static final Color COMMON_BANNER_COLOR = new Color(-1364283649);
/*  38 */   private static final Color UNCOMMON_BANNER_COLOR = new Color(-1930365185);
/*  39 */   private static final Color RARE_BANNER_COLOR = new Color(-103454721);
/*     */   
/*  41 */   public static final float LINE_SPACING = 36.0F * Settings.scale;
/*     */   public static final float LINE_WIDTH = 9999.0F;
/*  43 */   public static final float TEXT_LEADING_SPACE = scaled(60.0F);
/*     */   
/*     */   public static int desiredColumns;
/*     */   public AbstractCard card;
/*     */   public int count;
/*     */   public Hitbox hb;
/*  49 */   public int col = -1, row = -1;
/*     */   
/*     */   public TinyCard(AbstractCard card, int count) {
/*  52 */     this.card = card;
/*  53 */     this.count = count;
/*  54 */     this.hb = new Hitbox(approximateWidth(), ImageMaster.TINY_CARD_ATTACK.getHeight());
/*     */   }
/*     */   
/*     */   public float approximateWidth() {
/*  58 */     String text = getText();
/*  59 */     return TEXT_LEADING_SPACE + FontHelper.getSmartWidth(FontHelper.charDescFont, text, 9999.0F, LINE_SPACING);
/*     */   }
/*     */   
/*     */   private String getText() {
/*  63 */     String text = (this.count == 1) ? this.card.name : (this.count + " x " + this.card.name);
/*  64 */     if (text.length() > 18) {
/*  65 */       text = text.substring(0, 15) + "...";
/*     */     }
/*  67 */     return text;
/*     */   }
/*     */   
/*     */   public void render(SpriteBatch sb) {
/*  71 */     float x = this.hb.x;
/*  72 */     float y = this.hb.y;
/*  73 */     float width = scaled(46.0F);
/*  74 */     float height = scaled(46.0F);
/*     */     
/*  76 */     renderTinyCardIcon(sb, this.card, x, y, width, height);
/*  77 */     String text = getText();
/*     */     
/*  79 */     float textOffset = -(height / 2.0F) + scaled(7.0F);
/*     */     
/*  81 */     Color basicColor = this.card.upgraded ? Settings.GREEN_TEXT_COLOR : Settings.CREAM_COLOR;
/*  82 */     Color textColor = this.hb.hovered ? Settings.GOLD_COLOR : basicColor;
/*     */     
/*  84 */     if (this.hb.hovered) {
/*  85 */       FontHelper.renderSmartText(sb, FontHelper.charDescFont, text, x + TEXT_LEADING_SPACE + 3.0F * Settings.scale, y + height + textOffset, 9999.0F, LINE_SPACING, textColor);
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  95 */       FontHelper.renderSmartText(sb, FontHelper.charDescFont, text, x + TEXT_LEADING_SPACE, y + height + textOffset, 9999.0F, LINE_SPACING, textColor);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 105 */     this.hb.render(sb);
/*     */   }
/*     */   
/*     */   public boolean updateDidClick() {
/* 109 */     this.hb.update();
/* 110 */     if (this.hb.justHovered) {
/* 111 */       CardCrawlGame.sound.playV("UI_HOVER", 0.75F);
/*     */     }
/*     */     
/* 114 */     if (this.hb.hovered) {
/* 115 */       CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
/* 116 */       if (InputHelper.justClickedLeft) {
/* 117 */         CardCrawlGame.sound.play("UI_CLICK_1");
/* 118 */         this.hb.clickStarted = true;
/*     */       } 
/*     */     } 
/*     */     
/* 122 */     if (this.hb.clicked) {
/* 123 */       this.hb.clicked = false;
/* 124 */       return true;
/*     */     } 
/*     */     
/* 127 */     return false;
/*     */   }
/*     */   
/*     */   private Color getIconBackgroundColor(AbstractCard card) {
/* 131 */     switch (card.color) {
/*     */       case ATTACK:
/* 133 */         return RED_BACKGROUND_COLOR;
/*     */       case POWER:
/* 135 */         return GREEN_BACKGROUND_COLOR;
/*     */       case SKILL:
/* 137 */         return BLUE_BACKGROUND_COLOR;
/*     */       case STATUS:
/* 139 */         return PURPLE_BACKGROUND_COLOR;
/*     */       case CURSE:
/* 141 */         return COLORLESS_BACKGROUND_COLOR;
/*     */       case null:
/* 143 */         return CURSE_BACKGROUND_COLOR;
/*     */     } 
/* 145 */     return new Color(-9849601);
/*     */   }
/*     */ 
/*     */   
/*     */   private Color getIconDescriptionColor(AbstractCard card) {
/* 150 */     switch (card.color) {
/*     */       case ATTACK:
/* 152 */         return RED_DESCRIPTION_COLOR;
/*     */       case POWER:
/* 154 */         return GREEN_DESCRIPTION_COLOR;
/*     */       case SKILL:
/* 156 */         return BLUE_DESCRIPTION_COLOR;
/*     */       case STATUS:
/* 158 */         return PURPLE_DESCRIPTION_COLOR;
/*     */       case CURSE:
/* 160 */         return COLORLESS_DESCRIPTION_COLOR;
/*     */       case null:
/* 162 */         return CURSE_DESCRIPTION_COLOR;
/*     */     } 
/* 164 */     return new Color(-1303806465);
/*     */   }
/*     */ 
/*     */   
/*     */   private Color getIconBannerColor(AbstractCard card) {
/* 169 */     switch (card.rarity) {
/*     */       case ATTACK:
/*     */       case POWER:
/*     */       case SKILL:
/*     */       case STATUS:
/* 174 */         return COMMON_BANNER_COLOR;
/*     */       case CURSE:
/* 176 */         return UNCOMMON_BANNER_COLOR;
/*     */       case null:
/* 178 */         return RARE_BANNER_COLOR;
/*     */     } 
/* 180 */     return COMMON_BANNER_COLOR;
/*     */   }
/*     */   
/*     */   private Texture getIconPortrait(AbstractCard card) {
/* 184 */     switch (card.type) {
/*     */       case ATTACK:
/* 186 */         return ImageMaster.TINY_CARD_ATTACK;
/*     */       case POWER:
/* 188 */         return ImageMaster.TINY_CARD_POWER;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 193 */     return ImageMaster.TINY_CARD_SKILL;
/*     */   }
/*     */ 
/*     */   
/*     */   private void renderTinyCardIcon(SpriteBatch sb, AbstractCard card, float x, float y, float width, float height) {
/* 198 */     sb.setColor(getIconBackgroundColor(card));
/* 199 */     sb.draw(ImageMaster.TINY_CARD_BACKGROUND, x, y, width, height);
/*     */     
/* 201 */     sb.setColor(getIconDescriptionColor(card));
/* 202 */     sb.draw(ImageMaster.TINY_CARD_DESCRIPTION, x, y, width, height);
/*     */     
/* 204 */     sb.setColor(Color.WHITE);
/* 205 */     sb.draw(ImageMaster.TINY_CARD_PORTRAIT_SHADOW, x, y, width, height);
/* 206 */     sb.draw(getIconPortrait(card), x, y, width, height);
/* 207 */     sb.draw(ImageMaster.TINY_CARD_BANNER_SHADOW, x, y, width, height);
/*     */     
/* 209 */     sb.setColor(getIconBannerColor(card));
/* 210 */     sb.draw(ImageMaster.TINY_CARD_BANNER, x, y, width, height);
/*     */   }
/*     */   
/*     */   private static float scaled(float val) {
/* 214 */     return Settings.scale * val;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\screens\runHistory\TinyCard.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */