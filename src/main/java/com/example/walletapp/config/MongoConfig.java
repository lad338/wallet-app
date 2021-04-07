package com.example.walletapp.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.walletapp")
@EnableMongoAuditing
public class MongoConfig extends AbstractMongoClientConfiguration {
  @Value("${mongo.hosts}")
  private String hosts;

  @Value("${mongo.database}")
  private String database;

  @Bean
  MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory) {
    return new MongoTransactionManager(dbFactory);
  }

  @Override
  protected String getDatabaseName() {
    return database;
  }

  @Override
  public MongoClient mongoClient() {
    final ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s/%s?retryWrites=true", hosts, database));
    final MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
      .applyConnectionString(connectionString)
      .build();
    return MongoClients.create(mongoClientSettings);
  }

  @Override
  protected boolean autoIndexCreation() {
    return true;
  }
}
