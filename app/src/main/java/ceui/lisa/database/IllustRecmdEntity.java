package ceui.lisa.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "illust_recmd_table")
public final class IllustRecmdEntity {

    @PrimaryKey()
    private int illustID;
    private String illustJson;
    private long time;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getIllustID() {
        return illustID;
    }

    public void setIllustID(int illustID) {
        this.illustID = illustID;
    }

    public String getIllustJson() {
        return illustJson;
    }

    public void setIllustJson(String illustJson) {
        this.illustJson = illustJson;
    }

    @Override
    public String toString() {
        return "IllustHistoryEntity{" +
                "illustID=" + illustID +
                ", illustJson='" + illustJson + '\'' +
                ", time=" + time +
                '}';
    }
}
