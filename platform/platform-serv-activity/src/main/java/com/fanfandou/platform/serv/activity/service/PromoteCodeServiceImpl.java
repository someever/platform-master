package com.fanfandou.platform.serv.activity.service;


import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.platform.api.activity.entity.PromoteCode;
import com.fanfandou.platform.api.activity.service.PromoteCodeService;
import com.fanfandou.platform.serv.activity.dao.PromoteCodeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongliang.
 * Descreption:礼包批次管理.
 * Date:2016/3/13
 */
@Service("promoteCodeService")
public class PromoteCodeServiceImpl extends BaseLogger implements PromoteCodeService {
    @Autowired
    private PromoteCodeMapper promoteCodeMapper;

    @Override
    public PageResult codeListPage(Integer gameId, String codeName, Page page) throws Exception {
        if (page.getOrder() == null || page.getOrder().equals("")) {
            page.setOrder("id");
        }
        if (page.getSort() == null || page.getSort().equals("")) {
            page.setSort("desc");
        }
        if (gameId == -1) {
            gameId = null;
        }
//        if (codeName == null || codeName.equals("")) {
//            codeName = "%%";
//        }

        String codeNameStr = codeName;
        int num1 = (page.getPageIndex() - 1) * page.getPageSize();

        Map paramMap = new HashMap();
        paramMap.put("codeName", codeNameStr);
        paramMap.put("gameId", gameId);
        paramMap.put("str1", page.getOrder());
        paramMap.put("str2", page.getSort());
        paramMap.put("num1", num1);
        paramMap.put("num2", page.getPageSize());
        List<PromoteCode> promoteCodeList = this.promoteCodeMapper.pageList(paramMap);

        Map map = new HashMap();
        map.put("codeName", codeNameStr);
        map.put("gameId", gameId);
        int totalCount = this.promoteCodeMapper.totalCount(map);

        page.setTotalCount(totalCount);
        PageResult<PromoteCode> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setRows(promoteCodeList);
        return pageResult;
    }

    @Override
    public PageResult batchPage(Integer gameId, String batchId, String promoteCode, String drawRoleId, Integer drawStatus, Page page) throws Exception {
        if (page.getOrder() == null || page.getOrder().equals("")) {
            page.setOrder("id");
        }
        if (page.getSort() == null || page.getSort().equals("")) {
            page.setSort("desc");
        }
        if (gameId == -1) {
            gameId = null;
        }

        if (promoteCode != null) {
            if (promoteCode.equals("")) {
                promoteCode = null;
            }
        }

        if (drawRoleId != null) {
            if (drawRoleId.equals("")) {
                drawRoleId = null;
            }
        }

//        if (codeName == null || codeName.equals("")) {
//            codeName = "%%";
//        }

        String batchIdStr = batchId;
        int num1 = (page.getPageIndex() - 1) * page.getPageSize();

        Map paramMap = new HashMap();
        paramMap.put("batchId", batchIdStr);
        paramMap.put("gameId", gameId);
        paramMap.put("drawStatus", drawStatus);
        paramMap.put("str1", page.getOrder());
        paramMap.put("str2", page.getSort());
        paramMap.put("num1", num1);
        paramMap.put("num2", page.getPageSize());
        List<PromoteCode> promoteCodeList = new ArrayList<>();
        if (promoteCode != null || drawRoleId != null) {
            promoteCodeList = this.promoteCodeMapper.batchList(paramMap);
        } else {
            promoteCodeList = this.promoteCodeMapper.batchPageList(paramMap);
        }

        Map map = new HashMap();
        map.put("batchId", batchIdStr);
        map.put("gameId", gameId);
        map.put("drawStatus", drawStatus);
        int totalCount = this.promoteCodeMapper.batchTotalCount(map);
        page.setTotalCount(totalCount);
        if (promoteCode != null) {
            String[] codeId = promoteCode.split(",");
            List<PromoteCode> promoteCodePage = new ArrayList<>();
            for (PromoteCode code : promoteCodeList) {
                for (int i = 0; i < codeId.length; i++) {
                    if (code.getPromoteCode().equals(codeId[i])) {
                        promoteCodePage.add(code);
                    }
                }

            }
            page.setTotalCount(1);
            PageResult<PromoteCode> pageResult = new PageResult<>();
            pageResult.setPage(page);
            pageResult.setRows(promoteCodePage);
            return pageResult;
        }


        if (drawRoleId != null) {
            String[] roleId = drawRoleId.split(",");
            List<PromoteCode> promoteCodePage = new ArrayList<>();
            for (PromoteCode code : promoteCodeList) {
                for (int i = 0; i < roleId.length; i++) {
                    if (code.getDrawRoleId().equals(roleId[i])) {
                        promoteCodePage.add(code);
                    }
                }

            }
            page.setTotalCount(1);
            PageResult<PromoteCode> pageResult = new PageResult<>();
            pageResult.setPage(page);
            pageResult.setRows(promoteCodePage);
            return pageResult;
        }


        PageResult<PromoteCode> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setRows(promoteCodeList);
        return pageResult;
    }

    @Override
    public void updatePromoteCode(PromoteCode promoteCode) throws Exception {
        this.promoteCodeMapper.updateByPrimaryKey(promoteCode);
    }

    @Override
    public PromoteCode getPromoteCode(Integer id) throws Exception {
        Long codeId = Long.valueOf(id);
        return this.promoteCodeMapper.selectByPrimaryKey(codeId);
    }

    @Override
    public List<PromoteCode> getPromoteCodeById(int id) throws Exception {
        return this.promoteCodeMapper.getListById(id);
    }
}
