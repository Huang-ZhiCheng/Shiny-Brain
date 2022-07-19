package com.hzc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * @author : hzc
 * @date: 2022/4/10 - 04 - 10 - 16:03
 * @Description: com.hzc
 * @version: 1.0
 */
public class MysqlGenerator {
    public static void main(String[] args) {
        //此处默认有两个对应的实现类，大家不要导错包
        GlobalConfig globalConfig = new GlobalConfig();
        //设置全局的配置
        globalConfig.setActiveRecord(true)//是否支持AR模式
                .setAuthor("hzc")//设置作者
                .setOutputDir("D:\\seckill-demo\\src\\main\\java")//设置生成路径,项目名要指定好
                .setFileOverride(true)//设置文件覆盖
                .setIdType(IdType.AUTO) //设置主键生成策略
                .setServiceName("%sService")//设置生成的serivce接口的名字
                .setBaseResultMap(true) //设置基本的结果集映射
                .setBaseColumnList(true);//设置基本的列集合

        //设置数据源的配置
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver")
                .setUrl("jdbc:mysql://localhost:3306/seckill?serverTimezone=UTC")
                .setUsername("root").setPassword("root");

        // 进行策略配置
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig.setCapitalMode(true)//设置全局大写命名
                .setNaming(NamingStrategy.underline_to_camel)//数据库表映射到实体的命名策略
//                .setTablePrefix("tbl_")//设置表名前缀
                .setInclude();//生成的表(里面什么都不写默认全部生成,用字符串写了就只生成指定表)

        // 进行包名的策略配置
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent("com.hzc")//父项目名
                .setMapper("mapper")
                .setService("service")
                .setController("controller")
                .setEntity("pojo")
                .setXml("mapper");

        //整合配置
        AutoGenerator autoGenerator = new AutoGenerator();
        autoGenerator.setGlobalConfig(globalConfig).setDataSource(dataSourceConfig).setStrategy(strategyConfig)
                .setPackageInfo(packageConfig);
        autoGenerator.execute();
    }

}
