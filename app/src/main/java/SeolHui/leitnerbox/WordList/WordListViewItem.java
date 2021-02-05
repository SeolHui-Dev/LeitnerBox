package SeolHui.leitnerbox.WordList;

public class WordListViewItem {
    int image;
    String imagetitle;

    public int getImage(){
        return image;
    }
    public String getImagetitle(){
        return imagetitle;
    }
    public WordListViewItem(int image, String imagetitle){
        this.image=image;
        this.imagetitle=imagetitle;
    }

}
