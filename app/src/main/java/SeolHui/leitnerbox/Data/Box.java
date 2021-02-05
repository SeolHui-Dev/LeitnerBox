package SeolHui.leitnerbox.Data;

public class Box extends DataItem{
    String name;
    int wordView_INCNO;
    public Box(){

    }
    public Box(int INCNO,String name, int wordView_INCNO){
        super(INCNO);
        this.name = name;
        this.wordView_INCNO = wordView_INCNO;
    }

    public int getWordView_INCNO() {
        return wordView_INCNO;
    }

    public void setWordView_INCNO(int wordView_INCNO) {
        this.wordView_INCNO = wordView_INCNO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getQueryColumns(boolean isModify) {
        String query = "";
        if(isModify)
            query += " 'name' = " ;
        query += "'" + getName() + "' ";
        if(isModify)
            query += ",'wordView_INCNO' = ";
        else
            query += ",";
        query += "'" + getWordView_INCNO() + "' ";

        return query;
    }
}
