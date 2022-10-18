/*      */ package com.badlogic.gdx.graphics.g2d;
/*      */ 
/*      */ import com.badlogic.gdx.graphics.Texture;
/*      */ import com.badlogic.gdx.math.MathUtils;
/*      */ import com.badlogic.gdx.math.Rectangle;
/*      */ import com.badlogic.gdx.math.collision.BoundingBox;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class ParticleEmitter
/*      */ {
/*      */   private static final int UPDATE_SCALE = 1;
/*      */   private static final int UPDATE_ANGLE = 2;
/*      */   private static final int UPDATE_ROTATION = 4;
/*      */   private static final int UPDATE_VELOCITY = 8;
/*      */   private static final int UPDATE_WIND = 16;
/*      */   private static final int UPDATE_GRAVITY = 32;
/*      */   private static final int UPDATE_TINT = 64;
/*   38 */   private RangedNumericValue delayValue = new RangedNumericValue();
/*   39 */   private ScaledNumericValue lifeOffsetValue = new ScaledNumericValue();
/*   40 */   private RangedNumericValue durationValue = new RangedNumericValue();
/*   41 */   private ScaledNumericValue lifeValue = new ScaledNumericValue();
/*   42 */   private ScaledNumericValue emissionValue = new ScaledNumericValue();
/*   43 */   private ScaledNumericValue scaleValue = new ScaledNumericValue();
/*   44 */   private ScaledNumericValue rotationValue = new ScaledNumericValue();
/*   45 */   private ScaledNumericValue velocityValue = new ScaledNumericValue();
/*   46 */   private ScaledNumericValue angleValue = new ScaledNumericValue();
/*   47 */   private ScaledNumericValue windValue = new ScaledNumericValue();
/*   48 */   private ScaledNumericValue gravityValue = new ScaledNumericValue();
/*   49 */   private ScaledNumericValue transparencyValue = new ScaledNumericValue();
/*   50 */   private GradientColorValue tintValue = new GradientColorValue();
/*   51 */   private RangedNumericValue xOffsetValue = new ScaledNumericValue();
/*   52 */   private RangedNumericValue yOffsetValue = new ScaledNumericValue();
/*   53 */   private ScaledNumericValue spawnWidthValue = new ScaledNumericValue();
/*   54 */   private ScaledNumericValue spawnHeightValue = new ScaledNumericValue();
/*   55 */   private SpawnShapeValue spawnShapeValue = new SpawnShapeValue();
/*      */   private float accumulator;
/*      */   private Sprite sprite;
/*      */   private Particle[] particles;
/*      */   private int minParticleCount;
/*   60 */   private int maxParticleCount = 4; private float x; private float y; private String name; private String imagePath; private int activeCount; private boolean[] active; private boolean firstUpdate;
/*      */   private boolean flipX;
/*      */   private boolean flipY;
/*      */   private int updateFlags;
/*      */   private boolean allowCompletion;
/*      */   private BoundingBox bounds;
/*      */   private int emission;
/*      */   private int emissionDiff;
/*      */   private int emissionDelta;
/*      */   private int lifeOffset;
/*      */   private int lifeOffsetDiff;
/*      */   private int life;
/*      */   private int lifeDiff;
/*      */   private float spawnWidth;
/*      */   private float spawnWidthDiff;
/*      */   private float spawnHeight;
/*      */   private float spawnHeightDiff;
/*   77 */   public float duration = 1.0F; public float durationTimer;
/*      */   private float delay;
/*      */   private float delayTimer;
/*      */   private boolean attached;
/*      */   private boolean continuous;
/*      */   private boolean aligned;
/*      */   private boolean behind;
/*      */   private boolean additive = true;
/*      */   private boolean premultipliedAlpha = false;
/*      */   boolean cleansUpBlendFunction = true;
/*      */   
/*      */   public ParticleEmitter() {
/*   89 */     initialize();
/*      */   }
/*      */   
/*      */   public ParticleEmitter(BufferedReader reader) throws IOException {
/*   93 */     initialize();
/*   94 */     load(reader);
/*      */   }
/*      */   
/*      */   public ParticleEmitter(ParticleEmitter emitter) {
/*   98 */     this.sprite = emitter.sprite;
/*   99 */     this.name = emitter.name;
/*  100 */     this.imagePath = emitter.imagePath;
/*  101 */     setMaxParticleCount(emitter.maxParticleCount);
/*  102 */     this.minParticleCount = emitter.minParticleCount;
/*  103 */     this.delayValue.load(emitter.delayValue);
/*  104 */     this.durationValue.load(emitter.durationValue);
/*  105 */     this.emissionValue.load(emitter.emissionValue);
/*  106 */     this.lifeValue.load(emitter.lifeValue);
/*  107 */     this.lifeOffsetValue.load(emitter.lifeOffsetValue);
/*  108 */     this.scaleValue.load(emitter.scaleValue);
/*  109 */     this.rotationValue.load(emitter.rotationValue);
/*  110 */     this.velocityValue.load(emitter.velocityValue);
/*  111 */     this.angleValue.load(emitter.angleValue);
/*  112 */     this.windValue.load(emitter.windValue);
/*  113 */     this.gravityValue.load(emitter.gravityValue);
/*  114 */     this.transparencyValue.load(emitter.transparencyValue);
/*  115 */     this.tintValue.load(emitter.tintValue);
/*  116 */     this.xOffsetValue.load(emitter.xOffsetValue);
/*  117 */     this.yOffsetValue.load(emitter.yOffsetValue);
/*  118 */     this.spawnWidthValue.load(emitter.spawnWidthValue);
/*  119 */     this.spawnHeightValue.load(emitter.spawnHeightValue);
/*  120 */     this.spawnShapeValue.load(emitter.spawnShapeValue);
/*  121 */     this.attached = emitter.attached;
/*  122 */     this.continuous = emitter.continuous;
/*  123 */     this.aligned = emitter.aligned;
/*  124 */     this.behind = emitter.behind;
/*  125 */     this.additive = emitter.additive;
/*  126 */     this.premultipliedAlpha = emitter.premultipliedAlpha;
/*  127 */     this.cleansUpBlendFunction = emitter.cleansUpBlendFunction;
/*      */   }
/*      */   
/*      */   private void initialize() {
/*  131 */     this.durationValue.setAlwaysActive(true);
/*  132 */     this.emissionValue.setAlwaysActive(true);
/*  133 */     this.lifeValue.setAlwaysActive(true);
/*  134 */     this.scaleValue.setAlwaysActive(true);
/*  135 */     this.transparencyValue.setAlwaysActive(true);
/*  136 */     this.spawnShapeValue.setAlwaysActive(true);
/*  137 */     this.spawnWidthValue.setAlwaysActive(true);
/*  138 */     this.spawnHeightValue.setAlwaysActive(true);
/*      */   }
/*      */   
/*      */   public void setMaxParticleCount(int maxParticleCount) {
/*  142 */     this.maxParticleCount = maxParticleCount;
/*  143 */     this.active = new boolean[maxParticleCount];
/*  144 */     this.activeCount = 0;
/*  145 */     this.particles = new Particle[maxParticleCount];
/*      */   }
/*      */   
/*      */   public void addParticle() {
/*  149 */     int activeCount = this.activeCount;
/*  150 */     if (activeCount == this.maxParticleCount)
/*  151 */       return;  boolean[] active = this.active;
/*  152 */     for (int i = 0, n = active.length; i < n; i++) {
/*  153 */       if (!active[i]) {
/*  154 */         activateParticle(i);
/*  155 */         active[i] = true;
/*  156 */         this.activeCount = activeCount + 1;
/*      */         break;
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void addParticles(int count) {
/*  163 */     count = Math.min(count, this.maxParticleCount - this.activeCount);
/*  164 */     if (count == 0)
/*  165 */       return;  boolean[] active = this.active;
/*  166 */     int index = 0, n = active.length;
/*      */     
/*  168 */     for (int i = 0; i < count;) {
/*  169 */       for (; index < n; i++) {
/*  170 */         if (!active[index]) {
/*  171 */           activateParticle(index);
/*  172 */           active[index++] = true;
/*      */         } else {
/*      */           index++; continue;
/*      */         } 
/*      */       } 
/*      */     } 
/*  178 */     this.activeCount += count;
/*      */   }
/*      */   
/*      */   public void update(float delta) {
/*  182 */     this.accumulator += delta * 1000.0F;
/*  183 */     if (this.accumulator < 1.0F)
/*  184 */       return;  int deltaMillis = (int)this.accumulator;
/*  185 */     this.accumulator -= deltaMillis;
/*      */     
/*  187 */     if (this.delayTimer < this.delay) {
/*  188 */       this.delayTimer += deltaMillis;
/*      */     } else {
/*  190 */       boolean done = false;
/*  191 */       if (this.firstUpdate) {
/*  192 */         this.firstUpdate = false;
/*  193 */         addParticle();
/*      */       } 
/*      */       
/*  196 */       if (this.durationTimer < this.duration) {
/*  197 */         this.durationTimer += deltaMillis;
/*      */       }
/*  199 */       else if (!this.continuous || this.allowCompletion) {
/*  200 */         done = true;
/*      */       } else {
/*  202 */         restart();
/*      */       } 
/*      */       
/*  205 */       if (!done) {
/*  206 */         this.emissionDelta += deltaMillis;
/*  207 */         float emissionTime = this.emission + this.emissionDiff * this.emissionValue.getScale(this.durationTimer / this.duration);
/*  208 */         if (emissionTime > 0.0F) {
/*  209 */           emissionTime = 1000.0F / emissionTime;
/*  210 */           if (this.emissionDelta >= emissionTime) {
/*  211 */             int emitCount = (int)(this.emissionDelta / emissionTime);
/*  212 */             emitCount = Math.min(emitCount, this.maxParticleCount - this.activeCount);
/*  213 */             this.emissionDelta = (int)(this.emissionDelta - emitCount * emissionTime);
/*  214 */             this.emissionDelta = (int)(this.emissionDelta % emissionTime);
/*  215 */             addParticles(emitCount);
/*      */           } 
/*      */         } 
/*  218 */         if (this.activeCount < this.minParticleCount) addParticles(this.minParticleCount - this.activeCount);
/*      */       
/*      */       } 
/*      */     } 
/*  222 */     boolean[] active = this.active;
/*  223 */     int activeCount = this.activeCount;
/*  224 */     Particle[] particles = this.particles;
/*  225 */     for (int i = 0, n = active.length; i < n; i++) {
/*  226 */       if (active[i] && !updateParticle(particles[i], delta, deltaMillis)) {
/*  227 */         active[i] = false;
/*  228 */         activeCount--;
/*      */       } 
/*      */     } 
/*  231 */     this.activeCount = activeCount;
/*      */   }
/*      */   
/*      */   public void draw(Batch batch) {
/*  235 */     if (this.premultipliedAlpha) {
/*  236 */       batch.setBlendFunction(1, 771);
/*  237 */     } else if (this.additive) {
/*  238 */       batch.setBlendFunction(770, 1);
/*      */     } else {
/*  240 */       batch.setBlendFunction(770, 771);
/*      */     } 
/*  242 */     Particle[] particles = this.particles;
/*  243 */     boolean[] active = this.active;
/*      */     
/*  245 */     for (int i = 0, n = active.length; i < n; i++) {
/*  246 */       if (active[i]) particles[i].draw(batch);
/*      */     
/*      */     } 
/*  249 */     if (this.cleansUpBlendFunction && (this.additive || this.premultipliedAlpha)) {
/*  250 */       batch.setBlendFunction(770, 771);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void draw(Batch batch, float delta) {
/*  257 */     this.accumulator += delta * 1000.0F;
/*  258 */     if (this.accumulator < 1.0F) {
/*  259 */       draw(batch);
/*      */       return;
/*      */     } 
/*  262 */     int deltaMillis = (int)this.accumulator;
/*  263 */     this.accumulator -= deltaMillis;
/*      */     
/*  265 */     if (this.premultipliedAlpha) {
/*  266 */       batch.setBlendFunction(1, 771);
/*  267 */     } else if (this.additive) {
/*  268 */       batch.setBlendFunction(770, 1);
/*      */     } else {
/*  270 */       batch.setBlendFunction(770, 771);
/*      */     } 
/*      */     
/*  273 */     Particle[] particles = this.particles;
/*  274 */     boolean[] active = this.active;
/*  275 */     int activeCount = this.activeCount;
/*  276 */     for (int i = 0, n = active.length; i < n; i++) {
/*  277 */       if (active[i]) {
/*  278 */         Particle particle = particles[i];
/*  279 */         if (updateParticle(particle, delta, deltaMillis)) {
/*  280 */           particle.draw(batch);
/*      */         } else {
/*  282 */           active[i] = false;
/*  283 */           activeCount--;
/*      */         } 
/*      */       } 
/*      */     } 
/*  287 */     this.activeCount = activeCount;
/*      */     
/*  289 */     if (this.cleansUpBlendFunction && (this.additive || this.premultipliedAlpha)) {
/*  290 */       batch.setBlendFunction(770, 771);
/*      */     }
/*  292 */     if (this.delayTimer < this.delay) {
/*  293 */       this.delayTimer += deltaMillis;
/*      */       
/*      */       return;
/*      */     } 
/*  297 */     if (this.firstUpdate) {
/*  298 */       this.firstUpdate = false;
/*  299 */       addParticle();
/*      */     } 
/*      */     
/*  302 */     if (this.durationTimer < this.duration) {
/*  303 */       this.durationTimer += deltaMillis;
/*      */     } else {
/*  305 */       if (!this.continuous || this.allowCompletion)
/*  306 */         return;  restart();
/*      */     } 
/*      */     
/*  309 */     this.emissionDelta += deltaMillis;
/*  310 */     float emissionTime = this.emission + this.emissionDiff * this.emissionValue.getScale(this.durationTimer / this.duration);
/*  311 */     if (emissionTime > 0.0F) {
/*  312 */       emissionTime = 1000.0F / emissionTime;
/*  313 */       if (this.emissionDelta >= emissionTime) {
/*  314 */         int emitCount = (int)(this.emissionDelta / emissionTime);
/*  315 */         emitCount = Math.min(emitCount, this.maxParticleCount - activeCount);
/*  316 */         this.emissionDelta = (int)(this.emissionDelta - emitCount * emissionTime);
/*  317 */         this.emissionDelta = (int)(this.emissionDelta % emissionTime);
/*  318 */         addParticles(emitCount);
/*      */       } 
/*      */     } 
/*  321 */     if (activeCount < this.minParticleCount) addParticles(this.minParticleCount - activeCount); 
/*      */   }
/*      */   
/*      */   public void start() {
/*  325 */     this.firstUpdate = true;
/*  326 */     this.allowCompletion = false;
/*  327 */     restart();
/*      */   }
/*      */   
/*      */   public void reset() {
/*  331 */     this.emissionDelta = 0;
/*  332 */     this.durationTimer = this.duration;
/*  333 */     boolean[] active = this.active;
/*  334 */     for (int i = 0, n = active.length; i < n; i++)
/*  335 */       active[i] = false; 
/*  336 */     this.activeCount = 0;
/*  337 */     start();
/*      */   }
/*      */   
/*      */   private void restart() {
/*  341 */     this.delay = this.delayValue.active ? this.delayValue.newLowValue() : 0.0F;
/*  342 */     this.delayTimer = 0.0F;
/*      */     
/*  344 */     this.durationTimer -= this.duration;
/*  345 */     this.duration = this.durationValue.newLowValue();
/*      */     
/*  347 */     this.emission = (int)this.emissionValue.newLowValue();
/*  348 */     this.emissionDiff = (int)this.emissionValue.newHighValue();
/*  349 */     if (!this.emissionValue.isRelative()) this.emissionDiff -= this.emission;
/*      */     
/*  351 */     this.life = (int)this.lifeValue.newLowValue();
/*  352 */     this.lifeDiff = (int)this.lifeValue.newHighValue();
/*  353 */     if (!this.lifeValue.isRelative()) this.lifeDiff -= this.life;
/*      */     
/*  355 */     this.lifeOffset = this.lifeOffsetValue.active ? (int)this.lifeOffsetValue.newLowValue() : 0;
/*  356 */     this.lifeOffsetDiff = (int)this.lifeOffsetValue.newHighValue();
/*  357 */     if (!this.lifeOffsetValue.isRelative()) this.lifeOffsetDiff -= this.lifeOffset;
/*      */     
/*  359 */     this.spawnWidth = this.spawnWidthValue.newLowValue();
/*  360 */     this.spawnWidthDiff = this.spawnWidthValue.newHighValue();
/*  361 */     if (!this.spawnWidthValue.isRelative()) this.spawnWidthDiff -= this.spawnWidth;
/*      */     
/*  363 */     this.spawnHeight = this.spawnHeightValue.newLowValue();
/*  364 */     this.spawnHeightDiff = this.spawnHeightValue.newHighValue();
/*  365 */     if (!this.spawnHeightValue.isRelative()) this.spawnHeightDiff -= this.spawnHeight;
/*      */     
/*  367 */     this.updateFlags = 0;
/*  368 */     if (this.angleValue.active && this.angleValue.timeline.length > 1) this.updateFlags |= 0x2; 
/*  369 */     if (this.velocityValue.active) this.updateFlags |= 0x8; 
/*  370 */     if (this.scaleValue.timeline.length > 1) this.updateFlags |= 0x1; 
/*  371 */     if (this.rotationValue.active && this.rotationValue.timeline.length > 1) this.updateFlags |= 0x4; 
/*  372 */     if (this.windValue.active) this.updateFlags |= 0x10; 
/*  373 */     if (this.gravityValue.active) this.updateFlags |= 0x20; 
/*  374 */     if (this.tintValue.timeline.length > 1) this.updateFlags |= 0x40; 
/*      */   }
/*      */   
/*      */   protected Particle newParticle(Sprite sprite) {
/*  378 */     return new Particle(sprite);
/*      */   }
/*      */   private void activateParticle(int index) {
/*      */     float width, height, radiusX, radiusY, scaleY, radius2;
/*  382 */     Particle particle = this.particles[index];
/*  383 */     if (particle == null) {
/*  384 */       this.particles[index] = particle = newParticle(this.sprite);
/*  385 */       particle.flip(this.flipX, this.flipY);
/*      */     } 
/*      */     
/*  388 */     float percent = this.durationTimer / this.duration;
/*  389 */     int updateFlags = this.updateFlags;
/*      */     
/*  391 */     particle.currentLife = particle.life = this.life + (int)(this.lifeDiff * this.lifeValue.getScale(percent));
/*      */     
/*  393 */     if (this.velocityValue.active) {
/*  394 */       particle.velocity = this.velocityValue.newLowValue();
/*  395 */       particle.velocityDiff = this.velocityValue.newHighValue();
/*  396 */       if (!this.velocityValue.isRelative()) particle.velocityDiff -= particle.velocity;
/*      */     
/*      */     } 
/*  399 */     particle.angle = this.angleValue.newLowValue();
/*  400 */     particle.angleDiff = this.angleValue.newHighValue();
/*  401 */     if (!this.angleValue.isRelative()) particle.angleDiff -= particle.angle; 
/*  402 */     float angle = 0.0F;
/*  403 */     if ((updateFlags & 0x2) == 0) {
/*  404 */       angle = particle.angle + particle.angleDiff * this.angleValue.getScale(0.0F);
/*  405 */       particle.angle = angle;
/*  406 */       particle.angleCos = MathUtils.cosDeg(angle);
/*  407 */       particle.angleSin = MathUtils.sinDeg(angle);
/*      */     } 
/*      */     
/*  410 */     float spriteWidth = this.sprite.getWidth();
/*  411 */     particle.scale = this.scaleValue.newLowValue() / spriteWidth;
/*  412 */     particle.scaleDiff = this.scaleValue.newHighValue() / spriteWidth;
/*  413 */     if (!this.scaleValue.isRelative()) particle.scaleDiff -= particle.scale; 
/*  414 */     particle.setScale(particle.scale + particle.scaleDiff * this.scaleValue.getScale(0.0F));
/*      */     
/*  416 */     if (this.rotationValue.active) {
/*  417 */       particle.rotation = this.rotationValue.newLowValue();
/*  418 */       particle.rotationDiff = this.rotationValue.newHighValue();
/*  419 */       if (!this.rotationValue.isRelative()) particle.rotationDiff -= particle.rotation; 
/*  420 */       float rotation = particle.rotation + particle.rotationDiff * this.rotationValue.getScale(0.0F);
/*  421 */       if (this.aligned) rotation += angle; 
/*  422 */       particle.setRotation(rotation);
/*      */     } 
/*      */     
/*  425 */     if (this.windValue.active) {
/*  426 */       particle.wind = this.windValue.newLowValue();
/*  427 */       particle.windDiff = this.windValue.newHighValue();
/*  428 */       if (!this.windValue.isRelative()) particle.windDiff -= particle.wind;
/*      */     
/*      */     } 
/*  431 */     if (this.gravityValue.active) {
/*  432 */       particle.gravity = this.gravityValue.newLowValue();
/*  433 */       particle.gravityDiff = this.gravityValue.newHighValue();
/*  434 */       if (!this.gravityValue.isRelative()) particle.gravityDiff -= particle.gravity;
/*      */     
/*      */     } 
/*  437 */     float[] color = particle.tint;
/*  438 */     if (color == null) particle.tint = color = new float[3]; 
/*  439 */     float[] temp = this.tintValue.getColor(0.0F);
/*  440 */     color[0] = temp[0];
/*  441 */     color[1] = temp[1];
/*  442 */     color[2] = temp[2];
/*      */     
/*  444 */     particle.transparency = this.transparencyValue.newLowValue();
/*  445 */     particle.transparencyDiff = this.transparencyValue.newHighValue() - particle.transparency;
/*      */ 
/*      */     
/*  448 */     float x = this.x;
/*  449 */     if (this.xOffsetValue.active) x += this.xOffsetValue.newLowValue(); 
/*  450 */     float y = this.y;
/*  451 */     if (this.yOffsetValue.active) y += this.yOffsetValue.newLowValue(); 
/*  452 */     switch (this.spawnShapeValue.shape) {
/*      */       case square:
/*  454 */         width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/*  455 */         height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/*  456 */         x += MathUtils.random(width) - width / 2.0F;
/*  457 */         y += MathUtils.random(height) - height / 2.0F;
/*      */         break;
/*      */       
/*      */       case ellipse:
/*  461 */         width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/*  462 */         height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/*  463 */         radiusX = width / 2.0F;
/*  464 */         radiusY = height / 2.0F;
/*  465 */         if (radiusX == 0.0F || radiusY == 0.0F)
/*  466 */           break;  scaleY = radiusX / radiusY;
/*  467 */         if (this.spawnShapeValue.edges) {
/*      */           float spawnAngle;
/*  469 */           switch (this.spawnShapeValue.side) {
/*      */             case square:
/*  471 */               spawnAngle = -MathUtils.random(179.0F);
/*      */               break;
/*      */             case ellipse:
/*  474 */               spawnAngle = MathUtils.random(179.0F);
/*      */               break;
/*      */             default:
/*  477 */               spawnAngle = MathUtils.random(360.0F);
/*      */               break;
/*      */           } 
/*  480 */           float cosDeg = MathUtils.cosDeg(spawnAngle);
/*  481 */           float sinDeg = MathUtils.sinDeg(spawnAngle);
/*  482 */           x += cosDeg * radiusX;
/*  483 */           y += sinDeg * radiusX / scaleY;
/*  484 */           if ((updateFlags & 0x2) == 0) {
/*  485 */             particle.angle = spawnAngle;
/*  486 */             particle.angleCos = cosDeg;
/*  487 */             particle.angleSin = sinDeg;
/*      */           }  break;
/*      */         } 
/*  490 */         radius2 = radiusX * radiusX;
/*      */         while (true) {
/*  492 */           float px = MathUtils.random(width) - radiusX;
/*  493 */           float py = MathUtils.random(height) - radiusY;
/*  494 */           if (px * px + py * py <= radius2) {
/*  495 */             x += px;
/*  496 */             y += py / scaleY;
/*      */             break;
/*      */           } 
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case line:
/*  504 */         width = this.spawnWidth + this.spawnWidthDiff * this.spawnWidthValue.getScale(percent);
/*  505 */         height = this.spawnHeight + this.spawnHeightDiff * this.spawnHeightValue.getScale(percent);
/*  506 */         if (width != 0.0F) {
/*  507 */           float lineX = width * MathUtils.random();
/*  508 */           x += lineX;
/*  509 */           y += lineX * height / width; break;
/*      */         } 
/*  511 */         y += height * MathUtils.random();
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  516 */     float spriteHeight = this.sprite.getHeight();
/*  517 */     particle.setBounds(x - spriteWidth / 2.0F, y - spriteHeight / 2.0F, spriteWidth, spriteHeight);
/*      */     
/*  519 */     int offsetTime = (int)(this.lifeOffset + this.lifeOffsetDiff * this.lifeOffsetValue.getScale(percent));
/*  520 */     if (offsetTime > 0) {
/*  521 */       if (offsetTime >= particle.currentLife) offsetTime = particle.currentLife - 1; 
/*  522 */       updateParticle(particle, offsetTime / 1000.0F, offsetTime);
/*      */     } 
/*      */   }
/*      */   private boolean updateParticle(Particle particle, float delta, int deltaMillis) {
/*      */     float[] color;
/*  527 */     int life = particle.currentLife - deltaMillis;
/*  528 */     if (life <= 0) return false; 
/*  529 */     particle.currentLife = life;
/*      */     
/*  531 */     float percent = 1.0F - particle.currentLife / particle.life;
/*  532 */     int updateFlags = this.updateFlags;
/*      */     
/*  534 */     if ((updateFlags & 0x1) != 0) {
/*  535 */       particle.setScale(particle.scale + particle.scaleDiff * this.scaleValue.getScale(percent));
/*      */     }
/*  537 */     if ((updateFlags & 0x8) != 0) {
/*  538 */       float velocityX, velocityY, velocity = (particle.velocity + particle.velocityDiff * this.velocityValue.getScale(percent)) * delta;
/*      */ 
/*      */       
/*  541 */       if ((updateFlags & 0x2) != 0) {
/*  542 */         float angle = particle.angle + particle.angleDiff * this.angleValue.getScale(percent);
/*  543 */         velocityX = velocity * MathUtils.cosDeg(angle);
/*  544 */         velocityY = velocity * MathUtils.sinDeg(angle);
/*  545 */         if ((updateFlags & 0x4) != 0) {
/*  546 */           float rotation = particle.rotation + particle.rotationDiff * this.rotationValue.getScale(percent);
/*  547 */           if (this.aligned) rotation += angle; 
/*  548 */           particle.setRotation(rotation);
/*      */         } 
/*      */       } else {
/*  551 */         velocityX = velocity * particle.angleCos;
/*  552 */         velocityY = velocity * particle.angleSin;
/*  553 */         if (this.aligned || (updateFlags & 0x4) != 0) {
/*  554 */           float rotation = particle.rotation + particle.rotationDiff * this.rotationValue.getScale(percent);
/*  555 */           if (this.aligned) rotation += particle.angle; 
/*  556 */           particle.setRotation(rotation);
/*      */         } 
/*      */       } 
/*      */       
/*  560 */       if ((updateFlags & 0x10) != 0) {
/*  561 */         velocityX += (particle.wind + particle.windDiff * this.windValue.getScale(percent)) * delta;
/*      */       }
/*  563 */       if ((updateFlags & 0x20) != 0) {
/*  564 */         velocityY += (particle.gravity + particle.gravityDiff * this.gravityValue.getScale(percent)) * delta;
/*      */       }
/*  566 */       particle.translate(velocityX, velocityY);
/*      */     }
/*  568 */     else if ((updateFlags & 0x4) != 0) {
/*  569 */       particle.setRotation(particle.rotation + particle.rotationDiff * this.rotationValue.getScale(percent));
/*      */     } 
/*      */ 
/*      */     
/*  573 */     if ((updateFlags & 0x40) != 0) {
/*  574 */       color = this.tintValue.getColor(percent);
/*      */     } else {
/*  576 */       color = particle.tint;
/*      */     } 
/*  578 */     if (this.premultipliedAlpha) {
/*  579 */       float alphaMultiplier = this.additive ? 0.0F : 1.0F;
/*  580 */       float a = particle.transparency + particle.transparencyDiff * this.transparencyValue.getScale(percent);
/*  581 */       particle.setColor(color[0] * a, color[1] * a, color[2] * a, a * alphaMultiplier);
/*      */     } else {
/*  583 */       particle.setColor(color[0], color[1], color[2], particle.transparency + particle.transparencyDiff * this.transparencyValue
/*  584 */           .getScale(percent));
/*      */     } 
/*  586 */     return true;
/*      */   }
/*      */   
/*      */   public void setPosition(float x, float y) {
/*  590 */     if (this.attached) {
/*  591 */       float xAmount = x - this.x;
/*  592 */       float yAmount = y - this.y;
/*  593 */       boolean[] active = this.active;
/*  594 */       for (int i = 0, n = active.length; i < n; i++) {
/*  595 */         if (active[i]) this.particles[i].translate(xAmount, yAmount); 
/*      */       } 
/*  597 */     }  this.x = x;
/*  598 */     this.y = y;
/*      */   }
/*      */   
/*      */   public void setSprite(Sprite sprite) {
/*  602 */     this.sprite = sprite;
/*  603 */     if (sprite == null)
/*  604 */       return;  float originX = sprite.getOriginX();
/*  605 */     float originY = sprite.getOriginY();
/*  606 */     Texture texture = sprite.getTexture();
/*  607 */     for (int i = 0, n = this.particles.length; i < n; i++) {
/*  608 */       Particle particle = this.particles[i];
/*  609 */       if (particle == null)
/*  610 */         break;  particle.setTexture(texture);
/*  611 */       particle.setOrigin(originX, originY);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void allowCompletion() {
/*  618 */     this.allowCompletion = true;
/*  619 */     this.durationTimer = this.duration;
/*      */   }
/*      */   
/*      */   public Sprite getSprite() {
/*  623 */     return this.sprite;
/*      */   }
/*      */   
/*      */   public String getName() {
/*  627 */     return this.name;
/*      */   }
/*      */   
/*      */   public void setName(String name) {
/*  631 */     this.name = name;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getLife() {
/*  635 */     return this.lifeValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getScale() {
/*  639 */     return this.scaleValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getRotation() {
/*  643 */     return this.rotationValue;
/*      */   }
/*      */   
/*      */   public GradientColorValue getTint() {
/*  647 */     return this.tintValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getVelocity() {
/*  651 */     return this.velocityValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getWind() {
/*  655 */     return this.windValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getGravity() {
/*  659 */     return this.gravityValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getAngle() {
/*  663 */     return this.angleValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getEmission() {
/*  667 */     return this.emissionValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getTransparency() {
/*  671 */     return this.transparencyValue;
/*      */   }
/*      */   
/*      */   public RangedNumericValue getDuration() {
/*  675 */     return this.durationValue;
/*      */   }
/*      */   
/*      */   public RangedNumericValue getDelay() {
/*  679 */     return this.delayValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getLifeOffset() {
/*  683 */     return this.lifeOffsetValue;
/*      */   }
/*      */   
/*      */   public RangedNumericValue getXOffsetValue() {
/*  687 */     return this.xOffsetValue;
/*      */   }
/*      */   
/*      */   public RangedNumericValue getYOffsetValue() {
/*  691 */     return this.yOffsetValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getSpawnWidth() {
/*  695 */     return this.spawnWidthValue;
/*      */   }
/*      */   
/*      */   public ScaledNumericValue getSpawnHeight() {
/*  699 */     return this.spawnHeightValue;
/*      */   }
/*      */   
/*      */   public SpawnShapeValue getSpawnShape() {
/*  703 */     return this.spawnShapeValue;
/*      */   }
/*      */   
/*      */   public boolean isAttached() {
/*  707 */     return this.attached;
/*      */   }
/*      */   
/*      */   public void setAttached(boolean attached) {
/*  711 */     this.attached = attached;
/*      */   }
/*      */   
/*      */   public boolean isContinuous() {
/*  715 */     return this.continuous;
/*      */   }
/*      */   
/*      */   public void setContinuous(boolean continuous) {
/*  719 */     this.continuous = continuous;
/*      */   }
/*      */   
/*      */   public boolean isAligned() {
/*  723 */     return this.aligned;
/*      */   }
/*      */   
/*      */   public void setAligned(boolean aligned) {
/*  727 */     this.aligned = aligned;
/*      */   }
/*      */   
/*      */   public boolean isAdditive() {
/*  731 */     return this.additive;
/*      */   }
/*      */   
/*      */   public void setAdditive(boolean additive) {
/*  735 */     this.additive = additive;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean cleansUpBlendFunction() {
/*  741 */     return this.cleansUpBlendFunction;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setCleansUpBlendFunction(boolean cleansUpBlendFunction) {
/*  753 */     this.cleansUpBlendFunction = cleansUpBlendFunction;
/*      */   }
/*      */   
/*      */   public boolean isBehind() {
/*  757 */     return this.behind;
/*      */   }
/*      */   
/*      */   public void setBehind(boolean behind) {
/*  761 */     this.behind = behind;
/*      */   }
/*      */   
/*      */   public boolean isPremultipliedAlpha() {
/*  765 */     return this.premultipliedAlpha;
/*      */   }
/*      */   
/*      */   public void setPremultipliedAlpha(boolean premultipliedAlpha) {
/*  769 */     this.premultipliedAlpha = premultipliedAlpha;
/*      */   }
/*      */   
/*      */   public int getMinParticleCount() {
/*  773 */     return this.minParticleCount;
/*      */   }
/*      */   
/*      */   public void setMinParticleCount(int minParticleCount) {
/*  777 */     this.minParticleCount = minParticleCount;
/*      */   }
/*      */   
/*      */   public int getMaxParticleCount() {
/*  781 */     return this.maxParticleCount;
/*      */   }
/*      */   
/*      */   public boolean isComplete() {
/*  785 */     if (this.continuous && !this.allowCompletion) return false; 
/*  786 */     if (this.delayTimer < this.delay) return false; 
/*  787 */     return (this.durationTimer >= this.duration && this.activeCount == 0);
/*      */   }
/*      */   
/*      */   public float getPercentComplete() {
/*  791 */     if (this.delayTimer < this.delay) return 0.0F; 
/*  792 */     return Math.min(1.0F, this.durationTimer / this.duration);
/*      */   }
/*      */   
/*      */   public float getX() {
/*  796 */     return this.x;
/*      */   }
/*      */   
/*      */   public float getY() {
/*  800 */     return this.y;
/*      */   }
/*      */   
/*      */   public int getActiveCount() {
/*  804 */     return this.activeCount;
/*      */   }
/*      */   
/*      */   public String getImagePath() {
/*  808 */     return this.imagePath;
/*      */   }
/*      */   
/*      */   public void setImagePath(String imagePath) {
/*  812 */     this.imagePath = imagePath;
/*      */   }
/*      */   
/*      */   public void setFlip(boolean flipX, boolean flipY) {
/*  816 */     this.flipX = flipX;
/*  817 */     this.flipY = flipY;
/*  818 */     if (this.particles == null)
/*  819 */       return;  for (int i = 0, n = this.particles.length; i < n; i++) {
/*  820 */       Particle particle = this.particles[i];
/*  821 */       if (particle != null) particle.flip(flipX, flipY); 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void flipY() {
/*  826 */     this.angleValue.setHigh(-this.angleValue.getHighMin(), -this.angleValue.getHighMax());
/*  827 */     this.angleValue.setLow(-this.angleValue.getLowMin(), -this.angleValue.getLowMax());
/*      */     
/*  829 */     this.gravityValue.setHigh(-this.gravityValue.getHighMin(), -this.gravityValue.getHighMax());
/*  830 */     this.gravityValue.setLow(-this.gravityValue.getLowMin(), -this.gravityValue.getLowMax());
/*      */     
/*  832 */     this.windValue.setHigh(-this.windValue.getHighMin(), -this.windValue.getHighMax());
/*  833 */     this.windValue.setLow(-this.windValue.getLowMin(), -this.windValue.getLowMax());
/*      */     
/*  835 */     this.rotationValue.setHigh(-this.rotationValue.getHighMin(), -this.rotationValue.getHighMax());
/*  836 */     this.rotationValue.setLow(-this.rotationValue.getLowMin(), -this.rotationValue.getLowMax());
/*      */     
/*  838 */     this.yOffsetValue.setLow(-this.yOffsetValue.getLowMin(), -this.yOffsetValue.getLowMax());
/*      */   }
/*      */ 
/*      */   
/*      */   public BoundingBox getBoundingBox() {
/*  843 */     if (this.bounds == null) this.bounds = new BoundingBox();
/*      */     
/*  845 */     Particle[] particles = this.particles;
/*  846 */     boolean[] active = this.active;
/*  847 */     BoundingBox bounds = this.bounds;
/*      */     
/*  849 */     bounds.inf();
/*  850 */     for (int i = 0, n = active.length; i < n; i++) {
/*  851 */       if (active[i]) {
/*  852 */         Rectangle r = particles[i].getBoundingRectangle();
/*  853 */         bounds.ext(r.x, r.y, 0.0F);
/*  854 */         bounds.ext(r.x + r.width, r.y + r.height, 0.0F);
/*      */       } 
/*      */     } 
/*  857 */     return bounds;
/*      */   }
/*      */   
/*      */   public void save(Writer output) throws IOException {
/*  861 */     output.write(this.name + "\n");
/*  862 */     output.write("- Delay -\n");
/*  863 */     this.delayValue.save(output);
/*  864 */     output.write("- Duration - \n");
/*  865 */     this.durationValue.save(output);
/*  866 */     output.write("- Count - \n");
/*  867 */     output.write("min: " + this.minParticleCount + "\n");
/*  868 */     output.write("max: " + this.maxParticleCount + "\n");
/*  869 */     output.write("- Emission - \n");
/*  870 */     this.emissionValue.save(output);
/*  871 */     output.write("- Life - \n");
/*  872 */     this.lifeValue.save(output);
/*  873 */     output.write("- Life Offset - \n");
/*  874 */     this.lifeOffsetValue.save(output);
/*  875 */     output.write("- X Offset - \n");
/*  876 */     this.xOffsetValue.save(output);
/*  877 */     output.write("- Y Offset - \n");
/*  878 */     this.yOffsetValue.save(output);
/*  879 */     output.write("- Spawn Shape - \n");
/*  880 */     this.spawnShapeValue.save(output);
/*  881 */     output.write("- Spawn Width - \n");
/*  882 */     this.spawnWidthValue.save(output);
/*  883 */     output.write("- Spawn Height - \n");
/*  884 */     this.spawnHeightValue.save(output);
/*  885 */     output.write("- Scale - \n");
/*  886 */     this.scaleValue.save(output);
/*  887 */     output.write("- Velocity - \n");
/*  888 */     this.velocityValue.save(output);
/*  889 */     output.write("- Angle - \n");
/*  890 */     this.angleValue.save(output);
/*  891 */     output.write("- Rotation - \n");
/*  892 */     this.rotationValue.save(output);
/*  893 */     output.write("- Wind - \n");
/*  894 */     this.windValue.save(output);
/*  895 */     output.write("- Gravity - \n");
/*  896 */     this.gravityValue.save(output);
/*  897 */     output.write("- Tint - \n");
/*  898 */     this.tintValue.save(output);
/*  899 */     output.write("- Transparency - \n");
/*  900 */     this.transparencyValue.save(output);
/*  901 */     output.write("- Options - \n");
/*  902 */     output.write("attached: " + this.attached + "\n");
/*  903 */     output.write("continuous: " + this.continuous + "\n");
/*  904 */     output.write("aligned: " + this.aligned + "\n");
/*  905 */     output.write("additive: " + this.additive + "\n");
/*  906 */     output.write("behind: " + this.behind + "\n");
/*  907 */     output.write("premultipliedAlpha: " + this.premultipliedAlpha + "\n");
/*  908 */     output.write("- Image Path -\n");
/*  909 */     output.write(this.imagePath + "\n");
/*      */   }
/*      */   
/*      */   public void load(BufferedReader reader) throws IOException {
/*      */     try {
/*  914 */       this.name = readString(reader, "name");
/*  915 */       reader.readLine();
/*  916 */       this.delayValue.load(reader);
/*  917 */       reader.readLine();
/*  918 */       this.durationValue.load(reader);
/*  919 */       reader.readLine();
/*  920 */       setMinParticleCount(readInt(reader, "minParticleCount"));
/*  921 */       setMaxParticleCount(readInt(reader, "maxParticleCount"));
/*  922 */       reader.readLine();
/*  923 */       this.emissionValue.load(reader);
/*  924 */       reader.readLine();
/*  925 */       this.lifeValue.load(reader);
/*  926 */       reader.readLine();
/*  927 */       this.lifeOffsetValue.load(reader);
/*  928 */       reader.readLine();
/*  929 */       this.xOffsetValue.load(reader);
/*  930 */       reader.readLine();
/*  931 */       this.yOffsetValue.load(reader);
/*  932 */       reader.readLine();
/*  933 */       this.spawnShapeValue.load(reader);
/*  934 */       reader.readLine();
/*  935 */       this.spawnWidthValue.load(reader);
/*  936 */       reader.readLine();
/*  937 */       this.spawnHeightValue.load(reader);
/*  938 */       reader.readLine();
/*  939 */       this.scaleValue.load(reader);
/*  940 */       reader.readLine();
/*  941 */       this.velocityValue.load(reader);
/*  942 */       reader.readLine();
/*  943 */       this.angleValue.load(reader);
/*  944 */       reader.readLine();
/*  945 */       this.rotationValue.load(reader);
/*  946 */       reader.readLine();
/*  947 */       this.windValue.load(reader);
/*  948 */       reader.readLine();
/*  949 */       this.gravityValue.load(reader);
/*  950 */       reader.readLine();
/*  951 */       this.tintValue.load(reader);
/*  952 */       reader.readLine();
/*  953 */       this.transparencyValue.load(reader);
/*  954 */       reader.readLine();
/*  955 */       this.attached = readBoolean(reader, "attached");
/*  956 */       this.continuous = readBoolean(reader, "continuous");
/*  957 */       this.aligned = readBoolean(reader, "aligned");
/*  958 */       this.additive = readBoolean(reader, "additive");
/*  959 */       this.behind = readBoolean(reader, "behind");
/*      */ 
/*      */       
/*  962 */       String line = reader.readLine();
/*  963 */       if (line.startsWith("premultipliedAlpha")) {
/*  964 */         this.premultipliedAlpha = readBoolean(line);
/*  965 */         reader.readLine();
/*      */       } 
/*  967 */       setImagePath(reader.readLine());
/*  968 */     } catch (RuntimeException ex) {
/*  969 */       if (this.name == null) throw ex; 
/*  970 */       throw new RuntimeException("Error parsing emitter: " + this.name, ex);
/*      */     } 
/*      */   }
/*      */   
/*      */   static String readString(String line) throws IOException {
/*  975 */     return line.substring(line.indexOf(":") + 1).trim();
/*      */   }
/*      */   
/*      */   static String readString(BufferedReader reader, String name) throws IOException {
/*  979 */     String line = reader.readLine();
/*  980 */     if (line == null) throw new IOException("Missing value: " + name); 
/*  981 */     return readString(line);
/*      */   }
/*      */   
/*      */   static boolean readBoolean(String line) throws IOException {
/*  985 */     return Boolean.parseBoolean(readString(line));
/*      */   }
/*      */   
/*      */   static boolean readBoolean(BufferedReader reader, String name) throws IOException {
/*  989 */     return Boolean.parseBoolean(readString(reader, name));
/*      */   }
/*      */   
/*      */   static int readInt(BufferedReader reader, String name) throws IOException {
/*  993 */     return Integer.parseInt(readString(reader, name));
/*      */   }
/*      */   
/*      */   static float readFloat(BufferedReader reader, String name) throws IOException {
/*  997 */     return Float.parseFloat(readString(reader, name));
/*      */   }
/*      */   
/*      */   public static class Particle
/*      */     extends Sprite {
/*      */     protected int life;
/*      */     protected int currentLife;
/*      */     protected float scale;
/*      */     protected float scaleDiff;
/*      */     protected float rotation;
/*      */     protected float rotationDiff;
/*      */     protected float velocity;
/*      */     protected float velocityDiff;
/*      */     protected float angle;
/*      */     
/*      */     public Particle(Sprite sprite) {
/* 1013 */       super(sprite);
/*      */     }
/*      */     protected float angleDiff; protected float angleCos; protected float angleSin; protected float transparency; protected float transparencyDiff; protected float wind; protected float windDiff; protected float gravity; protected float gravityDiff;
/*      */     protected float[] tint; }
/*      */   
/*      */   public static class ParticleValue { boolean active;
/*      */     boolean alwaysActive;
/*      */     
/*      */     public void setAlwaysActive(boolean alwaysActive) {
/* 1022 */       this.alwaysActive = alwaysActive;
/*      */     }
/*      */     
/*      */     public boolean isAlwaysActive() {
/* 1026 */       return this.alwaysActive;
/*      */     }
/*      */     
/*      */     public boolean isActive() {
/* 1030 */       return (this.alwaysActive || this.active);
/*      */     }
/*      */     
/*      */     public void setActive(boolean active) {
/* 1034 */       this.active = active;
/*      */     }
/*      */     
/*      */     public void save(Writer output) throws IOException {
/* 1038 */       if (!this.alwaysActive) {
/* 1039 */         output.write("active: " + this.active + "\n");
/*      */       } else {
/* 1041 */         this.active = true;
/*      */       } 
/*      */     }
/*      */     public void load(BufferedReader reader) throws IOException {
/* 1045 */       if (!this.alwaysActive) {
/* 1046 */         this.active = ParticleEmitter.readBoolean(reader, "active");
/*      */       } else {
/* 1048 */         this.active = true;
/*      */       } 
/*      */     }
/*      */     public void load(ParticleValue value) {
/* 1052 */       this.active = value.active;
/* 1053 */       this.alwaysActive = value.alwaysActive;
/*      */     } }
/*      */ 
/*      */   
/*      */   public static class NumericValue extends ParticleValue {
/*      */     private float value;
/*      */     
/*      */     public float getValue() {
/* 1061 */       return this.value;
/*      */     }
/*      */     
/*      */     public void setValue(float value) {
/* 1065 */       this.value = value;
/*      */     }
/*      */     
/*      */     public void save(Writer output) throws IOException {
/* 1069 */       super.save(output);
/* 1070 */       if (!this.active)
/* 1071 */         return;  output.write("value: " + this.value + "\n");
/*      */     }
/*      */     
/*      */     public void load(BufferedReader reader) throws IOException {
/* 1075 */       super.load(reader);
/* 1076 */       if (!this.active)
/* 1077 */         return;  this.value = ParticleEmitter.readFloat(reader, "value");
/*      */     }
/*      */     
/*      */     public void load(NumericValue value) {
/* 1081 */       load(value);
/* 1082 */       this.value = value.value;
/*      */     } }
/*      */   
/*      */   public static class RangedNumericValue extends ParticleValue {
/*      */     private float lowMin;
/*      */     private float lowMax;
/*      */     
/*      */     public float newLowValue() {
/* 1090 */       return this.lowMin + (this.lowMax - this.lowMin) * MathUtils.random();
/*      */     }
/*      */     
/*      */     public void setLow(float value) {
/* 1094 */       this.lowMin = value;
/* 1095 */       this.lowMax = value;
/*      */     }
/*      */     
/*      */     public void setLow(float min, float max) {
/* 1099 */       this.lowMin = min;
/* 1100 */       this.lowMax = max;
/*      */     }
/*      */     
/*      */     public float getLowMin() {
/* 1104 */       return this.lowMin;
/*      */     }
/*      */     
/*      */     public void setLowMin(float lowMin) {
/* 1108 */       this.lowMin = lowMin;
/*      */     }
/*      */     
/*      */     public float getLowMax() {
/* 1112 */       return this.lowMax;
/*      */     }
/*      */     
/*      */     public void setLowMax(float lowMax) {
/* 1116 */       this.lowMax = lowMax;
/*      */     }
/*      */     
/*      */     public void save(Writer output) throws IOException {
/* 1120 */       super.save(output);
/* 1121 */       if (!this.active)
/* 1122 */         return;  output.write("lowMin: " + this.lowMin + "\n");
/* 1123 */       output.write("lowMax: " + this.lowMax + "\n");
/*      */     }
/*      */     
/*      */     public void load(BufferedReader reader) throws IOException {
/* 1127 */       super.load(reader);
/* 1128 */       if (!this.active)
/* 1129 */         return;  this.lowMin = ParticleEmitter.readFloat(reader, "lowMin");
/* 1130 */       this.lowMax = ParticleEmitter.readFloat(reader, "lowMax");
/*      */     }
/*      */     
/*      */     public void load(RangedNumericValue value) {
/* 1134 */       load(value);
/* 1135 */       this.lowMax = value.lowMax;
/* 1136 */       this.lowMin = value.lowMin;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ScaledNumericValue extends RangedNumericValue {
/* 1141 */     private float[] scaling = new float[] { 1.0F };
/* 1142 */     float[] timeline = new float[] { 0.0F }; private float highMin;
/*      */     private float highMax;
/*      */     private boolean relative;
/*      */     
/*      */     public float newHighValue() {
/* 1147 */       return this.highMin + (this.highMax - this.highMin) * MathUtils.random();
/*      */     }
/*      */     
/*      */     public void setHigh(float value) {
/* 1151 */       this.highMin = value;
/* 1152 */       this.highMax = value;
/*      */     }
/*      */     
/*      */     public void setHigh(float min, float max) {
/* 1156 */       this.highMin = min;
/* 1157 */       this.highMax = max;
/*      */     }
/*      */     
/*      */     public float getHighMin() {
/* 1161 */       return this.highMin;
/*      */     }
/*      */     
/*      */     public void setHighMin(float highMin) {
/* 1165 */       this.highMin = highMin;
/*      */     }
/*      */     
/*      */     public float getHighMax() {
/* 1169 */       return this.highMax;
/*      */     }
/*      */     
/*      */     public void setHighMax(float highMax) {
/* 1173 */       this.highMax = highMax;
/*      */     }
/*      */     
/*      */     public float[] getScaling() {
/* 1177 */       return this.scaling;
/*      */     }
/*      */     
/*      */     public void setScaling(float[] values) {
/* 1181 */       this.scaling = values;
/*      */     }
/*      */     
/*      */     public float[] getTimeline() {
/* 1185 */       return this.timeline;
/*      */     }
/*      */     
/*      */     public void setTimeline(float[] timeline) {
/* 1189 */       this.timeline = timeline;
/*      */     }
/*      */     
/*      */     public boolean isRelative() {
/* 1193 */       return this.relative;
/*      */     }
/*      */     
/*      */     public void setRelative(boolean relative) {
/* 1197 */       this.relative = relative;
/*      */     }
/*      */     
/*      */     public float getScale(float percent) {
/* 1201 */       int endIndex = -1;
/* 1202 */       float[] timeline = this.timeline;
/* 1203 */       int n = timeline.length;
/* 1204 */       for (int i = 1; i < n; i++) {
/* 1205 */         float t = timeline[i];
/* 1206 */         if (t > percent) {
/* 1207 */           endIndex = i;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1211 */       if (endIndex == -1) return this.scaling[n - 1]; 
/* 1212 */       float[] scaling = this.scaling;
/* 1213 */       int startIndex = endIndex - 1;
/* 1214 */       float startValue = scaling[startIndex];
/* 1215 */       float startTime = timeline[startIndex];
/* 1216 */       return startValue + (scaling[endIndex] - startValue) * (percent - startTime) / (timeline[endIndex] - startTime);
/*      */     }
/*      */     
/*      */     public void save(Writer output) throws IOException {
/* 1220 */       super.save(output);
/* 1221 */       if (!this.active)
/* 1222 */         return;  output.write("highMin: " + this.highMin + "\n");
/* 1223 */       output.write("highMax: " + this.highMax + "\n");
/* 1224 */       output.write("relative: " + this.relative + "\n");
/* 1225 */       output.write("scalingCount: " + this.scaling.length + "\n"); int i;
/* 1226 */       for (i = 0; i < this.scaling.length; i++)
/* 1227 */         output.write("scaling" + i + ": " + this.scaling[i] + "\n"); 
/* 1228 */       output.write("timelineCount: " + this.timeline.length + "\n");
/* 1229 */       for (i = 0; i < this.timeline.length; i++)
/* 1230 */         output.write("timeline" + i + ": " + this.timeline[i] + "\n"); 
/*      */     }
/*      */     
/*      */     public void load(BufferedReader reader) throws IOException {
/* 1234 */       super.load(reader);
/* 1235 */       if (!this.active)
/* 1236 */         return;  this.highMin = ParticleEmitter.readFloat(reader, "highMin");
/* 1237 */       this.highMax = ParticleEmitter.readFloat(reader, "highMax");
/* 1238 */       this.relative = ParticleEmitter.readBoolean(reader, "relative");
/* 1239 */       this.scaling = new float[ParticleEmitter.readInt(reader, "scalingCount")]; int i;
/* 1240 */       for (i = 0; i < this.scaling.length; i++)
/* 1241 */         this.scaling[i] = ParticleEmitter.readFloat(reader, "scaling" + i); 
/* 1242 */       this.timeline = new float[ParticleEmitter.readInt(reader, "timelineCount")];
/* 1243 */       for (i = 0; i < this.timeline.length; i++)
/* 1244 */         this.timeline[i] = ParticleEmitter.readFloat(reader, "timeline" + i); 
/*      */     }
/*      */     
/*      */     public void load(ScaledNumericValue value) {
/* 1248 */       load(value);
/* 1249 */       this.highMax = value.highMax;
/* 1250 */       this.highMin = value.highMin;
/* 1251 */       this.scaling = new float[value.scaling.length];
/* 1252 */       System.arraycopy(value.scaling, 0, this.scaling, 0, this.scaling.length);
/* 1253 */       this.timeline = new float[value.timeline.length];
/* 1254 */       System.arraycopy(value.timeline, 0, this.timeline, 0, this.timeline.length);
/* 1255 */       this.relative = value.relative;
/*      */     }
/*      */   }
/*      */   
/*      */   public static class GradientColorValue extends ParticleValue {
/* 1260 */     private static float[] temp = new float[4];
/*      */     
/* 1262 */     private float[] colors = new float[] { 1.0F, 1.0F, 1.0F };
/* 1263 */     float[] timeline = new float[] { 0.0F };
/*      */     
/*      */     public GradientColorValue() {
/* 1266 */       this.alwaysActive = true;
/*      */     }
/*      */     
/*      */     public float[] getTimeline() {
/* 1270 */       return this.timeline;
/*      */     }
/*      */     
/*      */     public void setTimeline(float[] timeline) {
/* 1274 */       this.timeline = timeline;
/*      */     }
/*      */ 
/*      */     
/*      */     public float[] getColors() {
/* 1279 */       return this.colors;
/*      */     }
/*      */ 
/*      */     
/*      */     public void setColors(float[] colors) {
/* 1284 */       this.colors = colors;
/*      */     }
/*      */     
/*      */     public float[] getColor(float percent) {
/* 1288 */       int startIndex = 0, endIndex = -1;
/* 1289 */       float[] timeline = this.timeline;
/* 1290 */       int n = timeline.length;
/* 1291 */       for (int i = 1; i < n; i++) {
/* 1292 */         float t = timeline[i];
/* 1293 */         if (t > percent) {
/* 1294 */           endIndex = i;
/*      */           break;
/*      */         } 
/* 1297 */         startIndex = i;
/*      */       } 
/* 1299 */       float startTime = timeline[startIndex];
/* 1300 */       startIndex *= 3;
/* 1301 */       float r1 = this.colors[startIndex];
/* 1302 */       float g1 = this.colors[startIndex + 1];
/* 1303 */       float b1 = this.colors[startIndex + 2];
/* 1304 */       if (endIndex == -1) {
/* 1305 */         temp[0] = r1;
/* 1306 */         temp[1] = g1;
/* 1307 */         temp[2] = b1;
/* 1308 */         return temp;
/*      */       } 
/* 1310 */       float factor = (percent - startTime) / (timeline[endIndex] - startTime);
/* 1311 */       endIndex *= 3;
/* 1312 */       temp[0] = r1 + (this.colors[endIndex] - r1) * factor;
/* 1313 */       temp[1] = g1 + (this.colors[endIndex + 1] - g1) * factor;
/* 1314 */       temp[2] = b1 + (this.colors[endIndex + 2] - b1) * factor;
/* 1315 */       return temp;
/*      */     }
/*      */     
/*      */     public void save(Writer output) throws IOException {
/* 1319 */       super.save(output);
/* 1320 */       if (!this.active)
/* 1321 */         return;  output.write("colorsCount: " + this.colors.length + "\n"); int i;
/* 1322 */       for (i = 0; i < this.colors.length; i++)
/* 1323 */         output.write("colors" + i + ": " + this.colors[i] + "\n"); 
/* 1324 */       output.write("timelineCount: " + this.timeline.length + "\n");
/* 1325 */       for (i = 0; i < this.timeline.length; i++)
/* 1326 */         output.write("timeline" + i + ": " + this.timeline[i] + "\n"); 
/*      */     }
/*      */     
/*      */     public void load(BufferedReader reader) throws IOException {
/* 1330 */       super.load(reader);
/* 1331 */       if (!this.active)
/* 1332 */         return;  this.colors = new float[ParticleEmitter.readInt(reader, "colorsCount")]; int i;
/* 1333 */       for (i = 0; i < this.colors.length; i++)
/* 1334 */         this.colors[i] = ParticleEmitter.readFloat(reader, "colors" + i); 
/* 1335 */       this.timeline = new float[ParticleEmitter.readInt(reader, "timelineCount")];
/* 1336 */       for (i = 0; i < this.timeline.length; i++)
/* 1337 */         this.timeline[i] = ParticleEmitter.readFloat(reader, "timeline" + i); 
/*      */     }
/*      */     
/*      */     public void load(GradientColorValue value) {
/* 1341 */       load(value);
/* 1342 */       this.colors = new float[value.colors.length];
/* 1343 */       System.arraycopy(value.colors, 0, this.colors, 0, this.colors.length);
/* 1344 */       this.timeline = new float[value.timeline.length];
/* 1345 */       System.arraycopy(value.timeline, 0, this.timeline, 0, this.timeline.length);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class SpawnShapeValue extends ParticleValue {
/* 1350 */     ParticleEmitter.SpawnShape shape = ParticleEmitter.SpawnShape.point;
/*      */     boolean edges;
/* 1352 */     ParticleEmitter.SpawnEllipseSide side = ParticleEmitter.SpawnEllipseSide.both;
/*      */     
/*      */     public ParticleEmitter.SpawnShape getShape() {
/* 1355 */       return this.shape;
/*      */     }
/*      */     
/*      */     public void setShape(ParticleEmitter.SpawnShape shape) {
/* 1359 */       this.shape = shape;
/*      */     }
/*      */     
/*      */     public boolean isEdges() {
/* 1363 */       return this.edges;
/*      */     }
/*      */     
/*      */     public void setEdges(boolean edges) {
/* 1367 */       this.edges = edges;
/*      */     }
/*      */     
/*      */     public ParticleEmitter.SpawnEllipseSide getSide() {
/* 1371 */       return this.side;
/*      */     }
/*      */     
/*      */     public void setSide(ParticleEmitter.SpawnEllipseSide side) {
/* 1375 */       this.side = side;
/*      */     }
/*      */     
/*      */     public void save(Writer output) throws IOException {
/* 1379 */       super.save(output);
/* 1380 */       if (!this.active)
/* 1381 */         return;  output.write("shape: " + this.shape + "\n");
/* 1382 */       if (this.shape == ParticleEmitter.SpawnShape.ellipse) {
/* 1383 */         output.write("edges: " + this.edges + "\n");
/* 1384 */         output.write("side: " + this.side + "\n");
/*      */       } 
/*      */     }
/*      */     
/*      */     public void load(BufferedReader reader) throws IOException {
/* 1389 */       super.load(reader);
/* 1390 */       if (!this.active)
/* 1391 */         return;  this.shape = ParticleEmitter.SpawnShape.valueOf(ParticleEmitter.readString(reader, "shape"));
/* 1392 */       if (this.shape == ParticleEmitter.SpawnShape.ellipse) {
/* 1393 */         this.edges = ParticleEmitter.readBoolean(reader, "edges");
/* 1394 */         this.side = ParticleEmitter.SpawnEllipseSide.valueOf(ParticleEmitter.readString(reader, "side"));
/*      */       } 
/*      */     }
/*      */     
/*      */     public void load(SpawnShapeValue value) {
/* 1399 */       load(value);
/* 1400 */       this.shape = value.shape;
/* 1401 */       this.edges = value.edges;
/* 1402 */       this.side = value.side;
/*      */     }
/*      */   }
/*      */   
/*      */   public enum SpawnShape {
/* 1407 */     point, line, square, ellipse;
/*      */   }
/*      */   
/*      */   public enum SpawnEllipseSide {
/* 1411 */     both, top, bottom;
/*      */   }
/*      */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\ParticleEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */