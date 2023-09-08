package com.example.heroku;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
        "JDBC_DATABASE_URL=r2dbc:postgres://dpg-cjt8krdhtt0c73astvqg-a.singapore-postgres.render.com/sale_management",
        "POSTGRESQL_PORT=5432",
        "POSTGRESQL_DB=sale_management",
        "POSTGRESQL_HOST=dpg-cjt8krdhtt0c73astvqg-a.singapore-postgres.render.com",
        "USER_NAME=sale_management_user",
        "PASSWORD=J6nLIQW7VRDPNXG6owJPLjEZb0c0q6kn",
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
        "INIT_ADMIN_STORE=trumbien_store",
        "LOGIN=INFO",
        "THYMELEFT=HTML",
        "RUN_MODE=dev",
        "FLICKR_KEY=d82298e7cabb6de301b1de5bc018b881",
        "FLICKR_SECRET=5b4fe60ee0a85084",
        "FLICKR_AUTH_TOKEN=72157720816792310-6ae700ae55e0f3f6",
        "FLICKR_TOKEN_SECRET=9a4dee6dbdb7f7be",
        "FLICKR_USER_NAME=xnguyena28",
        "FLICKR_NID=194077547@N04"
})
public class TestConfig {
    @Test
    public void JustEmpty() {}
}
