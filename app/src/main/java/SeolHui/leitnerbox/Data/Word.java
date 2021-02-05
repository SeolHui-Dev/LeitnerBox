package SeolHui.leitnerbox.Data;

public class Word extends DataItem{
    String data;
    int box_INCNO;
    int seqno;
    public Word(){

    }

    public Word(int INCNO, String data, int box_INCNO, int seqno) {
        super(INCNO);
        this.data = data;
        this.box_INCNO = box_INCNO;
        this.seqno = seqno;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getBox_INCNO() {
        return box_INCNO;
    }

    public void setBox_INCNO(int box_INCNO) {
        this.box_INCNO = box_INCNO;
    }

    public int getSeqno() {
        return seqno;
    }

    public void setSeqno(int seqno) {
        this.seqno = seqno;
    }

    public String getQueryColumns(boolean isModify) {
        String query = "";
        if(isModify)
            query += " 'data' = " ;
        query += "'" + getData() + "' ";
        if(isModify)
            query += ",'box_INCNO' = ";
        else
            query += ",";
        query += "'" + getBox_INCNO() + "' ";
        if(isModify)
            query += ",'seqno' = ";
        else
            query += ",";
        query += "'" + getSeqno() + "' ";
        return query;
    }
}
