package com.piles.util;

/**
 * Created by lgc on 18/1/7.
 */

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * <p>
 * 普通类调用Spring注解方式的Service层bean
 * </p>
 * @author zhengdong 2016年10月18日 下午3:54:29
 * @version V1.0
 */
public class SpringBeanFactoryUtils implements ApplicationContextAware {

    private static ApplicationContext appCtx;

    /**
     * 此方法可以把ApplicationContext对象inject到当前类中作为一个静态成员变量。
     *
     * @param applicationContext ApplicationContext 对象.
     * @throws BeansException
     * @author hzc
     */

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appCtx = applicationContext;
    }


    /**
     * 获取ApplicationContext
     *
     * @return
     * @author hzc
     */
    public  ApplicationContext getApplicationContext() {
        return appCtx;
    }

    /**
     * 这是一个便利的方法，帮助我们快速得到一个BEAN
     *
     * @param beanName bean的名字
     * @return 返回一个bean对象
     * @author hzc
     */
    public  Object getBean(String beanName) {
        return appCtx.getBean(beanName);
    }

    /**
     * 这是一个便利的方法，帮助我们快速得到一个BEAN
     *
     * @param c 服务bean
     * @return 返回一个bean对象
     * @author hzc
     */
    public  Object getBean(Class c) {
        return appCtx.getBean(c);
    }

    public String[] getBeanNames(Class c){
        return appCtx.getBeanNamesForType(c);
    }
}
