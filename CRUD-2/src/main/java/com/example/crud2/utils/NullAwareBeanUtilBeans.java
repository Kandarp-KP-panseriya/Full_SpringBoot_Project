package com.example.crud2.utils;
import com.example.crud2.exceptions.NotFoundExceptions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
@Slf4j
public class NullAwareBeanUtilBeans extends BeanUtilsBean
{
    @Override
    public void copyProperty(Object dest, String name, Object value)
            throws IllegalAccessException, InvocationTargetException {
        if (value == null) return;
        super.copyProperty(dest, name, value);
    }

    public void copyProperties(Object dest, Object orig, String debugMessage) {
        copyProperties(dest, orig, debugMessage, false, null);
    }

    public void copyProperties(Object dest, Object orig, String debugMessage, String errorMessage) {
        copyProperties(dest, orig, debugMessage, true, errorMessage);
    }

    private void copyProperties(Object dest, Object orig, String debugMessage, boolean throwError, String errorMessage) {
        try
        {
            super.copyProperties(dest, orig);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("ERROR (NULL AWARE BEAN) - {} : {}", debugMessage, e.getMessage(), e);
            if (throwError) {
                if (StringUtils.isEmpty(errorMessage)) {
                    errorMessage = e.getMessage();
                }
                throw new NotFoundExceptions(errorMessage);
            }
        }
    }
}