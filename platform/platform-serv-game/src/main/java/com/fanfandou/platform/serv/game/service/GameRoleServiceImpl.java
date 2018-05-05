package com.fanfandou.platform.serv.game.service;

import com.alibaba.fastjson.JSONObject;
import com.fanfandou.common.entity.resource.DicItem;
import com.fanfandou.common.entity.resource.GameCode;
import com.fanfandou.common.entity.result.Page;
import com.fanfandou.common.entity.result.PageResult;
import com.fanfandou.common.exception.ServiceException;
import com.fanfandou.common.sequence.TableSequenceGenerator;
import com.fanfandou.platform.api.billing.entity.BillingWalletBalance;
import com.fanfandou.platform.api.billing.entity.GoodsItem;
import com.fanfandou.platform.api.billing.entity.GoodsItemPackage;
import com.fanfandou.platform.api.billing.service.WalletSerivce;
import com.fanfandou.platform.api.constant.TableSequenceConstant;
import com.fanfandou.platform.api.game.entity.*;
import com.fanfandou.platform.api.game.service.GameAreaService;
import com.fanfandou.platform.api.game.service.GameRoleService;
import com.fanfandou.platform.api.game.service.OperationDispatchService;
import com.fanfandou.platform.api.user.entity.UserAccount;
import com.fanfandou.platform.api.user.service.AccountService;
import com.fanfandou.platform.serv.game.dao.GameRoleMapper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 游戏角色实现.
 * <p/>
 * Date: 2016-08-19 10:54.
 * author: Arvin.
 */
@Service("gameRoleService")
public class GameRoleServiceImpl implements GameRoleService {

    @Autowired
    private TableSequenceGenerator tableSequenceGenerator;

    @Autowired
    private GameRoleMapper gameRoleMapper;

    @Autowired
    private OperationDispatchService operationDispatchService;

    @Autowired
    private GameAreaService gameAreaService;

    @Autowired
    private WalletSerivce walletSerivce;
    @Autowired
    private AccountService accountService;


