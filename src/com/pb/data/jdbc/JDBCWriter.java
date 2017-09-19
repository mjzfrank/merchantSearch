package com.pb.data.jdbc;

import com.pb.data.MerchantData;
import com.pb.data.MerchantImageData;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by pb on 2017/5/4.
 */
public class JDBCWriter extends JDBCBase {
private static final String merchantTabelName="`xj`";

    public static void write(final MerchantData data) {
        final String sql = String.format("insert into %s (`ID`,`Name`,`Address`,`Tel`,`Stars`,`BusinessQualification`,`BusinessTime`,`AveragePrice`,`ImageNum`,`area`) values(?,?,?,?,?,?,?,?,0,?)", merchantTabelName);
        System.out.println(String.format("开始添加商铺：%s,地区:%s,号码：%d", data.getName(),data.getArea(), data.getMerchantId()) + "_" + new Date());
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setInt(1, data.getMerchantId());
                ps.setString(2, data.getName());
                ps.setString(3, data.getAddress());
                ps.setString(4, data.getTel());
                ps.setString(5, data.getStars());
                ps.setString(6, data.getBusinessQualification());
                ps.setString(7, data.getBusinessTime());
                ps.setString(8, data.getAveragePrice());
                ps.setString(9, data.getArea());
                return ps;
            }
        });
        System.out.println(String.format("添加商铺：%s,号码：%d，成功", data.getName(), data.getMerchantId()) + "_" + new Date());
    }

    public static void writeImage(final MerchantImageData imageData){
        final String sql="insert into downloadedpic values(?,?,?)";
        jdbcTemplate.update(createPSC(imageData,sql));
    }

    public static void writeFailImage(final MerchantImageData imageData){
        try {
            final String sql = "insert into downloadedfailpic values(?,?,?)";
            jdbcTemplate.update(createPSC(imageData, sql));
        }catch (Exception ex){

        }
    }

    private static PreparedStatementCreator createPSC(final MerchantImageData imageData,final String sql){
       return new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps=connection.prepareStatement(sql);
                ps.setString(1,imageData.getImageUrl());
                ps.setInt(2,imageData.getMerchantId());
                ps.setString(3,imageData.getName());
                return ps;
            }
        };
    }


    public static void writeNoFound(final String url,final String id, final String name){
        try {
            final String sql = "insert into nofoundurl values(?,?,?)";
            jdbcTemplate.update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, url);
                    ps.setString(2, id);
                    ps.setString(3, name);
                    return ps;
                }
            });
        }catch (Exception ex){

        }
    }

    public  static void updateImageNum(final int id){
        final String sql=String.format("update %s set ImageNum=ImageNum+1 where id=%d",merchantTabelName,id);
        jdbcTemplate.update(sql);
    }
}
