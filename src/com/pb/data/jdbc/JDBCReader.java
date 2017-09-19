package com.pb.data.jdbc;

import com.pb.data.MerchantData;
import com.pb.data.MerchantImageData;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pb on 2017/5/4.
 */
public class JDBCReader extends JDBCBase {

    public static MerchantData read(int id){
        final String sql="select * from xj where ID=?";
        try {
            Object obj = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper(MerchantData.class));
            if (obj != null) {
                return (MerchantData) obj;
            }
        }catch (EmptyResultDataAccessException exception){

        }
        return null;
        }

    public static List<MerchantData> readUnDownloadImage(){
        final String sql="select * from `xj` where ImageNum=0 order by ImageNum";
        List<MerchantData> obj= jdbcTemplate.query(sql, new ResultSetExtractor<List<MerchantData>>() {
            public List<MerchantData> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<MerchantData> datas=new ArrayList<MerchantData>(1000);
                while (rs.next()) {
                    MerchantData data = new MerchantData();
                    data.setAddress(rs.getString("Address"));
                    data.setMerchantId(rs.getInt("ID"));
                    data.setName(rs.getString("Name"));
                    data.setArea(rs.getString("Area"));
                    datas.add(data);
                }
                return datas;
            }
        });
        if(obj==null){
            return new ArrayList<MerchantData>(0);
        }
        return obj;
    }
    public static MerchantImageData readImage(String id){
       /* final String sql="select * from downloadedpic where Url=?";
        try {
            Object obj = jdbcTemplate.queryForObject(sql, new Object[]{id}, new BeanPropertyRowMapper(MerchantImageData.class));
            if (obj != null) {
                return (MerchantImageData) obj;
            }
        }catch (EmptyResultDataAccessException exception){

        }*/
        return null;
    }
    public static List<MerchantImageData> readFailImage(){
        final String sql="select * from downloadedfailpic";
        List<MerchantImageData> obj= jdbcTemplate.queryForList(sql,MerchantImageData.class);
        if(obj!=null){
            return new ArrayList<MerchantImageData>(0);
        }
        return obj;
    }

}
