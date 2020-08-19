var expanded;

function expandNav() {
	if(!expanded) {
		$('.navPlaceholder').show();
		$('body').addClass('expanded');
		$('.nav').addClass('expanded');
		expanded = true;
	} else {
		$('.navPlaceholder').hide();
		$('body').removeClass('expanded');
		$('.nav').removeClass('expanded');
		expanded = false;
	}
}