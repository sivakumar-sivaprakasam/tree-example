package com.example.tree_example;

import java.util.List;
import java.util.ArrayList;

public class ClientDto {
    private String clientId;
    private String parentId;
    private List<String> children;
    private List<String> docList;

    public ClientDto() {
    }

    public ClientDto(String clientId, String parentId) {
        this.clientId = clientId;
        this.parentId = parentId;
        this.children = new ArrayList<>();
        this.docList = new ArrayList<>();
    }

    public List<String> getChildren() {
        return this.children;
    }

    public List<String> getDocList() {
        return this.docList;
    }

    public String getClientId() {
        return clientId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setChildren(List<String> children) {
        this.children = children;
    }

    public void setDocList(List<String> cpyList) {
        this.docList = cpyList;
    }

    public void addChildren(String childId) {
        this.children.add(childId);
    }

    public void removeChildren(String childId) {
        for (String s : this.children) {
            if (childId.equals(s)) {
                this.children.remove(s);
            }
        }
    }

    public void addDocument(String docId) {
        this.docList.add(docId);
    }

    public void removeDocument(String docId) {
        for (String s : this.docList) {
            if (docId.equals(s)) {
                this.docList.remove(s);
            }
        }
    }

    @Override
    public String toString() {
        return "ClientDto [clientId=" + clientId + ", parentId=" + parentId + ", children=" + children + ", docList="
                + docList + "]";
    }
}