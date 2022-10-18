/*     */ package com.badlogic.gdx.maps.tiled;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetDescriptor;
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.AssetManager;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
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
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import com.badlogic.gdx.utils.XmlReader;
/*     */ import java.io.IOException;
/*     */ import java.util.StringTokenizer;
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
/*     */ public class TideMapLoader
/*     */   extends SynchronousAssetLoader<TiledMap, TideMapLoader.Parameters>
/*     */ {
/*     */   public static class Parameters
/*     */     extends AssetLoaderParameters<TiledMap> {}
/*     */   
/*  50 */   private XmlReader xml = new XmlReader();
/*     */   private XmlReader.Element root;
/*     */   
/*     */   public TideMapLoader() {
/*  54 */     super((FileHandleResolver)new InternalFileHandleResolver());
/*     */   }
/*     */   
/*     */   public TideMapLoader(FileHandleResolver resolver) {
/*  58 */     super(resolver);
/*     */   }
/*     */   
/*     */   public TiledMap load(String fileName) {
/*     */     try {
/*  63 */       FileHandle tideFile = resolve(fileName);
/*  64 */       this.root = this.xml.parse(tideFile);
/*  65 */       ObjectMap<String, Texture> textures = new ObjectMap();
/*  66 */       for (FileHandle textureFile : loadTileSheets(this.root, tideFile)) {
/*  67 */         textures.put(textureFile.path(), new Texture(textureFile));
/*     */       }
/*  69 */       ImageResolver.DirectImageResolver imageResolver = new ImageResolver.DirectImageResolver(textures);
/*  70 */       TiledMap map = loadMap(this.root, tideFile, (ImageResolver)imageResolver);
/*  71 */       map.setOwnedResources(textures.values().toArray());
/*  72 */       return map;
/*  73 */     } catch (IOException e) {
/*  74 */       throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public TiledMap load(AssetManager assetManager, String fileName, FileHandle tideFile, Parameters parameter) {
/*     */     try {
/*  82 */       return loadMap(this.root, tideFile, (ImageResolver)new ImageResolver.AssetManagerImageResolver(assetManager));
/*  83 */     } catch (Exception e) {
/*  84 */       throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Array<AssetDescriptor> getDependencies(String fileName, FileHandle tmxFile, Parameters parameter) {
/*  90 */     Array<AssetDescriptor> dependencies = new Array();
/*     */     try {
/*  92 */       this.root = this.xml.parse(tmxFile);
/*  93 */       for (FileHandle image : loadTileSheets(this.root, tmxFile)) {
/*  94 */         dependencies.add(new AssetDescriptor(image.path(), Texture.class));
/*     */       }
/*  96 */       return dependencies;
/*  97 */     } catch (IOException e) {
/*  98 */       throw new GdxRuntimeException("Couldn't load tilemap '" + fileName + "'", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private TiledMap loadMap(XmlReader.Element root, FileHandle tmxFile, ImageResolver imageResolver) {
/* 108 */     TiledMap map = new TiledMap();
/* 109 */     XmlReader.Element properties = root.getChildByName("Properties");
/* 110 */     if (properties != null) {
/* 111 */       loadProperties(map.getProperties(), properties);
/*     */     }
/* 113 */     XmlReader.Element tilesheets = root.getChildByName("TileSheets");
/* 114 */     for (XmlReader.Element tilesheet : tilesheets.getChildrenByName("TileSheet")) {
/* 115 */       loadTileSheet(map, tilesheet, tmxFile, imageResolver);
/*     */     }
/* 117 */     XmlReader.Element layers = root.getChildByName("Layers");
/* 118 */     for (XmlReader.Element layer : layers.getChildrenByName("Layer")) {
/* 119 */       loadLayer(map, layer);
/*     */     }
/* 121 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Array<FileHandle> loadTileSheets(XmlReader.Element root, FileHandle tideFile) throws IOException {
/* 129 */     Array<FileHandle> images = new Array();
/* 130 */     XmlReader.Element tilesheets = root.getChildByName("TileSheets");
/* 131 */     for (XmlReader.Element tileset : tilesheets.getChildrenByName("TileSheet")) {
/* 132 */       XmlReader.Element imageSource = tileset.getChildByName("ImageSource");
/* 133 */       FileHandle image = getRelativeFileHandle(tideFile, imageSource.getText());
/* 134 */       images.add(image);
/*     */     } 
/* 136 */     return images;
/*     */   }
/*     */   
/*     */   private void loadTileSheet(TiledMap map, XmlReader.Element element, FileHandle tideFile, ImageResolver imageResolver) {
/* 140 */     if (element.getName().equals("TileSheet")) {
/* 141 */       String id = element.getAttribute("Id");
/* 142 */       String description = element.getChildByName("Description").getText();
/* 143 */       String imageSource = element.getChildByName("ImageSource").getText();
/*     */       
/* 145 */       XmlReader.Element alignment = element.getChildByName("Alignment");
/* 146 */       String sheetSize = alignment.getAttribute("SheetSize");
/* 147 */       String tileSize = alignment.getAttribute("TileSize");
/* 148 */       String margin = alignment.getAttribute("Margin");
/* 149 */       String spacing = alignment.getAttribute("Spacing");
/*     */       
/* 151 */       String[] sheetSizeParts = sheetSize.split(" x ");
/* 152 */       int sheetSizeX = Integer.parseInt(sheetSizeParts[0]);
/* 153 */       int sheetSizeY = Integer.parseInt(sheetSizeParts[1]);
/*     */       
/* 155 */       String[] tileSizeParts = tileSize.split(" x ");
/* 156 */       int tileSizeX = Integer.parseInt(tileSizeParts[0]);
/* 157 */       int tileSizeY = Integer.parseInt(tileSizeParts[1]);
/*     */       
/* 159 */       String[] marginParts = margin.split(" x ");
/* 160 */       int marginX = Integer.parseInt(marginParts[0]);
/* 161 */       int marginY = Integer.parseInt(marginParts[1]);
/*     */       
/* 163 */       String[] spacingParts = margin.split(" x ");
/* 164 */       int spacingX = Integer.parseInt(spacingParts[0]);
/* 165 */       int spacingY = Integer.parseInt(spacingParts[1]);
/*     */       
/* 167 */       FileHandle image = getRelativeFileHandle(tideFile, imageSource);
/* 168 */       TextureRegion texture = imageResolver.getImage(image.path());
/*     */       
/* 170 */       TiledMapTileSets tilesets = map.getTileSets();
/* 171 */       int firstgid = 1;
/* 172 */       for (TiledMapTileSet tiledMapTileSet : tilesets) {
/* 173 */         firstgid += tiledMapTileSet.size();
/*     */       }
/*     */       
/* 176 */       TiledMapTileSet tileset = new TiledMapTileSet();
/* 177 */       tileset.setName(id);
/* 178 */       tileset.getProperties().put("firstgid", Integer.valueOf(firstgid));
/* 179 */       int gid = firstgid;
/*     */       
/* 181 */       int stopWidth = texture.getRegionWidth() - tileSizeX;
/* 182 */       int stopHeight = texture.getRegionHeight() - tileSizeY;
/*     */       int y;
/* 184 */       for (y = marginY; y <= stopHeight; y += tileSizeY + spacingY) {
/* 185 */         int x; for (x = marginX; x <= stopWidth; x += tileSizeX + spacingX) {
/* 186 */           StaticTiledMapTile staticTiledMapTile = new StaticTiledMapTile(new TextureRegion(texture, x, y, tileSizeX, tileSizeY));
/* 187 */           staticTiledMapTile.setId(gid);
/* 188 */           tileset.putTile(gid++, (TiledMapTile)staticTiledMapTile);
/*     */         } 
/*     */       } 
/*     */       
/* 192 */       XmlReader.Element properties = element.getChildByName("Properties");
/* 193 */       if (properties != null) {
/* 194 */         loadProperties(tileset.getProperties(), properties);
/*     */       }
/*     */       
/* 197 */       tilesets.addTileSet(tileset);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadLayer(TiledMap map, XmlReader.Element element) {
/* 202 */     if (element.getName().equals("Layer")) {
/* 203 */       String id = element.getAttribute("Id");
/* 204 */       String visible = element.getAttribute("Visible");
/*     */       
/* 206 */       XmlReader.Element dimensions = element.getChildByName("Dimensions");
/* 207 */       String layerSize = dimensions.getAttribute("LayerSize");
/* 208 */       String tileSize = dimensions.getAttribute("TileSize");
/*     */       
/* 210 */       String[] layerSizeParts = layerSize.split(" x ");
/* 211 */       int layerSizeX = Integer.parseInt(layerSizeParts[0]);
/* 212 */       int layerSizeY = Integer.parseInt(layerSizeParts[1]);
/*     */       
/* 214 */       String[] tileSizeParts = tileSize.split(" x ");
/* 215 */       int tileSizeX = Integer.parseInt(tileSizeParts[0]);
/* 216 */       int tileSizeY = Integer.parseInt(tileSizeParts[1]);
/*     */       
/* 218 */       TiledMapTileLayer layer = new TiledMapTileLayer(layerSizeX, layerSizeY, tileSizeX, tileSizeY);
/* 219 */       layer.setName(id);
/* 220 */       layer.setVisible(visible.equalsIgnoreCase("True"));
/* 221 */       XmlReader.Element tileArray = element.getChildByName("TileArray");
/* 222 */       Array<XmlReader.Element> rows = tileArray.getChildrenByName("Row");
/* 223 */       TiledMapTileSets tilesets = map.getTileSets();
/* 224 */       TiledMapTileSet currentTileSet = null;
/* 225 */       int firstgid = 0;
/*     */       
/* 227 */       for (int row = 0, rowCount = rows.size; row < rowCount; row++) {
/* 228 */         XmlReader.Element currentRow = (XmlReader.Element)rows.get(row);
/* 229 */         int y = rowCount - 1 - row;
/* 230 */         int x = 0;
/* 231 */         for (int child = 0, childCount = currentRow.getChildCount(); child < childCount; child++) {
/* 232 */           XmlReader.Element currentChild = currentRow.getChild(child);
/* 233 */           String name = currentChild.getName();
/* 234 */           if (name.equals("TileSheet")) {
/* 235 */             currentTileSet = tilesets.getTileSet(currentChild.getAttribute("Ref"));
/* 236 */             firstgid = ((Integer)currentTileSet.getProperties().get("firstgid", Integer.class)).intValue();
/* 237 */           } else if (name.equals("Null")) {
/* 238 */             x += currentChild.getIntAttribute("Count");
/* 239 */           } else if (name.equals("Static")) {
/* 240 */             TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
/* 241 */             cell.setTile(currentTileSet.getTile(firstgid + currentChild.getIntAttribute("Index")));
/* 242 */             layer.setCell(x++, y, cell);
/* 243 */           } else if (name.equals("Animated")) {
/*     */             
/* 245 */             int interval = currentChild.getInt("Interval");
/* 246 */             XmlReader.Element frames = currentChild.getChildByName("Frames");
/* 247 */             Array<StaticTiledMapTile> frameTiles = new Array();
/* 248 */             for (int frameChild = 0, frameChildCount = frames.getChildCount(); frameChild < frameChildCount; frameChild++) {
/* 249 */               XmlReader.Element frame = frames.getChild(frameChild);
/* 250 */               String frameName = frame.getName();
/* 251 */               if (frameName.equals("TileSheet")) {
/* 252 */                 currentTileSet = tilesets.getTileSet(frame.getAttribute("Ref"));
/* 253 */                 firstgid = ((Integer)currentTileSet.getProperties().get("firstgid", Integer.class)).intValue();
/* 254 */               } else if (frameName.equals("Static")) {
/* 255 */                 frameTiles.add(currentTileSet.getTile(firstgid + frame.getIntAttribute("Index")));
/*     */               } 
/*     */             } 
/* 258 */             TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
/* 259 */             cell.setTile((TiledMapTile)new AnimatedTiledMapTile(interval / 1000.0F, frameTiles));
/* 260 */             layer.setCell(x++, y, cell);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 265 */       XmlReader.Element properties = element.getChildByName("Properties");
/* 266 */       if (properties != null) {
/* 267 */         loadProperties(layer.getProperties(), properties);
/*     */       }
/*     */       
/* 270 */       map.getLayers().add(layer);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void loadProperties(MapProperties properties, XmlReader.Element element) {
/* 275 */     if (element.getName().equals("Properties")) {
/* 276 */       for (XmlReader.Element property : element.getChildrenByName("Property")) {
/* 277 */         String key = property.getAttribute("Key", null);
/* 278 */         String type = property.getAttribute("Type", null);
/* 279 */         String value = property.getText();
/*     */         
/* 281 */         if (type.equals("Int32")) {
/* 282 */           properties.put(key, Integer.valueOf(Integer.parseInt(value))); continue;
/* 283 */         }  if (type.equals("String")) {
/* 284 */           properties.put(key, value); continue;
/* 285 */         }  if (type.equals("Boolean")) {
/* 286 */           properties.put(key, Boolean.valueOf(value.equalsIgnoreCase("true"))); continue;
/*     */         } 
/* 288 */         properties.put(key, value);
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private static FileHandle getRelativeFileHandle(FileHandle file, String path) {
/* 295 */     StringTokenizer tokenizer = new StringTokenizer(path, "\\/");
/* 296 */     FileHandle result = file.parent();
/* 297 */     while (tokenizer.hasMoreElements()) {
/* 298 */       String token = tokenizer.nextToken();
/* 299 */       if (token.equals("..")) {
/* 300 */         result = result.parent(); continue;
/*     */       } 
/* 302 */       result = result.child(token);
/*     */     } 
/*     */     
/* 305 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\TideMapLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */