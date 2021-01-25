package lucie.alchemist.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

// Generated using: http://www.jsonschema2pojo.org/
public class DataThieving
{
    @SerializedName("disable")
    @Expose
    private boolean disable;

    @SerializedName("tag")
    @Expose
    private String tag;

    @SerializedName("table")
    @Expose
    private String table;

    public boolean getDisable()
    {
        return disable;
    }

    public void setDisable(boolean disable)
    {
        this.disable = disable;
    }

    public String getTag()
    {
        return tag;
    }

    public void setTag(String tag)
    {
        this.tag = tag;
    }

    public String getTable()
    {
        return table;
    }

    public void setTable(String table)
    {
        this.table = table;
    }
}