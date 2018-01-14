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
        System.out.println("Build() ....");
        if (params.size() == 0) {
            System.out.println("Build() .... params sind numm");
            return null;
        }

        List<BooleanExpression> predicates = new ArrayList<>();
        MyEventPredicate predicate;
        for (SearchCriteria param : params) {
            System.out.println("getting param...." + param);
            predicate = new MyEventPredicate(param);
            System.out.println("predicate: " + predicate);
            BooleanExpression exp = predicate.getPredicate();
            System.out.println("exp is : " + exp);
            if (exp != null) {
                System.out.println("exp is : " + exp);
                predicates.add(exp);
            }
        }

        System.out.println("getting result..." + predicates.size());
        BooleanExpression result = predicates.get(0);
        System.out.println("result is::: " + result);
        for (int i = 1; i < predicates.size(); i++) {
            System.out.println("setting result...");
            result = result.and(predicates.get(i));
        }
        return result;
    }
}
