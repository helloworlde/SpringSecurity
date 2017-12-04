package cn.com.hellowood.springsecurity.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * The type User model.
 *
 * @author HelloWood
 */
public class UserModel implements Serializable {

    private static final long serialVersionUID = -2857744165762872625L;

    private Integer id;

    private String username;

    private String password;

    private boolean enabled;

    private boolean locked;

    private boolean expired;

    private Integer category;

    private String categoryName;

    private String categoryValue;

    private String categoryDisplayValue;

    private Date addTime;

    private Date lastUpdateTime;

    private String comment;

    private RoleModel role;

    private List<MenuModel> menus;

    public UserModel() {
    }

    public UserModel(Integer id, String username, String password, boolean enabled, boolean locked, boolean expired, Integer category, String categoryValue, String categoryDisplayValue, Date addTime, Date lastUpdateTime, String comment) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.locked = locked;
        this.expired = expired;
        this.category = category;
        this.categoryValue = categoryValue;
        this.categoryDisplayValue = categoryDisplayValue;
        this.addTime = addTime;
        this.lastUpdateTime = lastUpdateTime;
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryValue() {
        return categoryValue;
    }

    public void setCategoryValue(String categoryValue) {
        this.categoryValue = categoryValue;
    }

    public String getCategoryDisplayValue() {
        return categoryDisplayValue;
    }

    public void setCategoryDisplayValue(String categoryDisplayValue) {
        this.categoryDisplayValue = categoryDisplayValue;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

    public List<MenuModel> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuModel> menus) {
        this.menus = menus;
    }
}
