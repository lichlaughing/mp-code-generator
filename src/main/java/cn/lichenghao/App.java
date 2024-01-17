package com.seatone;

import cn.hutool.core.io.FileUtil;
import cn.lichenghao.WordToCamelCaseUtil;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Property;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class App {
    /**
     * 数据库相关
     */
    private static final String DB_URL = "jdbc:mysql://192.168.1.18:3306/tdimp?&useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PWD = "root123";
    /**
     * 代码存放相关
     */
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String FILE_PATH = "/ge2/src/main/java";
    private static final String MAPPER_FILE_PATH = "/ge2/src/main/resources";
    private static final String PACKAGE_PATH = "cn.lichenghao";
    private static final String TABLES = "standard_color";

    // 入口
    public static void main(String[] args) {
        FileUtil.del(new File(PROJECT_PATH + FILE_PATH));
        FastAutoGenerator.create(getDataSourceConfig())
                .globalConfig(getGlobalConfig())
                .packageConfig(getPackageConfig())
                .templateConfig(getTemplateConfig())
                .injectionConfig(getInjectionConfig())
                .strategyConfig(getStrategyConfig())
                .execute();
    }

    // 数据库配置
    public static DataSourceConfig.Builder getDataSourceConfig() {
        return new DataSourceConfig.Builder(DB_URL, DB_USER, DB_PWD).dbQuery(new MySqlQuery());
    }

    // 全局配置
    public static Consumer<GlobalConfig.Builder> getGlobalConfig() {
        return builder -> {
            builder.outputDir(PROJECT_PATH + FILE_PATH) // 生成的输出路径
                    .author("chenghao.li")              // 生成的作者名字
                    .enableSwagger()                  // 开启swagger，需要添加swagger依赖并配置
                    // .dateType(DateType.TIME_PACK)    // 时间策略，DateType.ONLY_DATE 默认值: DateType.TIME_PACK
                    // .commentDate("yyyy-MM-dd")       // 注释日期，默认值: yyyy-MM-dd
                    .disableOpenDir();                  // 禁止打开输出目录，默认true
        };
    }

    // 包配置
    public static Consumer<PackageConfig.Builder> getPackageConfig() {
        return builder -> {
            builder.parent(PACKAGE_PATH)            // 父包名,下面包名写相对路径即可。如果不提供父包名，那么下面的包名要写完整。
                    .entity("repo.po")              // 实体类包名
                    .mapper("repo.dao")             // mapper层包名
                    .xml("mapper")                  // 数据访问层xml包名
                    .service("service")             // service层包名
                    .serviceImpl("service.impl")    // service实现类包名
                    .controller("controller")       // 控制层包名
                    .other("other")                 // 输出自定义文件时的包名
                    .pathInfo(Collections.singletonMap(OutputFile.xml, PROJECT_PATH + MAPPER_FILE_PATH + "/mapper")); // 自定义文件路径
        };
    }

    // 模版配置
    public static Consumer<TemplateConfig.Builder> getTemplateConfig() {
        return builder -> {
            builder.disable(TemplateType.SERVICE, TemplateType.SERVICEIMPL, TemplateType.CONTROLLER)
                    // 使用自定义模板
                    .service("/template/ge/service/service.java.vm")
                    .serviceImpl("/template/ge/service/serviceImpl.java.vm")
                    .controller("/template/ge/controller/controller.java.vm");
        };
    }

    // 自定义文件配置
    public static Consumer<InjectionConfig.Builder> getInjectionConfig() {
        Map<String, String> customFile = new HashMap<>();
        // 自定义文件，key:文件名称，value：模板路径
        customFile.put("convert/" + WordToCamelCaseUtil.convertWordsToCamelCase(TABLES) + "Convert.java", "/template/ge/convert/convert.java.vm");
        customFile.put("test/" + WordToCamelCaseUtil.convertWordsToCamelCase(TABLES) + "ServiceTest.java", "/template/ge/service/serviceTest.java.vm");
        customFile.put("test/" + WordToCamelCaseUtil.convertWordsToCamelCase(TABLES) + "ControllerTest.java", "/template/ge/controller/controllerTest.java.vm");
        customFile.put("vo/" + WordToCamelCaseUtil.convertWordsToCamelCase(TABLES) + "VO.java", "/template/ge/vo/VO.java.vm");
        customFile.put("vo/Create" + WordToCamelCaseUtil.convertWordsToCamelCase(TABLES) + "VO.java", "/template/ge/vo/CreateVO.java.vm");
        customFile.put("vo/Search" + WordToCamelCaseUtil.convertWordsToCamelCase(TABLES) + "VO.java", "/template/ge/vo/SearchVO.java.vm");
        customFile.put("vo/Del" + WordToCamelCaseUtil.convertWordsToCamelCase(TABLES) + "VO.java", "/template/ge/vo/DelVO.java.vm");
        return builder -> builder.beforeOutputFile((tableInfo, objMap) -> {

                })
                .customFile(customFile);
    }

    // 生成策略配置
    public static Consumer<StrategyConfig.Builder> getStrategyConfig() {
        return builder -> {
            builder.enableCapitalMode()     // 开启全局大写命名
                    .addInclude(TABLES)     // 匹配，指定要生成的数据表名，不写默认选定数据库所有表。include与exclude只能配置一项，支持正则匹配、例如^t_.*所有t_开头的表名
                    .addExclude()           // 排除，include与exclude只能配置一项，支持正则

                    // 实体策略配置
                    .entityBuilder()
                    .enableLombok()
                    .enableRemoveIsPrefix()                             // 开启 Boolean 类型字段移除 is 前缀
                    .enableTableFieldAnnotation()                       // 开启生成实体时生成字段注解
                    .addTableFills(new Property("updateTime", FieldFill.INSERT_UPDATE)) // 字段填充
                    .addTableFills(new Property("updateUser", FieldFill.INSERT_UPDATE))
                    .naming(NamingStrategy.underline_to_camel)          // 数据表映射实体命名策略：默认下划线转驼峰underline_to_camel
                    .columnNaming(NamingStrategy.underline_to_camel)    // 表字段映射实体属性命名规则：默认null，不指定按照naming执行
                    .idType(IdType.AUTO)                                // 添加全局主键类型
                    .formatFileName("%sPO")                             // 格式化实体名称
                    .build()

                    // mapper文件策略
                    .mapperBuilder()
                    .enableMapperAnnotation()   // 开启mapper注解
                    .enableBaseResultMap()      // 启用xml文件中的BaseResultMap 生成
                    .enableBaseColumnList()     // 启用xml文件中的BaseColumnList
                    //.cache(缓存类.class)设置缓存实现类
                    .formatMapperFileName("%sMapper")   // 格式化Dao类名称
                    .formatXmlFileName("%sMapper")      // 格式化xml文件名称
                    .build()

                    // service文件策略
                    .serviceBuilder()
                    .formatServiceFileName("%sService")         // 格式化 service 接口文件名称
                    .formatServiceImplFileName("%sServiceImpl") // 格式化 service 接口文件名称
                    .build()

                    // 控制层策略
                    .controllerBuilder()
                    .enableRestStyle()  // 开启生成@RestController
                    .formatFileName("%sController"); // 格式化文件名称
        };
    }
}
