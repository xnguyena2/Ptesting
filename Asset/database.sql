
CREATE TABLE IF NOT EXISTS search_token (id SERIAL PRIMARY KEY, beer_second_id VARCHAR, tokens TSVECTOR);
CREATE TABLE IF NOT EXISTS beer (id SERIAL PRIMARY KEY, beer_second_id VARCHAR, name VARCHAR, detail TEXT, category VARCHAR, meta_search TEXT, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS beer_unit (id SERIAL PRIMARY KEY, beer_unit_second_id VARCHAR, beer VARCHAR, name VARCHAR, price float8, discount float8, date_expire TIMESTAMP, volumetric float8, weight float8, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS image (id SERIAL PRIMARY KEY, imgid VARCHAR, thumbnail VARCHAR, medium VARCHAR, large VARCHAR, category VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, username VARCHAR, password VARCHAR, active BOOL, roles VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS device_config (id SERIAL PRIMARY KEY, color VARCHAR);

CREATE TABLE IF NOT EXISTS package_order (id SERIAL PRIMARY KEY, package_order_second_id VARCHAR, user_device_id VARCHAR, reciver_address VARCHAR, region_id INTEGER, district_id INTEGER, ward_id INTEGER, reciver_fullname VARCHAR, phone_number VARCHAR, total_price float8, ship_price float8, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS beer_order (id SERIAL PRIMARY KEY, name VARCHAR, package_order_second_id VARCHAR, beer_second_id VARCHAR, voucher_second_id VARCHAR, total_price float8, ship_price float8, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS beer_unit_order (id SERIAL PRIMARY KEY, name VARCHAR, package_order_second_id VARCHAR, beer_second_id VARCHAR, beer_unit_second_id VARCHAR, price float8, total_discount float8, number_unit INTEGER, createat TIMESTAMP);

CREATE TABLE IF NOT EXISTS user_device (id SERIAL PRIMARY KEY, device_id VARCHAR, user_first_name VARCHAR, user_last_name VARCHAR, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS user_address (id SERIAL PRIMARY KEY, device_id VARCHAR, reciver_fullname VARCHAR, phone_number VARCHAR, house_number VARCHAR, region INTEGER, district INTEGER, ward INTEGER, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS user_package (id SERIAL PRIMARY KEY, device_id VARCHAR, beer_id VARCHAR, beer_unit VARCHAR, number_unit INTEGER, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS voucher (id SERIAL PRIMARY KEY, voucher_second_id VARCHAR, detail VARCHAR, discount float8, amount float8, reuse INTEGER, for_all_beer BOOLEAN, for_all_user BOOLEAN, date_expire TIMESTAMP, status VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS voucher_relate_user_device (id SERIAL PRIMARY KEY, voucher_second_id VARCHAR, reuse INTEGER, device_id VARCHAR, createat TIMESTAMP, UNIQUE (voucher_second_id, device_id));
CREATE TABLE IF NOT EXISTS voucher_relate_beer (id SERIAL PRIMARY KEY, voucher_second_id VARCHAR, beer_second_id VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS beer_view_count (id SERIAL PRIMARY KEY, beer_id VARCHAR, device_id VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS notification (id SERIAL PRIMARY KEY, notification_second_id VARCHAR, title VARCHAR, detail VARCHAR, createat TIMESTAMP);
CREATE TABLE IF NOT EXISTS notification_relate_user_device (id SERIAL PRIMARY KEY, notification_second_id VARCHAR, user_device_id VARCHAR, createat TIMESTAMP, status VARCHAR);
CREATE TABLE IF NOT EXISTS shipping_provider (id SERIAL PRIMARY KEY, provider_id VARCHAR, name VARCHAR, config TEXT, createat TIMESTAMP);