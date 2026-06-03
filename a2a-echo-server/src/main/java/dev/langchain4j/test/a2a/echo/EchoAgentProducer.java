package dev.langchain4j.test.a2a.echo;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import org.a2aproject.sdk.server.PublicAgentCard;
import org.a2aproject.sdk.server.agentexecution.AgentExecutor;
import org.a2aproject.sdk.spec.AgentCapabilities;
import org.a2aproject.sdk.spec.AgentCard;
import org.a2aproject.sdk.spec.AgentInterface;
import org.a2aproject.sdk.spec.AgentSkill;

@ApplicationScoped
public class EchoAgentProducer {

    @Produces
    public AgentExecutor agentExecutor() {
        return new EchoAgentExecutor();
    }

    @Produces
    @PublicAgentCard
    public AgentCard agentCard() {
        return AgentCard.builder()
                .name("Echo Agent")
                .description("Echoes back the contextId and taskId from the message envelope")
                .version("1.0.0")
                .capabilities(AgentCapabilities.builder()
                        .streaming(true)
                        .build())
                .defaultInputModes(List.of("text"))
                .defaultOutputModes(List.of("text"))
                .skills(List.of(AgentSkill.builder()
                        .id("echo")
                        .name("Echo")
                        .description("Echoes message envelope metadata")
                        .tags(List.of("echo", "test"))
                        .build()))
                .supportedInterfaces(List.of(
                        new AgentInterface("JSONRPC", "http://localhost:8081")))
                .build();
    }
}
