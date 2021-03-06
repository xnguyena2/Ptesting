package com.example.heroku.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories
//@EnableR2dbcRepositories(basePackages = "com.example.heroku.model.repository")
public class R2dbcPostgresqlConfiguration extends AbstractR2dbcConfiguration {
    @Value("${spring.postgresql.host}")
    private String host;

    @Value("${spring.postgresql.port}")
    private int port;

    @Value("${spring.postgresql.db}")
    private String db;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.postgresql.poolsize}")
    private int poolSize;

    @Value("${spring.postgresql.timeidle}")
    private long timeidle;

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        PostgresqlConnectionFactory postgresqlConnectionFactory = new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration
                .builder()
                .host(host)
                .database(db)
                .username(username)
                .password(password)
                .port(port)
                .sslMode(SSLMode.REQUIRE)
                .build());

        ConnectionPoolConfiguration configuration = ConnectionPoolConfiguration.builder(postgresqlConnectionFactory)
                .maxIdleTime(Duration.ofMillis(timeidle))
                .maxSize(poolSize)
                .build();
        return new ConnectionPool(configuration);
    }

    /*
    @Override
    protected List<Object> getCustomConverters() {
        return Arrays.asList(
                new PostStatusWritingConverter()
        );
    }

    @Bean
    DatabaseClient databaseClient(ConnectionFactory connectionFactory) {
        return DatabaseClient.builder()
                .connectionFactory(connectionFactory)
                //.bindMarkers(() -> BindMarkersFactory.named(":", "", 20).create())
                .namedParameters(true)
                .build();
    }

    @Bean
    TransactionAwareConnectionFactoryProxy transactionAwareConnectionFactoryProxy(ConnectionFactory connectionFactory) {
        return new TransactionAwareConnectionFactoryProxy(connectionFactory);
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

    @Bean
    TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource("data.sql")));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }
    */
}
