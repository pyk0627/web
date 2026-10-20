package com.keshe.crop.controller;

import com.keshe.crop.filter.AuthFilter;
import com.keshe.crop.model.Crop;
import com.keshe.crop.model.CropForm;
import com.keshe.crop.model.OilRecord;
import com.keshe.crop.model.OilRecordForm;
import com.keshe.crop.model.Role;
import com.keshe.crop.model.SearchCriteria;
import com.keshe.crop.model.User;
import com.keshe.crop.repository.UserRepository;
import com.keshe.crop.service.CropService;
import com.keshe.crop.service.FavoriteService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final CropService cropService;
    private final UserRepository userRepository;
    private final FavoriteService favoriteService;

    public AdminController(CropService cropService, UserRepository userRepository,
                           FavoriteService favoriteService) {
        this.cropService = cropService;
        this.userRepository = userRepository;
        this.favoriteService = favoriteService;
    }

    // ==================== 后台首页 ====================

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("cropCount", cropService.count());
        model.addAttribute("oilCount", cropService.oilCount());
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("favoriteCount", favoriteService.count());
        model.addAttribute("familyCounts", cropService.familyCounts());
        model.addAttribute("recentCrops", cropService.recent(5));
        model.addAttribute("recentOils", cropService.recentOilRecords(5));
        return "admin/dashboard";
    }

    // ==================== 作物维护 ====================

    @GetMapping("/crops")
    public String crops(@RequestParam(required = false) String keyword,
                        @RequestParam(defaultValue = "1") int page,
                        Model model) {
        SearchCriteria criteria = new SearchCriteria();
        criteria.setKeyword(keyword);
        criteria.setPage(Math.max(1, page));
        criteria.setSize(10);
        criteria.setSort("newest");
        model.addAttribute("result", cropService.search(criteria));
        model.addAttribute("criteria", criteria);
        model.addAttribute("families", cropService.families());
        return "admin/crops";
    }

    @GetMapping("/crops/new")
    public String newCrop(Model model) {
        model.addAttribute("cropForm", new CropForm());
        model.addAttribute("mode", "create");
        addTaxonomyOptions(model);
        return "admin/crop-form";
    }

    @PostMapping("/crops")
    public String createCrop(@Valid @ModelAttribute("cropForm") CropForm form,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            addTaxonomyOptions(model);
            return "admin/crop-form";
        }
        try {
            Crop crop = cropService.create(form);
            redirectAttributes.addFlashAttribute("message", "已新增作物：" + crop.getChineseName());
            return "redirect:/admin/crops";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("cropError", e.getMessage());
            model.addAttribute("mode", "create");
            addTaxonomyOptions(model);
            return "admin/crop-form";
        }
    }

    @GetMapping("/crops/{id}/edit")
    public String editCrop(@PathVariable Long id, Model model) {
        Crop crop = cropService.requireCrop(id);
        model.addAttribute("cropForm", CropForm.from(crop));
        model.addAttribute("mode", "edit");
        model.addAttribute("crop", crop);
        addTaxonomyOptions(model);
        return "admin/crop-form";
    }

    @PostMapping("/crops/{id}")
    public String updateCrop(@PathVariable Long id,
                             @Valid @ModelAttribute("cropForm") CropForm form,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("crop", cropService.requireCrop(id));
            addTaxonomyOptions(model);
            return "admin/crop-form";
        }
        try {
            Crop crop = cropService.update(id, form);
            redirectAttributes.addFlashAttribute("message", "已保存作物：" + crop.getChineseName());
            return "redirect:/admin/crops";
        } catch (IllegalArgumentException e) {
            bindingResult.reject("cropError", e.getMessage());
            model.addAttribute("mode", "edit");
            model.addAttribute("crop", cropService.requireCrop(id));
            addTaxonomyOptions(model);
            return "admin/crop-form";
        }
    }

    @PostMapping("/crops/{id}/delete")
    public String deleteCrop(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Crop crop = cropService.requireCrop(id);
        cropService.delete(id);
        redirectAttributes.addFlashAttribute("message", "已删除作物：" + crop.getChineseName());
        return "redirect:/admin/crops";
    }

    // ==================== 含油率记录维护 ====================

    @GetMapping("/oils")
    public String oils(@RequestParam(required = false) Long cropId,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        List<OilRecord> records = cropService.allOilRecords(cropId, keyword);
        model.addAttribute("oilRecords", records);
        model.addAttribute("cropOptions", cropService.cropOptions());
        model.addAttribute("selectedCropId", cropId);
        model.addAttribute("keyword", keyword);
        return "admin/oils";
    }

    @GetMapping("/oils/new")
    public String newOil(@RequestParam(required = false) Long cropId, Model model) {
        OilRecordForm form = new OilRecordForm();
        form.setCropId(cropId);
        model.addAttribute("oilRecordForm", form);
        model.addAttribute("mode", "create");
        model.addAttribute("cropOptions", cropService.cropOptions());
        return "admin/oil-form";
    }

    @PostMapping("/oils")
    public String createOil(@Valid @ModelAttribute("oilRecordForm") OilRecordForm form,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "create");
            model.addAttribute("cropOptions", cropService.cropOptions());
            return "admin/oil-form";
        }
        try {
            OilRecord record = cropService.createOil(form);
            redirectAttributes.addFlashAttribute("message", "已新增含油率记录");
            return "redirect:/admin/oils?cropId=" + record.getCropId();
        } catch (IllegalArgumentException e) {
            bindingResult.reject("oilError", e.getMessage());
            model.addAttribute("mode", "create");
            model.addAttribute("cropOptions", cropService.cropOptions());
            return "admin/oil-form";
        }
    }

    @GetMapping("/oils/{id}/edit")
    public String editOil(@PathVariable Long id, Model model) {
        OilRecord record = cropService.requireOilRecord(id);
        model.addAttribute("oilRecordForm", OilRecordForm.from(record));
        model.addAttribute("mode", "edit");
        model.addAttribute("record", record);
        model.addAttribute("cropOptions", cropService.cropOptions());
        return "admin/oil-form";
    }

    @PostMapping("/oils/{id}")
    public String updateOil(@PathVariable Long id,
                            @Valid @ModelAttribute("oilRecordForm") OilRecordForm form,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("mode", "edit");
            model.addAttribute("record", cropService.requireOilRecord(id));
            model.addAttribute("cropOptions", cropService.cropOptions());
            return "admin/oil-form";
        }
        OilRecord record = cropService.updateOil(id, form);
        redirectAttributes.addFlashAttribute("message", "已保存含油率记录");
        return "redirect:/admin/oils?cropId=" + record.getCropId();
    }

    @PostMapping("/oils/{id}/delete")
    public String deleteOil(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        OilRecord record = cropService.requireOilRecord(id);
        cropService.deleteOil(id);
        redirectAttributes.addFlashAttribute("message", "已删除含油率记录");
        return "redirect:/admin/oils?cropId=" + record.getCropId();
    }

    // ==================== 用户维护 ====================

    @GetMapping("/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("adminCount", userRepository.countAdmins());
        return "admin/users";
    }

    @PostMapping("/users/{id}/role")
    public String toggleRole(@PathVariable Long id, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User current = (User) session.getAttribute(AuthFilter.SESSION_USER);
        User target = userRepository.findById(id).orElseThrow();
        if (current != null && current.getId().equals(target.getId())) {
            redirectAttributes.addFlashAttribute("error", "不能修改自己的角色");
            return "redirect:/admin/users";
        }
        Role newRole = target.getRole() == Role.ADMIN ? Role.USER : Role.ADMIN;
        if (target.getRole() == Role.ADMIN && newRole == Role.USER && userRepository.countAdmins() <= 1) {
            redirectAttributes.addFlashAttribute("error", "系统至少需要保留一个管理员");
            return "redirect:/admin/users";
        }
        userRepository.updateRole(id, newRole);
        redirectAttributes.addFlashAttribute("message", "已更新用户角色：" + target.getUsername() + " → " + newRole.getLabel());
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/enabled")
    public String toggleEnabled(@PathVariable Long id, HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User current = (User) session.getAttribute(AuthFilter.SESSION_USER);
        User target = userRepository.findById(id).orElseThrow();
        if (current != null && current.getId().equals(target.getId())) {
            redirectAttributes.addFlashAttribute("error", "不能停用自己的账号");
            return "redirect:/admin/users";
        }
        userRepository.updateEnabled(id, !target.isEnabled());
        redirectAttributes.addFlashAttribute("message", "已" + (target.isEnabled() ? "停用" : "启用")
                + "账号：" + target.getUsername());
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User current = (User) session.getAttribute(AuthFilter.SESSION_USER);
        User target = userRepository.findById(id).orElseThrow();
        if (current != null && current.getId().equals(target.getId())) {
            redirectAttributes.addFlashAttribute("error", "不能删除自己的账号");
            return "redirect:/admin/users";
        }
        if (target.getRole() == Role.ADMIN && userRepository.countAdmins() <= 1) {
            redirectAttributes.addFlashAttribute("error", "系统至少需要保留一个管理员");
            return "redirect:/admin/users";
        }
        userRepository.delete(id);
        redirectAttributes.addFlashAttribute("message", "已删除用户：" + target.getUsername());
        return "redirect:/admin/users";
    }

    private void addTaxonomyOptions(Model model) {
        model.addAttribute("families", cropService.families());
        model.addAttribute("genera", cropService.genera());
    }
}
