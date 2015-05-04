package ru.arslan.androidtest.githubv2.commits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;

import java.util.List;

import ru.arslan.androidtest.githubv2.helpers.NetworkHelper;
import ru.arslan.androidtest.githubv2.R;
import ru.arslan.androidtest.githubv2.repositories.ReposActivity;
import ru.arslan.androidtest.githubv2.session.SessionManager;

public class CommitsActivity extends ActionBarActivity implements CommitsListener {
    private Repository repository;
    private List<RepositoryCommit> mCommits;
    private CommitsTask mCommitTask;
    private SessionManager mSessionManager;
    private NetworkHelper mHelper;
    private ListView commitsView;
    private CommitsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos_single);
        commitsView = (ListView)findViewById(R.id.commitsView);
        mSessionManager = new SessionManager(getApplicationContext());
        mHelper = new NetworkHelper(getApplicationContext());
        mCommits = null;
        Intent i = getIntent();
        if(i!=null){
            repository = (Repository)i.getSerializableExtra(ReposActivity.KEY_REPOSITORY);
        }
        setTitle(repository.getName());
        if(mHelper.isOnline()){
            mCommitTask = new CommitsTask(this,getApplicationContext());
            mCommitTask.execute(repository);
        }else if(mSessionManager.getCommits(repository.getName())!=null){
            new Thread() {
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter = new CommitsAdapter(CommitsActivity.this ,mSessionManager.getCommits(repository.getName()));
                            commitsView.setAdapter(adapter);
                        }
                    });
                }

            }.start();
            Toast.makeText(getApplicationContext(), R.string.check_network_state, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), R.string.check_network_state, Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void getCommits(List<RepositoryCommit> commits) {
        if(commits!=null) {
            mSessionManager.saveCommits(commits, repository.getName());
            adapter = new CommitsAdapter(this,commits);
            commitsView.setAdapter(adapter);
        }
    }
}
