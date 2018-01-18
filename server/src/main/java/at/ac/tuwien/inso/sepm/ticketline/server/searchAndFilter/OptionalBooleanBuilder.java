package at.ac.tuwien.inso.sepm.ticketline.server.searchAndFilter;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.function.Function;

public class OptionalBooleanBuilder {

    private BooleanExpression predicate;

    public OptionalBooleanBuilder(BooleanExpression predicate) {
        this.predicate = predicate;
    }

    public <T> OptionalBooleanBuilder notNullAnd(Function<T, BooleanExpression> expressionFunction, T value) {
        if (value != null) {
            System.out.println("notNUllAnd Loc is not null: " + value);
            if (isNumeric(value.toString()) && Long.parseLong(value.toString()) != -1) {
                System.out.println("Adds zip to search : " + value.toString());
                return new OptionalBooleanBuilder(predicate.and(expressionFunction.apply(value)));
            }
        }
        return this;
    }

    public <T> OptionalBooleanBuilder notNullOr(Function<T, BooleanExpression> expressionFunction, T value) {
        if (value != null) {
            if (isNumeric(value.toString())) {
                return new OptionalBooleanBuilder(predicate.or(expressionFunction.apply(value)));
            }
        }
        return this;
    }

    public OptionalBooleanBuilder notEmptyAnd(Function<String, BooleanExpression> expressionFunction, String value) {
        if (!StringUtils.isEmpty(value)) {
            return new OptionalBooleanBuilder(predicate.and(expressionFunction.apply(value)));
        }
        return this;
    }

    public OptionalBooleanBuilder notEmptyOr(Function<String, BooleanExpression> expressionFunction, String value) {
        if (!StringUtils.isEmpty(value)) {
            return new OptionalBooleanBuilder(predicate.or(expressionFunction.apply(value)));
        }
        return this;
    }

    public <T> OptionalBooleanBuilder notEmptyAnd(Function<Collection<T>, BooleanExpression> expressionFunction, Collection<T> collection) {
        if (!CollectionUtils.isEmpty(collection)) {
            return new OptionalBooleanBuilder(predicate.and(expressionFunction.apply(collection)));
        }
        return this;
    }


    public OptionalBooleanBuilder notEmptyIntegerAnd(Function<String, BooleanExpression> expressionFunction, Integer value) {
        if (!StringUtils.isEmpty(value) || value != -1) {
            System.out.println("IntegerMethod: " + value);
            return new OptionalBooleanBuilder(predicate.and(expressionFunction.apply(value.toString())));
        }
        return this;
    }


    public BooleanExpression build() {
        return predicate;
    }

    public boolean isNumeric(String s) {
        return s != null && s.matches("[-+]?\\d*\\.?\\d+");
    }

}