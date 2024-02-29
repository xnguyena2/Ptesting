package com.example.heroku.model.repository;

import com.example.heroku.model.Product;
import com.example.heroku.model.UserFCM;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;

public interface BeerRepository extends ReactiveCrudRepository<Product, Long> {

    @Query(value = "INSERT INTO product( group_id, product_second_id, name, detail, sku, upc, category, unit_category_config, meta_search, status, createat ) VALUES ( :group_id, :product_second_id, :name, :detail, :sku, :upc, :category, :unit_category_config, :meta_search, :status, :createat ) ON CONFLICT (group_id, product_second_id) DO UPDATE SET name = :name, detail = :detail, sku = :sku, upc = :upc, category = :category, unit_category_config = :unit_category_config, meta_search = :meta_search, status = :status, createat = :createat")
    Mono<Product> saveProduct(@Param("group_id") String group_id, @Param("product_second_id") String product_second_id,
                              @Param("name") String name, @Param("detail") String detail,
                              @Param("sku") String sku, @Param("upc") String upc,
                              @Param("category") String category, @Param("unit_category_config") String unit_category_config,
                              @Param("meta_search") String meta_search,
                              @Param("status") Product.Status status, @Param("createat") Timestamp createat);


    @Query(value = "SELECT * FROM product WHERE product.product_second_id = :id AND product.group_id = :group_id")
    Mono<Product> findBySecondID(@Param("group_id")String groupID, @Param("id")String id);

    @Query(value = "SELECT * FROM product WHERE product.product_second_id = :id AND product.group_id = :group_id AND ( product.status IS DISTINCT FROM 'SOLD_OUT' AND product.status IS DISTINCT FROM 'HIDE' )")
    Mono<Product> findBySecondIDCanOrder(@Param("group_id")String groupID, @Param("id")String id);

    @Query(value = "DELETE FROM product WHERE product.product_second_id = :id AND product.group_id = :group_id")
    Mono<Product> deleteBySecondId(@Param("group_id")String groupID, @Param("id")String id);


    //search all
    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.status IS DISTINCT FROM 'HIDE' ORDER BY :property DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> findByIdNotNullDESC(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size, @Param("property")String property);
    //search all
    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.status IS DISTINCT FROM 'HIDE' ORDER BY :property ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> findByIdNotNullASC(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size, @Param("property")String property);

