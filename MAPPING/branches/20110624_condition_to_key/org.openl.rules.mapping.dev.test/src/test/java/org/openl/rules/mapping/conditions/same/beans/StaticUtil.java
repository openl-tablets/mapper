package org.openl.rules.mapping.conditions.same.beans;

/**
 *
 * @author Ivan Holub
 */
public class StaticUtil {

    private static boolean condition1Called = false;

    private static boolean condition2Called = false;

    private static boolean condition1value = false;

    private static boolean condition2value = false;

    public static boolean condition1(Object obj1, Object obj2) {
        condition1Called = true;
        return condition1value;
    }

    public static boolean isCondition1Called() {
        return condition1Called;
    }

    public static void resetCondition1() {
        condition1Called = false;
    }

    public static boolean condition2(Object obj1, Object obj2) {
        condition2Called = true;
        return condition2value;
    }

    public static boolean isCondition2Called() {
        return condition2Called;
    }

    public static void resetCondition2() {
        condition2Called = false;
    }

    public static void setCondition1value(boolean condition1value) {
        StaticUtil.condition1value = condition1value;
    }

    public static void setCondition2value(boolean condition2value) {
        StaticUtil.condition2value = condition2value;
    }
}
