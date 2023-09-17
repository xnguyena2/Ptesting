package com.example.heroku.response;

import com.example.heroku.model.UserPackageDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PackageDataResponse extends UserPackageDetail {

    private List<ProductInPackageResponse> items;

    public PackageDataResponse(UserPackageDetail s) {
        super.copy(s);
        items = new ArrayList<>();
    }

    public PackageDataResponse addItem(ProductInPackageResponse i) {
        items.add(i);
        return this;
    }
}
