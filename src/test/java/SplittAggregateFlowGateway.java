import org.springframework.integration.annotation.Gateway;
import org.springframework.messaging.Message;

public interface SplittAggregateFlowGateway {

    @Gateway(requestChannel = "in", replyChannel = "out")
    Message<String> run(Message<String> in);
}
