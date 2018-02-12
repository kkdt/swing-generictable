/** 
 * Copyright (C) 2018 thinh ho

 * This file is part of 'swing-generictable' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.generictable;

import java.util.Enumeration;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.swing.DefaultRowSorter;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

public class GenericTableController<T> {
    private final GenericTableModel<T> model;
    private final JTable table;
    
    /**
     * The table and table model are required for this controller.
     * 
     * @param table
     * @param model
     */
    public GenericTableController(JTable table, GenericTableModel<T> model) {
        Objects.requireNonNull(table, "Required non-null JTable");
        Objects.requireNonNull(model, "Required non-null GenericTableModel");
        this.table = table;
        this.model = model;
        init();
    }
    
    /**
     * Configure a row sorter for the table.
     * 
     * @param sorter
     * @return
     */
    public GenericTableController<T> withRowSorter(TableRowSorter<GenericTableModel<T>> sorter) {
        table.setRowSorter(sorter);
        return this;
    }
    
    /**
     * The default row sorter will be {@linkplain TableRowSorter}.
     * 
     * @return
     */
    public GenericTableController<T> defaultRowSorter() {
        table.setRowSorter(new TableRowSorter<>(this.model));
        return this;
    }
    
    /**
     * Configuration logic that attaches the table model to the configured table.
     */
    private void init() {
        // attach table model
        this.table.setModel(this.model);
        
        // set up column model
        TableColumnModel columnModel = table.getColumnModel();
        Enumeration<TableColumn> columns = columnModel.getColumns();
        while(columns.hasMoreElements()) {
            TableColumn column = columns.nextElement();
            int index = column.getModelIndex();
            int width = this.model.getColumnWidth(index);
            column.setPreferredWidth(width);
            if(column.getHeaderRenderer() != null && column.getHeaderRenderer() instanceof DefaultTableCellRenderer) {
                ((DefaultTableCellRenderer)column.getHeaderRenderer()).setToolTipText(this.model.getColumnToolTip(index));
            }
        }
    }
    
    /**
     * Modify the underlying table model per {@code Consumer} logic.
     * 
     * @param c the consumer.
     */
    public void refresh(Consumer<GenericTableModel<T>> c) {
        c.accept(model);
    }
    
    /**
     * Notify that the underlying table model may have changed.
     */
    public void refresh() {
        model.fireTableDataChanged();
    }
    
    /**
     * Add the table model entry.
     * 
     * @param entry
     */
    public void addEntry(T entry) {
        this.model.addEntry(entry);
    }
    
    /**
     * The entry at the specified <b>table row</b> (i.e. in table coordinates).
     * 
     * @param tableRow table row in table coordinate.
     * @return
     */
    public T getEntryAt(int tableRow) {
        int modelIndex = table.convertRowIndexToModel(tableRow);
        return model.getEntry(modelIndex);
    }
    
    /**
     * The selected entry if there is a table row selection.
     * 
     * @return the entry; or null.
     */
    public T getSelectedEntry() {
        T entry = null;
        int selectedRow = table.getSelectedRow();
        if(selectedRow >= 0) {
            entry = getEntryAt(selectedRow);
        }
        return entry;
    }
    
    /**
     * Remove the selected row if there is a table row selection.
     */
    public void removeSelectedEntry() {
        int selectedRow = table.getSelectedRow();
        if(selectedRow >= 0) {
            removeEntryAt(selectedRow);
        }
    }
    
    /**
     * Remove the entry at the specified <b>table row</b> (i.e. in table coordinates).
     * 
     * @param tableRow table row in table coordinate.
     */
    public void removeEntryAt(int tableRow) {
        int modelIndex = table.convertRowIndexToModel(tableRow);
        this.model.removeEntryAt(modelIndex);
    }
    
    /**
     * Clear the underlying table model.
     */
    public void clearTable() {
        this.model.removeAll();
    }
    
    /**
     * Total rows visible on the table.
     * 
     * @return
     */
    public int rowCount() {
        return this.table.getRowCount();
    }
    
    /**
     * Create a filter based on the specified predicate.
     * 
     * @param include true to include an entry in the table; false to exclude it.
     * @return
     */
    public RowFilter<GenericTableModel<T>, Integer> createFilter(Predicate<T> include) {
        return new RowFilter<GenericTableModel<T>, Integer>() {
            @Override
            public boolean include(RowFilter.Entry<? extends GenericTableModel<T>, ? extends Integer> entry) {
                Integer rowIndex = entry.getIdentifier();
                T e = entry.getModel().getEntry(rowIndex);
                return include.test(e);
            }
        };
    }
    
    /**
     * Filter the underlying table.
     * 
     * @param filter
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void filter(RowFilter<GenericTableModel<T>, Integer> filter) {
        RowSorter<?> sorter = table.getRowSorter();
        if(sorter != null && sorter instanceof DefaultRowSorter) {
            ((DefaultRowSorter)sorter).setRowFilter(filter);
        }
    }
}
