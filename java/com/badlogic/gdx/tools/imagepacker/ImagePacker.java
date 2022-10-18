/*     */ package com.badlogic.gdx.tools.imagepacker;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ImageObserver;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import javax.imageio.ImageIO;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ImagePacker
/*     */ {
/*     */   BufferedImage image;
/*     */   int padding;
/*     */   boolean duplicateBorder;
/*     */   Node root;
/*     */   Map<String, Rectangle> rects;
/*     */   
/*     */   static final class Node
/*     */   {
/*     */     public Node leftChild;
/*     */     public Node rightChild;
/*     */     public Rectangle rect;
/*     */     public String leaveName;
/*     */     
/*     */     public Node(int x, int y, int width, int height, Node leftChild, Node rightChild, String leaveName) {
/*  73 */       this.rect = new Rectangle(x, y, width, height);
/*  74 */       this.leftChild = leftChild;
/*  75 */       this.rightChild = rightChild;
/*  76 */       this.leaveName = leaveName;
/*     */     }
/*     */     
/*     */     public Node() {
/*  80 */       this.rect = new Rectangle();
/*     */     }
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
/*     */   public ImagePacker(int width, int height, int padding, boolean duplicateBorder) {
/* 101 */     this.image = new BufferedImage(width, height, 6);
/* 102 */     this.padding = padding;
/* 103 */     this.duplicateBorder = duplicateBorder;
/* 104 */     this.root = new Node(0, 0, width, height, null, null, null);
/* 105 */     this.rects = new HashMap<String, Rectangle>();
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
/*     */   public void insertImage(String name, BufferedImage image) {
/* 117 */     if (this.rects.containsKey(name)) throw new RuntimeException("Key with name '" + name + "' is already in map");
/*     */     
/* 119 */     int borderPixels = this.padding + (this.duplicateBorder ? 1 : 0);
/* 120 */     borderPixels <<= 1;
/* 121 */     Rectangle rect = new Rectangle(0, 0, image.getWidth() + borderPixels, image.getHeight() + borderPixels);
/* 122 */     Node node = insert(this.root, rect);
/*     */     
/* 124 */     if (node == null) throw new RuntimeException("Image didn't fit");
/*     */     
/* 126 */     node.leaveName = name;
/* 127 */     rect = new Rectangle(node.rect);
/* 128 */     rect.width -= borderPixels;
/* 129 */     rect.height -= borderPixels;
/* 130 */     borderPixels >>= 1;
/* 131 */     rect.x += borderPixels;
/* 132 */     rect.y += borderPixels;
/* 133 */     this.rects.put(name, rect);
/*     */     
/* 135 */     Graphics2D g = this.image.createGraphics();
/* 136 */     g.drawImage(image, rect.x, rect.y, (ImageObserver)null);
/*     */ 
/*     */     
/* 139 */     if (this.duplicateBorder) {
/* 140 */       g.drawImage(image, rect.x, rect.y - 1, rect.x + rect.width, rect.y, 0, 0, image.getWidth(), 1, null);
/* 141 */       g.drawImage(image, rect.x, rect.y + rect.height, rect.x + rect.width, rect.y + rect.height + 1, 0, image
/* 142 */           .getHeight() - 1, image.getWidth(), image.getHeight(), null);
/*     */       
/* 144 */       g.drawImage(image, rect.x - 1, rect.y, rect.x, rect.y + rect.height, 0, 0, 1, image.getHeight(), null);
/* 145 */       g.drawImage(image, rect.x + rect.width, rect.y, rect.x + rect.width + 1, rect.y + rect.height, image.getWidth() - 1, 0, image
/* 146 */           .getWidth(), image.getHeight(), null);
/*     */       
/* 148 */       g.drawImage(image, rect.x - 1, rect.y - 1, rect.x, rect.y, 0, 0, 1, 1, null);
/* 149 */       g.drawImage(image, rect.x + rect.width, rect.y - 1, rect.x + rect.width + 1, rect.y, image.getWidth() - 1, 0, image
/* 150 */           .getWidth(), 1, null);
/*     */       
/* 152 */       g.drawImage(image, rect.x - 1, rect.y + rect.height, rect.x, rect.y + rect.height + 1, 0, image.getHeight() - 1, 1, image
/* 153 */           .getHeight(), null);
/* 154 */       g.drawImage(image, rect.x + rect.width, rect.y + rect.height, rect.x + rect.width + 1, rect.y + rect.height + 1, image
/* 155 */           .getWidth() - 1, image.getHeight() - 1, image.getWidth(), image.getHeight(), null);
/*     */     } 
/*     */     
/* 158 */     g.dispose();
/*     */   }
/*     */   
/*     */   private Node insert(Node node, Rectangle rect) {
/* 162 */     if (node.leaveName == null && node.leftChild != null && node.rightChild != null) {
/* 163 */       Node newNode = null;
/*     */       
/* 165 */       newNode = insert(node.leftChild, rect);
/* 166 */       if (newNode == null) newNode = insert(node.rightChild, rect);
/*     */       
/* 168 */       return newNode;
/*     */     } 
/* 170 */     if (node.leaveName != null) return null;
/*     */     
/* 172 */     if (node.rect.width == rect.width && node.rect.height == rect.height) return node;
/*     */     
/* 174 */     if (node.rect.width < rect.width || node.rect.height < rect.height) return null;
/*     */     
/* 176 */     node.leftChild = new Node();
/* 177 */     node.rightChild = new Node();
/*     */     
/* 179 */     int deltaWidth = node.rect.width - rect.width;
/* 180 */     int deltaHeight = node.rect.height - rect.height;
/*     */     
/* 182 */     if (deltaWidth > deltaHeight) {
/* 183 */       node.leftChild.rect.x = node.rect.x;
/* 184 */       node.leftChild.rect.y = node.rect.y;
/* 185 */       node.leftChild.rect.width = rect.width;
/* 186 */       node.leftChild.rect.height = node.rect.height;
/*     */       
/* 188 */       node.rect.x += rect.width;
/* 189 */       node.rightChild.rect.y = node.rect.y;
/* 190 */       node.rect.width -= rect.width;
/* 191 */       node.rightChild.rect.height = node.rect.height;
/*     */     } else {
/* 193 */       node.leftChild.rect.x = node.rect.x;
/* 194 */       node.leftChild.rect.y = node.rect.y;
/* 195 */       node.leftChild.rect.width = node.rect.width;
/* 196 */       node.leftChild.rect.height = rect.height;
/*     */       
/* 198 */       node.rightChild.rect.x = node.rect.x;
/* 199 */       node.rect.y += rect.height;
/* 200 */       node.rightChild.rect.width = node.rect.width;
/* 201 */       node.rect.height -= rect.height;
/*     */     } 
/*     */     
/* 204 */     return insert(node.leftChild, rect);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BufferedImage getImage() {
/* 210 */     return this.image;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Rectangle> getRects() {
/* 215 */     return this.rects;
/*     */   }
/*     */   
/*     */   public static void main(String[] argv) throws IOException {
/* 219 */     Random rand = new Random(0L);
/* 220 */     ImagePacker packer = new ImagePacker(512, 512, 1, true);
/*     */     
/* 222 */     BufferedImage[] images = new BufferedImage[100]; int i;
/* 223 */     for (i = 0; i < images.length; i++) {
/* 224 */       Color color = new Color((float)Math.random(), (float)Math.random(), (float)Math.random(), 1.0F);
/* 225 */       images[i] = createImage(rand.nextInt(50) + 10, rand.nextInt(50) + 10, color);
/*     */     } 
/*     */ 
/*     */     
/* 229 */     Arrays.sort(images, new Comparator<BufferedImage>()
/*     */         {
/*     */           public int compare(BufferedImage o1, BufferedImage o2) {
/* 232 */             return o2.getWidth() * o2.getHeight() - o1.getWidth() * o1.getHeight();
/*     */           }
/*     */         });
/*     */     
/* 236 */     for (i = 0; i < images.length; i++) {
/* 237 */       packer.insertImage("" + i, images[i]);
/*     */     }
/* 239 */     ImageIO.write(packer.getImage(), "png", new File("packed.png"));
/*     */   }
/*     */   
/*     */   private static BufferedImage createImage(int width, int height, Color color) {
/* 243 */     BufferedImage image = new BufferedImage(width, height, 6);
/* 244 */     Graphics2D g = image.createGraphics();
/* 245 */     g.setColor(color);
/* 246 */     g.fillRect(0, 0, width, height);
/* 247 */     g.dispose();
/* 248 */     return image;
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\imagepacker\ImagePacker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */