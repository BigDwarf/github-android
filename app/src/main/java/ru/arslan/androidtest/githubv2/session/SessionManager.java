package ru.arslan.androidtest.githubv2.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;

import java.lang.reflect.Type;
import java.util.List;

import ru.arslan.androidtest.githubv2.login.LoginActivity;
import ru.arslan.androidtest.githubv2.repositories.ReposActivity;

public class SessionManager {
    private SharedPreferences pref;
    private Editor editor;
    private Context context;
    private Gson gson;
    private static final int PRIVATE_MODE       = 0;
    private static final String PREF_NAME       = "AndroidHivePref";
    private static final String IS_LOGIN        = "IsLoggedIn";
    public static final String KEY_LOGIN        = "name";
    public static final String KEY_PASS         = "pass";
    public static final String KEY_USER         = "user";
    public static final String KEY_REPOSITORIES = "repositories";
    public static final String COMMITS_PREFIX = "commit";

    public SessionManager(Context context){
        this.context = context;
        gson = new Gson();
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void createLoginSession(String name, String pass){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_LOGIN, name);
        editor.putString(KEY_PASS, pass);
        editor.commit();
    }
    public void checkLogin(){
        if(this.isLoggedIn()){
            Intent i = new Intent(context, ReposActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
    public String getUserLogin(){
        return pref.getString(KEY_LOGIN, null);
    }
    public String getUserPass(){
        return pref.getString(KEY_PASS, null);
    }

    public void saveUser(User user){
        String userToSave = gson.toJson(user);
        editor.putString(KEY_USER,userToSave);
        editor.commit();
    }
    public User getUser(){
        String user = pref.getString(KEY_USER,null);
        return gson.fromJson(user, User.class);
    }
    public void saveRepository(Repository repository){
        String repo = gson.toJson(repository);
        editor.putString(repository.getName(),repo);
        editor.commit();
    }
    public Repository getRepository(String name){
        String repo = pref.getString(name,null);
        return gson.fromJson(repo, Repository.class);
    }

    public void saveListRepos(List<Repository> repositories){
        String repos = gson.toJson(repositories);
        editor.putString(KEY_REPOSITORIES,repos);
        editor.commit();
    }
    public List<Repository> getListRepos(){
        Type type = new TypeToken<List<Repository>>(){}.getType();
        return gson.fromJson(pref.getString(KEY_REPOSITORIES,null),type);
    }
    public void saveCommits(List<RepositoryCommit> commits, String repoName){
        String commit = gson.toJson(commits);
        editor.putString(repoName + COMMITS_PREFIX, commit);
        editor.commit();
    }
    public List<RepositoryCommit> getCommits(String repoName){
        Type type = new TypeToken<List<RepositoryCommit>>(){}.getType();
        String repos = pref.getString(repoName + COMMITS_PREFIX,null);
        if(repos != null)
            return gson.fromJson(repos,type);
        else
            return null;
    }
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}
