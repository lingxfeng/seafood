package com.eastinno.otransos.platform.weixin.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.eastinno.otransos.ext.platform.dao.IJpaGenericDAO;
import com.eastinno.otransos.platform.weixin.domain.Follower;


public interface IFollowerDAO extends IJpaGenericDAO<Follower, Long> {
	@Query("select f.weixinOpenId from Follower f where f.account.id = ?1")
	List<String> getFollowerIdsByIds(Long accountId);
}
