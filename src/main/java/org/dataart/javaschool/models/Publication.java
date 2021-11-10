package org.dataart.javaschool.models;


import javax.persistence.*;

@Entity
@Table( name = "PUBLICATIONS" )
public class Publication {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="seq",sequenceName="INCREMENT", allocationSize=1)
    @Column(name = "ID")
    private int id;
    @Column (name = "ARTICLE")
    private String ARTICLE;
    @Column (name = "BODY", columnDefinition = "CLOB")
    private String BODY;
    @Column (name = "IMGNAME")
    private String IMGNAME;
    @Column (name = "TAG")
    private String TAG;

    public String getTAG() {
        switch (TAG)
        {
            case "SPORT":
                return "Спорт";
            case "WEATHER":
                return "Погода";
            case "POLITICS":
                return "Политика";
            case "GAMES":
                return "Игры";
            case "NATURE":
                return "Природа";
            case "HEALTH":
                return "Здоровье";
            case "TOURISM":
                return "Туризм";
            case "ANOTHER":
                return "Другое";
        }
        return "UNDIFINED_TAG";
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public String getIMGNAME() {
        return IMGNAME;
    }

    public void setIMGNAME(String IMGNAME) {
        this.IMGNAME = IMGNAME;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Publication()
    {

    }

    public Publication(int id, String Article, String Body, String IMGNAME, String TAG)
    {
        this.ARTICLE = Article;
        this.BODY = Body;
        this.id = id;
        this.IMGNAME = IMGNAME;
        this.TAG = TAG;
    }

    public boolean isInPage(int page, int maxId)
    {
        return (maxId-id+1)<=10*page && (maxId-id+1)>10*(page-1);
    }

    public String getArticle() {
        return ARTICLE;
    }

    public void setArticle(String article) {
        ARTICLE = article;
    }

    public String getBody() {
        return BODY;
    }

    public void setBody(String body) {
        BODY = body;
    }
}
