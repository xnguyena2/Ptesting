
CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, username VARCHAR, password VARCHAR, active BOOL, roles VARCHAR, createby VARCHAR, phone_number VARCHAR, phone_number_clean VARCHAR, status VARCHAR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS users_info (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, username VARCHAR, user_fullname VARCHAR, phone VARCHAR, title VARCHAR, roles VARCHAR, createat TIMESTAMP);


CREATE TABLE IF NOT EXISTS search_token (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, product_second_id VARCHAR, tokens TSVECTOR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS product (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, product_second_id VARCHAR, name VARCHAR, detail TEXT, category VARCHAR, unit_category_config VARCHAR, meta_search TEXT, visible_web BOOL, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS product_unit (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, product_second_id VARCHAR, product_unit_second_id VARCHAR, name VARCHAR, sku VARCHAR, upc VARCHAR, buy_price float8, price float8, promotional_price float8, inventory_number INTEGER, wholesale_price float8, wholesale_number INTEGER, discount float8, date_expire TIMESTAMP, volumetric float8, weight float8, visible BOOL, enable_warehouse BOOL, status VARCHAR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS image (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, imgid VARCHAR, tag VARCHAR, thumbnail VARCHAR, medium VARCHAR, large VARCHAR, category VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS device_config (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, color VARCHAR, categorys VARCHAR, config TEXT, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS package_order (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, package_order_second_id VARCHAR, user_device_id VARCHAR, voucher_second_id VARCHAR, reciver_address VARCHAR, region_id INTEGER, district_id INTEGER, ward_id INTEGER, reciver_fullname VARCHAR, phone_number VARCHAR, phone_number_clean VARCHAR, total_price float8, real_price float8, ship_price float8, discount float8, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS product_order (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, name VARCHAR, package_order_second_id VARCHAR, product_second_id VARCHAR, voucher_second_id VARCHAR, total_price float8, ship_price float8, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS product_unit_order (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, name VARCHAR, package_order_second_id VARCHAR, product_second_id VARCHAR, product_unit_second_id VARCHAR, price float8, total_discount float8, number_unit INTEGER, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS user_device (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, device_id VARCHAR, user_first_name VARCHAR, user_last_name VARCHAR, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS user_fcm (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, device_id VARCHAR, fcm_id VARCHAR, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS user_address (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, address_id VARCHAR, device_id VARCHAR, reciver_fullname VARCHAR, phone_number VARCHAR, house_number VARCHAR, region INTEGER, district INTEGER, ward INTEGER, status VARCHAR, createat TIMESTAMP);


CREATE TABLE IF NOT EXISTS user_package_detail (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, package_second_id VARCHAR, device_id VARCHAR, staff_id VARCHAR, package_type VARCHAR, area_id VARCHAR, area_name VARCHAR, table_id VARCHAR, table_name VARCHAR, voucher VARCHAR, price float8, payment float8, discount_amount float8, discount_percent float8, discount_promotional float8, discount_by_point float8, additional_fee float8, additional_config VARCHAR, ship_price float8, cost float8, profit float8, point INTEGER, note VARCHAR, image VARCHAR, progress VARCHAR, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS user_package (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, package_second_id VARCHAR, device_id VARCHAR, product_second_id VARCHAR, product_unit_second_id VARCHAR, number_unit INTEGER, buy_price float8, price float8, discount_amount float8, discount_percent float8, discount_promotional float8, note VARCHAR, status VARCHAR, createat TIMESTAMP);


CREATE TABLE IF NOT EXISTS voucher (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, voucher_second_id VARCHAR, detail VARCHAR, discount float8, amount float8, reuse INTEGER, for_all_product BOOLEAN, for_all_user BOOLEAN, package_voucher BOOLEAN, date_expire TIMESTAMP, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS voucher_relate_user_device (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, voucher_second_id VARCHAR, reuse INTEGER, device_id VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS voucher_relate_product (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, voucher_second_id VARCHAR, product_second_id VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS product_view_count (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, product_second_id VARCHAR, device_id VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS notification (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, notification_second_id VARCHAR, title VARCHAR, detail VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS notification_relate_user_device (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, notification_second_id VARCHAR, user_device_id VARCHAR, createat TIMESTAMP, status VARCHAR);
CREATE TABLE IF NOT EXISTS shipping_provider (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, provider_id VARCHAR, name VARCHAR, config TEXT, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS group_import (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, group_import_second_id VARCHAR, supplier_id VARCHAR, total_price float8, total_amount INTEGER, discount_amount float8, additional_fee float8, note TEXT, images VARCHAR, type VARCHAR, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS product_import (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, group_import_second_id VARCHAR, product_import_second_id VARCHAR, product_second_id VARCHAR, product_unit_second_id VARCHAR, product_unit_name_category VARCHAR, price float8, amount INTEGER, note TEXT, type VARCHAR, status VARCHAR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS store (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, name VARCHAR, time_open VARCHAR, address VARCHAR, phone VARCHAR, domain_url VARCHAR, status VARCHAR, store_type VARCHAR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS buyer (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, device_id VARCHAR, reciver_address VARCHAR, region_id INTEGER, district_id INTEGER, ward_id INTEGER, reciver_fullname VARCHAR, phone_number VARCHAR, phone_number_clean VARCHAR, total_price float8, real_price float8, ship_price float8, discount float8, point INTEGER, status VARCHAR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS payment_transaction (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, transaction_second_id VARCHAR NOT NULL, device_id VARCHAR, package_second_id VARCHAR, action_id VARCHAR, action_type VARCHAR, transaction_type VARCHAR, amount float8, category VARCHAR, money_source VARCHAR, note VARCHAR, status VARCHAR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS table_detail (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, area_id VARCHAR, table_id VARCHAR, table_name VARCHAR, package_second_id VARCHAR, detail VARCHAR, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS area (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, area_id VARCHAR, area_name VARCHAR, detail VARCHAR, meta_search VARCHAR, status VARCHAR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS tokens (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, token_second_id VARCHAR, token VARCHAR, status VARCHAR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS delete_request (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, delete_request_id VARCHAR, user_id VARCHAR, status VARCHAR, createat TIMESTAMP);

CREATE INDEX device_config_index ON device_config(group_id);
CREATE INDEX users_name_index ON users(username);
CREATE INDEX users_info_name_index ON users_info(username);
CREATE INDEX search_token_index ON search_token(tokens);
CREATE INDEX product_index ON product(product_second_id);
CREATE INDEX product_detail_index ON product(detail);
CREATE INDEX product_unit_index ON product_unit(product_unit_second_id);
CREATE INDEX product_unit_sku_index ON product_unit(sku);
CREATE INDEX product_unit_upc_index ON product_unit(upc);
CREATE INDEX group_import_index ON group_import(group_import_second_id);
CREATE INDEX product_import_index ON product_import(product_import_second_id);
CREATE INDEX product_import_product_second_id_index ON product_import(product_second_id);
CREATE INDEX product_import_product_unit_second_id_index ON product_import(product_unit_second_id);
CREATE INDEX user_device_index ON user_device(device_id);
CREATE INDEX user_package_detail_index ON user_package_detail(package_second_id);
CREATE INDEX user_package_index ON user_package(package_second_id);
CREATE INDEX store_index ON store(group_id);
CREATE INDEX store_domain_url_index ON store(domain_url);
CREATE INDEX buyer_index ON buyer(device_id);
CREATE INDEX buyer_phone_number_clean_index ON buyer(phone_number_clean);
CREATE INDEX voucher_index ON voucher(voucher_second_id);
CREATE INDEX voucher_relate_user_device_index ON voucher_relate_user_device(voucher_second_id);
CREATE INDEX voucher_relate_product_index ON voucher_relate_product(voucher_second_id);
CREATE INDEX notification_index ON notification(notification_second_id);
CREATE INDEX notification_relate_user_device_index ON notification_relate_user_device(user_device_id);
CREATE INDEX payment_transaction_index ON payment_transaction(transaction_second_id);
CREATE INDEX payment_transaction_action_index ON payment_transaction(action_id);
CREATE INDEX payment_transaction_createat_index ON payment_transaction(createat);
CREATE INDEX payment_transaction_group_index ON payment_transaction(group_id);
CREATE INDEX table_detail_index ON table_detail(table_id);
CREATE INDEX table_detail_area_index ON table_detail(area_id);
CREATE INDEX area_group_index ON area(group_id);
CREATE INDEX area_index ON area(area_name);
CREATE INDEX area_search_index ON area(meta_search);
CREATE INDEX tokens_group_index ON tokens(group_id);
CREATE INDEX tokens_second_id_index ON tokens(token_second_id);
CREATE INDEX delete_request_user_id_index ON delete_request(user_id);

ALTER TABLE device_config ADD CONSTRAINT UQ_device_config UNIQUE(group_id);
ALTER TABLE users ADD CONSTRAINT UQ_users_name UNIQUE(username);
ALTER TABLE users_info ADD CONSTRAINT UQ_users_info_name UNIQUE(username);
ALTER TABLE product ADD CONSTRAINT UQ_product_second_id UNIQUE(group_id, product_second_id);
ALTER TABLE product_unit ADD CONSTRAINT UQ_product_unit_second_id UNIQUE(group_id, product_second_id, product_unit_second_id);
ALTER TABLE product_unit ADD CONSTRAINT UQ_product_unit_sku UNIQUE(group_id, sku);
ALTER TABLE product_unit ADD CONSTRAINT UQ_product_unit_upc UNIQUE(group_id, upc);
ALTER TABLE search_token ADD CONSTRAINT UQ_search_token_product_second_id UNIQUE(group_id, product_second_id);
ALTER TABLE user_fcm ADD CONSTRAINT UQ_user_fcm_device_id UNIQUE(group_id, device_id);
ALTER TABLE voucher ADD CONSTRAINT UQ_voucher UNIQUE(group_id, voucher_second_id);
ALTER TABLE voucher_relate_user_device ADD CONSTRAINT UQ_voucher_relate_user_device UNIQUE(group_id, voucher_second_id, device_id);
ALTER TABLE group_import ADD CONSTRAINT UQ_group_import_second_id UNIQUE(group_id, group_import_second_id);
ALTER TABLE product_import ADD CONSTRAINT UQ_product_import_second_id UNIQUE(group_id, group_import_second_id, product_import_second_id);
ALTER TABLE user_device ADD CONSTRAINT UQ_user_device UNIQUE(group_id, device_id);
ALTER TABLE store ADD CONSTRAINT UQ_store UNIQUE(group_id);
ALTER TABLE store ADD CONSTRAINT UQ_domain_url UNIQUE(domain_url);
ALTER TABLE buyer ADD CONSTRAINT UQ_buyer UNIQUE(group_id, device_id);
ALTER TABLE user_package_detail ADD CONSTRAINT UQ_user_package_detail UNIQUE(group_id, package_second_id);
ALTER TABLE user_package ADD CONSTRAINT UQ_user_package UNIQUE(group_id, package_second_id, product_second_id, product_unit_second_id);
ALTER TABLE payment_transaction ADD CONSTRAINT UQ_payment_transaction UNIQUE(transaction_second_id);
ALTER TABLE payment_transaction ADD CONSTRAINT UQ_group_payment_transaction UNIQUE(group_id, transaction_second_id);
ALTER TABLE table_detail ADD CONSTRAINT UQ_table_detail UNIQUE(group_id, area_id, table_id);
ALTER TABLE area ADD CONSTRAINT UQ_area UNIQUE(group_id, area_id);
ALTER TABLE tokens ADD CONSTRAINT UQ_tokens UNIQUE(token_second_id);
ALTER TABLE delete_request ADD CONSTRAINT UQ_delete_request UNIQUE(user_id);

ALTER TABLE product_unit ADD CONSTRAINT FK_product_unit FOREIGN KEY(group_id, product_second_id) REFERENCES product(group_id, product_second_id) ON DELETE CASCADE;
ALTER TABLE search_token ADD CONSTRAINT FK_search_token FOREIGN KEY(group_id, product_second_id) REFERENCES product(group_id, product_second_id) ON DELETE CASCADE;
ALTER TABLE user_package ADD CONSTRAINT FK_user_package FOREIGN KEY(group_id, package_second_id) REFERENCES user_package_detail(group_id, package_second_id) ON DELETE CASCADE;
ALTER TABLE voucher_relate_user_device ADD CONSTRAINT FK_voucher_relate_user_device FOREIGN KEY(group_id, voucher_second_id) REFERENCES voucher(group_id, voucher_second_id) ON DELETE CASCADE;
ALTER TABLE voucher_relate_product ADD CONSTRAINT FK_voucher_relate_product FOREIGN KEY(group_id, voucher_second_id) REFERENCES voucher(group_id, voucher_second_id) ON DELETE CASCADE;
ALTER TABLE table_detail ADD CONSTRAINT FK_table_detail FOREIGN KEY(group_id, area_id) REFERENCES area(group_id, area_id) ON DELETE CASCADE;
ALTER TABLE users_info ADD CONSTRAINT FK_users_info FOREIGN KEY(username) REFERENCES users(username) ON DELETE CASCADE;

create or replace function getRoleIndex(roles VARCHAR)
returns int
language plpgsql
as
$$
declare
   roleIndex int;
begin

	if roles = 'ROLE_ROOT' then
		roleIndex := 0;
	elsif roles = 'ROLE_ADMIN' then
		roleIndex := 1;
	elsif roles = 'ROLE_USER' then
		roleIndex := 2;
	else
		roleIndex := -1;
	end if;

   return roleIndex;
end;
$$;

create or replace function checkRole(creator VARCHAR, newacc VARCHAR)
returns boolean
language plpgsql
as
$$
declare
   creatorRole VARCHAR;
   newaccRole VARCHAR;

   creatorRoleIndex int;
   newaccRoleIndex int;
begin
   select translate(roles,'{}','')
   into creatorRole
   from users
   where username=creator;

   select translate(roles,'{}','')
   into newaccRole
   from users
   where username=newacc;

   select getRoleIndex(creatorRole) into creatorRoleIndex;
   select getRoleIndex(newaccRole) into newaccRoleIndex;


	if creatorRoleIndex = -1 OR newaccRoleIndex = -1 then
		return false;
	end if;

   return creatorRoleIndex < newaccRoleIndex;
end;
$$;

create or replace function query_product (
  product_group_id VARCHAR, where_condiction VARCHAR
)
	returns table (id INTEGER, group_id VARCHAR, product_second_id VARCHAR, name VARCHAR, detail TEXT, category VARCHAR, meta_search TEXT, status VARCHAR, createat TIMESTAMP)
	language plpgsql
as
$$
begin
	RETURN QUERY EXECUTE 'SELECT * FROM product_' || product_group_id || ' ' || where_condiction;
end;
$$;


create or replace function create_product_view (
  group_id VARCHAR
)
	returns void
	language plpgsql
as
$$
begin
	EXECUTE 'CREATE VIEW product_' || group_id || ' AS SELECT * FROM product WHERE product.group_id = ' || quote_literal (group_id);
end;
$$;


create or replace function delete_all_data_belong_user (
  by_group_id VARCHAR
)
	returns boolean
	language plpgsql
as
$$
begin

    DELETE FROM image WHERE group_id = by_group_id;
    DELETE FROM users_info WHERE group_id = by_group_id;
    DELETE FROM users WHERE group_id = by_group_id;
    DELETE FROM search_token WHERE group_id = by_group_id;
    DELETE FROM device_config WHERE group_id = by_group_id;
    DELETE FROM package_order WHERE group_id = by_group_id;
    DELETE FROM product_order WHERE group_id = by_group_id;
    DELETE FROM product_unit_order WHERE group_id = by_group_id;
    DELETE FROM user_device WHERE group_id = by_group_id;
    DELETE FROM user_fcm WHERE group_id = by_group_id;
    DELETE FROM user_address WHERE group_id = by_group_id;
    DELETE FROM user_package WHERE group_id = by_group_id;
    DELETE FROM user_package_detail WHERE group_id = by_group_id;
    DELETE FROM voucher_relate_user_device WHERE group_id = by_group_id;
    DELETE FROM voucher_relate_product WHERE group_id = by_group_id;
    DELETE FROM voucher WHERE group_id = by_group_id;
    DELETE FROM product_view_count WHERE group_id = by_group_id;
    DELETE FROM notification WHERE group_id = by_group_id;
    DELETE FROM notification_relate_user_device WHERE group_id = by_group_id;
    DELETE FROM shipping_provider WHERE group_id = by_group_id;

    DELETE FROM product_unit WHERE group_id = by_group_id;
    DELETE FROM product WHERE group_id = by_group_id;
    DELETE FROM product_import WHERE group_id = by_group_id;
    DELETE FROM group_import WHERE group_id = by_group_id;

    DELETE FROM store WHERE group_id = by_group_id;

    DELETE FROM buyer WHERE group_id = by_group_id;

    DELETE FROM payment_transaction WHERE group_id = by_group_id;

    DELETE FROM table_detail WHERE group_id = by_group_id;
    DELETE FROM area WHERE group_id = by_group_id;
    DELETE FROM tokens WHERE group_id = by_group_id;

    RETURN TRUE;
end;
$$;

CREATE OR REPLACE FUNCTION add_buyer()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
BEGIN

	INSERT INTO
    	buyer (group_id , device_id, reciver_address, region_id, district_id, ward_id, reciver_fullname, phone_number, phone_number_clean, total_price, real_price, ship_price, discount, point, status, createat)
    VALUES(NEW.group_id, NEW.user_device_id, NEW.reciver_address, NEW.region_id, NEW.district_id, NEW.ward_id, NEW.reciver_fullname, NEW.phone_number, NEW.phone_number_clean, NEW.total_price, NEW.real_price, NEW.ship_price, NEW.discount, 0, NULL, NOW()) ON CONFLICT (group_id, device_id) DO
    UPDATE
    SET
    	total_price = buyer.total_price + NEW.total_price,
        real_price = buyer.real_price + NEW.real_price,
        ship_price = buyer.ship_price + NEW.ship_price,
        discount = buyer.discount + NEW.discount;

	RETURN NEW;
END;
$$;


CREATE OR REPLACE TRIGGER update_buyer AFTER INSERT ON package_order FOR EACH ROW EXECUTE PROCEDURE add_buyer();



create or replace function delete_all_data_belong_user_package_detail (
  by_group_id VARCHAR, by_package_second_id VARCHAR
)
	returns void
	language plpgsql
as
$$
begin

    DELETE FROM user_package_detail WHERE group_id = by_group_id AND package_second_id = by_package_second_id;
    DELETE FROM user_package WHERE group_id = by_group_id AND package_second_id = by_package_second_id;
    DELETE FROM payment_transaction WHERE group_id = by_group_id AND (package_second_id = by_package_second_id OR action_id = by_package_second_id);

end;
$$;

CREATE OR REPLACE FUNCTION decrese_product_unit_inventory()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
DECLARE
   _inventory_number INTEGER;
   _enable_warehouse BOOL;
BEGIN

    IF NEW.status = 'WEB_TEMP' OR NEW.status = 'WEB_SUBMIT'
    THEN
	    RETURN NEW;
    END IF;


    SELECT enable_warehouse, inventory_number
    INTO _enable_warehouse, _inventory_number
    FROM product_unit
    WHERE NEW.group_id = product_unit.group_id AND NEW.product_second_id = product_unit.product_second_id AND NEW.product_unit_second_id = product_unit.product_unit_second_id;

    IF _enable_warehouse <> TRUE OR _enable_warehouse IS NULL
    THEN
	    RETURN NEW;
    END IF;

    IF _inventory_number < NEW.number_unit
    THEN
--        PERFORM delete_all_data_belong_user_package_detail(NEW.group_id, NEW.package_second_id);
	    RAISE EXCEPTION 'inventory_number small than number_unit';
    END IF;

	UPDATE product_unit
    SET
    	inventory_number = product_unit.inventory_number - NEW.number_unit
    WHERE NEW.status <> 'RETURN' AND NEW.status <> 'CANCEL' AND NEW.group_id = product_unit.group_id AND NEW.product_second_id = product_unit.product_second_id AND NEW.product_unit_second_id = product_unit.product_unit_second_id AND product_unit.enable_warehouse = TRUE;

	RETURN NEW;
END;
$$;


CREATE OR REPLACE FUNCTION increase_product_unit_inventory()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
BEGIN

    IF OLD.status = 'WEB_TEMP' OR OLD.status = 'WEB_SUBMIT'
    THEN
	    RETURN OLD;
    END IF;

	UPDATE product_unit
    SET
    	inventory_number = product_unit.inventory_number + OLD.number_unit
    WHERE OLD.status <> 'RETURN' AND OLD.status <> 'CANCEL' AND OLD.group_id = product_unit.group_id AND OLD.product_second_id = product_unit.product_second_id AND OLD.product_unit_second_id = product_unit.product_unit_second_id AND product_unit.enable_warehouse = TRUE;

	RETURN OLD;
END;
$$;


CREATE OR REPLACE FUNCTION update_product_unit_inventory()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
DECLARE
   _inventory_number INTEGER;
   _inventory_number_new INTEGER;
   _enable_warehouse BOOL;
BEGIN


    IF NEW.status = 'WEB_TEMP' OR NEW.status = 'WEB_SUBMIT'
    THEN
	    RETURN NEW;
    END IF;

    
    SELECT enable_warehouse, inventory_number
    INTO _enable_warehouse, _inventory_number
    FROM product_unit
    WHERE NEW.group_id = product_unit.group_id AND NEW.product_second_id = product_unit.product_second_id AND NEW.product_unit_second_id = product_unit.product_unit_second_id;

    IF _enable_warehouse <> TRUE OR _enable_warehouse IS NULL
    THEN
	    RETURN NEW;
    END IF;

    _inventory_number_new = CASE
                        WHEN OLD.status <> 'RETURN' AND OLD.status <> 'CANCEL' AND NEW.status <> 'RETURN' AND NEW.status <> 'CANCEL' THEN _inventory_number + OLD.number_unit - NEW.number_unit
                        WHEN OLD.status <> NEW.status AND OLD.status <> 'RETURN' AND OLD.status <> 'CANCEL' AND (NEW.status = 'RETURN' OR NEW.status = 'CANCEL') THEN _inventory_number + OLD.number_unit
                        WHEN OLD.status <> NEW.status AND (OLD.status = 'RETURN' OR OLD.status = 'CANCEL') AND NEW.status <> 'RETURN' AND NEW.status = 'CANCEL' THEN _inventory_number - OLD.number_unit
--                        WHEN (OLD.status = 'RETURN' OR OLD.status <> 'CANCEL') AND (NEW.status = 'RETURN' OR NEW.status <> 'CANCEL') THEN ???
                        ELSE _inventory_number
                       END;
    IF _inventory_number_new < 0
    THEN
--        PERFORM delete_all_data_belong_user_package_detail(NEW.group_id, NEW.package_second_id);
	    RAISE EXCEPTION 'inventory_number_new small than number_unit';
    END IF;

	UPDATE product_unit
    SET
    	inventory_number = _inventory_number_new
    WHERE NEW.group_id = product_unit.group_id AND NEW.product_second_id = product_unit.product_second_id AND NEW.product_unit_second_id = product_unit.product_unit_second_id AND product_unit.enable_warehouse = TRUE;

	RETURN NEW;
END;
$$;


CREATE OR REPLACE TRIGGER decrese_product_unit_inventory AFTER INSERT ON user_package FOR EACH ROW EXECUTE PROCEDURE decrese_product_unit_inventory();
CREATE OR REPLACE TRIGGER increase_product_unit_inventory AFTER DELETE ON user_package FOR EACH ROW EXECUTE PROCEDURE increase_product_unit_inventory();
CREATE OR REPLACE TRIGGER update_product_unit_inventory AFTER UPDATE ON user_package FOR EACH ROW EXECUTE PROCEDURE update_product_unit_inventory();



CREATE OR REPLACE FUNCTION delete_user_package_detail_decrese_buyer()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
BEGIN

    IF NEW.status = 'WEB_TEMP' OR NEW.status = 'WEB_SUBMIT'
    THEN
	    RETURN OLD;
    END IF;


--  reset table
    UPDATE table_detail SET package_second_id = NULL WHERE table_detail.group_id = OLD.group_id AND package_second_id = OLD.package_second_id;


--  delete all payment_transaction belong to user_package_detail
    DELETE FROM payment_transaction WHERE group_id = OLD.group_id AND (package_second_id = OLD.package_second_id OR action_id = OLD.package_second_id) AND package_second_id IS NOT NULL;

    IF OLD.status <> 'DONE'
    THEN
	    RETURN OLD;
    END IF;

	UPDATE buyer
    SET
        total_price = buyer.total_price - OLD.price,
        real_price = buyer.real_price - OLD.payment,
        ship_price = buyer.ship_price - OLD.ship_price,
        discount = buyer.discount - (OLD.discount_amount + (OLD.discount_percent / 100) * OLD.price),
        point = buyer.point - OLD.point
    WHERE OLD.status = 'DONE' AND OLD.group_id = buyer.group_id AND OLD.device_id = buyer.device_id;

	RETURN OLD;
END;
$$;


CREATE OR REPLACE FUNCTION trigger_on_update_user_package_detail()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
BEGIN

    IF NEW.status = 'WEB_TEMP' OR NEW.status = 'WEB_SUBMIT'
    THEN
	    RETURN NEW;
    END IF;

--  set table for user_packge_detail
    IF OLD.table_id <> NEW.table_id
    THEN

        UPDATE table_detail SET package_second_id = NULL WHERE table_detail.group_id = NEW.group_id AND package_second_id = NEW.package_second_id;
        UPDATE table_detail SET package_second_id = NEW.package_second_id, createat = NOW() WHERE table_detail.group_id = NEW.group_id AND table_detail.table_id = NEW.table_id;

    END IF;

--  update payment transaction
    IF (OLD.status = 'CANCEL' OR OLD.status = 'RETURN') AND (NEW.status <> 'CANCEL' AND NEW.status <> 'RETURN')
    THEN
        INSERT INTO payment_transaction( group_id, transaction_second_id, device_id, package_second_id, action_id, action_type, transaction_type, amount, category, money_source, note, status, createat ) VALUES ( NEW.group_id, gen_random_uuid(), NEW.device_id, NEW.package_second_id, NEW.package_second_id, 'PAYMENT_ORDER', 'INCOME', NEW.payment, 'SELLING', NULL, CONCAT('REBUY-', NEW.package_second_id), 'CREATE', NOW() );
    ELSEIF (OLD.status <> 'CANCEL' AND OLD.status <> 'RETURN') AND (NEW.status = 'CANCEL' OR NEW.status = 'RETURN')
    THEN
        DELETE FROM payment_transaction WHERE group_id = NEW.group_id AND (package_second_id = NEW.package_second_id OR action_id = NEW.package_second_id) AND package_second_id IS NOT NULL AND action_id IS NOT NULL;
    ELSEIF NEW.payment - OLD.payment > 0 AND (NEW.status <> 'CANCEL' OR NEW.status <> 'RETURN')
    THEN

        INSERT INTO payment_transaction( group_id, transaction_second_id, device_id, package_second_id, action_id, action_type, transaction_type, amount, category, money_source, note, status, createat )
            VALUES ( NEW.group_id, gen_random_uuid(), NEW.device_id, NEW.package_second_id, NEW.package_second_id, 'PAYMENT_ORDER', 'INCOME', NEW.payment - OLD.payment, 'SELLING', NULL, CONCAT(CASE WHEN NEW.status = 'DONE' THEN 'DONE-' ELSE 'PAYMENT-' END, NEW.package_second_id), 'CREATE', NOW() );

    END IF;

	RETURN NEW;
END;
$$;


CREATE OR REPLACE FUNCTION trigger_on_insert_user_package_detail()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
BEGIN

    IF NEW.status = 'WEB_TEMP' OR NEW.status = 'WEB_SUBMIT'
    THEN
	    RETURN NEW;
    END IF;

--  set table for user_packge_detail
    IF NEW.table_id IS NOT NULL
    THEN

        UPDATE table_detail SET package_second_id = NULL WHERE table_detail.group_id = NEW.group_id AND package_second_id = NEW.package_second_id;
        UPDATE table_detail SET package_second_id = NEW.package_second_id, createat = NOW() WHERE table_detail.group_id = NEW.group_id AND table_detail.table_id = NEW.table_id;

    END IF;

--  add to payment transaction
    IF NEW.payment > 0 AND (NEW.status <> 'CANCEL' OR NEW.status <> 'RETURN')
    THEN

        INSERT INTO payment_transaction( group_id, transaction_second_id, device_id, package_second_id, action_id, action_type, transaction_type, amount, category, money_source, note, status, createat )
            VALUES ( NEW.group_id, gen_random_uuid(), NEW.device_id, NEW.package_second_id, NEW.package_second_id, 'PAYMENT_ORDER', 'INCOME', NEW.payment, 'SELLING', NULL, CONCAT(CASE WHEN NEW.status = 'DONE' THEN 'DONE-' ELSE 'PAYMENT-' END, NEW.package_second_id), 'CREATE', NOW() );

    END IF;


	RETURN NEW;
END;
$$;

CREATE OR REPLACE TRIGGER delete_user_package_detail_decrese_buyer AFTER DELETE ON user_package_detail FOR EACH ROW EXECUTE PROCEDURE delete_user_package_detail_decrese_buyer();
CREATE OR REPLACE TRIGGER trigger_on_update_user_package_detail AFTER UPDATE ON user_package_detail FOR EACH ROW EXECUTE PROCEDURE trigger_on_update_user_package_detail();
CREATE OR REPLACE TRIGGER trigger_on_insert_user_package_detail AFTER INSERT ON user_package_detail FOR EACH ROW EXECUTE PROCEDURE trigger_on_insert_user_package_detail();




CREATE OR REPLACE FUNCTION trigger_on_insert_product_import()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
DECLARE
   _inventory_number INTEGER;
   _inventory_number_new INTEGER;
   _buy_price float8;
   _buy_price_new float8;
   _enable_warehouse BOOL;
BEGIN

--  return should update status not insert
    IF NEW.status = 'RETURN'
    THEN
	    RETURN NEW;
    END IF;

    SELECT enable_warehouse, inventory_number, buy_price
    INTO _enable_warehouse, _inventory_number, _buy_price
    FROM product_unit
    WHERE NEW.group_id = product_unit.group_id AND NEW.product_second_id = product_unit.product_second_id AND NEW.product_unit_second_id = product_unit.product_unit_second_id;


    IF _enable_warehouse <> TRUE OR _enable_warehouse IS NULL
    THEN
	    RAISE EXCEPTION 'product not enable warehouse';
    END IF;


    IF NEW.type = 'UN_KNOW' OR NEW.type IS NULL
    THEN
	    RAISE EXCEPTION 'unknow import type';
    END IF;


    _inventory_number_new = CASE
                        WHEN NEW.type = 'IMPORT' THEN _inventory_number + NEW.amount
                        WHEN NEW.type = 'EXPORT' THEN _inventory_number - NEW.amount
                        WHEN NEW.type = 'UPDATE_NUMBER' THEN NEW.amount
                        ELSE -1
                       END;

    IF _inventory_number_new = -1
    THEN
	    RAISE EXCEPTION 'unknow import type :-1';
    END IF;


    IF _inventory_number_new < 0
    THEN
	    RAISE EXCEPTION 'inventory_number_new small than 0!';
    END IF;

    _buy_price_new = _buy_price;

    IF _inventory_number_new > 0 AND NEW.type = 'IMPORT'
    THEN
        _buy_price_new = (_buy_price * _inventory_number + NEW.amount * NEW.price) / _inventory_number_new;
    END IF;

	UPDATE product_unit
    SET
    	inventory_number = _inventory_number_new,
    	buy_price = _buy_price_new
    WHERE NEW.group_id = product_unit.group_id AND NEW.product_second_id = product_unit.product_second_id AND NEW.product_unit_second_id = product_unit.product_unit_second_id AND product_unit.enable_warehouse = TRUE;

	RETURN NEW;
END;
$$;



CREATE OR REPLACE FUNCTION trigger_on_delete_product_import()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
DECLARE
   _inventory_number INTEGER;
   _inventory_number_new INTEGER;
   _buy_price float8;
   _buy_price_new float8;
   _enable_warehouse BOOL;
BEGIN


    IF OLD.type = 'UN_KNOW' OR OLD.type IS NULL OR OLD.status = 'RETURN'
    THEN
	    RETURN OLD;
    END IF;

    SELECT enable_warehouse, inventory_number, buy_price
    INTO _enable_warehouse, _inventory_number, _buy_price
    FROM product_unit
    WHERE OLD.group_id = product_unit.group_id AND OLD.product_second_id = product_unit.product_second_id AND OLD.product_unit_second_id = product_unit.product_unit_second_id;


    IF _enable_warehouse <> TRUE OR _enable_warehouse IS NULL
    THEN
	    RETURN OLD;
    END IF;


    _inventory_number_new = CASE
                        WHEN OLD.type = 'IMPORT' THEN _inventory_number - OLD.amount
                        WHEN OLD.type = 'EXPORT' THEN _inventory_number + OLD.amount
                        WHEN OLD.type = 'UPDATE_NUMBER' THEN 0
                        ELSE -1
                       END;

    IF _inventory_number_new = -1
    THEN
	    RAISE EXCEPTION 'unknow import type :-1';
    END IF;


    IF _inventory_number_new < 0
    THEN
	    RAISE EXCEPTION 'inventory_number_new small than 0!';
    END IF;

    _buy_price_new = _buy_price;

    IF _inventory_number_new > 0 AND NEW.type = 'IMPORT'
    THEN
        _buy_price_new = (_buy_price * _inventory_number - OLD.amount * OLD.price) / _inventory_number_new;
    END IF;

	UPDATE product_unit
    SET
    	inventory_number = _inventory_number_new,
        buy_price = _buy_price_new
    WHERE OLD.group_id = product_unit.group_id AND OLD.product_second_id = product_unit.product_second_id AND OLD.product_unit_second_id = product_unit.product_unit_second_id AND product_unit.enable_warehouse = TRUE;

	RETURN OLD;
END;
$$;



CREATE OR REPLACE FUNCTION trigger_on_update_product_import()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
DECLARE
   _inventory_number INTEGER;
   _inventory_number_new INTEGER;
   _buy_price float8;
   _buy_price_new float8;
   _enable_warehouse BOOL;
BEGIN

    IF OLD.status = 'RETURN'
    THEN
	    RAISE EXCEPTION 'can not update on RETURN!';
    END IF;


    IF OLD.amount = NEW.amount
    THEN
	    RETURN NEW;
    END IF;

    SELECT enable_warehouse, inventory_number, buy_price
    INTO _enable_warehouse, _inventory_number, _buy_price
    FROM product_unit
    WHERE NEW.group_id = product_unit.group_id AND NEW.product_second_id = product_unit.product_second_id AND NEW.product_unit_second_id = product_unit.product_unit_second_id;


    IF _enable_warehouse <> TRUE OR _enable_warehouse IS NULL
    THEN
	    RAISE EXCEPTION 'product not enable warehouse';
    END IF;


    _inventory_number_new = CASE
                        WHEN NEW.type = 'IMPORT' THEN _inventory_number + NEW.number_unit - OLD.number_unit
                        WHEN NEW.type = 'EXPORT' THEN _inventory_number - NEW.number_unit + OLD.number_unit
                        WHEN NEW.type = 'UPDATE_NUMBER' THEN NEW.number_unit
                        ELSE -1
                       END;

    IF _inventory_number_new = -1
    THEN
	    RAISE EXCEPTION 'unknow import type :-1';
    END IF;


    IF _inventory_number_new < 0
    THEN
	    RAISE EXCEPTION 'inventory_number_new small than 0!';
    END IF;

    _buy_price_new = _buy_price;

    IF _inventory_number_new > 0 AND NEW.type = 'IMPORT'
    THEN
        _buy_price_new = (_buy_price * _inventory_number + (NEW.number_unit - OLD.number_unit) * NEW.price) / _inventory_number_new;
    END IF;

	UPDATE product_unit
    SET
    	inventory_number = _inventory_number_new,
        buy_price = _buy_price_new
    WHERE NEW.group_id = product_unit.group_id AND NEW.product_second_id = product_unit.product_second_id AND NEW.product_unit_second_id = product_unit.product_unit_second_id AND product_unit.enable_warehouse = TRUE;


	RETURN NEW;
END;
$$;


CREATE OR REPLACE TRIGGER trigger_on_delete_product_import AFTER DELETE ON product_import FOR EACH ROW EXECUTE PROCEDURE trigger_on_delete_product_import();
CREATE OR REPLACE TRIGGER trigger_on_update_product_import AFTER UPDATE ON product_import FOR EACH ROW EXECUTE PROCEDURE trigger_on_update_product_import();
CREATE OR REPLACE TRIGGER trigger_on_insert_product_import AFTER INSERT ON product_import FOR EACH ROW EXECUTE PROCEDURE trigger_on_insert_product_import();




