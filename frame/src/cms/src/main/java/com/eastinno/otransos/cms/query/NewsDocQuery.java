package com.eastinno.otransos.cms.query;

import java.util.List;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.NewsDir;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.support.query.QueryObject;

/**
 * @intro 文章查询对象
 * @version v0.1
 * @author zhangshh@k-touch.cn
 * @since 2014年5月4日 上午11:45:22
 */
public class NewsDocQuery extends QueryObject {
    private String searchKey;
    @POLoad(name = "dirId")
    private NewsDir dir;

    private Integer status;
    private String keyword;

    @Override
    public void customizeQuery() {
        if (StringUtils.hasText(searchKey)) {
            this.addQuery("obj.title", "%" + this.searchKey + "%", "like");
        }
        if (dir != null) {
            this.addQuery("obj.dirPath", dir.getDirPath() + "%", "like");
        }
        if (status != null) {
            this.addQuery("obj.status", status, "=");
        }
        if (!StringUtils.hasText(this.getOrderBy())) {
            this.setOrderBy("isTop desc,obj.sequence desc,obj.createDate");
            this.setOrderType("desc");
        }

        if (keyword != null) {
            String[] keys = keyword.split(",");
            if (keys != null && keys.length > 0) {
                String ksql = "(";
                List<Object> s = new java.util.ArrayList<Object>();
                for (int i = 0; i < keys.length; i++) {
                    ksql += "obj.keywords like ?";
                    s.add("%" + keys[i] + "%");
                    if (i < keys.length - 1)
                        ksql += " or ";
                }
                ksql += ")";
                this.addQuery(ksql, s.toArray());
            }
        }

        super.customizeQuery();
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public void setDir(NewsDir dir) {
        this.dir = dir;
    }

    public Integer getStatus() {
        return status;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public NewsDir getDir() {
        return dir;
    }

}
