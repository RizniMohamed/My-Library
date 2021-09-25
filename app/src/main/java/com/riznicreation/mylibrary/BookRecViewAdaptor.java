package com.riznicreation.mylibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class BookRecViewAdaptor extends RecyclerView.Adapter<BookRecViewAdaptor.ViewHolder>{

    private ArrayList<Book> books = new ArrayList<>();
    private final Context mContext;
    private final String activityName;

    public BookRecViewAdaptor(Context mContext, String activityName) {
        this.mContext = mContext;
        this.activityName = activityName;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_book,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Book book = books.get(position);

        AlertDialog.Builder builder_test = new AlertDialog.Builder(mContext);
                builder_test.setMessage(book.toString());
                builder_test.setNegativeButton("Dismiss",(dialog, which) -> {});
                builder_test.setPositiveButton("Visit",(dialog, which) -> {});
                builder_test.setCancelable(false);
      //   builder_test.create().show();

        holder.bookName.setText(book.getName());
        holder.shortDesc.setText(book.getShortDesc());
        holder.author.setText(book.getAuthor());

        Glide.with(mContext)
                .asBitmap()
                .load(book.getImgURL())
                .into(holder.imgBook);

        holder.imgBook.setOnClickListener(v -> {
            Intent intent = new Intent(mContext,BookActivity.class);
            intent.putExtra("bookID", book.getId()-1);
            mContext.startActivity(intent);
        });

        //Set expand and collapse view action
        holder.downArrow.setOnClickListener(v -> { book.setExpanded(book.isExpanded()); notifyItemChanged(position); });
        holder.bookName.setOnClickListener(v -> { book.setExpanded(book.isExpanded()); notifyItemChanged(position); });
        holder.upArrow.setOnClickListener(v -> { book.setExpanded(book.isExpanded()); notifyItemChanged(position); });
        holder.shortDesc.setOnClickListener(v -> { book.setExpanded(book.isExpanded()); notifyItemChanged(position); });

        //Handle expand and collapse button visibility
        if(book.isExpanded()){
            TransitionManager.beginDelayedTransition(holder.parent);
            holder.rvExpanded.setVisibility(View.GONE);
        }else{
            TransitionManager.beginDelayedTransition(holder.parent);
            holder.rvExpanded.setVisibility(View.VISIBLE);
            holder.downArrow.setVisibility(View.GONE);
        }

        if (activityName.equals("AllBooksActivity") ){
            holder.imgDelete.setVisibility(View.GONE);
        }else{
            holder.imgDelete.setVisibility(View.VISIBLE);

            holder.imgDelete.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Are you sure do you want to delete " + book.getName());
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    switch (activityName) {
                        case "AlreadyReadBooks": DB_Book.getInstance(mContext).removeFrom_alreadyReadBooks(book);break;
                        case "CurrentlyReadingBooks": DB_Book.getInstance(mContext).removeFrom_wishlist(book);break;
                        case "Wishlist": DB_Book.getInstance(mContext).removeFrom_currentlyReadingBook(book);break;
                        case "Favourite": DB_Book.getInstance(mContext).removeFrom_favouriteBooks(book);break;
                        default: Toast.makeText(mContext, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(mContext, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                    notifyItemChanged(position);
                });
                builder.setNegativeButton("No", (dialog, which) -> {
                });
                builder.create().show();
            });
        }

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private final MaterialCardView parent;
        private final ImageView imgBook;
        private final TextView bookName;
        private final TextView author;
        private final TextView shortDesc;
        private final RelativeLayout rvExpanded;
        private final ImageView upArrow;
        private final ImageView downArrow;
        private final ImageView imgDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.cvBook);
            imgBook = itemView.findViewById(R.id.imgBook);
            bookName = itemView.findViewById(R.id.txtBookName);
            author = itemView.findViewById(R.id.txtAuthor);
            shortDesc = itemView.findViewById(R.id.lblShortDesc);
            rvExpanded = itemView.findViewById(R.id.rvExpanded);
            upArrow = itemView.findViewById(R.id.imgUp);
            downArrow = itemView.findViewById(R.id.imgDown);
            imgDelete = itemView.findViewById(R.id.imgDelete);

        }
    }
}
