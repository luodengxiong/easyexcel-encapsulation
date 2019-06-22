package com.howie.easyexcelmethodencapsulation.vo;

/**
 * @date: 2019/6/21 15:54:14
 **/
public class FileVO {


    String teamName;
    String filePath;
    String date;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public FileVO(String teamName, String filePath,String date){
        this.teamName=teamName;
        this.filePath=filePath;
        this.date=date;
    }
}
