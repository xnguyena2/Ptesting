package com.example.heroku.model.joinwith;

import com.example.heroku.model.UserPackage;
import com.example.heroku.model.UserPackageDetail;
import com.example.heroku.response.PackageDataResponse;
import com.example.heroku.response.ProductInPackageResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPackageDetailJoinWithUserPackage extends UserPackageDetail {

    /**
     * {@link com.example.heroku.model.UserPackageDetail}
     */

    /**
     * {@link com.example.heroku.model.UserPackage}
     */

    protected Long child_id;
    protected String child_group_id;
    protected Timestamp child_createat;

    private String child_package_second_id;
    private String child_device_id;
    private String child_product_second_id;
    private String child_product_unit_second_id;
    private String child_product_name;
    private String child_product_unit_name;
    private String child_product_group_unit_name;
    private String child_product_type;
    private float child_number_services_unit;
    private float child_number_unit;
    private float child_price;
    private float child_buy_price;
    private float child_discount_amount;
    private float child_discount_percent;
    private float child_discount_promotional;
    private String child_note;
    private UserPackageDetail.Status child_status;
    private String child_depend_to_product;


    public String getID() {
        return getPackage_second_id();
    }

    public UserPackageDetail getParent() {
        return super.toBuilder().build();
    }

    public UserPackage getChild() {
        return UserPackage.builder()
                .id(child_id)
                .group_id(child_group_id)
                .createat(child_createat)
                .package_second_id(child_package_second_id)
                .product_name(child_product_name)
                .product_unit_name(child_product_unit_name)
                .product_group_unit_name(child_product_group_unit_name)
                .product_type(child_product_type)
                .number_services_unit(child_number_services_unit)
                .device_id(child_device_id)
                .product_second_id(child_product_second_id)
                .product_unit_second_id(child_product_unit_second_id)
                .number_unit(child_number_unit)
                .price(child_price)
                .buy_price(child_buy_price)
                .discount_percent(child_discount_percent)
                .discount_amount(child_discount_amount)
                .discount_promotional(child_discount_promotional)
                .note(child_note)
                .status(child_status)
                .depend_to_product(child_depend_to_product)
                .build();
    }

    public static PackageDataResponse GeneratePackageData(List<UserPackageDetailJoinWithUserPackage> listData) {
        if (listData.isEmpty()) {
            return PackageDataResponse.Empty();
        }
        UserPackageDetailJoinWithUserPackage firstElement = listData.get(0);
        PackageDataResponse result = new PackageDataResponse(firstElement.getParent());
        for (UserPackageDetailJoinWithUserPackage child : listData) {
            UserPackage userPackage = child.getChild();
            if (userPackage.getProduct_second_id() == null || userPackage.getProduct_second_id().isEmpty()) {
                continue;
            }
            ProductInPackageResponse productInPackageResponse = new ProductInPackageResponse(userPackage);
            result.addItem(productInPackageResponse);
        }
        return result;
    }
}
