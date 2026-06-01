package org.mario.a2a.myserver;

import org.a2aproject.sdk.server.agentexecution.AgentExecutor;
import org.a2aproject.sdk.server.agentexecution.RequestContext;
import org.a2aproject.sdk.server.tasks.AgentEmitter;
import org.a2aproject.sdk.spec.A2AError;
import org.a2aproject.sdk.spec.Part;
import org.a2aproject.sdk.spec.TaskNotCancelableError;
import org.a2aproject.sdk.spec.TaskState;
import org.a2aproject.sdk.spec.TextPart;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import java.util.List;

@ApplicationScoped
public class StyleScorerExecutorProducer {

    @Inject
    StyleScorer styleScorerAgent;

    @Produces
    public AgentExecutor agentExecutor() {
        return new StyleScorerExecutor(styleScorerAgent);
    }

    private static class StyleScorerExecutor implements AgentExecutor {

        private final StyleScorer styleScorer;

        public StyleScorerExecutor(StyleScorer StyleScorer) {
            this.styleScorer = StyleScorer;
        }

        @Override
        public void execute(RequestContext context, AgentEmitter emitter) throws A2AError {
            if (context.getTask() == null) {
                emitter.submit();
            }
            emitter.startWork();

            String userMessage = context.getUserInput();
            String response = "" + styleScorer.scoreStyle(userMessage, userMessage);

            System.out.println("StyleScorerExecutor: Generated response: " + response);

            TextPart responsePart = new TextPart(response);
            List<Part<?>> parts = List.of(responsePart);

            emitter.addArtifact(parts);
            emitter.complete();
        }

        @Override
        public void cancel(RequestContext context, AgentEmitter emitter) throws A2AError {
            if (context.getTask() == null) {
                throw new TaskNotCancelableError();
            }

            TaskState state = context.getTask().status().state();
            if (state == TaskState.TASK_STATE_CANCELED || state == TaskState.TASK_STATE_COMPLETED) {
                throw new TaskNotCancelableError();
            }

            emitter.cancel();
        }
    }
}
