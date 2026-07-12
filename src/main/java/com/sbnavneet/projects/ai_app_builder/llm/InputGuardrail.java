package com.sbnavneet.projects.ai_app_builder.llm;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Pre-LLM input guardrail that rejects clearly off-topic or malicious prompts
 * before they reach the AI model (saving tokens and preventing misuse).
 */
@Component
public class InputGuardrail {

    /**
     * Patterns that indicate prompt injection or jailbreak attempts.
     */
    private static final List<Pattern> INJECTION_PATTERNS = List.of(
        Pattern.compile("(?i)ignore\\s+(previous|above|all)\\s+(instructions|prompts|rules)"),
        Pattern.compile("(?i)you\\s+are\\s+now\\s+(a|an)\\s+"),
        Pattern.compile("(?i)disregard\\s+(your|the|all)\\s+(rules|instructions|guidelines)"),
        Pattern.compile("(?i)forget\\s+(everything|your\\s+instructions|your\\s+rules)"),
        Pattern.compile("(?i)act\\s+as\\s+(if|though)\\s+you\\s+(are|were)"),
        Pattern.compile("(?i)pretend\\s+(to be|you\\s+are)"),
        Pattern.compile("(?i)new\\s+instructions?:"),
        Pattern.compile("(?i)system\\s*prompt\\s*override")
    );

    /**
     * Keywords that indicate clearly off-topic requests.
     * These are broad categories that have nothing to do with coding.
     */
    private static final List<String> OFF_TOPIC_INDICATORS = List.of(
        "write me a poem",
        "write a poem",
        "write me a story",
        "write a story",
        "write an essay",
        "tell me a joke",
        "what is the meaning of life",
        "who won the",
        "recipe for",
        "how to cook",
        "relationship advice",
        "horoscope",
        "lottery numbers"
    );

    /**
     * Validates user input before sending to the LLM.
     *
     * @param userMessage the user's message
     * @return GuardrailResult indicating whether to proceed or block
     */
    public GuardrailResult validate(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return GuardrailResult.block("Please provide a message about what you'd like to build or modify in your project.");
        }

        String normalized = userMessage.toLowerCase().trim();

        // Check for prompt injection
        for (Pattern pattern : INJECTION_PATTERNS) {
            if (pattern.matcher(normalized).find()) {
                return GuardrailResult.block(
                    "I'm designed to help you build your project. I can only assist with code generation, debugging, and development tasks within this application."
                );
            }
        }

        // Check for clearly off-topic requests
        for (String indicator : OFF_TOPIC_INDICATORS) {
            if (normalized.contains(indicator)) {
                return GuardrailResult.block(
                    "I'm designed to help you build your project. I can only assist with code generation, debugging, and development tasks. Please ask me something related to your project!"
                );
            }
        }

        // Message length guardrail (prevent token abuse)
        if (userMessage.length() > 10_000) {
            return GuardrailResult.block("Message is too long. Please keep your request under 10,000 characters.");
        }

        return GuardrailResult.allow();
    }

    /**
     * Result of guardrail validation.
     */
    public record GuardrailResult(boolean allowed, String rejectionMessage) {
        public static GuardrailResult allow() {
            return new GuardrailResult(true, null);
        }

        public static GuardrailResult block(String reason) {
            return new GuardrailResult(false, reason);
        }
    }
}
