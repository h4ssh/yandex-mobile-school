package ru.yandex.mobile_school.model.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hash on 10/06/2017.
 */

public class RemoveNoteDTO {

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
