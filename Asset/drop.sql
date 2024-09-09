


DO
$$
DECLARE
    f record;
BEGIN
    FOR f IN SELECT table_name
			 FROM information_schema.views
		     WHERE table_schema NOT IN ('pg_catalog', 'information_schema')
		     AND table_name !~ '^pg_'
    loop
    	EXECUTE 'DROP VIEW ' || f.table_name || ' cascade;';
    end loop;
end;
$$;




DROP TABLE IF EXISTS image;
DROP TABLE IF EXISTS users_info;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS search_token;
DROP TABLE IF EXISTS device_config;
DROP TABLE IF EXISTS package_order;
DROP TABLE IF EXISTS product_order;
DROP TABLE IF EXISTS product_unit_order;
DROP TABLE IF EXISTS user_device;
DROP TABLE IF EXISTS user_fcm;
DROP TABLE IF EXISTS user_address;
DROP TABLE IF EXISTS user_package;
DROP TABLE IF EXISTS user_package_detail;
DROP TABLE IF EXISTS voucher_relate_user_device;
DROP TABLE IF EXISTS voucher_relate_product;
DROP TABLE IF EXISTS voucher;
DROP TABLE IF EXISTS product_view_count;
DROP TABLE IF EXISTS notification;
DROP TABLE IF EXISTS notification_relate_user_device;
DROP TABLE IF EXISTS shipping_provider;

DROP TABLE IF EXISTS product_combo_item;
DROP TABLE IF EXISTS product_unit;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS product_import;
DROP TABLE IF EXISTS group_import;

DROP TABLE IF EXISTS store;

DROP TABLE IF EXISTS buyer;

DROP TABLE IF EXISTS payment_transaction;

DROP TABLE IF EXISTS table_detail;
DROP TABLE IF EXISTS area;
DROP TABLE IF EXISTS tokens;
DROP TABLE IF EXISTS debt_transaction;
DROP TABLE IF EXISTS delete_request;
