package ru.arslan.androidtest.githubv2.login;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.egit.github.core.User;

import ru.arslan.androidtest.githubv2.helpers.NetworkHelper;
import ru.arslan.androidtest.githubv2.R;
import ru.arslan.androidtest.githubv2.repositories.ReposActivity;
import ru.arslan.androidtest.githubv2.session.SessionManager;

public class LoginActivity extends ActionBarActivity implements LoginListener{
    private EditText mUsername;
    private EditText mPassword;
    private Button mLoginButton;
    private LoginTask mTask;
    private SessionManager mSession;
    private NetworkHelper mHelper;
    @Override
    protected void onResume() {
        super.onResume();
        if(mSession.isLoggedIn())
            finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSession = new SessionManager(getApplicationContext());
        mHelper = new NetworkHelper(getApplicationContext());
        mUsername = (EditText)findViewById(R.id.usernameText);
        mPassword = (EditText)findViewById(R.id.passwordText);
        mLoginButton = (Button)findViewById(R.id.loginButton);
        mSession.checkLogin();
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mHelper.isOnline()){
                    Toast.makeText(getApplicationContext(),R.string.no_network_connection,Toast.LENGTH_LONG).show();
                }else if(isEmpty(mUsername) || isEmpty(mPassword)){
                    Toast.makeText(getApplicationContext(),R.string.enterLogPass, Toast.LENGTH_LONG).show();
                }else{
                    mSession.createLoginSession(prepareLogPass(mUsername), prepareLogPass(mPassword));
                    mTask = new LoginTask(LoginActivity.this);
                    if(mSession.isLoggedIn()){
                        mTask.execute(mSession.getUserLogin(), mSession.getUserPass());
                    }else {
                        mTask.execute(prepareLogPass(mUsername), prepareLogPass(mPassword));
                    }
                }
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    @Override
    public void loginSucceeded(User s) {
        Intent intent = new Intent(LoginActivity.this, ReposActivity.class);
        mSession.saveUser(s);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailed() {
        if(mHelper.isOnline()){
            Toast.makeText(getApplicationContext(),R.string.wrong_credentials,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),R.string.no_network_connection,Toast.LENGTH_LONG).show();
        }
    }
    private String prepareLogPass(EditText et){
        return et.getText().toString().trim();
    }
}
