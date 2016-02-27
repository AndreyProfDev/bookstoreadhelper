
/**
 * Downloads list of books from server
 */
$(document).ready(function() {
    get('/server/rest/books', function(data){
        $('#book-id').append(data.id);
        $('#book-name').append(data.name);
        $('#logged-user').append('Hello ' + $.cookie('restsecurity.username'));
    });
});