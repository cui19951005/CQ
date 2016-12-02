package com.cq.sdk.potential.validate;

import com.cq.sdk.utils.Validate;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/11/17.
 */
public class Validator {
    public static final Map<String,String> validate(Object o){
        HashMap<String,String> map=new HashMap<>();
        String fieldName=null;
        try {
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Required required = field.getAnnotation(Required.class);
                Email email = field.getAnnotation(Email.class);
                Size size = field.getAnnotation(Size.class);
                Pattern pattern=field.getAnnotation(Pattern.class);
                DateTime dateTime=field.getAnnotation(DateTime.class);
                Equals equals=field.getAnnotation(Equals.class);
                Object value = field.get(o);
                if (required != null && (value==null || (value instanceof String ? ((String)value).length()==0:false))) {
                    map.put(field.getName(),required.message());
                }
                if(value!=null) {
                    if (email != null && !Validate.email(value.toString())) {
                        map.put(field.getName(), email.message());
                    }
                    if (size != null && (value.toString().length() < size.min() || value.toString().length() >= size.max())) {
                        map.put(field.getName(), size.message());
                    }
                    if (pattern != null && !Validate.matcher(pattern.value(), value.toString())) {
                        map.put(field.getName(), pattern.message());
                    }
                    if (dateTime != null && !Validate.date(dateTime.value(), value.toString())) {
                        map.put(field.getName(), dateTime.message());
                    }
                    if (equals != null && equals.value() != "") {
                        fieldName = equals.value();
                        Field f = o.getClass().getDeclaredField(equals.value());
                        f.setAccessible(true);
                        Object fVal = f.get(o);
                        if (fVal != value && !fVal.equals(value)) {
                            map.put(field.getName(), equals.message());
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            map.put(fieldName,"No Such Field Error");
            e.printStackTrace();
        }
        return map;
    }
}
