/** 
 * Copyright (C) 2018 thinh ho
 * This file is part of 'swing-generictable' which is released under the MIT license.
 * See LICENSE at the project root directory.
 */
package kkdt.generictable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Tuple3 column meta data.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderedColumn {
    /**
     * Column index - must be configured accordingly in the UI model.
     * 
     * @return
     */
    int index();
    
    /**
     * Column name.
     * 
     * @return
     */
    String name();
    
    /**
     * Expected column type.
     * 
     * @return
     */
    @SuppressWarnings("rawtypes")
    Class type();
}
