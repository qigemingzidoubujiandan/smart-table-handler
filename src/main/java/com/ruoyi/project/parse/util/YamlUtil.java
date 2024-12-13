package com.ruoyi.project.parse.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

@Slf4j
public class YamlUtil {

    private volatile static Properties properties = new Properties();

    public static final String ACTIVE_DEV = "dev";
    public static final String ACTIVE_LOCAL = "localhost";
    public static final String ACTIVE_PROD = "prod";

    public static String get(String key) {
        if (properties.isEmpty()) {
            synchronized (YamlUtil.class) {
                if (properties.isEmpty()) {
                    initProps();
                }
            }
        }
        return properties.getProperty(key);
    }

    public static boolean isLocal() {
        String active = get("spring.profiles.active");
        return StrUtil.equals(active, ACTIVE_LOCAL);
    }

    private synchronized static void initProps() {
        // 主配置文件
        Properties props = convertProps("application.yml");
        String active = props.getProperty("spring.profiles.active");
        if (StrUtil.isNotBlank(active)) {
            // 子配置文件
            Properties activeProps = convertProps("application-" + active + ".yml");
            props.putAll(activeProps);
        }
        properties = props;
        log.info("yml load ：{}", properties);
    }

    private static Properties convertProps(String... ymlPath) {
        YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
        ClassPathResource[] classPathResources = Arrays.stream(ymlPath).map(ClassPathResource::new)
                .toArray(ClassPathResource[]::new);
        bean.setResources(classPathResources);
        Properties properties = bean.getObject();
        Objects.requireNonNull(properties);
        return properties;
    }
}
