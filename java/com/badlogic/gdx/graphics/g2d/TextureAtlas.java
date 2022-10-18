/*     */ package com.badlogic.gdx.graphics.g2d;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Pixmap;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.Disposable;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.ObjectSet;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Comparator;
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
/*     */ public class TextureAtlas
/*     */   implements Disposable
/*     */ {
/*  50 */   static final String[] tuple = new String[4];
/*     */   
/*  52 */   private final ObjectSet<Texture> textures = new ObjectSet(4);
/*  53 */   private final Array<AtlasRegion> regions = new Array();
/*     */   
/*     */   public TextureAtlas() {}
/*     */   
/*     */   public static class Page {
/*     */     public final FileHandle textureFile;
/*     */     public Texture texture;
/*     */     public final float width;
/*     */     public final float height;
/*     */     public final boolean useMipMaps;
/*     */     public final Pixmap.Format format;
/*     */     public final Texture.TextureFilter minFilter;
/*     */     public final Texture.TextureFilter magFilter;
/*     */     public final Texture.TextureWrap uWrap;
/*     */     public final Texture.TextureWrap vWrap;
/*     */     
/*  69 */     public Page(FileHandle handle, float width, float height, boolean useMipMaps, Pixmap.Format format, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, Texture.TextureWrap uWrap, Texture.TextureWrap vWrap) { this.width = width;
/*  70 */       this.height = height;
/*  71 */       this.textureFile = handle;
/*  72 */       this.useMipMaps = useMipMaps;
/*  73 */       this.format = format;
/*  74 */       this.minFilter = minFilter;
/*  75 */       this.magFilter = magFilter;
/*  76 */       this.uWrap = uWrap;
/*  77 */       this.vWrap = vWrap; } } public static class TextureAtlasData { public static class Page { public final FileHandle textureFile; public Texture texture; public final float width; public final float height; public final boolean useMipMaps; public Page(FileHandle handle, float width, float height, boolean useMipMaps, Pixmap.Format format, Texture.TextureFilter minFilter, Texture.TextureFilter magFilter, Texture.TextureWrap uWrap, Texture.TextureWrap vWrap) { this.width = width; this.height = height; this.textureFile = handle; this.useMipMaps = useMipMaps; this.format = format; this.minFilter = minFilter; this.magFilter = magFilter; this.uWrap = uWrap; this.vWrap = vWrap; }
/*     */       
/*     */       public final Pixmap.Format format; public final Texture.TextureFilter minFilter; public final Texture.TextureFilter magFilter;
/*     */       public final Texture.TextureWrap uWrap;
/*     */       public final Texture.TextureWrap vWrap; }
/*     */ 
/*     */     
/*     */     public static class Region { public TextureAtlas.TextureAtlasData.Page page;
/*     */       public int index;
/*     */       public String name;
/*     */       public float offsetX;
/*     */       public float offsetY;
/*     */       public int originalWidth;
/*     */       public int originalHeight;
/*     */       public boolean rotate;
/*     */       public int left;
/*     */       public int top;
/*     */       public int width;
/*     */       public int height;
/*     */       public boolean flip;
/*     */       public int[] splits;
/*     */       public int[] pads; }
/*  99 */     final Array<Page> pages = new Array();
/* 100 */     final Array<Region> regions = new Array();
/*     */     
/*     */     public TextureAtlasData(FileHandle packFile, FileHandle imagesDir, boolean flip) {
/* 103 */       BufferedReader reader = new BufferedReader(new InputStreamReader(packFile.read()), 64);
/*     */       try {
/* 105 */         Page pageImage = null;
/*     */         while (true) {
/* 107 */           String line = reader.readLine();
/* 108 */           if (line == null)
/* 109 */             break;  if (line.trim().length() == 0) {
/* 110 */             pageImage = null; continue;
/* 111 */           }  if (pageImage == null) {
/* 112 */             FileHandle file = imagesDir.child(line);
/*     */             
/* 114 */             float f1 = 0.0F, f2 = 0.0F;
/* 115 */             if (TextureAtlas.readTuple(reader) == 2) {
/* 116 */               f1 = Integer.parseInt(TextureAtlas.tuple[0]);
/* 117 */               f2 = Integer.parseInt(TextureAtlas.tuple[1]);
/* 118 */               TextureAtlas.readTuple(reader);
/*     */             } 
/* 120 */             Pixmap.Format format = Pixmap.Format.valueOf(TextureAtlas.tuple[0]);
/*     */             
/* 122 */             TextureAtlas.readTuple(reader);
/* 123 */             Texture.TextureFilter min = Texture.TextureFilter.valueOf(TextureAtlas.tuple[0]);
/* 124 */             Texture.TextureFilter max = Texture.TextureFilter.valueOf(TextureAtlas.tuple[1]);
/*     */             
/* 126 */             String direction = TextureAtlas.readValue(reader);
/* 127 */             Texture.TextureWrap repeatX = Texture.TextureWrap.ClampToEdge;
/* 128 */             Texture.TextureWrap repeatY = Texture.TextureWrap.ClampToEdge;
/* 129 */             if (direction.equals("x")) {
/* 130 */               repeatX = Texture.TextureWrap.Repeat;
/* 131 */             } else if (direction.equals("y")) {
/* 132 */               repeatY = Texture.TextureWrap.Repeat;
/* 133 */             } else if (direction.equals("xy")) {
/* 134 */               repeatX = Texture.TextureWrap.Repeat;
/* 135 */               repeatY = Texture.TextureWrap.Repeat;
/*     */             } 
/*     */             
/* 138 */             pageImage = new Page(file, f1, f2, min.isMipMap(), format, min, max, repeatX, repeatY);
/* 139 */             this.pages.add(pageImage); continue;
/*     */           } 
/* 141 */           boolean rotate = Boolean.valueOf(TextureAtlas.readValue(reader)).booleanValue();
/*     */           
/* 143 */           TextureAtlas.readTuple(reader);
/* 144 */           int left = Integer.parseInt(TextureAtlas.tuple[0]);
/* 145 */           int top = Integer.parseInt(TextureAtlas.tuple[1]);
/*     */           
/* 147 */           TextureAtlas.readTuple(reader);
/* 148 */           int width = Integer.parseInt(TextureAtlas.tuple[0]);
/* 149 */           int height = Integer.parseInt(TextureAtlas.tuple[1]);
/*     */           
/* 151 */           Region region = new Region();
/* 152 */           region.page = pageImage;
/* 153 */           region.left = left;
/* 154 */           region.top = top;
/* 155 */           region.width = width;
/* 156 */           region.height = height;
/* 157 */           region.name = line;
/* 158 */           region.rotate = rotate;
/*     */           
/* 160 */           if (TextureAtlas.readTuple(reader) == 4) {
/* 161 */             region
/* 162 */               .splits = new int[] { Integer.parseInt(TextureAtlas.tuple[0]), Integer.parseInt(TextureAtlas.tuple[1]), Integer.parseInt(TextureAtlas.tuple[2]), Integer.parseInt(TextureAtlas.tuple[3]) };
/*     */             
/* 164 */             if (TextureAtlas.readTuple(reader) == 4) {
/* 165 */               region
/* 166 */                 .pads = new int[] { Integer.parseInt(TextureAtlas.tuple[0]), Integer.parseInt(TextureAtlas.tuple[1]), Integer.parseInt(TextureAtlas.tuple[2]), Integer.parseInt(TextureAtlas.tuple[3]) };
/*     */               
/* 168 */               TextureAtlas.readTuple(reader);
/*     */             } 
/*     */           } 
/*     */           
/* 172 */           region.originalWidth = Integer.parseInt(TextureAtlas.tuple[0]);
/* 173 */           region.originalHeight = Integer.parseInt(TextureAtlas.tuple[1]);
/*     */           
/* 175 */           TextureAtlas.readTuple(reader);
/* 176 */           region.offsetX = Integer.parseInt(TextureAtlas.tuple[0]);
/* 177 */           region.offsetY = Integer.parseInt(TextureAtlas.tuple[1]);
/*     */           
/* 179 */           region.index = Integer.parseInt(TextureAtlas.readValue(reader));
/*     */           
/* 181 */           if (flip) region.flip = true;
/*     */           
/* 183 */           this.regions.add(region);
/*     */         }
/*     */       
/* 186 */       } catch (Exception ex) {
/* 187 */         throw new GdxRuntimeException("Error reading pack file: " + packFile, ex);
/*     */       } finally {
/* 189 */         StreamUtils.closeQuietly(reader);
/*     */       } 
/*     */       
/* 192 */       this.regions.sort(TextureAtlas.indexComparator);
/*     */     }
/*     */     
/*     */     public Array<Page> getPages() {
/* 196 */       return this.pages;
/*     */     }
/*     */     
/*     */     public Array<Region> getRegions() {
/* 200 */       return this.regions;
/*     */     } }
/*     */   public static class Region { public TextureAtlas.TextureAtlasData.Page page; public int index; public String name; public float offsetX; public float offsetY; public int originalWidth; public int originalHeight; public boolean rotate; public int left;
/*     */     public int top;
/*     */     public int width;
/*     */     public int height;
/*     */     public boolean flip;
/*     */     public int[] splits;
/*     */     public int[] pads; }
/*     */   
/*     */   public TextureAtlas(String internalPackFile) {
/* 211 */     this(Gdx.files.internal(internalPackFile));
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlas(FileHandle packFile) {
/* 216 */     this(packFile, packFile.parent());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TextureAtlas(FileHandle packFile, boolean flip) {
/* 222 */     this(packFile, packFile.parent(), flip);
/*     */   }
/*     */   
/*     */   public TextureAtlas(FileHandle packFile, FileHandle imagesDir) {
/* 226 */     this(packFile, imagesDir, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlas(FileHandle packFile, FileHandle imagesDir, boolean flip) {
/* 231 */     this(new TextureAtlasData(packFile, imagesDir, flip));
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlas(TextureAtlasData data) {
/* 236 */     if (data != null) load(data); 
/*     */   }
/*     */   
/*     */   private void load(TextureAtlasData data) {
/* 240 */     ObjectMap<TextureAtlasData.Page, Texture> pageToTexture = new ObjectMap();
/* 241 */     for (TextureAtlasData.Page page : data.pages) {
/* 242 */       Texture texture = null;
/* 243 */       if (page.texture == null) {
/* 244 */         texture = new Texture(page.textureFile, page.format, page.useMipMaps);
/* 245 */         texture.setFilter(page.minFilter, page.magFilter);
/* 246 */         texture.setWrap(page.uWrap, page.vWrap);
/*     */       } else {
/* 248 */         texture = page.texture;
/* 249 */         texture.setFilter(page.minFilter, page.magFilter);
/* 250 */         texture.setWrap(page.uWrap, page.vWrap);
/*     */       } 
/* 252 */       this.textures.add(texture);
/* 253 */       pageToTexture.put(page, texture);
/*     */     } 
/*     */     
/* 256 */     for (TextureAtlasData.Region region : data.regions) {
/* 257 */       int width = region.width;
/* 258 */       int height = region.height;
/* 259 */       AtlasRegion atlasRegion = new AtlasRegion((Texture)pageToTexture.get(region.page), region.left, region.top, region.rotate ? height : width, region.rotate ? width : height);
/*     */       
/* 261 */       atlasRegion.index = region.index;
/* 262 */       atlasRegion.name = region.name;
/* 263 */       atlasRegion.offsetX = region.offsetX;
/* 264 */       atlasRegion.offsetY = region.offsetY;
/* 265 */       atlasRegion.originalHeight = region.originalHeight;
/* 266 */       atlasRegion.originalWidth = region.originalWidth;
/* 267 */       atlasRegion.rotate = region.rotate;
/* 268 */       atlasRegion.splits = region.splits;
/* 269 */       atlasRegion.pads = region.pads;
/* 270 */       if (region.flip) atlasRegion.flip(false, true); 
/* 271 */       this.regions.add(atlasRegion);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public AtlasRegion addRegion(String name, Texture texture, int x, int y, int width, int height) {
/* 277 */     this.textures.add(texture);
/* 278 */     AtlasRegion region = new AtlasRegion(texture, x, y, width, height);
/* 279 */     region.name = name;
/* 280 */     region.originalWidth = width;
/* 281 */     region.originalHeight = height;
/* 282 */     region.index = -1;
/* 283 */     this.regions.add(region);
/* 284 */     return region;
/*     */   }
/*     */ 
/*     */   
/*     */   public AtlasRegion addRegion(String name, TextureRegion textureRegion) {
/* 289 */     return addRegion(name, textureRegion.texture, textureRegion.getRegionX(), textureRegion.getRegionY(), textureRegion
/* 290 */         .getRegionWidth(), textureRegion.getRegionHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<AtlasRegion> getRegions() {
/* 295 */     return this.regions;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtlasRegion findRegion(String name) {
/* 302 */     for (int i = 0, n = this.regions.size; i < n; i++) {
/* 303 */       if (((AtlasRegion)this.regions.get(i)).name.equals(name)) return (AtlasRegion)this.regions.get(i); 
/* 304 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public AtlasRegion findRegion(String name, int index) {
/* 311 */     for (int i = 0, n = this.regions.size; i < n; ) {
/* 312 */       AtlasRegion region = (AtlasRegion)this.regions.get(i);
/* 313 */       if (!region.name.equals(name) || 
/* 314 */         region.index != index) { i++; continue; }
/* 315 */        return region;
/*     */     } 
/* 317 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<AtlasRegion> findRegions(String name) {
/* 323 */     Array<AtlasRegion> matched = new Array();
/* 324 */     for (int i = 0, n = this.regions.size; i < n; i++) {
/* 325 */       AtlasRegion region = (AtlasRegion)this.regions.get(i);
/* 326 */       if (region.name.equals(name)) matched.add(new AtlasRegion(region)); 
/*     */     } 
/* 328 */     return matched;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<Sprite> createSprites() {
/* 335 */     Array<Sprite> sprites = new Array(this.regions.size);
/* 336 */     for (int i = 0, n = this.regions.size; i < n; i++)
/* 337 */       sprites.add(newSprite((AtlasRegion)this.regions.get(i))); 
/* 338 */     return sprites;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Sprite createSprite(String name) {
/* 346 */     for (int i = 0, n = this.regions.size; i < n; i++) {
/* 347 */       if (((AtlasRegion)this.regions.get(i)).name.equals(name)) return newSprite((AtlasRegion)this.regions.get(i)); 
/* 348 */     }  return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Sprite createSprite(String name, int index) {
/* 356 */     for (int i = 0, n = this.regions.size; i < n; ) {
/* 357 */       AtlasRegion region = (AtlasRegion)this.regions.get(i);
/* 358 */       if (!region.name.equals(name) || 
/* 359 */         region.index != index) { i++; continue; }
/* 360 */        return newSprite((AtlasRegion)this.regions.get(i));
/*     */     } 
/* 362 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<Sprite> createSprites(String name) {
/* 370 */     Array<Sprite> matched = new Array();
/* 371 */     for (int i = 0, n = this.regions.size; i < n; i++) {
/* 372 */       AtlasRegion region = (AtlasRegion)this.regions.get(i);
/* 373 */       if (region.name.equals(name)) matched.add(newSprite(region)); 
/*     */     } 
/* 375 */     return matched;
/*     */   }
/*     */   
/*     */   private Sprite newSprite(AtlasRegion region) {
/* 379 */     if (region.packedWidth == region.originalWidth && region.packedHeight == region.originalHeight) {
/* 380 */       if (region.rotate) {
/* 381 */         Sprite sprite = new Sprite(region);
/* 382 */         sprite.setBounds(0.0F, 0.0F, region.getRegionHeight(), region.getRegionWidth());
/* 383 */         sprite.rotate90(true);
/* 384 */         return sprite;
/*     */       } 
/* 386 */       return new Sprite(region);
/*     */     } 
/* 388 */     return new AtlasSprite(region);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public NinePatch createPatch(String name) {
/* 396 */     for (int i = 0, n = this.regions.size; i < n; i++) {
/* 397 */       AtlasRegion region = (AtlasRegion)this.regions.get(i);
/* 398 */       if (region.name.equals(name)) {
/* 399 */         int[] splits = region.splits;
/* 400 */         if (splits == null) throw new IllegalArgumentException("Region does not have ninepatch splits: " + name); 
/* 401 */         NinePatch patch = new NinePatch(region, splits[0], splits[1], splits[2], splits[3]);
/* 402 */         if (region.pads != null) patch.setPadding(region.pads[0], region.pads[1], region.pads[2], region.pads[3]); 
/* 403 */         return patch;
/*     */       } 
/*     */     } 
/* 406 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public ObjectSet<Texture> getTextures() {
/* 411 */     return this.textures;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void dispose() {
/* 417 */     for (ObjectSet.ObjectSetIterator<Texture> objectSetIterator = this.textures.iterator(); objectSetIterator.hasNext(); ) { Texture texture = objectSetIterator.next();
/* 418 */       texture.dispose(); }
/* 419 */      this.textures.clear();
/*     */   }
/*     */   
/* 422 */   static final Comparator<TextureAtlasData.Region> indexComparator = new Comparator<TextureAtlasData.Region>() {
/*     */       public int compare(TextureAtlas.TextureAtlasData.Region region1, TextureAtlas.TextureAtlasData.Region region2) {
/* 424 */         int i1 = region1.index;
/* 425 */         if (i1 == -1) i1 = Integer.MAX_VALUE; 
/* 426 */         int i2 = region2.index;
/* 427 */         if (i2 == -1) i2 = Integer.MAX_VALUE; 
/* 428 */         return i1 - i2;
/*     */       }
/*     */     };
/*     */   
/*     */   static String readValue(BufferedReader reader) throws IOException {
/* 433 */     String line = reader.readLine();
/* 434 */     int colon = line.indexOf(':');
/* 435 */     if (colon == -1) throw new GdxRuntimeException("Invalid line: " + line); 
/* 436 */     return line.substring(colon + 1).trim();
/*     */   }
/*     */ 
/*     */   
/*     */   static int readTuple(BufferedReader reader) throws IOException {
/* 441 */     String line = reader.readLine();
/* 442 */     int colon = line.indexOf(':');
/* 443 */     if (colon == -1) throw new GdxRuntimeException("Invalid line: " + line); 
/* 444 */     int i = 0, lastMatch = colon + 1;
/* 445 */     for (i = 0; i < 3; i++) {
/* 446 */       int comma = line.indexOf(',', lastMatch);
/* 447 */       if (comma == -1)
/* 448 */         break;  tuple[i] = line.substring(lastMatch, comma).trim();
/* 449 */       lastMatch = comma + 1;
/*     */     } 
/* 451 */     tuple[i] = line.substring(lastMatch).trim();
/* 452 */     return i + 1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class AtlasRegion
/*     */     extends TextureRegion
/*     */   {
/*     */     public int index;
/*     */ 
/*     */ 
/*     */     
/*     */     public String name;
/*     */ 
/*     */     
/*     */     public float offsetX;
/*     */ 
/*     */     
/*     */     public float offsetY;
/*     */ 
/*     */     
/*     */     public int packedWidth;
/*     */ 
/*     */     
/*     */     public int packedHeight;
/*     */ 
/*     */     
/*     */     public int originalWidth;
/*     */ 
/*     */     
/*     */     public int originalHeight;
/*     */ 
/*     */     
/*     */     public boolean rotate;
/*     */ 
/*     */     
/*     */     public int[] splits;
/*     */ 
/*     */     
/*     */     public int[] pads;
/*     */ 
/*     */ 
/*     */     
/*     */     public AtlasRegion(Texture texture, int x, int y, int width, int height) {
/* 497 */       super(texture, x, y, width, height);
/* 498 */       this.originalWidth = width;
/* 499 */       this.originalHeight = height;
/* 500 */       this.packedWidth = width;
/* 501 */       this.packedHeight = height;
/*     */     }
/*     */     
/*     */     public AtlasRegion(AtlasRegion region) {
/* 505 */       setRegion(region);
/* 506 */       this.index = region.index;
/* 507 */       this.name = region.name;
/* 508 */       this.offsetX = region.offsetX;
/* 509 */       this.offsetY = region.offsetY;
/* 510 */       this.packedWidth = region.packedWidth;
/* 511 */       this.packedHeight = region.packedHeight;
/* 512 */       this.originalWidth = region.originalWidth;
/* 513 */       this.originalHeight = region.originalHeight;
/* 514 */       this.rotate = region.rotate;
/* 515 */       this.splits = region.splits;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void flip(boolean x, boolean y) {
/* 521 */       super.flip(x, y);
/* 522 */       if (x) this.offsetX = this.originalWidth - this.offsetX - getRotatedPackedWidth(); 
/* 523 */       if (y) this.offsetY = this.originalHeight - this.offsetY - getRotatedPackedHeight();
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     public float getRotatedPackedWidth() {
/* 529 */       return this.rotate ? this.packedHeight : this.packedWidth;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public float getRotatedPackedHeight() {
/* 535 */       return this.rotate ? this.packedWidth : this.packedHeight;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 539 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AtlasSprite
/*     */     extends Sprite {
/*     */     final TextureAtlas.AtlasRegion region;
/*     */     float originalOffsetX;
/*     */     float originalOffsetY;
/*     */     
/*     */     public AtlasSprite(TextureAtlas.AtlasRegion region) {
/* 550 */       this.region = new TextureAtlas.AtlasRegion(region);
/* 551 */       this.originalOffsetX = region.offsetX;
/* 552 */       this.originalOffsetY = region.offsetY;
/* 553 */       setRegion(region);
/* 554 */       setOrigin(region.originalWidth / 2.0F, region.originalHeight / 2.0F);
/* 555 */       int width = region.getRegionWidth();
/* 556 */       int height = region.getRegionHeight();
/* 557 */       if (region.rotate) {
/* 558 */         super.rotate90(true);
/* 559 */         super.setBounds(region.offsetX, region.offsetY, height, width);
/*     */       } else {
/* 561 */         super.setBounds(region.offsetX, region.offsetY, width, height);
/* 562 */       }  setColor(1.0F, 1.0F, 1.0F, 1.0F);
/*     */     }
/*     */     
/*     */     public AtlasSprite(AtlasSprite sprite) {
/* 566 */       this.region = sprite.region;
/* 567 */       this.originalOffsetX = sprite.originalOffsetX;
/* 568 */       this.originalOffsetY = sprite.originalOffsetY;
/* 569 */       set(sprite);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setPosition(float x, float y) {
/* 574 */       super.setPosition(x + this.region.offsetX, y + this.region.offsetY);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setX(float x) {
/* 579 */       super.setX(x + this.region.offsetX);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setY(float y) {
/* 584 */       super.setY(y + this.region.offsetY);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setBounds(float x, float y, float width, float height) {
/* 589 */       float widthRatio = width / this.region.originalWidth;
/* 590 */       float heightRatio = height / this.region.originalHeight;
/* 591 */       this.region.offsetX = this.originalOffsetX * widthRatio;
/* 592 */       this.region.offsetY = this.originalOffsetY * heightRatio;
/* 593 */       int packedWidth = this.region.rotate ? this.region.packedHeight : this.region.packedWidth;
/* 594 */       int packedHeight = this.region.rotate ? this.region.packedWidth : this.region.packedHeight;
/* 595 */       super.setBounds(x + this.region.offsetX, y + this.region.offsetY, packedWidth * widthRatio, packedHeight * heightRatio);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSize(float width, float height) {
/* 600 */       setBounds(getX(), getY(), width, height);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOrigin(float originX, float originY) {
/* 605 */       super.setOrigin(originX - this.region.offsetX, originY - this.region.offsetY);
/*     */     }
/*     */ 
/*     */     
/*     */     public void setOriginCenter() {
/* 610 */       super.setOrigin(this.width / 2.0F - this.region.offsetX, this.height / 2.0F - this.region.offsetY);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void flip(boolean x, boolean y) {
/* 616 */       if (this.region.rotate) {
/* 617 */         super.flip(y, x);
/*     */       } else {
/* 619 */         super.flip(x, y);
/*     */       } 
/* 621 */       float oldOriginX = getOriginX();
/* 622 */       float oldOriginY = getOriginY();
/* 623 */       float oldOffsetX = this.region.offsetX;
/* 624 */       float oldOffsetY = this.region.offsetY;
/*     */       
/* 626 */       float widthRatio = getWidthRatio();
/* 627 */       float heightRatio = getHeightRatio();
/*     */       
/* 629 */       this.region.offsetX = this.originalOffsetX;
/* 630 */       this.region.offsetY = this.originalOffsetY;
/* 631 */       this.region.flip(x, y);
/* 632 */       this.originalOffsetX = this.region.offsetX;
/* 633 */       this.originalOffsetY = this.region.offsetY;
/* 634 */       this.region.offsetX *= widthRatio;
/* 635 */       this.region.offsetY *= heightRatio;
/*     */ 
/*     */       
/* 638 */       translate(this.region.offsetX - oldOffsetX, this.region.offsetY - oldOffsetY);
/* 639 */       setOrigin(oldOriginX, oldOriginY);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void rotate90(boolean clockwise) {
/* 645 */       super.rotate90(clockwise);
/*     */       
/* 647 */       float oldOriginX = getOriginX();
/* 648 */       float oldOriginY = getOriginY();
/* 649 */       float oldOffsetX = this.region.offsetX;
/* 650 */       float oldOffsetY = this.region.offsetY;
/*     */       
/* 652 */       float widthRatio = getWidthRatio();
/* 653 */       float heightRatio = getHeightRatio();
/*     */       
/* 655 */       if (clockwise) {
/* 656 */         this.region.offsetX = oldOffsetY;
/* 657 */         this.region.offsetY = this.region.originalHeight * heightRatio - oldOffsetX - this.region.packedWidth * widthRatio;
/*     */       } else {
/* 659 */         this.region.offsetX = this.region.originalWidth * widthRatio - oldOffsetY - this.region.packedHeight * heightRatio;
/* 660 */         this.region.offsetY = oldOffsetX;
/*     */       } 
/*     */ 
/*     */       
/* 664 */       translate(this.region.offsetX - oldOffsetX, this.region.offsetY - oldOffsetY);
/* 665 */       setOrigin(oldOriginX, oldOriginY);
/*     */     }
/*     */ 
/*     */     
/*     */     public float getX() {
/* 670 */       return super.getX() - this.region.offsetX;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getY() {
/* 675 */       return super.getY() - this.region.offsetY;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getOriginX() {
/* 680 */       return super.getOriginX() + this.region.offsetX;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getOriginY() {
/* 685 */       return super.getOriginY() + this.region.offsetY;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getWidth() {
/* 690 */       return super.getWidth() / this.region.getRotatedPackedWidth() * this.region.originalWidth;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getHeight() {
/* 695 */       return super.getHeight() / this.region.getRotatedPackedHeight() * this.region.originalHeight;
/*     */     }
/*     */     
/*     */     public float getWidthRatio() {
/* 699 */       return super.getWidth() / this.region.getRotatedPackedWidth();
/*     */     }
/*     */     
/*     */     public float getHeightRatio() {
/* 703 */       return super.getHeight() / this.region.getRotatedPackedHeight();
/*     */     }
/*     */     
/*     */     public TextureAtlas.AtlasRegion getAtlasRegion() {
/* 707 */       return this.region;
/*     */     }
/*     */     
/*     */     public String toString() {
/* 711 */       return this.region.toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g2d\TextureAtlas.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */