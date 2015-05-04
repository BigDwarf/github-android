package ru.arslan.androidtest.githubv2.repositories;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.arslan.androidtest.githubv2.helpers.NetworkHelper;
import ru.arslan.androidtest.githubv2.session.SessionManager;

public class ReposTask extends AsyncTask<User,Void,List<Repository>> {
    private RepositoryService mService;
    private List<Repository> mRepos;
    public ReposListener mReposListener;
    private SessionManager mSessionManager;
    private NetworkHelper mHelper;
    private ProgressDialog mDialog;
    private ReposActivity activity;
    @Override
    protected void onPostExecute(List<Repository> repositories) {
        if(mDialog.isShowing())
            mDialog.dismiss();
        mReposListener.getRepositories(repositories);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new ProgressDialog(activity);
        mDialog.setMessage("Loading...");
        mDialog.show();
    }

    public ReposTask(ReposActivity activity) {
        this.activity = activity;
        mHelper = new NetworkHelper(activity);
        mReposListener = activity;
        mSessionManager = new SessionManager(activity);
    }

    @Override
    protected List<Repository> doInBackground(User... strings) {
        if(mHelper.isOnline()) {
            mService = new RepositoryService();
            try {
                mRepos = new ArrayList<>();
                mRepos = mService.getRepositories(strings[0].getLogin());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (mRepos.size() != 0) {
                for (Repository repo : mRepos)
                    mSessionManager.saveRepository(repo);
                mSessionManager.saveListRepos(mRepos);
            }
            return this.mRepos;
        }else return null;
    }
}
