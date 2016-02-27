// conf.js
exports.config = {
  framework: 'jasmine',
  seleniumAddress: 'http://localhost:4444/wd/hub',
  specs: ['logintest.js'],
  multiCapabilities: [{
    browserName: 'chrome'
  }],
  onPrepare: function () {
    testutils = require('./testutils');
  }
};