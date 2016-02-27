


describe('Protractor login test', function() {
    it('Login validation should be performed', function() {
        testutils.openMainPage('', 'InvalidPassword');
        var loginError;

        loginError = element(by.id('login-error'));
        expect(loginError.getText()).toEqual('Login or password are incorrect. Please try again.');

        testutils.loginUser('user', '');
        expect(loginError.getText()).toEqual('Login or password are incorrect. Please try again.');

        testutils.loginUser('user', 'InvalidPassword');
        expect(loginError.getText()).toEqual('Login or password are incorrect. Please try again.');

        testutils.loginUser('user', 'password');
        browser.wait(protractor.ExpectedConditions.presenceOf($('#logged-user')), 5000);
        browser.wait(protractor.ExpectedConditions.textToBePresentInElement($('#logged-user'), 'Hello user'), 5000);
    });
});