package com.fanfandou.common.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by wudi.
 * Descreption:自定义文件日志.
 * Date:2017/3/14
 */
public class DefindLogger {

    private final static int priority_1 = 40010;
    private final static int priority_2 = 40020;
    private final static int priority_3 = 40030;
    private final static int priority_4 = 40040;
    private final static int priority_5 = 40050;
    private final static int priority_6 = 40060;
    private final static int priority_7 = 40070;
    private final static int priority_8 = 40080;
    private final static int priority_9 = 40090;
    private final static int priority_10 = 40100;
    private final static int priority_11 = 40110;
    private final static int priority_12 = 40120;
    private final Logger logger;

    private static final String FQCN;

    // 以下为自定义的日志级别， 自定义等级 40100 > 40000(error)>()
    public static final Level ACTIVATION = new DefindLevel(priority_1, "activation", SyslogAppender.LOG_LOCAL0);//激活设备
    public static final Level UPDATE = new DefindLevel(priority_2, "update", SyslogAppender.LOG_LOCAL0);//补丁更新
    public static final Level LOGINUI = new DefindLevel(priority_3, "loginUI", SyslogAppender.LOG_LOCAL0);//到达登录页面
    public static final Level INDENTIFICATION = new DefindLevel(priority_4, "identification", SyslogAppender.LOG_LOCAL0);//账号验证
    public static final Level ENTERGAME = new DefindLevel(priority_5, "enterGame", SyslogAppender.LOG_LOCAL0);//加载游戏
    public static final Level GMPAYMENT = new DefindLevel(priority_6, "gmpay", SyslogAppender.LOG_LOCAL0);//废了
    public static final Level LOGOUTROLE = new DefindLevel(priority_7, "logoutRole", SyslogAppender.LOG_LOCAL0);//废了
    public static final Level CGRECORD = new DefindLevel(priority_8, "CGActoin", SyslogAppender.LOG_LOCAL0);//CG记录
    public static final Level PREPAID = new DefindLevel(priority_9, "Prepaid", SyslogAppender.LOG_LOCAL0);//订单记录
    public static final Level PREORDER = new DefindLevel(priority_10, "preOrder", SyslogAppender.LOG_LOCAL0);//废了
    public static final Level ORDERING = new DefindLevel(priority_11, "ordering", SyslogAppender.LOG_LOCAL0);//废了

    static {
        FQCN = DefindLevel.class.getName();
    }

    private DefindLogger(Class<?> clazz) {
        logger = org.apache.log4j.Logger.getLogger(clazz);
    }

    private DefindLogger() {
        logger = org.apache.log4j.Logger.getRootLogger();
    }

    public static DefindLogger getLogger(Class<?> clazz) {
        return new DefindLogger(clazz);
    }

    public static DefindLogger getRootLogger() {
        return new DefindLogger();
    }

    /**
     * 激活设备接口.
     */
    public void  activation(Object message) {
        forcedLog(logger, ACTIVATION, message);
    }

    public void update(Object mesaage) {
        forcedLog(logger, UPDATE, mesaage);
    }

    public void loginUi(Object mesaage) {
        forcedLog(logger, LOGINUI, mesaage);
    }

    public void identification(Object mesaage) {
        forcedLog(logger, INDENTIFICATION, mesaage);
    }

    public void enterGame(Object mesaage) {
        forcedLog(logger, ENTERGAME, mesaage);
    }

    public void cgRecord(Object mesaage) {
        forcedLog(logger, CGRECORD, mesaage);
    }

    public void prepaid(Object mesaage) {
        forcedLog(logger, PREPAID, mesaage);
    }

    public void perOrder(Object mesaage) {
        forcedLog(logger, PREORDER, mesaage);
    }

    public void ordering(Object mesaage) {
        forcedLog(logger, ORDERING, mesaage);
    }


    public void gmPay(Object mesaage) {
        forcedLog(logger, GMPAYMENT, mesaage);
    }

    public void logoutRole(Object mesaage) {
        forcedLog(logger, LOGOUTROLE, mesaage);
    }

    private static void forcedLog(org.apache.log4j.Logger logger, Level level, Object message) {
        logger.callAppenders(new LoggingEvent(FQCN, logger, level, message, null));
    }

    private static void forcedLog(org.apache.log4j.Logger logger, Level level, Object message, Throwable t) {
        logger.callAppenders(new LoggingEvent(FQCN, logger, level, message, t));
    }
}
