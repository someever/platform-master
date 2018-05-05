package com.fanfandou.admin.operation.controller;

import com.fanfandou.admin.operation.service.MailOrderService;
import com.fanfandou.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;



/**
 * author zhongliang.
 * Description:GM指令管理操作.
 */
@RestController
@RequestMapping(value = "/operation/gmInstruct")
public class GMInstructController {

    @Autowired
    private MailOrderService mailOrderService;

    /**
     * 跳转到公告List页面
     *
     * @return 分类页面
     */
    @RequestMapping(value = "/gmInstructInit")
    public ModelAndView toList() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/operation/GMInstruct");
        return mav;
    }

    /**
     * 发送GM指令
     *
     * @throws ServiceException
     */
    @RequestMapping(value = "/gmInstruct")
    public void gmInstruct(String gmCmdValue, int gameId, int gmCmdType, String areaIds) throws Exception {
        mailOrderService.gmInstruct(gmCmdValue, gameId, gmCmdType, areaIds);
    }

}
