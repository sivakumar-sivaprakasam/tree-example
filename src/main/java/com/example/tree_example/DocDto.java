package com.example.tree_example;

public class DocDto {
    private String docId;
    private String clientId;

    public DocDto() {
    }

    public DocDto(String cpyId, String clientId) {
        this.docId = cpyId;
        this.clientId = clientId;
    }
    
    public String getDocId() {
        return this.docId;
    }

    public String getClientId() {
        return clientId;
    }

    @Override
    public String toString() {
        return "DocDto [docId=" + docId + ", clientId=" + clientId + "]";
    }
}