/**
 * 
 */
package com.eastinno.otransos.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import com.eastinno.otransos.search.Searchable;
import com.eastinno.otransos.search.impl.IndexHolder;
import com.eastinno.otransos.search.impl.SearchHelper;

/**
 * 测试索引过程
 * 
 * @Author <a href="mailto:ksmwly@gmail.com">lengyu</a>
 */
public class LuceneTester {

    /**
     * 测试添加索引
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        IndexHolder holder = IndexHolder.init("D:\\TEST");
        // for(int i=0;i<10;i++)
        // holder.optimize(Post.class);
        // System.exit(0);
        Post post = new Post();
        post.setId(1);
        post.setTitle("我们是中国人");
        post.setBody("转载请注明:文章转载自:开源中国社区");
        List<Post> objs = new ArrayList<Post>();
        objs.add(post);
        holder.add(objs);

        Query q = SearchHelper.makeQuery("title", "中国人", 1.0f);
        List<Long> idList = holder.find(Post.class, q, null, null, 1, 10);

        List<Class<? extends Searchable>> clzs = new ArrayList<Class<? extends Searchable>>();
        clzs.add(Post.class);
        List<Searchable> postList = holder.find(clzs, q, null, null, 1, 10);
        if (postList.size() > 0) {
            for (Searchable s : postList) {
                Post obj = (Post) s;
                System.out.println(obj.getId() + "   " + obj.getTitle() + "    " + obj.getBody());
            }
        }
    }
}
