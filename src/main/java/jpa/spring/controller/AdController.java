package jpa.spring.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api")
public class AdController {

    @Autowired
    private final AdService adService;

    @PostMapping("/admin/ads/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Ad>> createAd(@RequestBody Ad ad) {
        ResponseObject<Ad> object = new ResponseObject<>();
        Ad createdAd = adService.createAd(ad);
        object.setMessage("succsee");
        object.setData(createdAd);
        return new ResponseEntity<>(object, HttpStatus.CREATED);
    }

    @GetMapping("/user/ads/getAll")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<List<Ad>>> getAllAds() {
        ResponseObject<List<Ad>> result = new ResponseObject<>();
        List<Ad> ads = adService.getAllAds();
        result.setMessage("get all list successfully ");
        result.setData(ads);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/user/ads/{adId}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Ad>> getAdById(@PathVariable Long adId) {
        ResponseObject<Ad> result = new ResponseObject<>();
        Optional<Ad> ad = adService.getAdById(adId);
        result.setMessage("get one Ad successfully");
        result.setData(ad.get());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/admin/ads/{adId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseObject<Ad>> updateAd(@PathVariable Long adId, @RequestBody Ad ad) {
        ResponseObject<Ad> result = new ResponseObject<>();
        Ad updatedAd = adService.updateAd(adId, ad);
        result.setMessage("update Ad successfully");
        result.setData(updatedAd);
        return new ResponseEntity<>(result, HttpStatus.OK);
      
    }

    @DeleteMapping("/admin/ads/{adId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteAd(@PathVariable Long adId) {
        adService.deleteAd(adId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}