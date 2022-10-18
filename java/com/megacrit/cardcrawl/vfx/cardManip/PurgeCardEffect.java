/*     */ package com.megacrit.cardcrawl.vfx.cardManip;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.math.Interpolation;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.megacrit.cardcrawl.cards.AbstractCard;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*     */ import com.megacrit.cardcrawl.helpers.MathHelper;
/*     */ import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
/*     */ import com.megacrit.cardcrawl.vfx.combat.DamageImpactCurvyEffect;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PurgeCardEffect
/*     */   extends AbstractGameEffect
/*     */ {
/*     */   private AbstractCard card;
/*  25 */   private static final float PADDING = 30.0F * Settings.scale;
/*     */   private float scaleY;
/*     */   private Color rarityColor;
/*     */   
/*     */   public PurgeCardEffect(AbstractCard card) {
/*  30 */     this(card, Settings.WIDTH - 96.0F * Settings.scale, Settings.HEIGHT - 32.0F * Settings.scale);
/*     */   }
/*     */   
/*     */   public PurgeCardEffect(AbstractCard card, float x, float y) {
/*  34 */     this.card = card;
/*  35 */     this.startingDuration = 2.0F;
/*  36 */     this.duration = this.startingDuration;
/*  37 */     identifySpawnLocation(x, y);
/*  38 */     card.drawScale = 0.01F;
/*  39 */     card.targetDrawScale = 1.0F;
/*  40 */     CardCrawlGame.sound.play("CARD_BURN");
/*     */     
/*  42 */     initializeVfx();
/*     */   }
/*     */   
/*     */   private void initializeVfx() {
/*  46 */     switch (this.card.rarity) {
/*     */       case BLUE:
/*  48 */         this.rarityColor = new Color(0.2F, 0.8F, 0.8F, 0.01F);
/*     */         break;
/*     */       case COLORLESS:
/*  51 */         this.rarityColor = new Color(0.8F, 0.7F, 0.2F, 0.01F);
/*     */         break;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       default:
/*  62 */         this.rarityColor = new Color(0.6F, 0.6F, 0.6F, 0.01F);
/*     */         break;
/*     */     } 
/*     */     
/*  66 */     switch (this.card.color) {
/*     */       case BLUE:
/*  68 */         this.color = new Color(0.1F, 0.4F, 0.7F, 0.01F);
/*     */         break;
/*     */       case COLORLESS:
/*  71 */         this.color = new Color(0.4F, 0.4F, 0.4F, 0.01F);
/*     */         break;
/*     */       case GREEN:
/*  74 */         this.color = new Color(0.2F, 0.7F, 0.2F, 0.01F);
/*     */         break;
/*     */       case RED:
/*  77 */         this.color = new Color(0.9F, 0.3F, 0.2F, 0.01F);
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/*  82 */         this.color = new Color(0.2F, 0.15F, 0.2F, 0.01F);
/*     */         break;
/*     */     } 
/*  85 */     this.scale = Settings.scale;
/*  86 */     this.scaleY = Settings.scale;
/*     */   }
/*     */   
/*     */   private void identifySpawnLocation(float x, float y) {
/*  90 */     int effectCount = 0;
/*  91 */     for (AbstractGameEffect e : AbstractDungeon.effectList) {
/*  92 */       if (e instanceof PurgeCardEffect) {
/*  93 */         effectCount++;
/*     */       }
/*     */     } 
/*  96 */     for (AbstractGameEffect e : AbstractDungeon.topLevelEffects) {
/*  97 */       if (e instanceof PurgeCardEffect) {
/*  98 */         effectCount++;
/*     */       }
/*     */     } 
/*     */     
/* 102 */     this.card.current_x = x;
/* 103 */     this.card.current_y = y;
/* 104 */     this.card.target_y = Settings.HEIGHT * 0.5F;
/*     */     
/* 106 */     switch (effectCount) {
/*     */       case 0:
/* 108 */         this.card.target_x = Settings.WIDTH * 0.5F;
/*     */         return;
/*     */       case 1:
/* 111 */         this.card.target_x = Settings.WIDTH * 0.5F - PADDING - AbstractCard.IMG_WIDTH;
/*     */         return;
/*     */       case 2:
/* 114 */         this.card.target_x = Settings.WIDTH * 0.5F + PADDING + AbstractCard.IMG_WIDTH;
/*     */         return;
/*     */       case 3:
/* 117 */         this.card.target_x = Settings.WIDTH * 0.5F - (PADDING + AbstractCard.IMG_WIDTH) * 2.0F;
/*     */         return;
/*     */       case 4:
/* 120 */         this.card.target_x = Settings.WIDTH * 0.5F + (PADDING + AbstractCard.IMG_WIDTH) * 2.0F;
/*     */         return;
/*     */     } 
/* 123 */     this.card.target_x = MathUtils.random(Settings.WIDTH * 0.1F, Settings.WIDTH * 0.9F);
/* 124 */     this.card.target_y = MathUtils.random(Settings.HEIGHT * 0.2F, Settings.HEIGHT * 0.8F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update() {
/* 130 */     this.duration -= Gdx.graphics.getDeltaTime();
/* 131 */     if (this.duration < 0.5F) {
/* 132 */       if (!this.card.fadingOut) {
/* 133 */         this.card.fadingOut = true;
/* 134 */         if (!Settings.DISABLE_EFFECTS) {
/* 135 */           int i; for (i = 0; i < 16; i++) {
/* 136 */             AbstractDungeon.topLevelEffectsQueue.add(new DamageImpactCurvyEffect(this.card.current_x, this.card.current_y, this.color, false));
/*     */           }
/*     */           
/* 139 */           for (i = 0; i < 8; i++) {
/* 140 */             AbstractDungeon.effectsQueue.add(new DamageImpactCurvyEffect(this.card.current_x, this.card.current_y, this.rarityColor, false));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 145 */       updateVfx();
/*     */     } 
/*     */     
/* 148 */     this.card.update();
/*     */     
/* 150 */     if (this.duration < 0.0F) {
/* 151 */       this.isDone = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateVfx() {
/* 156 */     this.color.a = MathHelper.fadeLerpSnap(this.color.a, 0.5F);
/* 157 */     this.rarityColor.a = this.color.a;
/* 158 */     this.scale = Interpolation.swingOut.apply(1.6F, 1.0F, this.duration * 2.0F) * Settings.scale;
/* 159 */     this.scaleY = Interpolation.fade.apply(0.005F, 1.0F, this.duration * 2.0F) * Settings.scale;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(SpriteBatch sb) {
/* 164 */     sb.setColor(Color.WHITE);
/* 165 */     this.card.render(sb);
/* 166 */     renderVfx(sb);
/*     */   }
/*     */   
/*     */   private void renderVfx(SpriteBatch sb) {
/* 170 */     sb.setColor(this.color);
/* 171 */     TextureAtlas.AtlasRegion img = ImageMaster.CARD_POWER_BG_SILHOUETTE;
/* 172 */     sb.draw((TextureRegion)img, this.card.current_x + img.offsetX - img.originalWidth / 2.0F, this.card.current_y + img.offsetY - img.originalHeight / 2.0F, img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY, img.packedWidth, img.packedHeight, this.scale * 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 180 */         MathUtils.random(0.95F, 1.05F), this.scaleY * 
/* 181 */         MathUtils.random(0.95F, 1.05F), this.rotation);
/*     */ 
/*     */     
/* 184 */     sb.setBlendFunction(770, 1);
/* 185 */     sb.setColor(this.rarityColor);
/* 186 */     img = ImageMaster.CARD_SUPER_SHADOW;
/* 187 */     sb.draw((TextureRegion)img, this.card.current_x + img.offsetX - img.originalWidth / 2.0F, this.card.current_y + img.offsetY - img.originalHeight / 2.0F, img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY, img.packedWidth, img.packedHeight, this.scale * 0.75F * 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 195 */         MathUtils.random(0.95F, 1.05F), this.scaleY * 0.75F * 
/* 196 */         MathUtils.random(0.95F, 1.05F), this.rotation);
/*     */ 
/*     */     
/* 199 */     sb.draw((TextureRegion)img, this.card.current_x + img.offsetX - img.originalWidth / 2.0F, this.card.current_y + img.offsetY - img.originalHeight / 2.0F, img.originalWidth / 2.0F - img.offsetX, img.originalHeight / 2.0F - img.offsetY, img.packedWidth, img.packedHeight, this.scale * 0.5F * 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 207 */         MathUtils.random(0.95F, 1.05F), this.scaleY * 0.5F * 
/* 208 */         MathUtils.random(0.95F, 1.05F), this.rotation);
/*     */     
/* 210 */     sb.setBlendFunction(770, 771);
/*     */   }
/*     */   
/*     */   public void dispose() {}
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\vfx\cardManip\PurgeCardEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */