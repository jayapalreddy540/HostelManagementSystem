package tk.codme.hostelmanagementsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    int logos[];
    LayoutInflater inflater;

    public ImageAdapter(Context c,int[] logos) {
        mContext = c;
        this.logos=logos;
        inflater=(LayoutInflater.from(c));
    }

    public int getCount() {
        return logos.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View view, ViewGroup parent) {
     view=inflater.inflate(R.layout.activity_gridview,null);
     ImageView icon=(ImageView) view.findViewById(R.id.icon);
     icon.setImageResource(logos[position]);
     return view;
    }

}