package com.eastinno.otransos.web.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.eastinno.otransos.core.util.I18n;
import com.eastinno.otransos.web.exception.FrameworkException;

public class FileResourceLoader extends AbstractResourceLoader {
    public InputStream load(String name) {
        File file = new File(name);
        try {
            if (file.exists()) {
                FileInputStream in = new FileInputStream(file);
                return in;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FrameworkException(I18n.getLocaleMessage("core.web.ould.not.load.resource.documents") + name, e);
        }
        return null;
    }
}
