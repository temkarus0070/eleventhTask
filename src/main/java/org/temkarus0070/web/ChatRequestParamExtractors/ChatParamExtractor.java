package org.temkarus0070.web.ChatRequestParamExtractors;

import org.temkarus0070.application.domain.chat.Chat;

import java.util.Map;
import java.util.Optional;

public interface ChatParamExtractor<T extends Chat> {
    public Optional<T> extractChat(Map<String, String[]> params);

    public Optional<T> putChat(Map<String, String[]> params);
}
