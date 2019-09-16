package com.eastinno.otransos.web.core;

import com.eastinno.otransos.web.core.FrameworkEngine;

import junit.framework.TestCase;

public class FrameworkEngineText extends TestCase {
    public void testElimateScript() {
        String s = "<script src=''></script>";
        System.out.println(FrameworkEngine.eliminateScript(s));
    }
}
