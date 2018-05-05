package com.fanfandou.platform.serv.activity.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.common.constant.PublicCachedKeyConstant;
import com.fanfandou.common.entity.ActStatus;
import com.fanfandou.common.entity.ValidStatus;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.sequence.TableSequenceGenerator;
import com.fanfandou.common.service.cache.CacheService;
import com.fanfandou.common.util.CipherUtils;
import com.fanfandou.common.util.GenCodeUtil;
import com.fanfandou.common.util.HttpUtils;
import com.fanfandou.platform.api.activity.entity.LimitedType;
import com.fanfandou.platform.api.activity.entity.PromoteAwardPackage;
import com.fanfandou.platform.api.activity.entity.PromoteCode;
import com.fanfandou.platform.api.activity.entity.PromoteCodeBatch;
import com.fanfandou.platform.api.activity.entity.PromoteCodeBatchExample;
import com.fanfandou.platform.api.activity.entity.PromoteCodeExample;
import com.fanfandou.platform.api.activity.entity.PromoteUsingRule;
import com.fanfandou.platform.api.activity.exception.ActivityException;
import com.fanfandou.platform.api.activity.service.PromoteCodeBatchService;
import com.fanfandou.platform.api.billing.entity.GoodsItem;
import com.fanfandou.platform.api.billing.entity.GoodsItemPackage;
import com.fanfandou.platform.api.constant.IcachedConstant;
import com.fanfandou.platform.api.constant.TableSequenceConstant;
import com.fanfandou.platform.api.game.entity.GameArea;
import com.fanfandou.platform.api.game.entity.GameRole;
import com.fanfandou.platform.api.game.entity.OperationType;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.GameRoleService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import com.fanfandou.platform.api.user.entity.UserAccount;
import com.fanfandou.platform.api.user.service.AccountService;
import com.fanfandou.platform.serv.activity.dao.PromoteAwardPackageMapper;
import com.fanfandou.platform.serv.activity.dao.PromoteCodeBatchMapper;
import com.fanfandou.platform.serv.activity.dao.PromoteCodeMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhongliang.
 * Descreption:礼包批次管理.
 * Date:2016/3/13
 */
@Service("promoteCodeBatchService")
public class PromoteCodeBatchServiceImpl extends BaseLogger implements PromoteCodeBatchService {

    @Autowired
    private PromoteCodeBatchMapper promoteCodeBatchMapper;

    @Autowired
    private PromoteAwardPackageMapper promoteAwardPackageMapper;

    @Autowired
    private TableSequenceGenerator tableSequenceGenerator;

    @Autowired
    private PromoteCodeMapper promoteCodeMapper;

    @Autowired
    private OperationDispatchService operationDispatchService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private GameAreaService gameAreaService;

    @Autowired
    private GameRoleService gameRoleService;

    @Autowired
    private AccountService accountService;

    @Transactional(rollbackFor = {ServiceException.class, RuntimeException.class})
    @Override
    public void createCodeBatch(PromoteCodeBatch promoteCodeBatch) throws ServiceException {

        promoteCodeBatch.setCreateDate(new Date());
        promoteCodeBatch.setUsedAmount(0);
        promoteCodeBatch.setDeriveStatus(ValidStatus.VALID);//礼包码导出状态，1：准备导出，2：导出中，3：导出
        this.promoteCodeBatchMapper.insertSelective(promoteCodeBatch);
    }

    @Transactional(rollbackFor = {ServiceException.class, RuntimeException.class})
    @Override
    public void updateCodeBatch(PromoteCodeBatch promoteCodeBatch) throws ServiceException {
        this.promoteCodeMapper.deleteByBatchId(promoteCodeBatch.getBatchId());
        cacheService.del(PublicCachedKeyConstant.PROMOTE_CODE_BATCH_LIST + promoteCodeBatch.getBatchId());
        this.promoteCodeBatchMapper.updateByPrimaryKey(promoteCodeBatch);
    }