    //search all
    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.status IS DISTINCT FROM 'HIDE' ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> findByIdNotNull(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    //search all
    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id ORDER BY :property ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> findByGroupIdASC(@Param("group_id")String groupID, @Param("property")String property, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id ORDER BY :property DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> findByGroupIdDESC(@Param("group_id")String groupID, @Param("property")String property, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_price RIGHT JOIN ( SELECT * FROM product WHERE product.group_id = :group_id ) product ON product.product_second_id = product_price.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminGetAllBeerSortByPriceASC(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_price RIGHT JOIN ( SELECT * FROM product WHERE product.group_id = :group_id ) product ON product.product_second_id = product_price.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminGetAllBeerSortByPriceDESC(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM ( SELECT * FROM product WHERE product.group_id = :group_id ) product LEFT JOIN ( SELECT product_unit_order.product_second_id, SUM(product_unit_order.number_unit) AS sold FROM product_unit_order WHERE product_unit_order.group_id = :group_id GROUP BY product_unit_order.product_second_id ) product_sold ON product.product_second_id = product_sold.product_second_id ORDER BY sold DESC NULLS LAST LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminGetAllBeerSortBySoldDESC(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    //for user so must remove hide
    @Query(value = "SELECT product_temp.* FROM ( SELECT * FROM product WHERE product.group_id = :group_id AND product.status IS DISTINCT FROM 'HIDE' ) product_temp LEFT JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_unit_temp ON product_temp.product_second_id = product_unit_temp.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> getAllBeerSortByPriceASC(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_temp.* FROM ( SELECT * FROM product WHERE product.group_id = :group_id AND product.status IS DISTINCT FROM 'HIDE' ) product_temp LEFT JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_unit_temp ON product_temp.product_second_id = product_unit_temp.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> getAllBeerSortByPriceDESC(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM ( SELECT product.product_second_id, SUM(product_unit_order.number_unit) AS sold FROM product LEFT JOIN product_unit_order ON product.product_second_id = product_unit_order.product_second_id AND product.group_id = product_unit_order.group_id WHERE product.group_id = :group_id AND product.status IS DISTINCT FROM 'HIDE' GROUP BY product.product_second_id ) product_temp RIGHT JOIN ( SELECT * FROM product WHERE product.group_id = :group_id AND product.status IS DISTINCT FROM 'HIDE' ) product ON product.product_second_id = product_temp.product_second_id ORDER BY sold DESC NULLS LAST LIMIT :size OFFSET (:page * :size)")
    Flux<Product> getAllBeerSortBySoldDESC(@Param("group_id")String groupID, @Param("page")int page, @Param("size")int size);

    //search by category
    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.category = :category")
    Flux<Product> findByGroupIDAndCategory(@Param("group_id")String groupID, @Param("category") String category, Pageable pageable);

    @Query(value = "SELECT product.* FROM ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_price_temp RIGHT JOIN ( SELECT * FROM product WHERE product.group_id = :group_id AND product.category = :category ) product ON product.product_second_id = product_price_temp.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> AdminFindByCategoryBeerSortByPriceASC(@Param("group_id")String groupID, @Param("category") String category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_price_temp RIGHT JOIN ( SELECT * FROM product WHERE product.group_id = :group_id AND product.category = :category ) product ON product.product_second_id = product_price_temp.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> AdminFindByCategoryBeerSortByPriceDESC(@Param("group_id")String groupID, @Param("category") String category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM ( SELECT product_unit_order.product_second_id, SUM(product_unit_order.number_unit) AS sold FROM product_unit_order WHERE product_unit_order.group_id = :group_id GROUP BY product_unit_order.product_second_id ) product_sole_temp RIGHT JOIN ( SELECT * FROM product WHERE product.group_id = :group_id AND product.category = :category ) product ON product.product_second_id = product_sole_temp.product_second_id ORDER BY sold DESC NULLS LAST LIMIT :size OFFSET (:page * :size)")
    Flux<Product> AdminFindByCategoryBeerSortBySoldDESC(@Param("group_id")String groupID, @Param("category") String category, @Param("page")int page, @Param("size")int size);


    //search by category
    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND (product.category = :category) AND product.status IS DISTINCT FROM 'HIDE' ORDER BY :property DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> findByCategoryDESC(@Param("group_id")String groupID, @Param("category") String category, @Param("page")int page, @Param("size")int size, @Param("property")String property);
    //search by category
    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND (product.category = :category) AND product.status IS DISTINCT FROM 'HIDE' ORDER BY :property ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> findByCategoryASC(@Param("group_id")String groupID, @Param("category") String category, @Param("page")int page, @Param("size")int size, @Param("property")String property);

    @Query(value = "SELECT product.* FROM ( SELECT * FROM product WHERE product.group_id = :group_id AND product.category = :category AND product.status IS DISTINCT FROM 'HIDE' ) product LEFT JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_price ON product.product_second_id = product_price.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> findByCategoryBeerSortByPriceASC(@Param("group_id")String groupID, @Param("category") String category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM ( SELECT * FROM product WHERE product.group_id = :group_id AND product.category = :category AND product.status IS DISTINCT FROM 'HIDE' ) product LEFT JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_price ON product.product_second_id = product_price.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> findByCategoryBeerSortByPriceDESC(@Param("group_id")String groupID, @Param("category") String category, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM ( SELECT * FROM product WHERE product.group_id = :group_id AND product.category = :category AND product.status IS DISTINCT FROM 'HIDE' ) product LEFT JOIN ( SELECT product_unit_order.product_second_id, SUM(product_unit_order.number_unit) AS sold FROM product_unit_order WHERE product_unit_order.group_id = :group_id GROUP BY product_unit_order.product_second_id ) product_sold ON product.product_second_id = product_sold.product_second_id ORDER BY sold DESC NULLS LAST LIMIT :size OFFSET (:page * :size)")
    Flux<Product> findByCategoryBeerSortBySoldDESC(@Param("group_id")String groupID, @Param("category") String category, @Param("page")int page, @Param("size")int size);



    //search by query for admin
    @Query(value = "SELECT * FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeer(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_search.* FROM ( SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) ) product_search INNER JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_unit_temp ON product_search.product_second_id = product_unit_temp.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerSortByPriceASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_search.* FROM ( SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) ) product_search INNER JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_unit_temp ON product_search.product_second_id = product_unit_temp.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerSortByPriceDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) ORDER BY name ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerSortByNameASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) ORDER BY name DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerSortByNameDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) ORDER BY createat ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerSortByCreateASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerSortByCreateDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_search.* FROM ( SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) ) product_search LEFT JOIN ( SELECT product_unit_order.product_second_id, SUM(product_unit_order.number_unit) AS sold from product_unit_order WHERE product_unit_order.group_id = :group_id GROUP BY product_unit_order.product_second_id ) product_sold_temp ON product_search.product_second_id = product_sold_temp.product_second_id ORDER BY sold DESC NULLS LAST LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerSortBySoldDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);



    //search by query for user
    @Query(value = "SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) AND product.status IS DISTINCT FROM 'HIDE' LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeer(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_s.* FROM ( SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) AND product.status IS DISTINCT FROM 'HIDE' ) product_s INNER JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_unit_temp ON product_s.product_second_id = product_unit_temp.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerSortByPriceASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_s.* FROM ( SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) AND product.status IS DISTINCT FROM 'HIDE' ) product_s INNER JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_unit_temp ON product_s.product_second_id = product_unit_temp.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerSortByPriceDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) AND product.status IS DISTINCT FROM 'HIDE' ORDER BY name ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerSortByNameASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) AND product.status IS DISTINCT FROM 'HIDE' ORDER BY name DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerSortByNameDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) AND product.status IS DISTINCT FROM 'HIDE' ORDER BY createat ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerSortByCreateASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) AND product.status IS DISTINCT FROM 'HIDE' ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerSortByCreateDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_search.* FROM ( SELECT product.* FROM product INNER JOIN search_token ON search_token.product_second_id = product.product_second_id AND search_token.group_id = product.group_id WHERE product.group_id = :group_id AND search_token.tokens @@ to_tsquery(:search) AND product.status IS DISTINCT FROM 'HIDE' ) product_search LEFT JOIN ( SELECT product_unit_order.product_second_id, SUM(product_unit_order.number_unit) AS sold from product_unit_order WHERE product_unit_order.group_id = :group_id GROUP BY product_unit_order.product_second_id ) product_sold ON product_search.product_second_id = product_sold.product_second_id ORDER BY sold DESC NULLS LAST LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerSortBySoldDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);


    //SEARCH LIKE foradmin
    //select * FROM product where meta_search ~ '(?=.*go)(?=.*ha)'
    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerLike(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_temp.* FROM ( SELECT product.* FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search ) product_temp LEFT JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) as price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_unit_temp ON product_temp.product_second_id = product_unit_temp.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerLikeSortByPriceASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_temp.* FROM ( SELECT product.* FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search ) product_temp LEFT JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) as price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_unit_temp ON product_temp.product_second_id = product_unit_temp.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerLikeSortByPriceDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search ORDER BY name ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerLikeSortByNameASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search ORDER BY name DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerLikeSortByNameDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search ORDER BY createat ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerLikeSortCreateASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerLikeSortCreateDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_temp.* FROM ( SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search ) product_temp LEFT JOIN ( SELECT product_unit_order.product_second_id, SUM(product_unit_order.number_unit) AS sold FROM product_unit_order WHERE product_unit_order.group_id = :group_id GROUP BY product_unit_order.product_second_id ) product_sold_temp ON product_temp.product_second_id = product_sold_temp.product_second_id ORDER BY sold DESC NULLS LAST LIMIT :size OFFSET (:page * :size)")
    Flux<Product> adminSearchBeerLikeSortBySoldDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);


    //SEARCH LIKE
    //select * FROM product where meta_search ~ '(?=.*go)(?=.*ha)'
    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search AND product.status IS DISTINCT FROM 'HIDE' LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerLike(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_temp.* FROM ( SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search AND product.status IS DISTINCT FROM 'HIDE' ) product_temp INNER JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_unit_temp ON product_temp.product_second_id = product_unit_temp.product_second_id ORDER BY price ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerLikeSortByPriceASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_temp.* FROM ( SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search AND product.status IS DISTINCT FROM 'HIDE' ) product_temp INNER JOIN ( SELECT product_unit.product_second_id, MAX(product_unit.price) AS price FROM product_unit WHERE product_unit.group_id = :group_id GROUP BY product_unit.product_second_id ) product_unit_temp ON product_temp.product_second_id = product_unit_temp.product_second_id ORDER BY price DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerLikeSortByPriceDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search AND product.status IS DISTINCT FROM 'HIDE' ORDER BY name ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerLikeSortByNameASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search AND product.status IS DISTINCT FROM 'HIDE' ORDER BY name DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerLikeSortByNameDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search AND product.status IS DISTINCT FROM 'HIDE' ORDER BY createat ASC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerLikeSortCreateASC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search AND product.status IS DISTINCT FROM 'HIDE' ORDER BY createat DESC LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerLikeSortCreateDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);

    @Query(value = "SELECT product_temp.* FROM ( SELECT * FROM product WHERE product.group_id = :group_id AND product.meta_search LIKE :search AND product.status IS DISTINCT FROM 'HIDE' ) product_temp LEFT JOIN ( SELECT product_unit_order.product_second_id, SUM(product_unit_order.number_unit) AS sold FROM product_unit_order WHERE product_unit_order.group_id = :group_id GROUP BY product_unit_order.product_second_id ) product_unit_order_temp ON product_temp.product_second_id = product_unit_order_temp.product_second_id ORDER BY sold DESC NULLS LAST LIMIT :size OFFSET (:page * :size)")
    Flux<Product> searchBeerLikeSortBySoldDESC(@Param("group_id")String groupID, @Param("search")String search, @Param("page")int page, @Param("size")int size);


    //get recommend word
    //SELECT SUBSTRING(product.meta_search, '(?<=(nhap ngoai))(\w*)') FROM product;
}
