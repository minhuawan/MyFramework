/*     */ package com.badlogic.gdx.tools.particleeditor;
/*     */ 
/*     */ import com.badlogic.gdx.Gdx;
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEffect;
/*     */ import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import com.badlogic.gdx.utils.StreamUtils;
/*     */ import java.awt.FileDialog;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.Writer;
/*     */ import java.net.URI;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.JTable;
/*     */ import javax.swing.event.ListSelectionEvent;
/*     */ import javax.swing.event.ListSelectionListener;
/*     */ import javax.swing.event.TableModelEvent;
/*     */ import javax.swing.event.TableModelListener;
/*     */ import javax.swing.table.DefaultTableModel;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class EffectPanel
/*     */   extends JPanel
/*     */ {
/*     */   ParticleEditor editor;
/*     */   JTable emitterTable;
/*     */   DefaultTableModel emitterTableModel;
/*     */   int editIndex;
/*     */   String lastDir;
/*     */   
/*     */   public EffectPanel(ParticleEditor editor) {
/*  48 */     this.editor = editor;
/*  49 */     initializeComponents();
/*     */   }
/*     */   
/*     */   public ParticleEmitter newEmitter(String name, boolean select) {
/*  53 */     ParticleEmitter emitter = new ParticleEmitter();
/*     */     
/*  55 */     emitter.getDuration().setLow(1000.0F);
/*  56 */     emitter.getEmission().setHigh(50.0F);
/*  57 */     emitter.getLife().setHigh(500.0F);
/*  58 */     emitter.getScale().setHigh(32.0F, 32.0F);
/*     */     
/*  60 */     emitter.getTint().setColors(new float[] { 1.0F, 0.12156863F, 0.047058824F });
/*  61 */     emitter.getTransparency().setHigh(1.0F);
/*     */     
/*  63 */     emitter.setMaxParticleCount(25);
/*  64 */     emitter.setImagePath("particle.png");
/*     */     
/*  66 */     addEmitter(name, select, emitter);
/*  67 */     return emitter;
/*     */   }
/*     */   
/*     */   public ParticleEmitter newExampleEmitter(String name, boolean select) {
/*  71 */     ParticleEmitter emitter = new ParticleEmitter();
/*     */     
/*  73 */     emitter.getDuration().setLow(3000.0F);
/*     */     
/*  75 */     emitter.getEmission().setHigh(250.0F);
/*     */     
/*  77 */     emitter.getLife().setHigh(500.0F, 1000.0F);
/*  78 */     emitter.getLife().setTimeline(new float[] { 0.0F, 0.66F, 1.0F });
/*  79 */     emitter.getLife().setScaling(new float[] { 1.0F, 1.0F, 0.3F });
/*     */     
/*  81 */     emitter.getScale().setHigh(32.0F, 32.0F);
/*     */     
/*  83 */     emitter.getRotation().setLow(1.0F, 360.0F);
/*  84 */     emitter.getRotation().setHigh(180.0F, 180.0F);
/*  85 */     emitter.getRotation().setTimeline(new float[] { 0.0F, 1.0F });
/*  86 */     emitter.getRotation().setScaling(new float[] { 0.0F, 1.0F });
/*  87 */     emitter.getRotation().setRelative(true);
/*     */     
/*  89 */     emitter.getAngle().setHigh(45.0F, 135.0F);
/*  90 */     emitter.getAngle().setLow(90.0F);
/*  91 */     emitter.getAngle().setTimeline(new float[] { 0.0F, 0.5F, 1.0F });
/*  92 */     emitter.getAngle().setScaling(new float[] { 1.0F, 0.0F, 0.0F });
/*  93 */     emitter.getAngle().setActive(true);
/*     */     
/*  95 */     emitter.getVelocity().setHigh(30.0F, 300.0F);
/*  96 */     emitter.getVelocity().setActive(true);
/*     */     
/*  98 */     emitter.getTint().setColors(new float[] { 1.0F, 0.12156863F, 0.047058824F });
/*     */     
/* 100 */     emitter.getTransparency().setHigh(1.0F, 1.0F);
/* 101 */     emitter.getTransparency().setTimeline(new float[] { 0.0F, 0.2F, 0.8F, 1.0F });
/* 102 */     emitter.getTransparency().setScaling(new float[] { 0.0F, 1.0F, 0.75F, 0.0F });
/*     */     
/* 104 */     emitter.setMaxParticleCount(200);
/* 105 */     emitter.setImagePath("particle.png");
/*     */     
/* 107 */     addEmitter(name, select, emitter);
/* 108 */     return emitter;
/*     */   }
/*     */   
/*     */   private void addEmitter(String name, boolean select, ParticleEmitter emitter) {
/* 112 */     Array<ParticleEmitter> emitters = this.editor.effect.getEmitters();
/* 113 */     if (emitters.size == 0) {
/* 114 */       emitter.setPosition(this.editor.worldCamera.viewportWidth / 2.0F, this.editor.worldCamera.viewportHeight / 2.0F);
/*     */     } else {
/* 116 */       ParticleEmitter p = (ParticleEmitter)emitters.get(0);
/* 117 */       emitter.setPosition(p.getX(), p.getY());
/*     */     } 
/* 119 */     emitters.add(emitter);
/*     */     
/* 121 */     this.emitterTableModel.addRow(new Object[] { name, Boolean.valueOf(true) });
/* 122 */     if (select) {
/* 123 */       this.editor.reloadRows();
/* 124 */       int row = this.emitterTableModel.getRowCount() - 1;
/* 125 */       this.emitterTable.getSelectionModel().setSelectionInterval(row, row);
/*     */     } 
/*     */   }
/*     */   
/*     */   void emitterSelected() {
/* 130 */     int row = this.emitterTable.getSelectedRow();
/* 131 */     if (row == -1) {
/* 132 */       row = this.editIndex;
/* 133 */       this.emitterTable.getSelectionModel().setSelectionInterval(row, row);
/*     */     } 
/* 135 */     if (row == this.editIndex)
/* 136 */       return;  this.editIndex = row;
/* 137 */     this.editor.reloadRows();
/*     */   }
/*     */   
/*     */   void openEffect(boolean mergeIntoCurrent) {
/* 141 */     FileDialog dialog = new FileDialog(this.editor, "Open Effect", 0);
/* 142 */     if (this.lastDir != null) dialog.setDirectory(this.lastDir); 
/* 143 */     dialog.setVisible(true);
/* 144 */     String file = dialog.getFile();
/* 145 */     String dir = dialog.getDirectory();
/* 146 */     if (dir == null || file == null || file.trim().length() == 0)
/* 147 */       return;  this.lastDir = dir;
/* 148 */     ParticleEffect effect = new ParticleEffect();
/*     */     try {
/* 150 */       File effectFile = new File(dir, file);
/* 151 */       effect.loadEmitters(Gdx.files.absolute(effectFile.getAbsolutePath()));
/* 152 */       if (mergeIntoCurrent) {
/* 153 */         for (ParticleEmitter emitter : effect.getEmitters()) {
/* 154 */           addEmitter(emitter.getName(), false, emitter);
/*     */         }
/*     */       } else {
/* 157 */         this.editor.effect = effect;
/* 158 */         this.editor.effectFile = effectFile;
/*     */       } 
/* 160 */       this.emitterTableModel.getDataVector().removeAllElements();
/* 161 */       this.editor.particleData.clear();
/* 162 */     } catch (Exception ex) {
/* 163 */       System.out.println("Error loading effect: " + (new File(dir, file)).getAbsolutePath());
/* 164 */       ex.printStackTrace();
/* 165 */       JOptionPane.showMessageDialog(this.editor, "Error opening effect.");
/*     */       return;
/*     */     } 
/* 168 */     for (ParticleEmitter emitter : this.editor.effect.getEmitters()) {
/* 169 */       emitter.setPosition(this.editor.worldCamera.viewportWidth / 2.0F, this.editor.worldCamera.viewportHeight / 2.0F);
/* 170 */       this.emitterTableModel.addRow(new Object[] { emitter.getName(), Boolean.valueOf(true) });
/*     */     } 
/* 172 */     this.editIndex = 0;
/* 173 */     this.emitterTable.getSelectionModel().setSelectionInterval(this.editIndex, this.editIndex);
/* 174 */     this.editor.reloadRows();
/*     */   }
/*     */   
/*     */   void saveEffect() {
/* 178 */     FileDialog dialog = new FileDialog(this.editor, "Save Effect", 1);
/* 179 */     if (this.lastDir != null) dialog.setDirectory(this.lastDir); 
/* 180 */     dialog.setVisible(true);
/* 181 */     String file = dialog.getFile();
/* 182 */     String dir = dialog.getDirectory();
/* 183 */     if (dir == null || file == null || file.trim().length() == 0)
/* 184 */       return;  this.lastDir = dir;
/* 185 */     int index = 0;
/* 186 */     File effectFile = new File(dir, file);
/*     */ 
/*     */     
/* 189 */     URI effectDirUri = effectFile.getParentFile().toURI();
/* 190 */     for (ParticleEmitter emitter : this.editor.effect.getEmitters()) {
/* 191 */       emitter.setName((String)this.emitterTableModel.getValueAt(index++, 0));
/* 192 */       String imagePath = emitter.getImagePath();
/* 193 */       if ((imagePath.contains("/") || imagePath.contains("\\")) && !imagePath.contains("..")) {
/*     */         
/* 195 */         URI imageUri = (new File(emitter.getImagePath())).toURI();
/* 196 */         emitter.setImagePath(effectDirUri.relativize(imageUri).getPath());
/*     */       } 
/*     */     } 
/*     */     
/* 200 */     File outputFile = new File(dir, file);
/* 201 */     Writer fileWriter = null;
/*     */     try {
/* 203 */       fileWriter = new FileWriter(outputFile);
/* 204 */       this.editor.effect.save(fileWriter);
/* 205 */     } catch (Exception ex) {
/* 206 */       System.out.println("Error saving effect: " + outputFile.getAbsolutePath());
/* 207 */       ex.printStackTrace();
/* 208 */       JOptionPane.showMessageDialog(this.editor, "Error saving effect.");
/*     */     } finally {
/* 210 */       StreamUtils.closeQuietly(fileWriter);
/*     */     } 
/*     */   }
/*     */   
/*     */   void duplicateEmitter() {
/* 215 */     int row = this.emitterTable.getSelectedRow();
/* 216 */     if (row == -1)
/*     */       return; 
/* 218 */     String name = (String)this.emitterTableModel.getValueAt(row, 0);
/*     */     
/* 220 */     addEmitter(name, true, new ParticleEmitter((ParticleEmitter)this.editor.effect.getEmitters().get(row)));
/*     */   }
/*     */   
/*     */   void deleteEmitter() {
/* 224 */     if ((this.editor.effect.getEmitters()).size == 1)
/* 225 */       return;  int row = this.emitterTable.getSelectedRow();
/* 226 */     if (row == -1)
/* 227 */       return;  if (row <= this.editIndex) {
/* 228 */       int oldEditIndex = this.editIndex;
/* 229 */       this.editIndex = Math.max(0, this.editIndex - 1);
/* 230 */       if (oldEditIndex == row) this.editor.reloadRows(); 
/*     */     } 
/* 232 */     this.editor.effect.getEmitters().removeIndex(row);
/* 233 */     this.emitterTableModel.removeRow(row);
/* 234 */     this.emitterTable.getSelectionModel().setSelectionInterval(this.editIndex, this.editIndex);
/*     */   }
/*     */   
/*     */   void move(int direction) {
/* 238 */     if (direction < 0 && this.editIndex == 0)
/* 239 */       return;  Array<ParticleEmitter> emitters = this.editor.effect.getEmitters();
/* 240 */     if (direction > 0 && this.editIndex == emitters.size - 1)
/* 241 */       return;  int insertIndex = this.editIndex + direction;
/* 242 */     Object name = this.emitterTableModel.getValueAt(this.editIndex, 0);
/* 243 */     this.emitterTableModel.removeRow(this.editIndex);
/* 244 */     ParticleEmitter emitter = (ParticleEmitter)emitters.removeIndex(this.editIndex);
/* 245 */     this.emitterTableModel.insertRow(insertIndex, new Object[] { name });
/* 246 */     emitters.insert(insertIndex, emitter);
/* 247 */     this.editIndex = insertIndex;
/* 248 */     this.emitterTable.getSelectionModel().setSelectionInterval(this.editIndex, this.editIndex);
/*     */   }
/*     */   
/*     */   void emitterChecked(int index, boolean checked) {
/* 252 */     this.editor.setEnabled((ParticleEmitter)this.editor.effect.getEmitters().get(index), checked);
/* 253 */     this.editor.effect.start();
/*     */   }
/*     */   
/*     */   private void initializeComponents() {
/* 257 */     setLayout(new GridBagLayout());
/*     */     
/* 259 */     JPanel sideButtons = new JPanel(new GridBagLayout());
/* 260 */     add(sideButtons, new GridBagConstraints(1, 0, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
/*     */ 
/*     */     
/* 263 */     JButton newButton = new JButton("New");
/* 264 */     sideButtons.add(newButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 266 */     newButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 268 */             EffectPanel.this.newEmitter("Untitled", true);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 273 */     newButton = new JButton("Duplicate");
/* 274 */     sideButtons.add(newButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 276 */     newButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 278 */             EffectPanel.this.duplicateEmitter();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 283 */     JButton deleteButton = new JButton("Delete");
/* 284 */     sideButtons.add(deleteButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 286 */     deleteButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 288 */             EffectPanel.this.deleteEmitter();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 293 */     sideButtons.add(new JSeparator(0), new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 297 */     JButton saveButton = new JButton("Save");
/* 298 */     sideButtons.add(saveButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 300 */     saveButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 302 */             EffectPanel.this.saveEffect();
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 307 */     JButton openButton = new JButton("Open");
/* 308 */     sideButtons.add(openButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 310 */     openButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 312 */             EffectPanel.this.openEffect(false);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 317 */     JButton mergeButton = new JButton("Merge");
/* 318 */     sideButtons.add(mergeButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 320 */     mergeButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 322 */             EffectPanel.this.openEffect(true);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 327 */     JButton upButton = new JButton("Up");
/* 328 */     sideButtons.add(upButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 1.0D, 15, 2, new Insets(0, 0, 6, 0), 0, 0));
/*     */     
/* 330 */     upButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 332 */             EffectPanel.this.move(-1);
/*     */           }
/*     */         });
/*     */ 
/*     */     
/* 337 */     JButton downButton = new JButton("Down");
/* 338 */     sideButtons.add(downButton, new GridBagConstraints(0, -1, 1, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
/*     */     
/* 340 */     downButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 342 */             EffectPanel.this.move(1);
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 348 */     JScrollPane scroll = new JScrollPane();
/* 349 */     add(scroll, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */     
/* 352 */     this.emitterTable = new JTable() {
/*     */         public Class getColumnClass(int column) {
/* 354 */           return (column == 1) ? Boolean.class : super.getColumnClass(column);
/*     */         }
/*     */       };
/* 357 */     this.emitterTable.getTableHeader().setReorderingAllowed(false);
/* 358 */     this.emitterTable.setSelectionMode(0);
/* 359 */     scroll.setViewportView(this.emitterTable);
/* 360 */     this.emitterTableModel = new DefaultTableModel((Object[][])new String[0][0], (Object[])new String[] { "Emitter", "" });
/* 361 */     this.emitterTable.setModel(this.emitterTableModel);
/* 362 */     this.emitterTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
/*     */           public void valueChanged(ListSelectionEvent event) {
/* 364 */             if (event.getValueIsAdjusting())
/* 365 */               return;  EffectPanel.this.emitterSelected();
/*     */           }
/*     */         });
/* 368 */     this.emitterTableModel.addTableModelListener(new TableModelListener() {
/*     */           public void tableChanged(TableModelEvent event) {
/* 370 */             if (event.getColumn() != 1)
/* 371 */               return;  EffectPanel.this.emitterChecked(event.getFirstRow(), ((Boolean)EffectPanel.this.emitterTable.getValueAt(event.getFirstRow(), 1)).booleanValue());
/*     */           }
/*     */         });
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\particleeditor\EffectPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */