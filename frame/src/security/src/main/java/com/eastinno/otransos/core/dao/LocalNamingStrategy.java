package com.eastinno.otransos.core.dao;

import org.hibernate.cfg.DefaultComponentSafeNamingStrategy;
import org.hibernate.cfg.NamingStrategy;
import org.hibernate.internal.util.StringHelper;

public class LocalNamingStrategy extends DefaultComponentSafeNamingStrategy {
    private static final long serialVersionUID = 1L;
    private String tablePrefix = "";

    private boolean addUnderScores = false;

    public static final NamingStrategy INSTANCE = new LocalNamingStrategy();

    public LocalNamingStrategy() {
        this.tablePrefix = "t_";
        this.addUnderScores = true;
    }

    public void setTablePrefix(String tablePrefix) {
        if (StringHelper.isNotEmpty(tablePrefix))
            this.tablePrefix = tablePrefix;
    }

    public void setAddUnderScores(boolean addUnderScores) {
        this.addUnderScores = addUnderScores;
    }

    public String classToTableName(String className) {
        return addPrefixes(StringHelper.unqualify(className), this.tablePrefix);
    }

    public String tableName(String tableName) {
        return addPrefixes(tableName, this.tablePrefix);
    }

    private String addPrefixes(String name, String prefix) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix);
        if (this.addUnderScores)
            buffer.append(addUnderscores(name));
        else {
            buffer.append(name);
        }
        return buffer.toString();
    }

    public String collectionTableName(String ownerEntityTable, String associatedEntityTable, String propertyName) {
        return tableName(ownerEntityTable + '_' + propertyName);
    }
}
