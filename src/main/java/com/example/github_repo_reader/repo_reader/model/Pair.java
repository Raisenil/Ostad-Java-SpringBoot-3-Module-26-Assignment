package com.example.github_repo_reader.repo_reader.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Pair<FIRST, SECOND> {
    private final FIRST first;
    private final SECOND second;
}
