package com.eastinno.otransos.util.regx;

/**
 * ubb过滤
 * 
 * @author lengyu
 */
public class FilterDirector {

    private FilterBuilder builder;

    public FilterDirector(FilterBuilder builder) {
        this.builder = builder;
    }

    public void construct() {
        builder.buildFilter();
    }

}
