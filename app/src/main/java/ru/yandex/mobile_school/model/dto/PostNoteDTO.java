package ru.yandex.mobile_school.model.dto;

import com.google.gson.annotations.SerializedName;

public class PostNoteDTO {

    private String uuid;
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private int data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
