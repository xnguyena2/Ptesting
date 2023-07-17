package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BeerOrder extends BaseEntity {

    protected String name;

    protected String package_order_second_id;

    protected String product_second_id;

    protected String voucher_second_id;

    protected float total_price;

    protected float ship_price;
}
