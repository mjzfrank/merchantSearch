package com.pb.data.jdbc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by pb on 2017/5/4.
 */
public abstract class JDBCBase {
    private final static ApplicationContext ctx;
    public static final JdbcTemplate jdbcTemplate;
    static {
        ctx = new ClassPathXmlApplicationContext("springbeans.xml");
        jdbcTemplate= (JdbcTemplate) ctx.getBean("jdbcTemplate");
    }
}
