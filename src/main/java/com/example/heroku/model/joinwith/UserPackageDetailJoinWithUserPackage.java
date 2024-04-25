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
public class UserPackageDetailJoinWithUserPackage {
    //    UserPackageDetail
    protected Long id;
    protected String group_id;
    protected Timestamp createat;


    private String package_second_id;
    private String device_id;
    private String staff_id;
    private String package_type;
    private String table_id;
    private String table_name;
    private String area_id;
    private String area_name;
    private String voucher;
    private float price;
    private float payment;
    private float discount_amount;
    private float discount_percent;
    private float discount_promotional;
    private float discount_by_point;
    private float additional_fee;
    private String additional_config;
    private float ship_price;
    private float cost;
    private float profit;
    private int point;
    private String note;
    private String image;
    private String progress;
    private UserPackageDetail.Status status;


//    UserPackage

    protected Long child_id;
    protected String child_group_id;
    protected Timestamp child_createat;

    private String child_package_second_id;
    private String child_device_id;
    private String child_product_second_id;
    private String child_product_unit_second_id;
    private float child_number_unit;
    private float child_price;
    private float child_buy_price;
    private float child_discount_amount;
    private float child_discount_percent;
    private float child_discount_promotional;
    private String child_note;
    private UserPackageDetail.Status child_status;


    public String getID() {
        return package_second_id;
    }

    public UserPackageDetail getParent() {
        return UserPackageDetail.builder()
                .id(id)
                .createat(createat)
                .group_id(group_id)
                .package_second_id(package_second_id)
                .device_id(device_id)
                .staff_id(staff_id)
                .package_type(package_type)
                .table_id(table_id)
                .table_name(table_name)
                .area_id(area_id)
                .area_name(area_name)
                .voucher(voucher)
                .price(price)
                .payment(payment)
                .discount_amount(discount_amount)
                .discount_percent(discount_percent)
                .discount_promotional(discount_promotional)
                .discount_by_point(discount_by_point)
                .additional_fee(additional_fee)
                .additional_config(additional_config)
                .ship_price(ship_price)
                .cost(cost)
                .profit(profit)
                .point(point)
                .note(note)
                .image(image)
                .progress(progress)
                .status(status)
                .build();
    }

    public UserPackage getChild() {
        return UserPackage.builder()
                .id(child_id)
                .group_id(child_group_id)
                .createat(child_createat)
                .package_second_id(package_second_id)
                .device_id(device_id)
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