    @Override
    public PageResult getCodeBatch(Integer gameId, String batchName, Page page, String from, String to) throws Exception {

        if (page.getOrder() == null || page.getOrder().equals("")) {
            page.setOrder("id");
        }
        if (page.getSort() == null || page.getSort().equals("")) {
            page.setSort("desc");
        }
        if (gameId == -1) {
            gameId = null;
        }
        if (batchName == null || batchName.equals("")) {
            batchName = "%%";
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

        String packageNameStr = '%' + batchName + '%';
        int num1 = (page.getPageIndex() - 1) * page.getPageSize();

        Map paramMap = new HashMap();
        paramMap.put("batchName", packageNameStr);
        paramMap.put("gameId", gameId);
        paramMap.put("str1", page.getOrder());
        paramMap.put("str2", page.getSort());
        paramMap.put("num1", num1);
        paramMap.put("num2", page.getPageSize());
        paramMap.put("from", fromDate);
        paramMap.put("to", toDate);
        List<PromoteCodeBatch> promoteCodeBatchList = this.promoteCodeBatchMapper.pageList(paramMap);
        Map map = new HashMap();
        map.put("batchName", packageNameStr);
        map.put("gameId", gameId);
        map.put("from", fromDate);
        map.put("to", toDate);
        int totalCount = this.promoteCodeBatchMapper.totalCount(map);

        page.setTotalCount(totalCount);
        PageResult<PromoteCodeBatch> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setRows(promoteCodeBatchList);
        return pageResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS, rollbackFor = Exception.class)
    @Override
    public PromoteCodeBatch getCodeBatchById(Integer id) throws ServiceException {
        return this.promoteCodeBatchMapper.selectByPrimaryKey(id);
    }

    @Override
    public void deleteCodeBatchById(List<Integer> idList) throws ServiceException {
        for (int i = 0; i < idList.size(); i++) {
            int id = idList.get(i);
            this.promoteCodeBatchMapper.deleteByPrimaryKey(id);
            this.promoteCodeMapper.deleteByBatchId(id);
            cacheService.del(PublicCachedKeyConstant.PROMOTE_CODE_BATCH_LIST + id);
        }

    }

    @Override
    public void generatePromoteCode(PromoteCodeBatch promoteCodeBatch) throws Exception {
        logger.info("进来了----------------------------------" + promoteCodeBatch.getBatchId());
        promoteCodeBatch.setDeriveStatus(ValidStatus.INVALID);
        this.promoteCodeBatchMapper.updateByPrimaryKey(promoteCodeBatch);
        ExecutorService exe = Executors.newFixedThreadPool(20);
        exe.execute(new PromoteCodeBatchThread(promoteCodeBatch));
        exe.shutdown();
        while (true) {
            if (exe.isTerminated()) {
                promoteCodeBatch.setDeriveStatus(ValidStatus.REMOVED);
                this.promoteCodeBatchMapper.updateByPrimaryKey(promoteCodeBatch);
                break;
            }
            Thread.sleep(200);
        }
    }

    class PromoteCodeBatchThread extends Thread {
        private PromoteCodeBatch promoteCodeBatch;

        public PromoteCodeBatchThread(PromoteCodeBatch promoteCodeBatch) {
            this.promoteCodeBatch = promoteCodeBatch;
        }

        @Override
        public void run() {
            logger.info("我进来了批次生产----------------------------------数量" + promoteCodeBatch.getAmount());
            //获取礼包码个数
            int codeNum = promoteCodeBatch.getAmount();
            List<String> codes = new ArrayList<>();
            Date date = new Date();
            String startCode = "";
            if (promoteCodeBatch.getSiteId() == 34) {
                startCode = "TX";
            }
            if (codeNum > 0) {
                for (int i = 0; i < codeNum; i++) {
                    long seqValue = 0;
                    try {
                        seqValue = tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_ACTIVITY_PROMOTE_CODE);
                    } catch (ServiceException e) {
                        e.printStackTrace();
                    }

                    String code = startCode + GenCodeUtil.generateCode(String.format("%07d", i));
                    logger.info("这是一个code啊----------------------------------" + code);
                    PromoteCode promoteCode = new PromoteCode();
                    promoteCode.setCodeId(seqValue);
                    promoteCode.setBatchId(promoteCodeBatch.getBatchId());
                    promoteCode.setCreateDate(date);
                    promoteCode.setDeliverStatus(ActStatus.UNACT);
                    promoteCode.setValidStatus(ValidStatus.VALID);
                    promoteCode.setPromoteCode(code);
                    promoteCode.setDrawSiteId(promoteCodeBatch.getSiteId());
                    promoteCode.setDrawStatus(ActStatus.UNACT);
                    promoteCode.setDrawGameId(promoteCodeBatch.getGameId());
                    promoteCodeMapper.insertSelective(promoteCode);
                    logger.info("添加一个code啊----------------------------------" + promoteCode.getPromoteCode());
                    //cacheService.hPut(PublicCachedKeyConstant.ACTIVITY_PROMOTE_CODE, code, promoteCode);
                    codes.add(code);
                }

                cacheService.putList(PublicCachedKeyConstant.PROMOTE_CODE_BATCH_LIST + promoteCodeBatch.getBatchId(), codes);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class, RuntimeException.class})
    public String checkPromoteCode(int gameId, int siteId, String areaCode, int roleId, long userId, String code, String channel) throws ServiceException {
        /*PromoteCode promoteCode = cacheService.hGet(PublicCachedKeyConstant.ACTIVITY_PROMOTE_CODE, code,
                PromoteCode.class);*/

        GameArea gameArea = gameAreaService.getGameAreaByCode(gameId, areaCode);
        PromoteCodeExample promoteCodeExample = new PromoteCodeExample();
        promoteCodeExample.createCriteria().andPromoteCodeEqualTo(code);
        List<PromoteCode> promoteCodeList = promoteCodeMapper.selectByExample(promoteCodeExample);
        if (CollectionUtils.isEmpty(promoteCodeList)) {
            throw new ActivityException(ActivityException.GAME_ACITIVITT_PROMOTE_CODE_UNEXIST, "礼包码不存在");
        }
        PromoteCode promoteCode = promoteCodeMapper.selectByExample(promoteCodeExample).get(0);
        //PromoteCode promoteCode = promoteCodeMapper.selectByPrimaryKey()
        //礼包码校验

        if (gameId != promoteCode.getDrawGameId()) {
            throw new ActivityException(ActivityException.GAME_ACITIVITT_PROMOTE_CODE_NOTMATCH, "游戏不匹配");
        }

        long nowDate = new Date().getTime();
        //判断礼包码类型
        //根据激活码获取批次信息
        PromoteCodeBatch codeBatch = promoteCodeBatchMapper.selectByPrimaryKey(promoteCode.getBatchId());
        //再根据批次获取到包裹信息
        PromoteAwardPackage awardPackage = promoteAwardPackageMapper.selectByPrimaryKey(codeBatch.getPackageId());

        PromoteCodeExample promoteCodeExample1 = new PromoteCodeExample();
        promoteCodeExample1.createCriteria().andDrawRoleIdEqualTo(roleId + "").andBatchIdEqualTo(promoteCode.getBatchId());
        List<PromoteCode> promoteCodeList1 = promoteCodeMapper.selectByExample(promoteCodeExample1);

        if (codeBatch.getSiteId() != 0 && codeBatch.getSiteId() != siteId) {
            throw new ActivityException(ActivityException.GAME_ACITIVITT_PROMOTE_CODE_NOTMATCH, "渠道不匹配");
        }

        if (!CollectionUtils.isEmpty(promoteCodeList1)) {
            throw new ActivityException(ActivityException.GAME_ACITIVITT_PROMOTE_CODE_NOTMATCH, "同类型礼包只能领取一次");
        }

        String unlimitPromote = cacheService.hGet(IcachedConstant.ACTIVITY_PROMOTE_UNLIMIT_USER + gameId, roleId + promoteCode.getBatchId() + "", String.class);
        logger.info("unlimitPromote : " + unlimitPromote);
        if (!StringUtils.isEmpty(unlimitPromote)) {
            throw new ActivityException(ActivityException.GAME_ACITIVITT_PROMOTE_CODE_NOTMATCH, "你已领取过该类型礼包");
        }


        String tips = null;
        long promoteEndDate = codeBatch.getAvailableEndDate().getTime();
        if (nowDate > promoteEndDate) {
            throw new ActivityException(ActivityException.GAME_ACTIVITY_PROMOTE_CODE_OUTOFDATE, "激活码已过期");
        }
        if (promoteCode.getDrawStatus().equals(ActStatus.ACTED) || !promoteCode.getValidStatus()
                .equals(ValidStatus.VALID)) {
            throw new ActivityException(ActivityException.GAME_ACTIVITY_PROMOTE_CODE_DULPLICATE, "激活码已被使用或者无效");
        }

        List<String> roleIds = new ArrayList<>();
        roleIds.add(roleId + "");
        String title = "兑换码奖励";
        logger.info("开始区分礼包类型");
        switch (awardPackage.getPromoteCategory()) {
            case ACTIVE:
                logger.info("开始激活码");
                promoteCode.setDeliverDate(new Date());
                promoteCode.setDrawRoleId(roleId + "");
                promoteCode.setDrawUserId((int) userId);
                promoteCode.setDrawStatus(ActStatus.ACTED);
                promoteCode.setValidStatus(ValidStatus.INVALID);
                promoteCodeMapper.updateByPrimaryKeySelective(promoteCode);
                //cacheService.hPut(PublicCachedKeyConstant.ACTIVITY_PROMOTE_CODE, code, promoteCode);
                tips = codeBatch.getBatchDesc();
                break;

            case GIFT:
                logger.info("开始发放礼包");
                //可领取次数判断
                /*PromoteUsingRule usingRule = JSONObject
                        .toJavaObject(JSON.parseObject(codeBatch.getUsingRule()), PromoteUsingRule.class);*/
                JSONObject jsonObject = JSONObject.parseObject(codeBatch.getUsingRule());
                LimitedType limitedType = LimitedType.valueOf(jsonObject.getIntValue("limitedType"));
                String desc = "礼包码礼包";
                if (!StringUtils.isEmpty(codeBatch.getAwardGreet())) {
                    desc = codeBatch.getAwardGreet();
                }
                if (limitedType.equals(LimitedType.NOLIMIT)) {
                    logger.info("无限制领取礼包");
                    //直接发物品。
                    List<GoodsItem> goodsItems = JSON.parseArray(awardPackage.getItemsPackage(), GoodsItem.class);
                    GoodsItemPackage goodsItemPackage = new GoodsItemPackage();
                    goodsItemPackage.getGoodsItems().addAll(goodsItems);
                    goodsItemPackage.setPackageDesc(desc);
                    operationDispatchService.sendPackage(GameCode.getById(gameId), gameArea.getId(), title, desc
                            , goodsItemPackage, userId, 100000, roleIds, OperationType.MULTI_SEND_ITEM);
                    cacheService.hPut(IcachedConstant.ACTIVITY_PROMOTE_UNLIMIT_USER + gameId, roleId + promoteCode.getBatchId() + "", promoteCode.getBatchId() + "");
                    tips = awardPackage.getPackageGreet();
                } else if (limitedType.equals(LimitedType.TOTLE)) {
                    logger.info("可领取一次的礼包");
                    //int limit = jsonObject.getIntValue("limitedTimes");
                    //if (limit <= 0) {
                    //throw new ActivityException(ActivityException.GAME_ACTIVITY_LIMITID, "礼包码已领取完毕");
                    //}
                    //修改领取次数
                    //jsonObject.put("limitedTimes", limit - 1);
                    //codeBatch.setUsingRule(jsonObject.toString());
                    //promoteCodeBatchMapper.updateByPrimaryKeySelective(codeBatch);
                    //发送物品。
                    List<GoodsItem> goodsItems = JSON.parseArray(awardPackage.getItemsPackage(), GoodsItem.class);
                    GoodsItemPackage goodsItemPackage = new GoodsItemPackage();
                    goodsItemPackage.getGoodsItems().addAll(goodsItems);
                    goodsItemPackage.setPackageDesc(desc);
                    operationDispatchService.sendPackage(GameCode.getById(gameId), gameArea.getId(), title, desc
                            , goodsItemPackage, userId, 100000, roleIds, OperationType.MULTI_SEND_ITEM);

                    promoteCode.setDeliverDate(new Date());
                    promoteCode.setDrawRoleId(roleId + "");
                    promoteCode.setDrawUserId((int) userId);
                    promoteCode.setDrawSiteId(siteId);
                    promoteCode.setDrawDate(new Date());
                    promoteCode.setDrawGameAreaId(gameArea.getId());
                    promoteCode.setValidStatus(ValidStatus.INVALID);
                    promoteCode.setDrawStatus(ActStatus.ACTED);
                    promoteCode.setDeliverStatus(ActStatus.ACTED);
                    promoteCodeMapper.updateByPrimaryKeySelective(promoteCode);
                    tips = awardPackage.getPackageGreet();
                }
                //通过userid 查询 accountName
                List<UserAccount> accountEntity = accountService.getAccountsByUid(userId);
                String account = roleId + "";
                if (!org.springframework.util.CollectionUtils.isEmpty(accountEntity)) {
                    logger.error("乐盈统计查到改用户");
                    account = accountEntity.get(0).getAccountName();
                }
                lytRecoder(areaCode + "", account, roleId, code, channel, codeBatch.getBatchName());
                break;
            case TICKET:
                //领取礼券，用于在活动中换取实物，比如电影票

                break;
            default:
                throw new ActivityException(ActivityException.GAME_ACTIVITY_INVALID, "无效的类型");
        }

        return tips;
    }

    public void lytRecoder(String areaCode, String account, int roleId, String code, String channel, String batchName) {
        try {
            String appId = "20000009";
            String appKey = "McabJXdExQGdP7f5iFG3ad56xrapMitF";
            String checkUrl = "http://swglxh2.game.75wan.cn:30064/gift";
            List<NameValuePair> lytParams = new ArrayList<NameValuePair>();
            ;
            long time = new Date().getTime();
            String gifgName = batchName;
            String level = "1";

            String signStr = new StringBuilder(account).append("#").append(appId).append("#").append(code).append("#")
                    .append(gifgName).append("#").append(level).append("#").append(channel).append("#").append(channel)
                    .append("#").append(roleId).append("#").append(areaCode).append("#").append(time).append("#")
                    .append(appKey).toString();

            logger.info("recoder gift = " + signStr);
            String sign = CipherUtils.initMd5().encrypt(signStr);

            lytParams.add(new BasicNameValuePair("account", account));
            lytParams.add(new BasicNameValuePair("appId", appId));
            lytParams.add(new BasicNameValuePair("role_id", roleId + ""));
            lytParams.add(new BasicNameValuePair("gift_code", code));
            lytParams.add(new BasicNameValuePair("gift_name", gifgName));
            lytParams.add(new BasicNameValuePair("level", "1"));
            lytParams.add(new BasicNameValuePair("time", time + ""));
            lytParams.add(new BasicNameValuePair("server_id", areaCode + ""));
            lytParams.add(new BasicNameValuePair("partner", channel));
            lytParams.add(new BasicNameValuePair("promotion", channel));
            lytParams.add(new BasicNameValuePair("sign", sign));

            String resultLyt = null;
            try {
                resultLyt = HttpUtils.doPost(checkUrl, lytParams, "utf-8", "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            logger.info("resultLyt = " + resultLyt);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkUserActive(int gameId, int siteId, int userId) throws ServiceException {
        PromoteCodeExample promoteCodeExample = new PromoteCodeExample();
        promoteCodeExample.createCriteria().andDrawGameIdEqualTo(gameId).andDrawUserIdEqualTo(userId);
        List<PromoteCode> promoteCodes = promoteCodeMapper.selectByExample(promoteCodeExample);
        if (CollectionUtils.isEmpty(promoteCodes)) {
            throw new ActivityException(ActivityException.GAME_ACTIVITY_USERNOTACTIVE, "用户未激活");
        }
    }

    @Override
    public void getTicket(int gameId, int areaId, int siteId, int roleId) throws ServiceException {

        PromoteCodeBatch codeBatch = promoteCodeBatchMapper.selectByPrimaryKey(1000);
        //创建激活码
        long seqValue = tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_ACTIVITY_PROMOTE_CODE);
        String startCode = "";

        if (siteId == 34) {
            startCode = "TX";
        } else {
            startCode = "TK";
        }

        Date now = new Date();
        String codeTiket = startCode + GenCodeUtil.generateCode(String.format("%07d", seqValue));
        PromoteCode promoteCode = new PromoteCode();
        promoteCode.setCodeId(seqValue);
        promoteCode.setBatchId(1000);
        promoteCode.setCreateDate(now);
        promoteCode.setDeliverStatus(ActStatus.UNACT);
        promoteCode.setValidStatus(ValidStatus.VALID);
        promoteCode.setPromoteCode(codeTiket);
        promoteCode.setDrawSiteId(siteId);
        promoteCode.setDrawStatus(ActStatus.UNACT);
        promoteCode.setDrawGameId(gameId);
        promoteCode.setDrawRoleId(roleId + "");
        promoteCode.setDeliverDate(now);
        promoteCode.setDrawDate(now);
        promoteCodeMapper.insertSelective(promoteCode);
        List<String> roleIds = new ArrayList<>();
        roleIds.add(roleId + "");
        GoodsItemPackage goodsItemPackage = new GoodsItemPackage();
        goodsItemPackage.setPackageDesc(codeBatch.getAwardGreet() + "。以下是您的兑换码 : " + codeTiket);

        String title = "您的电影票兑换码";
        operationDispatchService.sendPackage(GameCode.getById(gameId), areaId, "系统", title
                , goodsItemPackage, 0, 100000, roleIds, OperationType.MULTI_SEND_ITEM);


    }

    @Override
    public List getPromoteCodeList() throws Exception {
        return cacheService.getList(PublicCachedKeyConstant.ACTIVITY_PROMOTE_CODE, List.class);
    }
}
