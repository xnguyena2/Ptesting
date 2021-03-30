package com.example.heroku.request.Order;

import com.example.heroku.model.PackageOrder;
import com.example.heroku.model.count.ResultWithCount;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchResult  extends ResultWithCount {
    private List<PackageOrder> result;

    public OrderSearchResult Add(PackageOrder newItem) {
        if (result == null) {
            result = new ArrayList<>();
        }
        result.add(newItem);
        return this;
    }

    public PackageOrder[] GetResultAsArray() {
        return result.toArray(new PackageOrder[0]);
    }
}
