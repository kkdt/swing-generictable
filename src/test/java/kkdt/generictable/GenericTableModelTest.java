/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'swing-generictable' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.generictable;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import kkdt.generictable.GenericTableModel;
import kkdt.generictable.OrderedColumn;

public class GenericTableModelTest {
    class AnimalTableModel extends GenericTableModel<Animal> {
        private static final long serialVersionUID = -5299726962275205603L;
    };
    
    class PersonTableModel extends GenericTableModel<Person> {
        private static final long serialVersionUID = -5299726962275205603L;
    };
    
    @Test
    public void testSpecificConstructor() {
        GenericTableModel<Animal> animalModel = new AnimalTableModel();
        assertTrue(animalModel.getColumnCount() == 3);
        assertTrue(animalModel.getColumnClass(0) == String.class);
        assertTrue(animalModel.getColumnClass(1) == String.class);
        assertTrue(animalModel.getColumnClass(2) == Integer.class);
        
        GenericTableModel<Person> personModel = new PersonTableModel();
        assertTrue(personModel.getColumnCount() == 3);
        assertTrue(personModel.getColumnClass(0) == String.class);
        assertTrue(personModel.getColumnClass(1) == String.class);
    }
    
    @Test
    public void testAnonymousConstructor() {
        GenericTableModel<Animal> animalModel = new GenericTableModel<Animal>() {
            private static final long serialVersionUID = 7923698012469056226L;
        };
        assertTrue(animalModel.getColumnCount() == 3);
        assertTrue(animalModel.getColumnClass(0) == String.class);
        assertTrue(animalModel.getColumnClass(1) == String.class);
        assertTrue(animalModel.getColumnClass(2) == Integer.class);
        
        GenericTableModel<Person> personModel = new GenericTableModel<Person>() {
            private static final long serialVersionUID = 7923698012469056226L;
        };
        assertTrue(personModel.getColumnCount() == 3);
        assertTrue(personModel.getColumnClass(0) == String.class);
        assertTrue(personModel.getColumnClass(1) == String.class);
    }
    
    @Test(expected=IllegalStateException.class)
    @SuppressWarnings("unused")
    public void testInvalidColumnIndexSetup() {
        class HelloWorld {
            @OrderedColumn(index=0, name="A", type = Double.class)
            Double a;
            @OrderedColumn(index=1, name="B", type = Integer.class)
            Integer b;
            @OrderedColumn(index=0, name="C", type = Float.class)
            Float c;
            public Double getA() {
                return a;
            }
            public void setA(Double a) {
                this.a = a;
            }
            public Integer getB() {
                return b;
            }
            public void setB(Integer b) {
                this.b = b;
            }
            public Float getC() {
                return c;
            }
            public void setC(Float c) {
                this.c = c;
            }
        }
        
        new GenericTableModel<HelloWorld>() {
            private static final long serialVersionUID = -555381326248513089L;
        };
    }
    
    @Test
    public void testSimpleAdd() {
        GenericTableModel<Animal> model = new GenericTableModel<Animal>() {
            private static final long serialVersionUID = 7923698012469056226L;
        };
        assertTrue(model.getRowCount() == 0);
        
        Animal dog = new Animal();
        dog.setAge(1);
        dog.setName("Chance");
        dog.setOwner("Sam");
        model.addEntry(dog);
        assertTrue(model.getRowCount() == 1);
        
        Animal cat = new Animal();
        cat.setAge(2);
        cat.setName("Casa");
        dog.setOwner("Peter");
        model.addEntry(cat);
        assertTrue(model.getRowCount() == 2);
        
        assertTrue(model.getEntry(0) == dog);
        assertTrue(model.getEntry(1) == cat);
    }
}
