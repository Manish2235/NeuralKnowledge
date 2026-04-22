package com.incapp.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiService {

    private final ChatClient chatClient;

    public AiService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    /**
     * General AI question (no document context)
     */
    public String ask(String question) {

        return chatClient.prompt()
                .system("You are a helpful AI assistant.")
                .user(question)
                .call()
                .content();
    }

    /**
     * Document-specific question answering (RAG-lite)
     */
    public String askFromDocument(String question, String content) {

        if (content == null || content.isBlank()) {
            return "The selected document has no readable content.";
        }

        String prompt = """
                You are an AI assistant.
                Answer strictly based on the document content provided below.
                If the answer is not present in the document, reply exactly:
                "The document does not contain this information."

                Document Content:
                ----------------
                %s

                Question:
                --------
                %s
                """.formatted(content, question);

        return chatClient.prompt()
                .system("You answer only from the given document.")
                .user(prompt)
                .call()
                .content();
    }
}
