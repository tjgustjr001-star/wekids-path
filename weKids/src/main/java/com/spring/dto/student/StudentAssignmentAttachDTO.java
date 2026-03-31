package com.spring.dto.student;

public class StudentAssignmentAttachDTO {
    private int ano;
    private int pno;
    private String uploadPath;
    private String fileName;
    private String fileType;
    private String attacher;
    private String regDate;

    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano = ano; }
    public int getPno() { return pno; }
    public void setPno(int pno) { this.pno = pno; }
    public String getUploadPath() { return uploadPath; }
    public void setUploadPath(String uploadPath) { this.uploadPath = uploadPath; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    public String getAttacher() { return attacher; }
    public void setAttacher(String attacher) { this.attacher = attacher; }
    public String getRegDate() { return regDate; }
    public void setRegDate(String regDate) { this.regDate = regDate; }
}
