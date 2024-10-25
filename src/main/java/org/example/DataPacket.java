package org.example;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class DataPacket implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private final ArrayList<String> dataList;
    private final long timestamp;

    public DataPacket(ArrayList<String> dataList, long timestamp)
    {
        this.dataList = dataList;
        this.timestamp = timestamp;
    }

    public ArrayList<String> getDataList()
    {
        return dataList;
    }

    public long getTimestamp()
    {
        return timestamp;
    }
}
