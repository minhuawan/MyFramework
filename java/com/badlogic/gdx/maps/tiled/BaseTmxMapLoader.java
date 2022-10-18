/*     */ package com.badlogic.gdx.maps.tiled;
/*     */ 
/*     */ import com.badlogic.gdx.assets.AssetLoaderParameters;
/*     */ import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Texture;
/*     */ import com.badlogic.gdx.graphics.g2d.TextureRegion;
/*     */ import com.badlogic.gdx.maps.ImageResolver;
/*     */ import com.badlogic.gdx.maps.MapLayer;
/*     */ import com.badlogic.gdx.maps.MapObject;
/*     */ import com.badlogic.gdx.maps.MapProperties;
/*     */ import com.badlogic.gdx.maps.objects.EllipseMapObject;
/*     */ import com.badlogic.gdx.maps.objects.PolygonMapObject;
/*     */ import com.badlogic.gdx.maps.objects.PolylineMapObject;
/*     */ import com.badlogic.gdx.maps.objects.RectangleMapObject;
/*     */ import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
/*     */ import com.badlogic.gdx.math.Polygon;
/*     */ import com.badlogic.gdx.math.Polyline;
/*     */ import com.badlogic.gdx.utils.Base64Coder;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import com.badlogic.gdx.utils.XmlReader;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import java.util.zip.InflaterInputStream;
/*     */ 
/*     */ public abstract class BaseTmxMapLoader<P extends AssetLoaderParameters<TiledMap>>
/*     */   extends AsynchronousAssetLoader<TiledMap, P> {
/*     */   protected static final int FLAG_FLIP_HORIZONTALLY = -2147483648;
/*     */   protected static final int FLAG_FLIP_VERTICALLY = 1073741824;
/*     */   protected static final int FLAG_FLIP_DIAGONALLY = 536870912;
/*     */   protected static final int MASK_CLEAR = -536870912;
/*     */   
/*     */   public static class Parameters
/*     */     extends AssetLoaderParameters<TiledMap> {
/*     */     public boolean generateMipMaps = false;
/*  42 */     public Texture.TextureFilter textureMinFilter = Texture.TextureFilter.Nearest;
/*     */     
/*  44 */     public Texture.TextureFilter textureMagFilter = Texture.TextureFilter.Nearest;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean convertObjectToTileSpace = false;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean flipY = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  57 */   protected XmlReader xml = new XmlReader();
/*     */   
/*     */   protected XmlReader.Element root;
/*     */   
/*     */   protected boolean convertObjectToTileSpace;
/*     */   protected boolean flipY = true;
/*     */   protected int mapTileWidth;
/*     */   protected int mapTileHeight;
/*     */   protected int mapWidthInPixels;
/*     */   protected int mapHeightInPixels;
/*     */   protected TiledMap map;
/*     */   
/*     */   public BaseTmxMapLoader(FileHandleResolver resolver) {
/*  70 */     super(resolver);
/*     */   }
/*     */   
/*     */   protected void loadTileLayer(TiledMap map, XmlReader.Element element) {
/*  74 */     if (element.getName().equals("layer")) {
/*  75 */       int width = element.getIntAttribute("width", 0);
/*  76 */       int height = element.getIntAttribute("height", 0);
/*  77 */       int tileWidth = element.getParent().getIntAttribute("tilewidth", 0);
/*  78 */       int tileHeight = element.getParent().getIntAttribute("tileheight", 0);
/*  79 */       TiledMapTileLayer layer = new TiledMapTileLayer(width, height, tileWidth, tileHeight);
/*     */       
/*  81 */       loadBasicLayerInfo(layer, element);
/*     */       
/*  83 */       int[] ids = getTileIds(element, width, height);
/*  84 */       TiledMapTileSets tilesets = map.getTileSets();
/*  85 */       for (int y = 0; y < height; y++) {
/*  86 */         for (int x = 0; x < width; x++) {
/*  87 */           int id = ids[y * width + x];
/*  88 */           boolean flipHorizontally = ((id & Integer.MIN_VALUE) != 0);
/*  89 */           boolean flipVertically = ((id & 0x40000000) != 0);
/*  90 */           boolean flipDiagonally = ((id & 0x20000000) != 0);
/*     */           
/*  92 */           TiledMapTile tile = tilesets.getTile(id & 0x1FFFFFFF);
/*  93 */           if (tile != null) {
/*  94 */             TiledMapTileLayer.Cell cell = createTileLayerCell(flipHorizontally, flipVertically, flipDiagonally);
/*  95 */             cell.setTile(tile);
/*  96 */             layer.setCell(x, this.flipY ? (height - 1 - y) : y, cell);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 101 */       XmlReader.Element properties = element.getChildByName("properties");
/* 102 */       if (properties != null) {
/* 103 */         loadProperties(layer.getProperties(), properties);
/*     */       }
/* 105 */       map.getLayers().add(layer);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void loadObjectGroup(TiledMap map, XmlReader.Element element) {
/* 110 */     if (element.getName().equals("objectgroup")) {
/* 111 */       String name = element.getAttribute("name", null);
/* 112 */       MapLayer layer = new MapLayer();
/* 113 */       layer.setName(name);
/* 114 */       XmlReader.Element properties = element.getChildByName("properties");
/* 115 */       if (properties != null) {
/* 116 */         loadProperties(layer.getProperties(), properties);
/*     */       }
/*     */       
/* 119 */       for (XmlReader.Element objectElement : element.getChildrenByName("object")) {
/* 120 */         loadObject(map, layer, objectElement);
/*     */       }
/*     */       
/* 123 */       map.getLayers().add(layer);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void loadImageLayer(TiledMap map, XmlReader.Element element, FileHandle tmxFile, ImageResolver imageResolver) {
/* 128 */     if (element.getName().equals("imagelayer")) {
/* 129 */       int x = Integer.parseInt(element.getAttribute("x", "0"));
/* 130 */       int y = Integer.parseInt(element.getAttribute("y", "0"));
/*     */       
/* 132 */       if (this.flipY) y = this.mapHeightInPixels - y;
/*     */       
/* 134 */       TextureRegion texture = null;
/*     */       
/* 136 */       XmlReader.Element image = element.getChildByName("image");
/*     */       
/* 138 */       if (image != null) {
/* 139 */         String source = image.getAttribute("source");
/* 140 */         FileHandle handle = getRelativeFileHandle(tmxFile, source);
/* 141 */         texture = imageResolver.getImage(handle.path());
/* 142 */         y -= texture.getRegionHeight();
/*     */       } 
/*     */       
/* 145 */       TiledMapImageLayer layer = new TiledMapImageLayer(texture, x, y);
/*     */       
/* 147 */       loadBasicLayerInfo(layer, element);
/*     */       
/* 149 */       XmlReader.Element properties = element.getChildByName("properties");
/* 150 */       if (properties != null) {
/* 151 */         loadProperties(layer.getProperties(), properties);
/*     */       }
/*     */       
/* 154 */       map.getLayers().add(layer);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void loadBasicLayerInfo(MapLayer layer, XmlReader.Element element) {
/* 159 */     String name = element.getAttribute("name", null);
/* 160 */     float opacity = Float.parseFloat(element.getAttribute("opacity", "1.0"));
/* 161 */     boolean visible = (element.getIntAttribute("visible", 1) == 1);
/*     */     
/* 163 */     layer.setName(name);
/* 164 */     layer.setOpacity(opacity);
/* 165 */     layer.setVisible(visible);
/*     */   }
/*     */   
/*     */   protected void loadObject(TiledMap map, MapLayer layer, XmlReader.Element element) {
/* 169 */     if (element.getName().equals("object")) {
/* 170 */       EllipseMapObject ellipseMapObject; RectangleMapObject rectangleMapObject; MapObject object = null;
/*     */       
/* 172 */       float scaleX = this.convertObjectToTileSpace ? (1.0F / this.mapTileWidth) : 1.0F;
/* 173 */       float scaleY = this.convertObjectToTileSpace ? (1.0F / this.mapTileHeight) : 1.0F;
/*     */       
/* 175 */       float x = element.getFloatAttribute("x", 0.0F) * scaleX;
/* 176 */       float y = (this.flipY ? (this.mapHeightInPixels - element.getFloatAttribute("y", 0.0F)) : element.getFloatAttribute("y", 0.0F)) * scaleY;
/*     */       
/* 178 */       float width = element.getFloatAttribute("width", 0.0F) * scaleX;
/* 179 */       float height = element.getFloatAttribute("height", 0.0F) * scaleY;
/*     */       
/* 181 */       if (element.getChildCount() > 0) {
/* 182 */         XmlReader.Element child = null;
/* 183 */         if ((child = element.getChildByName("polygon")) != null) {
/* 184 */           String[] points = child.getAttribute("points").split(" ");
/* 185 */           float[] vertices = new float[points.length * 2];
/* 186 */           for (int i = 0; i < points.length; i++) {
/* 187 */             String[] point = points[i].split(",");
/* 188 */             vertices[i * 2] = Float.parseFloat(point[0]) * scaleX;
/* 189 */             vertices[i * 2 + 1] = Float.parseFloat(point[1]) * scaleY * (this.flipY ? -1 : true);
/*     */           } 
/* 191 */           Polygon polygon = new Polygon(vertices);
/* 192 */           polygon.setPosition(x, y);
/* 193 */           PolygonMapObject polygonMapObject = new PolygonMapObject(polygon);
/* 194 */         } else if ((child = element.getChildByName("polyline")) != null) {
/* 195 */           String[] points = child.getAttribute("points").split(" ");
/* 196 */           float[] vertices = new float[points.length * 2];
/* 197 */           for (int i = 0; i < points.length; i++) {
/* 198 */             String[] point = points[i].split(",");
/* 199 */             vertices[i * 2] = Float.parseFloat(point[0]) * scaleX;
/* 200 */             vertices[i * 2 + 1] = Float.parseFloat(point[1]) * scaleY * (this.flipY ? -1 : true);
/*     */           } 
/* 202 */           Polyline polyline = new Polyline(vertices);
/* 203 */           polyline.setPosition(x, y);
/* 204 */           PolylineMapObject polylineMapObject = new PolylineMapObject(polyline);
/* 205 */         } else if ((child = element.getChildByName("ellipse")) != null) {
/* 206 */           ellipseMapObject = new EllipseMapObject(x, this.flipY ? (y - height) : y, width, height);
/*     */         } 
/*     */       } 
/* 209 */       if (ellipseMapObject == null) {
/* 210 */         String gid = null;
/* 211 */         if ((gid = element.getAttribute("gid", null)) != null) {
/* 212 */           int i = (int)Long.parseLong(gid);
/* 213 */           boolean flipHorizontally = ((i & Integer.MIN_VALUE) != 0);
/* 214 */           boolean flipVertically = ((i & 0x40000000) != 0);
/*     */           
/* 216 */           TiledMapTile tile = map.getTileSets().getTile(i & 0x1FFFFFFF);
/* 217 */           TiledMapTileMapObject tiledMapTileMapObject = new TiledMapTileMapObject(tile, flipHorizontally, flipVertically);
/* 218 */           TextureRegion textureRegion = tiledMapTileMapObject.getTextureRegion();
/* 219 */           tiledMapTileMapObject.getProperties().put("gid", Integer.valueOf(i));
/* 220 */           tiledMapTileMapObject.setX(x);
/* 221 */           tiledMapTileMapObject.setY(this.flipY ? y : (y - height));
/* 222 */           float objectWidth = element.getFloatAttribute("width", textureRegion.getRegionWidth());
/* 223 */           float objectHeight = element.getFloatAttribute("height", textureRegion.getRegionHeight());
/* 224 */           tiledMapTileMapObject.setScaleX(scaleX * objectWidth / textureRegion.getRegionWidth());
/* 225 */           tiledMapTileMapObject.setScaleY(scaleY * objectHeight / textureRegion.getRegionHeight());
/* 226 */           tiledMapTileMapObject.setRotation(element.getFloatAttribute("rotation", 0.0F));
/* 227 */           TiledMapTileMapObject tiledMapTileMapObject1 = tiledMapTileMapObject;
/*     */         } else {
/* 229 */           rectangleMapObject = new RectangleMapObject(x, this.flipY ? (y - height) : y, width, height);
/*     */         } 
/*     */       } 
/* 232 */       rectangleMapObject.setName(element.getAttribute("name", null));
/* 233 */       String rotation = element.getAttribute("rotation", null);
/* 234 */       if (rotation != null) {
/* 235 */         rectangleMapObject.getProperties().put("rotation", Float.valueOf(Float.parseFloat(rotation)));
/*     */       }
/* 237 */       String type = element.getAttribute("type", null);
/* 238 */       if (type != null) {
/* 239 */         rectangleMapObject.getProperties().put("type", type);
/*     */       }
/* 241 */       int id = element.getIntAttribute("id", 0);
/* 242 */       if (id != 0) {
/* 243 */         rectangleMapObject.getProperties().put("id", Integer.valueOf(id));
/*     */       }
/* 245 */       rectangleMapObject.getProperties().put("x", Float.valueOf(x));
/*     */       
/* 247 */       if (rectangleMapObject instanceof TiledMapTileMapObject) {
/* 248 */         rectangleMapObject.getProperties().put("y", Float.valueOf(y));
/*     */       } else {
/* 250 */         rectangleMapObject.getProperties().put("y", Float.valueOf(this.flipY ? (y - height) : y));
/*     */       } 
/* 252 */       rectangleMapObject.getProperties().put("width", Float.valueOf(width));
/* 253 */       rectangleMapObject.getProperties().put("height", Float.valueOf(height));
/* 254 */       rectangleMapObject.setVisible((element.getIntAttribute("visible", 1) == 1));
/* 255 */       XmlReader.Element properties = element.getChildByName("properties");
/* 256 */       if (properties != null) {
/* 257 */         loadProperties(rectangleMapObject.getProperties(), properties);
/*     */       }
/* 259 */       layer.getObjects().add((MapObject)rectangleMapObject);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void loadProperties(MapProperties properties, XmlReader.Element element) {
/* 264 */     if (element == null)
/* 265 */       return;  if (element.getName().equals("properties")) {
/* 266 */       for (XmlReader.Element property : element.getChildrenByName("property")) {
/* 267 */         String name = property.getAttribute("name", null);
/* 268 */         String value = property.getAttribute("value", null);
/* 269 */         String type = property.getAttribute("type", null);
/* 270 */         if (value == null) {
/* 271 */           value = property.getText();
/*     */         }
/* 273 */         Object castValue = castProperty(name, value, type);
/* 274 */         properties.put(name, castValue);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private Object castProperty(String name, String value, String type) {
/* 280 */     if (type == null)
/* 281 */       return value; 
/* 282 */     if (type.equals("int"))
/* 283 */       return Integer.valueOf(value); 
/* 284 */     if (type.equals("float"))
/* 285 */       return Float.valueOf(value); 
/* 286 */     if (type.equals("bool")) {
/* 287 */       return Boolean.valueOf(value);
/*     */     }
/* 289 */     throw new GdxRuntimeException("Wrong type given for property " + name + ", given : " + type + ", supported : string, bool, int, float");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected TiledMapTileLayer.Cell createTileLayerCell(boolean flipHorizontally, boolean flipVertically, boolean flipDiagonally) {
/* 295 */     TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
/* 296 */     if (flipDiagonally) {
/* 297 */       if (flipHorizontally && flipVertically) {
/* 298 */         cell.setFlipHorizontally(true);
/* 299 */         cell.setRotation(3);
/* 300 */       } else if (flipHorizontally) {
/* 301 */         cell.setRotation(3);
/* 302 */       } else if (flipVertically) {
/* 303 */         cell.setRotation(1);
/*     */       } else {
/* 305 */         cell.setFlipVertically(true);
/* 306 */         cell.setRotation(3);
/*     */       } 
/*     */     } else {
/* 309 */       cell.setFlipHorizontally(flipHorizontally);
/* 310 */       cell.setFlipVertically(flipVertically);
/*     */     } 
/* 312 */     return cell;
/*     */   }
/*     */   
/*     */   public static int[] getTileIds(XmlReader.Element element, int width, int height) {
/* 316 */     XmlReader.Element data = element.getChildByName("data");
/* 317 */     String encoding = data.getAttribute("encoding", null);
/* 318 */     if (encoding == null) {
/* 319 */       throw new GdxRuntimeException("Unsupported encoding (XML) for TMX Layer Data");
/*     */     }
/* 321 */     int[] ids = new int[width * height];
/* 322 */     if (encoding.equals("csv")) {
/* 323 */       String[] array = data.getText().split(",");
/* 324 */       for (int i = 0; i < array.length; i++) {
/* 325 */         ids[i] = (int)Long.parseLong(array[i].trim());
/*     */       }
/*     */     }
/* 328 */     else if (encoding.equals("base64")) {
/* 329 */       InputStream is = null;
/*     */       try {
/* 331 */         String compression = data.getAttribute("compression", null);
/* 332 */         byte[] bytes = Base64Coder.decode(data.getText());
/* 333 */         if (compression == null) {
/* 334 */           is = new ByteArrayInputStream(bytes);
/* 335 */         } else if (compression.equals("gzip")) {
/* 336 */           is = new BufferedInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes), bytes.length));
/* 337 */         } else if (compression.equals("zlib")) {
/* 338 */           is = new BufferedInputStream(new InflaterInputStream(new ByteArrayInputStream(bytes)));
/*     */         } else {
/* 340 */           throw new GdxRuntimeException("Unrecognised compression (" + compression + ") for TMX Layer Data");
/*     */         } 
/* 342 */         byte[] temp = new byte[4];
/* 343 */         for (int y = 0; y < height; y++) {
/* 344 */           for (int x = 0; x < width; x++) {
/* 345 */             int read = is.read(temp);
/* 346 */             while (read < temp.length) {
/* 347 */               int curr = is.read(temp, read, temp.length - read);
/* 348 */               if (curr == -1)
/* 349 */                 break;  read += curr;
/*     */             } 
/* 351 */             if (read != temp.length)
/* 352 */               throw new GdxRuntimeException("Error Reading TMX Layer Data: Premature end of tile data"); 
/* 353 */             ids[y * width + x] = unsignedByteToInt(temp[0]) | unsignedByteToInt(temp[1]) << 8 | 
/* 354 */               unsignedByteToInt(temp[2]) << 16 | unsignedByteToInt(temp[3]) << 24;
/*     */           } 
/*     */         } 
/* 357 */       } catch (IOException e) {
/* 358 */         throw new GdxRuntimeException("Error Reading TMX Layer Data - IOException: " + e.getMessage());
/*     */       } finally {
/* 360 */         StreamUtils.closeQuietly(is);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 365 */       throw new GdxRuntimeException("Unrecognised encoding (" + encoding + ") for TMX Layer Data");
/*     */     } 
/*     */     
/* 368 */     return ids;
/*     */   }
/*     */   
/*     */   protected static int unsignedByteToInt(byte b) {
/* 372 */     return b & 0xFF;
/*     */   }
/*     */   
/*     */   protected static FileHandle getRelativeFileHandle(FileHandle file, String path) {
/* 376 */     StringTokenizer tokenizer = new StringTokenizer(path, "\\/");
/* 377 */     FileHandle result = file.parent();
/* 378 */     while (tokenizer.hasMoreElements()) {
/* 379 */       String token = tokenizer.nextToken();
/* 380 */       if (token.equals("..")) {
/* 381 */         result = result.parent(); continue;
/*     */       } 
/* 383 */       result = result.child(token);
/*     */     } 
/*     */     
/* 386 */     return result;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\BaseTmxMapLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */