/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'swing-generictable' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.generictable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.table.AbstractTableModel;

/**
 * <p>
 * Usages of this table model as a subclass definition or annonymous class:
 * <pre>
 * class AnimalTableModel extends GenericTableModel&lt;Animal&gt; {...}
 * or 
 * ExtTableModel&lt;Animal&gt; animalModel = new GenericTableModel&lt;Animal&gt;() {...}
 * </pre>
 * </p>
 * 
 * @author thinh ho
 *
 * @param <T> The underlying UI model.
 */
public abstract class GenericTableModel<T> extends AbstractTableModel {
    private static final long serialVersionUID = -4251620593888597568L;
    
    /**
     * Storage for all added row entries.
     */
    protected Vector<T> data = new Vector<>();
    /**
     * All fields from the underlying table model object.
     */
    protected final Field[] fields;
    /**
     * Column names.
     */
    protected final String[] columnNames;
    /**
     * Column types.
     */
    @SuppressWarnings("rawtypes")
    protected final Class[] columnTypes;
    /**
     * Column value extraction functions.
     */
    protected final Function<T, Object>[] values;
    
    @SuppressWarnings("unchecked")
    public GenericTableModel() {
        // https://stackoverflow.com/questions/3403909/get-generic-type-of-class-at-runtime
        Class<T> type = null;
        String className = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
        try {
            type  = (Class<T>)Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(String.format("Cannot instantiate table model of type %s: %s", className, e.getMessage()), e);
        }
        
        // fields from the underlying model object
        List<Field> _fields = Stream.of(type.getDeclaredFields()).collect(Collectors.toList());
        int size = _fields.size();
        
        // sync up all array sizes
        fields = new Field[size];
        columnNames = new String[size];
        columnTypes = new Class[size];
        values = new Function[size];
        
        // slot the designated columns first
        Vector<Integer> nonColumnIndices = new Vector<>();
        for(int i = 0; i < size; i++) {
            Field f = _fields.get(i);
            OrderedColumn t = f.getAnnotation(OrderedColumn.class);
            if(t != null) {
                int columnIndex = t.index();
                if(columnNames[columnIndex] != null || columnTypes[columnIndex] != null || fields[columnIndex] != null) {
                    throw new IllegalStateException("Duplicate column index in annotation: " + f);
                }
                columnNames[columnIndex] = t.name();
                columnTypes[columnIndex] = t.type();
                fields[columnIndex] = f;
            } else {
                nonColumnIndices.add(i);
            }
        }
        
        // fill in the model attributes not annotated as columns
        nonColumnIndices.forEach(index -> {
            Field f = _fields.get(index);
            columnNames[index] = f.getName();
            columnTypes[index] = f.getType();
            fields[index] = f;
        });
    }
    
    /**
     * Assign the value extraction function to the specified column index.
     * 
     * @param columnIndex
     * @param f
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    public GenericTableModel<T> columnValue(int columnIndex, Function<T, Object> f) {
        values[columnIndex] = f;
        return this;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnTypes[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        T entry = getEntry(rowIndex);
        if(values[columnIndex] != null) {
            return values[columnIndex].apply(entry);
        }
        return getFieldValue(columnIndex, entry);
    }
    
    /**
     * Get the underlying model object at the specified row.
     * 
     * @param rowIndex
     * @return
     */
    public T getEntry(int rowIndex) {
        return data.get(rowIndex);
    }
    
    /**
     * Add the specified row data to the model.
     * 
     * @param entry
     */
    public void addEntry(T entry) {
        Objects.requireNonNull(entry);
        int rowCount = getRowCount();
        data.insertElementAt(entry, rowCount);
        fireTableRowsInserted(rowCount, rowCount + 1);
    }
    
    /**
     * Remove the specified entry at the specified row.
     * 
     * @param row
     */
    public void removeEntryAt(int row) {
        data.removeElementAt(row);
        fireTableRowsDeleted(row, row);
    }
    
    /**
     * Use reflection to obtain the field for the specified object. This is the
     * default mechanism for obtaining column values and is not recommended since
     * reflection could potentially have huge overhead for the underlying type.
     * 
     * @param columnIndex
     * @param obj
     * @return
     * @see #columnValue(int, Function)
     */
    protected Object getFieldValue(int columnIndex, Object obj) {
        Object value = null;
        Field f = fields[columnIndex];
        if(f != null) {
            f.setAccessible(true);
            try {
                value = f.get(obj);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new IllegalArgumentException("Cannot obtain value at columnIndex: " + columnIndex + " - " + e.getMessage(), e);
            }
            f.setAccessible(false);
        }
        return value;
    }

}
