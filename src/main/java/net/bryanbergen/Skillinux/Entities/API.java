package net.bryanbergen.Skillinux.Entities;

import java.util.Calendar;

public class API {

    private long keyID;
    private String vCode;
    private long accessMask;
    private Calendar expiry;
    
    public void setKeyID(long keyID) {
        this.keyID = keyID;
    }

    public void setvCode(String vCode) {
        this.vCode = vCode;
    }

    public void setAccessMask(long accessMask) {
        this.accessMask = accessMask;
    }

    public void setExpiry(Calendar expiry) {
        this.expiry = expiry;
    }

    public long getKeyID() {
        return keyID;
    }

    public String getvCode() {
        return vCode;
    }

    public long getAccessMask() {
        return accessMask;
    }

    public Calendar getExpiry() {
        return expiry;
    }
}
