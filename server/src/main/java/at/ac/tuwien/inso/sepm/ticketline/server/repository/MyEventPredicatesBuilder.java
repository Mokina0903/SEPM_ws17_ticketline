package at.ac.tuwien.inso.sepm.ticketline.server.repository;

import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.ArrayList;
import java.util.List;

public class MyEventPredicatesBuilder {
    private List<SearchCriteria> params;

    public MyEventPredicatesBuilder() {
        params = new ArrayList<>();
    }

    public MyEventPredicatesBuilder with(
        String key, String operation, Object value) {

        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build() {
        if (params.size() == 0) {
            return null;
        }

        List<BooleanExpression> predicates = new ArrayList<>();
        MyEventPredicate predicate;
        for (SearchCriteria param : params) {
            predicate = new MyEventPredicate(param);
            BooleanExpression exp = predicate.getPredicate();
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
