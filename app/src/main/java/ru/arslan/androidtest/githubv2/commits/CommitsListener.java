package ru.arslan.androidtest.githubv2.commits;

import org.eclipse.egit.github.core.RepositoryCommit;

import java.util.List;

public interface CommitsListener{
    public void getCommits(List<RepositoryCommit> commits);
}