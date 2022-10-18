/*     */ package com.badlogic.gdx.graphics.g3d.particles;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.emitters.Emitter;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.influencers.Influencer;
/*     */ import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderer;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.math.collision.BoundingBox;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ParticleController
/*     */   implements Json.Serializable, ResourceData.Configurable
/*     */ {
/*     */   protected static final float DEFAULT_TIME_STEP = 0.016666668F;
/*     */   public String name;
/*     */   public Emitter emitter;
/*     */   public Array<Influencer> influencers;
/*     */   public ParticleControllerRenderer<?, ?> renderer;
/*     */   public ParallelArray particles;
/*     */   public ParticleChannels particleChannels;
/*     */   public Matrix4 transform;
/*     */   public Vector3 scale;
/*     */   protected BoundingBox boundingBox;
/*     */   public float deltaTime;
/*     */   public float deltaTimeSqr;
/*     */   
/*     */   public ParticleController() {
/*  70 */     this.transform = new Matrix4();
/*  71 */     this.scale = new Vector3(1.0F, 1.0F, 1.0F);
/*  72 */     this.influencers = new Array(true, 3, Influencer.class);
/*  73 */     setTimeStep(0.016666668F);
/*     */   }
/*     */   
/*     */   public ParticleController(String name, Emitter emitter, ParticleControllerRenderer<?, ?> renderer, Influencer... influencers) {
/*  77 */     this();
/*  78 */     this.name = name;
/*  79 */     this.emitter = emitter;
/*  80 */     this.renderer = renderer;
/*  81 */     this.particleChannels = new ParticleChannels();
/*  82 */     this.influencers = new Array((Object[])influencers);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setTimeStep(float timeStep) {
/*  87 */     this.deltaTime = timeStep;
/*  88 */     this.deltaTimeSqr = this.deltaTime * this.deltaTime;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTransform(Matrix4 transform) {
/*  94 */     this.transform.set(transform);
/*  95 */     transform.getScale(this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransform(float x, float y, float z, float qx, float qy, float qz, float qw, float scale) {
/* 100 */     this.transform.set(x, y, z, qx, qy, qz, qw, scale, scale, scale);
/* 101 */     this.scale.set(scale, scale, scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void rotate(Quaternion rotation) {
/* 106 */     this.transform.rotate(rotation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void rotate(Vector3 axis, float angle) {
/* 113 */     this.transform.rotate(axis, angle);
/*     */   }
/*     */ 
/*     */   
/*     */   public void translate(Vector3 translation) {
/* 118 */     this.transform.translate(translation);
/*     */   }
/*     */   
/*     */   public void setTranslation(Vector3 translation) {
/* 122 */     this.transform.setTranslation(translation);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scale(float scaleX, float scaleY, float scaleZ) {
/* 127 */     this.transform.scale(scaleX, scaleY, scaleZ);
/* 128 */     this.transform.getScale(this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void scale(Vector3 scale) {
/* 133 */     scale(scale.x, scale.y, scale.z);
/*     */   }
/*     */ 
/*     */   
/*     */   public void mul(Matrix4 transform) {
/* 138 */     this.transform.mul(transform);
/* 139 */     this.transform.getScale(this.scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public void getTransform(Matrix4 transform) {
/* 144 */     transform.set(this.transform);
/*     */   }
/*     */   
/*     */   public boolean isComplete() {
/* 148 */     return this.emitter.isComplete();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/* 154 */     bind();
/* 155 */     if (this.particles != null) {
/* 156 */       end();
/* 157 */       this.particleChannels.resetIds();
/*     */     } 
/* 159 */     allocateChannels(this.emitter.maxParticleCount);
/*     */     
/* 161 */     this.emitter.init();
/* 162 */     for (Influencer influencer : this.influencers)
/* 163 */       influencer.init(); 
/* 164 */     this.renderer.init();
/*     */   }
/*     */   
/*     */   protected void allocateChannels(int maxParticleCount) {
/* 168 */     this.particles = new ParallelArray(maxParticleCount);
/*     */     
/* 170 */     this.emitter.allocateChannels();
/* 171 */     for (Influencer influencer : this.influencers)
/* 172 */       influencer.allocateChannels(); 
/* 173 */     this.renderer.allocateChannels();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void bind() {
/* 178 */     this.emitter.set(this);
/* 179 */     for (Influencer influencer : this.influencers)
/* 180 */       influencer.set(this); 
/* 181 */     this.renderer.set(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/* 186 */     this.emitter.start();
/* 187 */     for (Influencer influencer : this.influencers) {
/* 188 */       influencer.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void reset() {
/* 193 */     end();
/* 194 */     start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void end() {
/* 199 */     for (Influencer influencer : this.influencers)
/* 200 */       influencer.end(); 
/* 201 */     this.emitter.end();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void activateParticles(int startIndex, int count) {
/* 207 */     this.emitter.activateParticles(startIndex, count);
/* 208 */     for (Influencer influencer : this.influencers) {
/* 209 */       influencer.activateParticles(startIndex, count);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void killParticles(int startIndex, int count) {
/* 215 */     this.emitter.killParticles(startIndex, count);
/* 216 */     for (Influencer influencer : this.influencers) {
/* 217 */       influencer.killParticles(startIndex, count);
/*     */     }
/*     */   }
/*     */   
/*     */   public void update() {
/* 222 */     this.emitter.update();
/* 223 */     for (Influencer influencer : this.influencers) {
/* 224 */       influencer.update();
/*     */     }
/*     */   }
/*     */   
/*     */   public void draw() {
/* 229 */     if (this.particles.size > 0) {
/* 230 */       this.renderer.update();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ParticleController copy() {
/* 236 */     Emitter emitter = (Emitter)this.emitter.copy();
/* 237 */     Influencer[] influencers = new Influencer[this.influencers.size];
/* 238 */     int i = 0;
/* 239 */     for (Influencer influencer : this.influencers) {
/* 240 */       influencers[i++] = (Influencer)influencer.copy();
/*     */     }
/* 242 */     return new ParticleController(new String(this.name), emitter, (ParticleControllerRenderer<?, ?>)this.renderer.copy(), influencers);
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 247 */     this.emitter.dispose();
/* 248 */     for (Influencer influencer : this.influencers) {
/* 249 */       influencer.dispose();
/*     */     }
/*     */   }
/*     */   
/*     */   public BoundingBox getBoundingBox() {
/* 254 */     if (this.boundingBox == null) this.boundingBox = new BoundingBox(); 
/* 255 */     calculateBoundingBox();
/* 256 */     return this.boundingBox;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void calculateBoundingBox() {
/* 261 */     this.boundingBox.clr();
/* 262 */     ParallelArray.FloatChannel positionChannel = this.particles.<ParallelArray.FloatChannel>getChannel(ParticleChannels.Position);
/* 263 */     for (int pos = 0, c = positionChannel.strideSize * this.particles.size; pos < c; pos += positionChannel.strideSize) {
/* 264 */       this.boundingBox.ext(positionChannel.data[pos + 0], positionChannel.data[pos + 1], positionChannel.data[pos + 2]);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private <K extends Influencer> int findIndex(Class<K> type) {
/* 271 */     for (int i = 0; i < this.influencers.size; i++) {
/* 272 */       Influencer influencer = (Influencer)this.influencers.get(i);
/* 273 */       if (ClassReflection.isAssignableFrom(type, influencer.getClass())) {
/* 274 */         return i;
/*     */       }
/*     */     } 
/* 277 */     return -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public <K extends Influencer> K findInfluencer(Class<K> influencerClass) {
/* 282 */     int index = findIndex(influencerClass);
/* 283 */     return (index > -1) ? (K)this.influencers.get(index) : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public <K extends Influencer> void removeInfluencer(Class<K> type) {
/* 288 */     int index = findIndex(type);
/* 289 */     if (index > -1) this.influencers.removeIndex(index);
/*     */   
/*     */   }
/*     */   
/*     */   public <K extends Influencer> boolean replaceInfluencer(Class<K> type, K newInfluencer) {
/* 294 */     int index = findIndex(type);
/* 295 */     if (index > -1) {
/* 296 */       this.influencers.insert(index, newInfluencer);
/* 297 */       this.influencers.removeIndex(index + 1);
/* 298 */       return true;
/*     */     } 
/* 300 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(Json json) {
/* 305 */     json.writeValue("name", this.name);
/* 306 */     json.writeValue("emitter", this.emitter, Emitter.class);
/* 307 */     json.writeValue("influencers", this.influencers, Array.class, Influencer.class);
/* 308 */     json.writeValue("renderer", this.renderer, ParticleControllerRenderer.class);
/*     */   }
/*     */ 
/*     */   
/*     */   public void read(Json json, JsonValue jsonMap) {
/* 313 */     this.name = (String)json.readValue("name", String.class, jsonMap);
/* 314 */     this.emitter = (Emitter)json.readValue("emitter", Emitter.class, jsonMap);
/* 315 */     this.influencers.addAll((Array)json.readValue("influencers", Array.class, Influencer.class, jsonMap));
/* 316 */     this.renderer = (ParticleControllerRenderer<?, ?>)json.readValue("renderer", ParticleControllerRenderer.class, jsonMap);
/*     */   }
/*     */ 
/*     */   
/*     */   public void save(AssetManager manager, ResourceData data) {
/* 321 */     this.emitter.save(manager, data);
/* 322 */     for (Influencer influencer : this.influencers)
/* 323 */       influencer.save(manager, data); 
/* 324 */     this.renderer.save(manager, data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(AssetManager manager, ResourceData data) {
/* 329 */     this.emitter.load(manager, data);
/* 330 */     for (Influencer influencer : this.influencers)
/* 331 */       influencer.load(manager, data); 
/* 332 */     this.renderer.load(manager, data);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\particles\ParticleController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */