let vh = window.innerHeight * 0.01;
document.documentElement.style.setProperty('--vh', `${vh}px`);


var expanded;

function expandNav() {
	if(!expanded) {
		$('.navPlaceholder').show();
		$('html').addClass('expanded');
		expanded = true;
	} else {
		$('.navPlaceholder').hide();
		$('html').removeClass('expanded');
		expanded = false;
	}
}