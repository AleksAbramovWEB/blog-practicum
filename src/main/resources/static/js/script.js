
$(document).ready(function () {
    $("#tags").selectize({
        delimiter: ",",
        persist: false,
        maxItems: null,
        create: function (input) {
            return {
                value: input,
                text: input,
            };
        }
    });
});

function deleteComment(postId, commentPostId) {
    const form = document.getElementById('post-' + postId + '-comment-' +  commentPostId);

    form.submit();
}