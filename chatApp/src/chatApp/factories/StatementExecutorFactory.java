package chatApp.factories;

import chatApp.domain.chat.ChatType;
import chatApp.domain.exceptions.ClassOfChatAppNotFoundException;
import chatApp.services.persistence.statementExecutors.GroupChatStatementExecutor;
import chatApp.services.persistence.statementExecutors.PrivateChatStatementExecutor;
import chatApp.services.persistence.statementExecutors.RoomChatStatementExecutor;
import chatApp.services.persistence.statementExecutors.StatementExecutor;

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
