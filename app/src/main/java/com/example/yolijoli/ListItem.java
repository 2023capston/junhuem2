package com.example.yolijoli;

public class ListItem {
    /* 아이템의 정보를 담기 위한 클래스 */

    String id;
    String RCP_NM;
    String rcp_parts_dtls;
    String att_file_no_main;
    public ListItem(String id, String rcp_nm, String rcp_parts_dtls, String att_file_no_main) {
        this.RCP_NM = rcp_nm;
        this.id = id;
        this.rcp_parts_dtls = rcp_parts_dtls;
        this.att_file_no_main = att_file_no_main;
    }
    public String getID() {
        return id;
    }
    public void setID(String id) {
            this.id = id;
    }
    public String getRcp_nm() {
        return RCP_NM;
    }
    public void setRcp_nm(String rcp_nm) {
        this.RCP_NM = rcp_nm;
    }
    public String getAtt_file_no_main() {
        return att_file_no_main;
    }
    public void setAtt_file_no_main(String rcp_nm) {
        this.att_file_no_main = att_file_no_main;
    }
}
