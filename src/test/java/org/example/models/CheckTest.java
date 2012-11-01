package org.example.models;

import org.junit.Test;
import org.mat.nounou.util.Check;
import static org.junit.Assert.*;

/**
 * User: mlecoutre
 * Date: 01/11/12
 * Time: 13:27
 */
public class CheckTest {

    @Test
    public void testCheckIsNotEmptyOrNull (){
        assertFalse("check should return FALSE if obj is null",Check.checkIsNotEmptyOrNull(null));
        assertFalse("check should return FALSE if obj is empty", Check.checkIsNotEmptyOrNull(""));
        assertTrue("check should return TRUE if obj is empty", Check.checkIsNotEmptyOrNull(3));

    }

    @Test
    public void testCheckIsEmptyOrNull (){
        assertTrue("check should return TRUE if obj is null",Check.checkIsEmptyOrNull(null));
        assertTrue("check should return TRUE if obj is empty", Check.checkIsEmptyOrNull(""));
        assertFalse("check should return FALSE if obj is empty", Check.checkIsEmptyOrNull(3));

    }


}
