package cn.com.hellowood.springsecurity.mapper;

import cn.com.hellowood.springsecurity.model.MenuModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * The interface Permission model mapper.
 *
 * @author HelloWood
 */
@Mapper
public interface MenuMapper {
    /**
     * Delete by primary key int.
     *
     * @param id the id
     * @return the int
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * Insert int.
     *
     * @param record the record
     * @return the int
     */
    int insert(MenuModel record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(MenuModel record);

    /**
     * Select by primary key permission model.
     *
     * @param id the id
     * @return the permission model
     */
    MenuModel selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(MenuModel record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(MenuModel record);

    /**
     * Gets menu by role id.
     *
     * @param roleId the role id
     * @return the menu by role id
     */
    List<MenuModel> getMenuByRoleId(@Param("roleId") Integer roleId);
}