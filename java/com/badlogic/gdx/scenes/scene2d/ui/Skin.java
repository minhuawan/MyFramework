/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.BitmapFont;
/*     */ import com.badlogic.gdx.graphics.g2d.NinePatch;
/*     */ import com.badlogic.gdx.graphics.g2d.Sprite;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.Json;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.SerializationException;
/*     */ import com.badlogic.gdx.utils.reflect.ClassReflection;
/*     */ import com.badlogic.gdx.utils.reflect.Method;
/*     */ import com.badlogic.gdx.utils.reflect.ReflectionException;
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
/*     */ public class Skin
/*     */   implements Disposable
/*     */ {
/*  58 */   ObjectMap<Class, ObjectMap<String, Object>> resources = new ObjectMap();
/*     */ 
/*     */   
/*     */   TextureAtlas atlas;
/*     */ 
/*     */ 
/*     */   
/*     */   public Skin() {}
/*     */ 
/*     */   
/*     */   public Skin(FileHandle skinFile) {
/*  69 */     FileHandle atlasFile = skinFile.sibling(skinFile.nameWithoutExtension() + ".atlas");
/*  70 */     if (atlasFile.exists()) {
/*  71 */       this.atlas = new TextureAtlas(atlasFile);
/*  72 */       addRegions(this.atlas);
/*     */     } 
/*     */     
/*  75 */     load(skinFile);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Skin(FileHandle skinFile, TextureAtlas atlas) {
/*  81 */     this.atlas = atlas;
/*  82 */     addRegions(atlas);
/*  83 */     load(skinFile);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Skin(TextureAtlas atlas) {
/*  89 */     this.atlas = atlas;
/*  90 */     addRegions(atlas);
/*     */   }
/*     */ 
/*     */   
/*     */   public void load(FileHandle skinFile) {
/*     */     try {
/*  96 */       getJsonLoader(skinFile).fromJson(Skin.class, skinFile);
/*  97 */     } catch (SerializationException ex) {
/*  98 */       throw new SerializationException("Error reading file: " + skinFile, ex);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void addRegions(TextureAtlas atlas) {
/* 104 */     Array<TextureAtlas.AtlasRegion> regions = atlas.getRegions();
/* 105 */     for (int i = 0, n = regions.size; i < n; i++) {
/* 106 */       TextureAtlas.AtlasRegion region = (TextureAtlas.AtlasRegion)regions.get(i);
/* 107 */       String name = region.name;
/* 108 */       if (region.index != -1) {
/* 109 */         name = name + "_" + region.index;
/*     */       }
/* 111 */       add(name, region, TextureRegion.class);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void add(String name, Object resource) {
/* 116 */     add(name, resource, resource.getClass());
/*     */   }
/*     */   
/*     */   public void add(String name, Object resource, Class<TextureRegion> type) {
/* 120 */     if (name == null) throw new IllegalArgumentException("name cannot be null."); 
/* 121 */     if (resource == null) throw new IllegalArgumentException("resource cannot be null."); 
/* 122 */     ObjectMap<String, Object> typeResources = (ObjectMap<String, Object>)this.resources.get(type);
/* 123 */     if (typeResources == null) {
/* 124 */       typeResources = new ObjectMap((type == TextureRegion.class || type == Drawable.class || type == Sprite.class) ? 256 : 64);
/* 125 */       this.resources.put(type, typeResources);
/*     */     } 
/* 127 */     typeResources.put(name, resource);
/*     */   }
/*     */   
/*     */   public void remove(String name, Class type) {
/* 131 */     if (name == null) throw new IllegalArgumentException("name cannot be null."); 
/* 132 */     ObjectMap<String, Object> typeResources = (ObjectMap<String, Object>)this.resources.get(type);
/* 133 */     typeResources.remove(name);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(Class<T> type) {
/* 139 */     return get("default", type);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T get(String name, Class<T> type) {
/* 145 */     if (name == null) throw new IllegalArgumentException("name cannot be null."); 
/* 146 */     if (type == null) throw new IllegalArgumentException("type cannot be null.");
/*     */     
/* 148 */     if (type == Drawable.class) return (T)getDrawable(name); 
/* 149 */     if (type == TextureRegion.class) return (T)getRegion(name); 
/* 150 */     if (type == NinePatch.class) return (T)getPatch(name); 
/* 151 */     if (type == Sprite.class) return (T)getSprite(name);
/*     */     
/* 153 */     ObjectMap<String, Object> typeResources = (ObjectMap<String, Object>)this.resources.get(type);
/* 154 */     if (typeResources == null) throw new GdxRuntimeException("No " + type.getName() + " registered with name: " + name); 
/* 155 */     Object resource = typeResources.get(name);
/* 156 */     if (resource == null) throw new GdxRuntimeException("No " + type.getName() + " registered with name: " + name); 
/* 157 */     return (T)resource;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <T> T optional(String name, Class<T> type) {
/* 163 */     if (name == null) throw new IllegalArgumentException("name cannot be null."); 
/* 164 */     if (type == null) throw new IllegalArgumentException("type cannot be null."); 
/* 165 */     ObjectMap<String, Object> typeResources = (ObjectMap<String, Object>)this.resources.get(type);
/* 166 */     if (typeResources == null) return null; 
/* 167 */     return (T)typeResources.get(name);
/*     */   }
/*     */   
/*     */   public boolean has(String name, Class type) {
/* 171 */     ObjectMap<String, Object> typeResources = (ObjectMap<String, Object>)this.resources.get(type);
/* 172 */     if (typeResources == null) return false; 
/* 173 */     return typeResources.containsKey(name);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> ObjectMap<String, T> getAll(Class<T> type) {
/* 178 */     return (ObjectMap<String, T>)this.resources.get(type);
/*     */   }
/*     */   
/*     */   public Color getColor(String name) {
/* 182 */     return get(name, Color.class);
/*     */   }
/*     */   
/*     */   public BitmapFont getFont(String name) {
/* 186 */     return get(name, BitmapFont.class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureRegion getRegion(String name) {
/* 192 */     TextureRegion region = optional(name, TextureRegion.class);
/* 193 */     if (region != null) return region;
/*     */     
/* 195 */     Texture texture = optional(name, Texture.class);
/* 196 */     if (texture == null) throw new GdxRuntimeException("No TextureRegion or Texture registered with name: " + name); 
/* 197 */     region = new TextureRegion(texture);
/* 198 */     add(name, region, TextureRegion.class);
/* 199 */     return region;
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<TextureRegion> getRegions(String regionName) {
/* 204 */     Array<TextureRegion> regions = null;
/* 205 */     int i = 0;
/* 206 */     TextureRegion region = optional(regionName + "_" + i++, TextureRegion.class);
/* 207 */     if (region != null) {
/* 208 */       regions = new Array();
/* 209 */       while (region != null) {
/* 210 */         regions.add(region);
/* 211 */         region = optional(regionName + "_" + i++, TextureRegion.class);
/*     */       } 
/*     */     } 
/* 214 */     return regions;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TiledDrawable getTiledDrawable(String name) {
/* 220 */     TiledDrawable tiled = optional(name, TiledDrawable.class);
/* 221 */     if (tiled != null) return tiled;
/*     */     
/* 223 */     tiled = new TiledDrawable(getRegion(name));
/* 224 */     tiled.setName(name);
/* 225 */     add(name, tiled, TiledDrawable.class);
/* 226 */     return tiled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NinePatch getPatch(String name) {
/* 233 */     NinePatch patch = optional(name, NinePatch.class);
/* 234 */     if (patch != null) return patch;
/*     */     
/*     */     try {
/* 237 */       TextureRegion region = getRegion(name);
/* 238 */       if (region instanceof TextureAtlas.AtlasRegion) {
/* 239 */         int[] splits = ((TextureAtlas.AtlasRegion)region).splits;
/* 240 */         if (splits != null) {
/* 241 */           patch = new NinePatch(region, splits[0], splits[1], splits[2], splits[3]);
/* 242 */           int[] pads = ((TextureAtlas.AtlasRegion)region).pads;
/* 243 */           if (pads != null) patch.setPadding(pads[0], pads[1], pads[2], pads[3]); 
/*     */         } 
/*     */       } 
/* 246 */       if (patch == null) patch = new NinePatch(region); 
/* 247 */       add(name, patch, NinePatch.class);
/* 248 */       return patch;
/* 249 */     } catch (GdxRuntimeException ex) {
/* 250 */       throw new GdxRuntimeException("No NinePatch, TextureRegion, or Texture registered with name: " + name);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Sprite getSprite(String name) {
/* 258 */     Sprite sprite = optional(name, Sprite.class);
/* 259 */     if (sprite != null) return sprite;  try {
/*     */       TextureAtlas.AtlasSprite atlasSprite;
/*     */       Sprite sprite1;
/* 262 */       TextureRegion textureRegion = getRegion(name);
/* 263 */       if (textureRegion instanceof TextureAtlas.AtlasRegion) {
/* 264 */         TextureAtlas.AtlasRegion region = (TextureAtlas.AtlasRegion)textureRegion;
/* 265 */         if (region.rotate || region.packedWidth != region.originalWidth || region.packedHeight != region.originalHeight)
/* 266 */           atlasSprite = new TextureAtlas.AtlasSprite(region); 
/*     */       } 
/* 268 */       if (atlasSprite == null) sprite1 = new Sprite(textureRegion); 
/* 269 */       add(name, sprite1, Sprite.class);
/* 270 */       return sprite1;
/* 271 */     } catch (GdxRuntimeException ex) {
/* 272 */       throw new GdxRuntimeException("No NinePatch, TextureRegion, or Texture registered with name: " + name);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Drawable getDrawable(String name) {
/*     */     TextureRegionDrawable textureRegionDrawable;
/*     */     SpriteDrawable spriteDrawable;
/* 279 */     Drawable drawable = optional(name, Drawable.class);
/* 280 */     if (drawable != null) return drawable;
/*     */ 
/*     */     
/*     */     try {
/* 284 */       TextureRegion textureRegion = getRegion(name);
/* 285 */       if (textureRegion instanceof TextureAtlas.AtlasRegion) {
/* 286 */         TextureAtlas.AtlasRegion region = (TextureAtlas.AtlasRegion)textureRegion;
/* 287 */         if (region.splits != null) {
/* 288 */           NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(getPatch(name));
/* 289 */         } else if (region.rotate || region.packedWidth != region.originalWidth || region.packedHeight != region.originalHeight) {
/* 290 */           spriteDrawable = new SpriteDrawable(getSprite(name));
/*     */         } 
/* 292 */       }  if (spriteDrawable == null) textureRegionDrawable = new TextureRegionDrawable(textureRegion); 
/* 293 */     } catch (GdxRuntimeException gdxRuntimeException) {}
/*     */ 
/*     */ 
/*     */     
/* 297 */     if (textureRegionDrawable == null) {
/* 298 */       NinePatch patch = optional(name, NinePatch.class);
/* 299 */       if (patch != null) {
/* 300 */         NinePatchDrawable ninePatchDrawable = new NinePatchDrawable(patch);
/*     */       } else {
/* 302 */         Sprite sprite = optional(name, Sprite.class);
/* 303 */         if (sprite != null) {
/* 304 */           spriteDrawable = new SpriteDrawable(sprite);
/*     */         } else {
/* 306 */           throw new GdxRuntimeException("No Drawable, NinePatch, TextureRegion, Texture, or Sprite registered with name: " + name);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 311 */     if (spriteDrawable instanceof BaseDrawable) ((BaseDrawable)spriteDrawable).setName(name);
/*     */     
/* 313 */     add(name, spriteDrawable, Drawable.class);
/* 314 */     return (Drawable)spriteDrawable;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String find(Object resource) {
/* 320 */     if (resource == null) throw new IllegalArgumentException("style cannot be null."); 
/* 321 */     ObjectMap<String, Object> typeResources = (ObjectMap<String, Object>)this.resources.get(resource.getClass());
/* 322 */     if (typeResources == null) return null; 
/* 323 */     return (String)typeResources.findKey(resource, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public Drawable newDrawable(String name) {
/* 328 */     return newDrawable(getDrawable(name));
/*     */   }
/*     */ 
/*     */   
/*     */   public Drawable newDrawable(String name, float r, float g, float b, float a) {
/* 333 */     return newDrawable(getDrawable(name), new Color(r, g, b, a));
/*     */   }
/*     */ 
/*     */   
/*     */   public Drawable newDrawable(String name, Color tint) {
/* 338 */     return newDrawable(getDrawable(name), tint);
/*     */   }
/*     */ 
/*     */   
/*     */   public Drawable newDrawable(Drawable drawable) {
/* 343 */     if (drawable instanceof TiledDrawable) return (Drawable)new TiledDrawable((TextureRegionDrawable)drawable); 
/* 344 */     if (drawable instanceof TextureRegionDrawable) return (Drawable)new TextureRegionDrawable((TextureRegionDrawable)drawable); 
/* 345 */     if (drawable instanceof NinePatchDrawable) return (Drawable)new NinePatchDrawable((NinePatchDrawable)drawable); 
/* 346 */     if (drawable instanceof SpriteDrawable) return (Drawable)new SpriteDrawable((SpriteDrawable)drawable); 
/* 347 */     throw new GdxRuntimeException("Unable to copy, unknown drawable type: " + drawable.getClass());
/*     */   }
/*     */ 
/*     */   
/*     */   public Drawable newDrawable(Drawable drawable, float r, float g, float b, float a) {
/* 352 */     return newDrawable(drawable, new Color(r, g, b, a));
/*     */   }
/*     */ 
/*     */   
/*     */   public Drawable newDrawable(Drawable drawable, Color tint) {
/*     */     SpriteDrawable spriteDrawable;
/* 358 */     if (drawable instanceof TextureRegionDrawable) {
/* 359 */       Drawable newDrawable = ((TextureRegionDrawable)drawable).tint(tint);
/* 360 */     } else if (drawable instanceof NinePatchDrawable) {
/* 361 */       NinePatchDrawable ninePatchDrawable = ((NinePatchDrawable)drawable).tint(tint);
/* 362 */     } else if (drawable instanceof SpriteDrawable) {
/* 363 */       spriteDrawable = ((SpriteDrawable)drawable).tint(tint);
/*     */     } else {
/* 365 */       throw new GdxRuntimeException("Unable to copy, unknown drawable type: " + drawable.getClass());
/*     */     } 
/* 367 */     if (spriteDrawable instanceof BaseDrawable) {
/* 368 */       BaseDrawable named = (BaseDrawable)spriteDrawable;
/* 369 */       if (drawable instanceof BaseDrawable) {
/* 370 */         named.setName(((BaseDrawable)drawable).getName() + " (" + tint + ")");
/*     */       } else {
/* 372 */         named.setName(" (" + tint + ")");
/*     */       } 
/*     */     } 
/* 375 */     return (Drawable)spriteDrawable;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEnabled(Actor actor, boolean enabled) {
/* 384 */     Method method = findMethod(actor.getClass(), "getStyle");
/* 385 */     if (method == null)
/*     */       return; 
/*     */     try {
/* 388 */       style = method.invoke(actor, new Object[0]);
/* 389 */     } catch (Exception ignored) {
/*     */       return;
/*     */     } 
/*     */     
/* 393 */     String name = find(style);
/* 394 */     if (name == null)
/* 395 */       return;  name = name.replace("-disabled", "") + (enabled ? "" : "-disabled");
/* 396 */     Object style = get(name, style.getClass());
/*     */     
/* 398 */     method = findMethod(actor.getClass(), "setStyle");
/* 399 */     if (method == null)
/*     */       return;  try {
/* 401 */       method.invoke(actor, new Object[] { style });
/* 402 */     } catch (Exception exception) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureAtlas getAtlas() {
/* 408 */     return this.atlas;
/*     */   }
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 413 */     if (this.atlas != null) this.atlas.dispose(); 
/* 414 */     for (ObjectMap.Values<ObjectMap<String, Object>> values = this.resources.values().iterator(); values.hasNext(); ) { ObjectMap<String, Object> entry = values.next();
/* 415 */       for (ObjectMap.Values values1 = entry.values().iterator(); values1.hasNext(); ) { Object resource = values1.next();
/* 416 */         if (resource instanceof Disposable) ((Disposable)resource).dispose();  }
/*     */        }
/*     */   
/*     */   }
/*     */   protected Json getJsonLoader(final FileHandle skinFile) {
/* 421 */     final Skin skin = this;
/*     */     
/* 423 */     Json json = new Json()
/*     */       {
/*     */         public <T> T readValue(Class<T> type, Class elementType, JsonValue jsonData) {
/* 426 */           if (jsonData.isString() && !ClassReflection.isAssignableFrom(CharSequence.class, type))
/* 427 */             return Skin.this.get(jsonData.asString(), type); 
/* 428 */           return (T)super.readValue(type, elementType, jsonData);
/*     */         }
/*     */       };
/* 431 */     json.setTypeName(null);
/* 432 */     json.setUsePrototypes(false);
/*     */     
/* 434 */     json.setSerializer(Skin.class, (Json.Serializer)new Json.ReadOnlySerializer<Skin>() {
/*     */           public Skin read(Json json, JsonValue typeToValueMap, Class ignored) {
/* 436 */             for (JsonValue valueMap = typeToValueMap.child; valueMap != null; valueMap = valueMap.next) {
/*     */               try {
/* 438 */                 readNamedObjects(json, ClassReflection.forName(valueMap.name()), valueMap);
/* 439 */               } catch (ReflectionException ex) {
/* 440 */                 throw new SerializationException(ex);
/*     */               } 
/*     */             } 
/* 443 */             return skin;
/*     */           }
/*     */           
/*     */           private void readNamedObjects(Json json, Class<Skin.TintedDrawable> type, JsonValue valueMap) {
/* 447 */             Class<Drawable> addType = (type == Skin.TintedDrawable.class) ? Drawable.class : type;
/* 448 */             for (JsonValue valueEntry = valueMap.child; valueEntry != null; valueEntry = valueEntry.next) {
/* 449 */               Object object = json.readValue(type, valueEntry);
/* 450 */               if (object != null) {
/*     */                 try {
/* 452 */                   Skin.this.add(valueEntry.name, object, addType);
/* 453 */                   if (addType != Drawable.class && ClassReflection.isAssignableFrom(Drawable.class, addType))
/* 454 */                     Skin.this.add(valueEntry.name, object, Drawable.class); 
/* 455 */                 } catch (Exception ex) {
/* 456 */                   throw new SerializationException("Error reading " + 
/* 457 */                       ClassReflection.getSimpleName(type) + ": " + valueEntry.name, ex);
/*     */                 } 
/*     */               }
/*     */             } 
/*     */           }
/*     */         });
/* 463 */     json.setSerializer(BitmapFont.class, (Json.Serializer)new Json.ReadOnlySerializer<BitmapFont>() {
/*     */           public BitmapFont read(Json json, JsonValue jsonData, Class type) {
/* 465 */             String path = (String)json.readValue("file", String.class, jsonData);
/* 466 */             int scaledSize = ((Integer)json.readValue("scaledSize", int.class, Integer.valueOf(-1), jsonData)).intValue();
/* 467 */             Boolean flip = (Boolean)json.readValue("flip", Boolean.class, Boolean.valueOf(false), jsonData);
/* 468 */             Boolean markupEnabled = (Boolean)json.readValue("markupEnabled", Boolean.class, Boolean.valueOf(false), jsonData);
/*     */             
/* 470 */             FileHandle fontFile = skinFile.parent().child(path);
/* 471 */             if (!fontFile.exists()) fontFile = Gdx.files.internal(path); 
/* 472 */             if (!fontFile.exists()) throw new SerializationException("Font file not found: " + fontFile);
/*     */ 
/*     */             
/* 475 */             String regionName = fontFile.nameWithoutExtension();
/*     */             try {
/*     */               BitmapFont font;
/* 478 */               Array<TextureRegion> regions = skin.getRegions(regionName);
/* 479 */               if (regions != null) {
/* 480 */                 font = new BitmapFont(new BitmapFont.BitmapFontData(fontFile, flip.booleanValue()), regions, true);
/*     */               } else {
/* 482 */                 TextureRegion region = skin.<TextureRegion>optional(regionName, TextureRegion.class);
/* 483 */                 if (region != null) {
/* 484 */                   font = new BitmapFont(fontFile, region, flip.booleanValue());
/*     */                 } else {
/* 486 */                   FileHandle imageFile = fontFile.parent().child(regionName + ".png");
/* 487 */                   if (imageFile.exists()) {
/* 488 */                     font = new BitmapFont(fontFile, imageFile, flip.booleanValue());
/*     */                   } else {
/* 490 */                     font = new BitmapFont(fontFile, flip.booleanValue());
/*     */                   } 
/*     */                 } 
/* 493 */               }  (font.getData()).markupEnabled = markupEnabled.booleanValue();
/*     */               
/* 495 */               if (scaledSize != -1) font.getData().setScale(scaledSize / font.getCapHeight()); 
/* 496 */               return font;
/* 497 */             } catch (RuntimeException ex) {
/* 498 */               throw new SerializationException("Error loading bitmap font: " + fontFile, ex);
/*     */             } 
/*     */           }
/*     */         });
/*     */     
/* 503 */     json.setSerializer(Color.class, (Json.Serializer)new Json.ReadOnlySerializer<Color>() {
/*     */           public Color read(Json json, JsonValue jsonData, Class type) {
/* 505 */             if (jsonData.isString()) return Skin.this.<Color>get(jsonData.asString(), Color.class); 
/* 506 */             String hex = (String)json.readValue("hex", String.class, null, jsonData);
/* 507 */             if (hex != null) return Color.valueOf(hex); 
/* 508 */             float r = ((Float)json.readValue("r", float.class, Float.valueOf(0.0F), jsonData)).floatValue();
/* 509 */             float g = ((Float)json.readValue("g", float.class, Float.valueOf(0.0F), jsonData)).floatValue();
/* 510 */             float b = ((Float)json.readValue("b", float.class, Float.valueOf(0.0F), jsonData)).floatValue();
/* 511 */             float a = ((Float)json.readValue("a", float.class, Float.valueOf(1.0F), jsonData)).floatValue();
/* 512 */             return new Color(r, g, b, a);
/*     */           }
/*     */         });
/*     */     
/* 516 */     json.setSerializer(TintedDrawable.class, (Json.Serializer)new Json.ReadOnlySerializer() {
/*     */           public Object read(Json json, JsonValue jsonData, Class type) {
/* 518 */             String name = (String)json.readValue("name", String.class, jsonData);
/* 519 */             Color color = (Color)json.readValue("color", Color.class, jsonData);
/* 520 */             Drawable drawable = Skin.this.newDrawable(name, color);
/* 521 */             if (drawable instanceof BaseDrawable) {
/* 522 */               BaseDrawable named = (BaseDrawable)drawable;
/* 523 */               named.setName(jsonData.name + " (" + name + ", " + color + ")");
/*     */             } 
/* 525 */             return drawable;
/*     */           }
/*     */         });
/*     */     
/* 529 */     return json;
/*     */   }
/*     */   
/*     */   private static Method findMethod(Class type, String name) {
/* 533 */     Method[] methods = ClassReflection.getMethods(type);
/* 534 */     for (int i = 0, n = methods.length; i < n; i++) {
/* 535 */       Method method = methods[i];
/* 536 */       if (method.getName().equals(name)) return method; 
/*     */     } 
/* 538 */     return null;
/*     */   }
/*     */   
/*     */   public static class TintedDrawable {
/*     */     public String name;
/*     */     public Color color;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Skin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */