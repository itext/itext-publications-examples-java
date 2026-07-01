#!/usr/bin/env groovy
@Library('pipeline-library')_

def repoName = "Examples"
def dependencyRegex = "(?!(cross-module|functional)-tests|profiling).*"

automaticJavaBuild(repoName, dependencyRegex)