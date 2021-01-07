package com.tuannh.s2pc;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Test {
    public static void main(String[] args) {
        Map<String, String> a = new HashMap<>();
        a.put("A", "B");
        a.put("B", "C");
        System.out.println(a);
        a.keySet().removeIf(entry -> Objects.equals(entry, "A"));
        System.out.println(a);
    }
}
