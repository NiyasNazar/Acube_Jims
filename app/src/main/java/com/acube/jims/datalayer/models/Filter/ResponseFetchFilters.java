
package com.acube.jims.datalayer.models.Filter;

import java.util.List;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseFetchFilters {

    @SerializedName("catresult")
    @Expose
    private List<Catresult> catresult = null;
    @SerializedName("colorresult")
    @Expose
    private List<Colorresult> colorresult = null;
    @SerializedName("karatresult")
    @Expose
    private List<Karatresult> karatresult = null;

    public List<Catresult> getCatresult() {
        return catresult;
    }

    public void setCatresult(List<Catresult> catresult) {
        this.catresult = catresult;
    }

    public List<Colorresult> getColorresult() {
        return colorresult;
    }

    public void setColorresult(List<Colorresult> colorresult) {
        this.colorresult = colorresult;
    }

    public List<Karatresult> getKaratresult() {
        return karatresult;
    }

    public void setKaratresult(List<Karatresult> karatresult) {
        this.karatresult = karatresult;
    }

}
