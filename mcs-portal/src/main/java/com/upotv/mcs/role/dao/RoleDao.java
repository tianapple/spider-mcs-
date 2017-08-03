package com.upotv.mcs.role.dao;
import com.upotv.mcs.core.McsBaseDao;
import com.upotv.mcs.role.entity.PermissionVo;
import com.upotv.mcs.role.entity.Role;
import com.upotv.mcs.role.entity.RoleVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * Created by wangyunpeng on 2017/7/17.
 */
public interface RoleDao extends McsBaseDao {

    List<Role> getRoleListPage(RoleVo vo);

    Role getRoleByName(String name);

    int insert(RoleVo vo);

    int update(RoleVo vo);

    int delete(int roleid);

    int insertPermission(PermissionVo vo);

    int deletePermission(int roleid);

    List<PermissionVo> getPermissionList(PermissionVo vo);
}