    @Override
    public GameRole createRole(GameRole gameRole, int siteId, GameCode gameCode) throws ServiceException {
        gameRole.setId(tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_ROLE));
        gameRole.setRoleId(tableSequenceGenerator.generate(TableSequenceConstant.PLATFORM_GAME_ROLE + gameCode.getCode()));
        gameRole.setCreateTime(new Date());
        gameRole.setLastLoginTime(new Date());
        gameRoleMapper.insert(gameRole);
        return gameRole;
    }

    @Override
    public void updateRole(GameRole gameRole) {
        gameRoleMapper.updateByPrimaryKeySelective(gameRole);
    }

    @Override
    public GameRole getRoleById(GameCode gameCode, long roleId) {
        GameRoleExample gameRoleExample = new GameRoleExample();
        gameRoleExample.createCriteria()
                .andRoleIdEqualTo(roleId);
        List<GameRole> result = gameRoleMapper.selectByExample(gameRoleExample);
        if (CollectionUtils.isNotEmpty(result)) {
            return result.get(0);
        }
        return null;
    }

    public GameRole getUserByRoleId(int gameId, int areaId, long roleId) {
        GameRoleExample gameRoleExample = new GameRoleExample();
        gameRoleExample.createCriteria().andRoleIdEqualTo(roleId);
        List<GameRole> result = gameRoleMapper.selectByExample(gameRoleExample);
        if (CollectionUtils.isNotEmpty(result)) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public GameRole getRoleByUserId(GameCode gameCode, int areaId, long userId) {
        GameRoleExample gameRoleExample = new GameRoleExample();
        gameRoleExample.createCriteria()
                .andUserIdEqualTo(userId)
                .andAreaIdEqualTo(areaId);
        List<GameRole> result = gameRoleMapper.selectByExample(gameRoleExample);
        if (CollectionUtils.isNotEmpty(result)) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public GameRole getRoleByUserId(GameCode gameCode, long userId) {
        GameRoleExample gameRoleExample = new GameRoleExample();
        gameRoleExample.createCriteria()
                .andUserIdEqualTo(userId);
        List<GameRole> result = gameRoleMapper.selectByExample(gameRoleExample);
        if (CollectionUtils.isNotEmpty(result)) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public GameRole getRoleByName(GameCode gameCode, int areaId, String roleName) {
        GameRoleExample gameRoleExample = new GameRoleExample();
        gameRoleExample.createCriteria()
                .andRoleNameEqualTo(roleName);
        List<GameRole> result = gameRoleMapper.selectByExample(gameRoleExample);
        if (CollectionUtils.isNotEmpty(result)) {
            return result.get(0);
        }
        return null;
    }


    @Override
    public List<GameRole> getRoleList(GameCode gameCode, long userId) {
        GameRoleExample gameRoleExample = new GameRoleExample();
        gameRoleExample.createCriteria().andGameIdEqualTo(gameCode.getId())
                .andUserIdEqualTo(userId);
        return gameRoleMapper.selectByExample(gameRoleExample);
    }

    @Override
    public GameRole getGameRole(int gameId, int areaId, int userId, int roleId, String roleName) {
        GameRole gameRole = new GameRole();
        if (roleId != -1) {
            gameRole = this.getRoleById(GameCode.getById(gameId), roleId);
        } else if (areaId != -1 && !StringUtils.isEmpty(roleName)) {
            gameRole = this.getRoleByName(GameCode.getById(gameId), areaId, roleName);
        } else if (areaId != -1 && userId != -1) {
            gameRole = this.getRoleByUserId(GameCode.getById(gameId), areaId, userId);
        }
        return gameRole;
    }

    @Override
    public List<GameRole> getGameRoleByArea(int gameId, int areaId) throws ServiceException {
        GameRoleExample gameRoleExample = new GameRoleExample();
        gameRoleExample.createCriteria().andAreaIdEqualTo(areaId);
        List<GameRole> gameRoleList = gameRoleMapper.selectByExample(gameRoleExample);
        return gameRoleList;
    }

    @Override
    public GameRoleInfoView getGameRoleView(GameCode gameCode, int areaId, int roleId, int infoType, int value)
            throws ServiceException {
        //先从游戏中获取角色信息
        String jsonAll = operationDispatchService.getRoleInfos(gameCode, areaId, roleId, infoType, value);
        JSONObject jsonAllObj = JSONObject.parseObject(jsonAll);
        JSONObject roleInfoObj = jsonAllObj.getJSONObject("extData");
        String roleName = roleInfoObj.getString("roleName");
        int gold = roleInfoObj.getIntValue("gold");
        int gem = roleInfoObj.getIntValue("gem");
        int level = roleInfoObj.getIntValue("level");
        int vipLevel = roleInfoObj.getIntValue("vipLevel");
        int fightPower = roleInfoObj.getIntValue("fightPower");
        int lastLoginTime = roleInfoObj.getIntValue("lastLoginTime");
        int lastLogoutTime = roleInfoObj.getIntValue("lastLogoutTime");
        int state = roleInfoObj.getIntValue("state");
        GameRoleInfoView gameRoleInfoView = new GameRoleInfoView();
        gameRoleInfoView.setRoleName(roleName);
        gameRoleInfoView.setGem(gem);
        gameRoleInfoView.setGold(gold);
        gameRoleInfoView.setLevel(level);
        gameRoleInfoView.setVipLevel(vipLevel);
        gameRoleInfoView.setFightPower(fightPower);
        gameRoleInfoView.setLastLoginTime(lastLoginTime);
        gameRoleInfoView.setLastLogoutTime(lastLogoutTime);
        gameRoleInfoView.setState(state);
        return gameRoleInfoView;
    }

    @Override
    public GameRole getRoleByRoleName(GameCode gameCode, String roleName) {
        GameRoleExample gameRoleExample = new GameRoleExample();
        gameRoleExample.createCriteria()
                .andGameIdEqualTo(gameCode.getId())
                .andRoleNameEqualTo(roleName);
        List<GameRole> result = gameRoleMapper.selectByExample(gameRoleExample);
        if (CollectionUtils.isNotEmpty(result)) {
            return result.get(0);
        }
        return null;
    }

    @Override
    public PageResult gameRolePage(Page page, Integer gameId, Integer siteId, Integer roleId, String roleName, Integer areaId) throws Exception {

        if (page.getOrder() == null || page.getOrder().equals("")) {
            page.setOrder("id");
        }
        if (page.getSort() == null || page.getSort().equals("")) {
            page.setSort("desc");
        }
        if (gameId != null && gameId == -1) {
            gameId = null;
        }

        int num1 = (page.getPageIndex() - 1) * page.getPageSize();

        Map paramMap = new HashMap();
        paramMap.put("roleName", roleName);
        paramMap.put("gameId", gameId);
        paramMap.put("str1", page.getOrder());
        paramMap.put("str2", page.getSort());
        paramMap.put("num1", num1);
        paramMap.put("num2", page.getPageSize());
        paramMap.put("siteId", siteId);
        paramMap.put("roleId", roleId);
        paramMap.put("areaId", areaId);
        List<GameRole> gameRoleList = this.gameRoleMapper.pageList(paramMap);
        for (int i = 0; i < gameRoleList.size(); i++) {
            GameArea gameArea = gameAreaService.getGameAreaById(gameRoleList.get(i).getAreaId());
            if (gameArea != null) {
                gameRoleList.get(i).setAreaName(gameArea.getAreaName());
            } else {
                gameRoleList.get(i).setAreaName("无区服信息");
            }

            UserAccount userAccount = accountService.getAccountsByUid(gameRoleList.get(i).getUserId()).get(0);
            if (userAccount != null) {
                gameRoleList.get(i).setAccountName(userAccount.getAccountName());
            } else {
                gameRoleList.get(i).setAccountName("无账号ID");
            }
        }
        Map map = new HashMap();
        map.put("roleName", roleName);
        map.put("gameId", gameId);
        map.put("siteId", siteId);
        map.put("roleId", roleId);
        map.put("areaId", areaId);
        int totalCount = this.gameRoleMapper.totalCount(map);

        page.setTotalCount(totalCount);
        PageResult<GameRole> pageResult = new PageResult<>();
        pageResult.setPage(page);
        pageResult.setRows(gameRoleList);
        return pageResult;
    }

    @Override
    public GameRole gameRoleById(int id) throws ServiceException {
        long idl = Long.valueOf(id);
        return this.gameRoleMapper.selectByPrimaryKey(idl);
    }

    @Override
    public void returnDoubleMoney(int areaId, long roleId) throws ServiceException {
        GameRole gameRole = getUserByRoleId(27, areaId, roleId);
        if (gameRole != null) {
            BillingWalletBalance balance = walletSerivce.queryBalance(gameRole.getUserId());
            List<String> roleIds = new ArrayList<>();
            roleIds.add(roleId + "");
            int totalMoney = balance.getAmount();
            if (totalMoney != 0) {
                GoodsItemPackage goodsItemPackage = new GoodsItemPackage();
                GoodsItem goodsItem = new GoodsItem();
                DicItem dicItem = new DicItem();
                dicItem.setValue("2");
                goodsItem.setItemId(0);
                goodsItem.setItemName("宝石");
                goodsItem.setValue(totalMoney * 2 * 10);
                goodsItem.setItemType(dicItem);
                goodsItemPackage.getGoodsItems().add(goodsItem);
                goodsItemPackage.setPackageDesc("感谢你封测期间的参与，以下是封测期间充值的返利，请查收！");
                operationDispatchService.sendPackage(GameCode.getById(27), areaId, "系统", "封测返利", goodsItemPackage, roleId, roleId, roleIds, OperationType.MULTI_SEND_ITEM);
                walletSerivce.withdrawWallet(gameRole.getUserId(), totalMoney);
            }
        }
    }
}
