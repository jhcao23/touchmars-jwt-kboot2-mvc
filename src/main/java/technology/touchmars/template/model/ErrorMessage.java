package technology.touchmars.template.model;

import lombok.Data;

@Data
public class ErrorMessage implements Replyable {
    private String errMsg;
}
