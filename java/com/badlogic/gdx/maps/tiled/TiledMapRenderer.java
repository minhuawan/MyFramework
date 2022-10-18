package com.badlogic.gdx.maps.tiled;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapRenderer;

public interface TiledMapRenderer extends MapRenderer {
  void renderObjects(MapLayer paramMapLayer);
  
  void renderObject(MapObject paramMapObject);
  
  void renderTileLayer(TiledMapTileLayer paramTiledMapTileLayer);
  
  void renderImageLayer(TiledMapImageLayer paramTiledMapImageLayer);
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\maps\tiled\TiledMapRenderer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */