var gulp = require('gulp');
var Server = require('karma').Server;

gulp.task('karma-runner', function (done) {
    new Server({
        configFile: __dirname + '/src/test/karma.conf.js'
    }, done).start();
});

gulp.task('default', ['karma-runner']);