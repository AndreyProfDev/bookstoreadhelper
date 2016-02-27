
/**
 * Perform get request with authorization support
 * @param restUrl - url for get request
 * @param callback - callback to call on get response
 */
function get(restUrl, callback){
    $.ajax({
        url: restUrl
    }).then(function(data, status, jqxhr) {
        callback(data);
    }).fail(function(jqXHR, textStatus, errorThrown){
        if (jqXHR.status === 401) { // HTTP Status 401: Unauthorized

            var preLoginInfo = JSON.stringify({method: 'GET', url: '/'});
            $.cookie('restsecurity.pre.login.request', preLoginInfo);
            window.location = '/loginPage.html';

        } else {
            alert('Unexpected error(' + jqXHR.status + ') occured. Please try again...');
        }
    });
}
