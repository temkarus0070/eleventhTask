package org.temkarus0070.application.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.temkarus0070.application.domain.chat.ChatType;
import org.temkarus0070.application.domain.exceptions.ClassOfChatAppNotFoundException;
import org.temkarus0070.application.services.persistence.statementExecutors.GroupChatStatementExecutor;
import org.temkarus0070.application.services.persistence.statementExecutors.PrivateChatStatementExecutor;
import org.temkarus0070.application.services.persistence.statementExecutors.RoomChatStatementExecutor;
import org.temkarus0070.application.services.persistence.statementExecutors.StatementExecutor;

@Component
public class StatementExecutorFactory {
    @Autowired
    private ApplicationContext applicationContext;

    public StatementExecutor getStatementPreparator(ChatType chatType) {

        switch (chatType) {
            case PRIVATE:
                return applicationContext.getBean(PrivateChatStatementExecutor.class);
            case GROUP:
                return applicationContext.getBean(GroupChatStatementExecutor.class);
            case ROOM:
                return applicationContext.getBean(RoomChatStatementExecutor.class);
        }
        throw new ClassOfChatAppNotFoundException();
    }
}
