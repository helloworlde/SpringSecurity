package cn.com.hellowood.springsecurity.mapper;

import cn.com.hellowood.springsecurity.model.RoleModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface RoleModel mapper.
 *
 * @author HelloWood
 */
@Mapper
public interface RoleMapper {
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
    int insert(RoleModel record);

    /**
     * Insert selective int.
     *
     * @param record the record
     * @return the int
     */
    int insertSelective(RoleModel record);

    /**
     * Select by primary key role.
     *
     * @param id the id
     * @return the role
     */
    RoleModel selectByPrimaryKey(Integer id);

    /**
     * Update by primary key selective int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKeySelective(RoleModel record);

    /**
     * Update by primary key int.
     *
     * @param record the record
     * @return the int
     */
    int updateByPrimaryKey(RoleModel record);
}