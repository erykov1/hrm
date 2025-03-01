package erykmarnik.hrm.utils.sample

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

trait TimeSample {
  static Instant NOW = Instant.now(Clock.tickMillis(ZoneId.systemDefault()))
  Instant WEEK_EARLIER = NOW.minus(7, ChronoUnit.DAYS)
  Instant THREE_DAYS_LATER = NOW.plus(3, ChronoUnit.DAYS)
  Instant WEEK_LATER = NOW.plus(7, ChronoUnit.DAYS)
}