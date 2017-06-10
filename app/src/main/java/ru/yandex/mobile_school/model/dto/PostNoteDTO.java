package ru.yandex.mobile_school.model.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by hash on 10/06/2017.
 */

public class PostNoteDTO {

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
}
