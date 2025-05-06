package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.example.app.domain.Meal;

@Mapper
public interface MealMapper {
	
	@Select("SELECT * FROM meat ORDER BY RAND() LIMIT #{count}")
	List<Meal> findMeat(@Param("count") int count);
	
	@Select("SELECT * FROM fish ORDER BY RAND() LIMIT #{count}")
	List<Meal> findFish(@Param("count") int count);
	
	@Select("SELECT * FROM other ORDER BY RAND() LIMIT #{count}")
	List<Meal> findOther(@Param("count") int count);
	
	@Select("SELECT ingredient FROM meatingredients WHERE dish_id =#{dishId}")
	List<String> findMeatIngredients(@Param("dishId") Integer dishId);
	
	@Select("SELECT ingredient FROM fishingredients WHERE dish_id =#{dishId}")
	List<String> findFishIngredients(@Param("dishId") Integer dishId);
	
	@Select("SELECT ingredient FROM otheringredients WHERE dish_id =#{dishId}")
	List<String> findOtherIngredients(@Param("dishId") Integer dishId);

	@Select("SELECT * FROM meat WHERE id = #{id}")
	Meal findMeatById(@Param("id") Integer id);

	@Select("SELECT * FROM fish WHERE id = #{id}")
	Meal findFishById(@Param("id") Integer id);

	@Select("SELECT * FROM other WHERE id = #{id}")
	Meal findOtherById(@Param("id") Integer id);

	// 料理名を dishId と dishType から取得（共通化）
		@Select(""
				+ "SELECT maindish FROM meat WHERE id = #{dishId} AND #{dishType} = 'meat'\n"
				+ "UNION\n"
				+ "SELECT maindish FROM fish WHERE id = #{dishId} AND #{dishType} = 'fish'\n"
				+ "UNION\n"
				+ "SELECT maindish FROM other WHERE id = #{dishId} AND #{dishType} = 'other'"
				+ "")
		String findMaindishByDishIdAndType(@Param("dishId") Integer dishId, @Param("dishType") String dishType);
}
