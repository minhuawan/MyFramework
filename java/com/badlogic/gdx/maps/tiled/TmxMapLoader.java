/*     */ package com.badlogic.gdx.maps.tiled;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.TextureLoader;
/*     */ import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.ImageResolver;
/*     */ import com.badlogic.gdx.maps.MapProperties;
/*     */ import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
/*     */ import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
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
/*     */ public class TmxMapLoader
/*     */   extends BaseTmxMapLoader<TmxMapLoader.Parameters>
/*     */ {
/*     */   public static class Parameters
/*     */     extends BaseTmxMapLoader.Parameters {}
/*     */   
/*     */   public TmxMapLoader() {
/*  49 */     super((FileHandleResolver)new InternalFileHandleResolver());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TmxMapLoader(FileHandleResolver resolver) {
/*  56 */     super(resolver);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TiledMap load(String fileName) {
/*  65 */     return load(fileName, new Parameters());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TiledMap load(String fileName, Parameters parameters) {
/*     */     try {
/*  75 */       this.convertObjectToTileSpace = parameters.convertObjectToTileSpace;
/*  76 */       this.flipY = parameters.flipY;
/*  77 */       FileHandle tmxFile = resolve(fileName);
/*  78 */       this.root = this.xml.parse(tmxFile);
/*  79 */       ObjectMap<String, Texture> textures = new ObjectMap();
/*  80 */       Array<FileHandle> textureFiles = loadTilesets(this.root, tmxFile);
/*  81 */       textureFiles.addAll(loadImages(this.root, tmxFile));
/*     */       
/*  83 */       for (FileHandle textureFile : textureFiles) {
/*  84 */         Texture texture = new Texture(textureFile, parameters.generateMipMaps);
/*  85 */         texture.setFilter(parameters.textureMinFilter, parameters.textureMagFilter);
/*  86 */         textures.put(textureFile.path(), texture);
/*     */       } 
/*     */       
/*  89 */       ImageResolver.DirectImageResolver imageResolver = new ImageResolver.DirectImageResolver(textures);
/*  90 */       TiledMap map = loadTilemap(this.root, tmxFile, (ImageResolver)imageResolver);
/*  91 */       map.setOwnedResources(textures.values().toArray());
/*  92 */       return map;
/*  93 */     } catch (IOException e) {
/*  94 */       throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void loadAsync(AssetManager manager, String fileName, FileHandle tmxFile, Parameters parameter) {
/* 100 */     this.map = null;
/*     */     
/* 102 */     if (parameter != null) {
/* 103 */       this.convertObjectToTileSpace = parameter.convertObjectToTileSpace;
/* 104 */       this.flipY = parameter.flipY;
/*     */     } else {
/* 106 */       this.convertObjectToTileSpace = false;
/* 107 */       this.flipY = true;
/*     */     } 
/*     */     try {
/* 110 */       this.map = loadTilemap(this.root, tmxFile, (ImageResolver)new ImageResolver.AssetManagerImageResolver(manager));
/* 111 */     } catch (Exception e) {
/* 112 */       throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public TiledMap loadSync(AssetManager manager, String fileName, FileHandle file, Parameters parameter) {
/* 118 */     return this.map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle tmxFile, Parameters parameter) {
/* 128 */     Array<AssetDescriptor> dependencies = new Array();
/*     */     try {
/* 130 */       this.root = this.xml.parse(tmxFile);
/* 131 */       boolean generateMipMaps = (parameter != null) ? parameter.generateMipMaps : false;
/* 132 */       TextureLoader.TextureParameter texParams = new TextureLoader.TextureParameter();
/* 133 */       texParams.genMipMaps = generateMipMaps;
/* 134 */       if (parameter != null) {
/* 135 */         texParams.minFilter = parameter.textureMinFilter;
/* 136 */         texParams.magFilter = parameter.textureMagFilter;
/*     */       } 
/* 138 */       for (FileHandle image : loadTilesets(this.root, tmxFile)) {
/* 139 */         dependencies.add(new AssetDescriptor(image, Texture.class, (AssetLoaderParameters)texParams));
/*     */       }
/* 141 */       for (FileHandle image : loadImages(this.root, tmxFile)) {
/* 142 */         dependencies.add(new AssetDescriptor(image, Texture.class, (AssetLoaderParameters)texParams));
/*     */       }
/* 144 */       return dependencies;
/* 145 */     } catch (IOException e) {
/* 146 */       throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected TiledMap loadTilemap(XmlReader.Element root, FileHandle tmxFile, ImageResolver imageResolver) {
/* 156 */     TiledMap map = new TiledMap();
/*     */     
/* 158 */     String mapOrientation = root.getAttribute("orientation", null);
/* 159 */     int mapWidth = root.getIntAttribute("width", 0);
/* 160 */     int mapHeight = root.getIntAttribute("height", 0);
/* 161 */     int tileWidth = root.getIntAttribute("tilewidth", 0);
/* 162 */     int tileHeight = root.getIntAttribute("tileheight", 0);
/* 163 */     int hexSideLength = root.getIntAttribute("hexsidelength", 0);
/* 164 */     String staggerAxis = root.getAttribute("staggeraxis", null);
/* 165 */     String staggerIndex = root.getAttribute("staggerindex", null);
/* 166 */     String mapBackgroundColor = root.getAttribute("backgroundcolor", null);
/*     */     
/* 168 */     MapProperties mapProperties = map.getProperties();
/* 169 */     if (mapOrientation != null) {
/* 170 */       mapProperties.put("orientation", mapOrientation);
/*     */     }
/* 172 */     mapProperties.put("width", Integer.valueOf(mapWidth));
/* 173 */     mapProperties.put("height", Integer.valueOf(mapHeight));
/* 174 */     mapProperties.put("tilewidth", Integer.valueOf(tileWidth));
/* 175 */     mapProperties.put("tileheight", Integer.valueOf(tileHeight));
/* 176 */     mapProperties.put("hexsidelength", Integer.valueOf(hexSideLength));
/* 177 */     if (staggerAxis != null) {
/* 178 */       mapProperties.put("staggeraxis", staggerAxis);
/*     */     }
/* 180 */     if (staggerIndex != null) {
/* 181 */       mapProperties.put("staggerindex", staggerIndex);
/*     */     }
/* 183 */     if (mapBackgroundColor != null) {
/* 184 */       mapProperties.put("backgroundcolor", mapBackgroundColor);
/*     */     }
/* 186 */     this.mapTileWidth = tileWidth;
/* 187 */     this.mapTileHeight = tileHeight;
/* 188 */     this.mapWidthInPixels = mapWidth * tileWidth;
/* 189 */     this.mapHeightInPixels = mapHeight * tileHeight;
/*     */     
/* 191 */     if (mapOrientation != null && 
/* 192 */       "staggered".equals(mapOrientation) && 
/* 193 */       mapHeight > 1) {
/* 194 */       this.mapWidthInPixels += tileWidth / 2;
/* 195 */       this.mapHeightInPixels = this.mapHeightInPixels / 2 + tileHeight / 2;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 200 */     XmlReader.Element properties = root.getChildByName("properties");
/* 201 */     if (properties != null) {
/* 202 */       loadProperties(map.getProperties(), properties);
/*     */     }
/* 204 */     Array<XmlReader.Element> tilesets = root.getChildrenByName("tileset");
/* 205 */     for (XmlReader.Element element : tilesets) {
/* 206 */       loadTileSet(map, element, tmxFile, imageResolver);
/* 207 */       root.removeChild(element);
/*     */     } 
/* 209 */     for (int i = 0, j = root.getChildCount(); i < j; i++) {
/* 210 */       XmlReader.Element element = root.getChild(i);
/* 211 */       String name = element.getName();
/* 212 */       if (name.equals("layer")) {
/* 213 */         loadTileLayer(map, element);
/* 214 */       } else if (name.equals("objectgroup")) {
/* 215 */         loadObjectGroup(map, element);
/*     */       }
/* 217 */       else if (name.equals("imagelayer")) {
/* 218 */         loadImageLayer(map, element, tmxFile, imageResolver);
/*     */       } 
/*     */     } 
/* 221 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Array<FileHandle> loadTilesets(XmlReader.Element root, FileHandle tmxFile) throws IOException {
/* 229 */     Array<FileHandle> images = new Array();
/* 230 */     for (XmlReader.Element tileset : root.getChildrenByName("tileset")) {
/* 231 */       String source = tileset.getAttribute("source", null);
/* 232 */       if (source != null) {
/* 233 */         FileHandle tsxFile = getRelativeFileHandle(tmxFile, source);
/* 234 */         tileset = this.xml.parse(tsxFile);
/* 235 */         XmlReader.Element element = tileset.getChildByName("image");
/* 236 */         if (element != null) {
/* 237 */           String imageSource = tileset.getChildByName("image").getAttribute("source");
/* 238 */           FileHandle image = getRelativeFileHandle(tsxFile, imageSource);
/* 239 */           images.add(image); continue;
/*     */         } 
/* 241 */         for (XmlReader.Element tile : tileset.getChildrenByName("tile")) {
/* 242 */           String imageSource = tile.getChildByName("image").getAttribute("source");
/* 243 */           FileHandle image = getRelativeFileHandle(tsxFile, imageSource);
/* 244 */           images.add(image);
/*     */         } 
/*     */         continue;
/*     */       } 
/* 248 */       XmlReader.Element imageElement = tileset.getChildByName("image");
/* 249 */       if (imageElement != null) {
/* 250 */         String imageSource = tileset.getChildByName("image").getAttribute("source");
/* 251 */         FileHandle image = getRelativeFileHandle(tmxFile, imageSource);
/* 252 */         images.add(image); continue;
/*     */       } 
/* 254 */       for (XmlReader.Element tile : tileset.getChildrenByName("tile")) {
/* 255 */         String imageSource = tile.getChildByName("image").getAttribute("source");
/* 256 */         FileHandle image = getRelativeFileHandle(tmxFile, imageSource);
/* 257 */         images.add(image);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 262 */     return images;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Array<FileHandle> loadImages(XmlReader.Element root, FileHandle tmxFile) throws IOException {
/* 270 */     Array<FileHandle> images = new Array();
/*     */     
/* 272 */     for (XmlReader.Element imageLayer : root.getChildrenByName("imagelayer")) {
/* 273 */       XmlReader.Element image = imageLayer.getChildByName("image");
/* 274 */       String source = image.getAttribute("source", null);
/*     */       
/* 276 */       if (source != null) {
/* 277 */         FileHandle handle = getRelativeFileHandle(tmxFile, source);
/*     */         
/* 279 */         if (!images.contains(handle, false)) {
/* 280 */           images.add(handle);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 285 */     return images;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void loadTileSet(TiledMap map, XmlReader.Element element, FileHandle tmxFile, ImageResolver imageResolver) {
/* 314 */     if (element.getName().equals("tileset")) {
/* 315 */       String name = element.get("name", null);
/* 316 */       int firstgid = element.getIntAttribute("firstgid", 1);
/* 317 */       int tilewidth = element.getIntAttribute("tilewidth", 0);
/* 318 */       int tileheight = element.getIntAttribute("tileheight", 0);
/* 319 */       int spacing = element.getIntAttribute("spacing", 0);
/* 320 */       int margin = element.getIntAttribute("margin", 0);
/* 321 */       String source = element.getAttribute("source", null);
/*     */       
/* 323 */       int offsetX = 0;
/* 324 */       int offsetY = 0;
/*     */       
/* 326 */       String imageSource = "";
/* 327 */       int imageWidth = 0, imageHeight = 0;
/*     */       
/* 329 */       FileHandle image = null;
/* 330 */       if (source != null) {
/* 331 */         FileHandle tsx = getRelativeFileHandle(tmxFile, source);
/*     */         try {
/* 333 */           element = this.xml.parse(tsx);
/* 334 */           name = element.get("name", null);
/* 335 */           tilewidth = element.getIntAttribute("tilewidth", 0);
/* 336 */           tileheight = element.getIntAttribute("tileheight", 0);
/* 337 */           spacing = element.getIntAttribute("spacing", 0);
/* 338 */           margin = element.getIntAttribute("margin", 0);
/* 339 */           XmlReader.Element offset = element.getChildByName("tileoffset");
/* 340 */           if (offset != null) {
/* 341 */             offsetX = offset.getIntAttribute("x", 0);
/* 342 */             offsetY = offset.getIntAttribute("y", 0);
/*     */           } 
/* 344 */           XmlReader.Element imageElement = element.getChildByName("image");
/* 345 */           if (imageElement != null) {
/* 346 */             imageSource = imageElement.getAttribute("source");
/* 347 */             imageWidth = imageElement.getIntAttribute("width", 0);
/* 348 */             imageHeight = imageElement.getIntAttribute("height", 0);
/* 349 */             image = getRelativeFileHandle(tsx, imageSource);
/*     */           } 
/* 351 */         } catch (IOException e) {
/* 352 */           throw new GdxRuntimeException("Error parsing external tileset.");
/*     */         } 
/*     */       } else {
/* 355 */         XmlReader.Element offset = element.getChildByName("tileoffset");
/* 356 */         if (offset != null) {
/* 357 */           offsetX = offset.getIntAttribute("x", 0);
/* 358 */           offsetY = offset.getIntAttribute("y", 0);
/*     */         } 
/* 360 */         XmlReader.Element imageElement = element.getChildByName("image");
/* 361 */         if (imageElement != null) {
/* 362 */           imageSource = imageElement.getAttribute("source");
/* 363 */           imageWidth = imageElement.getIntAttribute("width", 0);
/* 364 */           imageHeight = imageElement.getIntAttribute("height", 0);
/* 365 */           image = getRelativeFileHandle(tmxFile, imageSource);
/*     */         } 
/*     */       } 
/*     */       
/* 369 */       TiledMapTileSet tileset = new TiledMapTileSet();
/* 370 */       tileset.setName(name);
/* 371 */       tileset.getProperties().put("firstgid", Integer.valueOf(firstgid));
/* 372 */       if (image != null) {
/* 373 */         TextureRegion texture = imageResolver.getImage(image.path());
/*     */         
/* 375 */         MapProperties props = tileset.getProperties();
/* 376 */         props.put("imagesource", imageSource);
/* 377 */         props.put("imagewidth", Integer.valueOf(imageWidth));
/* 378 */         props.put("imageheight", Integer.valueOf(imageHeight));
/* 379 */         props.put("tilewidth", Integer.valueOf(tilewidth));
/* 380 */         props.put("tileheight", Integer.valueOf(tileheight));
/* 381 */         props.put("margin", Integer.valueOf(margin));
/* 382 */         props.put("spacing", Integer.valueOf(spacing));
/*     */         
/* 384 */         int stopWidth = texture.getRegionWidth() - tilewidth;
/* 385 */         int stopHeight = texture.getRegionHeight() - tileheight;
/*     */         
/* 387 */         int id = firstgid;
/*     */         int y;
/* 389 */         for (y = margin; y <= stopHeight; y += tileheight + spacing) {
/* 390 */           int x; for (x = margin; x <= stopWidth; x += tilewidth + spacing) {
/* 391 */             TextureRegion tileRegion = new TextureRegion(texture, x, y, tilewidth, tileheight);
/* 392 */             StaticTiledMapTile staticTiledMapTile = new StaticTiledMapTile(tileRegion);
/* 393 */             staticTiledMapTile.setId(id);
/* 394 */             staticTiledMapTile.setOffsetX(offsetX);
/* 395 */             staticTiledMapTile.setOffsetY(this.flipY ? -offsetY : offsetY);
/* 396 */             tileset.putTile(id++, (TiledMapTile)staticTiledMapTile);
/*     */           } 
/*     */         } 
/*     */       } else {
/* 400 */         Array<XmlReader.Element> array = element.getChildrenByName("tile");
/* 401 */         for (XmlReader.Element tileElement : array) {
/* 402 */           XmlReader.Element imageElement = tileElement.getChildByName("image");
/* 403 */           if (imageElement != null) {
/* 404 */             imageSource = imageElement.getAttribute("source");
/* 405 */             imageWidth = imageElement.getIntAttribute("width", 0);
/* 406 */             imageHeight = imageElement.getIntAttribute("height", 0);
/* 407 */             image = getRelativeFileHandle(tmxFile, imageSource);
/*     */           } 
/* 409 */           TextureRegion texture = imageResolver.getImage(image.path());
/* 410 */           StaticTiledMapTile staticTiledMapTile = new StaticTiledMapTile(texture);
/* 411 */           staticTiledMapTile.setId(firstgid + tileElement.getIntAttribute("id"));
/* 412 */           staticTiledMapTile.setOffsetX(offsetX);
/* 413 */           staticTiledMapTile.setOffsetY(this.flipY ? -offsetY : offsetY);
/* 414 */           tileset.putTile(staticTiledMapTile.getId(), (TiledMapTile)staticTiledMapTile);
/*     */         } 
/*     */       } 
/* 417 */       Array<XmlReader.Element> tileElements = element.getChildrenByName("tile");
/*     */       
/* 419 */       Array<AnimatedTiledMapTile> animatedTiles = new Array();
/*     */       
/* 421 */       for (XmlReader.Element tileElement : tileElements) {
/* 422 */         int localtid = tileElement.getIntAttribute("id", 0);
/* 423 */         TiledMapTile tile = tileset.getTile(firstgid + localtid);
/* 424 */         if (tile != null) {
/* 425 */           AnimatedTiledMapTile animatedTiledMapTile; XmlReader.Element animationElement = tileElement.getChildByName("animation");
/* 426 */           if (animationElement != null) {
/*     */             
/* 428 */             Array<StaticTiledMapTile> staticTiles = new Array();
/* 429 */             IntArray intervals = new IntArray();
/* 430 */             for (XmlReader.Element frameElement : animationElement.getChildrenByName("frame")) {
/* 431 */               staticTiles.add(tileset.getTile(firstgid + frameElement.getIntAttribute("tileid")));
/* 432 */               intervals.add(frameElement.getIntAttribute("duration"));
/*     */             } 
/*     */             
/* 435 */             AnimatedTiledMapTile animatedTile = new AnimatedTiledMapTile(intervals, staticTiles);
/* 436 */             animatedTile.setId(tile.getId());
/* 437 */             animatedTiles.add(animatedTile);
/* 438 */             animatedTiledMapTile = animatedTile;
/*     */           } 
/*     */           
/* 441 */           String terrain = tileElement.getAttribute("terrain", null);
/* 442 */           if (terrain != null) {
/* 443 */             animatedTiledMapTile.getProperties().put("terrain", terrain);
/*     */           }
/* 445 */           String probability = tileElement.getAttribute("probability", null);
/* 446 */           if (probability != null) {
/* 447 */             animatedTiledMapTile.getProperties().put("probability", probability);
/*     */           }
/* 449 */           XmlReader.Element element1 = tileElement.getChildByName("properties");
/* 450 */           if (element1 != null) {
/* 451 */             loadProperties(animatedTiledMapTile.getProperties(), element1);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 456 */       for (AnimatedTiledMapTile tile : animatedTiles) {
/* 457 */         tileset.putTile(tile.getId(), (TiledMapTile)tile);
/*     */       }
/*     */       
/* 460 */       XmlReader.Element properties = element.getChildByName("properties");
/* 461 */       if (properties != null) {
/* 462 */         loadProperties(tileset.getProperties(), properties);
/*     */       }
/* 464 */       map.getTileSets().addTileSet(tileset);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\TmxMapLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */