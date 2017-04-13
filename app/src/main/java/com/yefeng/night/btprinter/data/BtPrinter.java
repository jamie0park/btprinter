package com.yefeng.night.btprinter.data;

/**
 * Created by jamie0park on 02/03/2017.
 */

public class BtPrinter {
    String macAddr;
    String posId;
    String btNm;
    String printCd;
    String useYn;
    int rank;
    String pairYn;
    String regDtm;

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getPosId() {
        return posId;
    }

    public void setPosId(String posId) {
        this.posId = posId;
    }

    public String getBtNm() {
        return btNm;
    }

    public void setBtNm(String btNm) {
        this.btNm = btNm;
    }

    public String getPrintCd() {
        return printCd;
    }

    public void setPrintCd(String printCd) {
        this.printCd = printCd;
    }

    public String getUseYn() {
        return useYn;
    }

    public void setUseYn(String useYn) {
        this.useYn = useYn;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getPairYn() {
        return pairYn;
    }

    public void setPairYn(String pairYn) {
        this.pairYn = pairYn;
    }

    public String getRegDtm() {
        return regDtm;
    }

    public void setRegDtm(String regDtm) {
        this.regDtm = regDtm;
    }
}
