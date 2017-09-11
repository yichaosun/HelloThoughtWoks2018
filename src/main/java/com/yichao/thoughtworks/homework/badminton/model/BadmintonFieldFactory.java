package com.yichao.thoughtworks.homework.badminton.model;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BadmintonFieldFactory {
    private static Map<String, BadmintonField> badmintonFields = new HashMap<String, BadmintonField>();

    static {
        try {
            String text = IOUtils.toString(Thread.currentThread().getClass().getResourceAsStream("/properties/badmintonProp.json"), "utf8");
            List<BadmintonField> fields = JSON.parseArray(text, BadmintonField.class);
            for (BadmintonField field :
                    fields) {
                badmintonFields.put(field.getName(), field);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BadmintonField getBadmintonField(String name) {
        return badmintonFields.get(name);
    }

    public static Set<Map.Entry<String, BadmintonField>> getAllFields() {
        return badmintonFields.entrySet();
    }

}
