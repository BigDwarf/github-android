package ru.arslan.androidtest.githubv2.login;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.CommitService;

/**
 * Created by arslan on 23.04.15.
 */
public interface LoginListener {
    public void  loginSucceeded(User s);
    public void  loginFailed();
}
