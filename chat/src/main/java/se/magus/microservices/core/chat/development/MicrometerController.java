package se.magus.microservices.core.chat.development;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RestController
public class MicrometerController {
    private final AtomicInteger myGauge  = new AtomicInteger();

    public MicrometerController(MeterRegistry registry) {
        Gauge.builder("my.gauge", myGauge, AtomicInteger::get)
                .description("Gauges something")
                .tags("region", "us-east")
                .register(registry);
    }

    /*
    curl http://localhost:8081/setGauge?value=42
     */
    @GetMapping("/setGauge")
    public void setGauge(@RequestParam int value) {
        log.info("Setting gauge to " + value);
        myGauge.set(value);
    }
}
