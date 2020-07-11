package edu.neu.stickemup;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // Constructor
    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }
        String imageUrl = mThumbIds.get(position);
        Picasso.get().load(imageUrl).into(imageView);
        return imageView;
    }

    public ArrayList<String> mThumbIds = new ArrayList<>(Arrays.asList("https://cdn.shopify.com/s/files/1/1061/1924/products/Nerd_with_Glasses_Emoji_2a8485bc-f136-4156-9af6-297d8522d8d1_large.png?v=1571606036",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Hungry_Face_Emoji_large.png?v=1571606037",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Wink_Emoji_large.png?v=1571606035",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Fearful_Face_Emoji_large.png?v=1571606037",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Flushed_Face_Emoji_large.png?v=1571606037",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Slightly_Smiling_Face_Emoji_87fdae9b-b2af-4619-a37f-e484c5e2e7a4_large.png?v=1571606036",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Smiling_Face_Emoji_large.png?v=1571606036",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Tears_of_Joy_Emoji_8afc0e22-e3d4-4b07-be7f-77296331c687_large.png?v=1571606036",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Tongue_Out_Emoji_d97a3d11-d4f0-440f-b33f-3ab99cfa5173_large.png?v=1571606036",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Hugging_Face_Emoji_2028ce8b-c213-4d45-94aa-21e1a0842b4d_large.png?v=1571606036",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Shyly_Smiling_Face_Emoji_large.png?v=1571606037",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Victory_Hand_Emoji_large.png?v=1571606062",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Thumbs_Up_Hand_Sign_Emoji_large.png?v=1571606063",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/White_Thumbs_Down_Sign_Emoji_large.png?v=1571606067",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Ok_Hand_Sign_Emoji_large.png?v=1571606063",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/White_Open_Hands_Sign_Emoji_large.png?v=1571606067",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Praying_Emoji_large.png?v=1571606063",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Very_Angry_Emoji_7f7bb8df-d9dc-4cda-b79f-5453e764d4ea_large.png?v=1571606036",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Nerd_with_Glasses_Emoji_2a8485bc-f136-4156-9af6-297d8522d8d1_large.png?v=1571606036",
            "https://cdn.shopify.com/s/files/1/1061/1924/products/Very_sad_emoji_icon_png_large.png?v=1571606089"));
}