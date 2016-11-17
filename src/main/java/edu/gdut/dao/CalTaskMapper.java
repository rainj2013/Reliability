package edu.gdut.dao;

import edu.gdut.domain.CalTask;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Author:  rainj2013
 * Email:  yangyujian25@gmail.com
 * Date:  16-11-15 下午10:00
 */
@Mapper
public interface CalTaskMapper {
    @Select("SELECT * FROM calTask WHERE id = #{id}")
    CalTask findById(@Param("id") String id);

    @Select("SELECT id, resultFile, dataFile, remark, algoName, subTime, finTime, status FROM calTask ORDER BY subTime " +
            "DESC LIMIT 0, #{value}")
    List<CalTask> findTop(@Param("value")int value);

    @Insert("insert into calTask(id, dataFile, algoName, subTime, remark, status) values (#{id}, #{dataFile}, #{algoName}," +
            "#{subTime}, #{remark}, #{status})")
    void insert(CalTask calTask);

    @Update("update calTask set resultFile=#{resultFile}, finTime=#{finTime}, status=#{status} where id=#{id}")
    boolean update(CalTask calTask);
}

