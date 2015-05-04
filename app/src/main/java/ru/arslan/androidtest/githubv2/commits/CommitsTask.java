package ru.arslan.androidtest.githubv2.commits;

import android.content.Context;
import android.os.AsyncTask;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.service.CommitService;

import java.io.IOException;
import java.util.List;

import ru.arslan.androidtest.githubv2.helpers.NetworkHelper;

public class CommitsTask extends AsyncTask<Repository, Void, List<RepositoryCommit>> {
    private CommitService commitService;
    private List<RepositoryCommit> commits;
    private CommitsListener mListener;
    private NetworkHelper mHelper;
    public CommitsTask(CommitsListener commitsListener, Context context){
        mListener = commitsListener;
        mHelper = new NetworkHelper(context);
    }
    @Override
    protected void onPostExecute(List<RepositoryCommit> repositoryCommits) {
        super.onPostExecute(repositoryCommits);
        mListener.getCommits(repositoryCommits);

    }

    @Override

    protected List<RepositoryCommit> doInBackground(Repository... repositories) {
        if(mHelper.isOnline()){
            commitService = new CommitService();
            commits = null;
            try {
                commits = commitService.getCommits(repositories[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return commits;
        }
        return null;
    }
}