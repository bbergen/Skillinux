package net.bryanbergen.Skillinux.Entities;

import java.util.Calendar;
import net.bryanbergen.Skillinux.Util.CalendarUtil;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class API {

    private long keyID;
    private String vCode;
    private long accessMask;
    private Calendar expiry;
    
    public API() {
        this(-1, null, -1, null);
    }
    
    public API(long keyID, String vCode, long accessMask, Calendar expiry) {
        this.keyID = keyID;
        this.vCode = vCode;
        this.accessMask = accessMask;
        this.expiry = expiry;
    }
    
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
    
    @Override
    public String toString() {
        StringBuilder api = new StringBuilder();
        api.append("KeyID=").append(keyID);
        api.append(",vCode=").append(vCode);
        api.append(",accessMask=").append(accessMask);
        api.append(",expiry=").append(CalendarUtil.getFormattedDate("yyyy-MM-dd", expiry));
        return api.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (this == o) {
            return true;
        } else if (!(o instanceof API)) {
            return false;
        }
        
        API api = (API)o;
        if (api.getKeyID() != this.keyID) {
            return false;
        } else if (!api.getvCode().equals(this.vCode)) {
            return false;
        } else if (api.getAccessMask() != this.accessMask) {
            return false;
        } else {
            return api.getExpiry().equals(this.expiry);
        } 
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17,31).
                append(keyID).
                append(vCode).
                append(accessMask).
                append(expiry).
                toHashCode();
    }
}
