package com.azure.openai;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.*;
import com.azure.core.credential.AzureKeyCredential;

import java.util.ArrayList;
import java.util.List;

public class GetChatCompletionsSample {

    public static void main(String[] args) {
        String azureOpenaiKey = System.getenv("OPENAI_API_KEY");;
        String endpoint = System.getenv("OPENAI_API_BASE");;
        String deploymentOrModelId = "gpt-35-turbo";

        OpenAIClient client = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(azureOpenaiKey))
                .buildClient();

        List<ChatMessage> chatMessages = new ArrayList();
        chatMessages.add(new ChatMessage(ChatRole.SYSTEM).setContent("You are a helpful assistant."));
        chatMessages.add(new ChatMessage(ChatRole.USER).setContent("Does Azure OpenAI support customer managed keys?"));
        chatMessages.add(new ChatMessage(ChatRole.ASSISTANT).setContent("Yes, customer managed keys are supported by Azure OpenAI?"));
        chatMessages.add(new ChatMessage(ChatRole.USER).setContent("Do other Azure Cognitive Services support this too?"));

        ChatCompletions chatCompletions = client.getChatCompletions(deploymentOrModelId, new ChatCompletionsOptions(chatMessages));

        System.out.printf("Model ID=%s is created at %d.%n", chatCompletions.getId(), chatCompletions.getCreated());
        for (ChatChoice choice : chatCompletions.getChoices()) {
            ChatMessage message = choice.getMessage();
            System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex(), message.getRole());
            System.out.println("Message:");
            System.out.println(message.getContent());
        }

        System.out.println();
        CompletionsUsage usage = chatCompletions.getUsage();
        System.out.printf("Usage: number of prompt token is %d, "
                        + "number of completion token is %d, and number of total tokens in request and response is %d.%n",
                usage.getPromptTokens(), usage.getCompletionTokens(), usage.getTotalTokens());
    }
}
