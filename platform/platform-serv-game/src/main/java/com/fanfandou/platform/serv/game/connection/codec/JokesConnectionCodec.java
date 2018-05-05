package com.fanfandou.platform.serv.game.connection.codec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fanfandou.common.base.BaseLogger;
import com.fanfandou.platform.api.game.entity.Message;
import com.fanfandou.platform.serv.game.entity.jokes.JokesCommonType;
import com.fanfandou.platform.serv.game.entity.jokes.base.JokesParams;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by wudi.
 * Descreption:十冷通讯协议.
 * Date:2017/3/8
 */
public class JokesConnectionCodec extends BaseLogger implements ConnectionCodec<Message> {

    /**
     * encode包头长度.
     */
    private static final short REQ_PACKAGE_HEAD_SIZE = 12;

    private static final int RES_PACKAGE_HEAD_SIZE = 14;

    private static final int MSG_ID = 3000;

    private static final int GATE_ID = 0;

    private static final int maxLenBytes = 2;

    private static final int maxTypeLen = 2;




    /**
     * 大端转小端.
     */
    private static final short swap16(short word) {
        return (short) ((word & 0xFF) << 8 | 0xFF & (word >> 8));
    }

    /**
     * 大端转小端.
     */
    private static final int swap32(int i) {
        return (i & 0xFF) << 24
                | (0xFF & i >> 8) << 16
                | (0xFF & i >> 16) << 8
                | (0xFF & i >> 24);
    }

    @Override
    public int getLenthFieldOffset() {
        return 0;
    }

    @Override
    public int getlenthFieldLenth() {
        return 2;
    }

    @Override
    public int getlenthAdjustment() {
        return -2;
    }

    @Override
    public int getinitialBytesToTrip() {
        return 0;
    }

    @Override
    public byte[] encode(Message msg) {
        String jsonBody = JSON.toJSONString(msg.getMsgBody());
        logger.info("encode : " + jsonBody);
        byte[] msgBodyBytes = jsonBody.getBytes();
        short msgLenth = (short) (REQ_PACKAGE_HEAD_SIZE + (short) msgBodyBytes.length);
        logger.info("msgLength:" + msgLenth);
        byte[] lenBytes = Shorts.toByteArray(swap16(msgLenth));
        byte[] msgBytes = Shorts.toByteArray(swap16((short) MSG_ID));
        byte[] gateBytes = Ints.toByteArray(swap32(GATE_ID));
        byte[] bodyLenthBytes = Ints.toByteArray(swap32(msgBodyBytes.length));

        return Bytes.concat(lenBytes, msgBytes, gateBytes, bodyLenthBytes, msgBodyBytes);
    }

    @Override
    public Message decode(byte[] datas) {
        //先取包头
        byte[] maxLengBytes = Arrays.copyOfRange(datas, 0, maxLenBytes);//包长度 2个字节
        logger.info("decode maxLengBytes = " + maxLengBytes.length);
        byte[] msgIdBytes = Arrays.copyOfRange(datas, maxLenBytes, maxLenBytes + maxTypeLen);//协议编号 两个字节
        //byte[] gateBytes = Arrays.copyOfRange(datas, maxLenBytes + maxTypeLen, maxLenBytes + maxTypeLen + maxGateLen);
        // gete内容，虽然没啥用 但还是占用了四个字节
        int maxLen = swap16(Shorts.fromByteArray(maxLengBytes));
        logger.info("datas lengh = " + datas.length + ", headlenth : " + maxLen);
        byte[] msgBodyBytes = Arrays.copyOfRange(datas, RES_PACKAGE_HEAD_SIZE, maxLen);
        int typeId = swap16(Shorts.fromByteArray(msgIdBytes));
        //根据type直接转换bean

        if (typeId == 3001 || typeId == 3004) {
            String msgBodyContent = "";
            try {
                msgBodyContent = new String(msgBodyBytes, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            msgBodyContent = msgBodyContent.replace("\u0000", "");
            logger.info("decode msgBodyContent = " + msgBodyContent);
            JSONObject jsonObject = JSON.parseObject(msgBodyContent);
            int roleId = jsonObject.getIntValue("srcRoleId");
            if (typeId == 3004) {
                //游戏主动推送消息
                int type = jsonObject.getIntValue("type");
                if (type == 1) {
                    JokesCommonType msgType = JokesCommonType.valueOf(3109);
                    return new Message<>(roleId + "", jsonObject, msgType.getMsgType(), 3109);
                } else if (type == 2) {
                    JokesCommonType msgType = JokesCommonType.valueOf(3112);
                    String transId = jsonObject.getString("transId");
                    return new Message<>(transId, jsonObject, msgType.getMsgType(), msgType.getId());
                }

            }
            String transId = "2";
            int actionId = jsonObject.getIntValue("action");
            if (actionId != JokesParams.ACTION_BROADCAST_SERVERID) {
                transId = jsonObject.getString("transId");
            }
            JokesCommonType msgType = JokesCommonType.valueOf(3100 + actionId);

            if (msgType != null) {

                return new Message<>(transId, jsonObject, msgType.getMsgType(), 3100 + actionId);
            }
        }
        return null;
    }
}
