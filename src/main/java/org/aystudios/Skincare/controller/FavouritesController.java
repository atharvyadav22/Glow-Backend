package org.aystudios.Skincare.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.aystudios.Skincare.dto.FavouriteResponseDTO;
import org.aystudios.Skincare.dto.ProductResponseDTO;
import org.aystudios.Skincare.service.FavouritesService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.aystudios.Skincare.security.CustomUserDetails;
import java.util.List;

@RestController
@RequestMapping("/api/favourites")
@Tag(name = "5. Favourites")
public class FavouritesController {

    private final FavouritesService favouritesService;

    public FavouritesController(FavouritesService favouritesService){
        this.favouritesService = favouritesService;
    }


    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllFavourites(@AuthenticationPrincipal CustomUserDetails user){
        List<ProductResponseDTO> response = favouritesService.getFavourites(user.getId());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<FavouriteResponseDTO> isFavourite(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long productId){
        FavouriteResponseDTO response = favouritesService.isFavourite(user.getId(), productId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}")
    public ResponseEntity<FavouriteResponseDTO> toggleFavourites(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long productId){

        FavouriteResponseDTO responseDTO = favouritesService.toggleFavourite(user.getId(), productId);
        return ResponseEntity.ok(responseDTO);
    }
}
