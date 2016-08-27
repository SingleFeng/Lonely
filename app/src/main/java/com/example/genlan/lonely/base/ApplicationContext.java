package com.example.genlan.lonely.base;

import com.example.genlan.lonely.config.ConfigSettings;

/**
 * Created by GenLan on 2016/8/26.
 */
public interface ApplicationContext {
    /**
     * 获取参数配置对象
     * @return 参数配置对象
     */
    public ConfigSettings getConfigSettings();
}
