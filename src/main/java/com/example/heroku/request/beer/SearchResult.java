package com.example.heroku.request.beer;

import com.example.heroku.model.count.ResultWithCount;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResult<T> extends ResultWithCount {
    private boolean isNormalSearch;
    private String searchTxt;
    private List<T> result;

    public SearchResult<T> Add(T newItem) {
        if (result == null) {
            result = new ArrayList<>();
        }
        result.add(newItem);
        return this;
    }
}
