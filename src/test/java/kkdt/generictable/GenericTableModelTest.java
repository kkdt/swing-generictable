/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'swing-generictable' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.generictable;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import kkdt.generictable.GenericTableModel;
import kkdt.generictable.OrderedColumn;

public class GenericTableModelTest {
    @SuppressWarnings("unused")
    private static final class Animal {
        @OrderedColumn(index=0, name="Animal Name", type = String.class)
        private String name;
        
        @OrderedColumn(index=1, name="Animal Owner", type = String.class)
        private String owner;
        
        @OrderedColumn(index=2, name="Age", type = Integer.class)
        private int age;

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOwner() {
            return owner;
        }

        public void setOwner(String owner) {
            this.owner = owner;
        }
        
    }
    
    @SuppressWarnings("unused")
    private static final class Person {
        @OrderedColumn(index=0, name="First Name", type = String.class)
        String firstName;
        
        @OrderedColumn(index=1, name="Last Name", type = String.class, editable=true)
        String lastName;
        
        String sex;

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }

    
    class AnimalTableModel extends GenericTableModel<Animal> {
        private static final long serialVersionUID = -5299726962275205603L;
    };
    
    class PersonTableModel extends GenericTableModel<Person> {
        private static final long serialVersionUID = -5299726962275205603L;
    };
    
    private static GenericTableModel<Animal> animalModel;
    private static GenericTableModel<Person> personModel;
    
    @BeforeClass
    public static void setup() {
        animalModel = new GenericTableModel<GenericTableModelTest.Animal>() {
            private static final long serialVersionUID = -2989361988481260848L;
        };
        
        personModel = new GenericTableModel<GenericTableModelTest.Person>() {
            private static final long serialVersionUID = 460819636767559440L;
        };
    }
    
    @Before
    public void before() {
        animalModel.removeAll();
        personModel.removeAll();
        
        assertTrue(animalModel.size() == 0);
        assertTrue(animalModel.getRowCount() == 0);
        assertTrue(personModel.size() == 0);
        assertTrue(personModel.getRowCount() == 0);
    }
    
    @Test
    public void testSpecificConstructor() {
        GenericTableModel<Animal> model1 = new AnimalTableModel();
        assertTrue(model1.getColumnCount() == 3);
        assertTrue(model1.getColumnClass(0) == String.class);
        assertTrue(model1.getColumnClass(1) == String.class);
        assertTrue(model1.getColumnClass(2) == Integer.class);
        
        GenericTableModel<Person> model2 = new PersonTableModel();
        assertTrue(model2.getColumnCount() == 3);
        assertTrue(model2.getColumnClass(0) == String.class);
        assertTrue(model2.getColumnClass(1) == String.class);
    }
    
    @Test
    public void testAnonymousConstructor() {
        GenericTableModel<Animal> model1 = new GenericTableModel<Animal>() {
            private static final long serialVersionUID = 7923698012469056226L;
        };
        assertTrue(model1.getColumnCount() == 3);
        assertTrue(model1.getColumnClass(0) == String.class);
        assertTrue(model1.getColumnClass(1) == String.class);
        assertTrue(model1.getColumnClass(2) == Integer.class);
        
        GenericTableModel<Person> model2 = new GenericTableModel<Person>() {
            private static final long serialVersionUID = 7923698012469056226L;
        };
        assertTrue(model2.getColumnCount() == 3);
        assertTrue(model2.getColumnClass(0) == String.class);
        assertTrue(model2.getColumnClass(1) == String.class);
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
        assertTrue(animalModel.getRowCount() == 0);
        
        Animal dog = new Animal();
        dog.setAge(1);
        dog.setName("Chance");
        dog.setOwner("Sam");
        animalModel.addEntry(dog);
        assertTrue(animalModel.getRowCount() == 1);
        
        Animal cat = new Animal();
        cat.setAge(2);
        cat.setName("Casa");
        dog.setOwner("Peter");
        animalModel.addEntry(cat);
        assertTrue(animalModel.getRowCount() == 2);
        
        assertTrue(animalModel.getEntry(0) == dog);
        assertTrue(animalModel.getEntry(1) == cat);
        
        animalModel.removeAll();
        assertTrue(animalModel.size() == 0);
    }
    
    @Test
    public void testRemoveAllEmptyModel() {
        assertTrue(animalModel.size() == 0);
        animalModel.removeAll();
    }
}
