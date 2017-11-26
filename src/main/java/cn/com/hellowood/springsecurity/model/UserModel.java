package cn.com.hellowood.springsecurity.model;

import java.util.List;

/**
 * The type User model.
 *
 * @author HelloWood
 */
public class UserModel {

    private Integer id;

    private String username;

    private String password;

    private Boolean enabled;

    private Boolean locked;

    private Boolean expired;

    private RoleModel role;

    private List<MenuModel> menus;

    /**
     * Instantiates a new User model.
     */
    public UserModel() {
    }

    /**
     * Instantiates a new User model.
     *
     * @param id       the id
     * @param username the username
     * @param password the password
     * @param enabled  the enabled
     */
    public UserModel(Integer id, String username, String password, Boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets enabled.
     *
     * @return the enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Sets enabled.
     *
     * @param enabled the enabled
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Gets locked.
     *
     * @return the locked
     */
    public Boolean getLocked() {
        return locked;
    }

    /**
     * Sets locked.
     *
     * @param locked the locked
     */
    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    /**
     * Gets expired.
     *
     * @return the expired
     */
    public Boolean getExpired() {
        return expired;
    }

    /**
     * Sets expired.
     *
     * @param expired the expired
     */
    public void setExpired(Boolean expired) {
        this.expired = expired;
    }


    /**
     * Gets menus.
     *
     * @return the menus
     */
    public List<MenuModel> getMenus() {
        return menus;
    }

    /**
     * Sets menus.
     *
     * @param menus the menus
     */
    public void setMenus(List<MenuModel> menus) {
        this.menus = menus;
    }

    public RoleModel getRole() {
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", locked=" + locked +
                ", expired=" + expired +
                ", role=" + role +
                ", menus=" + menus +
                '}';
    }
}
