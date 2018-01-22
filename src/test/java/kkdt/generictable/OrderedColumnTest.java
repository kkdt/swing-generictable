/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'swing-generictable' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.generictable;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import kkdt.generictable.OrderedColumn;

public class OrderedColumnTest {
    @Test
    public void test1() {
        Animal a = new Animal();
        List<OrderedColumn> columns = Stream.of(a.getClass().getDeclaredFields())
            .map(f -> f.getAnnotation(OrderedColumn.class)).collect(Collectors.toList());
        assertTrue(columns.size() == 3);
        
        columns.forEach(c -> assertTrue(c != null));
    }
    
    @Test
    public void test2() {
        Person a = new Person();
        List<Field> columns = Stream.of(a.getClass().getDeclaredFields())
            .filter(f -> f.getAnnotation(OrderedColumn.class) != null)
            .filter(f -> f != null)
            .collect(Collectors.toList());
        assertTrue(columns.size() == 2);
    }
}
