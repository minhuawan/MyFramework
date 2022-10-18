/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import com.badlogic.gdx.utils.Array;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JTable;
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
/*     */ public class TemplatePickerPanel<T>
/*     */   extends EditorPanel<Array<T>>
/*     */   implements LoaderButton.Listener<T>
/*     */ {
/*     */   Array<T> loadedTemplates;
/*     */   Array<T> excludedTemplates;
/*     */   Class<T> type;
/*     */   JTable templatesTable;
/*     */   DefaultTableModel templatesTableModel;
/*     */   boolean isOneModelSelectedRequired = true;
/*     */   boolean isMultipleSelectionAllowed = true;
/*     */   Listener listener;
/*  31 */   int lastSelected = -1;
/*     */   
/*     */   public TemplatePickerPanel(FlameMain editor, Array<T> value, Listener listener, Class<T> type) {
/*  34 */     this(editor, value, listener, type, (LoaderButton<T>)null, true, true);
/*     */   }
/*     */   
/*     */   public TemplatePickerPanel(FlameMain editor, Array<T> value, Listener listener, Class<T> type, LoaderButton<T> loaderButton) {
/*  38 */     this(editor, value, listener, type, loaderButton, true, true);
/*     */   }
/*     */ 
/*     */   
/*     */   public TemplatePickerPanel(FlameMain editor, Array<T> value, Listener listener, Class<T> type, LoaderButton<T> loaderButton, boolean isOneModelSelectedRequired, boolean isMultipleSelectionAllowed) {
/*  43 */     super(editor, "", "");
/*  44 */     this.type = type;
/*  45 */     this.listener = listener;
/*  46 */     this.isOneModelSelectedRequired = isOneModelSelectedRequired;
/*  47 */     this.isMultipleSelectionAllowed = isMultipleSelectionAllowed;
/*  48 */     this.loadedTemplates = new Array();
/*  49 */     this.excludedTemplates = new Array();
/*  50 */     initializeComponents(type, loaderButton);
/*  51 */     setValue(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setValue(Array<T> value) {
/*  56 */     super.setValue(value);
/*  57 */     if (value == null)
/*  58 */       return;  if (!this.isMultipleSelectionAllowed && value.size > 1)
/*  59 */       throw new RuntimeException("Multiple selection must be enabled to ensure consistency between picked and available models."); 
/*  60 */     for (int i = 0; i < value.size; i++) {
/*  61 */       T model = (T)value.get(i);
/*  62 */       int index = this.loadedTemplates.indexOf(model, true);
/*  63 */       if (index > -1) {
/*  64 */         EditorPanel.setValue(this.templatesTableModel, Boolean.valueOf(true), index, 1);
/*  65 */         this.lastSelected = index;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setOneModelSelectionRequired(boolean isOneModelSelectionRequired) {
/*  71 */     this.isOneModelSelectedRequired = isOneModelSelectionRequired;
/*     */   }
/*     */   
/*     */   public void setMultipleSelectionAllowed(boolean isMultipleSelectionAllowed) {
/*  75 */     this.isMultipleSelectionAllowed = isMultipleSelectionAllowed;
/*     */   }
/*     */   
/*     */   public void setExcludedTemplates(Array<T> excludedTemplates) {
/*  79 */     this.excludedTemplates.clear();
/*  80 */     this.excludedTemplates.addAll(excludedTemplates);
/*     */   }
/*     */   
/*     */   public void setLoadedTemplates(Array<T> templates) {
/*  84 */     this.loadedTemplates.clear();
/*  85 */     this.loadedTemplates.addAll(templates);
/*  86 */     this.loadedTemplates.removeAll(this.excludedTemplates, true);
/*  87 */     this.templatesTableModel.getDataVector().removeAllElements();
/*  88 */     int i = 0;
/*  89 */     for (T template : templates) {
/*  90 */       this.templatesTableModel.addRow(new Object[] { getTemplateName(template, i), Boolean.valueOf(false) });
/*  91 */       i++;
/*     */     } 
/*  93 */     this.lastSelected = -1;
/*  94 */     setValue(this.value);
/*     */   }
/*     */   
/*     */   protected String getTemplateName(T template, int index) {
/*  98 */     String name = this.editor.assetManager.getAssetFileName(template);
/*  99 */     return (name == null) ? ("template " + index) : name;
/*     */   }
/*     */ 
/*     */   
/*     */   public void reloadTemplates() {
/* 104 */     setLoadedTemplates(this.editor.assetManager.getAll(this.type, new Array()));
/*     */   }
/*     */   
/*     */   protected void initializeComponents(Class<T> type, LoaderButton<T> loaderButton) {
/* 108 */     int i = 0;
/* 109 */     if (loaderButton != null) {
/* 110 */       loaderButton.setListener(this);
/* 111 */       this.contentPanel.add(loaderButton, new GridBagConstraints(0, i++, 1, 1, 1.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 6), 0, 0));
/*     */     } 
/*     */ 
/*     */     
/* 115 */     JScrollPane scroll = new JScrollPane();
/* 116 */     this.contentPanel.add(scroll, new GridBagConstraints(0, i, 1, 1, 1.0D, 0.0D, 10, 1, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */     
/* 119 */     this.templatesTable = new JTable() {
/*     */         public Class getColumnClass(int column) {
/* 121 */           return (column == 1) ? Boolean.class : super.getColumnClass(column);
/*     */         }
/*     */         
/*     */         public Dimension getPreferredScrollableViewportSize() {
/* 125 */           Dimension dim = super.getPreferredScrollableViewportSize();
/* 126 */           dim.height = (getPreferredSize()).height;
/* 127 */           return dim;
/*     */         }
/*     */       };
/* 130 */     this.templatesTable.getTableHeader().setReorderingAllowed(false);
/* 131 */     this.templatesTable.setSelectionMode(0);
/* 132 */     scroll.setViewportView(this.templatesTable);
/* 133 */     this.templatesTableModel = new DefaultTableModel((Object[][])new String[0][0], (Object[])new String[] { "Template", "Selected" });
/* 134 */     this.templatesTable.setModel(this.templatesTableModel);
/* 135 */     reloadTemplates();
/*     */     
/* 137 */     this.templatesTableModel.addTableModelListener(new TableModelListener() {
/*     */           public void tableChanged(TableModelEvent event) {
/* 139 */             if (event.getColumn() != 1)
/* 140 */               return;  int row = event.getFirstRow();
/* 141 */             boolean checked = ((Boolean)TemplatePickerPanel.this.templatesTable.getValueAt(row, 1)).booleanValue();
/* 142 */             if (TemplatePickerPanel.this.isOneModelSelectedRequired && TemplatePickerPanel.this.value.size == 1 && !checked) {
/* 143 */               EditorPanel.setValue(TemplatePickerPanel.this.templatesTableModel, Boolean.valueOf(true), row, 1);
/*     */               
/*     */               return;
/*     */             } 
/* 147 */             TemplatePickerPanel.this.templateChecked(row, Boolean.valueOf(checked));
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   protected void templateChecked(int index, Boolean isChecked) {
/* 154 */     T template = (T)this.loadedTemplates.get(index);
/* 155 */     if (isChecked.booleanValue()) {
/* 156 */       if (!this.isMultipleSelectionAllowed && 
/* 157 */         this.lastSelected > -1) {
/* 158 */         this.value.removeValue(this.loadedTemplates.get(this.lastSelected), true);
/* 159 */         EditorPanel.setValue(this.templatesTableModel, Boolean.valueOf(false), this.lastSelected, 1);
/*     */       } 
/*     */       
/* 162 */       this.value.add(template);
/* 163 */       this.lastSelected = index;
/*     */     } else {
/*     */       
/* 166 */       this.value.removeValue(template, true);
/*     */     } 
/* 168 */     this.listener.onTemplateChecked(template, isChecked.booleanValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public void onResourceLoaded(T model) {
/* 173 */     reloadTemplates();
/* 174 */     if (this.lastSelected == -1 && this.isOneModelSelectedRequired) {
/* 175 */       templateChecked(this.loadedTemplates.size - 1, Boolean.valueOf(true));
/*     */     } else {
/*     */       
/* 178 */       setValue(this.value);
/*     */     } 
/*     */     
/* 181 */     revalidate();
/* 182 */     repaint();
/*     */   }
/*     */   
/*     */   public static interface Listener<T> {
/*     */     void onTemplateChecked(T param1T, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\TemplatePickerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */