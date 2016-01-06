package ssbit.glwzz.aimgproc;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AdapterOfOnePicRecView extends RecyclerView.Adapter<AdapterOfOnePicRecView.ViewHolder> {
    private ArrayList<OnePic> allOnePic;
    private Context context;

    public AdapterOfOnePicRecView(ArrayList<OnePic> allOnePic, Context context) {
        this.allOnePic = allOnePic;
        this.context = context;
    }

    public void setAllOnePic(ArrayList<OnePic> allOnePic) {
        this.allOnePic = allOnePic;
        this.notifyDataSetChanged();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.layout_rec_one_pic, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setContent(allOnePic.get(position));

    }

    @Override
    public int getItemCount() {
        return allOnePic.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView imageView_pic;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView_pic = (AppCompatImageView) itemView.findViewById(R.id.imageView_pic);

        }

        public void setContent(final OnePic onePic) {
            imageView_pic.setImageBitmap(onePic.getBitmap());
            imageView_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(MyExtraName.pic, onePic);
                    ((AppCompatActivity) context).
                            startActivityForResult(intent,
                                    Msg.detailRequestCode);
                }
            });

        }

    }
}

