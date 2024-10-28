package jpa.spring.service;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jpa.spring.model.entities.Ad;
import jpa.spring.repository.AdRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdService {

    @Autowired
    private final AdRepository adRepository;

    public Ad createAd(Ad ad) {
        return adRepository.save(ad);
    }

    public List<Ad> getAllAds() {
        return adRepository.findAll();
    }

    public Optional<Ad> getAdById(Long adId) {
        return adRepository.findById(adId);
    }

    public Ad updateAd(Long adId, Ad updatedAd) {
        Optional<Ad> optionalAd = adRepository.findById(adId);
        if (optionalAd.isPresent()) {
            Ad existingAd = optionalAd.get();
            existingAd.setTitle(updatedAd.getTitle());
            existingAd.setContent(updatedAd.getContent());
            existingAd.setStartDate(updatedAd.getStartDate());
            existingAd.setEndDate(updatedAd.getEndDate());
            existingAd.setDelFlag(updatedAd.getDelFlag());
            return adRepository.save(existingAd);
        }
        return null; // Or throw an exception
    }

    public void deleteAd(Long adId) {
        adRepository.deleteById(adId);
    }
}