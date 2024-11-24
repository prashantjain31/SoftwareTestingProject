package org.example.utils;

public class ArraysHelper {
    public static String toString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "";

        StringBuilder b = new StringBuilder();
        b.append("\n");

        for(int i = 0;i <= iMax;i++) {
            b.append(String.valueOf(a[i]));
            b.append("\n");
        }

        return b.toString();
    }
}
