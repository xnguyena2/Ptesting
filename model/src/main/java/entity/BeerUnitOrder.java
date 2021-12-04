package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BeerUnitOrder {

    protected String name;

    protected String package_order_second_id;

    protected String beer_second_id;

    protected String beer_unit_second_id;

    protected int number_unit;

    protected float price;

    protected float total_discount;

    protected Timestamp createat;
}
