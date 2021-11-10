package org.dataart.javaschool.controllers;

import org.dataart.javaschool.dao.PublicationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Controller
@RequestMapping("/")
public class PublicationController {
    private final PublicationDAO publicationDAO;

    @Autowired
    public PublicationController(PublicationDAO publicationDAO) {
        this.publicationDAO = publicationDAO;
    }

    @GetMapping("")
    public String showAllPublications(Model model) {
        model.addAttribute("status", 0);
        model.addAttribute("publications", publicationDAO.showAllPublications("%%"));
        model.addAttribute("page", publicationDAO.currentPage(1));
        return "all";
    }

    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public String pagesShow(@PathVariable("page") int page, Model model) {
        model.addAttribute("status", 0);
        model.addAttribute("publications", publicationDAO.showAllPublications("%%"));
        model.addAttribute("page", publicationDAO.currentPage(page));
        return "all";
    }

    @RequestMapping(value = "/tag/{TAG}", method = RequestMethod.GET)
    public String tagesShow(@PathVariable("TAG") String tag, Model model) {
        model.addAttribute("status", 0);
        model.addAttribute("publications", publicationDAO.showAllPublications(tag));
        model.addAttribute("page", publicationDAO.currentPage(1));
        return "tag/taggedPage";
    }

   @GetMapping("publications/{id}")
   public String showThisPublish(@PathVariable("id") int id, Model model) {
       model.addAttribute("publication", publicationDAO.showThisPublish(id));
       return "publications/show";
    }

    @RequestMapping(value = "/{page}", method = RequestMethod.POST)
    public String addPublication(@RequestParam("file") MultipartFile file,
                                 Model model ,
                                 @PathVariable(required = false) int page)
                                throws IOException {
        page = page == 0? 1:page;
        model.addAttribute("status", publicationDAO.addPublication(file, "%%"));
        model.addAttribute("publications", publicationDAO.showAllPublications("%%"));
        model.addAttribute("page", publicationDAO.currentPage(page));
        return "all";
    }
}