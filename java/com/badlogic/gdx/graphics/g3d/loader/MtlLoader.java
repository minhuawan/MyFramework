/*     */ package com.badlogic.gdx.graphics.g3d.loader;
/*     */ 
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMaterial;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelTexture;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
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
/*     */ class MtlLoader
/*     */ {
/* 330 */   public Array<ModelMaterial> materials = new Array();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void load(FileHandle file) {
/* 336 */     String curMatName = "default";
/* 337 */     Color difcolor = Color.WHITE;
/* 338 */     Color speccolor = Color.WHITE;
/* 339 */     float opacity = 1.0F;
/* 340 */     float shininess = 0.0F;
/* 341 */     String texFilename = null;
/*     */     
/* 343 */     if (file == null || !file.exists())
/*     */       return; 
/* 345 */     BufferedReader reader = new BufferedReader(new InputStreamReader(file.read()), 4096); try {
/*     */       String line;
/* 347 */       while ((line = reader.readLine()) != null) {
/*     */         
/* 349 */         if (line.length() > 0 && line.charAt(0) == '\t') line = line.substring(1).trim();
/*     */         
/* 351 */         String[] tokens = line.split("\\s+");
/*     */         
/* 353 */         if (tokens[0].length() == 0)
/*     */           continue; 
/* 355 */         if (tokens[0].charAt(0) == '#') {
/*     */           continue;
/*     */         }
/* 358 */         String key = tokens[0].toLowerCase();
/* 359 */         if (key.equals("newmtl")) {
/* 360 */           ModelMaterial modelMaterial = new ModelMaterial();
/* 361 */           modelMaterial.id = curMatName;
/* 362 */           modelMaterial.diffuse = new Color(difcolor);
/* 363 */           modelMaterial.specular = new Color(speccolor);
/* 364 */           modelMaterial.opacity = opacity;
/* 365 */           modelMaterial.shininess = shininess;
/* 366 */           if (texFilename != null) {
/* 367 */             ModelTexture tex = new ModelTexture();
/* 368 */             tex.usage = 2;
/* 369 */             tex.fileName = new String(texFilename);
/* 370 */             if (modelMaterial.textures == null) modelMaterial.textures = new Array(1); 
/* 371 */             modelMaterial.textures.add(tex);
/*     */           } 
/* 373 */           this.materials.add(modelMaterial);
/*     */           
/* 375 */           if (tokens.length > 1) {
/* 376 */             curMatName = tokens[1];
/* 377 */             curMatName = curMatName.replace('.', '_');
/*     */           } else {
/* 379 */             curMatName = "default";
/*     */           } 
/* 381 */           difcolor = Color.WHITE;
/* 382 */           speccolor = Color.WHITE;
/* 383 */           opacity = 1.0F;
/* 384 */           shininess = 0.0F; continue;
/* 385 */         }  if (key.equals("kd") || key.equals("ks")) {
/*     */           
/* 387 */           float r = Float.parseFloat(tokens[1]);
/* 388 */           float g = Float.parseFloat(tokens[2]);
/* 389 */           float b = Float.parseFloat(tokens[3]);
/* 390 */           float a = 1.0F;
/* 391 */           if (tokens.length > 4) a = Float.parseFloat(tokens[4]);
/*     */           
/* 393 */           if (tokens[0].toLowerCase().equals("kd")) {
/* 394 */             difcolor = new Color();
/* 395 */             difcolor.set(r, g, b, a); continue;
/*     */           } 
/* 397 */           speccolor = new Color();
/* 398 */           speccolor.set(r, g, b, a); continue;
/*     */         } 
/* 400 */         if (key.equals("tr") || key.equals("d")) {
/* 401 */           opacity = Float.parseFloat(tokens[1]); continue;
/* 402 */         }  if (key.equals("ns")) {
/* 403 */           shininess = Float.parseFloat(tokens[1]); continue;
/* 404 */         }  if (key.equals("map_kd")) {
/* 405 */           texFilename = file.parent().child(tokens[1]).path();
/*     */         }
/*     */       } 
/*     */       
/* 409 */       reader.close();
/* 410 */     } catch (IOException e) {
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 415 */     ModelMaterial mat = new ModelMaterial();
/* 416 */     mat.id = curMatName;
/* 417 */     mat.diffuse = new Color(difcolor);
/* 418 */     mat.specular = new Color(speccolor);
/* 419 */     mat.opacity = opacity;
/* 420 */     mat.shininess = shininess;
/* 421 */     if (texFilename != null) {
/* 422 */       ModelTexture tex = new ModelTexture();
/* 423 */       tex.usage = 2;
/* 424 */       tex.fileName = new String(texFilename);
/* 425 */       if (mat.textures == null) mat.textures = new Array(1); 
/* 426 */       mat.textures.add(tex);
/*     */     } 
/* 428 */     this.materials.add(mat);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ModelMaterial getMaterial(String name) {
/* 434 */     for (ModelMaterial m : this.materials) {
/* 435 */       if (m.id.equals(name)) return m; 
/* 436 */     }  ModelMaterial mat = new ModelMaterial();
/* 437 */     mat.id = name;
/* 438 */     mat.diffuse = new Color(Color.WHITE);
/* 439 */     this.materials.add(mat);
/* 440 */     return mat;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\loader\MtlLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */