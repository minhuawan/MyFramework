/*     */ package com.megacrit.cardcrawl.potions;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.SpriteBatch;
/*     */ import com.badlogic.gdx.math.MathUtils;
/*     */ import com.megacrit.cardcrawl.actions.AbstractGameAction;
/*     */ import com.megacrit.cardcrawl.core.AbstractCreature;
/*     */ import com.megacrit.cardcrawl.core.CardCrawlGame;
/*     */ import com.megacrit.cardcrawl.core.Settings;
/*     */ import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
/*     */ import com.megacrit.cardcrawl.helpers.GameDataStringBuilder;
/*     */ import com.megacrit.cardcrawl.helpers.Hitbox;
/*     */ import com.megacrit.cardcrawl.helpers.ImageMaster;
/*     */ import com.megacrit.cardcrawl.helpers.MathHelper;
/*     */ import com.megacrit.cardcrawl.helpers.PowerTip;
/*     */ import com.megacrit.cardcrawl.helpers.TipHelper;
/*     */ import com.megacrit.cardcrawl.helpers.input.InputHelper;
/*     */ import com.megacrit.cardcrawl.localization.UIStrings;
/*     */ import com.megacrit.cardcrawl.rooms.AbstractRoom;
/*     */ import com.megacrit.cardcrawl.ui.panels.TopPanel;
/*     */ import com.megacrit.cardcrawl.vfx.FlashPotionEffect;
/*     */ import com.megacrit.cardcrawl.vfx.RarePotionParticleEffect;
/*     */ import com.megacrit.cardcrawl.vfx.UncommonPotionParticleEffect;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPotion
/*     */ {
/*  34 */   private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("AbstractPotion");
/*  35 */   public static final String[] TEXT = uiStrings.TEXT; public String ID;
/*     */   public String name;
/*     */   public String description;
/*  38 */   public int slot = -1;
/*  39 */   public ArrayList<PowerTip> tips = new ArrayList<>();
/*     */   
/*     */   private Texture containerImg;
/*     */   private Texture liquidImg;
/*     */   private Texture hybridImg;
/*     */   private Texture spotsImg;
/*  45 */   protected Color labOutlineColor = Settings.HALF_TRANSPARENT_BLACK_COLOR;
/*     */   private Texture outlineImg;
/*     */   public float posX;
/*  48 */   private ArrayList<FlashPotionEffect> effect = new ArrayList<>(); public float posY; private static final int RAW_W = 64;
/*  49 */   public float scale = Settings.scale;
/*     */   public boolean isObtained = false;
/*  51 */   private float sparkleTimer = 0.0F;
/*     */   
/*     */   private static final int FLASH_COUNT = 1;
/*     */   
/*     */   private static final float FLASH_INTERVAL = 0.33F;
/*  56 */   private int flashCount = 0;
/*  57 */   private float flashTimer = 0.0F;
/*     */   
/*     */   public final PotionEffect p_effect;
/*     */   
/*     */   public final PotionColor color;
/*     */   public Color liquidColor;
/*  63 */   public Color hybridColor = null; public Color spotsColor = null;
/*     */   public PotionRarity rarity;
/*     */   public PotionSize size;
/*  66 */   protected int potency = 0;
/*  67 */   public Hitbox hb = new Hitbox(64.0F * Settings.scale, 64.0F * Settings.scale);
/*  68 */   private float angle = 0.0F; protected boolean canUse = false;
/*     */   public boolean discarded = false;
/*     */   public boolean isThrown = false;
/*     */   public boolean targetRequired = false;
/*  72 */   private static final Color PLACEHOLDER_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.75F);
/*     */   
/*     */   public enum PotionSize {
/*  75 */     T, S, M, SPHERE, H, BOTTLE, HEART, SNECKO, FAIRY, GHOST, JAR, BOLT, CARD, MOON, SPIKY, EYE, ANVIL;
/*     */   }
/*     */   
/*     */   public enum PotionRarity {
/*  79 */     PLACEHOLDER, COMMON, UNCOMMON, RARE;
/*     */   }
/*     */   
/*     */   public enum PotionColor {
/*  83 */     POISON, BLUE, FIRE, GREEN, EXPLOSIVE, WEAK, FEAR, STRENGTH, WHITE, FAIRY, ANCIENT, ELIXIR, NONE, ENERGY, SWIFT, FRUIT, SNECKO, SMOKE, STEROID, SKILL, ATTACK, POWER;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public enum PotionEffect
/*     */   {
/*  90 */     NONE, RAINBOW, OSCILLATE;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AbstractPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionEffect effect, Color liquidColor, Color hybridColor, Color spotsColor) {
/* 103 */     this.ID = id;
/* 104 */     this.name = name;
/* 105 */     this.rarity = rarity;
/* 106 */     this.color = null;
/* 107 */     this.liquidColor = liquidColor.cpy();
/* 108 */     this.hybridColor = hybridColor;
/* 109 */     this.spotsColor = spotsColor;
/* 110 */     this.p_effect = effect;
/* 111 */     this.size = size;
/*     */     
/* 113 */     initializeImage();
/* 114 */     initializeData();
/*     */   }
/*     */   
/*     */   public AbstractPotion(String name, String id, PotionRarity rarity, PotionSize size, PotionColor color) {
/* 118 */     this.color = color;
/* 119 */     this.size = size;
/* 120 */     this.ID = id;
/* 121 */     this.name = name;
/* 122 */     this.rarity = rarity;
/* 123 */     this.p_effect = PotionEffect.NONE;
/*     */     
/* 125 */     initializeImage();
/* 126 */     initializeColor();
/* 127 */     initializeData();
/*     */   }
/*     */   
/*     */   private void initializeImage() {
/* 131 */     switch (this.size) {
/*     */       case NONE:
/* 133 */         this.containerImg = ImageMaster.POTION_T_CONTAINER;
/* 134 */         this.liquidImg = ImageMaster.POTION_T_LIQUID;
/* 135 */         this.hybridImg = ImageMaster.POTION_T_HYBRID;
/* 136 */         this.spotsImg = ImageMaster.POTION_T_SPOTS;
/* 137 */         this.outlineImg = ImageMaster.POTION_T_OUTLINE;
/*     */         return;
/*     */       case OSCILLATE:
/* 140 */         this.containerImg = ImageMaster.POTION_S_CONTAINER;
/* 141 */         this.liquidImg = ImageMaster.POTION_S_LIQUID;
/* 142 */         this.hybridImg = ImageMaster.POTION_S_HYBRID;
/* 143 */         this.spotsImg = ImageMaster.POTION_S_SPOTS;
/* 144 */         this.outlineImg = ImageMaster.POTION_S_OUTLINE;
/*     */         return;
/*     */       case RAINBOW:
/* 147 */         this.containerImg = ImageMaster.POTION_M_CONTAINER;
/* 148 */         this.liquidImg = ImageMaster.POTION_M_LIQUID;
/* 149 */         this.hybridImg = ImageMaster.POTION_M_HYBRID;
/* 150 */         this.spotsImg = ImageMaster.POTION_M_SPOTS;
/* 151 */         this.outlineImg = ImageMaster.POTION_M_OUTLINE;
/*     */         return;
/*     */       case null:
/* 154 */         this.containerImg = ImageMaster.POTION_SPHERE_CONTAINER;
/* 155 */         this.liquidImg = ImageMaster.POTION_SPHERE_LIQUID;
/* 156 */         this.hybridImg = ImageMaster.POTION_SPHERE_HYBRID;
/* 157 */         this.spotsImg = ImageMaster.POTION_SPHERE_SPOTS;
/* 158 */         this.outlineImg = ImageMaster.POTION_SPHERE_OUTLINE;
/*     */         return;
/*     */       case null:
/* 161 */         this.containerImg = ImageMaster.POTION_H_CONTAINER;
/* 162 */         this.liquidImg = ImageMaster.POTION_H_LIQUID;
/* 163 */         this.hybridImg = ImageMaster.POTION_H_HYBRID;
/* 164 */         this.spotsImg = ImageMaster.POTION_H_SPOTS;
/* 165 */         this.outlineImg = ImageMaster.POTION_H_OUTLINE;
/*     */         return;
/*     */       case null:
/* 168 */         this.containerImg = ImageMaster.POTION_BOTTLE_CONTAINER;
/* 169 */         this.liquidImg = ImageMaster.POTION_BOTTLE_LIQUID;
/* 170 */         this.hybridImg = ImageMaster.POTION_BOTTLE_HYBRID;
/* 171 */         this.spotsImg = ImageMaster.POTION_BOTTLE_SPOTS;
/* 172 */         this.outlineImg = ImageMaster.POTION_BOTTLE_OUTLINE;
/*     */         return;
/*     */       case null:
/* 175 */         this.containerImg = ImageMaster.POTION_HEART_CONTAINER;
/* 176 */         this.liquidImg = ImageMaster.POTION_HEART_LIQUID;
/* 177 */         this.hybridImg = ImageMaster.POTION_HEART_HYBRID;
/* 178 */         this.spotsImg = ImageMaster.POTION_HEART_SPOTS;
/* 179 */         this.outlineImg = ImageMaster.POTION_HEART_OUTLINE;
/*     */         return;
/*     */       case null:
/* 182 */         this.containerImg = ImageMaster.POTION_SNECKO_CONTAINER;
/* 183 */         this.liquidImg = ImageMaster.POTION_SNECKO_LIQUID;
/* 184 */         this.hybridImg = ImageMaster.POTION_SNECKO_HYBRID;
/* 185 */         this.spotsImg = ImageMaster.POTION_SNECKO_SPOTS;
/* 186 */         this.outlineImg = ImageMaster.POTION_SNECKO_OUTLINE;
/*     */         return;
/*     */       case null:
/* 189 */         this.containerImg = ImageMaster.POTION_FAIRY_CONTAINER;
/* 190 */         this.liquidImg = ImageMaster.POTION_FAIRY_LIQUID;
/* 191 */         this.hybridImg = ImageMaster.POTION_FAIRY_HYBRID;
/* 192 */         this.spotsImg = ImageMaster.POTION_FAIRY_SPOTS;
/* 193 */         this.outlineImg = ImageMaster.POTION_FAIRY_OUTLINE;
/*     */         return;
/*     */       case null:
/* 196 */         this.containerImg = ImageMaster.POTION_GHOST_CONTAINER;
/* 197 */         this.liquidImg = ImageMaster.POTION_GHOST_LIQUID;
/* 198 */         this.hybridImg = ImageMaster.POTION_GHOST_HYBRID;
/* 199 */         this.spotsImg = ImageMaster.POTION_GHOST_SPOTS;
/* 200 */         this.outlineImg = ImageMaster.POTION_GHOST_OUTLINE;
/*     */         return;
/*     */       case null:
/* 203 */         this.containerImg = ImageMaster.POTION_JAR_CONTAINER;
/* 204 */         this.liquidImg = ImageMaster.POTION_JAR_LIQUID;
/* 205 */         this.hybridImg = ImageMaster.POTION_JAR_HYBRID;
/* 206 */         this.spotsImg = ImageMaster.POTION_JAR_SPOTS;
/* 207 */         this.outlineImg = ImageMaster.POTION_JAR_OUTLINE;
/*     */         return;
/*     */       case null:
/* 210 */         this.containerImg = ImageMaster.POTION_BOLT_CONTAINER;
/* 211 */         this.liquidImg = ImageMaster.POTION_BOLT_LIQUID;
/* 212 */         this.hybridImg = ImageMaster.POTION_BOLT_HYBRID;
/* 213 */         this.spotsImg = ImageMaster.POTION_BOLT_SPOTS;
/* 214 */         this.outlineImg = ImageMaster.POTION_BOLT_OUTLINE;
/*     */         return;
/*     */       case null:
/* 217 */         this.containerImg = ImageMaster.POTION_CARD_CONTAINER;
/* 218 */         this.liquidImg = ImageMaster.POTION_CARD_LIQUID;
/* 219 */         this.hybridImg = ImageMaster.POTION_CARD_HYBRID;
/* 220 */         this.spotsImg = ImageMaster.POTION_CARD_SPOTS;
/* 221 */         this.outlineImg = ImageMaster.POTION_CARD_OUTLINE;
/*     */         return;
/*     */       case null:
/* 224 */         this.containerImg = ImageMaster.POTION_MOON_CONTAINER;
/* 225 */         this.liquidImg = ImageMaster.POTION_MOON_LIQUID;
/* 226 */         this.hybridImg = ImageMaster.POTION_MOON_HYBRID;
/* 227 */         this.outlineImg = ImageMaster.POTION_MOON_OUTLINE;
/*     */         return;
/*     */       case null:
/* 230 */         this.containerImg = ImageMaster.POTION_SPIKY_CONTAINER;
/* 231 */         this.liquidImg = ImageMaster.POTION_SPIKY_LIQUID;
/* 232 */         this.hybridImg = ImageMaster.POTION_SPIKY_HYBRID;
/* 233 */         this.outlineImg = ImageMaster.POTION_SPIKY_OUTLINE;
/*     */         return;
/*     */       case null:
/* 236 */         this.containerImg = ImageMaster.POTION_EYE_CONTAINER;
/* 237 */         this.liquidImg = ImageMaster.POTION_EYE_LIQUID;
/* 238 */         this.hybridImg = ImageMaster.POTION_EYE_HYBRID;
/* 239 */         this.outlineImg = ImageMaster.POTION_EYE_OUTLINE;
/*     */         return;
/*     */       case null:
/* 242 */         this.containerImg = ImageMaster.POTION_ANVIL_CONTAINER;
/* 243 */         this.liquidImg = ImageMaster.POTION_ANVIL_LIQUID;
/* 244 */         this.hybridImg = ImageMaster.POTION_ANVIL_HYBRID;
/* 245 */         this.outlineImg = ImageMaster.POTION_ANVIL_OUTLINE;
/*     */         return;
/*     */     } 
/* 248 */     this.containerImg = null;
/* 249 */     this.liquidImg = null;
/* 250 */     this.hybridImg = null;
/* 251 */     this.spotsImg = null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void flash() {
/* 257 */     this.flashCount = 1;
/*     */   }
/*     */   
/*     */   private void initializeColor() {
/* 261 */     if (this.color == null) {
/*     */       return;
/*     */     }
/*     */     
/* 265 */     switch (this.color) {
/*     */       case NONE:
/* 267 */         this.liquidColor = Color.SKY.cpy();
/*     */         return;
/*     */       case OSCILLATE:
/* 270 */         this.liquidColor = Color.WHITE.cpy();
/* 271 */         this.hybridColor = Color.LIGHT_GRAY.cpy();
/*     */         return;
/*     */       case RAINBOW:
/* 274 */         this.liquidColor = Color.CLEAR.cpy();
/* 275 */         this.spotsColor = Color.WHITE.cpy();
/*     */         return;
/*     */       case null:
/* 278 */         this.liquidColor = Color.GOLD.cpy();
/*     */         return;
/*     */       case null:
/* 281 */         this.liquidColor = Color.ORANGE.cpy();
/*     */         return;
/*     */       case null:
/* 284 */         this.liquidColor = Color.RED.cpy();
/* 285 */         this.hybridColor = Color.ORANGE.cpy();
/*     */         return;
/*     */       case null:
/* 288 */         this.liquidColor = Color.CHARTREUSE.cpy();
/*     */         return;
/*     */       case null:
/* 291 */         this.liquidColor = Color.LIME.cpy();
/* 292 */         this.spotsColor = Color.FOREST.cpy();
/*     */         return;
/*     */       case null:
/* 295 */         this.liquidColor = Color.DARK_GRAY.cpy();
/* 296 */         this.spotsColor = Color.CORAL.cpy();
/*     */         return;
/*     */       case null:
/* 299 */         this.liquidColor = Color.DARK_GRAY.cpy();
/* 300 */         this.hybridColor = Color.CORAL.cpy();
/*     */         return;
/*     */       case null:
/* 303 */         this.liquidColor = Color.valueOf("0d429dff");
/* 304 */         this.spotsColor = Color.CYAN.cpy();
/*     */         return;
/*     */       case null:
/* 307 */         this.liquidColor = Color.VIOLET.cpy();
/* 308 */         this.hybridColor = Color.MAROON.cpy();
/*     */         return;
/*     */       case null:
/* 311 */         this.liquidColor = Color.BLACK.cpy();
/* 312 */         this.hybridColor = Color.SCARLET.cpy();
/*     */         return;
/*     */       case null:
/* 315 */         this.liquidColor = Color.GOLD.cpy();
/* 316 */         this.spotsColor = Color.DARK_GRAY.cpy();
/*     */         return;
/*     */       case null:
/* 319 */         this.liquidColor = Color.GOLD.cpy();
/* 320 */         this.hybridColor = Color.CYAN.cpy();
/*     */         return;
/*     */       case null:
/* 323 */         this.liquidColor = Color.ORANGE.cpy();
/* 324 */         this.hybridColor = Color.LIME.cpy();
/*     */         return;
/*     */       case null:
/* 327 */         this.liquidColor = Settings.GREEN_TEXT_COLOR.cpy();
/* 328 */         this.hybridColor = Settings.GOLD_COLOR.cpy();
/*     */         return;
/*     */       case null:
/* 331 */         this.liquidColor = Color.GRAY.cpy();
/* 332 */         this.hybridColor = Color.DARK_GRAY.cpy();
/*     */         return;
/*     */       case null:
/* 335 */         this.liquidColor = Settings.RED_TEXT_COLOR.cpy();
/* 336 */         this.hybridColor = Color.FIREBRICK.cpy();
/*     */         return;
/*     */       case null:
/* 339 */         this.liquidColor = Color.FOREST.cpy();
/* 340 */         this.hybridColor = Color.CHARTREUSE.cpy();
/*     */         return;
/*     */       case null:
/* 343 */         this.liquidColor = Color.NAVY.cpy();
/* 344 */         this.hybridColor = Color.SKY.cpy();
/*     */         return;
/*     */     } 
/* 347 */     this.liquidColor = Color.RED.cpy();
/* 348 */     this.spotsColor = Color.RED.cpy();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void move(float setX, float setY) {
/* 354 */     this.posX = setX;
/* 355 */     this.posY = setY;
/*     */   }
/*     */   
/*     */   public void adjustPosition(int slot) {
/* 359 */     this.posX = TopPanel.potionX + slot * Settings.POTION_W;
/* 360 */     this.posY = Settings.POTION_Y;
/* 361 */     this.hb.move(this.posX, this.posY);
/*     */   }
/*     */   
/*     */   public int getPrice() {
/* 365 */     switch (this.rarity) {
/*     */       case NONE:
/* 367 */         return 50;
/*     */       case OSCILLATE:
/* 369 */         return 75;
/*     */       case RAINBOW:
/* 371 */         return 100;
/*     */     } 
/* 373 */     return 999;
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract void use(AbstractCreature paramAbstractCreature);
/*     */ 
/*     */   
/*     */   public boolean canDiscard() {
/* 381 */     if ((AbstractDungeon.getCurrRoom()).event != null && 
/* 382 */       (AbstractDungeon.getCurrRoom()).event instanceof com.megacrit.cardcrawl.events.shrines.WeMeetAgain) {
/* 383 */       return false;
/*     */     }
/*     */     
/* 386 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initializeData() {}
/*     */   
/*     */   public boolean canUse() {
/* 393 */     if ((AbstractDungeon.getCurrRoom()).event != null && 
/* 394 */       (AbstractDungeon.getCurrRoom()).event instanceof com.megacrit.cardcrawl.events.shrines.WeMeetAgain) {
/* 395 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 400 */     if ((AbstractDungeon.getCurrRoom()).monsters == null || (AbstractDungeon.getCurrRoom()).monsters
/* 401 */       .areMonstersBasicallyDead() || AbstractDungeon.actionManager.turnHasEnded || 
/* 402 */       (AbstractDungeon.getCurrRoom()).phase != AbstractRoom.RoomPhase.COMBAT) {
/* 403 */       return false;
/*     */     }
/* 405 */     return true;
/*     */   }
/*     */   
/*     */   public void update() {
/* 409 */     if (this.isObtained) {
/* 410 */       this.hb.update();
/* 411 */       updateFlash();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateFlash() {
/* 416 */     if (this.flashCount != 0) {
/* 417 */       this.flashTimer -= Gdx.graphics.getDeltaTime();
/* 418 */       if (this.flashTimer < 0.0F) {
/* 419 */         this.flashTimer = 0.33F;
/* 420 */         this.flashCount--;
/* 421 */         this.effect.add(new FlashPotionEffect(this));
/*     */       } 
/*     */     } 
/*     */     
/* 425 */     for (Iterator<FlashPotionEffect> i = this.effect.iterator(); i.hasNext(); ) {
/* 426 */       FlashPotionEffect e = i.next();
/* 427 */       e.update();
/* 428 */       if (e.isDone) {
/* 429 */         i.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setAsObtained(int potionSlot) {
/* 435 */     this.slot = potionSlot;
/* 436 */     this.isObtained = true;
/* 437 */     adjustPosition(potionSlot);
/*     */   }
/*     */   
/*     */   public static void playPotionSound() {
/* 441 */     int tmp = MathUtils.random(2);
/* 442 */     if (tmp == 0) {
/* 443 */       CardCrawlGame.sound.play("POTION_1");
/* 444 */     } else if (tmp == 1) {
/* 445 */       CardCrawlGame.sound.play("POTION_2");
/*     */     } else {
/* 447 */       CardCrawlGame.sound.play("POTION_3");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void renderLightOutline(SpriteBatch sb) {
/* 452 */     if (!(this instanceof PotionSlot)) {
/* 453 */       sb.setColor(Settings.QUARTER_TRANSPARENT_BLACK_COLOR);
/* 454 */       sb.draw(this.outlineImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderOutline(SpriteBatch sb) {
/* 475 */     if (!(this instanceof PotionSlot)) {
/* 476 */       sb.setColor(Settings.HALF_TRANSPARENT_BLACK_COLOR);
/* 477 */       sb.draw(this.outlineImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderOutline(SpriteBatch sb, Color c) {
/* 498 */     if (!(this instanceof PotionSlot)) {
/* 499 */       sb.setColor(c);
/* 500 */       sb.draw(this.outlineImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderShiny(SpriteBatch sb) {
/* 521 */     if (!(this instanceof PotionSlot)) {
/* 522 */       sb.setBlendFunction(770, 1);
/* 523 */       sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
/* 524 */       sb.draw(this.containerImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 541 */       sb.setBlendFunction(770, 771);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void render(SpriteBatch sb) {
/* 551 */     updateFlash();
/* 552 */     updateEffect();
/*     */     
/* 554 */     if (this instanceof PotionSlot) {
/* 555 */       sb.setColor(PLACEHOLDER_COLOR);
/* 556 */       sb.draw(ImageMaster.POTION_PLACEHOLDER, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 574 */       sb.setColor(this.liquidColor);
/* 575 */       sb.draw(this.liquidImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 593 */       if (this.hybridColor != null) {
/* 594 */         sb.setColor(this.hybridColor);
/* 595 */         sb.draw(this.hybridImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 614 */       if (this.spotsColor != null) {
/* 615 */         sb.setColor(this.spotsColor);
/* 616 */         sb.draw(this.spotsImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 635 */       sb.setColor(Color.WHITE);
/* 636 */       sb.draw(this.containerImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 655 */     for (FlashPotionEffect e : this.effect) {
/* 656 */       e.render(sb, this.posX, this.posY);
/*     */     }
/*     */     
/* 659 */     if (this.hb != null) {
/* 660 */       this.hb.render(sb);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateEffect() {
/* 665 */     switch (this.p_effect) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case RAINBOW:
/* 674 */         this.liquidColor.r = (MathUtils.cosDeg((float)(System.currentTimeMillis() / 10L % 360L)) + 1.25F) / 2.3F;
/* 675 */         this.liquidColor.g = (MathUtils.cosDeg((float)((System.currentTimeMillis() + 1000L) / 10L % 360L)) + 1.25F) / 2.3F;
/* 676 */         this.liquidColor.b = (MathUtils.cosDeg((float)((System.currentTimeMillis() + 2000L) / 10L % 360L)) + 1.25F) / 2.3F;
/* 677 */         this.liquidColor.a = 1.0F;
/*     */         break;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void shopRender(SpriteBatch sb) {
/* 685 */     generateSparkles(0.0F, 0.0F, false);
/* 686 */     updateFlash();
/* 687 */     updateEffect();
/*     */     
/* 689 */     if (this.hb.hovered) {
/* 690 */       TipHelper.queuePowerTips(InputHelper.mX + 50.0F * Settings.scale, InputHelper.mY + 50.0F * Settings.scale, this.tips);
/*     */ 
/*     */ 
/*     */       
/* 694 */       this.scale = 1.5F * Settings.scale;
/*     */     } else {
/* 696 */       this.scale = MathHelper.scaleLerpSnap(this.scale, 1.2F * Settings.scale);
/*     */     } 
/*     */     
/* 699 */     renderOutline(sb);
/*     */     
/* 701 */     sb.setColor(this.liquidColor);
/* 702 */     sb.draw(this.liquidImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 720 */     if (this.hybridColor != null) {
/* 721 */       sb.setColor(this.hybridColor);
/* 722 */       sb.draw(this.hybridImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 741 */     if (this.spotsColor != null) {
/* 742 */       sb.setColor(this.spotsColor);
/* 743 */       sb.draw(this.spotsImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 762 */     sb.setColor(Color.WHITE);
/* 763 */     sb.draw(this.containerImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 781 */     for (FlashPotionEffect e : this.effect) {
/* 782 */       e.render(sb, this.posX, this.posY);
/*     */     }
/*     */     
/* 785 */     if (this.hb != null) {
/* 786 */       this.hb.render(sb);
/*     */     }
/*     */   }
/*     */   
/*     */   public void labRender(SpriteBatch sb) {
/* 791 */     updateFlash();
/* 792 */     updateEffect();
/*     */     
/* 794 */     if (this.hb.hovered) {
/* 795 */       TipHelper.queuePowerTips(150.0F * Settings.scale, 800.0F * Settings.scale, this.tips);
/* 796 */       this.scale = 1.5F * Settings.scale;
/*     */     } else {
/* 798 */       this.scale = MathHelper.scaleLerpSnap(this.scale, 1.2F * Settings.scale);
/*     */     } 
/*     */     
/* 801 */     renderOutline(sb, this.labOutlineColor);
/*     */     
/* 803 */     sb.setColor(this.liquidColor);
/* 804 */     sb.draw(this.liquidImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 822 */     if (this.hybridColor != null) {
/* 823 */       sb.setColor(this.hybridColor);
/* 824 */       sb.draw(this.hybridImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 843 */     if (this.spotsColor != null) {
/* 844 */       sb.setColor(this.spotsColor);
/* 845 */       sb.draw(this.spotsImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 864 */     sb.setColor(Color.WHITE);
/* 865 */     sb.draw(this.containerImg, this.posX - 32.0F, this.posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, this.scale, this.scale, this.angle, 0, 0, 64, 64, false, false);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 883 */     for (FlashPotionEffect e : this.effect) {
/* 884 */       e.render(sb, this.posX, this.posY);
/*     */     }
/*     */     
/* 887 */     if (this.hb != null) {
/* 888 */       this.hb.render(sb);
/*     */     }
/*     */   }
/*     */   
/*     */   public void generateSparkles(float x, float y, boolean usePositions) {
/* 893 */     if (Settings.DISABLE_EFFECTS) {
/*     */       return;
/*     */     }
/*     */     
/* 897 */     if (usePositions) {
/* 898 */       switch (this.rarity) {
/*     */         case RAINBOW:
/* 900 */           this.sparkleTimer -= Gdx.graphics.getDeltaTime();
/* 901 */           if (this.sparkleTimer < 0.0F) {
/* 902 */             AbstractDungeon.topLevelEffects.add(new RarePotionParticleEffect(x, y));
/* 903 */             this.sparkleTimer = MathUtils.random(0.35F, 0.5F);
/*     */           } 
/*     */           break;
/*     */         case OSCILLATE:
/* 907 */           this.sparkleTimer -= Gdx.graphics.getDeltaTime();
/* 908 */           if (this.sparkleTimer < 0.0F) {
/* 909 */             AbstractDungeon.topLevelEffects.add(new UncommonPotionParticleEffect(x, y));
/* 910 */             this.sparkleTimer = MathUtils.random(0.25F, 0.3F);
/*     */           } 
/*     */           break;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     } else {
/* 920 */       switch (this.rarity) {
/*     */         case RAINBOW:
/* 922 */           this.sparkleTimer -= Gdx.graphics.getDeltaTime();
/* 923 */           if (this.sparkleTimer < 0.0F) {
/* 924 */             AbstractDungeon.topLevelEffects.add(new RarePotionParticleEffect(this.hb));
/* 925 */             this.sparkleTimer = MathUtils.random(0.35F, 0.5F);
/*     */           } 
/*     */           break;
/*     */         case OSCILLATE:
/* 929 */           this.sparkleTimer -= Gdx.graphics.getDeltaTime();
/* 930 */           if (this.sparkleTimer < 0.0F) {
/* 931 */             AbstractDungeon.topLevelEffects.add(new UncommonPotionParticleEffect(this.hb));
/* 932 */             this.sparkleTimer = MathUtils.random(0.25F, 0.3F);
/*     */           } 
/*     */           break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract int getPotency(int paramInt);
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPotency() {
/* 947 */     int potency = getPotency(AbstractDungeon.ascensionLevel);
/* 948 */     if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic("SacredBark")) {
/* 949 */       potency *= 2;
/*     */     }
/* 951 */     return potency;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onPlayerDeath() {
/* 956 */     return false;
/*     */   }
/*     */   
/*     */   protected void addToBot(AbstractGameAction action) {
/* 960 */     AbstractDungeon.actionManager.addToBottom(action);
/*     */   }
/*     */   
/*     */   protected void addToTop(AbstractGameAction action) {
/* 964 */     addToTop(action);
/*     */   }
/*     */   
/*     */   public static String gameDataUploadHeader() {
/* 968 */     GameDataStringBuilder sb = new GameDataStringBuilder();
/* 969 */     sb.addFieldData("name");
/* 970 */     sb.addFieldData("rarity");
/* 971 */     sb.addFieldData("color");
/* 972 */     sb.addFieldData("text");
/* 973 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getUploadData(String color) {
/* 977 */     GameDataStringBuilder sb = new GameDataStringBuilder();
/* 978 */     sb.addFieldData(this.name);
/* 979 */     sb.addFieldData(this.rarity.toString());
/* 980 */     sb.addFieldData(color);
/*     */     
/* 982 */     String originalValue = String.valueOf(getPotency(0));
/* 983 */     String comboDesc = this.description;
/* 984 */     if (getPotency(0) != getPotency(15)) {
/* 985 */       comboDesc = this.description.replace(originalValue, String.format("%s(%s)", new Object[] { originalValue, Integer.valueOf(getPotency(15)) }));
/*     */     }
/*     */     
/* 988 */     sb.addFieldData(comboDesc);
/* 989 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public abstract AbstractPotion makeCopy();
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\potions\AbstractPotion.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */