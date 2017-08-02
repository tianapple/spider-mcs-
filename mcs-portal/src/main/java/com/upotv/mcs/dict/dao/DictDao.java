package com.upotv.mcs.dict.dao;

import com.upotv.mcs.core.McsBaseDao;
import com.upotv.mcs.dict.entity.McsCode;
import com.upotv.mcs.dict.entity.McsCodeSelectVo;
import com.upotv.mcs.dict.entity.McsCodeVo;
import com.upotv.mcs.menu.controller.MenuController;

import java.util.List;

/**
 * Created by wow on 2017/6/28.
 */
public interface DictDao extends McsBaseDao {

    public List<McsCode> getDictListPage(McsCodeSelectVo code);

    /**
     * 根据指定类型下的可用的字典
     * @param type
     * @return
     */
    public List<McsCode> getDictByType(String type);

    /**
     * 获得所有的可用的字典
     * @return
     */
    public List<McsCode> getAllDict();

}

