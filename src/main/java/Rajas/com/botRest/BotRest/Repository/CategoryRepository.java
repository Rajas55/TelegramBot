package Rajas.com.botRest.BotRest.Repository;

import Rajas.com.botRest.BotRest.Entity.CategoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<CategoryModel,Integer> {
    @Query(value = "select cat_id from category_model where category =:m", nativeQuery = true)
    public  int getCategoryId(@Param("m")String category);
}
