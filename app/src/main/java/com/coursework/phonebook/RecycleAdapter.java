package com.coursework.phonebook;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder>{
    private List<Contact> mDataset;
    private int count=0;
    private RecycleAdapter that;
    private Context context;
    private  FragmentManager fragmentManager;

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;
        public LinearLayout linearLayout;

        public MyViewHolder(LinearLayout v) {
            super(v);
            linearLayout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecycleAdapter(List<Contact> myDataset) {
        mDataset = myDataset;
        that = this;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public RecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_layout, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
     //   holder.textView.setText(mDataset[position]);

        LinearLayout linearLayout =(LinearLayout) holder.linearLayout.getChildAt(0);
        View v = holder.linearLayout;
        ImageView avatar =  v.findViewById(R.id.contact_photo);
        String photo = mDataset.get(position).getPhotoPath();
        if(photo!=null){
            if(!photo.matches(".")||
                    !photo.matches("null")) {

                avatar.setImageBitmap(BitmapFactory.decodeFile(mDataset.get(position).getPhotoPath()));
            }
        }
        TextView contactName =(TextView)linearLayout.getChildAt(0);
        contactName.setText(mDataset.get(position).toString());
        final TextView contactPhone =(TextView)linearLayout.getChildAt(1);
        contactPhone.setText(mDataset.get(position).getPhone());

        final TextView contactCompany =(TextView)linearLayout.getChildAt(2);

        contactCompany.setText(mDataset.get(position).getCompany());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            private boolean isClosed =true;
            @Override
            public void onClick(View v) {
                if(isClosed){
                    contactPhone.setVisibility(View.VISIBLE);
                    contactCompany.setVisibility(View.VISIBLE);
                    contactPhone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String dial = "tel:" + contactPhone.getText().toString();
                            context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));

                        }
                    });

                }
                else {
                    contactPhone.setVisibility(View.GONE);
                    contactCompany.setVisibility(View.GONE);
                }
                isClosed=!isClosed;

            }
        });

        holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            private boolean isClosed =true;
            @Override
            public boolean onLongClick(View v) {
                if(isClosed){
                    ImageView imageView = v.findViewById(R.id.contact_photo);
                    imageView.setVisibility(View.GONE);
                    ImageView edit = v.findViewById(R.id.contact_edit);
                    edit.setVisibility(View.VISIBLE);
                    edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AddContactController edit = new AddContactController();
                            edit.setEdit(true,position);
                            edit.setRecycleAdapter(that);
                            edit.show(fragmentManager,"Editor");
                        }
                    });
                    ImageView del = v.findViewById(R.id.contact_delete);
                    del.setVisibility(View.VISIBLE);
                    del.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            delete(position);
                        }
                    });
                }
                else{
                    ImageView imageView = v.findViewById(R.id.contact_photo);
                    imageView.setVisibility(View.VISIBLE);
                    imageView = v.findViewById(R.id.contact_edit);
                    imageView.setVisibility(View.GONE);
                    imageView = v.findViewById(R.id.contact_delete);
                    imageView.setVisibility(View.GONE);

                }
                isClosed=!isClosed;
                return false;
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public void add(Contact contact){
        new SQLHelper().insert(contact);
        mDataset.add(contact);
        notifyDataSetChanged();
    }
    public void edit( int pos,Contact contact){
        if(pos<0)pos=0;
        if(pos>=getItemCount())pos--;
        new SQLHelper().edit( mDataset.get(pos),contact);
        mDataset.set(pos,contact);
        notifyDataSetChanged();
    }
    public void delete(int position){
        new SQLHelper().remove(mDataset.get(position%getItemCount()));
        if(getItemCount()>1){
        mDataset.remove(position);
        }
        else mDataset = new ArrayList<Contact>();
        notifyItemRemoved(position);
    }

}
