package com.example.employeecrudwithapi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeecrudwithapi.R;
import com.example.employeecrudwithapi.model.ListItem;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private List<ListItem> items;
    private LayoutInflater inflater;

    public CustomListAdapter(Context context, List<ListItem> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_layout, parent, false);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.itemImage);
            holder.nameTextView = convertView.findViewById(R.id.itemName);
            holder.descriptionTextView = convertView.findViewById(R.id.itemDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListItem item = items.get(position);
        holder.nameTextView.setText(item.getName());
        holder.descriptionTextView.setText(item.getDescription());
        holder.imageView.setImageResource(item.getImageResource());

        convertView.setOnClickListener(v ->
                Toast.makeText(context, "Clicked: " + item.getName(), Toast.LENGTH_SHORT).show()
        );

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView descriptionTextView;
    }
}
