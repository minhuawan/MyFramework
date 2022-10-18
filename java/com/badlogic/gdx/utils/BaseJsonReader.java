package com.badlogic.gdx.utils;

import com.badlogic.gdx.files.FileHandle;
import java.io.InputStream;

public interface BaseJsonReader {
  JsonValue parse(InputStream paramInputStream);
  
  JsonValue parse(FileHandle paramFileHandle);
}


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gd\\utils\BaseJsonReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */