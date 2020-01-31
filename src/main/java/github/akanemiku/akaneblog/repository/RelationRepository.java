package github.akanemiku.akaneblog.repository;

import github.akanemiku.akaneblog.model.Meta;
import github.akanemiku.akaneblog.model.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RelationRepository extends JpaRepository<Relation, Integer> {

    /**
     * 通过mid查找所有关系
     * @param mid
     * @return
     */
    @Query(nativeQuery = true,value = "select * from t_relation where mid=?1")
    List<Relation> findRelationByMid(Integer mid);
}
