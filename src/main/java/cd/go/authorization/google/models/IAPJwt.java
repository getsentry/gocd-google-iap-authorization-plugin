package cd.go.authorization.google.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static cd.go.authorization.google.utils.Util.GSON;

public class IAPJwt {
    @SerializedName("jwt")
    @Expose
    private String jwt;

    IAPJwt() {
    }

    public IAPJwt(String jwt) {
        this.jwt = jwt;
    }

    public String jwt() {
        return jwt;
    }

    public String toJSON() {
        return GSON.toJson(this);
    }

    public static IAPJwt fromJSON(String json) {
        return GSON.fromJson(json, IAPJwt.class);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IAPJwt j = (IAPJwt) o;

        if (jwt != j.jwt) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return jwt.hashCode();
    }
}
