package org.otomotus.backend.specification;

import jakarta.persistence.criteria.Predicate;
import org.otomotus.backend.entity.AuctionEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AuctionSpecification {

    public static Specification<AuctionEntity> filter(
            String brand, String model, Integer minYear, Integer maxYear,
            BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (brand != null) predicates.add(cb.equal(root.get("car").get("brand"), brand));
            if (model != null) predicates.add(cb.equal(root.get("car").get("model"), model));
            if (minYear != null) predicates.add(cb.ge(root.get("car").get("productionYear"), minYear));
            if (maxYear != null) predicates.add(cb.le(root.get("car").get("productionYear"), maxYear));
            if (minPrice != null) predicates.add(cb.ge(root.get("price"), minPrice));
            if (maxPrice != null) predicates.add(cb.le(root.get("price"), maxPrice));

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        };
    }

}
