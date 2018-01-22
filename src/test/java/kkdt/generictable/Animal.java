/** 
s * Copyright (C) 2018 thinh ho
 * This file is part of 'swing-generictable' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.generictable;

import kkdt.generictable.OrderedColumn;

class Animal {
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
