package ru.evgentrigub.android.photogallery;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;



public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mPhotoRecyclerView;
    private List<GalleryItem> mItems = new ArrayList<>();
    private GalleryItemAdapter mGalleryItemAdapter;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    public interface Listener {
        void onImageClicked(View view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();

        Log.i(TAG, "Background thread started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.photo_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mPhotoRecyclerView.setHasFixedSize(true);

        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mGalleryItemAdapter = new GalleryItemAdapter(mItems);
            mPhotoRecyclerView.setAdapter(mGalleryItemAdapter);
        }
    }
    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
//        mThumbnailDownloader.quit();
        Log.i(TAG, "Background thread destroy");
    }

    public static List<String> getStringsMonth(Context ctx) {
        List<String> items = new ArrayList<>();
        String arr[] = ctx.getResources().getStringArray(R.array.month);
        for (String s : arr) items.add(s);
        Collections.shuffle(items);
        return items;
    }

    private class GalleryItemAdapter extends RecyclerView.Adapter<ImageViewHolder> {

        private List<GalleryItem> items;
        private boolean isImageScaled = false;

        Listener mListener;


        public GalleryItemAdapter(List<GalleryItem> items) {
            this.items = items;
        }

        public GalleryItemAdapter(Listener listener) {
            mListener = listener;
        }


//        @Override
//        public GalleryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
//
//            return new GalleryItemViewHolder(view);
//        }

//        @Override
//        public GalleryItemAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
//            ImageViewHolder holder = ImageViewHolder.inflate(parent);
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mListener.onImageClicked(view);
//                }
//            });
//            return holder;
//        }


        @NonNull
        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageViewHolder holder = ImageViewHolder.inflate(parent);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), ActivityFullTransition.class);
//                    ActivityOptionsCompat options = ActivityOptionsCompat
//                            .makeSceneTransitionAnimation(Objects.requireNonNull(getActivity()), view, getString(R.string.transition_test));
                    startActivity(intent);
                }
            });

            return holder;
        }

        public void onBindViewHolder(ImageViewHolder holder, int position){
            holder.setResource(items.get(position), getActivity());

        }


//        public void onBindViewHolder(ImageViewHolder holder, final int position) {
//            holder.setResource(items.get(position));
//            ImageViewHolder view = (ImageViewHolder) holder;
//            view.ivItem.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //открытие фотки в полный размер!
////                    fullImageDialog(view);
//
//                }
//            });
//        }
        public void fullImageDialog(View view){
            final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.activity_full_transition);
            dialog.setCancelable(true);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.show();
            dialog.getWindow().setAttributes(lp);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }


//        class GalleryItemViewHolder extends RecyclerView.ViewHolder{
//
//            private ImageView ivItem;;
//
//            public GalleryItemViewHolder(View v) {
//                super(v);
//                ivItem = v.findViewById(R.id.iv_item);
//            }
//
//            void setResource(GalleryItem item){
//                Picasso.with(getActivity())
//                        .load(item.getUrl())
//                        .noFade()
//                        .into(ivItem);
//            }
//        }

    }

    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    private class FetchItemsTask extends AsyncTask<Void,Void,List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
            return new FlickrFetchr().fetchItems();
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }
}
