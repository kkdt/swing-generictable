/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'swing-generictable' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.generictable;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

@SuppressWarnings("unused")
public class OrderedColumnTest {
    private static final class Employee {
        @OrderedColumn(index=0, name="Last Name", type = String.class)
        String lastName;
        @OrderedColumn(index=1, name="First Name", type = String.class)
        String firstName;
    }
    
    private static final class Company {
        @OrderedColumn(index=0, name="Company Name", type = String.class)
        String name;
        @OrderedColumn(index=1, name="Address", type = String.class)
        String address;
        int companyId;
    }
    
    @Test
    public void test1() {
        Employee a = new Employee();
        List<OrderedColumn> columns = Stream.of(a.getClass().getDeclaredFields())
            .map(f -> f.getAnnotation(OrderedColumn.class))
            .collect(Collectors.toList());
        assertTrue(columns.size() == 2);
        columns.forEach(c -> assertTrue(c != null));
    }
    
    @Test
    public void test2() {
        Company a = new Company();
        List<Field> columns = Stream.of(a.getClass().getDeclaredFields())
            .filter(f -> f.getAnnotation(OrderedColumn.class) != null)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        assertTrue(columns.size() == 2);
    }
}
