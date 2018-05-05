// Generated by http://code.google.com/p/protostuff/ ... DO NOT EDIT!
// Generated from proto

package com.fanfandou.platform.serv.game.entity.tol.base;

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
public final class Msg_Ping_Res implements Externalizable, Message<Msg_Ping_Res>, Schema<Msg_Ping_Res>
{

    public static Schema<Msg_Ping_Res> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Msg_Ping_Res getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Msg_Ping_Res DEFAULT_INSTANCE = new Msg_Ping_Res();

    
    private Long mMs;

    public Msg_Ping_Res()
    {

    }

    // getters and setters

    // mMs

    public Long getMMs()
    {
        return mMs;
    }


    public void setMMs(Long mMs)
    {
        this.mMs = mMs;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        final Msg_Ping_Res that = (Msg_Ping_Res) obj;
        return
                Objects.equals(this.mMs, that.mMs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mMs);
    }

    @Override
    public String toString() {
        return "Msg_Ping_Res{" +
                    "mMs=" + mMs +
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

    public Schema<Msg_Ping_Res> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Msg_Ping_Res newMessage()
    {
        return new Msg_Ping_Res();
    }

    public Class<Msg_Ping_Res> typeClass()
    {
        return Msg_Ping_Res.class;
    }

    public String messageName()
    {
        return Msg_Ping_Res.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Msg_Ping_Res.class.getName();
    }

    public boolean isInitialized(Msg_Ping_Res message)
    {
        return true;
    }

    public void mergeFrom(Input input, Msg_Ping_Res message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                case 1:
                    message.mMs = input.readUInt64();
                    break;
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Msg_Ping_Res message) throws IOException
    {
        if(message.mMs != null)
            output.writeUInt64(1, message.mMs, false);
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
