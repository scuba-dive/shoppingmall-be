package io.groom.scubadive.shoppingmall.product.repository;

import io.groom.scubadive.shoppingmall.product.domain.Product;
import io.groom.scubadive.shoppingmall.product.domain.ProductOption;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final EntityManager em;
    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of("id", "name", "price", "createdAt");


    @Override
    public Page<ProductOption> findProductOptionPageable(Pageable pageable) {
        String baseQuery =
                "select po from ProductOption po " +
                "join fetch po.product p";

        String finalQuery = baseQuery + " " + createOrderByClause(pageable);

        List<ProductOption> content = em.createQuery(finalQuery
                        , ProductOption.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        Long total = em.createQuery("select count(po) from ProductOption po", Long.class)
                .getSingleResult();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Product> findProductsPageable(Pageable pageable) {
        String baseQuery = "select p from Product p " +
                "join fetch p.options po " +
                "left join po.productOptionImages poi";

        String finalQuery = baseQuery + " " + createOrderByClause(pageable);

        List<Product> content = em.createQuery(finalQuery, Product.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        Long total = em.createQuery("select count(p) from Product p", Long.class).getSingleResult();

        return new PageImpl<>(content, pageable, total);
    };


    private String createOrderByClause(Pageable pageable) {
        if (pageable.getSort().isEmpty()) {
            return " order by p.id desc";
        }

        String orderBy = pageable.getSort().stream()
                .filter(order -> ALLOWED_SORT_FIELDS.contains(order.getProperty()))
                .map(order -> "p." + order.getProperty() + " " + order.getDirection().name())
                .reduce((o1, o2) -> o1 + ", " + o2)
                .orElse("p.id desc");

        return " order by " + orderBy;
    }
}
