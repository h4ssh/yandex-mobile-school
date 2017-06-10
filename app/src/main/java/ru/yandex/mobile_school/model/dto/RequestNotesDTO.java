package ru.yandex.mobile_school.model.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestNotesDTO {

    private int userId;
    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<NoteDTO> data;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NoteDTO> getData() {
        return data;
    }

    public void setData(List<NoteDTO> data) {
        this.data = data;
    }
}
