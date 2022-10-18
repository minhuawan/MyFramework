/*     */ package com.badlogic.gdx.graphics.g3d.loader;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.ModelLoader;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.Material;
/*     */ import com.badlogic.gdx.graphics.g3d.Model;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMaterial;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMesh;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMeshPart;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelNode;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelNodePart;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.FloatArray;
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
/*     */ public class ObjLoader
/*     */   extends ModelLoader<ObjLoader.ObjLoaderParameters>
/*     */ {
/*     */   public static boolean logWarning = false;
/*     */   
/*     */   public static class ObjLoaderParameters
/*     */     extends ModelLoader.ModelParameters
/*     */   {
/*     */     public boolean flipV;
/*     */     
/*     */     public ObjLoaderParameters() {}
/*     */     
/*     */     public ObjLoaderParameters(boolean flipV) {
/*  76 */       this.flipV = flipV;
/*     */     }
/*     */   }
/*     */   
/*  80 */   final FloatArray verts = new FloatArray(300);
/*  81 */   final FloatArray norms = new FloatArray(300);
/*  82 */   final FloatArray uvs = new FloatArray(200);
/*  83 */   final Array<Group> groups = new Array(10);
/*     */   
/*     */   public ObjLoader() {
/*  86 */     this(null);
/*     */   }
/*     */   
/*     */   public ObjLoader(FileHandleResolver resolver) {
/*  90 */     super(resolver);
/*     */   }
/*     */ 
/*     */   
/*     */   public Model loadModel(FileHandle fileHandle, boolean flipV) {
/*  95 */     return loadModel(fileHandle, new ObjLoaderParameters(flipV));
/*     */   }
/*     */ 
/*     */   
/*     */   public ModelData loadModelData(FileHandle file, ObjLoaderParameters parameters) {
/* 100 */     return loadModelData(file, (parameters == null) ? false : parameters.flipV);
/*     */   }
/*     */   
/*     */   protected ModelData loadModelData(FileHandle file, boolean flipV) {
/* 104 */     if (logWarning) {
/* 105 */       Gdx.app.error("ObjLoader", "Wavefront (OBJ) is not fully supported, consult the documentation for more information");
/*     */     }
/*     */ 
/*     */     
/* 109 */     MtlLoader mtl = new MtlLoader();
/*     */ 
/*     */ 
/*     */     
/* 113 */     Group activeGroup = new Group("default");
/* 114 */     this.groups.add(activeGroup);
/*     */     
/* 116 */     BufferedReader reader = new BufferedReader(new InputStreamReader(file.read()), 4096);
/* 117 */     int id = 0; try {
/*     */       String line;
/* 119 */       while ((line = reader.readLine()) != null) {
/*     */         
/* 121 */         String[] tokens = line.split("\\s+");
/* 122 */         if (tokens.length < 1)
/*     */           break; 
/* 124 */         if (tokens[0].length() == 0)
/*     */           continue;  char firstChar;
/* 126 */         if ((firstChar = tokens[0].toLowerCase().charAt(0)) == '#')
/*     */           continue; 
/* 128 */         if (firstChar == 'v') {
/* 129 */           if (tokens[0].length() == 1) {
/* 130 */             this.verts.add(Float.parseFloat(tokens[1]));
/* 131 */             this.verts.add(Float.parseFloat(tokens[2]));
/* 132 */             this.verts.add(Float.parseFloat(tokens[3])); continue;
/* 133 */           }  if (tokens[0].charAt(1) == 'n') {
/* 134 */             this.norms.add(Float.parseFloat(tokens[1]));
/* 135 */             this.norms.add(Float.parseFloat(tokens[2]));
/* 136 */             this.norms.add(Float.parseFloat(tokens[3])); continue;
/* 137 */           }  if (tokens[0].charAt(1) == 't') {
/* 138 */             this.uvs.add(Float.parseFloat(tokens[1]));
/* 139 */             this.uvs.add(flipV ? (1.0F - Float.parseFloat(tokens[2])) : Float.parseFloat(tokens[2]));
/*     */           }  continue;
/* 141 */         }  if (firstChar == 'f') {
/*     */           
/* 143 */           Array<Integer> faces = activeGroup.faces;
/* 144 */           for (int j = 1; j < tokens.length - 2; j--) {
/* 145 */             String[] parts = tokens[1].split("/");
/* 146 */             faces.add(Integer.valueOf(getIndex(parts[0], this.verts.size)));
/* 147 */             if (parts.length > 2) {
/* 148 */               if (j == 1) activeGroup.hasNorms = true; 
/* 149 */               faces.add(Integer.valueOf(getIndex(parts[2], this.norms.size)));
/*     */             } 
/* 151 */             if (parts.length > 1 && parts[1].length() > 0) {
/* 152 */               if (j == 1) activeGroup.hasUVs = true; 
/* 153 */               faces.add(Integer.valueOf(getIndex(parts[1], this.uvs.size)));
/*     */             } 
/* 155 */             parts = tokens[++j].split("/");
/* 156 */             faces.add(Integer.valueOf(getIndex(parts[0], this.verts.size)));
/* 157 */             if (parts.length > 2) faces.add(Integer.valueOf(getIndex(parts[2], this.norms.size))); 
/* 158 */             if (parts.length > 1 && parts[1].length() > 0) faces.add(Integer.valueOf(getIndex(parts[1], this.uvs.size))); 
/* 159 */             parts = tokens[++j].split("/");
/* 160 */             faces.add(Integer.valueOf(getIndex(parts[0], this.verts.size)));
/* 161 */             if (parts.length > 2) faces.add(Integer.valueOf(getIndex(parts[2], this.norms.size))); 
/* 162 */             if (parts.length > 1 && parts[1].length() > 0) faces.add(Integer.valueOf(getIndex(parts[1], this.uvs.size))); 
/* 163 */             activeGroup.numFaces++;
/*     */           }  continue;
/* 165 */         }  if (firstChar == 'o' || firstChar == 'g') {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 170 */           if (tokens.length > 1) {
/* 171 */             activeGroup = setActiveGroup(tokens[1]); continue;
/*     */           } 
/* 173 */           activeGroup = setActiveGroup("default"); continue;
/* 174 */         }  if (tokens[0].equals("mtllib")) {
/* 175 */           mtl.load(file.parent().child(tokens[1])); continue;
/* 176 */         }  if (tokens[0].equals("usemtl")) {
/* 177 */           if (tokens.length == 1) {
/* 178 */             activeGroup.materialName = "default"; continue;
/*     */           } 
/* 180 */           activeGroup.materialName = tokens[1].replace('.', '_');
/*     */         } 
/*     */       } 
/* 183 */       reader.close();
/* 184 */     } catch (IOException e) {
/* 185 */       return null;
/*     */     } 
/*     */ 
/*     */     
/* 189 */     for (int i = 0; i < this.groups.size; i++) {
/* 190 */       if (((Group)this.groups.get(i)).numFaces < 1) {
/* 191 */         this.groups.removeIndex(i);
/* 192 */         i--;
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 197 */     if (this.groups.size < 1) return null;
/*     */ 
/*     */     
/* 200 */     int numGroups = this.groups.size;
/*     */     
/* 202 */     ModelData data = new ModelData();
/*     */     
/* 204 */     for (int g = 0; g < numGroups; g++) {
/* 205 */       Group group = (Group)this.groups.get(g);
/* 206 */       Array<Integer> faces = group.faces;
/* 207 */       int numElements = faces.size;
/* 208 */       int numFaces = group.numFaces;
/* 209 */       boolean hasNorms = group.hasNorms;
/* 210 */       boolean hasUVs = group.hasUVs;
/*     */       
/* 212 */       float[] finalVerts = new float[numFaces * 3 * (3 + (hasNorms ? 3 : 0) + (hasUVs ? 2 : 0))];
/*     */       
/* 214 */       for (int j = 0, vi = 0; j < numElements; ) {
/* 215 */         int vertIndex = ((Integer)faces.get(j++)).intValue() * 3;
/* 216 */         finalVerts[vi++] = this.verts.get(vertIndex++);
/* 217 */         finalVerts[vi++] = this.verts.get(vertIndex++);
/* 218 */         finalVerts[vi++] = this.verts.get(vertIndex);
/* 219 */         if (hasNorms) {
/* 220 */           int normIndex = ((Integer)faces.get(j++)).intValue() * 3;
/* 221 */           finalVerts[vi++] = this.norms.get(normIndex++);
/* 222 */           finalVerts[vi++] = this.norms.get(normIndex++);
/* 223 */           finalVerts[vi++] = this.norms.get(normIndex);
/*     */         } 
/* 225 */         if (hasUVs) {
/* 226 */           int uvIndex = ((Integer)faces.get(j++)).intValue() * 2;
/* 227 */           finalVerts[vi++] = this.uvs.get(uvIndex++);
/* 228 */           finalVerts[vi++] = this.uvs.get(uvIndex);
/*     */         } 
/*     */       } 
/*     */       
/* 232 */       int numIndices = (numFaces * 3 >= 32767) ? 0 : (numFaces * 3);
/* 233 */       short[] finalIndices = new short[numIndices];
/*     */       
/* 235 */       if (numIndices > 0) {
/* 236 */         for (int k = 0; k < numIndices; k++) {
/* 237 */           finalIndices[k] = (short)k;
/*     */         }
/*     */       }
/*     */       
/* 241 */       Array<VertexAttribute> attributes = new Array();
/* 242 */       attributes.add(new VertexAttribute(1, 3, "a_position"));
/* 243 */       if (hasNorms) attributes.add(new VertexAttribute(8, 3, "a_normal")); 
/* 244 */       if (hasUVs) attributes.add(new VertexAttribute(16, 2, "a_texCoord0"));
/*     */       
/* 246 */       String stringId = Integer.toString(++id);
/* 247 */       String nodeId = "default".equals(group.name) ? ("node" + stringId) : group.name;
/* 248 */       String meshId = "default".equals(group.name) ? ("mesh" + stringId) : group.name;
/* 249 */       String partId = "default".equals(group.name) ? ("part" + stringId) : group.name;
/* 250 */       ModelNode node = new ModelNode();
/* 251 */       node.id = nodeId;
/* 252 */       node.meshId = meshId;
/* 253 */       node.scale = new Vector3(1.0F, 1.0F, 1.0F);
/* 254 */       node.translation = new Vector3();
/* 255 */       node.rotation = new Quaternion();
/* 256 */       ModelNodePart pm = new ModelNodePart();
/* 257 */       pm.meshPartId = partId;
/* 258 */       pm.materialId = group.materialName;
/* 259 */       node.parts = new ModelNodePart[] { pm };
/* 260 */       ModelMeshPart part = new ModelMeshPart();
/* 261 */       part.id = partId;
/* 262 */       part.indices = finalIndices;
/* 263 */       part.primitiveType = 4;
/* 264 */       ModelMesh mesh = new ModelMesh();
/* 265 */       mesh.id = meshId;
/* 266 */       mesh.attributes = (VertexAttribute[])attributes.toArray(VertexAttribute.class);
/* 267 */       mesh.vertices = finalVerts;
/* 268 */       mesh.parts = new ModelMeshPart[] { part };
/* 269 */       data.nodes.add(node);
/* 270 */       data.meshes.add(mesh);
/* 271 */       ModelMaterial mm = mtl.getMaterial(group.materialName);
/* 272 */       data.materials.add(mm);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 282 */     if (this.verts.size > 0) this.verts.clear(); 
/* 283 */     if (this.norms.size > 0) this.norms.clear(); 
/* 284 */     if (this.uvs.size > 0) this.uvs.clear(); 
/* 285 */     if (this.groups.size > 0) this.groups.clear();
/*     */     
/* 287 */     return data;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Group setActiveGroup(String name) {
/* 293 */     for (Group group1 : this.groups) {
/* 294 */       if (group1.name.equals(name)) return group1; 
/*     */     } 
/* 296 */     Group group = new Group(name);
/* 297 */     this.groups.add(group);
/* 298 */     return group;
/*     */   }
/*     */   
/*     */   private int getIndex(String index, int size) {
/* 302 */     if (index == null || index.length() == 0) return 0; 
/* 303 */     int idx = Integer.parseInt(index);
/* 304 */     if (idx < 0) {
/* 305 */       return size + idx;
/*     */     }
/* 307 */     return idx - 1;
/*     */   }
/*     */   
/*     */   private class Group {
/*     */     final String name;
/*     */     String materialName;
/*     */     Array<Integer> faces;
/*     */     int numFaces;
/*     */     boolean hasNorms;
/*     */     boolean hasUVs;
/*     */     Material mat;
/*     */     
/*     */     Group(String name) {
/* 320 */       this.name = name;
/* 321 */       this.faces = new Array(200);
/* 322 */       this.numFaces = 0;
/* 323 */       this.mat = new Material("");
/* 324 */       this.materialName = "default";
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\loader\ObjLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */