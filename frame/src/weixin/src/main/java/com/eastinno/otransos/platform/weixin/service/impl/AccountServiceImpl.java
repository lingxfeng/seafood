package com.eastinno.otransos.platform.weixin.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.platform.weixin.dao.IAccountDAO;
import com.eastinno.otransos.platform.weixin.domain.Account;
import com.eastinno.otransos.platform.weixin.service.IAccountService;
import com.eastinno.otransos.web.tools.IPageList;


/**
 * AccountServiceImpl
 * @author ksmwly@gmail.com
 */
@Service
public class AccountServiceImpl implements IAccountService{
	@Resource
	private IAccountDAO accountDao;
	
	public void setAccountDao(IAccountDAO AccountDao){
		this.accountDao=AccountDao;
	}
	
	public Long addAccount(Account account) {
		this.accountDao.save(account);
		if (account != null && account.getId() != null) {
			return account.getId();
		}
		return null;
	}
	
	public Account getAccount(Long id) {
		Account account = this.accountDao.get(id);
		return account;
		}
	
	
	public IPageList getAccountBy(IQueryObject queryObj) {	
		return this.accountDao.findBy(queryObj);	
	}
	
	public boolean updateAccount(Long id, Account account) {
		if (id != null) {
			account.setId(id);
		} else {
			return false;
		}
		this.accountDao.update(account);
		return true;
	}

	@Override
	public boolean delAccount(Long id) {
		Account account = this.getAccount(id);
        if (account != null) {
            this.accountDao.remove(id);
            return true;
        }
        return false;
	}

	@Override
	public boolean batchDelAccounts(List<Serializable> ids) {
		// TODO Auto-generated method stub
		return false;
	}	
	
}
