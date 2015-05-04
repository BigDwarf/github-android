package ru.arslan.androidtest.githubv2.login;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.UserService;

import java.io.IOException;

/**
 * Created by arslan on 23.04.15.
 */
public class LoginTask extends AsyncTask<String, Void, User> {
    public static final String TAG = "LoginTask";
    private LoginListener mListener;
    private LoginActivity mActivity;
    private ProgressDialog mDialog;

    public LoginTask(LoginActivity activity){
        mActivity = activity;
        mListener = mActivity;
    }
    protected User doInBackground(String... strings) {
        User usr = null;
        GitHubClient client  = new GitHubClient();
        client.setCredentials(strings[0], strings[1]);
        UserService service = new UserService(client);

        try {
            usr = service.getUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(usr!=null){
            return usr;
        }else{
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(User s) {
        if(mDialog.isShowing())
            mDialog.dismiss();

        if(s!=null) {
            mListener.loginSucceeded(s);
        }
        else{
            mListener.loginFailed();
        }
        super.onPostExecute(s);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mDialog = new ProgressDialog(mActivity);
        mDialog.setMessage("Loading...");
        mDialog.show();
    }
}
