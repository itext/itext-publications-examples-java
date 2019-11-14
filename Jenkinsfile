#!/usr/bin/env groovy
@Library('pipeline-library')_

def schedule
switch (env.BRANCH_NAME) {
    case 'master': schedule = '@monthly'; break;
    case 'develop': schedule = '@midnight'; break;
    default: schedule = ''; break;
}

pipeline {

    agent any

    environment {
        JDK_VERSION = 'jdk-8-oracle'
        ITEXT7_LICENSEKEY = "${env.WORKSPACE}/license"
    }

    options {
        ansiColor('xterm')
        buildDiscarder(logRotator(artifactNumToKeepStr: '1'))
        parallelsAlwaysFailFast()
        retry(1)
        skipStagesAfterUnstable()
        timeout(time: 1, unit: 'HOURS')
        timestamps()
    }

    triggers {
        cron(schedule)
    }

    tools {
        maven 'M3'
        jdk "${JDK_VERSION}"
    }

    stages {
        stage('Clean workspace') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                withMaven(jdk: "${JDK_VERSION}", maven: 'M3', mavenLocalRepo: '.repository') {
                    sh 'mvn clean'
                    sh 'mvn dependency:purge-local-repository -Dinclude=com.itextpdf -DresolutionFuzziness=groupId -DreResolve=false'
                }
            }
        }
        stage('Compile') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                withMaven(jdk: "${JDK_VERSION}", maven: 'M3', mavenLocalRepo: '.repository') {
                    sh 'mvn compile test-compile'
                }
            }
        }
        stage('Static Code Analysis') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                withMaven(jdk: "${JDK_VERSION}", maven: 'M3', mavenLocalRepo: '.repository') {
                    sh 'mvn checkstyle:checkstyle spotbugs:check pmd:pmd -Dpmd.analysisCache=true javadoc:javadoc -Dmaven.javadoc.failOnError=false'
                }
            }
            post {
                always {
                    publishHTML (target: [
                        allowMissing: false,
                        alwaysLinkToLastBuild: false,
                        keepAll: true,
                        reportDir: 'target/site',
                        reportFiles: 'checkstyle.html',
                        reportName: 'Checkstyle Report'
                    ])
                }
            }
        }
        stage('Prepare test environment') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                parallel (
                    "Typography" : {
                        dir ('license') {
                            sh 'git archive --format=tar --remote=ssh://git@git.itextsupport.com:7999/i7j/typography.git develop:src/test/resources/com/itextpdf/typography -- itextkey-typography.xml | tar -O -xf - > itextkey-typography.xml'
                        }
                    },
                    "Multiple Products" : {
                        dir ('license') {
                            sh 'git archive --format=tar --remote=ssh://git@git.itextsupport.com:7999/i7j/licensekey.git develop:src/test/resources/com/itextpdf/licensekey -- all-products.xml | tar -O -xf - > itextkey-multiple-products.xml'
                        }
                    },
                    "pdfHTML + pdfCalligraph" : {
                        dir ('license') {
                            sh 'git archive --format=tar --remote=ssh://git@git.itextsupport.com:7999/i7j/licensekey.git develop:src/test/resources/com/itextpdf/licensekey -- all-products.xml | tar -O -xf - > itextkey-html2pdf_typography.xml'
                        }
                    },
                    "All Products" : {
                        dir ('license') {
                            sh 'git archive --format=tar --remote=ssh://git@git.itextsupport.com:7999/i7j/licensekey.git develop:src/test/resources/com/itextpdf/licensekey -- all-products.xml | tar -O -xf - > all-products.xml'
                        }
                    }
                )
            }
        }
        stage('Run Tests') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }
            steps {
                withMaven(jdk: "${JDK_VERSION}", maven: 'M3', mavenLocalRepo: '.repository') {
                    sh 'mvn verify -DgsExec="${gsExec}" -DcompareExec="${compareExec}" -Dmaven.test.skip=false -Dmaven.test.failure.ignore=false -Dmaven.javadoc.skip=true -Dcheckstyle.skip=true -Dpmd.skip=true -Dspotbugs.skip=true'
                }
            }
            post {
                always {
                    junit 'target/failsafe-reports/*.xml'
                }
            }
        }
    }

    post {
        always {
            echo 'One way or another, I have finished \uD83E\uDD16'
        }
        success {
            echo 'I succeeeded! \u263A'
            deleteDir() /* clean up our workspace */
        }
        unstable {
            echo 'I am unstable \uD83D\uDE2E'
        }
        failure {
            echo 'I failed \uD83D\uDCA9'
        }
        changed {
            echo 'Things were different before... \uD83E\uDD14'
        }
        fixed {
            script {
                if ((env.BRANCH_NAME == 'master') || (env.BRANCH_NAME == 'develop')) {
                    slackNotifier("#ci", currentBuild.currentResult, "${env.BRANCH_NAME} - Back to normal")
                }
            }
        }
        regression {
            script {
                if ((env.BRANCH_NAME == 'master') || (env.BRANCH_NAME == 'develop')) {
                    slackNotifier("#ci", currentBuild.currentResult, "${env.BRANCH_NAME} - First failure")
                }
            }
        }
    }

}