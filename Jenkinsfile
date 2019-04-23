#!/usr/bin/env groovy

pipeline {

    agent {
        node {
            label 'linux'
        }
    }

    options {
        buildDiscarder(logRotator(artifactNumToKeepStr:'1'))
        skipStagesAfterUnstable()
        retry(1)
        timeout(time: 30, unit: 'MINUTES')
        timestamps ()
    }

    tools {
        maven 'M3'
        jdk '1.8'
    }

    environment {
        ITEXT7_LICENSEKEY = "${env.WORKSPACE}/license"
    }

    stages {
        stage('Compile') {
            steps {
                withMaven(jdk: '1.8', maven: 'M3') {
                    sh 'mvn compile test-compile'
                }
            }
        }
        stage('Static Code Analysis') {
            parallel {
                stage('Checkstyle') {
                    steps {
                        withMaven(jdk: '1.8', maven: 'M3') {
                            sh 'mvn checkstyle:checkstyle'
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
                stage('Findbugs') {
                    steps {
                        withMaven(jdk: '1.8', maven: 'M3') {
                            sh 'mvn findbugs:check'
                        }
                    }
                }
                stage('PMD') {
                    steps {
                        withMaven(jdk: '1.8', maven: 'M3') {
                            sh 'mvn pmd:pmd -Dpmd.analysisCache=true'
                        }
                    }
                }
            }
        }
        stage('Prepare test environment') {
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
            parallel {
                stage('Surefire (Unit Tests)') {
                    steps {
                        withMaven(jdk: '1.8', maven: 'M3') {
                            sh 'mvn surefire:test -DgsExec=$(which gs) -DcompareExec=$(which compare) -Dmaven.test.skip=false -Dmaven.test.failure.ignore=false -Dmaven.javadoc.failOnError=false'
                        }
                    }
                    post {
                        always {
                            junit 'target/surefire-reports/*.xml'
                        }
                    }
                }
                stage('Failsafe (Integration Tests)') {
                    steps {
                        withMaven(jdk: '1.8', maven: 'M3') {
                            sh 'mvn failsafe:integration-test failsafe:verify -DgsExec=$(which gs) -DcompareExec=$(which compare) -Dmaven.test.skip=false -Dmaven.test.failure.ignore=false -Dmaven.javadoc.failOnError=false'
                        }
                    }
                    post {
                        always {
                            junit 'target/failsafe-reports/*.xml'
                        }
                    }
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
    }

}