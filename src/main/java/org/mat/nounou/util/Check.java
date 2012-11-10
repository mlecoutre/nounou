package org.mat.nounou.util;

/**
 * User: mlecoutre
 * Date: 01/11/12
 * Time: 13:24
 */
public class Check {

    public static boolean checkIsNotEmptyOrNull(Object obj) {
        return obj != null && !"".equals(obj.toString());
    }

    public static boolean checkIsEmptyOrNull(Object obj) {
        return obj == null  || "".equals(obj.toString());
    }
}
