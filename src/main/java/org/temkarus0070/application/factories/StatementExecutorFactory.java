package org.temkarus0070.application.factories;

import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.exceptions.ClassOfChatAppNotFoundException;
import org.temkarus0070.application.services.persistence.statementExecutors.GroupChatStatementExecutor;
import org.temkarus0070.application.services.persistence.statementExecutors.PrivateChatStatementExecutor;
import org.temkarus0070.application.services.persistence.statementExecutors.RoomChatStatementExecutor;
import org.temkarus0070.application.services.persistence.statementExecutors.StatementExecutor;

public class StatementExecutorFactory {
    public static StatementExecutor getStatementPreparator(ChatType chatType) {
        switch (chatType) {
            case PRIVATE:
                return new PrivateChatStatementExecutor();
            case GROUP:
                return new GroupChatStatementExecutor();
            case ROOM:
                return new RoomChatStatementExecutor();
        }
        throw new ClassOfChatAppNotFoundException();
    }
}
