package org.mario.a2a.myserver;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

import java.time.Duration;

@ApplicationScoped
public class CreativeWriterProducer {

    static final ChatModel model = OllamaChatModel.builder()
            .baseUrl("http://127.0.0.1:11434")
            .modelName("qwen2.5:7b")
            .timeout(Duration.ofMinutes(10))
            .temperature(0.0)
            .logRequests(true)
            .logResponses(true)
            .build();

    @Produces
    public CreativeWriter creativeWriter() {
        return AiServices.builder(CreativeWriter.class)
                .chatModel(model)
                .build();
    }
}
