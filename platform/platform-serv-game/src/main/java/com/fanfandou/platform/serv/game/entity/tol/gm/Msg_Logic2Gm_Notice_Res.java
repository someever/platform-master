// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from proto

package com.fanfandou.platform.serv.game.entity.tol.gm;

import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.Schema;

import javax.annotation.Generated;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Objects;


@Generated("java_bean")
public final class Msg_Logic2Gm_Notice_Res implements Externalizable, Message<Msg_Logic2Gm_Notice_Res>, Schema<Msg_Logic2Gm_Notice_Res>
{

    public static Schema<Msg_Logic2Gm_Notice_Res> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Msg_Logic2Gm_Notice_Res getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Msg_Logic2Gm_Notice_Res DEFAULT_INSTANCE = new Msg_Logic2Gm_Notice_Res();

    
    private String mSerialNumber;
    private Integer mRet;

    public Msg_Logic2Gm_Notice_Res()
    {

    }

    // getters and setters

    // mSerialNumber

    public String getMSerialNumber()
    {
        return mSerialNumber;
    }


    public void setMSerialNumber(String mSerialNumber)
    {
        this.mSerialNumber = mSerialNumber;
    }

    // mRet

    public Integer getMRet()
    {
        return mRet;
    }


    public void setMRet(Integer mRet)
    {
        this.mRet = mRet;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final Msg_Logic2Gm_Notice_Res that = (Msg_Logic2Gm_Notice_Res) obj;
        return
                Objects.equals(this.mSerialNumber, that.mSerialNumber) &&
                Objects.equals(this.mRet, that.mRet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mSerialNumber, mRet);
    }

    @Override
    public String toString() {
        return "Msg_Logic2Gm_Notice_Res{" +
                    "mSerialNumber=" + mSerialNumber +
                    ", mRet=" + mRet +
                '}';
    }
    // java serialization

    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    // message method

    public Schema<Msg_Logic2Gm_Notice_Res> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Msg_Logic2Gm_Notice_Res newMessage()
    {
        return new Msg_Logic2Gm_Notice_Res();
    }

    public Class<Msg_Logic2Gm_Notice_Res> typeClass()
    {
        return Msg_Logic2Gm_Notice_Res.class;
    }

    public String messageName()
    {
        return Msg_Logic2Gm_Notice_Res.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Msg_Logic2Gm_Notice_Res.class.getName();
    }

    public boolean isInitialized(Msg_Logic2Gm_Notice_Res message)
    {
        return true;
    }

    public void mergeFrom(Input input, Msg_Logic2Gm_Notice_Res message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    message.mSerialNumber = input.readString();
                    break;
                case 2:
                    message.mRet = input.readUInt32();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Msg_Logic2Gm_Notice_Res message) throws IOException
    {
        if(message.mSerialNumber != null)
            output.writeString(1, message.mSerialNumber, false);

        if(message.mRet != null)
            output.writeUInt32(2, message.mRet, false);
    }

    public String getFieldName(int number)
    {
        return Integer.toString(number);
    }

    public int getFieldNumber(String name)
    {
        return Integer.parseInt(name);
    }
    

}
