package es.upm.oeg.epnoi.collector.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 19/02/15.
 */
@Component
public class ErrorHandler implements Processor{

    Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @Override
    public void process(Exchange exchange) throws Exception {

        // the caused by exception is stored in a property on the exchange
        Throwable caused = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);

        log.error("Error processing message: {}", exchange.getIn(), caused);
    }
}
