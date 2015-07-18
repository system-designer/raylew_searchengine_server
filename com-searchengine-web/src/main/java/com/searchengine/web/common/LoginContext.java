package com.searchengine.web.common;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class LoginContext {
    /**
     * Constant for the LoginContext object.
     */
    public static final String HTTP_LOGIN_CONTEXT = "com.searchengine.common.web.LoginContext";
    private final static Log log = LogFactory.getLog(LoginContext.class);
    private final static ThreadLocal<LoginContext> holder = new ThreadLocal<LoginContext>();
    private static final int SESSION_TIMEOUT = 24 * 60 * 60 * 1000;    //单位：毫秒

    private static final int SESSION_CHECKSUM = 610;
    /**
     * 用户ID
     */
    private long userId;
    /**
     * 用户角色
     */
    private int roleType;
    /**
     * 登录名称
     */
    private String loginName;
    /**
     * 显示名称
     */
    private String nickName;
    /**
     * passport cookie的checksum
     */
    private int checksum;
    /**
     * 创建时间 默认为当前时间
     */
    private long created = System.currentTimeMillis();
    /**
     * 过期时间 如果没有指定，就使用拦截器默认的
     */
    private long expires;
    /**
     * 是否持久化
     */
    private boolean persistent;

    public LoginContext() {

    }

    public LoginContext(long userId, String loginName, String nickName) {
        this.userId = userId;
        this.loginName = loginName;
        this.nickName = nickName;
        this.setTimeout(SESSION_TIMEOUT);
        this.setPersistent(true);
        this.setChecksum(SESSION_CHECKSUM);
    }

    /**
     * 取出登录的上下文
     *
     * @return null 如果没有的话
     */
    public static LoginContext getLoginContext() {
        return holder.get();
    }

    /**
     * 实际上是将loginContext放到了actionContext中
     *
     * @param loginContext 对象
     */
    public static void setLoginContext(LoginContext loginContext) {
        holder.set(loginContext);
    }

    /**
     * 删除上下文、其实一般不用删除
     */
    public static void remove() {
        holder.remove();
    }

    /**
     * 反向构造上下文。
     *
     * @param value 需要反向构造的串。形式如下：userId=1071682780741632,loginName=aishangzoulu,nickName=昵称
     * @return 上下文
     * @see #toCookieValue()
     */
    public static LoginContext parse(String value) {
        LoginContext context = new LoginContext();
        setValue(value, context);
        return context;
    }

    /**
     * Method setValue ...
     *
     * @param value   of type String
     * @param context of type LoginContext
     */
    protected static void setValue(String value, LoginContext context) {
        if (StringUtils.isNotEmpty(value)) {
            String[] fields = value.split(",");
            for (String keyValues : fields) {
                String[] keyValue = keyValues.split("=");
                if (keyValue.length == 2) {
                    try {
                        String field = keyValue[0];
                        if (StringUtils.isNotBlank(field)) {
                            BeanUtils.setProperty(context, field, StringEscapeUtils.unescapeJava(keyValue[1]));
                        }
                    } catch (Exception e) {
                        log.warn("praser error!", e);
                    }
                }
            }
        }
    }

    /**
     * Method getUserId returns the userId of this LoginContext object.
     * <p/>
     * 用户ID
     *
     * @return the userId (type long) of this LoginContext object.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Method setUserId sets the userId of this LoginContext object.
     * <p/>
     * 用户ID
     *
     * @param userId the userId of this LoginContext object.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * Method getPin returns the pin of this LoginContext object.
     * <p/>
     * 登录名称
     *
     * @return the loginName (type String) of this LoginContext object.
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * Method setLoginName sets the loginName of this LoginContext object.
     * <p/>
     * 登录名称
     *
     * @param loginName the pin of this LoginContext object.
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * Method getNickName returns the nick of this LoginContext object.
     * <p/>
     * 显示名称
     *
     * @return the nickName (type String) of this LoginContext object.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Method setNickName sets the nickName of this LoginContext object.
     * <p/>
     * 显示名称
     *
     * @param nickName the nickName of this LoginContext object.
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 将实体系列化成字符串。默认值不再写入cookie，以减少cookie的大小。
     *
     * @return 字符串。形式：字段1=值1,字段2=值2。该方法不会返回空
     * @see #parse(String)
     */
    public String toCookieValue() {
        final StringBuilder sb = new StringBuilder();
        sb.append("WWWRAYLEWINFO12334");
        if (userId != 0) {
            sb.append(",userId=").append(userId);
        }
        if (StringUtils.isNotEmpty(loginName)) {
            sb.append(",loginName=").append(StringEscapeUtils.escapeJava(loginName));
        }
        if (StringUtils.isNotEmpty(nickName)) {
            sb.append(",nickName=").append(StringEscapeUtils.escapeJava(nickName));
        }
        if (created != 0) {
            sb.append(",created=").append(created);
        }
        if (checksum != 0) {
            sb.append(",checksum=").append(checksum);
        }
        if (expires != 0) {
            sb.append(",expires=").append(expires);
        }
        if (persistent) {
            sb.append(",persistent=").append(persistent);
        }
        return sb.length() > 0 ? sb.substring(1) : "";
    }

    /**
     * 判断是否登录。标准：trim(pin).length > 0
     *
     * @return true 已经登录 false 没有登录
     */
    public boolean getLogin() {
        return isLogin();
    }

    /**
     * 判断是否登录。标准：trim(loginName).length > 0
     *
     * @return true 已经登录 false 没有登录
     */
    public boolean isLogin() {
        return StringUtils.isNotBlank(loginName);
    }

    /**
     * Method getChecksum returns the checksum of this LoginContext object.
     * <p/>
     * passport cookie的checksum
     *
     * @return the checksum (type int) of this LoginContext object.
     */
    public int getChecksum() {
        return checksum;
    }

    /**
     * Method setChecksum sets the checksum of this LoginContext object.
     * <p/>
     * passport cookie的checksum
     *
     * @param checksum the checksum of this LoginContext object.
     */
    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }

    /**
     * Method getCreated returns the created of this LoginContext object.
     * <p/>
     * 创建时间
     *
     * @return the created (type long) of this LoginContext object.
     */
    public long getCreated() {
        return created;
    }

    /**
     * Method setCreated sets the created of this LoginContext object.
     * <p/>
     * 创建时间
     *
     * @param created the created of this LoginContext object.
     */
    public void setCreated(long created) {
        this.created = created;
    }

    /**
     * 创建日期
     *
     * @return 创建日期
     */
    public Date getCreatedDate() {
        return new Date(created);
    }

    /**
     * Method setCreatedDate sets the createdDate of this LoginContext object.
     *
     * @param created the createdDate of this LoginContext object.
     */
    public void setCreatedDate(Date created) {
        this.created = created.getTime();
    }

    /**
     * 设置创建时间等于当前日期
     */
    public void setCreated() {
        this.created = System.currentTimeMillis();
    }

    /**
     * Method getExpires returns the expires of this LoginContext object.
     * <p/>
     * 过期时间
     *
     * @return the expires (type long) of this LoginContext object.
     */
    public long getExpires() {
        return expires;
    }

    /**
     * Method setExpires sets the expires of this LoginContext object.
     * <p/>
     * 过期时间
     *
     * @param expires the expires of this LoginContext object.
     */
    public void setExpires(long expires) {
        this.expires = expires;
    }

    /**
     * Method getExpiresDate returns the expiresDate of this LoginContext
     * object.
     *
     * @return the expiresDate (type Date) of this LoginContext object.
     */
    public Date getExpiresDate() {
        return new Date(expires);
    }

    /**
     * Method setExpiresDate sets the expiresDate of this LoginContext object.
     *
     * @param expires the expiresDate of this LoginContext object.
     */
    public void setExpiresDate(Date expires) {
        this.expires = expires.getTime();
    }

    /**
     * 设置cookie的过期时间，单位：毫秒
     *
     * @param timeout
     */
    public void setTimeout(long timeout) {
        this.expires = this.created + timeout;
    }

    /**
     * Method isPersistent returns the persistent of this LoginContext object.
     * <p/>
     * 是否持久化
     *
     * @return the persistent (type boolean) of this LoginContext object.
     */
    public boolean isPersistent() {
        return persistent;
    }

    /**
     * Method setPersistent sets the persistent of this LoginContext object.
     * <p/>
     * 是否持久化
     *
     * @param persistent the persistent of this LoginContext object.
     */
    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

}
