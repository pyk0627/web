package com.keshe.crop.controller;

import com.keshe.crop.filter.AuthFilter;
import com.keshe.crop.model.Crop;
import com.keshe.crop.model.OilRecord;
import com.keshe.crop.model.User;
import com.keshe.crop.service.CropService;
import com.keshe.crop.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CropController {

    private final CropService cropService;
    private final FavoriteService favoriteService;

    public CropController(CropService cropService, FavoriteService favoriteService) {
        this.cropService = cropService;
        this.favoriteService = favoriteService;
    }

    @GetMapping("/crop/{id}")
    public String detail(@PathVariable Long id, HttpSession session, Model model) {
        Crop crop = cropService.requireCrop(id);
        List<OilRecord> oilRecords = cropService.oilRecords(id);
        User user = (User) session.getAttribute(AuthFilter.SESSION_USER);
        boolean favorite = user != null && favoriteService.isFavorite(user.getId(), id);

        model.addAttribute("crop", crop);
        model.addAttribute("oilRecords", oilRecords);
        model.addAttribute("relatedCrops", cropService.related(crop, 4));
        model.addAttribute("favorite", favorite);
        model.addAttribute("externalLinks", buildExternalLinks(crop));
        return "crop/detail";
    }

    @PostMapping("/favorites/{id}")
    public String toggleFavorite(@PathVariable Long id, HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute(AuthFilter.SESSION_USER);
        if (user == null) {
            return "redirect:/login";
        }
        Crop crop = cropService.requireCrop(id);
        boolean added = favoriteService.toggle(user.getId(), id);
        redirectAttributes.addFlashAttribute("message",
                added ? "已收藏“" + crop.getChineseName() + "”" : "已取消收藏");
        return "redirect:/crop/" + id;
    }

    @GetMapping("/favorites")
    public String favorites(HttpSession session, Model model) {
        User user = (User) session.getAttribute(AuthFilter.SESSION_USER);
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("favoriteCrops", favoriteService.listByUser(user.getId()));
        return "favorites";
    }

    private List<Map<String, String>> buildExternalLinks(Crop crop) {
        List<Map<String, String>> links = new ArrayList<>();
        if (crop.getReferenceUrl() != null && !crop.getReferenceUrl().isBlank()) {
            links.add(link(crop.getReferenceTitle() == null ? "项目参考资料" : crop.getReferenceTitle(),
                    crop.getReferenceUrl(), "参考资料"));
        }
        links.add(link("百度百科：搜索“" + crop.getChineseName() + "”",
                "https://baike.baidu.com/item/" + encode(crop.getChineseName()), "中文百科"));
        if (crop.getLatinName() != null && !crop.getLatinName().isBlank()) {
            links.add(link("GBIF 全球生物多样性信息网络",
                    "https://www.gbif.org/species/search?q=" + encode(crop.getLatinName()), "国际数据库"));
            links.add(link("Plants of the World Online（Kew）",
                    "https://powo.science.kew.org/results?q=" + encode(crop.getLatinName()), "国际数据库"));
        }
        links.add(link("iPlant 中国植物志",
                "https://www.iplant.cn/info/" + encode(crop.getChineseName()), "国内数据库"));
        return links;
    }

    private Map<String, String> link(String title, String url, String source) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("title", title);
        map.put("url", url);
        map.put("source", source);
        return map;
    }

    private String encode(String text) {
        return URLEncoder.encode(text, StandardCharsets.UTF_8);
    }
}
