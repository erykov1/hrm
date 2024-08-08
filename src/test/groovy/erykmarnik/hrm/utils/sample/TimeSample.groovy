package erykmarnik.hrm.utils.sample

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit

trait TimeSample {
  static Instant NOW = Instant.now(Clock.tickMillis(ZoneId.systemDefault()))
  static Instant WEEK_LATER = NOW.plus(7, ChronoUnit.DAYS)
}