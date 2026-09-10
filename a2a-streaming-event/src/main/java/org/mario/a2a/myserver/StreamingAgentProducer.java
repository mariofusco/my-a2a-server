package org.mario.a2a.myserver;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.a2aproject.sdk.server.PublicAgentCard;
import org.a2aproject.sdk.server.agentexecution.AgentExecutor;
import org.a2aproject.sdk.spec.AgentCapabilities;
import org.a2aproject.sdk.spec.AgentCard;
import org.a2aproject.sdk.spec.AgentInterface;
import org.a2aproject.sdk.spec.AgentSkill;

import java.util.List;

@ApplicationScoped
public class StreamingAgentProducer {

    @Produces
    public AgentExecutor agentExecutor() {
        return new StreamingAgentExecutor();
    }

    @Produces
    @PublicAgentCard
    public AgentCard agentCard() {
        return AgentCard.builder()
                .name("Streaming Agent")
                .description("Simply modifies the status and adds artifacts")
                .version("1.0.0")
                .capabilities(AgentCapabilities.builder()
                        .streaming(true)
                        .build())
                .defaultInputModes(List.of("text"))
                .defaultOutputModes(List.of("text"))
                .skills(List.of(AgentSkill.builder()
                        .id("test")
                        .name("StreamingTest")
                        .description("Modifies the status and adds artifacts")
                        .tags(List.of("test", "test"))
                        .build()))
                .supportedInterfaces(List.of(
                        new AgentInterface("JSONRPC", "http://localhost:8080")))
                .build();
    }
}
