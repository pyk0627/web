package com.keshe.crop.service;

import com.keshe.crop.model.Crop;
import com.keshe.crop.repository.CropRepository;
import com.keshe.crop.repository.FavoriteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final CropRepository cropRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, CropRepository cropRepository) {
        this.favoriteRepository = favoriteRepository;
        this.cropRepository = cropRepository;
    }

    public boolean isFavorite(Long userId, Long cropId) {
        return favoriteRepository.exists(userId, cropId);
    }

    @Transactional
    public boolean toggle(Long userId, Long cropId) {
        if (favoriteRepository.exists(userId, cropId)) {
            favoriteRepository.remove(userId, cropId);
            return false;
        }
        favoriteRepository.add(userId, cropId);
        return true;
    }

    public List<Crop> listByUser(Long userId) {
        List<Long> ids = favoriteRepository.findCropIdsByUserId(userId);
        if (ids.isEmpty()) {
            return List.of();
        }
        Map<Long, Integer> order = ids.stream()
                .collect(Collectors.toMap(Function.identity(), ids::indexOf, (a, b) -> a));
        return cropRepository.findByIds(ids).stream()
                .sorted(Comparator.comparingInt(crop -> order.getOrDefault(crop.getId(), Integer.MAX_VALUE)))
                .toList();
    }

    public long count() {
        return favoriteRepository.count();
    }

    public long countByUser(Long userId) {
        return favoriteRepository.countByUserId(userId);
    }
}
