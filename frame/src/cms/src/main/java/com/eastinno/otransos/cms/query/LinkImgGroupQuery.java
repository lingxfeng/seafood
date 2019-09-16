package com.eastinno.otransos.cms.query;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

import org.springframework.util.StringUtils;

import com.eastinno.otransos.cms.domain.LinkImgType;
import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.support.query.QueryObject;

/**
 * @intro 文章查询对象
 * @version v0.1
 * @author zhangshh@k-touch.cn
 * @since 2014年5月4日 上午11:45:22
 */
public class LinkImgGroupQuery extends QueryObject {
    private String searchKey;

    @POLoad(name = "typeId")
    @ManyToOne(fetch = FetchType.LAZY)
    private LinkImgType type;

    @Override
    public void customizeQuery() {
        if (StringUtils.hasText(searchKey)) {
            this.addQuery("obj.title", "%" + this.searchKey + "%", "like");
        }
        if (type != null) {
            this.addQuery("obj.type", type, "=");
        }
        super.customizeQuery();
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public LinkImgType getType() {
        return type;
    }

    public void setType(LinkImgType type) {
        this.type = type;
    }

    public String getSearchKey() {
        return searchKey;
    }

}
