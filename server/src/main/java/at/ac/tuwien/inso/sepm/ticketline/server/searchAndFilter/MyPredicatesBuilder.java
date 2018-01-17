/*
package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.ArrayList;
import java.util.List;

public class MyPredicatesBuilder {
    private List<SearchCriteria> params;
    private String path;

    public MyPredicatesBuilder(String path) {
        this.path = path;
        params = new ArrayList<>();
    }

    public MyPredicatesBuilder with(
        String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build() {
        if (params.size() == 0) {
            return null;
        }

        List<BooleanExpression> predicates = new ArrayList<>();
        MyPredicate predicate;
        for (SearchCriteria param : params) {
            predicate = new MyPredicate(param);
            BooleanExpression exp = predicate.getPredicate(path);
            if (exp != null) {
                predicates.add(exp);
            }
        }

        BooleanExpression result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = result.and(predicates.get(i));
        }
        return result;
    }
}
*/
