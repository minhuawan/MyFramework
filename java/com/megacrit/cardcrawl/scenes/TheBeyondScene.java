/*     */ package com.megacrit.cardcrawl.scenes;
/*     */ 
/*     */ public class TheBeyondScene extends AbstractScene {
/*     */   private final TextureAtlas.AtlasRegion bg1;
/*     */   private final TextureAtlas.AtlasRegion bg2;
/*     */   private final TextureAtlas.AtlasRegion floor;
/*     */   private final TextureAtlas.AtlasRegion ceiling;
/*     */   private final TextureAtlas.AtlasRegion fg;
/*     */   private final TextureAtlas.AtlasRegion mg1;
/*     */   private final TextureAtlas.AtlasRegion mg2;
/*     */   private final TextureAtlas.AtlasRegion mg3;
/*     */   private final TextureAtlas.AtlasRegion mg4;
/*     */   private final TextureAtlas.AtlasRegion c1;
/*     */   private final TextureAtlas.AtlasRegion c2;
/*     */   private final TextureAtlas.AtlasRegion c3;
/*     */   private final TextureAtlas.AtlasRegion c4;
/*     */   private final TextureAtlas.AtlasRegion f1;
/*     */   private final TextureAtlas.AtlasRegion f2;
/*     */   private final TextureAtlas.AtlasRegion f3;
/*     */   private final TextureAtlas.AtlasRegion f4;
/*     */   private final TextureAtlas.AtlasRegion f5;
/*     */   private final TextureAtlas.AtlasRegion i1;
/*     */   private final TextureAtlas.AtlasRegion i2;
/*     */   private final TextureAtlas.AtlasRegion i3;
/*     */   private final TextureAtlas.AtlasRegion i4;
/*     */   private final TextureAtlas.AtlasRegion i5;
/*     */   private final TextureAtlas.AtlasRegion s1;
/*     */   private final TextureAtlas.AtlasRegion s2;
/*  29 */   private ColumnConfig columnConfig = ColumnConfig.OPEN; private final TextureAtlas.AtlasRegion s3; private final TextureAtlas.AtlasRegion s4; private final TextureAtlas.AtlasRegion s5; private boolean renderAltBg; private boolean renderM1; private boolean renderM2; private boolean renderM3; private boolean renderM4; private boolean renderF1; private boolean renderF2; private boolean renderF3;
/*     */   private boolean renderF4;
/*  31 */   private Color overlayColor = new Color(1.0F, 1.0F, 1.0F, 0.2F), tmpColor = new Color(1.0F, 1.0F, 1.0F, 1.0F); private boolean renderF5; private boolean renderIce; private boolean renderI1; private boolean renderI2; private boolean renderI3; private boolean renderI4; private boolean renderI5; private boolean renderStalactites; private boolean renderS1; private boolean renderS2; private boolean renderS3; private boolean renderS4; private boolean renderS5;
/*  32 */   private Color whiteColor = new Color(1.0F, 1.0F, 1.0F, 1.0F);
/*     */   
/*     */   public TheBeyondScene() {
/*  35 */     super("beyondScene/scene.atlas");
/*  36 */     this.bg1 = this.atlas.findRegion("mod/bg1");
/*  37 */     this.bg2 = this.atlas.findRegion("mod/bg2");
/*  38 */     this.floor = this.atlas.findRegion("mod/floor");
/*  39 */     this.ceiling = this.atlas.findRegion("mod/ceiling");
/*  40 */     this.fg = this.atlas.findRegion("mod/fg");
/*     */     
/*  42 */     this.mg1 = this.atlas.findRegion("mod/mod1");
/*  43 */     this.mg2 = this.atlas.findRegion("mod/mod2");
/*  44 */     this.mg3 = this.atlas.findRegion("mod/mod3");
/*  45 */     this.mg4 = this.atlas.findRegion("mod/mod4");
/*     */     
/*  47 */     this.c1 = this.atlas.findRegion("mod/c1");
/*  48 */     this.c2 = this.atlas.findRegion("mod/c2");
/*  49 */     this.c3 = this.atlas.findRegion("mod/c3");
/*  50 */     this.c4 = this.atlas.findRegion("mod/c4");
/*     */     
/*  52 */     this.f1 = this.atlas.findRegion("mod/f1");
/*  53 */     this.f2 = this.atlas.findRegion("mod/f2");
/*  54 */     this.f3 = this.atlas.findRegion("mod/f3");
/*  55 */     this.f4 = this.atlas.findRegion("mod/f4");
/*  56 */     this.f5 = this.atlas.findRegion("mod/f5");
/*     */     
/*  58 */     this.i1 = this.atlas.findRegion("mod/i1");
/*  59 */     this.i2 = this.atlas.findRegion("mod/i2");
/*  60 */     this.i3 = this.atlas.findRegion("mod/i3");
/*  61 */     this.i4 = this.atlas.findRegion("mod/i4");
/*  62 */     this.i5 = this.atlas.findRegion("mod/i5");
/*     */     
/*  64 */     this.s1 = this.atlas.findRegion("mod/s1");
/*  65 */     this.s2 = this.atlas.findRegion("mod/s2");
/*  66 */     this.s3 = this.atlas.findRegion("mod/s3");
/*  67 */     this.s4 = this.atlas.findRegion("mod/s4");
/*  68 */     this.s5 = this.atlas.findRegion("mod/s5");
/*     */     
/*  70 */     this.ambianceName = "AMBIANCE_BEYOND";
/*  71 */     fadeInAmbiance();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private enum ColumnConfig
/*     */   {
/*  78 */     OPEN, SMALL_ONLY, SMALL_PLUS_LEFT, SMALL_PLUS_RIGHT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void randomizeScene() {
/*  83 */     this.overlayColor.r = MathUtils.random(0.7F, 0.9F);
/*  84 */     this.overlayColor.g = MathUtils.random(0.7F, 0.9F);
/*  85 */     this.overlayColor.b = MathUtils.random(0.7F, 1.0F);
/*  86 */     this.overlayColor.a = MathUtils.random(0.0F, 0.2F);
/*     */ 
/*     */     
/*  89 */     this.renderAltBg = MathUtils.randomBoolean(0.2F);
/*  90 */     this.renderM1 = false;
/*  91 */     this.renderM2 = false;
/*  92 */     this.renderM3 = false;
/*  93 */     this.renderM4 = false;
/*  94 */     if (!this.renderAltBg && 
/*  95 */       MathUtils.randomBoolean(0.8F)) {
/*  96 */       this.renderM1 = MathUtils.randomBoolean();
/*  97 */       this.renderM2 = MathUtils.randomBoolean();
/*  98 */       this.renderM3 = MathUtils.randomBoolean();
/*  99 */       if (!this.renderM3) {
/* 100 */         this.renderM4 = MathUtils.randomBoolean();
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 106 */     if (MathUtils.randomBoolean(0.6F)) {
/* 107 */       this.columnConfig = ColumnConfig.OPEN;
/*     */     }
/* 109 */     else if (MathUtils.randomBoolean()) {
/* 110 */       this.columnConfig = ColumnConfig.SMALL_ONLY;
/*     */     }
/* 112 */     else if (MathUtils.randomBoolean()) {
/* 113 */       this.columnConfig = ColumnConfig.SMALL_PLUS_LEFT;
/*     */     } else {
/* 115 */       this.columnConfig = ColumnConfig.SMALL_PLUS_RIGHT;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     this.renderF1 = false;
/* 122 */     this.renderF2 = false;
/* 123 */     this.renderF3 = false;
/* 124 */     this.renderF4 = false;
/* 125 */     this.renderF5 = false;
/*     */     
/* 127 */     int floaterCount = 0;
/* 128 */     this.renderF1 = MathUtils.randomBoolean(0.25F);
/* 129 */     if (this.renderF1) {
/* 130 */       floaterCount++;
/*     */     }
/*     */     
/* 133 */     this.renderF2 = MathUtils.randomBoolean(0.25F);
/* 134 */     if (this.renderF2) {
/* 135 */       floaterCount++;
/*     */     }
/*     */     
/* 138 */     if (floaterCount < 2) {
/* 139 */       this.renderF3 = MathUtils.randomBoolean(0.25F);
/* 140 */       if (this.renderF3) {
/* 141 */         floaterCount++;
/*     */       }
/*     */     } 
/*     */     
/* 145 */     if (floaterCount < 2) {
/* 146 */       this.renderF4 = MathUtils.randomBoolean(0.25F);
/* 147 */       if (this.renderF4) {
/* 148 */         floaterCount++;
/*     */       }
/*     */     } 
/*     */     
/* 152 */     if (floaterCount < 2) {
/* 153 */       this.renderF5 = MathUtils.randomBoolean(0.25F);
/*     */     }
/*     */ 
/*     */     
/* 157 */     if (MathUtils.randomBoolean(0.3F) || Settings.DISABLE_EFFECTS) {
/* 158 */       this.renderF1 = false;
/* 159 */       this.renderF2 = false;
/* 160 */       this.renderF3 = false;
/* 161 */       this.renderF4 = false;
/* 162 */       this.renderF5 = false;
/*     */     } 
/*     */ 
/*     */     
/* 166 */     this.renderIce = MathUtils.randomBoolean();
/* 167 */     if (this.renderIce) {
/* 168 */       this.renderIce = true;
/* 169 */       this.renderI1 = MathUtils.randomBoolean();
/* 170 */       this.renderI2 = MathUtils.randomBoolean();
/* 171 */       this.renderI3 = MathUtils.randomBoolean();
/* 172 */       this.renderI4 = MathUtils.randomBoolean();
/* 173 */       this.renderI5 = MathUtils.randomBoolean();
/*     */     } else {
/* 175 */       this.renderI1 = false;
/* 176 */       this.renderI2 = false;
/* 177 */       this.renderI3 = false;
/* 178 */       this.renderI4 = false;
/* 179 */       this.renderI5 = false;
/*     */     } 
/*     */ 
/*     */     
/* 183 */     this.renderStalactites = MathUtils.randomBoolean();
/* 184 */     if (this.renderStalactites) {
/* 185 */       this.renderStalactites = true;
/* 186 */       this.renderS1 = MathUtils.randomBoolean();
/* 187 */       this.renderS2 = MathUtils.randomBoolean();
/* 188 */       this.renderS3 = MathUtils.randomBoolean();
/* 189 */       this.renderS4 = MathUtils.randomBoolean();
/* 190 */       this.renderS5 = MathUtils.randomBoolean();
/*     */     } else {
/* 192 */       this.renderS1 = false;
/* 193 */       this.renderS2 = false;
/* 194 */       this.renderS3 = false;
/* 195 */       this.renderS4 = false;
/* 196 */       this.renderS5 = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void nextRoom(AbstractRoom room) {
/* 206 */     super.nextRoom(room);
/* 207 */     randomizeScene();
/* 208 */     if (room instanceof com.megacrit.cardcrawl.rooms.MonsterRoomBoss) {
/* 209 */       CardCrawlGame.music.silenceBGM();
/*     */     }
/* 211 */     fadeInAmbiance();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderCombatRoomBg(SpriteBatch sb) {
/* 216 */     float prevAlpha = this.overlayColor.a;
/* 217 */     this.overlayColor.a = 1.0F;
/* 218 */     sb.setColor(this.overlayColor);
/* 219 */     this.overlayColor.a = prevAlpha;
/*     */ 
/*     */     
/* 222 */     renderAtlasRegionIf(sb, this.floor, true);
/* 223 */     renderAtlasRegionIf(sb, this.ceiling, true);
/*     */ 
/*     */     
/* 226 */     renderAtlasRegionIf(sb, this.bg1, true);
/* 227 */     renderAtlasRegionIf(sb, this.bg2, this.renderAltBg);
/* 228 */     renderAtlasRegionIf(sb, this.mg2, this.renderM2);
/* 229 */     renderAtlasRegionIf(sb, this.mg1, this.renderM1);
/* 230 */     renderAtlasRegionIf(sb, this.mg3, this.renderM3);
/* 231 */     renderAtlasRegionIf(sb, this.mg4, this.renderM4);
/*     */ 
/*     */     
/* 234 */     switch (this.columnConfig) {
/*     */ 
/*     */       
/*     */       case SMALL_ONLY:
/* 238 */         renderAtlasRegionIf(sb, this.c1, true);
/* 239 */         renderAtlasRegionIf(sb, this.c4, true);
/*     */         break;
/*     */       case SMALL_PLUS_LEFT:
/* 242 */         renderAtlasRegionIf(sb, this.c1, true);
/* 243 */         renderAtlasRegionIf(sb, this.c2, true);
/* 244 */         renderAtlasRegionIf(sb, this.c4, true);
/*     */         break;
/*     */       case SMALL_PLUS_RIGHT:
/* 247 */         renderAtlasRegionIf(sb, this.c1, true);
/* 248 */         renderAtlasRegionIf(sb, this.c3, true);
/* 249 */         renderAtlasRegionIf(sb, this.c4, true);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 254 */     renderAtlasRegionIf(sb, this.s1, this.renderS1);
/* 255 */     renderAtlasRegionIf(sb, this.s2, this.renderS2);
/* 256 */     renderAtlasRegionIf(sb, this.s3, this.renderS3);
/* 257 */     renderAtlasRegionIf(sb, this.s4, this.renderS4);
/* 258 */     renderAtlasRegionIf(sb, this.s5, this.renderS5);
/*     */     
/* 260 */     sb.setColor(this.overlayColor);
/* 261 */     sb.setBlendFunction(770, 1);
/*     */ 
/*     */     
/* 264 */     renderAtlasRegionIf(sb, this.bg1, true);
/* 265 */     renderAtlasRegionIf(sb, this.bg2, this.renderAltBg);
/* 266 */     renderAtlasRegionIf(sb, this.mg2, this.renderM2);
/* 267 */     renderAtlasRegionIf(sb, this.mg1, this.renderM1);
/* 268 */     renderAtlasRegionIf(sb, this.mg3, this.renderM3);
/* 269 */     renderAtlasRegionIf(sb, this.mg4, this.renderM4);
/*     */ 
/*     */     
/* 272 */     switch (this.columnConfig) {
/*     */ 
/*     */       
/*     */       case SMALL_ONLY:
/* 276 */         renderAtlasRegionIf(sb, this.c1, true);
/* 277 */         renderAtlasRegionIf(sb, this.c4, true);
/*     */         break;
/*     */       case SMALL_PLUS_LEFT:
/* 280 */         renderAtlasRegionIf(sb, this.c1, true);
/* 281 */         renderAtlasRegionIf(sb, this.c2, true);
/* 282 */         renderAtlasRegionIf(sb, this.c4, true);
/*     */         break;
/*     */       case SMALL_PLUS_RIGHT:
/* 285 */         renderAtlasRegionIf(sb, this.c1, true);
/* 286 */         renderAtlasRegionIf(sb, this.c3, true);
/* 287 */         renderAtlasRegionIf(sb, this.c4, true);
/*     */         break;
/*     */     } 
/*     */ 
/*     */     
/* 292 */     renderAtlasRegionIf(sb, this.s1, this.renderS1);
/* 293 */     renderAtlasRegionIf(sb, this.s2, this.renderS2);
/* 294 */     renderAtlasRegionIf(sb, this.s3, this.renderS3);
/* 295 */     renderAtlasRegionIf(sb, this.s4, this.renderS4);
/* 296 */     renderAtlasRegionIf(sb, this.s5, this.renderS5);
/* 297 */     sb.setBlendFunction(770, 771);
/*     */     
/* 299 */     this.overlayColor.a = 1.0F;
/* 300 */     sb.setColor(this.overlayColor);
/* 301 */     this.overlayColor.a = prevAlpha;
/*     */ 
/*     */     
/* 304 */     renderAtlasRegionIf(sb, this.i1, this.renderI1);
/* 305 */     renderAtlasRegionIf(sb, this.i2, this.renderI2);
/* 306 */     renderAtlasRegionIf(sb, this.i3, this.renderI3);
/* 307 */     renderAtlasRegionIf(sb, this.i4, this.renderI4);
/* 308 */     renderAtlasRegionIf(sb, this.i5, this.renderI5);
/*     */     
/* 310 */     this.tmpColor.r = (1.0F + this.overlayColor.r) / 2.0F;
/* 311 */     this.tmpColor.g = (1.0F + this.overlayColor.g) / 2.0F;
/* 312 */     this.tmpColor.b = (1.0F + this.overlayColor.b) / 2.0F;
/* 313 */     sb.setColor(this.tmpColor);
/*     */ 
/*     */     
/* 316 */     renderAtlasRegionIf(sb, 
/*     */         
/* 318 */         MathUtils.cosDeg((float)((System.currentTimeMillis() + 180L) / 180L % 360L)) * 40.0F * Settings.xScale, 
/* 319 */         MathUtils.cosDeg((float)((System.currentTimeMillis() + 500L) / 72L % 360L)) * 20.0F * Settings.scale, 
/* 320 */         MathUtils.cosDeg((float)((System.currentTimeMillis() + 180L) / 180L % 360L)), this.f1, this.renderF1);
/*     */ 
/*     */ 
/*     */     
/* 324 */     renderAtlasRegionIf(sb, 
/*     */         
/* 326 */         MathUtils.cosDeg((float)((System.currentTimeMillis() + 91723L) / 72L % 360L)) * 20.0F, 0.0F, 
/*     */         
/* 328 */         (float)(System.currentTimeMillis() / 120L % 360L), this.f2, this.renderF2);
/*     */ 
/*     */ 
/*     */     
/* 332 */     renderAtlasRegionIf(sb, -80.0F * Settings.scale, 
/*     */ 
/*     */         
/* 335 */         MathUtils.cosDeg((float)(System.currentTimeMillis() + 73L)) * 10.0F - 90.0F * Settings.scale, 
/* 336 */         (float)(System.currentTimeMillis() / 1000L % 360L) * 2.0F, this.f3, this.renderF3);
/*     */ 
/*     */ 
/*     */     
/* 340 */     renderAtlasRegionIf(sb, 0.0F, 
/*     */ 
/*     */         
/* 343 */         MathUtils.cosDeg((float)((System.currentTimeMillis() + 4442L) / 20L % 360L)) * 30.0F * Settings.scale, 
/* 344 */         MathUtils.cosDeg((float)((System.currentTimeMillis() + 4442L) / 10L % 360L)) * 20.0F, this.f4, this.renderF4);
/*     */ 
/*     */ 
/*     */     
/* 348 */     renderAtlasRegionIf(sb, 0.0F, MathUtils.cosDeg((float)(System.currentTimeMillis() / 48L % 360L)) * 20.0F, 0.0F, this.f5, this.renderF5);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderCombatRoomFg(SpriteBatch sb) {
/* 353 */     sb.setColor(this.tmpColor);
/* 354 */     renderAtlasRegionIf(sb, this.fg, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderCampfireRoom(SpriteBatch sb) {
/* 359 */     sb.setColor(Color.WHITE);
/* 360 */     renderAtlasRegionIf(sb, this.campfireBg, true);
/* 361 */     sb.setBlendFunction(770, 1);
/* 362 */     this.whiteColor.a = MathUtils.cosDeg((float)(System.currentTimeMillis() / 3L % 360L)) / 10.0F + 0.8F;
/* 363 */     sb.setColor(this.whiteColor);
/* 364 */     renderQuadrupleSize(sb, this.campfireGlow, !CampfireUI.hidden);
/* 365 */     sb.setBlendFunction(770, 771);
/* 366 */     sb.setColor(Color.WHITE);
/* 367 */     renderAtlasRegionIf(sb, this.campfireKindling, true);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\megacrit\cardcrawl\scenes\TheBeyondScene.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */