package cz.marek_b.edp.retry_topics_dlq.carrier_api;

import cz.marek_b.edp.retry_topics_dlq.ex.CarrierTimeoutException;
import cz.marek_b.edp.retry_topics_dlq.ex.InvalidAddressException;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CarrierApi {

    private static final Random RANDOM = new Random();

    public void doSomething() throws CarrierTimeoutException, InvalidAddressException {
        var rand = RANDOM.nextInt(10);

        if (rand > 5 && rand < 8) {
            throw new CarrierTimeoutException("Carrier timeout");
        }

        if (rand >= 8) {
            throw new InvalidAddressException("Invalid address");
        }

        System.out.println("Carrier api success");
    }

}
