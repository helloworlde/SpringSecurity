package cn.com.hellowood.springsecurity.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author HelloWood
 */
public class RoleModel implements Serializable {

    private static final long serialVersionUID = -6849858006084750094L;

    private Integer id;

    private String name;

    private boolean active;

    private String value;

    private String description;

    private Date lastUpdateTime;

    public RoleModel() {
    }

    public RoleModel(Integer id, String name, boolean active, String value, String description, Date lastUpdateTime) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.value = value;
        this.description = description;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}