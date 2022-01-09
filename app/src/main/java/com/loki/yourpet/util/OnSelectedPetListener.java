package com.loki.yourpet.util;

import com.loki.yourpet.models.Animal;

import java.util.List;

public interface OnSelectedPetListener {
    public void onPetSelected(Integer position, List<Animal> animals, String source);
}
