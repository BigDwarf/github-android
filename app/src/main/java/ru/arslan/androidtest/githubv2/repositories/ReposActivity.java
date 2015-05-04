package ru.arslan.androidtest.githubv2.repositories;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;

import java.util.ArrayList;
import java.util.List;

import ru.arslan.androidtest.githubv2.commits.CommitsActivity;
import ru.arslan.androidtest.githubv2.R;
import ru.arslan.androidtest.githubv2.session.SessionManager;

public class ReposActivity extends ActionBarActivity implements ReposListener{
    public static final String KEY_REPOSITORY = "repos";
    private User usr;
    private List<Repository> mReps;
    private ListView mReposList;
    private RepositoriesAdapter adapter;
    private ReposTask mReposTask;
    private SessionManager mSession;
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService(new Intent(this, ConnectionService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Intent intent = new Intent(this, ConnectionService.class);
//        intent.putExtra(ConnectionService.TAG_INTERVAL, 100);
//        intent.putExtra(ConnectionService.TAG_URL_PING, "http://www.google.com");
//        intent.putExtra(ConnectionService.TAG_ACTIVITY_NAME, this.getClass().getName());
//        startService(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        mSession = new SessionManager(getApplicationContext());
        mReposList = (ListView)findViewById(R.id.reposView);
        usr = mSession.getUser();
        startTask();
        mReposList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Repository selected = mReps.get(i);
                Intent intent = new Intent(ReposActivity.this, CommitsActivity.class);
                intent.putExtra(KEY_REPOSITORY, selected);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:
                mSession.logoutUser();
                finish();
                break;
            case R.id.action_renew:
                startTask();
                break;
            default:
                break;
        }
        return true;
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void getRepositories(List<Repository> reps) {
        if(reps!=null) {
            mReps = new ArrayList<>(reps);
        }else{
            mReps = mSession.getListRepos();
            Toast.makeText(getApplicationContext(), R.string.cashed_copy, Toast.LENGTH_LONG).show();
        }
        adapter = new RepositoriesAdapter(this, mReps);
        mReposList.setAdapter(adapter);
    }
    private void startTask(){
        mReposTask = new ReposTask(ReposActivity.this);
        mReposTask.execute(usr);
    }
}
