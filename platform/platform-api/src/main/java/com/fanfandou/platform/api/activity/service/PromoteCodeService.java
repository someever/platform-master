package com.fanfandou.platform.api.activity.service;

import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.activity.entity.PromoteCode;
import com.fanfandou.platform.api.activity.entity.PromoteCodeBatch;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Created by zhongliang.
 * Descreption:礼包码-code管理.
 * Date:2016/11/22
 */
public interface PromoteCodeService {

    /**
     * 批次分页查询.
     */
    PageResult codeListPage(Integer gameId, String codeName, Page page) throws Exception;


    /**
     * 批次分页查询.
     */
    PageResult batchPage(Integer gameId, String batchId, String promoteCode, String drawRoleId, Integer drawStatus, Page page) throws Exception;


    /**
     * 修改code
     *
     * @param promoteCode
     * @throws Exception
     */
    void updatePromoteCode(PromoteCode promoteCode) throws Exception;

    PromoteCode getPromoteCode(Integer id) throws Exception;

    List<PromoteCode> getPromoteCodeById(int id) throws Exception;
}
