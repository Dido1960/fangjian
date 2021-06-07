package com.ejiaoyi.admin.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.ejiaoyi.common.dto.Generator;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器工具类
 *
 * @author Z0001
 * @since 2020-5-8
 */
public class GeneratorUtil {

    private static final String GENERATOR_ENTITY = "entity";

    private static final String GENERATOR_CONTROLLER = "controller";

    private static final String GENERATOR_SERVICE = "service";

    private static final String GENERATOR_MAPPER = "mapper";

    private static final String GENERATOR_MAPPER_XML = "mapperXml";

    private static final String MODULE_SWAGGER2 = "swagger2";

    private static final String MODULE_OVERRIDE = "override";

    public static void generator(Generator generator) {
        List<String> generators = generator.getGenerator();
        List<String> modules = CollectionUtils.isEmpty(generator.getModule()) ? new ArrayList<>() : generator.getModule();

        // init the mybatis plus auto generator
        AutoGenerator autoGenerator = new AutoGenerator();

        // global config
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "\\" + generator.getModuleName() + "\\src\\main\\java");
        globalConfig.setAuthor(generator.getAuthor());
        globalConfig.setOpen(false);
        globalConfig.setSwagger2(modules.contains(MODULE_SWAGGER2));
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setIdType(IdType.AUTO);
        globalConfig.setFileOverride(modules.contains(MODULE_OVERRIDE));
        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://127.0.0.1/" + generator.getDbName() + "?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername(generator.getDbUser());
        dataSourceConfig.setPassword(generator.getDbPwd());
        dataSourceConfig.setDbType(DbType.MYSQL);
        autoGenerator.setDataSource(dataSourceConfig);

        // package config
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(generator.getModuleName());
        packageConfig.setParent("com.ejiaoyi");
        autoGenerator.setPackageInfo(packageConfig);

        // template
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setEntity(generators.contains(GENERATOR_ENTITY) ? "generator/entity.java" : "");
        templateConfig.setService(generators.contains(GENERATOR_SERVICE) ? "generator/service.java" : "");
        templateConfig.setServiceImpl(generators.contains(GENERATOR_SERVICE) ? "generator/serviceImpl.java" : "");
        templateConfig.setController(generators.contains(GENERATOR_CONTROLLER) ? "generator/controller.java" : "");
        templateConfig.setMapper(generators.contains(GENERATOR_MAPPER) ? "generator/mapper.java" : "");
        templateConfig.setXml(generators.contains(GENERATOR_MAPPER_XML) ? "generator/mapper.xml" : "");
        autoGenerator.setTemplate(templateConfig);

        // strategy
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setInclude(generator.getEntity().toArray(new String[]{}));
        autoGenerator.setStrategy(strategy);
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
    }
}
