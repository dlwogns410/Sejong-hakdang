package com.pProject.ganada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VocaAdapter extends RecyclerView.Adapter<VocaAdapter.ViewHolder> {
    private List<Voca> vocaList;
    private Activity context;
    private VocaDB vocaDB;
    private Context mContext;

    public VocaAdapter(Context context, List<Voca> list) {
        this.mContext = context;
        this.vocaList = list;
    }

    //어떤 xml로 뷰 홀더를 생성할지 지정
    @NonNull
    @Override
    public VocaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_voca_book, parent, false);
        VocaAdapter.ViewHolder vh = new VocaAdapter.ViewHolder(view);

        return vh;
    }

    //뷰 홀더에 데이터를 바인딩
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Voca voca = vocaList.get(position);
        holder.itemView.setTag(position);
//        holder.word_id.setText(String.valueOf(voca.getId()));
        holder.word.setText(voca.getWord());
        holder.word_pic.setImageURI(Uri.parse(voca.getPicture_uri()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String word = holder.word.getText().toString();
                Intent intent;
                intent = new Intent(mContext, VocaBookDetail.class);
                intent.putExtra("position", voca.toString());
                intent.putExtra("id", voca.getId());
                intent.putExtra("word", word);
                intent.putExtra("ex_sentence", voca.ex_sentence);
                intent.putExtra("picture_uri", voca.picture_uri);
                intent.putExtra("checked", true);
                intent.putExtra("type", voca.getType());
                mContext.startActivity(intent);
            }
        });
    }

    //뷰 홀더 개수 리턴턴
    @Override
    public int getItemCount() {
        return vocaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView word_id;
        ImageView word_pic;
        TextView word;

        ViewHolder(View view) {
            super(view);
//            word_id = view.findViewById(R.id.word_id);
            word = view.findViewById(R.id.word);
            word_pic = view.findViewById(R.id.word_pic);

            itemView.setClickable(true);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition();
//                    if(pos != RecyclerView.NO_POSITION) {
//                        Intent intent = new Intent(mContext,VocaBookDetail.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        intent.putExtra("word_id",String.valueOf(vocaList.get(pos).getId()));
//                        mContext.startActivity(intent);
//                    }
//                }
//            });
        }
    }
}
