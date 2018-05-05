package com.fanfandou.platform.api.game.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameRoleDisposeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GameRoleDisposeExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNull() {
            addCriterion("role_id is null");
            return (Criteria) this;
        }

        public Criteria andRoleIdIsNotNull() {
            addCriterion("role_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoleIdEqualTo(Long value) {
            addCriterion("role_id =", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotEqualTo(Long value) {
            addCriterion("role_id <>", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThan(Long value) {
            addCriterion("role_id >", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdGreaterThanOrEqualTo(Long value) {
            addCriterion("role_id >=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThan(Long value) {
            addCriterion("role_id <", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdLessThanOrEqualTo(Long value) {
            addCriterion("role_id <=", value, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdIn(List<Long> values) {
            addCriterion("role_id in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotIn(List<Long> values) {
            addCriterion("role_id not in", values, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdBetween(Long value1, Long value2) {
            addCriterion("role_id between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleIdNotBetween(Long value1, Long value2) {
            addCriterion("role_id not between", value1, value2, "roleId");
            return (Criteria) this;
        }

        public Criteria andRoleNameIsNull() {
            addCriterion("role_name is null");
            return (Criteria) this;
        }

        public Criteria andRoleNameIsNotNull() {
            addCriterion("role_name is not null");
            return (Criteria) this;
        }

        public Criteria andRoleNameEqualTo(String value) {
            addCriterion("role_name =", value, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameNotEqualTo(String value) {
            addCriterion("role_name <>", value, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameGreaterThan(String value) {
            addCriterion("role_name >", value, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameGreaterThanOrEqualTo(String value) {
            addCriterion("role_name >=", value, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameLessThan(String value) {
            addCriterion("role_name <", value, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameLessThanOrEqualTo(String value) {
            addCriterion("role_name <=", value, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameLike(String value) {
            addCriterion("role_name like", value, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameNotLike(String value) {
            addCriterion("role_name not like", value, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameIn(List<String> values) {
            addCriterion("role_name in", values, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameNotIn(List<String> values) {
            addCriterion("role_name not in", values, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameBetween(String value1, String value2) {
            addCriterion("role_name between", value1, value2, "roleName");
            return (Criteria) this;
        }

        public Criteria andRoleNameNotBetween(String value1, String value2) {
            addCriterion("role_name not between", value1, value2, "roleName");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("user_id is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("user_id is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("user_id =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("user_id <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("user_id >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("user_id >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("user_id <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("user_id <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("user_id in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("user_id not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("user_id between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("user_id not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andGameIdIsNull() {
            addCriterion("game_id is null");
            return (Criteria) this;
        }

        public Criteria andGameIdIsNotNull() {
            addCriterion("game_id is not null");
            return (Criteria) this;
        }

        public Criteria andGameIdEqualTo(Integer value) {
            addCriterion("game_id =", value, "gameId");
            return (Criteria) this;
        }

        public Criteria andGameIdNotEqualTo(Integer value) {
            addCriterion("game_id <>", value, "gameId");
            return (Criteria) this;
        }

        public Criteria andGameIdGreaterThan(Integer value) {
            addCriterion("game_id >", value, "gameId");
            return (Criteria) this;
        }

        public Criteria andGameIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("game_id >=", value, "gameId");
            return (Criteria) this;
        }

        public Criteria andGameIdLessThan(Integer value) {
            addCriterion("game_id <", value, "gameId");
            return (Criteria) this;
        }

        public Criteria andGameIdLessThanOrEqualTo(Integer value) {
            addCriterion("game_id <=", value, "gameId");
            return (Criteria) this;
        }

        public Criteria andGameIdIn(List<Integer> values) {
            addCriterion("game_id in", values, "gameId");
            return (Criteria) this;
        }

        public Criteria andGameIdNotIn(List<Integer> values) {
            addCriterion("game_id not in", values, "gameId");
            return (Criteria) this;
        }

        public Criteria andGameIdBetween(Integer value1, Integer value2) {
            addCriterion("game_id between", value1, value2, "gameId");
            return (Criteria) this;
        }

        public Criteria andGameIdNotBetween(Integer value1, Integer value2) {
            addCriterion("game_id not between", value1, value2, "gameId");
            return (Criteria) this;
        }

        public Criteria andSiteIdIsNull() {
            addCriterion("site_id is null");
            return (Criteria) this;
        }

        public Criteria andSiteIdIsNotNull() {
            addCriterion("site_id is not null");
            return (Criteria) this;
        }

        public Criteria andSiteIdEqualTo(Integer value) {
            addCriterion("site_id =", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotEqualTo(Integer value) {
            addCriterion("site_id <>", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdGreaterThan(Integer value) {
            addCriterion("site_id >", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("site_id >=", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLessThan(Integer value) {
            addCriterion("site_id <", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdLessThanOrEqualTo(Integer value) {
            addCriterion("site_id <=", value, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdIn(List<Integer> values) {
            addCriterion("site_id in", values, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotIn(List<Integer> values) {
            addCriterion("site_id not in", values, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdBetween(Integer value1, Integer value2) {
            addCriterion("site_id between", value1, value2, "siteId");
            return (Criteria) this;
        }

        public Criteria andSiteIdNotBetween(Integer value1, Integer value2) {
            addCriterion("site_id not between", value1, value2, "siteId");
            return (Criteria) this;
        }

        public Criteria andAreaIdIsNull() {
            addCriterion("area_id is null");
            return (Criteria) this;
        }

        public Criteria andAreaIdIsNotNull() {
            addCriterion("area_id is not null");
            return (Criteria) this;
        }

        public Criteria andAreaIdEqualTo(Integer value) {
            addCriterion("area_id =", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdNotEqualTo(Integer value) {
            addCriterion("area_id <>", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdGreaterThan(Integer value) {
            addCriterion("area_id >", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("area_id >=", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdLessThan(Integer value) {
            addCriterion("area_id <", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdLessThanOrEqualTo(Integer value) {
            addCriterion("area_id <=", value, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdIn(List<Integer> values) {
            addCriterion("area_id in", values, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdNotIn(List<Integer> values) {
            addCriterion("area_id not in", values, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdBetween(Integer value1, Integer value2) {
            addCriterion("area_id between", value1, value2, "areaId");
            return (Criteria) this;
        }

        public Criteria andAreaIdNotBetween(Integer value1, Integer value2) {
            addCriterion("area_id not between", value1, value2, "areaId");
            return (Criteria) this;
        }

        public Criteria andClosureReasonIsNull() {
            addCriterion("closure_reason is null");
            return (Criteria) this;
        }

        public Criteria andClosureReasonIsNotNull() {
            addCriterion("closure_reason is not null");
            return (Criteria) this;
        }

        public Criteria andClosureReasonEqualTo(String value) {
            addCriterion("closure_reason =", value, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonNotEqualTo(String value) {
            addCriterion("closure_reason <>", value, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonGreaterThan(String value) {
            addCriterion("closure_reason >", value, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonGreaterThanOrEqualTo(String value) {
            addCriterion("closure_reason >=", value, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonLessThan(String value) {
            addCriterion("closure_reason <", value, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonLessThanOrEqualTo(String value) {
            addCriterion("closure_reason <=", value, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonLike(String value) {
            addCriterion("closure_reason like", value, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonNotLike(String value) {
            addCriterion("closure_reason not like", value, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonIn(List<String> values) {
            addCriterion("closure_reason in", values, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonNotIn(List<String> values) {
            addCriterion("closure_reason not in", values, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonBetween(String value1, String value2) {
            addCriterion("closure_reason between", value1, value2, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureReasonNotBetween(String value1, String value2) {
            addCriterion("closure_reason not between", value1, value2, "closureReason");
            return (Criteria) this;
        }

        public Criteria andClosureTimeIsNull() {
            addCriterion("closure_time is null");
            return (Criteria) this;
        }

        public Criteria andClosureTimeIsNotNull() {
            addCriterion("closure_time is not null");
            return (Criteria) this;
        }

        public Criteria andClosureTimeEqualTo(Date value) {
            addCriterion("closure_time =", value, "closureTime");
            return (Criteria) this;
        }

        public Criteria andClosureTimeNotEqualTo(Date value) {
            addCriterion("closure_time <>", value, "closureTime");
            return (Criteria) this;
        }

        public Criteria andClosureTimeGreaterThan(Date value) {
            addCriterion("closure_time >", value, "closureTime");
            return (Criteria) this;
        }

        public Criteria andClosureTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("closure_time >=", value, "closureTime");
            return (Criteria) this;
        }

        public Criteria andClosureTimeLessThan(Date value) {
            addCriterion("closure_time <", value, "closureTime");
            return (Criteria) this;
        }

        public Criteria andClosureTimeLessThanOrEqualTo(Date value) {
            addCriterion("closure_time <=", value, "closureTime");
            return (Criteria) this;
        }

        public Criteria andClosureTimeIn(List<Date> values) {
            addCriterion("closure_time in", values, "closureTime");
            return (Criteria) this;
        }

        public Criteria andClosureTimeNotIn(List<Date> values) {
            addCriterion("closure_time not in", values, "closureTime");
            return (Criteria) this;
        }

        public Criteria andClosureTimeBetween(Date value1, Date value2) {
            addCriterion("closure_time between", value1, value2, "closureTime");
            return (Criteria) this;
        }

        public Criteria andClosureTimeNotBetween(Date value1, Date value2) {
            addCriterion("closure_time not between", value1, value2, "closureTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andRoleStatusIsNull() {
            addCriterion("role_status is null");
            return (Criteria) this;
        }

        public Criteria andRoleStatusIsNotNull() {
            addCriterion("role_status is not null");
            return (Criteria) this;
        }

        public Criteria andRoleStatusEqualTo(Integer value) {
            addCriterion("role_status =", value, "roleStatus");
            return (Criteria) this;
        }

        public Criteria andRoleStatusNotEqualTo(Integer value) {
            addCriterion("role_status <>", value, "roleStatus");
            return (Criteria) this;
        }

        public Criteria andRoleStatusGreaterThan(Integer value) {
            addCriterion("role_status >", value, "roleStatus");
            return (Criteria) this;
        }

        public Criteria andRoleStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("role_status >=", value, "roleStatus");
            return (Criteria) this;
        }

        public Criteria andRoleStatusLessThan(Integer value) {
            addCriterion("role_status <", value, "roleStatus");
            return (Criteria) this;
        }

        public Criteria andRoleStatusLessThanOrEqualTo(Integer value) {
            addCriterion("role_status <=", value, "roleStatus");
            return (Criteria) this;
        }

        public Criteria andRoleStatusIn(List<Integer> values) {
            addCriterion("role_status in", values, "roleStatus");
            return (Criteria) this;
        }

        public Criteria andRoleStatusNotIn(List<Integer> values) {
            addCriterion("role_status not in", values, "roleStatus");
            return (Criteria) this;
        }

        public Criteria andRoleStatusBetween(Integer value1, Integer value2) {
            addCriterion("role_status between", value1, value2, "roleStatus");
            return (Criteria) this;
        }

        public Criteria andRoleStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("role_status not between", value1, value2, "roleStatus");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonIsNull() {
            addCriterion("disableSendMsg_reason is null");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonIsNotNull() {
            addCriterion("disableSendMsg_reason is not null");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonEqualTo(String value) {
            addCriterion("disableSendMsg_reason =", value, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonNotEqualTo(String value) {
            addCriterion("disableSendMsg_reason <>", value, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonGreaterThan(String value) {
            addCriterion("disableSendMsg_reason >", value, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonGreaterThanOrEqualTo(String value) {
            addCriterion("disableSendMsg_reason >=", value, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonLessThan(String value) {
            addCriterion("disableSendMsg_reason <", value, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonLessThanOrEqualTo(String value) {
            addCriterion("disableSendMsg_reason <=", value, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonLike(String value) {
            addCriterion("disableSendMsg_reason like", value, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonNotLike(String value) {
            addCriterion("disableSendMsg_reason not like", value, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonIn(List<String> values) {
            addCriterion("disableSendMsg_reason in", values, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonNotIn(List<String> values) {
            addCriterion("disableSendMsg_reason not in", values, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonBetween(String value1, String value2) {
            addCriterion("disableSendMsg_reason between", value1, value2, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgReasonNotBetween(String value1, String value2) {
            addCriterion("disableSendMsg_reason not between", value1, value2, "disablesendmsgReason");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeIsNull() {
            addCriterion("disableSendMsg_time is null");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeIsNotNull() {
            addCriterion("disableSendMsg_time is not null");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeEqualTo(Date value) {
            addCriterion("disableSendMsg_time =", value, "disablesendmsgTime");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeNotEqualTo(Date value) {
            addCriterion("disableSendMsg_time <>", value, "disablesendmsgTime");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeGreaterThan(Date value) {
            addCriterion("disableSendMsg_time >", value, "disablesendmsgTime");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("disableSendMsg_time >=", value, "disablesendmsgTime");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeLessThan(Date value) {
            addCriterion("disableSendMsg_time <", value, "disablesendmsgTime");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeLessThanOrEqualTo(Date value) {
            addCriterion("disableSendMsg_time <=", value, "disablesendmsgTime");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeIn(List<Date> values) {
            addCriterion("disableSendMsg_time in", values, "disablesendmsgTime");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeNotIn(List<Date> values) {
            addCriterion("disableSendMsg_time not in", values, "disablesendmsgTime");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeBetween(Date value1, Date value2) {
            addCriterion("disableSendMsg_time between", value1, value2, "disablesendmsgTime");
            return (Criteria) this;
        }

        public Criteria andDisablesendmsgTimeNotBetween(Date value1, Date value2) {
            addCriterion("disableSendMsg_time not between", value1, value2, "disablesendmsgTime");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}