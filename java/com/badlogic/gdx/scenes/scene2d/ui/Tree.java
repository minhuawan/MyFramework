/*     */ package com.badlogic.gdx.scenes.scene2d.ui;
/*     */ 
/*     */ import com.badlogic.gdx.graphics.Color;
/*     */ import com.badlogic.gdx.graphics.g2d.Batch;
/*     */ import com.badlogic.gdx.scenes.scene2d.Actor;
/*     */ import com.badlogic.gdx.scenes.scene2d.EventListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.Group;
/*     */ import com.badlogic.gdx.scenes.scene2d.InputEvent;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Layout;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.Selection;
/*     */ import com.badlogic.gdx.scenes.scene2d.utils.UIUtils;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Tree
/*     */   extends WidgetGroup
/*     */ {
/*     */   TreeStyle style;
/*  40 */   final Array<Node> rootNodes = new Array();
/*     */   final Selection<Node> selection;
/*  42 */   float ySpacing = 4.0F; float iconSpacingLeft = 2.0F; float iconSpacingRight = 2.0F; float padding = 0.0F;
/*     */   
/*     */   float indentSpacing;
/*     */   private float leftColumnWidth;
/*     */   private float prefWidth;
/*     */   private float prefHeight;
/*     */   
/*     */   public Tree(Skin skin) {
/*  50 */     this(skin.<TreeStyle>get(TreeStyle.class));
/*     */   }
/*     */   private boolean sizeInvalid = true; private Node foundNode; Node overNode; Node rangeStart; private ClickListener clickListener;
/*     */   public Tree(Skin skin, String styleName) {
/*  54 */     this(skin.<TreeStyle>get(styleName, TreeStyle.class));
/*     */   }
/*     */   
/*     */   public Tree(TreeStyle style) {
/*  58 */     this.selection = new Selection<Node>() {
/*     */         protected void changed() {
/*  60 */           switch (size()) {
/*     */             case 0:
/*  62 */               Tree.this.rangeStart = null;
/*     */               break;
/*     */             case 1:
/*  65 */               Tree.this.rangeStart = (Tree.Node)first();
/*     */               break;
/*     */           } 
/*     */         }
/*     */       };
/*  70 */     this.selection.setActor((Actor)this);
/*  71 */     this.selection.setMultiple(true);
/*  72 */     setStyle(style);
/*  73 */     initialize();
/*     */   }
/*     */   
/*     */   private void initialize() {
/*  77 */     addListener((EventListener)(this.clickListener = new ClickListener() {
/*     */           public void clicked(InputEvent event, float x, float y) {
/*  79 */             Tree.Node node = Tree.this.getNodeAt(y);
/*  80 */             if (node == null)
/*  81 */               return;  if (node != Tree.this.getNodeAt(getTouchDownY()))
/*  82 */               return;  if (Tree.this.selection.getMultiple() && Tree.this.selection.hasItems() && UIUtils.shift()) {
/*     */               
/*  84 */               if (Tree.this.rangeStart == null) Tree.this.rangeStart = node; 
/*  85 */               Tree.Node rangeStart = Tree.this.rangeStart;
/*  86 */               if (!UIUtils.ctrl()) Tree.this.selection.clear(); 
/*  87 */               float start = rangeStart.actor.getY(), end = node.actor.getY();
/*  88 */               if (start > end) {
/*  89 */                 Tree.this.selectNodes(Tree.this.rootNodes, end, start);
/*     */               } else {
/*  91 */                 Tree.this.selectNodes(Tree.this.rootNodes, start, end);
/*  92 */                 Tree.this.selection.items().orderedItems().reverse();
/*     */               } 
/*     */               
/*  95 */               Tree.this.selection.fireChangeEvent();
/*  96 */               Tree.this.rangeStart = rangeStart;
/*     */               return;
/*     */             } 
/*  99 */             if (node.children.size > 0 && (!Tree.this.selection.getMultiple() || !UIUtils.ctrl())) {
/*     */               
/* 101 */               float rowX = node.actor.getX();
/* 102 */               if (node.icon != null) rowX -= Tree.this.iconSpacingRight + node.icon.getMinWidth(); 
/* 103 */               if (x < rowX) {
/* 104 */                 node.setExpanded(!node.expanded);
/*     */                 return;
/*     */               } 
/*     */             } 
/* 108 */             if (!node.isSelectable())
/* 109 */               return;  Tree.this.selection.choose(node);
/* 110 */             if (!Tree.this.selection.isEmpty()) Tree.this.rangeStart = node; 
/*     */           }
/*     */           
/*     */           public boolean mouseMoved(InputEvent event, float x, float y) {
/* 114 */             Tree.this.setOverNode(Tree.this.getNodeAt(y));
/* 115 */             return false;
/*     */           }
/*     */           
/*     */           public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
/* 119 */             super.exit(event, x, y, pointer, toActor);
/* 120 */             if (toActor == null || !toActor.isDescendantOf((Actor)Tree.this)) Tree.this.setOverNode((Tree.Node)null); 
/*     */           }
/*     */         }));
/*     */   }
/*     */   
/*     */   public void setStyle(TreeStyle style) {
/* 126 */     this.style = style;
/* 127 */     this.indentSpacing = Math.max(style.plus.getMinWidth(), style.minus.getMinWidth()) + this.iconSpacingLeft;
/*     */   }
/*     */   
/*     */   public void add(Node node) {
/* 131 */     insert(this.rootNodes.size, node);
/*     */   }
/*     */   
/*     */   public void insert(int index, Node node) {
/* 135 */     remove(node);
/* 136 */     node.parent = null;
/* 137 */     this.rootNodes.insert(index, node);
/* 138 */     node.addToTree(this);
/* 139 */     invalidateHierarchy();
/*     */   }
/*     */   
/*     */   public void remove(Node node) {
/* 143 */     if (node.parent != null) {
/* 144 */       node.parent.remove(node);
/*     */       return;
/*     */     } 
/* 147 */     this.rootNodes.removeValue(node, true);
/* 148 */     node.removeFromTree(this);
/* 149 */     invalidateHierarchy();
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearChildren() {
/* 154 */     super.clearChildren();
/* 155 */     setOverNode((Node)null);
/* 156 */     this.rootNodes.clear();
/* 157 */     this.selection.clear();
/*     */   }
/*     */   
/*     */   public Array<Node> getNodes() {
/* 161 */     return this.rootNodes;
/*     */   }
/*     */   
/*     */   public void invalidate() {
/* 165 */     super.invalidate();
/* 166 */     this.sizeInvalid = true;
/*     */   }
/*     */   
/*     */   private void computeSize() {
/* 170 */     this.sizeInvalid = false;
/* 171 */     this.prefWidth = this.style.plus.getMinWidth();
/* 172 */     this.prefWidth = Math.max(this.prefWidth, this.style.minus.getMinWidth());
/* 173 */     this.prefHeight = getHeight();
/* 174 */     this.leftColumnWidth = 0.0F;
/* 175 */     computeSize(this.rootNodes, this.indentSpacing);
/* 176 */     this.leftColumnWidth += this.iconSpacingLeft + this.padding;
/* 177 */     this.prefWidth += this.leftColumnWidth + this.padding;
/* 178 */     this.prefHeight = getHeight() - this.prefHeight;
/*     */   }
/*     */   
/*     */   private void computeSize(Array<Node> nodes, float indent) {
/* 182 */     float ySpacing = this.ySpacing;
/* 183 */     float spacing = this.iconSpacingLeft + this.iconSpacingRight;
/* 184 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 185 */       Node node = (Node)nodes.get(i);
/* 186 */       float rowWidth = indent + this.iconSpacingRight;
/* 187 */       Actor actor = node.actor;
/* 188 */       if (actor instanceof Layout) {
/* 189 */         Layout layout = (Layout)actor;
/* 190 */         rowWidth += layout.getPrefWidth();
/* 191 */         node.height = layout.getPrefHeight();
/* 192 */         layout.pack();
/*     */       } else {
/* 194 */         rowWidth += actor.getWidth();
/* 195 */         node.height = actor.getHeight();
/*     */       } 
/* 197 */       if (node.icon != null) {
/* 198 */         rowWidth += spacing + node.icon.getMinWidth();
/* 199 */         node.height = Math.max(node.height, node.icon.getMinHeight());
/*     */       } 
/* 201 */       this.prefWidth = Math.max(this.prefWidth, rowWidth);
/* 202 */       this.prefHeight -= node.height + ySpacing;
/* 203 */       if (node.expanded) computeSize(node.children, indent + this.indentSpacing); 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void layout() {
/* 208 */     if (this.sizeInvalid) computeSize(); 
/* 209 */     layout(this.rootNodes, this.leftColumnWidth + this.indentSpacing + this.iconSpacingRight, getHeight() - this.ySpacing / 2.0F);
/*     */   }
/*     */   
/*     */   private float layout(Array<Node> nodes, float indent, float y) {
/* 213 */     float ySpacing = this.ySpacing;
/* 214 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 215 */       Node node = (Node)nodes.get(i);
/* 216 */       float x = indent;
/* 217 */       if (node.icon != null) x += node.icon.getMinWidth(); 
/* 218 */       y -= node.height;
/* 219 */       node.actor.setPosition(x, y);
/* 220 */       y -= ySpacing;
/* 221 */       if (node.expanded) y = layout(node.children, indent + this.indentSpacing, y); 
/*     */     } 
/* 223 */     return y;
/*     */   }
/*     */   
/*     */   public void draw(Batch batch, float parentAlpha) {
/* 227 */     Color color = getColor();
/* 228 */     batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
/* 229 */     if (this.style.background != null) this.style.background.draw(batch, getX(), getY(), getWidth(), getHeight()); 
/* 230 */     draw(batch, this.rootNodes, this.leftColumnWidth);
/* 231 */     super.draw(batch, parentAlpha);
/*     */   }
/*     */ 
/*     */   
/*     */   private void draw(Batch batch, Array<Node> nodes, float indent) {
/* 236 */     Drawable plus = this.style.plus, minus = this.style.minus;
/* 237 */     float x = getX(), y = getY();
/* 238 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 239 */       Node node = (Node)nodes.get(i);
/* 240 */       Actor actor = node.actor;
/*     */       
/* 242 */       if (this.selection.contains(node) && this.style.selection != null) {
/* 243 */         this.style.selection.draw(batch, x, y + actor.getY() - this.ySpacing / 2.0F, getWidth(), node.height + this.ySpacing);
/* 244 */       } else if (node == this.overNode && this.style.over != null) {
/* 245 */         this.style.over.draw(batch, x, y + actor.getY() - this.ySpacing / 2.0F, getWidth(), node.height + this.ySpacing);
/*     */       } 
/*     */       
/* 248 */       if (node.icon != null) {
/* 249 */         float iconY = actor.getY() + Math.round((node.height - node.icon.getMinHeight()) / 2.0F);
/* 250 */         batch.setColor(actor.getColor());
/* 251 */         node.icon.draw(batch, x + node.actor.getX() - this.iconSpacingRight - node.icon.getMinWidth(), y + iconY, node.icon
/* 252 */             .getMinWidth(), node.icon.getMinHeight());
/* 253 */         batch.setColor(Color.WHITE);
/*     */       } 
/*     */       
/* 256 */       if (node.children.size != 0) {
/*     */         
/* 258 */         Drawable expandIcon = node.expanded ? minus : plus;
/* 259 */         float iconY = actor.getY() + Math.round((node.height - expandIcon.getMinHeight()) / 2.0F);
/* 260 */         expandIcon.draw(batch, x + indent - this.iconSpacingLeft, y + iconY, expandIcon.getMinWidth(), expandIcon.getMinHeight());
/* 261 */         if (node.expanded) draw(batch, node.children, indent + this.indentSpacing); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Node getNodeAt(float y) {
/* 267 */     this.foundNode = null;
/* 268 */     getNodeAt(this.rootNodes, y, getHeight());
/* 269 */     return this.foundNode;
/*     */   }
/*     */   
/*     */   private float getNodeAt(Array<Node> nodes, float y, float rowY) {
/* 273 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 274 */       Node node = (Node)nodes.get(i);
/* 275 */       if (y >= rowY - node.height - this.ySpacing && y < rowY) {
/* 276 */         this.foundNode = node;
/* 277 */         return -1.0F;
/*     */       } 
/* 279 */       rowY -= node.height + this.ySpacing;
/* 280 */       if (node.expanded) {
/* 281 */         rowY = getNodeAt(node.children, y, rowY);
/* 282 */         if (rowY == -1.0F) return -1.0F; 
/*     */       } 
/*     */     } 
/* 285 */     return rowY;
/*     */   }
/*     */   
/*     */   void selectNodes(Array<Node> nodes, float low, float high) {
/* 289 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 290 */       Node node = (Node)nodes.get(i);
/* 291 */       if (node.actor.getY() < low)
/* 292 */         break;  if (node.isSelectable()) {
/* 293 */         if (node.actor.getY() <= high) this.selection.add(node); 
/* 294 */         if (node.expanded) selectNodes(node.children, low, high); 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public Selection<Node> getSelection() {
/* 299 */     return this.selection;
/*     */   }
/*     */   
/*     */   public TreeStyle getStyle() {
/* 303 */     return this.style;
/*     */   }
/*     */   
/*     */   public Array<Node> getRootNodes() {
/* 307 */     return this.rootNodes;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node getOverNode() {
/* 312 */     return this.overNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getOverObject() {
/* 317 */     if (this.overNode == null) return null; 
/* 318 */     return this.overNode.getObject();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOverNode(Node overNode) {
/* 323 */     this.overNode = overNode;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPadding(float padding) {
/* 328 */     this.padding = padding;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getIndentSpacing() {
/* 333 */     return this.indentSpacing;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setYSpacing(float ySpacing) {
/* 338 */     this.ySpacing = ySpacing;
/*     */   }
/*     */   
/*     */   public float getYSpacing() {
/* 342 */     return this.ySpacing;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIconSpacing(float left, float right) {
/* 347 */     this.iconSpacingLeft = left;
/* 348 */     this.iconSpacingRight = right;
/*     */   }
/*     */   
/*     */   public float getPrefWidth() {
/* 352 */     if (this.sizeInvalid) computeSize(); 
/* 353 */     return this.prefWidth;
/*     */   }
/*     */   
/*     */   public float getPrefHeight() {
/* 357 */     if (this.sizeInvalid) computeSize(); 
/* 358 */     return this.prefHeight;
/*     */   }
/*     */   
/*     */   public void findExpandedObjects(Array objects) {
/* 362 */     findExpandedObjects(this.rootNodes, objects);
/*     */   }
/*     */   
/*     */   public void restoreExpandedObjects(Array objects) {
/* 366 */     for (int i = 0, n = objects.size; i < n; i++) {
/* 367 */       Node node = findNode(objects.get(i));
/* 368 */       if (node != null) {
/* 369 */         node.setExpanded(true);
/* 370 */         node.expandTo();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   static boolean findExpandedObjects(Array<Node> nodes, Array objects) {
/* 376 */     boolean expanded = false;
/* 377 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 378 */       Node node = (Node)nodes.get(i);
/* 379 */       if (node.expanded && !findExpandedObjects(node.children, objects)) objects.add(node.object); 
/*     */     } 
/* 381 */     return expanded;
/*     */   }
/*     */ 
/*     */   
/*     */   public Node findNode(Object object) {
/* 386 */     if (object == null) throw new IllegalArgumentException("object cannot be null."); 
/* 387 */     return findNode(this.rootNodes, object);
/*     */   } static Node findNode(Array<Node> nodes, Object object) {
/*     */     int i;
/*     */     int n;
/* 391 */     for (i = 0, n = nodes.size; i < n; i++) {
/* 392 */       Node node = (Node)nodes.get(i);
/* 393 */       if (object.equals(node.object)) return node; 
/*     */     } 
/* 395 */     for (i = 0, n = nodes.size; i < n; i++) {
/* 396 */       Node node = (Node)nodes.get(i);
/* 397 */       Node found = findNode(node.children, object);
/* 398 */       if (found != null) return found; 
/*     */     } 
/* 400 */     return null;
/*     */   }
/*     */   
/*     */   public void collapseAll() {
/* 404 */     collapseAll(this.rootNodes);
/*     */   }
/*     */   
/*     */   static void collapseAll(Array<Node> nodes) {
/* 408 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 409 */       Node node = (Node)nodes.get(i);
/* 410 */       node.setExpanded(false);
/* 411 */       collapseAll(node.children);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void expandAll() {
/* 416 */     expandAll(this.rootNodes);
/*     */   }
/*     */   
/*     */   static void expandAll(Array<Node> nodes) {
/* 420 */     for (int i = 0, n = nodes.size; i < n; i++) {
/* 421 */       ((Node)nodes.get(i)).expandAll();
/*     */     }
/*     */   }
/*     */   
/*     */   public ClickListener getClickListener() {
/* 426 */     return this.clickListener;
/*     */   }
/*     */   
/*     */   public static class Node {
/*     */     final Actor actor;
/*     */     Node parent;
/* 432 */     final Array<Node> children = new Array(0);
/*     */     boolean selectable = true;
/*     */     boolean expanded;
/*     */     Drawable icon;
/*     */     float height;
/*     */     Object object;
/*     */     
/*     */     public Node(Actor actor) {
/* 440 */       if (actor == null) throw new IllegalArgumentException("actor cannot be null."); 
/* 441 */       this.actor = actor;
/*     */     }
/*     */     
/*     */     public void setExpanded(boolean expanded) {
/* 445 */       if (expanded == this.expanded)
/* 446 */         return;  this.expanded = expanded;
/* 447 */       if (this.children.size == 0)
/* 448 */         return;  Tree tree = getTree();
/* 449 */       if (tree == null)
/* 450 */         return;  if (expanded) {
/* 451 */         for (int i = 0, n = this.children.size; i < n; i++)
/* 452 */           ((Node)this.children.get(i)).addToTree(tree); 
/*     */       } else {
/* 454 */         for (int i = 0, n = this.children.size; i < n; i++)
/* 455 */           ((Node)this.children.get(i)).removeFromTree(tree); 
/*     */       } 
/* 457 */       tree.invalidateHierarchy();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void addToTree(Tree tree) {
/* 462 */       tree.addActor(this.actor);
/* 463 */       if (!this.expanded)
/* 464 */         return;  for (int i = 0, n = this.children.size; i < n; i++) {
/* 465 */         ((Node)this.children.get(i)).addToTree(tree);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void removeFromTree(Tree tree) {
/* 470 */       tree.removeActor(this.actor);
/* 471 */       if (!this.expanded)
/* 472 */         return;  Object[] children = this.children.items;
/* 473 */       for (int i = 0, n = this.children.size; i < n; i++)
/* 474 */         ((Node)children[i]).removeFromTree(tree); 
/*     */     }
/*     */     
/*     */     public void add(Node node) {
/* 478 */       insert(this.children.size, node);
/*     */     }
/*     */     
/*     */     public void addAll(Array<Node> nodes) {
/* 482 */       for (int i = 0, n = nodes.size; i < n; i++)
/* 483 */         insert(this.children.size, (Node)nodes.get(i)); 
/*     */     }
/*     */     
/*     */     public void insert(int index, Node node) {
/* 487 */       node.parent = this;
/* 488 */       this.children.insert(index, node);
/* 489 */       updateChildren();
/*     */     }
/*     */     
/*     */     public void remove() {
/* 493 */       Tree tree = getTree();
/* 494 */       if (tree != null) {
/* 495 */         tree.remove(this);
/* 496 */       } else if (this.parent != null) {
/* 497 */         this.parent.remove(this);
/*     */       } 
/*     */     }
/*     */     public void remove(Node node) {
/* 501 */       this.children.removeValue(node, true);
/* 502 */       if (!this.expanded)
/* 503 */         return;  Tree tree = getTree();
/* 504 */       if (tree == null)
/* 505 */         return;  node.removeFromTree(tree);
/* 506 */       if (this.children.size == 0) this.expanded = false; 
/*     */     }
/*     */     
/*     */     public void removeAll() {
/* 510 */       Tree tree = getTree();
/* 511 */       if (tree != null)
/* 512 */         for (int i = 0, n = this.children.size; i < n; i++) {
/* 513 */           ((Node)this.children.get(i)).removeFromTree(tree);
/*     */         } 
/* 515 */       this.children.clear();
/*     */     }
/*     */ 
/*     */     
/*     */     public Tree getTree() {
/* 520 */       Group parent = this.actor.getParent();
/* 521 */       if (!(parent instanceof Tree)) return null; 
/* 522 */       return (Tree)parent;
/*     */     }
/*     */     
/*     */     public Actor getActor() {
/* 526 */       return this.actor;
/*     */     }
/*     */     
/*     */     public boolean isExpanded() {
/* 530 */       return this.expanded;
/*     */     }
/*     */ 
/*     */     
/*     */     public Array<Node> getChildren() {
/* 535 */       return this.children;
/*     */     }
/*     */     
/*     */     public void updateChildren() {
/* 539 */       if (!this.expanded)
/* 540 */         return;  Tree tree = getTree();
/* 541 */       if (tree == null)
/* 542 */         return;  for (int i = 0, n = this.children.size; i < n; i++) {
/* 543 */         ((Node)this.children.get(i)).addToTree(tree);
/*     */       }
/*     */     }
/*     */     
/*     */     public Node getParent() {
/* 548 */       return this.parent;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setIcon(Drawable icon) {
/* 553 */       this.icon = icon;
/*     */     }
/*     */     
/*     */     public Object getObject() {
/* 557 */       return this.object;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setObject(Object object) {
/* 562 */       this.object = object;
/*     */     }
/*     */     
/*     */     public Drawable getIcon() {
/* 566 */       return this.icon;
/*     */     }
/*     */     
/*     */     public int getLevel() {
/* 570 */       int level = 0;
/* 571 */       Node current = this;
/*     */       while (true) {
/* 573 */         level++;
/* 574 */         current = current.getParent();
/* 575 */         if (current == null)
/* 576 */           return level; 
/*     */       } 
/*     */     }
/*     */     
/*     */     public Node findNode(Object object) {
/* 581 */       if (object == null) throw new IllegalArgumentException("object cannot be null."); 
/* 582 */       if (object.equals(this.object)) return this; 
/* 583 */       return Tree.findNode(this.children, object);
/*     */     }
/*     */ 
/*     */     
/*     */     public void collapseAll() {
/* 588 */       setExpanded(false);
/* 589 */       Tree.collapseAll(this.children);
/*     */     }
/*     */ 
/*     */     
/*     */     public void expandAll() {
/* 594 */       setExpanded(true);
/* 595 */       if (this.children.size > 0) Tree.expandAll(this.children);
/*     */     
/*     */     }
/*     */     
/*     */     public void expandTo() {
/* 600 */       Node node = this.parent;
/* 601 */       while (node != null) {
/* 602 */         node.setExpanded(true);
/* 603 */         node = node.parent;
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean isSelectable() {
/* 608 */       return this.selectable;
/*     */     }
/*     */     
/*     */     public void setSelectable(boolean selectable) {
/* 612 */       this.selectable = selectable;
/*     */     }
/*     */     
/*     */     public void findExpandedObjects(Array objects) {
/* 616 */       if (this.expanded && !Tree.findExpandedObjects(this.children, objects)) objects.add(this.object); 
/*     */     }
/*     */     
/*     */     public void restoreExpandedObjects(Array objects) {
/* 620 */       for (int i = 0, n = objects.size; i < n; i++) {
/* 621 */         Node node = findNode(objects.get(i));
/* 622 */         if (node != null) {
/* 623 */           node.setExpanded(true);
/* 624 */           node.expandTo();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static class TreeStyle
/*     */   {
/*     */     public Drawable plus;
/*     */     public Drawable minus;
/*     */     public Drawable over;
/*     */     public Drawable selection;
/*     */     public Drawable background;
/*     */     
/*     */     public TreeStyle() {}
/*     */     
/*     */     public TreeStyle(Drawable plus, Drawable minus, Drawable selection) {
/* 641 */       this.plus = plus;
/* 642 */       this.minus = minus;
/* 643 */       this.selection = selection;
/*     */     }
/*     */     
/*     */     public TreeStyle(TreeStyle style) {
/* 647 */       this.plus = style.plus;
/* 648 */       this.minus = style.minus;
/* 649 */       this.selection = style.selection;
/*     */     }
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\scenes\scene2\\ui\Tree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */