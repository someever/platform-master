package com.fanfandou.admin.operation.controller;


import com.fanfandou.admin.api.operation.entity.Notice;
import com.fanfandou.admin.api.operation.entity.NoticeEnum;
import com.fanfandou.admin.api.operation.service.NoticeService;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * author zhongliang.
 * Description:玩具管理操作.
 */
@RestController
@RequestMapping(value = "/operation/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private OperationDispatchService operationDispatchService;

    /**
     * 跳转到公告List页面
     *
     * @return 分类页面
     */
    @RequestMapping(value = "/noticeInit")
    public ModelAndView toList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/operation/NoticeList");
        return mav;
    }

    /**
     * 跳转到公告添加页面
     *
     * @return 分类页面
     */
    @RequestMapping(value = "/noticeAdd")
    public ModelAndView toAdd() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/operation/NoticeAdd");
        return mav;
    }

    /**
     * 跳转到公告添加页面
     *
     * @return 分类页面
     */
    @RequestMapping(value = "/noticeEdit")
    public ModelAndView toEdit() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/operation/NoticeEdit");
        return mav;
    }

    /**
     * 跳转到公告修改加页面
     *
     * @return 分类页面
     */
    @RequestMapping(value = "/noticeUpdate")
    public ModelAndView toUpdate() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/operation/NoticeUpdate");
        return mav;
    }

    /**
     * 添加
     *
     * @param notice      公告对象
     * @param noticeTypes 公告类型
     * @throws ServiceException
     */
    @RequestMapping(value = "/insert")
    public void insert(Notice notice, int noticeTypes) throws Exception {
        notice.setNoticeType(NoticeEnum.ValueOf(noticeTypes));
        try {
            Subject subject = SecurityUtils.getSubject();
            String publisher = subject.getPrincipal().toString();
            notice.setPublisher(publisher);
            this.noticeService.addNotice(notice);
        } catch (Exception e) {
            return;
        }

        Notice noticeData = this.noticeService.getNoticeNewData();
        if (noticeData.getNoticeType() == NoticeEnum.NOTICE_ROLE) {
            String regExhtml = "<[^>]+>"; // 定义HTML标签的正则表达式
            Pattern phtml = Pattern.compile(regExhtml, Pattern.CASE_INSENSITIVE);
            Matcher mhtml = phtml.matcher(noticeData.getNoticeContent());
            String html = mhtml.replaceAll(""); // 过滤html标签
            noticeData.setNoticeContent(html);
            String[] areaIds = noticeData.getAreaIds().split(",");
            for (int i = 0; i < areaIds.length; i++) {
                if (areaIds[i] != null && !areaIds[i].equals("")) {
                    GameCode gameCode = new GameCode();
                    gameCode.setId(Integer.parseInt(noticeData.getGameIds()));
                    int areaId = Integer.parseInt(areaIds[i]);
                    this.operationDispatchService.sendScrollNotice(gameCode, noticeData.getId(), areaId, noticeData.getBeginTime().getTime(), noticeData.getEndTime().getTime(), noticeData.getNoticeContent(), noticeData.getPublishCount());
                }
            }
        }

    }

    /**
     * 修改
     *
     * @param notice      公告对象
     * @param noticeTypes 公告类型
     * @throws ServiceException
     */
    @RequestMapping(value = "/update")
    public void update(Notice notice, int noticeTypes) throws Exception {
        Subject subject = SecurityUtils.getSubject();
        String publisher = subject.getPrincipal().toString();
        notice.setPublisher(publisher);
        notice.setNoticeType(NoticeEnum.ValueOf(noticeTypes));
        this.noticeService.updNotice(notice);
    }

    /**
     * 根据id查询公告
     *
     * @param noticeId 公告id
     * @return 公告对象
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/getNotice")
    public Notice getNoticeById(Integer noticeId) throws Exception {
        Notice notice = this.noticeService.selNoticeById(noticeId);
        return notice;
    }

    /**
     * 根据id删除.
     *
     * @param noticeIds id集合
     */

    @RequestMapping(value = "/delete")
    public void delete(String noticeIds) throws ServiceException {
        String[] notices = noticeIds.split(",");
        List<Integer> noticeList = new ArrayList<Integer>();
        for (int i = 0; i < notices.length; i++) {
            noticeList.add(Integer.parseInt(notices[i]));
        }
        this.noticeService.delNotice(noticeList);
    }

    /**
     * 分页查询
     *
     * @param noticeTitle 公告标题
     * @param gameId      游戏id
     * @param siteId      渠道id
     * @param page        page对象
     * @param from        开始时间
     * @param to          结束时间
     * @return 分页对象集合
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping("/pageList")
    public PageResult<Notice> getPageList(String noticeTitle, String gameId, String siteId, Page page, String from, String to) throws Exception {
        return this.noticeService.getPageList(noticeTitle, gameId, siteId, page, from, to);
    }

    /**
     * 根据id修改状态
     *
     * @param ids   id集合
     * @param state 状态码
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/updateState")
    public void noticeState(String ids, int state) throws Exception {
        this.noticeService.updInvalid(ids, state);
    }

    /**
     * 查询是否存在重复的公告
     *
     * @param gameId 游戏id
     * @param areaId 区服id
     * @return 数量
     * @throws ServiceException
     */
    @ResponseBody
    @RequestMapping(value = "/noticeCount")
    public int noticeCount(Integer noticeId, int gameId, int areaId, int noticeTypes, int siteId) throws ServiceException {
        return this.noticeService.noticeCount(gameId, areaId, noticeTypes, siteId, noticeId);
    }

}
