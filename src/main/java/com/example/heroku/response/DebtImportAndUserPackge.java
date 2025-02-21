package com.example.heroku.response;

import com.example.heroku.request.warehouse.GroupImportWithItem;
import lombok.*;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebtImportAndUserPackge {
    private List<PackageDataResponse> listPackage;
    private List<GroupImportWithItem> listImport;

    public DebtImportAndUserPackge setPackage(List<PackageDataResponse> p) {
        listPackage = p;
        return this;
    }

    public DebtImportAndUserPackge setImport(List<GroupImportWithItem> i) {
        listImport = i;
        return this;
    }
}
