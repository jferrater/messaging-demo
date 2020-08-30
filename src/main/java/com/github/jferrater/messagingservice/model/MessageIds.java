package com.github.jferrater.messagingservice.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.ArrayList;
import java.util.List;

public class MessageIds {

    @Schema(description = "The list of message id")
    private List<String> ids = new ArrayList<>();

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
