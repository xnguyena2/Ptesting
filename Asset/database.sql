
CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, username VARCHAR, password VARCHAR, active BOOL, roles VARCHAR, createby VARCHAR, createat TIMESTAMP);


CREATE TABLE IF NOT EXISTS search_token (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, product_second_id VARCHAR, tokens TSVECTOR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS product (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, product_second_id VARCHAR, name VARCHAR, detail TEXT, category VARCHAR, meta_search TEXT, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS product_unit (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, product_unit_second_id VARCHAR, product_second_id VARCHAR, name VARCHAR, price float8, discount float8, date_expire TIMESTAMP, volumetric float8, weight float8, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS image (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, imgid VARCHAR, tag VARCHAR, thumbnail VARCHAR, medium VARCHAR, large VARCHAR, category VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS device_config (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, color VARCHAR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS package_order (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, package_order_second_id VARCHAR, user_device_id VARCHAR, voucher_second_id VARCHAR, reciver_address VARCHAR, region_id INTEGER, district_id INTEGER, ward_id INTEGER, reciver_fullname VARCHAR, phone_number VARCHAR, phone_number_clean VARCHAR, total_price float8, real_price float8, ship_price float8, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS product_order (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, name VARCHAR, package_order_second_id VARCHAR, product_second_id VARCHAR, voucher_second_id VARCHAR, total_price float8, ship_price float8, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS product_unit_order (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, name VARCHAR, package_order_second_id VARCHAR, product_second_id VARCHAR, product_unit_second_id VARCHAR, price float8, total_discount float8, number_unit INTEGER, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS user_device (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, device_id VARCHAR, user_first_name VARCHAR, user_last_name VARCHAR, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS user_fcm (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, device_id VARCHAR, fcm_id VARCHAR, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS user_address (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, address_id VARCHAR, device_id VARCHAR, reciver_fullname VARCHAR, phone_number VARCHAR, house_number VARCHAR, region INTEGER, district INTEGER, ward INTEGER, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS user_package (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, device_id VARCHAR, product_second_id VARCHAR, product_unit_second_id VARCHAR, number_unit INTEGER, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS voucher (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, voucher_second_id VARCHAR, detail VARCHAR, discount float8, amount float8, reuse INTEGER, for_all_product BOOLEAN, for_all_user BOOLEAN, package_voucher BOOLEAN, date_expire TIMESTAMP, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS voucher_relate_user_device (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, voucher_second_id VARCHAR, reuse INTEGER, device_id VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS voucher_relate_product (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, voucher_second_id VARCHAR, product_second_id VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS product_view_count (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, product_second_id VARCHAR, device_id VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS notification (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, notification_second_id VARCHAR, title VARCHAR, detail VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS notification_relate_user_device (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, notification_second_id VARCHAR, user_device_id VARCHAR, createat TIMESTAMP, status VARCHAR);
CREATE TABLE IF NOT EXISTS shipping_provider (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, provider_id VARCHAR, name VARCHAR, config TEXT, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS product_import (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, product_import_second_id VARCHAR, product_id VARCHAR, product_name VARCHAR, price float8, amount float8, detail TEXT, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS store (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, name VARCHAR, status VARCHAR, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS buyer (id SERIAL PRIMARY KEY, group_id VARCHAR NOT NULL, device_id VARCHAR, reciver_address VARCHAR, region_id INTEGER, district_id INTEGER, ward_id INTEGER, reciver_fullname VARCHAR, phone_number VARCHAR, phone_number_clean VARCHAR, total_price float8, real_price float8, ship_price float8, status VARCHAR, createat TIMESTAMP);

CREATE INDEX search_token_index ON search_token(tokens);
CREATE INDEX product_index ON product(product_second_id);
CREATE INDEX product_detail_index ON product(detail);
CREATE INDEX users_name_index ON users(username);
CREATE INDEX product_import_index ON product_import(product_import_second_id);
CREATE INDEX user_device_index ON user_device(device_id);
CREATE INDEX store_index ON store(group_id);
CREATE INDEX buyer_index ON buyer(device_id);

ALTER TABLE users ADD CONSTRAINT UQ_users_name UNIQUE(username);
ALTER TABLE product ADD CONSTRAINT UQ_product_second_id UNIQUE(group_id, product_second_id);
ALTER TABLE product_unit ADD CONSTRAINT UQ_product_unit_second_id UNIQUE(group_id, product_unit_second_id);
ALTER TABLE search_token ADD CONSTRAINT UQ_search_token_product_second_id UNIQUE(group_id, product_second_id);
ALTER TABLE user_fcm ADD CONSTRAINT UQ_user_fcm_device_id UNIQUE(group_id, device_id);
ALTER TABLE voucher_relate_user_device ADD CONSTRAINT UQ_voucher_relate_user_device UNIQUE(group_id, voucher_second_id, device_id);
ALTER TABLE product_import ADD CONSTRAINT UQ_product_import_second_id UNIQUE(group_id, product_import_second_id);
ALTER TABLE user_device ADD CONSTRAINT UQ_user_device UNIQUE(group_id, device_id);
ALTER TABLE store ADD CONSTRAINT UQ_store UNIQUE(group_id);
ALTER TABLE buyer ADD CONSTRAINT UQ_buyer UNIQUE(group_id, device_id);

ALTER TABLE product_unit ADD CONSTRAINT FK_product_unit FOREIGN KEY(group_id, product_second_id) REFERENCES product(group_id, product_second_id) ON DELETE CASCADE;
ALTER TABLE search_token ADD CONSTRAINT FK_search_token FOREIGN KEY(group_id, product_second_id) REFERENCES product(group_id, product_second_id) ON DELETE CASCADE;

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

CREATE OR REPLACE FUNCTION add_buyer()
  RETURNS TRIGGER
  LANGUAGE PLPGSQL
  AS
$$
BEGIN

	INSERT INTO
    	buyer (group_id , device_id, reciver_address, region_id, district_id, ward_id, reciver_fullname, phone_number, phone_number_clean, total_price, real_price, ship_price, status, createat)
    VALUES(NEW.group_id, NEW.user_device_id, NEW.reciver_address, NEW.region_id, NEW.district_id, NEW.ward_id, NEW.reciver_fullname, NEW.phone_number, NEW.phone_number_clean, NEW.total_price, NEW.real_price, NEW.ship_price, NULL, NOW()) ON CONFLICT (group_id, device_id) DO
    UPDATE
    SET
    	createat = NOW(),
    	total_price = buyer.total_price + NEW.total_price,
        real_price = buyer.real_price + NEW.real_price,
        ship_price = buyer.ship_price + NEW.ship_price;

	RETURN NEW;
END;
$$;


CREATE OR REPLACE TRIGGER update_buyer AFTER INSERT ON package_order FOR EACH ROW EXECUTE PROCEDURE add_buyer();