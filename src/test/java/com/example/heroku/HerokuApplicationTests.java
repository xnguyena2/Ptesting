package com.example.heroku;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(properties = {
		"JDBC_DATABASE_URL=r2dbc:postgresql://ec2-52-203-165-126.compute-1.amazonaws.com:5432/dab63e0a6snl77",
		"POSTGRESQL_PORT=5432",
		"POSTGRESQL_DB=dab63e0a6snl77",
		"POSTGRESQL_HOST=ec2-52-203-165-126.compute-1.amazonaws.com",
		"USER_NAME=pnkrlyomnugskb",
		"PASSWORD=4b16a191f30c89f36908193809d12d3a8d4c3ffb73277357e1b0e0515169cea2"})
public class HerokuApplicationTests {

	@Test
	public void contextLoads() {
	}

}

