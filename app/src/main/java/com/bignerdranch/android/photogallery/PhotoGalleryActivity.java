package com.bignerdranch.android.photogallery;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected android.support.v4.app.Fragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
