package erykmarnik.hrm.config;

import org.testcontainers.containers.PostgreSQLContainer;

import java.time.Duration;

public class PostgresContainerConfig {
  private static PostgreSQLContainer postgreSQLContainer;

  public static void init() {
    postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(
            "postgres")
            .withUsername("postgres")
            .withPassword("admin").withStartupTimeout(Duration.ofSeconds(600));
    postgreSQLContainer.start();
  }

  public static PostgreSQLContainer getPostgreSQLContainer() {
    return postgreSQLContainer;
  }
}
