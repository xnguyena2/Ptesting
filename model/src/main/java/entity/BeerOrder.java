package entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BeerOrder {

    protected String name;

    protected String package_order_second_id;

    protected String product_second_id;

    protected String voucher_second_id;

    protected float total_price;

    protected float ship_price;

    protected Timestamp createat;
}
