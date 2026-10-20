package com.keshe.crop.controller;

import com.keshe.crop.model.Crop;
import com.keshe.crop.repository.UserRepository;
import com.keshe.crop.service.CropService;
import com.keshe.crop.service.FavoriteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final CropService cropService;
    private final UserRepository userRepository;
    private final FavoriteService favoriteService;

    public HomeController(CropService cropService, UserRepository userRepository,
                          FavoriteService favoriteService) {
        this.cropService = cropService;
        this.userRepository = userRepository;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Crop> featured = cropService.featured(6);
        model.addAttribute("featuredCrops", featured);
        model.addAttribute("familyCounts", cropService.familyCounts());
        model.addAttribute("cropCount", cropService.count());
        model.addAttribute("oilCropCount", cropService.countWithOil());
        model.addAttribute("oilRecordCount", cropService.oilCount());
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("favoriteCount", favoriteService.count());
        model.addAttribute("families", cropService.families());
        return "index";
    }

    @GetMapping("/about")
    public String about() {
        return "about";
    }
}
