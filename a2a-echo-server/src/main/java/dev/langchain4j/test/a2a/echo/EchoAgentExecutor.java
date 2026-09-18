package dev.langchain4j.test.a2a.echo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.a2aproject.sdk.server.agentexecution.AgentExecutor;
import org.a2aproject.sdk.server.agentexecution.RequestContext;
import org.a2aproject.sdk.server.tasks.AgentEmitter;
import org.a2aproject.sdk.spec.A2AError;
import org.a2aproject.sdk.spec.Part;
import org.a2aproject.sdk.spec.TextPart;

/**
 * A2A agent that echoes back received messages and maintains a simple context cache based on contextId.
 * It responds to the "stop" command by returning the contextId, taskId, and user input, along with the cached context.
 */
public class EchoAgentExecutor implements AgentExecutor {

    private final Map<String, List<String>> easyContextCache = new HashMap<>();

    @Override
    public void execute(RequestContext context, AgentEmitter emitter) throws A2AError {
        if (context.getTask() == null) {
            emitter.submit();
        }

        String text = ((TextPart) context.getMessage().parts().getFirst()).text();
        setContext(context.getContextId(), text);
        if (text.contains("stop")) {
            String response = "contextId=" + context.getContextId()
                + "|taskId=" + context.getTaskId()
                + "|input=" + context.getUserInput();
            emitter.addArtifact(List.of(new TextPart(response, null)));
            emitter.addArtifact(getContext(context.getContextId()));
            emitter.complete();
            return;
        }

        emitter.requiresInput();
    }

    private List<Part<?>> getContext(String contextId) {
        List<String> con = easyContextCache.get(contextId);
        if (con == null) {
            return List.of();
        }
        return con.stream()
            .map(s -> new TextPart(s, null))
            .collect(Collectors.toList());
    }

    private void setContext(String contextId, String context) {
        List<String> con = easyContextCache.computeIfAbsent(contextId, k -> new ArrayList<>());
        con.add(context);
    }

    @Override
    public void cancel(RequestContext context, AgentEmitter emitter) throws A2AError {
        emitter.cancel();
    }
}
