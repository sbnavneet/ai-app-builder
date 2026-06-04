import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final AiGenerationService aiGenerationService;


    @PostMapping(value = "/api/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public FLux<ServerSentEvent<String>> streamChat(@RequestBody ChatRequest request) {
        //TODO: process POST request
        
        return entity;
    }
    
}
