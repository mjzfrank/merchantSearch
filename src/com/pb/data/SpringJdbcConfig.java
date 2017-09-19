package com.pb.data;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Created by pb on 2017/5/4.
 */

    public class SpringJdbcConfig {
        public static DataSource mysqlDataSource() {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            dataSource.setDriverClassName("com.mysql.jdbc.Driver");
            dataSource.setUrl("jdbc:mysql://localhost:3306/menchan?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC");
            dataSource.setUsername("root");
            dataSource.setPassword("19930107");

            return dataSource;
        }
    }
