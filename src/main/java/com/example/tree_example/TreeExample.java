package com.example.tree_example;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class TreeExample {
    Map<String, ClientDto> clientMap = new HashMap<>();
    Map<String, DocDto> documentMap = new HashMap<>();

    public static void main(String... args) throws FileNotFoundException, IOException, URISyntaxException {
        TreeExample treeExample = new TreeExample();
        treeExample.loadDataset();
        // System.out.println("Before changing the parent...");
        // treeExample.printContent();
        treeExample.updateParent("D", "G");
        // System.out.println("After changing the parent...");
        // treeExample.printContent();
    }

    private void loadDataset() throws URISyntaxException, IOException {
        // Client Stock
        Path path = Paths.get(ClassLoader.getSystemResource("client_stock.csv").toURI());
        try (Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1)
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build()) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    String clientId = line[0];
                    String parentId = line[1];
                    if ("".equals(parentId)) {
                        parentId = null;
                    }
                    ClientDto dto = new ClientDto(clientId, parentId);
                    clientMap.put(line[0], dto);
                    if (null != parentId && clientMap.containsKey(parentId)) {
                        (clientMap.get(parentId)).addChildren(clientId);
                    }
                }
            }
        }

        // Document Stock
        path = Paths.get(ClassLoader.getSystemResource("doc_stock.csv").toURI());
        try (Reader reader = Files.newBufferedReader(path)) {
            try (CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1)
                    .withCSVParser(new CSVParserBuilder().withSeparator(',').build()).build()) {
                String[] line;
                while ((line = csvReader.readNext()) != null) {
                    String docId = line[0];
                    String clientId = line[1];
                    if ("".equals(clientId)) {
                        clientId = null;
                    }
                    DocDto dto = new DocDto(docId, clientId);
                    documentMap.put(line[0], dto);
                    if (null != clientId && clientMap.containsKey(clientId)) {
                        (clientMap.get(clientId)).addDocument(docId);
                    }
                }
            }
        }
    }

    private void printContent() {
        for (Map.Entry<String, ClientDto> m : clientMap.entrySet()) {
            System.out.println(m.getKey() + " --> " + m.getValue());
        }
    }

    private void updateParent(String clientId, String newParentId) {
        if (clientMap.containsKey(clientId)) {
            ClientDto dto = clientMap.get(clientId);
            String existingParent = dto.getParentId();
            if (null != existingParent) {
                if (clientMap.containsKey(existingParent)) {
                    (clientMap.get(existingParent)).removeChildren(clientId);
                }
            }
            dto.setParentId(newParentId);
            if (null != newParentId) {
                if (clientMap.containsKey(newParentId)) {
                    (clientMap.get(newParentId)).addChildren(clientId);
                }
            }
            System.out.println(getAllChildrens(dto));
        }
    }

    private String getUltimateRoot(String clientId) {
        StringBuilder sb = new StringBuilder(clientId);
        ClientDto clientDto = clientMap.get(clientId);
        while (clientDto.getParentId() != null) {
            sb.append("|");
            sb.append(clientDto.getParentId());
            clientDto = clientMap.get(clientDto.getParentId());
        }
        return sb.toString();
    }

    private List<ClientDto> getAllChildrens(ClientDto dto) {
        List<ClientDto> dtoList = new ArrayList<>();
        addAllChildren(dto, dtoList, getUltimateRoot(dto.getClientId()));
        return dtoList;
    }

    private void addAllChildren(ClientDto dto, List<ClientDto> dtoList, String tmp) {
        if (null != dto) {
            dtoList.add(dto);
            List<String> childList = dto.getChildren();
            if (null != childList) {
                for (String s : childList) {
                    dto = clientMap.get(s);
                    String newTmp = (s + "|" + tmp);
                    if (null != dto.getDocList()) {
                        for (String d : dto.getDocList()) {
                            System.out.println("Hierarchy for Document Id: " + d + " >>> " + newTmp);
                        }
                    }
                    addAllChildren(dto, dtoList, newTmp);
                }
            }
        }
    }
}