package com.keshe.crop.service;

import com.keshe.crop.model.Crop;
import com.keshe.crop.model.CropForm;
import com.keshe.crop.model.FamilyCount;
import com.keshe.crop.model.OilRecord;
import com.keshe.crop.model.OilRecordForm;
import com.keshe.crop.model.PageResult;
import com.keshe.crop.model.SearchCriteria;
import com.keshe.crop.repository.CropRepository;
import com.keshe.crop.repository.OilRecordRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CropService {

    private final CropRepository cropRepository;
    private final OilRecordRepository oilRecordRepository;

    public CropService(CropRepository cropRepository, OilRecordRepository oilRecordRepository) {
        this.cropRepository = cropRepository;
        this.oilRecordRepository = oilRecordRepository;
    }

    public PageResult<Crop> search(SearchCriteria criteria) {
        return cropRepository.search(criteria);
    }

    public Crop requireCrop(Long id) {
        return cropRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "作物不存在"));
    }

    public List<OilRecord> oilRecords(Long cropId) {
        return oilRecordRepository.findByCropId(cropId);
    }

    public List<Crop> related(Crop crop, int limit) {
        if (crop.getFamily() == null || crop.getFamily().isBlank()) {
            return List.of();
        }
        return cropRepository.findRelated(crop.getFamily(), crop.getId(), limit);
    }

    public List<Crop> findByIds(List<Long> ids) {
        return cropRepository.findByIds(ids);
    }

    public List<Crop> featured(int limit) {
        return cropRepository.findFeatured(limit);
    }

    public List<Crop> recent(int limit) {
        return cropRepository.findRecent(limit);
    }

    public List<String> families() {
        return cropRepository.findFamilies();
    }

    public List<String> genera() {
        return cropRepository.findGenera();
    }

    public List<String> generaByFamily(String family) {
        return cropRepository.findGeneraByFamily(family);
    }

    public List<FamilyCount> familyCounts() {
        return cropRepository.countByFamily();
    }

    public List<Crop> cropOptions() {
        return cropRepository.findAllForSelect();
    }

    public long count() {
        return cropRepository.count();
    }

    public long countWithOil() {
        return cropRepository.countWithOil();
    }

    @Transactional
    public Crop create(CropForm form) {
        if (cropRepository.existsByName(form.getChineseName(), null)) {
            throw new IllegalArgumentException("中文名称为“" + form.getChineseName() + "”的作物已存在");
        }
        Crop crop = form.toCrop();
        Long id = cropRepository.save(crop);
        crop.setId(id);
        return crop;
    }

    @Transactional
    public Crop update(Long id, CropForm form) {
        requireCrop(id);
        if (cropRepository.existsByName(form.getChineseName(), id)) {
            throw new IllegalArgumentException("中文名称为“" + form.getChineseName() + "”的作物已存在");
        }
        Crop crop = form.toCrop();
        crop.setId(id);
        cropRepository.update(crop);
        return requireCrop(id);
    }

    @Transactional
    public void delete(Long id) {
        requireCrop(id);
        cropRepository.delete(id);
    }

    public List<OilRecord> allOilRecords(Long cropId, String keyword) {
        return oilRecordRepository.findAll(cropId, keyword);
    }

    public List<OilRecord> recentOilRecords(int limit) {
        return oilRecordRepository.findRecent(limit);
    }

    public OilRecord requireOilRecord(Long id) {
        return oilRecordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "含油率记录不存在"));
    }

    @Transactional
    public OilRecord createOil(OilRecordForm form) {
        requireCrop(form.getCropId());
        OilRecord record = form.toOilRecord();
        Long id = oilRecordRepository.save(record);
        record.setId(id);
        return record;
    }

    @Transactional
    public OilRecord updateOil(Long id, OilRecordForm form) {
        requireOilRecord(id);
        requireCrop(form.getCropId());
        OilRecord record = form.toOilRecord();
        record.setId(id);
        oilRecordRepository.update(record);
        return requireOilRecord(id);
    }

    @Transactional
    public void deleteOil(Long id) {
        requireOilRecord(id);
        oilRecordRepository.delete(id);
    }

    public long oilCount() {
        return oilRecordRepository.count();
    }
}
