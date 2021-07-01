package com.hienthai.quizappmvvm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoryQuizAdapter extends RecyclerView.Adapter<CategoryQuizAdapter.ViewHolder> {

    private List<Quiz> quizList;
    private OnCategoryClicked onCategoryClicked;

    public CategoryQuizAdapter(OnCategoryClicked onCategoryClicked) {
        this.onCategoryClicked = onCategoryClicked;
    }

    public void setQuizList(List<Quiz> quizList) {
        this.quizList = quizList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryQuizAdapter.ViewHolder holder, int position) {

        Quiz quiz = quizList.get(position);

        Glide.with(holder.itemView.getContext()).load(quiz.getImage()).into(holder.img_item_category);
        holder.txt_title_item_category.setText(quiz.getName());
        holder.txt_desc_item_category.setText(quiz.getDesc());
        holder.txt_difficulty_item_category.setText(quiz.getLevel());
    }

    @Override
    public int getItemCount() {
        return quizList != null ? quizList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView img_item_category;
        private TextView txt_title_item_category, txt_desc_item_category, txt_difficulty_item_category;
        private Button btn_item_category;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_item_category = itemView.findViewById(R.id.img_item_category);
            txt_title_item_category = itemView.findViewById(R.id.txt_title_item_category);
            txt_desc_item_category = itemView.findViewById(R.id.txt_desc_item_category);
            txt_difficulty_item_category = itemView.findViewById(R.id.txt_difficulty_item_category);
            btn_item_category = itemView.findViewById(R.id.btn_item_category);

            btn_item_category.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onCategoryClicked.onItemClicked(getAdapterPosition());
        }
    }

    public interface OnCategoryClicked {
        void onItemClicked(int positon);
    }
}
