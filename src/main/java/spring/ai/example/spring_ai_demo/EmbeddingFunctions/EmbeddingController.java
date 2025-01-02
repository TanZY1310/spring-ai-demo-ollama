package spring.ai.example.spring_ai_demo.EmbeddingFunctions;

import org.slf4j.Logger;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//Sample test of using embedding models api
//Embedding converts text, image and video into arrays of floating point numbers called vectors to capture meaning of the text, image and video
//Length of embedding array is known as vector's dimentionality

@RestController
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;

    private final VectorStore vectorStore;

    private final ChatClient chatClient;

    @Value("classpath:embed.txt")
    private Resource resource;

    @Autowired
    public EmbeddingController(EmbeddingModel embeddingModel, VectorStore vectorStore, ChatClient.Builder chatClientBuilder) {
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    //Converts into arrays of floating point numbers
    @GetMapping("/ai/embedding")
    public Map embed(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        EmbeddingResponse embeddingResponse = this.embeddingModel.embedForResponse(List.of(message));
        return Map.of("embedding", embeddingResponse);
    }

    @GetMapping("/embedding/test")
    public String storeAndRetrieveEmbeddings() {
        List<Document> documents = List.of(new Document("I like Spring Boot"), new Document("I like Java"));
        vectorStore.add(documents);

        //Retrieve
        SearchRequest query = SearchRequest.query("Spring Boot").withTopK(2);
        List<Document> similarDocuments = vectorStore.similaritySearch(query);

        return similarDocuments.stream().map(Document::getContent)
                .collect(Collectors.joining(System.lineSeparator()));

    }

    //Store Document into embedding format
    @GetMapping("/embedding/store")
    public void storeEmbeddingsFromTextFile() {
        var textReader = new TextReader(resource);
        textReader.setCharset(Charset.defaultCharset());
        List<Document> documents = textReader.get();

        vectorStore.add(documents);
    }

    //RAG - Retrieval Augmented Generation
    @GetMapping("/embedding/rag/people")
    Person chatWithRag(@RequestParam String name) {
        //Query VectorStore using natural language while looking for information about a person
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.query(name).withTopK(2));
        String information = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining(System.lineSeparator()));

        //Construct the systemMessage to indicate the AI model to use passed information
        var systemPromptTemplate = new SystemPromptTemplate("""
              You are a helpful assistant.
              
              Use the following information to answer the question:
              {information}
              
              Output strictly in JSON format.
              """);
        var systemMessage = systemPromptTemplate.createMessage(
                Map.of("information", information));

        //Constructing the userMessage to ask AI  model to tell about the person
        PromptTemplate userMessagePromptTemplate = new PromptTemplate("""
            Tell me about {name}.
        
            Provide the details as a JSON object:
            {{
                "name": "string",
                "occupation": "string",
                "notableWorks": "string",
                "voiceRoles": "string"
            }}
        """);

        Map<String,Object> model = Map.of("name", name);
        var userMessage = new UserMessage(userMessagePromptTemplate.create(model).getContents());

        var prompt = new Prompt(List.of(systemMessage, userMessage));

        String response = chatClient.prompt(prompt).call().content();

        // Validate response
        if (!response.trim().startsWith("{") || !response.trim().endsWith("}")) {
            throw new RuntimeException("AI response is not in JSON format: " + response);
        }

        // Convert response to Person object
        var outputConverter = new BeanOutputConverter<>(Person.class);
        return outputConverter.convert(response);
    }

}

