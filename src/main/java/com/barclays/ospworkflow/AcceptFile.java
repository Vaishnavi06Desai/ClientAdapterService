package com.barclays.ospworkflow;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class AcceptFile {
    private File file;
    private String docType;

    public MultipartFile getFile() {
        return (MultipartFile) file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
}
