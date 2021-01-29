package com.jt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

//不需要数据源,为什么会报数据源错误???? 原因:继承父级的包,springBoot有开箱即用的规则.
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) //排除数据源启动
public class SpringBootRun {
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootRun.class,args);
	}
}
