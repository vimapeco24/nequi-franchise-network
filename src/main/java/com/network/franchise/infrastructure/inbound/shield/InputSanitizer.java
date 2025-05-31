package com.network.franchise.infrastructure.inbound.shield;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Component;

@Component
public class InputSanitizer {

    /**
     * Sanitizes the input string to prevent XSS attacks.
     * @param value the input string to sanitize
     * @return the sanitized string
     * <p>
     * This method uses the OWASP Java HTML Sanitizer library to remove potentially dangerous HTML tags and attributes.
     * It also replaces certain HTML entities with their corresponding characters.
     * The sanitization policy allows formatting and links, but you can customize it as needed.
     * <p>
     * Note: This method is not a complete solution for preventing XSS attacks.
     * It is recommended to use additional security measures such as input validation, output encoding, and Content Security Policy (CSP) headers.
     *
     * @see <a href="https://owasp.org/www-project-java-html-sanitizer/">OWASP Java HTML Sanitizer</a>
     * @see <a href="https://owasp.org/www-community/attacks/xss/">Cross-Site Scripting (XSS)</a>
     * @see <a href="https://owasp.org/www-community/OWASP_Java_HTML_Sanitizer_Project">OWASP Java HTML Sanitizer Project</a>
     * @see <a href="https://owasp.org/www-community/OWASP_Prevention_Cheat_Sheets">OWASP Prevention Cheat Sheets</a>
     *
     * @Recommended to use for: Raul Bolivar Navas - Since 2022-10-05
     */
    public static String blindStr(String value) {
        PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.LINKS);
        return policy.sanitize(value)
                .replace("&#34;", "'")
                .replace("&#43;", "+")
                .replace("&#39;", "'")
                .replace("&#61;", "=")
                .replace("&amp;", " & ")
                .replace("&#64;", "@");
    }
}
