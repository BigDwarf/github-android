package ru.arslan.androidtest.githubv2.commits;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.eclipse.egit.github.core.RepositoryCommit;


import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import ru.arslan.androidtest.githubv2.R;

public class CommitsAdapter extends ArrayAdapter<RepositoryCommit> {
    private Activity context;
    private List<RepositoryCommit> commits;

    public CommitsAdapter(Activity context, List<RepositoryCommit> commits){
        super(context, R.layout.list_repository_single,commits );
        this.context = context;
        this.commits = commits;
    }
    static class ViewHolder{
        TextView hash;
        TextView commitMessage;
        TextView author;
        TextView date;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_commits_single, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.hash = (TextView)convertView.findViewById(R.id.hash);
            viewHolder.commitMessage= (TextView) convertView.findViewById(R.id.commitMessage);
            viewHolder.author = (TextView)convertView.findViewById(R.id.author);
            viewHolder.date = (TextView)convertView.findViewById(R.id.date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.hash.setText(String.valueOf(commits.get(position).getSha()));
        viewHolder.commitMessage.setText(commits.get(position).getCommit().getMessage());
        viewHolder.author.setText(commits.get(position).getCommit().getCommitter().getName());
        viewHolder.date.setText(prepareDate(commits.get(position).getCommit().getCommitter().getDate()));
        return convertView;
    }
    public static String prepareDate(Date date){
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String commitDate = df.format(date);
        return commitDate;
    }
}
