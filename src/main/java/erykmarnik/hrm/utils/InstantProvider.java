package erykmarnik.hrm.utils;

import org.springframework.context.annotation.Scope;

import java.time.*;

@Scope("singleton")
public class InstantProvider {
  private Clock clock = Clock.systemUTC();

  public Instant now() {
    return Instant.now(clock);
  }

  public void useFixedClock(Instant instant) {
    clock = Clock.fixed(instant, ZoneId.systemDefault());
  }

  public void useSystemClock() {
    clock = Clock.systemUTC();
  }
}
