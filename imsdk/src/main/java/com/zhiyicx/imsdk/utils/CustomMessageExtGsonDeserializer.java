package com.zhiyicx.imsdk.utils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.zhiyicx.imsdk.entity.MessageExt;

import java.lang.reflect.Type;

/**
 * @Describe
 * @Author Jungle68
 * @Date 2017/11/10
 * @Contact master.jungle68@gmail.com
 */
public class CustomMessageExtGsonDeserializer implements JsonDeserializer<MessageExt> {
    @Override
    public MessageExt deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        MessageExt data;
        Gson gson = new Gson();
        if (json.isJsonObject()) {
            //类型正确
            data = gson.fromJson(gson.toJson(json.getAsJsonObject()), MessageExt.class);
        } else {
            //类型错误
            data = new MessageExt();
        }
        return data;
    }
}