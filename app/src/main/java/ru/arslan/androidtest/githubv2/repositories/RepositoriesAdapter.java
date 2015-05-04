package ru.arslan.androidtest.githubv2.repositories;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.eclipse.egit.github.core.Repository;

import java.util.List;

import ru.arslan.androidtest.githubv2.R;

public class RepositoriesAdapter extends ArrayAdapter<Repository> {
    private Activity context;
    private List<Repository> repository;

    public RepositoriesAdapter(Activity context, List<Repository> repository){
        super(context, R.layout.list_repository_single,repository );
        this.context = context;
        this.repository = repository;
    }
    static class ViewHolder{
        ImageView avatarImage;
        TextView repoName;
        TextView authorName;
        TextView description;
        TextView forks;
        TextView watches;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_repository_single, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.avatarImage = (ImageView)convertView.findViewById(R.id.avatar);
            viewHolder.repoName = (TextView)convertView.findViewById(R.id.repoName);
            viewHolder.authorName = (TextView)convertView.findViewById(R.id.authorName);
            viewHolder.description= (TextView) convertView.findViewById(R.id.description);
            viewHolder.forks = (TextView)convertView.findViewById(R.id.forks);
            viewHolder.watches = (TextView)convertView.findViewById(R.id.watches);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Picasso.with(context)
                .load(repository.get(position).getOwner().getAvatarUrl())
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.avatarImage);
        viewHolder.repoName.setText(repository.get(position).getName());
        viewHolder.authorName.setText(repository.get(position).getOwner().getName());
        viewHolder.description.setText(repository.get(position).getDescription());
        viewHolder.forks.setText(String.valueOf(repository.get(position).getForks()));
        viewHolder.watches.setText(String.valueOf(repository.get(position).getWatchers()));
        return convertView;
    }
}
