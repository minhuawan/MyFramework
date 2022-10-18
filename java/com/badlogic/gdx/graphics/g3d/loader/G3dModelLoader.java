/*     */ package com.badlogic.gdx.graphics.g3d.loader;
/*     */ 
/*     */ import com.badlogic.gdx.assets.loaders.FileHandleResolver;
/*     */ import com.badlogic.gdx.assets.loaders.ModelLoader;
/*     */ import com.badlogic.gdx.files.FileHandle;
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.VertexAttribute;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelAnimation;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelData;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMaterial;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMesh;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelMeshPart;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelNode;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelNodeAnimation;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelNodeKeyframe;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelNodePart;
/*     */ import com.badlogic.gdx.graphics.g3d.model.data.ModelTexture;
/*     */ import com.badlogic.gdx.math.Matrix4;
/*     */ import com.badlogic.gdx.math.Quaternion;
/*     */ import com.badlogic.gdx.math.Vector2;
/*     */ import com.badlogic.gdx.math.Vector3;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.ArrayMap;
/*     */ import com.badlogic.gdx.utils.BaseJsonReader;
/*     */ import com.badlogic.gdx.utils.GdxRuntimeException;
/*     */ import com.badlogic.gdx.utils.JsonValue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class G3dModelLoader
/*     */   extends ModelLoader<ModelLoader.ModelParameters>
/*     */ {
/*     */   public static final short VERSION_HI = 0;
/*     */   public static final short VERSION_LO = 1;
/*     */   protected final BaseJsonReader reader;
/*     */   private final Quaternion tempQ;
/*     */   
/*     */   public G3dModelLoader(BaseJsonReader reader) {
/*  51 */     this(reader, null);
/*     */   }
/*     */   
/*     */   public G3dModelLoader(BaseJsonReader reader, FileHandleResolver resolver) {
/*  55 */     super(resolver);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 288 */     this.tempQ = new Quaternion(); this.reader = reader;
/*     */   }
/*     */   public ModelData loadModelData(FileHandle fileHandle, ModelLoader.ModelParameters parameters) { return parseModel(fileHandle); }
/* 291 */   public ModelData parseModel(FileHandle handle) { JsonValue json = this.reader.parse(handle); ModelData model = new ModelData(); JsonValue version = json.require("version"); model.version[0] = version.getShort(0); model.version[1] = version.getShort(1); if (model.version[0] != 0 || model.version[1] != 1) throw new GdxRuntimeException("Model version not supported");  model.id = json.getString("id", ""); parseMeshes(model, json); parseMaterials(model, json, handle.parent().path()); parseNodes(model, json); parseAnimations(model, json); return model; } private void parseMeshes(ModelData model, JsonValue json) { JsonValue meshes = json.get("meshes"); if (meshes != null) { model.meshes.ensureCapacity(meshes.size); for (JsonValue mesh = meshes.child; mesh != null; mesh = mesh.next) { ModelMesh jsonMesh = new ModelMesh(); String id = mesh.getString("id", ""); jsonMesh.id = id; JsonValue attributes = mesh.require("attributes"); jsonMesh.attributes = parseAttributes(attributes); jsonMesh.vertices = mesh.require("vertices").asFloatArray(); JsonValue meshParts = mesh.require("parts"); Array<ModelMeshPart> parts = new Array(); for (JsonValue meshPart = meshParts.child; meshPart != null; meshPart = meshPart.next) { ModelMeshPart jsonPart = new ModelMeshPart(); String partId = meshPart.getString("id", null); if (partId == null) throw new GdxRuntimeException("Not id given for mesh part");  for (ModelMeshPart other : parts) { if (other.id.equals(partId)) throw new GdxRuntimeException("Mesh part with id '" + partId + "' already in defined");  }  jsonPart.id = partId; String type = meshPart.getString("type", null); if (type == null) throw new GdxRuntimeException("No primitive type given for mesh part '" + partId + "'");  jsonPart.primitiveType = parseType(type); jsonPart.indices = meshPart.require("indices").asShortArray(); parts.add(jsonPart); }  jsonMesh.parts = (ModelMeshPart[])parts.toArray(ModelMeshPart.class); model.meshes.add(jsonMesh); }  }  } private int parseType(String type) { if (type.equals("TRIANGLES")) return 4;  if (type.equals("LINES")) return 1;  if (type.equals("POINTS")) return 0;  if (type.equals("TRIANGLE_STRIP")) return 5;  if (type.equals("LINE_STRIP")) return 3;  throw new GdxRuntimeException("Unknown primitive type '" + type + "', should be one of triangle, trianglestrip, line, linestrip, lineloop or point"); } private VertexAttribute[] parseAttributes(JsonValue attributes) { Array<VertexAttribute> vertexAttributes = new Array(); int unit = 0; int blendWeightCount = 0; for (JsonValue value = attributes.child; value != null; value = value.next) { String attribute = value.asString(); String attr = attribute; if (attr.equals("POSITION")) { vertexAttributes.add(VertexAttribute.Position()); } else if (attr.equals("NORMAL")) { vertexAttributes.add(VertexAttribute.Normal()); } else if (attr.equals("COLOR")) { vertexAttributes.add(VertexAttribute.ColorUnpacked()); } else if (attr.equals("COLORPACKED")) { vertexAttributes.add(VertexAttribute.ColorPacked()); } else if (attr.equals("TANGENT")) { vertexAttributes.add(VertexAttribute.Tangent()); } else if (attr.equals("BINORMAL")) { vertexAttributes.add(VertexAttribute.Binormal()); } else if (attr.startsWith("TEXCOORD")) { vertexAttributes.add(VertexAttribute.TexCoords(unit++)); } else if (attr.startsWith("BLENDWEIGHT")) { vertexAttributes.add(VertexAttribute.BoneWeight(blendWeightCount++)); } else { throw new GdxRuntimeException("Unknown vertex attribute '" + attr + "', should be one of position, normal, uv, tangent or binormal"); }  }  return (VertexAttribute[])vertexAttributes.toArray(VertexAttribute.class); } private ModelNode parseNodesRecursively(JsonValue json) { ModelNode jsonNode = new ModelNode();
/*     */     
/* 293 */     String id = json.getString("id", null);
/* 294 */     if (id == null) throw new GdxRuntimeException("Node id missing."); 
/* 295 */     jsonNode.id = id;
/*     */     
/* 297 */     JsonValue translation = json.get("translation");
/* 298 */     if (translation != null && translation.size != 3) throw new GdxRuntimeException("Node translation incomplete"); 
/* 299 */     jsonNode
/* 300 */       .translation = (translation == null) ? null : new Vector3(translation.getFloat(0), translation.getFloat(1), translation.getFloat(2));
/*     */     
/* 302 */     JsonValue rotation = json.get("rotation");
/* 303 */     if (rotation != null && rotation.size != 4) throw new GdxRuntimeException("Node rotation incomplete"); 
/* 304 */     jsonNode
/* 305 */       .rotation = (rotation == null) ? null : new Quaternion(rotation.getFloat(0), rotation.getFloat(1), rotation.getFloat(2), rotation.getFloat(3));
/*     */     
/* 307 */     JsonValue scale = json.get("scale");
/* 308 */     if (scale != null && scale.size != 3) throw new GdxRuntimeException("Node scale incomplete"); 
/* 309 */     jsonNode.scale = (scale == null) ? null : new Vector3(scale.getFloat(0), scale.getFloat(1), scale.getFloat(2));
/*     */     
/* 311 */     String meshId = json.getString("mesh", null);
/* 312 */     if (meshId != null) jsonNode.meshId = meshId;
/*     */     
/* 314 */     JsonValue materials = json.get("parts");
/* 315 */     if (materials != null) {
/* 316 */       jsonNode.parts = new ModelNodePart[materials.size];
/* 317 */       int i = 0;
/* 318 */       for (JsonValue material = materials.child; material != null; material = material.next, i++) {
/* 319 */         ModelNodePart nodePart = new ModelNodePart();
/*     */         
/* 321 */         String meshPartId = material.getString("meshpartid", null);
/* 322 */         String materialId = material.getString("materialid", null);
/* 323 */         if (meshPartId == null || materialId == null) {
/* 324 */           throw new GdxRuntimeException("Node " + id + " part is missing meshPartId or materialId");
/*     */         }
/* 326 */         nodePart.materialId = materialId;
/* 327 */         nodePart.meshPartId = meshPartId;
/*     */         
/* 329 */         JsonValue bones = material.get("bones");
/* 330 */         if (bones != null) {
/* 331 */           nodePart.bones = new ArrayMap(true, bones.size, String.class, Matrix4.class);
/* 332 */           int j = 0;
/* 333 */           for (JsonValue bone = bones.child; bone != null; bone = bone.next, j++) {
/* 334 */             String nodeId = bone.getString("node", null);
/* 335 */             if (nodeId == null) throw new GdxRuntimeException("Bone node ID missing");
/*     */             
/* 337 */             Matrix4 transform = new Matrix4();
/*     */             
/* 339 */             JsonValue val = bone.get("translation");
/* 340 */             if (val != null && val.size >= 3) transform.translate(val.getFloat(0), val.getFloat(1), val.getFloat(2));
/*     */             
/* 342 */             val = bone.get("rotation");
/* 343 */             if (val != null && val.size >= 4) {
/* 344 */               transform.rotate(this.tempQ.set(val.getFloat(0), val.getFloat(1), val.getFloat(2), val.getFloat(3)));
/*     */             }
/* 346 */             val = bone.get("scale");
/* 347 */             if (val != null && val.size >= 3) transform.scale(val.getFloat(0), val.getFloat(1), val.getFloat(2));
/*     */             
/* 349 */             nodePart.bones.put(nodeId, transform);
/*     */           } 
/*     */         } 
/*     */         
/* 353 */         jsonNode.parts[i] = nodePart;
/*     */       } 
/*     */     } 
/*     */     
/* 357 */     JsonValue children = json.get("children");
/* 358 */     if (children != null) {
/* 359 */       jsonNode.children = new ModelNode[children.size];
/*     */       
/* 361 */       int i = 0;
/* 362 */       for (JsonValue child = children.child; child != null; child = child.next, i++) {
/* 363 */         jsonNode.children[i] = parseNodesRecursively(child);
/*     */       }
/*     */     } 
/*     */     
/* 367 */     return jsonNode; }
/*     */   private void parseMaterials(ModelData model, JsonValue json, String materialDir) { JsonValue materials = json.get("materials"); if (materials != null) { model.materials.ensureCapacity(materials.size); for (JsonValue material = materials.child; material != null; material = material.next) { ModelMaterial jsonMaterial = new ModelMaterial(); String id = material.getString("id", null); if (id == null) throw new GdxRuntimeException("Material needs an id.");  jsonMaterial.id = id; JsonValue diffuse = material.get("diffuse"); if (diffuse != null) jsonMaterial.diffuse = parseColor(diffuse);  JsonValue ambient = material.get("ambient"); if (ambient != null) jsonMaterial.ambient = parseColor(ambient);  JsonValue emissive = material.get("emissive"); if (emissive != null) jsonMaterial.emissive = parseColor(emissive);  JsonValue specular = material.get("specular"); if (specular != null) jsonMaterial.specular = parseColor(specular);  JsonValue reflection = material.get("reflection"); if (reflection != null) jsonMaterial.reflection = parseColor(reflection);  jsonMaterial.shininess = material.getFloat("shininess", 0.0F); jsonMaterial.opacity = material.getFloat("opacity", 1.0F); JsonValue textures = material.get("textures"); if (textures != null) for (JsonValue texture = textures.child; texture != null; texture = texture.next) { ModelTexture jsonTexture = new ModelTexture(); String textureId = texture.getString("id", null); if (textureId == null) throw new GdxRuntimeException("Texture has no id.");  jsonTexture.id = textureId; String fileName = texture.getString("filename", null); if (fileName == null) throw new GdxRuntimeException("Texture needs filename.");  jsonTexture.fileName = materialDir + ((materialDir.length() == 0 || materialDir.endsWith("/")) ? "" : "/") + fileName; jsonTexture.uvTranslation = readVector2(texture.get("uvTranslation"), 0.0F, 0.0F); jsonTexture.uvScaling = readVector2(texture.get("uvScaling"), 1.0F, 1.0F); String textureType = texture.getString("type", null); if (textureType == null) throw new GdxRuntimeException("Texture needs type.");  jsonTexture.usage = parseTextureUsage(textureType); if (jsonMaterial.textures == null) jsonMaterial.textures = new Array();  jsonMaterial.textures.add(jsonTexture); }   model.materials.add(jsonMaterial); }  }  }
/*     */   private int parseTextureUsage(String value) { if (value.equalsIgnoreCase("AMBIENT")) return 4;  if (value.equalsIgnoreCase("BUMP")) return 8;  if (value.equalsIgnoreCase("DIFFUSE")) return 2;  if (value.equalsIgnoreCase("EMISSIVE")) return 3;  if (value.equalsIgnoreCase("NONE")) return 1;  if (value.equalsIgnoreCase("NORMAL")) return 7;  if (value.equalsIgnoreCase("REFLECTION")) return 10;  if (value.equalsIgnoreCase("SHININESS")) return 6;  if (value.equalsIgnoreCase("SPECULAR")) return 5;  if (value.equalsIgnoreCase("TRANSPARENCY")) return 9;  return 0; }
/*     */   private Color parseColor(JsonValue colorArray) { if (colorArray.size >= 3) return new Color(colorArray.getFloat(0), colorArray.getFloat(1), colorArray.getFloat(2), 1.0F);  throw new GdxRuntimeException("Expected Color values <> than three."); }
/* 371 */   private Vector2 readVector2(JsonValue vectorArray, float x, float y) { if (vectorArray == null) return new Vector2(x, y);  if (vectorArray.size == 2) return new Vector2(vectorArray.getFloat(0), vectorArray.getFloat(1));  throw new GdxRuntimeException("Expected Vector2 values <> than two."); } private Array<ModelNode> parseNodes(ModelData model, JsonValue json) { JsonValue nodes = json.get("nodes"); if (nodes != null) { model.nodes.ensureCapacity(nodes.size); for (JsonValue node = nodes.child; node != null; node = node.next) model.nodes.add(parseNodesRecursively(node));  }  return model.nodes; } private void parseAnimations(ModelData model, JsonValue json) { JsonValue animations = json.get("animations");
/* 372 */     if (animations == null)
/*     */       return; 
/* 374 */     model.animations.ensureCapacity(animations.size);
/*     */     
/* 376 */     for (JsonValue anim = animations.child; anim != null; anim = anim.next) {
/* 377 */       JsonValue nodes = anim.get("bones");
/* 378 */       if (nodes != null) {
/* 379 */         ModelAnimation animation = new ModelAnimation();
/* 380 */         model.animations.add(animation);
/* 381 */         animation.nodeAnimations.ensureCapacity(nodes.size);
/* 382 */         animation.id = anim.getString("id");
/* 383 */         for (JsonValue node = nodes.child; node != null; node = node.next) {
/* 384 */           ModelNodeAnimation nodeAnim = new ModelNodeAnimation();
/* 385 */           animation.nodeAnimations.add(nodeAnim);
/* 386 */           nodeAnim.nodeId = node.getString("boneId");
/*     */ 
/*     */           
/* 389 */           JsonValue keyframes = node.get("keyframes");
/* 390 */           if (keyframes != null && keyframes.isArray()) {
/* 391 */             for (JsonValue keyframe = keyframes.child; keyframe != null; keyframe = keyframe.next) {
/* 392 */               float keytime = keyframe.getFloat("keytime", 0.0F) / 1000.0F;
/* 393 */               JsonValue translation = keyframe.get("translation");
/* 394 */               if (translation != null && translation.size == 3) {
/* 395 */                 if (nodeAnim.translation == null)
/* 396 */                   nodeAnim.translation = new Array(); 
/* 397 */                 ModelNodeKeyframe<Vector3> tkf = new ModelNodeKeyframe();
/* 398 */                 tkf.keytime = keytime;
/* 399 */                 tkf.value = new Vector3(translation.getFloat(0), translation.getFloat(1), translation.getFloat(2));
/* 400 */                 nodeAnim.translation.add(tkf);
/*     */               } 
/* 402 */               JsonValue rotation = keyframe.get("rotation");
/* 403 */               if (rotation != null && rotation.size == 4) {
/* 404 */                 if (nodeAnim.rotation == null)
/* 405 */                   nodeAnim.rotation = new Array(); 
/* 406 */                 ModelNodeKeyframe<Quaternion> rkf = new ModelNodeKeyframe();
/* 407 */                 rkf.keytime = keytime;
/* 408 */                 rkf.value = new Quaternion(rotation.getFloat(0), rotation.getFloat(1), rotation.getFloat(2), rotation.getFloat(3));
/* 409 */                 nodeAnim.rotation.add(rkf);
/*     */               } 
/* 411 */               JsonValue scale = keyframe.get("scale");
/* 412 */               if (scale != null && scale.size == 3) {
/* 413 */                 if (nodeAnim.scaling == null)
/* 414 */                   nodeAnim.scaling = new Array(); 
/* 415 */                 ModelNodeKeyframe<Vector3> skf = new ModelNodeKeyframe();
/* 416 */                 skf.keytime = keytime;
/* 417 */                 skf.value = new Vector3(scale.getFloat(0), scale.getFloat(1), scale.getFloat(2));
/* 418 */                 nodeAnim.scaling.add(skf);
/*     */               } 
/*     */             } 
/*     */           } else {
/* 422 */             JsonValue translationKF = node.get("translation");
/* 423 */             if (translationKF != null && translationKF.isArray()) {
/* 424 */               nodeAnim.translation = new Array();
/* 425 */               nodeAnim.translation.ensureCapacity(translationKF.size);
/* 426 */               for (JsonValue keyframe = translationKF.child; keyframe != null; keyframe = keyframe.next) {
/* 427 */                 ModelNodeKeyframe<Vector3> kf = new ModelNodeKeyframe();
/* 428 */                 nodeAnim.translation.add(kf);
/* 429 */                 kf.keytime = keyframe.getFloat("keytime", 0.0F) / 1000.0F;
/* 430 */                 JsonValue translation = keyframe.get("value");
/* 431 */                 if (translation != null && translation.size >= 3) {
/* 432 */                   kf.value = new Vector3(translation.getFloat(0), translation.getFloat(1), translation.getFloat(2));
/*     */                 }
/*     */               } 
/*     */             } 
/*     */             
/* 437 */             JsonValue rotationKF = node.get("rotation");
/* 438 */             if (rotationKF != null && rotationKF.isArray()) {
/* 439 */               nodeAnim.rotation = new Array();
/* 440 */               nodeAnim.rotation.ensureCapacity(rotationKF.size);
/* 441 */               for (JsonValue keyframe = rotationKF.child; keyframe != null; keyframe = keyframe.next) {
/* 442 */                 ModelNodeKeyframe<Quaternion> kf = new ModelNodeKeyframe();
/* 443 */                 nodeAnim.rotation.add(kf);
/* 444 */                 kf.keytime = keyframe.getFloat("keytime", 0.0F) / 1000.0F;
/* 445 */                 JsonValue rotation = keyframe.get("value");
/* 446 */                 if (rotation != null && rotation.size >= 4) {
/* 447 */                   kf.value = new Quaternion(rotation.getFloat(0), rotation.getFloat(1), rotation.getFloat(2), rotation.getFloat(3));
/*     */                 }
/*     */               } 
/*     */             } 
/* 451 */             JsonValue scalingKF = node.get("scaling");
/* 452 */             if (scalingKF != null && scalingKF.isArray()) {
/* 453 */               nodeAnim.scaling = new Array();
/* 454 */               nodeAnim.scaling.ensureCapacity(scalingKF.size);
/* 455 */               for (JsonValue keyframe = scalingKF.child; keyframe != null; keyframe = keyframe.next) {
/* 456 */                 ModelNodeKeyframe<Vector3> kf = new ModelNodeKeyframe();
/* 457 */                 nodeAnim.scaling.add(kf);
/* 458 */                 kf.keytime = keyframe.getFloat("keytime", 0.0F) / 1000.0F;
/* 459 */                 JsonValue scaling = keyframe.get("value");
/* 460 */                 if (scaling != null && scaling.size >= 3)
/* 461 */                   kf.value = new Vector3(scaling.getFloat(0), scaling.getFloat(1), scaling.getFloat(2)); 
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }  }
/*     */ 
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\graphics\g3d\loader\G3dModelLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */