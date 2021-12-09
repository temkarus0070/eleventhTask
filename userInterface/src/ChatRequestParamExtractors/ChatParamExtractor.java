package ChatRequestParamExtractors;

import chatApp.domain.chat.Chat;

import java.util.Map;
import java.util.Optional;

public interface ChatParamExtractor<T extends Chat> {
    public Optional<T> extractChat(Map<String, String[]> params);

    public Optional<T> putChat(Map<String, String[]> params);
}
