package com.example.heroku.request.beer;

import com.example.heroku.model.count.ResultWithCount;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult extends ResultWithCount {
    private boolean isNormalSearch;
    private String searchTxt;
    private List<BeerSubmitData> result;

    public SearchResult Add(BeerSubmitData newItem) {
        if (result == null) {
            result = new ArrayList<>();
        }
        result.add(newItem);
        return this;
    }

    public BeerSubmitData[] GetResultAsArray() {
        return result.toArray(new BeerSubmitData[0]);
    }
}
