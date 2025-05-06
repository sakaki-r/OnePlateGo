package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.example.app.domain.Favorite;

@Mapper
public interface FavoriteMapper {

    @Insert("""
        INSERT INTO favorite(member_id, dish_id, dish_type, created_at)
        VALUES (#{memberId}, #{dishId}, #{dishType}, NOW())
    """)
    void insert(Favorite favorite);

    @Delete("""
        DELETE FROM favorite
        WHERE member_id = #{memberId}
        AND dish_id = #{dishId}
        AND dish_type = #{dishType}
    """)
    void delete(Favorite favorite);
    
    @Select("""
            SELECT COUNT(*) FROM favorite
            WHERE member_id = #{memberId}
            AND dish_id = #{dishId}
            AND dish_type = #{dishType}
        """)
        int isFavorited(Favorite favorite);

    @Select("""
    	    SELECT f.id, f.member_id, f.dish_id, f.dish_type, f.created_at,
    	           CASE f.dish_type
    	               WHEN 'meat' THEN m.maindish
    	               WHEN 'fish' THEN fi.maindish
    	               WHEN 'other' THEN o.maindish
    	               ELSE ''
    	           END AS dish_name
    	    FROM favorite f
    	    LEFT JOIN meat m ON f.dish_type = 'meat' AND f.dish_id = m.id
    	    LEFT JOIN fish fi ON f.dish_type = 'fish' AND f.dish_id = fi.id
    	    LEFT JOIN other o ON f.dish_type = 'other' AND f.dish_id = o.id
    	    WHERE f.member_id = #{memberId}
    	""")
        
    	@Results(id="FavoriteResult", value= {
    			@Result(property="id", column="id"),
    			@Result(property="memberId", column="member_id"),
    			@Result(property="dishId", column="dish_id"),
    			@Result(property="dishType", column="dish_type"),
    			@Result(property="createdAt", column="created_at"),
    			@Result(property="dishName", column="dish_name")
    	})
    
    	List<Favorite> findByMemberId(int memberId);

}
