package org.aystudios.Skincare.controller;

import org.aystudios.Skincare.dto.FavouriteResponseDTO;
import org.aystudios.Skincare.dto.ProductResponseDTO;
import org.aystudios.Skincare.service.FavouritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/favourites")
public class FavouritesController {

    //FIXME: Use Long Id instead of String email for auth
    private final FavouritesService favouritesService;

    public FavouritesController(FavouritesService favouritesService){
        this.favouritesService = favouritesService;
    }


    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllFavourites(@AuthenticationPrincipal String email){
        List<ProductResponseDTO> response = favouritesService.getFavourites(email);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<FavouriteResponseDTO> isFavourite(@AuthenticationPrincipal String email, @PathVariable Long productId){
        FavouriteResponseDTO response = favouritesService.isFavourite(email, productId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<FavouriteResponseDTO> toggleFavourites(@AuthenticationPrincipal String email, @PathVariable Long productId){

        FavouriteResponseDTO responseDTO = favouritesService.toggleFavourite(email, productId);
        return ResponseEntity.ok(responseDTO);
    }
}
