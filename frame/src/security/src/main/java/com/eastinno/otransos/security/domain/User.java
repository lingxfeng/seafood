package com.eastinno.otransos.security.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.eastinno.otransos.container.annonation.POLoad;
import com.eastinno.otransos.core.util.CommUtil;
import com.eastinno.otransos.web.ajax.IJsonObject;

/**
 * @intro 用户基础模型
 * @version v0.1
 * @author maowei
 * @since 2008年5月20日 下午6:33:24
 */
@Entity(name = "Disco_UserInfo")
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends TenantObject implements IJsonObject {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @NotBlank(message = "code不能为空")
    @Column(nullable = false, unique = true, length = 32)
    @Size(max = 32, min = 2, message = "长度必须在2-32个字符以内")
    private String code; // 编号 是唯一的

    @NotNull(message = "用户名不能为空")
    @Column(length = 30, unique = true)
    private String name;// 用户名且唯一

    private String nickname;// 昵称
    private String trueName;// 真实姓名
    private String namePy;// 姓名拼音

    @Column(length = 1)
    private String sex;// 性别
    @Column(length = 12)
    private String tel;// 座机

    @Column(length = 11)
    @Length(min = 11, max = 11)
    @Pattern(regexp = "(\\+\\d+)?1[34578]\\d{9}$")
    private String mobileTel;// 移动电话

    @Email()
    @Column(length = 100)
    private String email;

    @Column(length = 64)
    private String password;
    @Transient
    private String password2;// 确认密码

    private String salt;// 加密凭证

    private String pic;// 头像

    @Column(length = 255)
    private String intro;// 个人简介
    private Boolean isTenantAdmin=false;//是否租户管理员
    private Date registerTime = new Date();// 注册日期
    private Date lastLoginTime = new Date();// 最后登陆日期
    private Date lastLogoutTime;// 最后退出日期
    private Date passwordChangeTime;// 密码更改日期
    private Date modifyDate;// 帐号修改日期
    private Date birthday;// 生日(出生年月)

    private String idCard;// 身份证号
    private Integer loginTimes = Integer.valueOf(0);// 登陆次数
    private Integer status = Integer.valueOf(0);// -2删除(回收站) -1锁定、0未审核、1通过
    private String lastLoginIP;// 最后登陆IP

    private String safeQuestion;// 密码保护问题
    private String safeAnswer;// 密码保护问题答案

    // 随机码(激活帐户、安全校验时使用)
    private String randomCode;

    private String problem;
    private String solution;
    private Long imUin;

    @Column(unique = true)
    private String sinaOpenId;// 新浪
    @Column(unique = true)
    private String alipayOpenId;// 支付宝
    @Column(unique = true)
    private String weixinOpenId;// 微信
    @Column(unique = true)
    private String tencentOpenId;// 腾讯QQ

    private Integer type = 4;// 用户类型1平台管理员2租户管理员3租户下的员工4租户下的业务注册用户
    /**
     * 是否允许修改密码,默认为true
     */
    private Boolean enableChangePassword = true;

    /**
     * 密码过期天数，为-1表示永不过期，为0表示立即要修改密码，大于1表示过期的天数
     */
    private Integer passwordExpiredDays = -1;

    /**
     * 是否允许同时登录，默认值由登录模块定义
     */
    private Boolean loginAtSameTime = Boolean.valueOf(false);

    /**
     * 是否使用USB
     */
    private Boolean useUsb = Boolean.valueOf(false);

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> roles = new ArrayList<Role>();

    @POLoad(name = "deptId")
    @ManyToOne()
    private Department dept;// 部门

    public Object toJSonObject() {
        Map<String, Object> map = CommUtil.obj2mapExcept(this, new String[] {"authorities", "roles"});
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (getRoles() != null) {
            for (Role p : getRoles()) {
                list.add(CommUtil.obj2map(p, new String[] {"id", "title", "name"}));
            }

        }
        map.put("roles", list);
        if (dept != null){
        	map.put("dept", CommUtil.obj2map(dept, new String[] {"id", "sn", "title"}));
        }
        if (tenant != null){
        	map.put("tenant", CommUtil.obj2map(tenant, new String[] {"id", "title", "code"}));
        }
        return map;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTrueName() {
        return trueName;
    }

    public void setTrueName(String trueName) {
        this.trueName = trueName;
    }

    public String getNamePy() {
        return namePy;
    }

    public void setNamePy(String namePy) {
        this.namePy = namePy;
    }
    
	public Department getDept() {
		return dept;
	}

	public void setDept(Department dept) {
		this.dept = dept;
	}

	public String getSex() {
        return sex;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobileTel() {
        return mobileTel;
    }

    public void setMobileTel(String mobileTel) {
        this.mobileTel = mobileTel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Date lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public Date getPasswordChangeTime() {
        return passwordChangeTime;
    }

    public void setPasswordChangeTime(Date passwordChangeTime) {
        this.passwordChangeTime = passwordChangeTime;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public Integer getLoginTimes() {
        return loginTimes;
    }

    public void setLoginTimes(Integer loginTimes) {
        this.loginTimes = loginTimes;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }
    
    public Boolean getIsTenantAdmin() {
		return isTenantAdmin;
	}

	public void setIsTenantAdmin(Boolean isTenantAdmin) {
		this.isTenantAdmin = isTenantAdmin;
	}

	public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public String getSafeQuestion() {
        return safeQuestion;
    }

    public void setSafeQuestion(String safeQuestion) {
        this.safeQuestion = safeQuestion;
    }

    public String getSafeAnswer() {
        return safeAnswer;
    }

    public void setSafeAnswer(String safeAnswer) {
        this.safeAnswer = safeAnswer;
    }

    public String getRandomCode() {
        return randomCode;
    }

    public void setRandomCode(String randomCode) {
        this.randomCode = randomCode;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public Long getImUin() {
        return imUin;
    }

    public void setImUin(Long imUin) {
        this.imUin = imUin;
    }

    public String getSinaOpenId() {
        return sinaOpenId;
    }

    public void setSinaOpenId(String sinaOpenId) {
        this.sinaOpenId = sinaOpenId;
    }

    public String getAlipayOpenId() {
        return alipayOpenId;
    }

    public void setAlipayOpenId(String alipayOpenId) {
        this.alipayOpenId = alipayOpenId;
    }

    public String getWeixinOpenId() {
        return weixinOpenId;
    }

    public void setWeixinOpenId(String weixinOpenId) {
        this.weixinOpenId = weixinOpenId;
    }

    public String getTencentOpenId() {
        return tencentOpenId;
    }

    public void setTencentOpenId(String tencentOpenId) {
        this.tencentOpenId = tencentOpenId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer i) {
        this.type = i;
    }

    public Boolean getEnableChangePassword() {
        return enableChangePassword;
    }

    public void setEnableChangePassword(Boolean enableChangePassword) {
        this.enableChangePassword = enableChangePassword;
    }

    public Integer getPasswordExpiredDays() {
        return passwordExpiredDays;
    }

    public void setPasswordExpiredDays(Integer passwordExpiredDays) {
        this.passwordExpiredDays = passwordExpiredDays;
    }

    public Boolean getLoginAtSameTime() {
        return loginAtSameTime;
    }

    public void setLoginAtSameTime(Boolean loginAtSameTime) {
        this.loginAtSameTime = loginAtSameTime;
    }

    public Boolean getUseUsb() {
        return useUsb;
    }

    public void setUseUsb(Boolean useUsb) {
        this.useUsb = useUsb;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        for (Role r : this.roles) {
            if (r.getId().equals(role.getId())) {
                return;
            }
        }
        this.roles.add(role);
    }
    
    public void delRole(Role role) {
        for (Role r : this.roles) {
            if (r.getId().equals(role.getId())) {
                this.roles.remove(r);
                return;
            }
        }
    }

}