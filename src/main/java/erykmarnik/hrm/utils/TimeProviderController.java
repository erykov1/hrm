package erykmarnik.hrm.utils;

import erykmarnik.hrm.config.Profiles;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.Instant;

@RestController
@Hidden
@RequestMapping("/api/time")
@Profile(Profiles.TEST)
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
class TimeProviderController {
  InstantProvider instantProvider;

  @Autowired
  TimeProviderController(InstantProvider instantProvider) {
    this.instantProvider = instantProvider;
  }

  @PostMapping("/fixedClock")
  @PreAuthorize("hasRole('ADMIN')")
  void useFixedClock(@RequestBody Instant instant) {
    instantProvider.useFixedClock(instant);
  }

  @GetMapping("/systemClock")
  @PreAuthorize("hasRole('ADMIN')")
  void useSystemClock() {
    instantProvider.useSystemClock();
  }
}
