package entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BeerUnitOrder extends BaseEntity {

    protected String name;

    protected String package_order_second_id;

    protected String product_second_id;

    protected String product_unit_second_id;

    protected int number_unit;

    protected float price;

    protected float total_discount;
}
