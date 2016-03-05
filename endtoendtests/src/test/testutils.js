exports.openMainPage = function (username, password){
    browser.ignoreSynchronization = true;
    browser.driver.get('http://localhost:8000');

    browser.wait(function() {
        return element(by.id('fld-username')).isPresent();
    }, 30000);

    exports.loginUser(username, password);
};

exports.loginUser = function (login, password){

    var fldUserName = element(by.id('fld-username'));
    fldUserName.clear().then(function() {
        fldUserName.sendKeys(login);
    });

    var fldPassword = element(by.id('fld-password'));
    fldPassword.clear().then(function() {
        fldPassword.sendKeys(password);
    });

    element(by.id('btn-submit')).click();
};