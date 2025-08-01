package com.example.heroku.config;

import com.example.heroku.config.maper.JsonToProductPriceItemArrayConverter;
import com.example.heroku.config.maper.ProductPriceItemArrayToJsonConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories
//@EnableR2dbcRepositories(basePackages = "com.example.heroku.model.repository")
public class R2dbcPostgresqlConfiguration extends AbstractR2dbcConfiguration {

    @Value("${spring.postgresql.externalurl}")
    private String externalUrl;

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

    // Inject ObjectMapper here, just like in your previous R2dbcConfig
    private final ObjectMapper objectMapper;

    public R2dbcPostgresqlConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    protected List<Object> getCustomConverters() {
        List<Object> converters = new ArrayList<>();
        converters.add(new JsonToProductPriceItemArrayConverter(objectMapper));
        converters.add(new ProductPriceItemArrayToJsonConverter(objectMapper));
        return converters;
    }

    @Bean
    @Override
    public ConnectionFactory connectionFactory() {
        ConnectionFactory postgresqlConnectionFactory;
        if (externalUrl != null && !externalUrl.isEmpty()) {
            postgresqlConnectionFactory = ConnectionFactories.get(externalUrl);
        } else {
            postgresqlConnectionFactory = new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration
                    .builder()
                    .host(host)
                    .database(db)
                    .username(username)
                    .password(password)
                    .port(port)
                    .sslMode(SSLMode.REQUIRE)
                    .build());
        }

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
