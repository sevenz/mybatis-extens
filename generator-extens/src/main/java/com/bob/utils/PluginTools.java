package com.bob.utils;

import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.PluginConfiguration;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangxiang on 17/6/30.
 */
public class PluginTools {

    /**
     * 检查插件依赖
     *
     * @param context 上下文
     * @param plugin  插件
     * @return
     */
    public static boolean checkDependencyPlugin(Context context, Class plugin) {
        return getPluginIndex(context, plugin) >= 0;
    }

    /**
     * 获取插件所在位置
     *
     * @param context 上下文
     * @param plugin  插件
     * @return -1:未找到
     */
    public static int getPluginIndex(Context context, Class plugin) {
        List<PluginConfiguration> list = getConfigPlugins(context);
        // 检查是否配置了ModelColumnPlugin插件
        for (int i = 0; i < list.size(); i++) {
            PluginConfiguration config = list.get(i);
            if (plugin.getName().equals(config.getConfigurationType())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取插件列表
     *
     * @param ctx 上下文
     * @return
     */
    public static List<PluginConfiguration> getConfigPlugins(Context ctx) {
        try {
            // 利用反射获取pluginConfigurations属性
            Field field = Context.class.getDeclaredField("pluginConfigurations");
            field.setAccessible(true);
            Object obj = field.get(ctx);
            if (obj instanceof List<?>) {
                return (List<PluginConfiguration>) field.get(ctx);
            }
        } catch (Exception e) {
        }
        return new ArrayList<>();
    }

    /**
     * 获取插件配置
     *
     * @param context 上下文
     * @param plugin  插件
     * @return
     */
    public static PluginConfiguration getPluginConfiguration(Context context, Class plugin) {
        int index = getPluginIndex(context, plugin);
        if (index > -1) {
            return getConfigPlugins(context).get(index);
        }
        return null;
    }

    /**
     * 插件位置需要配置在某些插件后面
     *
     * @param context
     * @param plugin
     * @param warnings
     * @param plugins
     * @return
     */
    public static boolean shouldAfterPlugins(Context context, Class plugin, List<String> warnings, Class... plugins) {
        int index = getPluginIndex(context, plugin);
        if (plugins != null) {
            for (Class cls : plugins) {
                int index1 = getPluginIndex(context, cls);
                if (index1 != -1 && index1 >= index) {
                    warnings.add("bob:插件" + plugin.getSimpleName() + "插件建议配置在插件" + cls.getSimpleName() + "后面，否则某些功能可能得不到增强！");
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}