package com.fanfandou.admin.server.operation.serviceImpl;

import com.fanfandou.admin.api.operation.entity.Notice;
import com.fanfandou.admin.api.operation.entity.NoticeEnum;
import com.fanfandou.admin.api.operation.service.NoticeService;
import com.fanfandou.admin.server.operation.dao.NoticeMapper;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.constant.PublicCachedKeyConstant;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.service.cache.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangzhenwei on 2016/6/16.
 * Description service实现类
 */
@Service("noticeService")
public class NoticeServiceImpl extends BaseLogger implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Autowired
    private CacheService cacheService;

    /**
     * 查询所有公告
     */
    public List<Notice> selectAll() {
        return this.noticeMapper.selectAll();
    }

    /**
     * 获取分页数据
     * 模糊查询，分页，排序
     */
    public PageResult<Notice> getPageList(String noticeTitleStr, String gameIds, String siteIdss, Page page, String from, String to) throws Exception {
        if (page.getOrder() == null || page.getOrder().equals("")) {
            page.setOrder("id");
        }
        if (page.getSort() == null || page.getSort().equals("")) {
            page.setSort("desc");
        }
        if (gameIds.equals("")) {
            gameIds = null;
        }
        if (gameIds.equals("-1")) {
            gameIds = null;
        }
        if (siteIdss.equals("")) {
            siteIdss = "%%";
        }
        if (noticeTitleStr == null || noticeTitleStr.equals("")) {
            noticeTitleStr = "%%";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date fromDate = null;
        Date toDate = null;
        if (from == null) {
            from = "1900-01-01 00:00:00";
            fromDate = sdf.parse(from);
        } else {
            fromDate = sdf.parse(from);
        }

        if (to == null) {
            toDate = new Date();
        } else {
            toDate = sdf.parse(to);
        }

        String noticeTitle = '%' + noticeTitleStr + '%';
        String siteIds = "%" + siteIdss + ",%";
        int num1 = (page.getPageIndex() - 1) * page.getPageSize();

        Map paramMap = new HashMap();
        paramMap.put("noticeTitle", noticeTitle);
        paramMap.put("gameIds", gameIds);
        paramMap.put("siteIds", siteIds);
        paramMap.put("str1", page.getOrder());
        paramMap.put("str2", page.getSort());
        paramMap.put("num1", num1);
        paramMap.put("num2", page.getPageSize());
        paramMap.put("from", fromDate);
        paramMap.put("to", toDate);
        List<Notice> noticeList = this.noticeMapper.pageList(paramMap);

        Map map = new HashMap();
        map.put("noticeTitle", noticeTitle);
        map.put("gameIds", gameIds);
        map.put("siteIds", siteIds);
        map.put("from", fromDate);
        map.put("to", toDate);
        int totalCount = this.noticeMapper.totalCount(map);

        page.setTotalCount(totalCount);
        PageResult<Notice> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setRows(noticeList);
        return pageResult;
    }


    public void updateCache(Notice notice) throws ServiceException {
        if (notice.getNoticeType().equals(NoticeEnum.NOTICE_ROLE)) {
            return;
        }
        logger.info("updateCache公告信息 content = " + notice.getNoticeContent());
        String siteIdArra[] = notice.getSiteIds().split(",");
        String areaIdArra[] = notice.getAreaIds().split(",");
        for (String siteId : siteIdArra) {
            if (notice.getNoticeType().equals(NoticeEnum.NOTICE_WORLD)) {
                logger.info("刷新世界公告信息");
                cacheService.put(PublicCachedKeyConstant.GAME_WORLD_NOTICE + notice.getGameIds() + siteId, notice);
            }
            for (String areaId : areaIdArra) {
                if (StringUtils.isEmpty(siteId)) {
                    siteId = "0";
                }
                if (StringUtils.isEmpty(areaId)) {
                    areaId = "0";
                }
                if (notice.getNoticeType().equals(NoticeEnum.NOTICE_POPUP)) {
                    logger.info("刷新首页公告信息");
                    cacheService.put(PublicCachedKeyConstant.GAME_NOTICE_LIST + notice.getGameIds() + siteId + areaId + notice.getNoticeType().getId(), notice);
                }
            }
        }
    }


    /**
     * 添加公告.
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void addNotice(Notice notice) throws Exception {
        String str = notice.getSiteIds();
        String siteIds = ',' + str;
        notice.setSiteIds(siteIds);
        String str2 = notice.getAreaIds();
        String areaIds = ',' + str2;
        notice.setAreaIds(areaIds);
        notice.setCreateTime(new Date());
        notice.setNoticeState(1);
        updateCache(notice);
        this.noticeMapper.insert(notice);
    }

    /**
     * 删除公告.
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void delNotice(List<Integer> idList) {
        for (int i = 0; i < idList.size(); i++) {
            int id = idList.get(i);
            Notice notice = noticeMapper.selectById(id);
            this.noticeMapper.delete(id);
            //cacheService.hDel(PublicCachedKeyConstant.GAME_NOTICE_LIST + notice.getGameIds() + notice.getsi + areaId + 1, notice.getId() + "");
            String siteIdArra[] = notice.getSiteIds().split(",");
            String areaIdArra[] = notice.getAreaIds().split(",");
            for (String siteId : siteIdArra) {
                if (notice.getNoticeType().equals(NoticeEnum.NOTICE_WORLD)) {
                    logger.info("刷新世界公告信息");
                    cacheService.del(PublicCachedKeyConstant.GAME_WORLD_NOTICE + notice.getGameIds() + siteId);
                }
                for (String areaId : areaIdArra) {
                    if (StringUtils.isEmpty(siteId)) {
                        siteId = "0";
                    }
                    if (StringUtils.isEmpty(areaId)) {
                        areaId = "0";
                    }
                    cacheService.del(PublicCachedKeyConstant.GAME_NOTICE_LIST + notice.getGameIds() + siteId + areaId + 1);
                }
            }
        }
    }

    /**
     * 更新公告.
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void updNotice(Notice notice) throws Exception {
        String str = notice.getSiteIds();
        String siteIds = ',' + str;
        String str2 = notice.getAreaIds();
        String areaIds = ',' + str2;
        notice.setSiteIds(siteIds);
        notice.setAreaIds(areaIds);
        updateCache(notice);
        this.noticeMapper.update(notice);
    }

    /**
     * 通过id查找公告
     */
    public Notice selNoticeById(int id) throws Exception {
        Notice notice = this.noticeMapper.selectById(id);// cacheService.hGet(PublicCachedKeyConstant.GAME_NOTICE_LIST, id + "", Notice.class);
        return notice;
    }

    /**
     * 开启或关闭
     */
    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    public void updInvalid(String ids, int state) throws Exception {
        String noticeIds[] = ids.split(",");
        for (String id : noticeIds) {
            Notice notice = this.selNoticeById(Integer.parseInt(id));
            if (state == 0 || state == 1) {
                notice.setNoticeState(state);
            }
            this.updNotice(notice);
        }
    }

    public Notice findByGameSiteAreaId(int gameId, int siteId, int areaId, int noticeType) {
        Notice notice = null;
        if (noticeType != 4) {
            notice = cacheService.get(PublicCachedKeyConstant.GAME_NOTICE_LIST + gameId + siteId + areaId + noticeType, Notice.class);
        } else {
            logger.info("获取世界公告");
            notice = cacheService.get(PublicCachedKeyConstant.GAME_WORLD_NOTICE + gameId + siteId, Notice.class);
        }

        if (notice == null) {
            Map idMap = new HashMap();
            idMap.put("gameIds", gameId);
            String siteIds = "%," + siteId + ",%";
            if (noticeType == 4) {
                siteIds = null;
            }
            idMap.put("siteIds", siteIds);
            String areaIds = "";
            if (areaId == 0) {
                areaIds = null;
            } else {
                areaIds = "%," + areaId + ",%";
            }

            idMap.put("areaIds", areaIds);
            idMap.put("noticeType", noticeType);
            notice = noticeMapper.selByArea(idMap);

            if (notice != null && noticeType == 1) {
                logger.info("缓存首页公告");
                cacheService.put(PublicCachedKeyConstant.GAME_NOTICE_LIST + gameId + siteId + areaId + noticeType, notice);
            }

            if (notice != null && noticeType == 4) {
                logger.info("缓存世界公告");
                cacheService.put(PublicCachedKeyConstant.GAME_WORLD_NOTICE + gameId + siteId, notice);
            }
        }

        return notice;
    }

    @Override
    public int noticeCount(int gameId, int areaId, int noticeTypes, int siteId, Integer noticeId) {
        Map idMap = new HashMap();
        idMap.put("gameIds", gameId);
        idMap.put("noticeType", noticeTypes);
        idMap.put("noticeId", noticeId);
        String areaIds = "";
        if (areaId == 0) {
            areaIds = null;
        } else {
            areaIds = "%," + areaId + ",%";
        }

        String siteIds = "";
        if (siteId == 0) {
            siteIds = null;
        } else {
            siteIds = "%," + siteId + ",%";
        }
        idMap.put("areaIds", areaIds);
        idMap.put("siteIds", siteIds);

        return noticeMapper.noticeCount(idMap);
    }

    @Override
    public Notice getNoticeNewData() {
        return noticeMapper.getRowData();
    }
}
