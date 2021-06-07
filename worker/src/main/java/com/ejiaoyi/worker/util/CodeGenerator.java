package com.ejiaoyi.worker.util;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

/**
 * mybatis plus auto generator 代码生成器工具
 *
 * @author Z0001
 * @since 2020-03-18
 */
public class CodeGenerator {

    /**
     * 读取控制台内容
     *
     * @param tip 输入提示
     * @return 输入值
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("please enter " + tip + "：");
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("please enter the correct " + tip + "！");
    }

    public static void main(String[] args) {
        // init the mybatis plus auto generator
        AutoGenerator autoGenerator = new AutoGenerator();

        // scanner
        String author = "samzqr";
        String moduleName = CodeGenerator.scanner("module name");
        String entityName = CodeGenerator.scanner("entity name (multiple entity name split by ,)");
//        String dbName = CodeGenerator.scanner("database name");
        String dbName="work1";
        String dbUserName = "root";
        String dbPwd = "root";

        // global config
        GlobalConfig globalConfig = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        globalConfig.setOutputDir(projectPath + "\\" + moduleName + "\\src\\main\\java");
        globalConfig.setAuthor(author);
        globalConfig.setOpen(false);
        globalConfig.setBaseResultMap(true);
        globalConfig.setBaseColumnList(true);
        globalConfig.setIdType(IdType.AUTO);
        autoGenerator.setGlobalConfig(globalConfig);

        // 数据源配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setUrl("jdbc:mysql://127.0.0.1/" + dbName + "?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername(dbUserName);
        dataSourceConfig.setPassword(dbPwd);
        dataSourceConfig.setDbType(DbType.MYSQL);
        autoGenerator.setDataSource(dataSourceConfig);

        // package config
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(moduleName);
        packageConfig.setParent("com.ejiaoyi");
        autoGenerator.setPackageInfo(packageConfig);

        // strategy
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setInclude(entityName.split(","));
        autoGenerator.setStrategy(strategy);
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
    }
}
