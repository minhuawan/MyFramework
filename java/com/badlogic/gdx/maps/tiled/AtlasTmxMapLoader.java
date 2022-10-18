/*     */ package com.badlogic.gdx.maps.tiled;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureAtlas;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.MapProperties;
/*     */ import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
/*     */ import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.ObjectSet;
/*     */ import com.badlogic.gdx.utils.XmlReader;
/*     */ import java.io.IOException;
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
/*     */ public class AtlasTmxMapLoader
/*     */   extends BaseTmxMapLoader<AtlasTmxMapLoader.AtlasTiledMapLoaderParameters>
/*     */ {
/*     */   public static class AtlasTiledMapLoaderParameters
/*     */     extends BaseTmxMapLoader.Parameters
/*     */   {
/*     */     public boolean forceTextureFilters = false;
/*     */   }
/*  68 */   protected Array<Texture> trackedTextures = new Array();
/*     */ 
/*     */ 
/*     */   
/*     */   public static class DirectAtlasResolver
/*     */     implements AtlasResolver
/*     */   {
/*     */     private final ObjectMap<String, TextureAtlas> atlases;
/*     */ 
/*     */     
/*     */     public DirectAtlasResolver(ObjectMap<String, TextureAtlas> atlases) {
/*  79 */       this.atlases = atlases;
/*     */     }
/*     */     
/*     */     public TextureAtlas getAtlas(String name)
/*     */     {
/*  84 */       return (TextureAtlas)this.atlases.get(name); } } private static interface AtlasResolver { TextureAtlas getAtlas(String param1String); public static class DirectAtlasResolver implements AtlasResolver { public TextureAtlas getAtlas(String name) { return (TextureAtlas)this.atlases.get(name); }
/*     */       
/*     */       private final ObjectMap<String, TextureAtlas> atlases;
/*     */       public DirectAtlasResolver(ObjectMap<String, TextureAtlas> atlases) {
/*     */         this.atlases = atlases;
/*     */       } }
/*     */     public static class AssetManagerAtlasResolver implements AtlasResolver { private final AssetManager assetManager;
/*     */       
/*     */       public AssetManagerAtlasResolver(AssetManager assetManager) {
/*  93 */         this.assetManager = assetManager;
/*     */       }
/*     */       
/*     */       public TextureAtlas getAtlas(String name)
/*     */       {
/*  98 */         return (TextureAtlas)this.assetManager.get(name, TextureAtlas.class); } } } public static class AssetManagerAtlasResolver implements AtlasResolver { private final AssetManager assetManager; public TextureAtlas getAtlas(String name) { return (TextureAtlas)this.assetManager.get(name, TextureAtlas.class); }
/*     */     
/*     */     public AssetManagerAtlasResolver(AssetManager assetManager) {
/*     */       this.assetManager = assetManager;
/*     */     } }
/*     */   public AtlasTmxMapLoader() {
/* 104 */     super((FileHandleResolver)new InternalFileHandleResolver());
/*     */   }
/*     */   
/*     */   public AtlasTmxMapLoader(FileHandleResolver resolver) {
/* 108 */     super(resolver);
/*     */   }
/*     */   
/*     */   public TiledMap load(String fileName) {
/* 112 */     return load(fileName, new AtlasTiledMapLoaderParameters());
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle tmxFile, AtlasTiledMapLoaderParameters parameter) {
/* 117 */     Array<AssetDescriptor> dependencies = new Array();
/*     */     try {
/* 119 */       this.root = this.xml.parse(tmxFile);
/*     */       
/* 121 */       XmlReader.Element properties = this.root.getChildByName("properties");
/* 122 */       if (properties != null) {
/* 123 */         for (XmlReader.Element property : properties.getChildrenByName("property")) {
/* 124 */           String name = property.getAttribute("name");
/* 125 */           String value = property.getAttribute("value");
/* 126 */           if (name.startsWith("atlas")) {
/* 127 */             FileHandle atlasHandle = getRelativeFileHandle(tmxFile, value);
/* 128 */             dependencies.add(new AssetDescriptor(atlasHandle, TextureAtlas.class));
/*     */           } 
/*     */         } 
/*     */       }
/* 132 */     } catch (IOException e) {
/* 133 */       throw new GdxRuntimeException("Unable to parse .tmx file.");
/*     */     } 
/* 135 */     return dependencies;
/*     */   }
/*     */   
/*     */   public TiledMap load(String fileName, AtlasTiledMapLoaderParameters parameter) {
/*     */     try {
/* 140 */       if (parameter != null) {
/* 141 */         this.convertObjectToTileSpace = parameter.convertObjectToTileSpace;
/* 142 */         this.flipY = parameter.flipY;
/*     */       } else {
/* 144 */         this.convertObjectToTileSpace = false;
/* 145 */         this.flipY = true;
/*     */       } 
/*     */       
/* 148 */       FileHandle tmxFile = resolve(fileName);
/* 149 */       this.root = this.xml.parse(tmxFile);
/* 150 */       ObjectMap<String, TextureAtlas> atlases = new ObjectMap();
/* 151 */       FileHandle atlasFile = loadAtlas(this.root, tmxFile);
/* 152 */       if (atlasFile == null) {
/* 153 */         throw new GdxRuntimeException("Couldn't load atlas");
/*     */       }
/*     */       
/* 156 */       TextureAtlas atlas = new TextureAtlas(atlasFile);
/* 157 */       atlases.put(atlasFile.path(), atlas);
/*     */       
/* 159 */       AtlasResolver.DirectAtlasResolver atlasResolver = new AtlasResolver.DirectAtlasResolver(atlases);
/* 160 */       TiledMap map = loadMap(this.root, tmxFile, atlasResolver);
/* 161 */       map.setOwnedResources(atlases.values().toArray());
/* 162 */       setTextureFilters(parameter.textureMinFilter, parameter.textureMagFilter);
/* 163 */       return map;
/* 164 */     } catch (IOException e) {
/* 165 */       throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected FileHandle loadAtlas(XmlReader.Element root, FileHandle tmxFile) throws IOException {
/* 171 */     XmlReader.Element e = root.getChildByName("properties");
/*     */     
/* 173 */     if (e != null) {
/* 174 */       for (XmlReader.Element property : e.getChildrenByName("property")) {
/* 175 */         String name = property.getAttribute("name", null);
/* 176 */         String value = property.getAttribute("value", null);
/* 177 */         if (name.equals("atlas")) {
/* 178 */           if (value == null) {
/* 179 */             value = property.getText();
/*     */           }
/*     */           
/* 182 */           if (value == null || value.length() == 0) {
/*     */             continue;
/*     */           }
/*     */ 
/*     */           
/* 187 */           return getRelativeFileHandle(tmxFile, value);
/*     */         } 
/*     */       } 
/*     */     }
/* 191 */     FileHandle atlasFile = tmxFile.sibling(tmxFile.nameWithoutExtension() + ".atlas");
/* 192 */     return atlasFile.exists() ? atlasFile : null;
/*     */   }
/*     */   
/*     */   private void setTextureFilters(Texture.TextureFilter min, Texture.TextureFilter mag) {
/* 196 */     for (Texture texture : this.trackedTextures) {
/* 197 */       texture.setFilter(min, mag);
/*     */     }
/* 199 */     this.trackedTextures.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadAsync(AssetManager manager, String fileName, FileHandle tmxFile, AtlasTiledMapLoaderParameters parameter) {
/* 204 */     this.map = null;
/*     */     
/* 206 */     if (parameter != null) {
/* 207 */       this.convertObjectToTileSpace = parameter.convertObjectToTileSpace;
/* 208 */       this.flipY = parameter.flipY;
/*     */     } else {
/* 210 */       this.convertObjectToTileSpace = false;
/* 211 */       this.flipY = true;
/*     */     } 
/*     */     
/*     */     try {
/* 215 */       this.map = loadMap(this.root, tmxFile, new AtlasResolver.AssetManagerAtlasResolver(manager));
/* 216 */     } catch (Exception e) {
/* 217 */       throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TiledMap loadSync(AssetManager manager, String fileName, FileHandle file, AtlasTiledMapLoaderParameters parameter) {
/* 223 */     if (parameter != null) {
/* 224 */       setTextureFilters(parameter.textureMinFilter, parameter.textureMagFilter);
/*     */     }
/*     */     
/* 227 */     return this.map;
/*     */   }
/*     */   
/*     */   protected TiledMap loadMap(XmlReader.Element root, FileHandle tmxFile, AtlasResolver resolver) {
/* 231 */     TiledMap map = new TiledMap();
/*     */     
/* 233 */     String mapOrientation = root.getAttribute("orientation", null);
/* 234 */     int mapWidth = root.getIntAttribute("width", 0);
/* 235 */     int mapHeight = root.getIntAttribute("height", 0);
/* 236 */     int tileWidth = root.getIntAttribute("tilewidth", 0);
/* 237 */     int tileHeight = root.getIntAttribute("tileheight", 0);
/* 238 */     String mapBackgroundColor = root.getAttribute("backgroundcolor", null);
/*     */     
/* 240 */     MapProperties mapProperties = map.getProperties();
/* 241 */     if (mapOrientation != null) {
/* 242 */       mapProperties.put("orientation", mapOrientation);
/*     */     }
/* 244 */     mapProperties.put("width", Integer.valueOf(mapWidth));
/* 245 */     mapProperties.put("height", Integer.valueOf(mapHeight));
/* 246 */     mapProperties.put("tilewidth", Integer.valueOf(tileWidth));
/* 247 */     mapProperties.put("tileheight", Integer.valueOf(tileHeight));
/* 248 */     if (mapBackgroundColor != null) {
/* 249 */       mapProperties.put("backgroundcolor", mapBackgroundColor);
/*     */     }
/*     */     
/* 252 */     this.mapTileWidth = tileWidth;
/* 253 */     this.mapTileHeight = tileHeight;
/* 254 */     this.mapWidthInPixels = mapWidth * tileWidth;
/* 255 */     this.mapHeightInPixels = mapHeight * tileHeight;
/*     */     
/* 257 */     if (mapOrientation != null && 
/* 258 */       "staggered".equals(mapOrientation) && 
/* 259 */       mapHeight > 1) {
/* 260 */       this.mapWidthInPixels += tileWidth / 2;
/* 261 */       this.mapHeightInPixels = this.mapHeightInPixels / 2 + tileHeight / 2;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 266 */     for (int i = 0, j = root.getChildCount(); i < j; i++) {
/* 267 */       XmlReader.Element element = root.getChild(i);
/* 268 */       String elementName = element.getName();
/* 269 */       if (elementName.equals("properties")) {
/* 270 */         loadProperties(map.getProperties(), element);
/* 271 */       } else if (elementName.equals("tileset")) {
/* 272 */         loadTileset(map, element, tmxFile, resolver);
/* 273 */       } else if (elementName.equals("layer")) {
/* 274 */         loadTileLayer(map, element);
/* 275 */       } else if (elementName.equals("objectgroup")) {
/* 276 */         loadObjectGroup(map, element);
/*     */       } 
/*     */     } 
/* 279 */     return map;
/*     */   }
/*     */   
/*     */   protected void loadTileset(TiledMap map, XmlReader.Element element, FileHandle tmxFile, AtlasResolver resolver) {
/* 283 */     if (element.getName().equals("tileset")) {
/* 284 */       String name = element.get("name", null);
/* 285 */       int firstgid = element.getIntAttribute("firstgid", 1);
/* 286 */       int tilewidth = element.getIntAttribute("tilewidth", 0);
/* 287 */       int tileheight = element.getIntAttribute("tileheight", 0);
/* 288 */       int spacing = element.getIntAttribute("spacing", 0);
/* 289 */       int margin = element.getIntAttribute("margin", 0);
/* 290 */       String source = element.getAttribute("source", null);
/*     */       
/* 292 */       int offsetX = 0;
/* 293 */       int offsetY = 0;
/*     */       
/* 295 */       String imageSource = "";
/* 296 */       int imageWidth = 0, imageHeight = 0;
/*     */       
/* 298 */       FileHandle image = null;
/* 299 */       if (source != null) {
/* 300 */         FileHandle tsx = getRelativeFileHandle(tmxFile, source);
/*     */         try {
/* 302 */           element = this.xml.parse(tsx);
/* 303 */           name = element.get("name", null);
/* 304 */           tilewidth = element.getIntAttribute("tilewidth", 0);
/* 305 */           tileheight = element.getIntAttribute("tileheight", 0);
/* 306 */           spacing = element.getIntAttribute("spacing", 0);
/* 307 */           margin = element.getIntAttribute("margin", 0);
/* 308 */           XmlReader.Element offset = element.getChildByName("tileoffset");
/* 309 */           if (offset != null) {
/* 310 */             offsetX = offset.getIntAttribute("x", 0);
/* 311 */             offsetY = offset.getIntAttribute("y", 0);
/*     */           } 
/* 313 */           XmlReader.Element imageElement = element.getChildByName("image");
/* 314 */           if (imageElement != null) {
/* 315 */             imageSource = imageElement.getAttribute("source");
/* 316 */             imageWidth = imageElement.getIntAttribute("width", 0);
/* 317 */             imageHeight = imageElement.getIntAttribute("height", 0);
/* 318 */             image = getRelativeFileHandle(tsx, imageSource);
/*     */           } 
/* 320 */         } catch (IOException e) {
/* 321 */           throw new GdxRuntimeException("Error parsing external tileset.");
/*     */         } 
/*     */       } else {
/* 324 */         XmlReader.Element offset = element.getChildByName("tileoffset");
/* 325 */         if (offset != null) {
/* 326 */           offsetX = offset.getIntAttribute("x", 0);
/* 327 */           offsetY = offset.getIntAttribute("y", 0);
/*     */         } 
/* 329 */         XmlReader.Element imageElement = element.getChildByName("image");
/* 330 */         if (imageElement != null) {
/* 331 */           imageSource = imageElement.getAttribute("source");
/* 332 */           imageWidth = imageElement.getIntAttribute("width", 0);
/* 333 */           imageHeight = imageElement.getIntAttribute("height", 0);
/* 334 */           image = getRelativeFileHandle(tmxFile, imageSource);
/*     */         } 
/*     */       } 
/*     */       
/* 338 */       String atlasFilePath = (String)map.getProperties().get("atlas", String.class);
/* 339 */       if (atlasFilePath == null) {
/* 340 */         FileHandle atlasFile = tmxFile.sibling(tmxFile.nameWithoutExtension() + ".atlas");
/* 341 */         if (atlasFile.exists()) atlasFilePath = atlasFile.name(); 
/*     */       } 
/* 343 */       if (atlasFilePath == null) {
/* 344 */         throw new GdxRuntimeException("The map is missing the 'atlas' property");
/*     */       }
/*     */ 
/*     */       
/* 348 */       FileHandle atlasHandle = getRelativeFileHandle(tmxFile, atlasFilePath);
/* 349 */       atlasHandle = resolve(atlasHandle.path());
/* 350 */       TextureAtlas atlas = resolver.getAtlas(atlasHandle.path());
/* 351 */       String regionsName = name;
/*     */       
/* 353 */       for (ObjectSet.ObjectSetIterator<Texture> objectSetIterator = atlas.getTextures().iterator(); objectSetIterator.hasNext(); ) { Texture texture = objectSetIterator.next();
/* 354 */         this.trackedTextures.add(texture); }
/*     */ 
/*     */       
/* 357 */       TiledMapTileSet tileset = new TiledMapTileSet();
/* 358 */       MapProperties props = tileset.getProperties();
/* 359 */       tileset.setName(name);
/* 360 */       props.put("firstgid", Integer.valueOf(firstgid));
/* 361 */       props.put("imagesource", imageSource);
/* 362 */       props.put("imagewidth", Integer.valueOf(imageWidth));
/* 363 */       props.put("imageheight", Integer.valueOf(imageHeight));
/* 364 */       props.put("tilewidth", Integer.valueOf(tilewidth));
/* 365 */       props.put("tileheight", Integer.valueOf(tileheight));
/* 366 */       props.put("margin", Integer.valueOf(margin));
/* 367 */       props.put("spacing", Integer.valueOf(spacing));
/*     */       
/* 369 */       if (imageSource != null && imageSource.length() > 0) {
/* 370 */         int lastgid = firstgid + imageWidth / tilewidth * imageHeight / tileheight - 1;
/* 371 */         for (TextureAtlas.AtlasRegion region : atlas.findRegions(regionsName)) {
/*     */           
/* 373 */           if (region != null) {
/* 374 */             int tileid = region.index + 1;
/* 375 */             if (tileid >= firstgid && tileid <= lastgid) {
/* 376 */               StaticTiledMapTile tile = new StaticTiledMapTile((TextureRegion)region);
/* 377 */               tile.setId(tileid);
/* 378 */               tile.setOffsetX(offsetX);
/* 379 */               tile.setOffsetY(this.flipY ? -offsetY : offsetY);
/* 380 */               tileset.putTile(tileid, (TiledMapTile)tile);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 386 */       for (XmlReader.Element tileElement : element.getChildrenByName("tile")) {
/* 387 */         StaticTiledMapTile staticTiledMapTile; int tileid = firstgid + tileElement.getIntAttribute("id", 0);
/* 388 */         TiledMapTile tile = tileset.getTile(tileid);
/* 389 */         if (tile == null) {
/* 390 */           XmlReader.Element imageElement = tileElement.getChildByName("image");
/* 391 */           if (imageElement != null) {
/*     */             
/* 393 */             String regionName = imageElement.getAttribute("source");
/* 394 */             regionName = regionName.substring(0, regionName.lastIndexOf('.'));
/* 395 */             TextureAtlas.AtlasRegion region = atlas.findRegion(regionName);
/* 396 */             if (region == null) throw new GdxRuntimeException("Tileset region not found: " + regionName); 
/* 397 */             staticTiledMapTile = new StaticTiledMapTile((TextureRegion)region);
/* 398 */             staticTiledMapTile.setId(tileid);
/* 399 */             staticTiledMapTile.setOffsetX(offsetX);
/* 400 */             staticTiledMapTile.setOffsetY(this.flipY ? -offsetY : offsetY);
/* 401 */             tileset.putTile(tileid, (TiledMapTile)staticTiledMapTile);
/*     */           } 
/*     */         } 
/* 404 */         if (staticTiledMapTile != null) {
/* 405 */           String terrain = tileElement.getAttribute("terrain", null);
/* 406 */           if (terrain != null) {
/* 407 */             staticTiledMapTile.getProperties().put("terrain", terrain);
/*     */           }
/* 409 */           String probability = tileElement.getAttribute("probability", null);
/* 410 */           if (probability != null) {
/* 411 */             staticTiledMapTile.getProperties().put("probability", probability);
/*     */           }
/* 413 */           XmlReader.Element element1 = tileElement.getChildByName("properties");
/* 414 */           if (element1 != null) {
/* 415 */             loadProperties(staticTiledMapTile.getProperties(), element1);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 420 */       Array<XmlReader.Element> tileElements = element.getChildrenByName("tile");
/*     */       
/* 422 */       Array<AnimatedTiledMapTile> animatedTiles = new Array();
/*     */       
/* 424 */       for (XmlReader.Element tileElement : tileElements) {
/* 425 */         int localtid = tileElement.getIntAttribute("id", 0);
/* 426 */         TiledMapTile tile = tileset.getTile(firstgid + localtid);
/* 427 */         if (tile != null) {
/* 428 */           AnimatedTiledMapTile animatedTiledMapTile; XmlReader.Element animationElement = tileElement.getChildByName("animation");
/* 429 */           if (animationElement != null) {
/*     */             
/* 431 */             Array<StaticTiledMapTile> staticTiles = new Array();
/* 432 */             IntArray intervals = new IntArray();
/* 433 */             for (XmlReader.Element frameElement : animationElement.getChildrenByName("frame")) {
/* 434 */               staticTiles.add(tileset.getTile(firstgid + frameElement.getIntAttribute("tileid")));
/* 435 */               intervals.add(frameElement.getIntAttribute("duration"));
/*     */             } 
/*     */             
/* 438 */             AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(intervals, staticTiles);
/* 439 */             animatedTile.setId(tile.getId());
/* 440 */             animatedTiles.add(animatedTile);
/* 441 */             animatedTiledMapTile = animatedTile;
/*     */           } 
/*     */           
/* 444 */           String terrain = tileElement.getAttribute("terrain", null);
/* 445 */           if (terrain != null) {
/* 446 */             animatedTiledMapTile.getProperties().put("terrain", terrain);
/*     */           }
/* 448 */           String probability = tileElement.getAttribute("probability", null);
/* 449 */           if (probability != null) {
/* 450 */             animatedTiledMapTile.getProperties().put("probability", probability);
/*     */           }
/* 452 */           XmlReader.Element element1 = tileElement.getChildByName("properties");
/* 453 */           if (element1 != null) {
/* 454 */             loadProperties(animatedTiledMapTile.getProperties(), element1);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 459 */       for (AnimatedTiledMapTile tile : animatedTiles) {
/* 460 */         tileset.putTile(tile.getId(), (TiledMapTile)tile);
/*     */       }
/*     */       
/* 463 */       XmlReader.Element properties = element.getChildByName("properties");
/* 464 */       if (properties != null) {
/* 465 */         loadProperties(tileset.getProperties(), properties);
/*     */       }
/* 467 */       map.getTileSets().addTileSet(tileset);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\AtlasTmxMapLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */