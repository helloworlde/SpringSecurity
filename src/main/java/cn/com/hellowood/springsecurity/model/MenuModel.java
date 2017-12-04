package cn.com.hellowood.springsecurity.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author HelloWood
 */
public class MenuModel implements Serializable {

    private static final long serialVersionUID = 3902090352219967399L;

    private Integer id;

    private String value;

    private String displayValue;

    private String url;

    private Integer category;

    private String description;

    private boolean active;

    private Date lastUpdateTime;

    public MenuModel() {
    }

    public MenuModel(Integer id, String value, String displayValue, String url, Integer category, String description, boolean active, Date lastUpdateTime) {
        this.id = id;
        this.value = value;
        this.displayValue = displayValue;
        this.url = url;
        this.category = category;
        this.description = description;
        this.active = active;
        this.lastUpdateTime = lastUpdateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public void setDisplayValue(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}