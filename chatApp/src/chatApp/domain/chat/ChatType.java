package chatApp.domain.chat;

import java.util.Arrays;

public enum ChatType {

    GROUP,
    PRIVATE,
    ROOM,
    ANY;


    public String[] getValues() {
        if (this.name().equals("ANY"))
            return Arrays.stream(values()).map(Enum::name).toArray(String[]::new);
        return new String[]{this.name()};
    }

}
