package org.mario.a2a.myserver;

import org.a2aproject.sdk.server.agentexecution.AgentExecutor;
import org.a2aproject.sdk.server.agentexecution.RequestContext;
import org.a2aproject.sdk.server.tasks.AgentEmitter;
import org.a2aproject.sdk.spec.A2AError;
import org.a2aproject.sdk.spec.TextPart;

import java.util.List;

/**
 * A2A agent that simply modifies the status and adds artifacts
 */
public class StreamingAgentExecutor implements AgentExecutor {

    @Override
    public void execute(RequestContext context, AgentEmitter emitter) throws A2AError {
        if (context.getTask() == null) {
            emitter.submit();
        }

        emitter.startWork();

        emitter.addArtifact(List.of(new TextPart("1.0")));
        emitter.addArtifact(List.of(new TextPart("1.0")));
        emitter.addArtifact(List.of(new TextPart("1.0")));

        emitter.complete();
    }

    @Override
    public void cancel(RequestContext context, AgentEmitter emitter) throws A2AError {
        emitter.cancel();
    }
}
