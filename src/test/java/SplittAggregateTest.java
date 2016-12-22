import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class SplittAggregateTest {

    @Test
    public void testPerformance() {

        final Integer runs = 1500;
        final List<String[]> stringList =  new ArrayList<>();
        final String slowFlowName = "splittAggregate-performance-notOk-flow.xml";
        final String fastFlowName = "splittAggregate-performance-ok-flow.xml";

        for (Integer i=0; i <= runs; i++) {
            stringList.add(i, new String[]{i.toString(),i.toString(),i.toString(),i.toString(),i.toString()});
        }

        Message<String> message = MessageBuilder.withPayload("foo").setHeader("splittHeader", stringList).build();

        StopWatch watchNotOk = new StopWatch();
        watchNotOk.start();
        getFlow(slowFlowName).run(message);
        watchNotOk.stop();

        StopWatch watchOk = new StopWatch();
        watchOk.start();
        getFlow(fastFlowName).run(message);
        watchOk.stop();

        System.out.println("Elapsed Ok: " + watchOk.getTotalTimeSeconds());
        System.out.println("Elapsed Not Ok: " + watchNotOk.getTotalTimeSeconds());

        assertThat(watchNotOk.getTotalTimeSeconds(), greaterThanOrEqualTo(watchOk.getTotalTimeSeconds()));
        assertThat(watchOk.getTotalTimeSeconds(), lessThanOrEqualTo(Double.valueOf(5)));
    }

    private SplittAggregateFlowGateway getFlow(String name) {
        return new ClassPathXmlApplicationContext(name, SplittAggregateTest.class).getBean(SplittAggregateFlowGateway.class);
    }

}
