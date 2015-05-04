package ru.arslan.androidtest.githubv2.repositories;

import org.eclipse.egit.github.core.Repository;

import java.util.List;

/**
 * Created by arslan on 24.04.15.
 */
public interface ReposListener {
    public void getRepositories(List<Repository> reps);
}
