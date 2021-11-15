package org.dataart.javaschool.dao;

import org.dataart.javaschool.HibernateUtils;
import org.dataart.javaschool.models.Publication;
import org.dataart.javaschool.servise.PathServise;
import org.hibernate.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Query;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@ConfigurationProperties(prefix = "storage")
@Component
public class PublicationDAO {

    SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
    private List <Publication> publications;

    public int addPublication(MultipartFile file, String tag) throws IOException {
        PathServise pathServise = new PathServise();
        File fileD = pathServise.getPath().toFile();

        file.transferTo(Paths.get(fileD + "/" + file.getOriginalFilename()));
        ZipInputStream zin = new ZipInputStream(new FileInputStream(fileD + "/" + file.getOriginalFilename()));
        ZipEntry entry;
        String Article ="";
        String Body = "";
        StringBuilder ImgName = new StringBuilder(String.valueOf(java.util.UUID.randomUUID()));

        if((entry = zin.getNextEntry())!=null){
            do {
                String fileName = entry.getName();
                switch (fileName) {
                    case "article.txt":
                        byte[] buffer = new byte[(int) entry.getSize()];
                        zin.read(buffer);
                        String text = new String(buffer, StandardCharsets.UTF_8);

                        if (text.contains("\r\n")) {
                            Article = text.substring(0, text.indexOf("\r\n"));
                            Body = text.substring(text.indexOf("\r\n") + 2);
                        }

                        if (Body.equals("")|| Article.equals("")) {
                            zin.close();
                            new File(fileD + "/" + file.getOriginalFilename()).delete();
                            return 2; // Body or(and) Article is not stated
                        }

                        break;

                    case "img.jpg":
                        File fileIMG = pathServise.getPath().toFile();
                        byte[] bufferImg = new byte[(int) entry.getSize()];
                        ImgName.append(entry.getName());
                        FileOutputStream fos = new FileOutputStream(fileIMG+"/images/" + ImgName);
                        int len;
                        while ((len = zin.read(bufferImg)) > 0) {
                            fos.write(bufferImg, 0, len);
                        }
                        fos.close();
                        break;
                    default:
                        zin.close();
                        new File(fileD + "/" + file.getOriginalFilename()).delete();
                        return 3; //Redundant files detected
                }
            }
            while((entry = zin.getNextEntry())!=null);
        }
        else
        {
            zin.close();
            new File(fileD + "/" + file.getOriginalFilename()).delete();
            return 4; //Empty ZIP;
        }
        zin.close();
        Session session = sessionFactory.openSession();
        Publication publ = new Publication(1, Article, Body, ImgName.toString(), tag);
        Transaction tx = null;
        try{
            tx = session.getTransaction();
            tx.begin();
            session.save(publ);
            tx.commit();
        }
        catch (HibernateException e) {
            if (tx!=null) tx.rollback();
            e.printStackTrace();
        }
        finally {
            session.close();
        }
        return 1; //Succeeded
    }

    public List <Publication> showAllPublications(String TAGo)

    {
        TAGo = "%"+TAGo+"%";
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("FROM Publication WHERE TAG LIKE :TAGo");
        query.setParameter("TAGo", TAGo);
        publications = query.getResultList();
        List<Publication> Copy = publications.subList(0, publications.size());
        Collections.reverse(Copy);
        session.close();
        return Copy;
    }

    public int currentPage(int id){
        return id;
    }

    public Publication showThisPublish(int id)
    {
        showAllPublications("%%");
        return publications.stream().filter(publications->publications.getId() == id).findAny().orElse(null);
    }
}
