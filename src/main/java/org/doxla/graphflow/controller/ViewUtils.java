package org.doxla.graphflow.controller;

public class ViewUtils {
    public static String redirect(String to, Object... args) {
        return "redirect:" + String.format(to, args);
    }
}
