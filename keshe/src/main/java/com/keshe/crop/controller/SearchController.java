package com.keshe.crop.controller;

import com.keshe.crop.model.Crop;
import com.keshe.crop.model.PageResult;
import com.keshe.crop.model.ResultField;
import com.keshe.crop.model.SearchCriteria;
import com.keshe.crop.service.CropService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SearchController {

    private final CropService cropService;

    public SearchController(CropService cropService) {
        this.cropService = cropService;
    }

    @GetMapping("/search")
    public String search(@ModelAttribute("criteria") SearchCriteria criteria, Model model) {
        criteria.normalize();

        List<String> requested = criteria.getFields();
        List<ResultField> selectedFields = new ArrayList<>();
        if (requested == null || requested.isEmpty()) {
            selectedFields.addAll(ResultField.defaults());
        } else if (requested.contains("all")) {
            selectedFields.addAll(ResultField.all());
        } else {
            for (String code : requested) {
                ResultField field = ResultField.fromCode(code);
                if (field != null && !selectedFields.contains(field)) {
                    selectedFields.add(field);
                }
            }
            if (selectedFields.isEmpty()) {
                selectedFields.addAll(ResultField.defaults());
            }
        }
        criteria.setFields(selectedFields.stream().map(ResultField::getCode).toList());

        PageResult<Crop> result = cropService.search(criteria);
        model.addAttribute("result", result);
        model.addAttribute("selectedFields", selectedFields);
        model.addAttribute("selectedCodes", criteria.getFields());
        model.addAttribute("allFields", ResultField.all());
        model.addAttribute("families", cropService.families());
        model.addAttribute("genera", cropService.genera());
        model.addAttribute("sizeOptions", List.of(5, 10, 20, 50));
        return "search";
    }
}
