const fs = require('fs')

let slackPayload = function () {
    let attachment = {}
    let endpoint = process.env.ENDPOINT || 'https://practice.expandtesting.com/';
    let githubRunId = process.env.GITHUB_RUN_ID;
    let testRunHtml = process.env.TEST_RUN_HTML;
    let githubRepo = process.env.GITHUB_REPOSITORY

    let results = {
          passed: parseInt(process.env.PASSED),
          failed: parseInt(process.env.FAILED),
          skipped: parseInt(process.env.SKIPPED)
    }
    let messageText = `*Title:* :qafellas: \`Playwright Api Tests\`\n\n*Env:* ${endpoint}\n\n:github: Github: https://github.com/${githubRepo}/actions/runs/${githubRunId}\n\n:graph: HTML Report: ${testRunHtml}\n\n*Total Test Cases:* ${results.passed+results.skipped+results.failed}\n\n:white_check_mark: Passed: ${results.passed} | :x: Failed: ${results.failed} | ⏩ Skipped: ${results.skipped}\n\n`

    if(results.failed>0){
        attachment = {
            color: '#dc3545',
            text: `${messageText}:alert: Test execution *FAILED*. Check the failures`,
            ts: Date.now().toString(),
        }
    }
    else if(results.failed == 0 && results.skipped == 0 && results.passed == 0){
        attachment = {
            color: '#808080',
            elements:[],
            text: `${messageText}:mag: Something went wrong with test execution!!!`,
            ts: Date.now().toString(),
        }
    }
    else if(results.failed == 0){
        attachment = {
            color: '#008000',
            elements:[],
            text: `${messageText}:party-blob: Test execution *SUCCEEDED*`,
            ts: Date.now().toString(),
          }
    }
    const body = {

        attachments: [
            attachment
          ],

    };

    return body;
};

const jsonString = JSON.stringify(slackPayload())
fs.writeFile('jsonReporter.json', jsonString, err => {
    if (err) {
        console.log('Error writing file', err)
    } else {
        console.log('Successfully wrote file')
    }
})