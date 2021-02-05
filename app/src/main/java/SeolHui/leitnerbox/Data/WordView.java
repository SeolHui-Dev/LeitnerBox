package SeolHui.leitnerbox.Data;

public class WordView extends DataItem{
    String name;
    int seqno;

    public WordView() {
    }

    public WordView(int INCNO, String name, int seqno) {
        super(INCNO);
        this.name = name;
        this.seqno = seqno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
            query += " 'name' = " ;
        query += "'" + getName() + "' ";
        if(isModify)
            query += ",'seqno' = ";
        else
            query += ",";
        query += "'" + getSeqno() + "' ";

        return query;
    }
}
