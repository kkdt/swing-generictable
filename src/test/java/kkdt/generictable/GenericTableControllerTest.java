/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'swing-generictable' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.generictable;

import static org.junit.Assert.assertTrue;

import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class GenericTableControllerTest {
    private static final class Employee {
        @OrderedColumn(index=0, name="Last Name", type = String.class)
        String lastName;
        @OrderedColumn(index=1, name="First Name", type = String.class)
        String firstName;
        @OrderedColumn(index=2, name="Age", type = Integer.class)
        int age;
        @OrderedColumn(index=3, name="Sex", type = String.class)
        String sex;
    }
    
    private static GenericTableModel<Employee> tableModel;
    private static TableModelListener modelListener;
    private static final AtomicInteger counter = new AtomicInteger(0);
    
    private Employee male;
    private Employee female;
    
    @BeforeClass
    public static void setup() {
        tableModel = new GenericTableModel<Employee>() {
            private static final long serialVersionUID = -5436097893548874385L;
        };
        
        modelListener = e -> {
            counter.incrementAndGet();
        };
    }
    
    @Before
    public void before() {
        counter.set(0);
        
        tableModel.removeAll();
        assertTrue(tableModel.size() == 0);
        tableModel.removeTableModelListener(modelListener);
        
        male = new Employee();
        male.age = 10;
        male.firstName = "Billy";
        male.lastName = "Bob";
        
        female = new Employee();
        female.age = 20;
        female.firstName = "GI";
        female.lastName = "Jane";
    }
    
    private void checkTableHeaders(GenericTableModel<Employee> tableModel, JTable table) {
        TableColumnModel columnModel = table.getColumnModel();
        Enumeration<TableColumn> columns = columnModel.getColumns();
        while(columns.hasMoreElements()) {
            TableColumn column = columns.nextElement();
            int index = column.getModelIndex();
            assertTrue(tableModel.getColumnName(index).equals(column.getHeaderValue()));
        }
    }
    
    @Test
    public void testConstructor() {
        new GenericTableController<Employee>(new JTable(), tableModel);
        assertTrue(true);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullTable() {
        new GenericTableController<Employee>(null, tableModel);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullTableModel() {
        new GenericTableController<Employee>(new JTable(), null);
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructorNullArguments() {
        new GenericTableController<Employee>(null, null);
    }
    
    @Test
    public void testModelToTableHeaders() {
        JTable table = new JTable();
        new GenericTableController<Employee>(table, tableModel);
        checkTableHeaders(tableModel, table);
    }
    
    @Test
    public void testColumnRemoveToTableHeaders() {
        JTable table = new JTable();
        new GenericTableController<Employee>(table, tableModel);
        
        int columnIndex = 0;
        TableColumn column = table.getColumnModel().getColumn(columnIndex);
        assertTrue(column.getHeaderValue().equals(tableModel.getColumnName(columnIndex)));
        
        table.removeColumn(column);
        checkTableHeaders(tableModel, table);
    }
    
    @Test
    public void testRefresh() {
        tableModel.addTableModelListener(modelListener);
        GenericTableController<Employee> tableController = new GenericTableController<>(new JTable(), tableModel);
        assertTrue(counter.get() == 0);
        tableController.refresh();
        assertTrue(counter.get() == 1);
        tableController.refresh(m -> {
            m.addEntry(new Employee());
        });
        assertTrue(tableController.rowCount() == 1);
        assertTrue(counter.get() == 2);
        tableModel.removeTableModelListener(modelListener);
    }
    
    @Test
    public void addEntries() {
        GenericTableController<Employee> tableController = new GenericTableController<>(new JTable(), tableModel);
        assertTrue(tableController.rowCount() == 0);
        int size = 10;
        for(int i = 0; i < size; i++) {
            Employee e = new Employee();
            e.firstName = "Employee" + i;
            e.lastName = "EmployeLast" + i;
            tableController.addEntry(e);
        }
        assertTrue(tableController.rowCount() == size);
    }
    
    @Test
    public void testGetEntryAt() {
        JTable table = new JTable();
        GenericTableController<Employee> tableController = new GenericTableController<>(table, tableModel);
        assertTrue(tableModel.getRowCount() == 0);
        assertTrue(tableController.rowCount() == 0);
        
        Employee e1 = new Employee();
        e1.age = 9;
        e1.firstName = "Little";
        e1.lastName = "Boy";
        
        tableController.addEntry(e1);
        assertTrue(tableModel.getRowCount() == 1);
        assertTrue(tableController.rowCount() == 1);
        assertTrue(tableController.getEntryAt(0) == e1);
        
        Employee e2 = new Employee();
        e2.age = 11;
        e2.firstName = "Little";
        e2.lastName = "Girl";
        
        Employee e3 = new Employee();
        e3.age = 12;
        e3.firstName = "Hello";
        e3.lastName = "World";
        
        tableController.addEntry(e2);
        tableController.addEntry(e3);
        assertTrue(tableModel.getRowCount() == 3);
        assertTrue(tableController.rowCount() == 3);
        assertTrue(tableController.getEntryAt(1) == e2);
        assertTrue(tableController.getEntryAt(2) == e3);
        
        // set up sorter
        TableRowSorter<GenericTableModel<Employee>> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        
        // filter to show only e3
        sorter.setRowFilter(RowFilter.regexFilter("Hello"));
        assertTrue(tableModel.getRowCount() == 3);
        assertTrue(table.getRowCount() == 1);
        assertTrue(tableController.rowCount() == 1);
        assertTrue(tableController.getEntryAt(0) == e3);
        
        // filter to show e1 and e2
        sorter.setRowFilter(RowFilter.regexFilter("Little"));
        assertTrue(tableModel.getRowCount() == 3);
        assertTrue(table.getRowCount() == 2);
        assertTrue(tableController.rowCount() == 2);
        assertTrue(tableController.getEntryAt(0) == e1);
        assertTrue(tableController.getEntryAt(1) == e2);
        
        // select row 2
        table.getSelectionModel().addSelectionInterval(1, 1);
        assertTrue(tableController.getSelectedEntry() == e2);
        
        // remove e1
        table.getSelectionModel().addSelectionInterval(0, 0);
        tableController.removeSelectedEntry();
        assertTrue(tableModel.getRowCount() == 2);
        assertTrue(table.getRowCount() == 1);
        assertTrue(tableController.rowCount() == 1);
        assertTrue(tableController.getEntryAt(0) == e2);
    }
    
    @Test
    public void testCreateFilter() {
        JTable table = new JTable();
        GenericTableController<Employee> tableController = new GenericTableController<>(table, tableModel);
        tableController.addEntry(male);
        tableController.addEntry(female);
        assertTrue(tableController.rowCount() == 2);
        assertTrue(tableModel.size() == 2);
        
        // no affect becuase no sorter specified
        tableController.filter(tableController.createFilter(e -> e.age < 20));
        assertTrue(tableController.rowCount() == tableModel.size());
        
        // set up filter
        tableController = tableController.defaultRowSorter();
        
        // filters
        
        tableController.filter(tableController.createFilter(e -> e.age < 20));
        assertTrue(tableController.rowCount() == 1);
        assertTrue(tableController.getEntryAt(0) == male);
        
        tableController.filter(tableController.createFilter(e -> e.age >= 20));
        assertTrue(tableController.rowCount() == 1);
        assertTrue(tableController.getEntryAt(0) == female);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryAtInvalidRowEmptyTable() {
        new GenericTableController<>(new JTable(), tableModel)
            .getEntryAt(0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetEntryAtInvalidRow() {
        GenericTableController<Employee> tableController = new GenericTableController<>(new JTable(), tableModel);
        tableController.addEntry(new Employee());
        tableController.getEntryAt(1);
    }
}
