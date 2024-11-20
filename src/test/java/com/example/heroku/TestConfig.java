package com.example.heroku;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
        "FIRE_BASE_TOKEN=",
        "JDBC_DATABASE_URL=jdbc:postgres://dpg-csunbcdds78s73eobnn0-a.singapore-postgres.render.com/sale_management_dev_sdcw",
        "POSTGRESQL_PORT=5432",
        "POSTGRESQL_DB=sale_management_dev_sdcw",
        "POSTGRESQL_HOST=dpg-csunbcdds78s73eobnn0-a.singapore-postgres.render.com",
        "DB_USER_NAME=sale_management_dev_sdcw_user",
        "DB_PASSWORD=VaKtTYTJ6g5bGhAdQdPWPKimJhFZF1RT",
        "DB_DRIVER=org.postgresql.Driver",
        "POSTGRESQL_POOLSIZE=10",
        "POSTGRESQL_TIMEIDLE=10000",
        "RESET_DB=true",
        "PORT=5000",
        "JWT_ACCESSTOKEN=accessToken",
        "JWT_USING_COOKIE=true",
        "JWT_HTTPONLY_SECURE=false",
        "JWT_SECRET=binhdiepquinnnnnnnnnnnnnnnnnnn",
        "JWT_TIME_EXPIRE=3600000",
        "INIT_ADMIN_NAME=binhdiepquin",
        "INIT_ADMIN_PASSWORD=binhdiepquin123",
        "INIT_ADMIN_STORE=trumbien_store_binhdiepquin",
        "LOGS_LEVEL=INFO",
        "THYMELEFT=HTML",
        "RUN_MODE=dev",
        "FLICKR_KEY=8294cbd4c540c0d895ec99bb8b1fcac2",
        "FLICKR_SECRET=f8fa9dcd4c4642b8",
        "FLICKR_AUTH_TOKEN=72157718531338752-478e88fe3e96fc70",
        "FLICKR_TOKEN_SECRET=fdc5a5980651383c",
        "FLICKR_USER_NAME=xnguyena2",
        "FLICKR_NID=192362717@N03"
})
public class TestConfig {
    @Test
    public void JustEmpty() {}
}
