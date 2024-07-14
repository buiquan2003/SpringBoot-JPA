package jpa.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jpa.spring.core.ResponseObject;
import jpa.spring.model.entities.Ad;
import jpa.spring.service.AdService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private final AdService adService;

    @PostMapping("")
    public ResponseEntity<ResponseObject<Ad>> createAd(@RequestBody Ad ad) {
        ResponseObject<Ad> object = new ResponseObject<>();
        Ad createdAd = adService.createAd(ad);
        object.setMessage("succsee");
        object.setData(createdAd);
        return new ResponseEntity<>(object, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<Ad>> getAllAds() {
        List<Ad> ads = adService.getAllAds();
        return new ResponseEntity<>(ads, HttpStatus.OK);
    }

    @GetMapping("/{adId}")
    public ResponseEntity<Ad> getAdById(@PathVariable Long adId) {
        Optional<Ad> ad = adService.getAdById(adId);
        return ad.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{adId}")
    public ResponseEntity<Ad> updateAd(@PathVariable Long adId, @RequestBody Ad ad) {
        Ad updatedAd = adService.updateAd(adId, ad);
        if (updatedAd != null) {
            return new ResponseEntity<>(updatedAd, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{adId}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long adId) {
        adService.deleteAd(adId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}