package org.example.backend.Controller;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.net.MalformedURLException;
import java.net.URL;

@RestController
public class ChatController {

    /**
     * spring ai 自动装配的 可以直接注入使用
     */
//    字符串格式
    @Resource
    private OpenAiChatModel openAiChatModel;

    @RequestMapping(value = "/llm/chat")
    public String chat(@RequestParam(value = "message") String message) {
        return openAiChatModel.call(message);
    }

//    输出`json`格式
    @RequestMapping(value = "/llm/chat2")
    public Object chat2(@RequestParam(value = "message") String message) {
        ChatResponse chatResponse =openAiChatModel.call(new Prompt(message));
        return chatResponse.getResult().getOutput().getText();
    }

    //流式
    @RequestMapping(value = "/llm/chat3")
    public Object chat3(@RequestParam(value = "message") String message) {
        Flux<ChatResponse> flux =openAiChatModel.stream(new Prompt(message));
        flux.toStream().forEach(chatResponse -> {
            System.out.println(chatResponse.getResult().getOutput().getText());
        });
        return flux.collectList();
    }
//文字+图片
    @Resource
    private ChatModel chatModel;


    @RequestMapping(value="/llm/multi")
    public Object multi(String message,String imageUrl) throws MalformedURLException {
        // 将 String 转为 URL
        URL imageUrlObj = new URL(imageUrl);
        UserMessage userMessage = new UserMessage(message, new Media(MimeTypeUtils.IMAGE_PNG, imageUrlObj));

        ChatResponse response = chatModel.call(new Prompt(userMessage, OpenAiChatOptions.builder()
                .model(OpenAiApi.ChatModel.GPT_4_O.getValue())
                .build()));
        return response.getResult().getOutput().getText();
    }

}