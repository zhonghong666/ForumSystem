var documentHeight = 0;
var topPadding = 15;
$(function() {
    var offset = $(".left").offset();
    documentHeight = $(document).height();console.log($(".left").height());
    $(window).scroll(function() {
        var sideBarHeight = $(".left").height();
        if ($(window).scrollTop() > offset.top) {

            var newPosition = ($(window).scrollTop() - offset.top) + topPadding;
            var maxPosition = documentHeight - (sideBarHeight - 1000);
            if (newPosition > maxPosition) {
                newPosition = maxPosition;
            }
            $(".left").stop().animate({
                marginTop: newPosition
            });
        } else {
            $(".left").stop().animate({
                marginTop: 0
            });
        }
    });
});