package com.example.heroku.model;

import com.example.heroku.model.entity.BaseEntity;
import com.example.heroku.util.Util;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="product_import")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImport extends BaseEntity {

    private String group_import_second_id;

    private String product_second_id;

    private String product_unit_second_id;

    private String product_unit_name_category;

    private float price;

    private int amount;

    private String note;

    private ImportType type;

    private Status status;

    // GroupImportJoinProductImport


    public ProductImport AutoFill() {
        return (ProductImport) super.AutoFillIfNull();
    }



    public enum ImportType {
        UN_KNOW("UN_KNOW"),
        UPDATE_NUMBER("UPDATE_NUMBER"),
        DELETE_PRODUCT("DELETE_PRODUCT"),
        SELLING("SELLING"),
        SELLING_RETURN("SELLING_RETURN"),

        CHECK_WAREHOUSE("CHECK_WAREHOUSE"),
        IMPORT("IMPORT"),
        EXPORT("EXPORT");


        private String name;

        ImportType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, ImportType> lookup = new HashMap<>();

        static {
            for (ImportType sts : ImportType.values()) {
                lookup.put(sts.getName(), sts);
            }
        }

        public static ImportType get(String text) {
            try {
                ImportType val = lookup.get(text);
                if (val == null) {
                    return UN_KNOW;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return UN_KNOW;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }



    public enum Status {
        CREATE("CREATE"),
        DONE("DONE"),
        RETURN("RETURN");


        private String name;

        Status(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        private static final Map<String, Status> lookup = new HashMap<>();

        static {
            for (Status sts : Status.values()) {
                lookup.put(sts.getName(), sts);
            }
        }

        public static Status get(String text) {
            try {
                Status val = lookup.get(text);
                if (val == null) {
                    return CREATE;
                }
                return val;
            } catch (Exception ex) {
                ex.printStackTrace();
                return CREATE;
            }
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
