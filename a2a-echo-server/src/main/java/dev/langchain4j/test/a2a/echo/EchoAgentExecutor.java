package dev.langchain4j.test.a2a.echo;

import java.util.List;

import org.a2aproject.sdk.server.agentexecution.AgentExecutor;
import org.a2aproject.sdk.server.agentexecution.RequestContext;
import org.a2aproject.sdk.server.tasks.AgentEmitter;
import org.a2aproject.sdk.spec.A2AError;
import org.a2aproject.sdk.spec.TextPart;

/**
 * A2A agent that echoes back the contextId and taskId received in the message envelope.
 * Used to verify that the langchain4j A2A client correctly sets these fields.
 */
public class EchoAgentExecutor implements AgentExecutor {

    @Override
    public void execute(RequestContext context, AgentEmitter emitter) throws A2AError {
        if (context.getTask() == null) {
            emitter.submit();
        }

        String response = "contextId=" + context.getContextId()
                + "|taskId=" + context.getTaskId()
                + "|input=" + context.getUserInput();

        emitter.startWork();
        emitter.addArtifact(List.of(new TextPart(response, null)));
        emitter.requiresInput();
    }

    @Override
    public void cancel(RequestContext context, AgentEmitter emitter) throws A2AError {
        emitter.cancel();
    }
}
