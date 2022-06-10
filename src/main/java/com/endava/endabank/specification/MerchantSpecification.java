package com.endava.endabank.specification;

import com.endava.endabank.dto.merchant.MerchantFilterAuditDto;
import com.endava.endabank.model.Merchant;
import com.endava.endabank.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class MerchantSpecification {

    public Specification<Merchant> filterAuditMerchant(MerchantFilterAuditDto request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Merchant, User> reviewedBy = root.join("reviewedBy");
            if (request.getMerchantName() != null && !request.getMerchantName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("storeName")),
                         request.getMerchantName().toLowerCase() + "%"));
            }
            if (request.getAdminName() != null && !request.getAdminName().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(reviewedBy.get("firstName")),
                         request.getAdminName().toLowerCase() + "%"));
            }
            if (request.getStartDate() != null && !request.getStartDate().isEmpty()) {
                LocalDateTime startDate = LocalDateTime.parse(request.getStartDate()+"T00:00:00");
                LocalDateTime endDate = LocalDateTime.parse(request.getStartDate()+"T23:59:59");
                if (request.getEndDate() != null && !request.getEndDate().isEmpty()) {
                    endDate = LocalDateTime.parse(request.getEndDate()+"T23:59:59");
                }
                predicates.add(criteriaBuilder.between(root.get("updatedAt"),
                        startDate, endDate));
            }
            predicates.add(criteriaBuilder.notEqual(root.get("merchantRequestState").get("id"), 1));
            query.orderBy(criteriaBuilder.desc(root.get("updatedAt")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));

        };
    }
}
