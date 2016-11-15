package edu.gdut.dao;

import edu.gdut.domain.CalTask;
import org.apache.ibatis.annotations.*;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-15 下午10:00
 */
@Mapper
public interface CalTaskMapper {
    @Select("SELECT * FROM calTask WHERE id = #{id}")
    CalTask findById(@Param("id") String id);

    @Insert("insert into callTask(id, dataFile, algoName, subTime, remark) values (#{id}, #{dataFile}, #{algoName}," +
            "#{subTime}, #{remark})")
    void insert(CalTask calTask);

    @Update("update callTask set resultFile=#{resultFile}, finTime=#{finTime} where id=#{id}")
    boolean update(CalTask calTask);
}

