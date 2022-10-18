/*     */ package com.badlogic.gdx.tools.flame;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Cursor;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseAdapter;
/*     */ import java.awt.event.MouseEvent;
/*     */ import javax.swing.BorderFactory;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JCheckBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JToggleButton;
/*     */ import javax.swing.event.ChangeListener;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class EditorPanel<T>
/*     */   extends JPanel
/*     */ {
/*     */   private String name;
/*     */   private String description;
/*     */   protected T value;
/*     */   private JPanel titlePanel;
/*     */   JToggleButton activeButton;
/*     */   JPanel contentPanel;
/*     */   JToggleButton advancedButton;
/*     */   JButton removeButton;
/*     */   JPanel advancedPanel;
/*     */   private boolean hasAdvanced;
/*     */   JLabel nameLabel;
/*     */   JLabel descriptionLabel;
/*     */   protected boolean isAlwaysActive;
/*     */   protected boolean isAlwaysShown = false;
/*     */   protected boolean isRemovable;
/*     */   protected FlameMain editor;
/*     */   
/*     */   public EditorPanel(FlameMain editor, String name, String description, boolean alwaysActive, boolean isRemovable) {
/*  59 */     this.editor = editor;
/*  60 */     this.name = name;
/*  61 */     this.description = description;
/*  62 */     this.isRemovable = isRemovable;
/*  63 */     this.isAlwaysActive = alwaysActive;
/*  64 */     initializeComponents();
/*  65 */     showContent(false);
/*     */   }
/*     */   
/*     */   public EditorPanel(FlameMain editor, String name, String description) {
/*  69 */     this(editor, name, description, true, false);
/*     */   }
/*     */   
/*     */   protected void activate() {}
/*     */   
/*     */   public void showContent(boolean visible) {
/*  75 */     this.contentPanel.setVisible(visible);
/*  76 */     this.advancedPanel.setVisible((visible && this.advancedButton.isSelected()));
/*  77 */     this.advancedButton.setVisible((visible && this.hasAdvanced));
/*  78 */     this.descriptionLabel.setText(visible ? this.description : "");
/*     */   }
/*     */   
/*     */   public void setIsAlwayShown(boolean isAlwaysShown) {
/*  82 */     showContent(isAlwaysShown);
/*  83 */     this.isAlwaysShown = isAlwaysShown;
/*  84 */     this.titlePanel.setCursor(null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void update(FlameMain editor) {}
/*     */   
/*     */   public void setHasAdvanced(boolean hasAdvanced) {
/*  91 */     this.hasAdvanced = hasAdvanced;
/*  92 */     this.advancedButton.setVisible(hasAdvanced);
/*     */   }
/*     */   
/*     */   public JPanel getContentPanel() {
/*  96 */     return this.contentPanel;
/*     */   }
/*     */   
/*     */   public JPanel getAdvancedPanel() {
/* 100 */     return this.advancedPanel;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 104 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setEmbedded() {
/* 108 */     GridBagLayout layout = (GridBagLayout)getLayout();
/* 109 */     GridBagConstraints constraints = layout.getConstraints(this.contentPanel);
/* 110 */     constraints.insets = new Insets(0, 0, 0, 0);
/* 111 */     layout.setConstraints(this.contentPanel, constraints);
/*     */     
/* 113 */     this.titlePanel.setVisible(false);
/*     */   }
/*     */   
/*     */   protected void initializeComponents() {
/* 117 */     setLayout(new GridBagLayout());
/*     */     
/* 119 */     this.titlePanel = new JPanel(new GridBagLayout());
/* 120 */     add(this.titlePanel, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 17, 2, new Insets(3, 0, 3, 0), 0, 0));
/*     */     
/* 122 */     this.titlePanel.setCursor(Cursor.getPredefinedCursor(12));
/*     */     
/* 124 */     this.nameLabel = new JLabel(this.name);
/* 125 */     this.titlePanel.add(this.nameLabel, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(3, 6, 3, 6), 0, 0));
/*     */     
/* 127 */     this.nameLabel.setFont(this.nameLabel.getFont().deriveFont(1));
/*     */ 
/*     */     
/* 130 */     this.descriptionLabel = new JLabel(this.description);
/* 131 */     this.titlePanel.add(this.descriptionLabel, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 17, 0, new Insets(3, 6, 3, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 135 */     this.advancedButton = new JToggleButton("Advanced");
/* 136 */     this.titlePanel.add(this.advancedButton, new GridBagConstraints(2, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */     
/* 138 */     this.advancedButton.setVisible(false);
/*     */ 
/*     */     
/* 141 */     this.activeButton = new JToggleButton("Active");
/* 142 */     this.titlePanel.add(this.activeButton, new GridBagConstraints(3, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */     
/* 146 */     this.removeButton = new JButton("X");
/* 147 */     this.titlePanel.add(this.removeButton, new GridBagConstraints(4, 0, 1, 1, 0.0D, 0.0D, 10, 0, new Insets(0, 0, 0, 6), 0, 0));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     this.contentPanel = new JPanel(new GridBagLayout());
/* 153 */     add(this.contentPanel, new GridBagConstraints(1, 1, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 6, 6, 6), 0, 0));
/*     */     
/* 155 */     this.contentPanel.setVisible(false);
/*     */ 
/*     */     
/* 158 */     this.advancedPanel = new JPanel(new GridBagLayout());
/* 159 */     add(this.advancedPanel, new GridBagConstraints(1, 2, 1, 1, 0.0D, 0.0D, 10, 1, new Insets(0, 6, 6, 6), 0, 0));
/*     */     
/* 161 */     this.advancedPanel.setVisible(false);
/*     */ 
/*     */ 
/*     */     
/* 165 */     this.titlePanel.addMouseListener(new MouseAdapter() {
/*     */           public void mouseClicked(MouseEvent event) {
/* 167 */             if (!EditorPanel.this.isAlwaysShown)
/* 168 */               EditorPanel.this.showContent(!EditorPanel.this.contentPanel.isVisible()); 
/*     */           }
/*     */         });
/* 171 */     this.activeButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 173 */             EditorPanel.this.activate();
/*     */           }
/*     */         });
/* 176 */     this.advancedButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 178 */             EditorPanel.this.advancedPanel.setVisible(EditorPanel.this.advancedButton.isSelected());
/*     */           }
/*     */         });
/*     */     
/* 182 */     this.removeButton.addActionListener(new ActionListener() {
/*     */           public void actionPerformed(ActionEvent event) {
/* 184 */             EditorPanel.this.removePanel();
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   protected void removePanel() {
/* 190 */     Container parent = getParent();
/* 191 */     parent.remove(this);
/* 192 */     parent.validate();
/* 193 */     parent.repaint();
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 197 */     this.name = name;
/* 198 */     this.nameLabel.setText(name);
/*     */   }
/*     */   
/*     */   public void setDescription(String desc) {
/* 202 */     this.description = desc;
/* 203 */     this.descriptionLabel.setText(desc);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addContent(int row, int column, JComponent component) {
/* 208 */     addContent(row, column, component, true, 10, 1);
/*     */   }
/*     */   
/*     */   protected void addContent(int row, int column, JComponent component, boolean addBorder) {
/* 212 */     addContent(row, column, component, addBorder, 10, 1);
/*     */   }
/*     */   
/*     */   protected void addContent(int row, int column, JComponent component, int anchor, int fill) {
/* 216 */     addContent(row, column, component, true, anchor, fill);
/*     */   }
/*     */   
/*     */   protected void addContent(int row, int column, JComponent component, boolean addBorders, int anchor, int fill, float wx, float wy) {
/* 220 */     addContent(this.contentPanel, row, column, component, addBorders, anchor, fill, wx, wy);
/*     */   }
/*     */   
/*     */   protected void addContent(int row, int column, JComponent component, boolean addBorders, int anchor, int fill) {
/* 224 */     addContent(row, column, component, addBorders, anchor, fill, 1.0F, 1.0F);
/*     */   }
/*     */   
/*     */   public void setValue(T value) {
/* 228 */     this.value = value;
/* 229 */     this.activeButton.setVisible((value == null) ? false : (!this.isAlwaysActive));
/* 230 */     this.removeButton.setVisible(this.isRemovable);
/*     */   }
/*     */   
/*     */   public static void addContent(JPanel panel, int row, int column, JComponent component, boolean addBorders, int anchor, int fill, float wx, float wy) {
/* 234 */     if (addBorders) component.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)); 
/* 235 */     panel.add(component, new GridBagConstraints(column, row, 1, 1, wx, wy, anchor, fill, new Insets(0, 0, 0, 0), 0, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static <K> void setValue(JSpinner spinner, K object) {
/* 240 */     ChangeListener[] listeners = spinner.getChangeListeners();
/* 241 */     ChangeListener listener = null;
/* 242 */     if (listeners != null && listeners.length > 0) {
/* 243 */       listener = listeners[0];
/* 244 */       spinner.removeChangeListener(listener);
/*     */     } 
/* 246 */     spinner.setValue(object);
/* 247 */     if (listener != null) spinner.addChangeListener(listener); 
/*     */   }
/*     */   
/*     */   protected static void setValue(JCheckBox checkBox, boolean isSelected) {
/* 251 */     ActionListener[] listeners = checkBox.getActionListeners();
/* 252 */     ActionListener listener = null;
/* 253 */     if (listeners != null && listeners.length > 0) {
/* 254 */       listener = listeners[0];
/* 255 */       checkBox.removeActionListener(listener);
/*     */     } 
/* 257 */     checkBox.setSelected(isSelected);
/* 258 */     if (listener != null) checkBox.addActionListener(listener); 
/*     */   }
/*     */   
/*     */   protected static <K> void setValue(Slider slider, float value) {
/* 262 */     ChangeListener[] listeners = slider.spinner.getChangeListeners();
/* 263 */     ChangeListener listener = null;
/* 264 */     if (listeners != null && listeners.length > 0) {
/* 265 */       listener = listeners[0];
/* 266 */       slider.spinner.removeChangeListener(listener);
/*     */     } 
/* 268 */     slider.setValue(value);
/* 269 */     if (listener != null) slider.spinner.addChangeListener(listener); 
/*     */   }
/*     */   
/*     */   protected static void setValue(DefaultTableModel tableModel, Object value, int row, int column) {
/* 273 */     TableModelListener[] listeners = tableModel.getTableModelListeners();
/* 274 */     TableModelListener listener = null;
/* 275 */     if (listeners != null && listeners.length > 0) {
/* 276 */       listener = listeners[0];
/* 277 */       tableModel.removeTableModelListener(listener);
/*     */     } 
/* 279 */     tableModel.setValueAt(value, row, column);
/* 280 */     if (listener != null) tableModel.addTableModelListener(listener); 
/*     */   }
/*     */ }


/* Location:              D:\files\slayTheSpire-export\!\com\badlogic\gdx\tools\flame\EditorPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */