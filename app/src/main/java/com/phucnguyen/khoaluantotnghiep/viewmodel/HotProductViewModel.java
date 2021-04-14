package com.phucnguyen.khoaluantotnghiep.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HotProductViewModel extends ViewModel  {
    MutableLiveData<Integer> selectedCategoryPos;

    public HotProductViewModel() {
        selectedCategoryPos = new MutableLiveData<Integer>(0); //default value is 0
    }

    public LiveData<Integer> getSelectedCategoryPos() {
        return selectedCategoryPos;
    }

    public void setSelectedCategoryPos(int selectedPos) {
        selectedCategoryPos.setValue(selectedPos);
    }
}
