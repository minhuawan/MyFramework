/*     */ package com.badlogic.gdx.tiledmappacker;
/*     */ 
/*     */ import com.badlogic.gdx.ApplicationListener;
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.resolvers.AbsoluteFileHandleResolver;
/*     */ import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
/*     */ import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.maps.MapLayer;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMap;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTile;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
/*     */ import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
/*     */ import com.badlogic.gdx.maps.tiled.TmxMapLoader;
/*     */ import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
/*     */ import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.tools.texturepacker.TexturePacker;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.IntArray;
/*     */ import com.badlogic.gdx.utils.ObjectMap;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.io.File;
/*     */ import java.io.FilenameFilter;
/*     */ import java.io.IOException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.transform.Transformer;
/*     */ import javax.xml.transform.TransformerConfigurationException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ import javax.xml.transform.TransformerFactory;
/*     */ import javax.xml.transform.dom.DOMSource;
/*     */ import javax.xml.transform.stream.StreamResult;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TiledMapPacker
/*     */ {
/*     */   private TexturePacker packer;
/*     */   private TiledMap map;
/*  79 */   private TmxMapLoader mapLoader = new TmxMapLoader((FileHandleResolver)new AbsoluteFileHandleResolver());
/*     */   
/*     */   private TiledMapPackerSettings settings;
/*     */   private static final String TilesetsOutputDir = "tileset";
/*  83 */   static String AtlasOutputName = "packed";
/*     */   
/*  85 */   private HashMap<String, IntArray> tilesetUsedIds = new HashMap<String, IntArray>();
/*     */   
/*     */   private ObjectMap<String, TiledMapTileSet> tilesetsToPack;
/*     */   
/*     */   static File inputDir;
/*     */   
/*     */   static File outputDir;
/*     */   private FileHandle currentDir;
/*     */   
/*     */   private static class TmxFilter
/*     */     implements FilenameFilter
/*     */   {
/*     */     public boolean accept(File dir, String name) {
/*  98 */       return name.endsWith(".tmx");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class DirFilter
/*     */     implements FilenameFilter
/*     */   {
/*     */     public boolean accept(File f, String s) {
/* 108 */       return (new File(f, s)).isDirectory();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public TiledMapPacker() {
/* 114 */     this(new TiledMapPackerSettings());
/*     */   }
/*     */ 
/*     */   
/*     */   public TiledMapPacker(TiledMapPackerSettings settings) {
/* 119 */     this.settings = settings;
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
/*     */   public void processInputDir(TexturePacker.Settings texturePackerSettings) throws IOException {
/* 144 */     FileHandle inputDirHandle = new FileHandle(inputDir.getCanonicalPath());
/* 145 */     File[] mapFilesInCurrentDir = inputDir.listFiles(new TmxFilter());
/* 146 */     this.tilesetsToPack = new ObjectMap();
/*     */ 
/*     */     
/* 149 */     for (File mapFile : mapFilesInCurrentDir) {
/* 150 */       processSingleMap(mapFile, inputDirHandle, texturePackerSettings);
/*     */     }
/*     */     
/* 153 */     processSubdirectories(inputDirHandle, texturePackerSettings);
/*     */     
/* 155 */     boolean combineTilesets = this.settings.combineTilesets;
/* 156 */     if (combineTilesets == true) {
/* 157 */       packTilesets(inputDirHandle, texturePackerSettings);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void processSubdirectories(FileHandle currentDir, TexturePacker.Settings texturePackerSettings) throws IOException {
/* 165 */     File parentPath = new File(currentDir.path());
/* 166 */     File[] directories = parentPath.listFiles(new DirFilter());
/*     */     
/* 168 */     for (File directory : directories) {
/* 169 */       currentDir = new FileHandle(directory.getCanonicalPath());
/* 170 */       File[] mapFilesInCurrentDir = directory.listFiles(new TmxFilter());
/*     */       
/* 172 */       for (File mapFile : mapFilesInCurrentDir) {
/* 173 */         processSingleMap(mapFile, currentDir, texturePackerSettings);
/*     */       }
/*     */       
/* 176 */       processSubdirectories(currentDir, texturePackerSettings);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void processSingleMap(File mapFile, FileHandle dirHandle, TexturePacker.Settings texturePackerSettings) throws IOException {
/* 181 */     boolean combineTilesets = this.settings.combineTilesets;
/* 182 */     if (!combineTilesets) {
/* 183 */       this.tilesetUsedIds = new HashMap<String, IntArray>();
/* 184 */       this.tilesetsToPack = new ObjectMap();
/*     */     } 
/*     */     
/* 187 */     this.map = this.mapLoader.load(mapFile.getCanonicalPath());
/*     */ 
/*     */     
/* 190 */     boolean stripUnusedTiles = this.settings.stripUnusedTiles;
/* 191 */     if (stripUnusedTiles) {
/* 192 */       stripUnusedTiles();
/*     */     } else {
/* 194 */       for (TiledMapTileSet tileset : this.map.getTileSets()) {
/* 195 */         String tilesetName = tileset.getName();
/* 196 */         if (!this.tilesetsToPack.containsKey(tilesetName)) {
/* 197 */           this.tilesetsToPack.put(tilesetName, tileset);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 202 */     if (!combineTilesets) {
/* 203 */       FileHandle tmpHandle = new FileHandle(mapFile.getName());
/* 204 */       this.settings.atlasOutputName = tmpHandle.nameWithoutExtension();
/*     */       
/* 206 */       packTilesets(dirHandle, texturePackerSettings);
/*     */     } 
/*     */     
/* 209 */     FileHandle tmxFile = new FileHandle(mapFile.getCanonicalPath());
/* 210 */     writeUpdatedTMX(this.map, tmxFile);
/*     */   }
/*     */   
/*     */   private void stripUnusedTiles() {
/* 214 */     int mapWidth = ((Integer)this.map.getProperties().get("width", Integer.class)).intValue();
/* 215 */     int mapHeight = ((Integer)this.map.getProperties().get("height", Integer.class)).intValue();
/* 216 */     int numlayers = this.map.getLayers().getCount();
/* 217 */     int bucketSize = mapWidth * mapHeight * numlayers;
/*     */     
/* 219 */     Iterator<MapLayer> it = this.map.getLayers().iterator();
/* 220 */     while (it.hasNext()) {
/* 221 */       MapLayer layer = it.next();
/*     */ 
/*     */       
/* 224 */       if (layer instanceof TiledMapTileLayer) {
/* 225 */         TiledMapTileLayer tlayer = (TiledMapTileLayer)layer;
/*     */         
/* 227 */         for (int y = 0; y < mapHeight; y++) {
/* 228 */           for (int x = 0; x < mapWidth; x++) {
/* 229 */             if (tlayer.getCell(x, y) != null) {
/* 230 */               TiledMapTile tile = tlayer.getCell(x, y).getTile();
/* 231 */               if (tile instanceof AnimatedTiledMapTile) {
/* 232 */                 AnimatedTiledMapTile aTile = (AnimatedTiledMapTile)tile;
/* 233 */                 for (StaticTiledMapTile t : aTile.getFrameTiles()) {
/* 234 */                   addTile((TiledMapTile)t, bucketSize);
/*     */                 }
/*     */               } 
/*     */               
/* 238 */               addTile(tile, bucketSize);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addTile(TiledMapTile tile, int bucketSize) {
/* 247 */     int tileid = tile.getId() & 0x1FFFFFFF;
/* 248 */     String tilesetName = tilesetNameFromTileId(this.map, tileid);
/* 249 */     IntArray usedIds = getUsedIdsBucket(tilesetName, bucketSize);
/* 250 */     usedIds.add(tileid);
/*     */ 
/*     */     
/* 253 */     if (!this.tilesetsToPack.containsKey(tilesetName)) {
/* 254 */       this.tilesetsToPack.put(tilesetName, this.map.getTileSets().getTileSet(tilesetName));
/*     */     }
/*     */   }
/*     */   
/*     */   private String tilesetNameFromTileId(TiledMap map, int tileid) {
/* 259 */     String name = "";
/* 260 */     if (tileid == 0) {
/* 261 */       return "";
/*     */     }
/*     */     
/* 264 */     for (TiledMapTileSet tileset : map.getTileSets()) {
/* 265 */       int firstgid = ((Integer)tileset.getProperties().get("firstgid", Integer.valueOf(-1), Integer.class)).intValue();
/* 266 */       if (firstgid == -1)
/* 267 */         continue;  if (tileid >= firstgid) {
/* 268 */         name = tileset.getName(); continue;
/*     */       } 
/* 270 */       return name;
/*     */     } 
/*     */ 
/*     */     
/* 274 */     return name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IntArray getUsedIdsBucket(String tilesetName, int size) {
/* 283 */     if (this.tilesetUsedIds.containsKey(tilesetName)) {
/* 284 */       return this.tilesetUsedIds.get(tilesetName);
/*     */     }
/*     */     
/* 287 */     if (size <= 0) {
/* 288 */       return null;
/*     */     }
/*     */     
/* 291 */     IntArray bucket = new IntArray(size);
/* 292 */     this.tilesetUsedIds.put(tilesetName, bucket);
/* 293 */     return bucket;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void packTilesets(FileHandle inputDirHandle, TexturePacker.Settings texturePackerSettings) throws IOException {
/* 303 */     this.packer = new TexturePacker(texturePackerSettings);
/*     */     
/* 305 */     for (ObjectMap.Values<TiledMapTileSet> values = this.tilesetsToPack.values().iterator(); values.hasNext(); ) { TiledMapTileSet set = values.next();
/* 306 */       String tilesetName = set.getName();
/* 307 */       System.out.println("Processing tileset " + tilesetName);
/*     */       
/* 309 */       IntArray usedIds = this.settings.stripUnusedTiles ? getUsedIdsBucket(tilesetName, -1) : null;
/*     */       
/* 311 */       int tileWidth = ((Integer)set.getProperties().get("tilewidth", Integer.class)).intValue();
/* 312 */       int tileHeight = ((Integer)set.getProperties().get("tileheight", Integer.class)).intValue();
/* 313 */       int firstgid = ((Integer)set.getProperties().get("firstgid", Integer.class)).intValue();
/* 314 */       String imageName = (String)set.getProperties().get("imagesource", String.class);
/*     */       
/* 316 */       TileSetLayout layout = new TileSetLayout(firstgid, set, inputDirHandle);
/*     */       
/* 318 */       for (int gid = layout.firstgid, i = 0; i < layout.numTiles; gid++, i++) {
/* 319 */         boolean verbose = this.settings.verbose;
/*     */         
/* 321 */         if (usedIds != null && !usedIds.contains(gid)) {
/* 322 */           if (verbose) {
/* 323 */             System.out.println("Stripped id #" + gid + " from tileset \"" + tilesetName + "\"");
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 328 */           Vector2 tileLocation = layout.getLocation(gid);
/* 329 */           BufferedImage tile = new BufferedImage(tileWidth, tileHeight, 6);
/*     */           
/* 331 */           Graphics g = tile.createGraphics();
/* 332 */           g.drawImage(layout.image, 0, 0, tileWidth, tileHeight, (int)tileLocation.x, (int)tileLocation.y, (int)tileLocation.x + tileWidth, (int)tileLocation.y + tileHeight, null);
/*     */ 
/*     */           
/* 335 */           if (verbose) {
/* 336 */             System.out.println("Adding " + tileWidth + "x" + tileHeight + " (" + (int)tileLocation.x + ", " + (int)tileLocation.y + ")");
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 341 */           int adjustedGid = gid - layout.firstgid;
/* 342 */           String separator = "_";
/* 343 */           String regionName = tilesetName + "_" + adjustedGid;
/*     */           
/* 345 */           this.packer.addImage(tile, regionName);
/*     */         } 
/*     */       }  }
/* 348 */      String tilesetOutputDir = outputDir.toString() + "/" + this.settings.tilesetOutputDirectory;
/* 349 */     File relativeTilesetOutputDir = new File(tilesetOutputDir);
/* 350 */     File outputDirTilesets = new File(relativeTilesetOutputDir.getCanonicalPath());
/*     */     
/* 352 */     outputDirTilesets.mkdirs();
/* 353 */     this.packer.pack(outputDirTilesets, this.settings.atlasOutputName + ".atlas");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void writeUpdatedTMX(TiledMap tiledMap, FileHandle tmxFileHandle) throws IOException {
/* 359 */     DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
/*     */     
/*     */     try {
/* 362 */       DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
/* 363 */       Document doc = docBuilder.parse(tmxFileHandle.read());
/*     */       
/* 365 */       Node map = doc.getFirstChild();
/* 366 */       while (map.getNodeType() != 1 || map.getNodeName() != "map") {
/* 367 */         if ((map = map.getNextSibling()) == null) {
/* 368 */           throw new GdxRuntimeException("Couldn't find map node!");
/*     */         }
/*     */       } 
/*     */       
/* 372 */       setProperty(doc, map, "atlas", this.settings.tilesetOutputDirectory + "/" + this.settings.atlasOutputName + ".atlas");
/*     */       
/* 374 */       TransformerFactory transformerFactory = TransformerFactory.newInstance();
/* 375 */       Transformer transformer = transformerFactory.newTransformer();
/* 376 */       DOMSource source = new DOMSource(doc);
/*     */       
/* 378 */       outputDir.mkdirs();
/* 379 */       StreamResult result = new StreamResult(new File(outputDir, tmxFileHandle.name()));
/* 380 */       transformer.transform(source, result);
/*     */     }
/* 382 */     catch (ParserConfigurationException e) {
/* 383 */       throw new RuntimeException("ParserConfigurationException: " + e.getMessage());
/* 384 */     } catch (SAXException e) {
/* 385 */       throw new RuntimeException("SAXException: " + e.getMessage());
/* 386 */     } catch (TransformerConfigurationException e) {
/* 387 */       throw new RuntimeException("TransformerConfigurationException: " + e.getMessage());
/* 388 */     } catch (TransformerException e) {
/* 389 */       throw new RuntimeException("TransformerException: " + e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void setProperty(Document doc, Node parent, String name, String value) {
/* 394 */     Node properties = getFirstChildNodeByName(parent, "properties");
/* 395 */     Node property = getFirstChildByNameAttrValue(properties, "property", "name", name);
/*     */     
/* 397 */     NamedNodeMap attributes = property.getAttributes();
/* 398 */     Node valueNode = attributes.getNamedItem("value");
/* 399 */     if (valueNode == null) {
/* 400 */       valueNode = doc.createAttribute("value");
/* 401 */       valueNode.setNodeValue(value);
/* 402 */       attributes.setNamedItem(valueNode);
/*     */     } else {
/* 404 */       valueNode.setNodeValue(value);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Node getFirstChildNodeByName(Node parent, String child) {
/* 410 */     NodeList childNodes = parent.getChildNodes();
/* 411 */     for (int i = 0; i < childNodes.getLength(); i++) {
/* 412 */       if (childNodes.item(i).getNodeName().equals(child)) {
/* 413 */         return childNodes.item(i);
/*     */       }
/*     */     } 
/*     */     
/* 417 */     Node newNode = parent.getOwnerDocument().createElement(child);
/*     */     
/* 419 */     if (childNodes.item(0) != null) {
/* 420 */       return parent.insertBefore(newNode, childNodes.item(0));
/*     */     }
/* 422 */     return parent.appendChild(newNode);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Node getFirstChildByNameAttrValue(Node node, String childName, String attr, String value) {
/* 428 */     NodeList childNodes = node.getChildNodes();
/* 429 */     for (int i = 0; i < childNodes.getLength(); i++) {
/* 430 */       if (childNodes.item(i).getNodeName().equals(childName)) {
/* 431 */         NamedNodeMap namedNodeMap = childNodes.item(i).getAttributes();
/* 432 */         Node attribute = namedNodeMap.getNamedItem(attr);
/* 433 */         if (attribute.getNodeValue().equals(value)) return childNodes.item(i);
/*     */       
/*     */       } 
/*     */     } 
/* 437 */     Node newNode = node.getOwnerDocument().createElement(childName);
/* 438 */     NamedNodeMap attributes = newNode.getAttributes();
/*     */     
/* 440 */     Attr nodeAttr = node.getOwnerDocument().createAttribute(attr);
/* 441 */     nodeAttr.setNodeValue(value);
/* 442 */     attributes.setNamedItem(nodeAttr);
/*     */     
/* 444 */     if (childNodes.item(0) != null) {
/* 445 */       return node.insertBefore(newNode, childNodes.item(0));
/*     */     }
/* 447 */     return node.appendChild(newNode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 456 */     final TexturePacker.Settings texturePackerSettings = new TexturePacker.Settings();
/* 457 */     texturePackerSettings.paddingX = 2;
/* 458 */     texturePackerSettings.paddingY = 2;
/* 459 */     texturePackerSettings.edgePadding = true;
/* 460 */     texturePackerSettings.duplicatePadding = true;
/* 461 */     texturePackerSettings.bleed = true;
/* 462 */     texturePackerSettings.alias = true;
/* 463 */     texturePackerSettings.useIndexes = true;
/*     */     
/* 465 */     final TiledMapPackerSettings packerSettings = new TiledMapPackerSettings();
/*     */     
/* 467 */     if (args.length == 0) {
/* 468 */       printUsage();
/* 469 */       System.exit(0);
/* 470 */     } else if (args.length == 1) {
/* 471 */       inputDir = new File(args[0]);
/* 472 */       outputDir = new File(inputDir, "../output/");
/* 473 */     } else if (args.length == 2) {
/* 474 */       inputDir = new File(args[0]);
/* 475 */       outputDir = new File(args[1]);
/*     */     } else {
/* 477 */       inputDir = new File(args[0]);
/* 478 */       outputDir = new File(args[1]);
/* 479 */       processExtraArgs(args, packerSettings);
/*     */     } 
/*     */     
/* 482 */     TiledMapPacker packer = new TiledMapPacker(packerSettings);
/* 483 */     LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
/* 484 */     config.forceExit = false;
/* 485 */     config.width = 100;
/* 486 */     config.height = 50;
/* 487 */     config.title = "TiledMapPacker";
/* 488 */     new LwjglApplication(new ApplicationListener()
/*     */         {
/*     */           public void resume() {}
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*     */           public void resize(int width, int height) {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void render() {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void pause() {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void dispose() {}
/*     */ 
/*     */ 
/*     */           
/*     */           public void create() {
/* 512 */             TiledMapPacker packer = new TiledMapPacker(packerSettings);
/*     */             
/* 514 */             if (!TiledMapPacker.inputDir.exists()) {
/* 515 */               System.out.println(TiledMapPacker.inputDir.getAbsolutePath());
/* 516 */               throw new RuntimeException("Input directory does not exist: " + TiledMapPacker.inputDir);
/*     */             } 
/*     */             
/*     */             try {
/* 520 */               packer.processInputDir(texturePackerSettings);
/* 521 */             } catch (IOException e) {
/* 522 */               throw new RuntimeException("Error processing map: " + e.getMessage());
/*     */             } 
/* 524 */             System.out.println("Finished processing.");
/* 525 */             Gdx.app.exit();
/*     */           }
/*     */         },  config);
/*     */   }
/*     */   
/*     */   private static void processExtraArgs(String[] args, TiledMapPackerSettings packerSettings) {
/* 531 */     String stripUnused = "--strip-unused";
/* 532 */     String combineTilesets = "--combine-tilesets";
/* 533 */     String verbose = "-v";
/*     */     
/* 535 */     int length = args.length - 2;
/* 536 */     String[] argsNotDir = new String[length];
/* 537 */     System.arraycopy(args, 2, argsNotDir, 0, length);
/*     */     
/* 539 */     for (String string : argsNotDir) {
/* 540 */       if (stripUnused.equals(string)) {
/* 541 */         packerSettings.stripUnusedTiles = true;
/*     */       }
/* 543 */       else if (combineTilesets.equals(string)) {
/* 544 */         packerSettings.combineTilesets = true;
/*     */       }
/* 546 */       else if (verbose.equals(string)) {
/* 547 */         packerSettings.verbose = true;
/*     */       } else {
/*     */         
/* 550 */         System.out.println("\nOption \"" + string + "\" not recognized.\n");
/* 551 */         printUsage();
/* 552 */         System.exit(0);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void printUsage() {
/* 558 */     System.out.println("Usage: INPUTDIR [OUTPUTDIR] [--strip-unused] [--combine-tilesets] [-v]");
/* 559 */     System.out.println("Processes a directory of Tiled .tmx maps. Unable to process maps with XML");
/* 560 */     System.out.println("tile layer format.");
/* 561 */     System.out.println("  --strip-unused             omits all tiles that are not used. Speeds up");
/* 562 */     System.out.println("                             the processing. Smaller tilesets.");
/* 563 */     System.out.println("  --combine-tilesets         instead of creating a tileset for each map,");
/* 564 */     System.out.println("                             this combines the tilesets into some kind");
/* 565 */     System.out.println("                             of monster tileset. Has problems with tileset");
/* 566 */     System.out.println("                             location. Has problems with nested folders.");
/* 567 */     System.out.println("                             Not recommended.");
/* 568 */     System.out.println("  -v                         outputs which tiles are stripped and included");
/* 569 */     System.out.println();
/*     */   }
/*     */   
/*     */   public static class TiledMapPackerSettings {
/*     */     public boolean stripUnusedTiles = false;
/*     */     public boolean combineTilesets = false;
/*     */     public boolean verbose = false;
/* 576 */     public String tilesetOutputDirectory = "tileset";
/* 577 */     public String atlasOutputName = TiledMapPacker.AtlasOutputName;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tiledmappacker\TiledMapPacker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */